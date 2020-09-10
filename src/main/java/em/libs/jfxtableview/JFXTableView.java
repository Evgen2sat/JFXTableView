package em.libs.jfxtableview;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.Collectors;

public class JFXTableView<T> extends TableView<T> implements ObservableOnSubscribe<String> {
    private StackPane background = null;
    private boolean allowFiltering = true;
    private ObservableEmitter<String> stringEmitter;
    private final Observable<String> stringObservable;
    private JFXIconButton btnExportToCSV;
    private Label lblCountRows;

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
        lblCountRows.setText(Messages.getString("AMOUNT") + ": " + filteredList.size());
    };

    public JFXTableView(StackPane background) {
        super();
        stringObservable = Observable.create(this);
        init(background);
    }

    private void init(StackPane background) {
        this.background = background;
        initActionColumns();

        WeakListChangeListener<? super TableColumn<T, ?>> tableColumnWeakListChangeListener = new WeakListChangeListener<>(tableColumnListChangeListener);
        this.getColumns().addListener(tableColumnWeakListChangeListener);
    }

    private void initActionColumns() {
        TableColumn<T, Integer> clmnExportToCSV = new TableColumn<>();
        clmnExportToCSV.setMinWidth(50);
        clmnExportToCSV.setPrefWidth(50);
        clmnExportToCSV.setCellFactory(new Callback<TableColumn<T, Integer>, TableCell<T, Integer>>() {
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

        btnExportToCSV = new JFXIconButton("\uF56D", new FontAwesome().getFontSolid(), Messages.getString("EXPORT_DATA"));
        btnExportToCSV.getStyleClass().add("jfx-without-radius-button");
        btnExportToCSV.setOnAction(event -> {
            try {
                ExportToCSV.writeExcel(getDataForExport());
            } catch (Exception e) {
                stringEmitter.onNext(Messages.getString("EXPORT_TO_CSV_ERROR") + ": " + e.getMessage());
            }
        });
        vBox.getChildren().add(btnExportToCSV);

        lblCountRows = new Label();

        WeakListChangeListener<? super T> filteredListWeakChangeListener = new WeakListChangeListener<>(filteredListChangeListener);
        filteredList.addListener(filteredListWeakChangeListener);

        vBox.getChildren().add(lblCountRows);

        clmnExportToCSV.setGraphic(vBox);

        this.getColumns().add(0, clmnExportToCSV);
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
        btnExportToCSV.setOnAction(action);
    }
}
