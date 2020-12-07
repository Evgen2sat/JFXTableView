package em.libs.jfxtableview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import em.libs.jfxtableview.columns.JFXTableColumn;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.font.FontAwesome;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.EXPORT_ICON;
import static em.libs.jfxtableview.Constants.MENU_ICON;

public class JFXTableView<T> extends TableView<T> implements ObservableOnSubscribe<String> {
    private StackPane background = null;
    private boolean allowFiltering = true;
    private boolean visibleNumberedRowsColumn = false;
    private ObservableEmitter<String> stringEmitter;
    private final Observable<String> stringObservable;
    private Label lblCountRows;
    private Label lblAmount;
    private JFXPopup popupActions;
    private TableColumn clmnActions;
    private VBox vBoxActions;
    private ScrollPane srlpPopup;
    private JFXButton btnExport;

    private ObservableList<T> data;
    private ObservableList<T> backingList = FXCollections.observableArrayList();
    private FilteredList<T> filteredList = new FilteredList<>(new SortedList<>(backingList));
    private SortedList<T> sortedControlList = new SortedList<>(this.filteredList);
    private Map<JFXTableColumn<T, ?>, Set<T>> filteredItemsByColumns = new HashMap<>();
    private Map<JFXTableColumn<T, ?>, JFXFilterFieldView> filteredFieldByColumns = new HashMap<>();

    private final ListChangeListener<? super T> itemsChangedListener = lc -> {
        initItems();
    };

    private final ListChangeListener<? super TableColumn<T, ?>> tableColumnListChangeListener = lc -> {
        while (lc.next()) {
            setAllowFilteringColumn(lc.getAddedSubList());
        }
    };

    private final ListChangeListener<? super T> filteredListChangeListener = lc -> {
        lblAmount.setText(Messages.getString("AMOUNT"));
        lblCountRows.setText(String.valueOf(filteredList.size()));
    };

    public JFXTableView(StackPane background) {
        super();
        stringObservable = Observable.create(this);
        init(background);
    }

    private void init(StackPane background) {
        this.background = background;

        initActionColumn();

        WeakListChangeListener<? super TableColumn<T, ?>> tableColumnWeakListChangeListener = new WeakListChangeListener<>(tableColumnListChangeListener);
        this.getColumns().addListener(tableColumnWeakListChangeListener);
    }

    private void initActionColumn() {
        clmnActions = new TableColumn();
        clmnActions.setMinWidth(30);
        clmnActions.setPrefWidth(30);
        clmnActions.setMaxWidth(30);
        clmnActions.setReorderable(false);
        clmnActions.setSortable(false);

        initPopupActions();
        initLblActions();

        this.getColumns().add(0, clmnActions);
    }

    private void initLblActions() {
        Label lblActions = new Label(MENU_ICON);
        lblActions.setFont(new FontAwesome().getFontSolid());
        lblActions.setTooltip(new Tooltip(Messages.getString("SHOW_ACTIONS")));
        lblActions.setStyle("-fx-text-fill: -primary-color;");
        lblActions.setCursor(Cursor.HAND);
        lblActions.setOnMouseClicked(event -> {
            popupActions.show(lblActions);
        });

        clmnActions.setGraphic(lblActions);
    }

    private void initPopupActions() {
        popupActions = new JFXPopup();

        srlpPopup();
    }

    private void srlpPopup() {
        srlpPopup = new ScrollPane();
        srlpPopup.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        initVboxActions();

        popupActions.setPopupContent(srlpPopup);
    }

    private void initVboxActions() {
        vBoxActions = new VBox();
        vBoxActions.setSpacing(10);
        vBoxActions.setMaxHeight(200);
        vBoxActions.setPrefWidth(200);

        JFXButton btnClearFilters = new JFXButton(Messages.getString("CLEAR_FILTERS"));
        btnClearFilters.setPrefWidth(200);
        btnClearFilters.setAlignment(Pos.CENTER_LEFT);
        btnClearFilters.getStyleClass().add("jfx-without-radius-button");
        btnClearFilters.setOnAction(event -> {
            popupActions.hide();
            for (var entry : filteredFieldByColumns.entrySet()) {
                entry.getValue().clearFilter();
            }
        });
        vBoxActions.getChildren().add(btnClearFilters);

        btnExport = new JFXButton(Messages.getString("EXPORT_DATA"));
        btnExport.setPrefWidth(200);
        btnExport.setAlignment(Pos.CENTER_LEFT);
        btnExport.getStyleClass().add("jfx-without-radius-button");
        btnExport.setOnAction(event -> {
            try {
                ExportToCSV.writeExcel(getDataForExport());
            } catch (Exception e) {
                stringEmitter.onNext(Messages.getString("EXPORT_TO_CSV_ERROR") + ": " + e.getMessage());
            }
        });
        vBoxActions.getChildren().add(btnExport);

        srlpPopup.setContent(vBoxActions);
    }

    private void initNumberedRowsColumn() {
        TableColumn<T, Integer> clmnNumberedRows = new TableColumn<>();
        clmnNumberedRows.setMinWidth(50);
        clmnNumberedRows.setPrefWidth(50);
        clmnNumberedRows.setCellFactory(new Callback<TableColumn<T, Integer>, TableCell<T, Integer>>() {
            @Override
            public TableCell<T, Integer> call(TableColumn<T, Integer> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            setText(String.valueOf(getTableRow().getIndex() + 1));
                            setStyle("-fx-background-color: #D0D0D0");
                        } else {
                            setText(null);
                            setStyle(null);
                        }
                    }
                };
            }
        });

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        lblCountRows = new Label();
        lblAmount = new Label();

        WeakListChangeListener<? super T> filteredListWeakChangeListener = new WeakListChangeListener<>(filteredListChangeListener);
        filteredList.addListener(filteredListWeakChangeListener);

        vBox.getChildren().add(lblAmount);
        vBox.getChildren().add(lblCountRows);

        clmnNumberedRows.setGraphic(vBox);

        this.getColumns().add(0, clmnNumberedRows);
    }

    public List<List<Object>> getDataForExport() {
        List<TableColumn<T, ?>> columns = new ArrayList<>();
        List<List<Object>> result = new ArrayList<>();
        getVisibleColumnsWithData(getVisibleLeafColumns(), columns);

        result.add(columns.stream().map(column -> (((JFXTableColumn) column).getColumnName())).collect(Collectors.toList()));

        for (T item : filteredList) {
            List<Object> row = new ArrayList<>();
            for (TableColumn<T, ?> column : columns) {
                row.add(column.getCellData(item));
            }
            result.add(row);
        }

        return result;
    }

    private void getVisibleColumnsWithData(ObservableList<TableColumn<T, ?>> columns, List<TableColumn<T, ?>> result) {
        for (TableColumn<T, ?> column : columns) {
            if (!column.getColumns().isEmpty()) {
                getVisibleColumnsWithData(column.getColumns(), result);
            }

            if (column instanceof JFXTableColumn) {
                result.add(column);
            }
        }
    }

    private void setAllowFilteringColumn(List<? extends TableColumn<T, ?>> columns) {
        columns.forEach(column -> {
            if (!column.getColumns().isEmpty()) {
                setAllowFilteringColumn(column.getColumns());
            }

            if (column instanceof JFXTableColumn) {
                ((JFXTableColumn) column).setIsAllowFiltering(allowFiltering);
            }
        });
    }

    public void initItems() {
        backingList.clear();
        backingList.addAll(data);

        filteredItemsByColumns.clear();

        filteredList.setPredicate(v -> true);

        sortedControlList.comparatorProperty().bind(this.comparatorProperty());

        for (var entry : filteredFieldByColumns.entrySet()) {
            entry.getValue().updateFilterField();
        }
    }

    /**
     * Returns the backing {@link ObservableList} originally provided to the constructor.
     *
     * @return ObservableList
     */
    public ObservableList<T> getBackingList() {
        return backingList;
    }

    public void setData(ObservableList<T> value) {
        this.data = value;

        initItems();

        this.setItems(sortedControlList);
        WeakListChangeListener<? super T> itemsChangedWeakListener = new WeakListChangeListener<>(itemsChangedListener);
        value.addListener(itemsChangedWeakListener);
    }

    public FilteredList<T> getFilteredList() {
        return filteredList;
    }

    public void setFilteredItem(Set<T> filteredItems, JFXTableColumn<T, ?> column, JFXFilterFieldView filterField) {
        if (!filteredItemsByColumns.containsKey(column)) {
            filteredItemsByColumns.put(column, filteredItems);
        } else {
            filteredItemsByColumns.get(column).clear();
            filteredItemsByColumns.get(column).addAll(filteredItems);
        }

        if (!filteredFieldByColumns.containsKey(column)) {
            filteredFieldByColumns.put(column, filterField);
        }

        Set<T> retainItems = new HashSet<>(getBackingList());
        for (var entry : filteredItemsByColumns.entrySet()) {
            retainItems.retainAll(entry.getValue());
        }
        getFilteredList().setPredicate(retainItems::contains);
    }

    public StackPane getBG() {
        return background;
    }

    public boolean isAllowFiltering() {
        return allowFiltering;
    }

    public void setAllowFiltering(boolean allowFiltering) {
        this.allowFiltering = allowFiltering;
    }

    public void onSubscribe(Observer<String> observer) {
        stringObservable.subscribe(observer);
    }

    @Override
    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        this.stringEmitter = emitter;
    }

    public void setExportDataAction(EventHandler<ActionEvent> action) {
        btnExport.setOnAction(action);
    }

    public void setVisibleNumberedRowsColumn() {
        if (!visibleNumberedRowsColumn) {
            initNumberedRowsColumn();
            visibleNumberedRowsColumn = true;
        }
    }
}
