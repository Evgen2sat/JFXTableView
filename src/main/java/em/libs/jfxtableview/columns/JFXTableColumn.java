package em.libs.jfxtableview.columns;

import com.jfoenix.controls.JFXButton;
import em.libs.jfxtableview.JFXIconButton;
import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.cellFactories.JFXDefaultTableCell;
import em.libs.jfxtableview.enums.ClosingResult;
import em.libs.jfxtableview.enums.TotalTypeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.font.FontAwesome;
import em.libs.jfxtableview.jfxSimpleDialogBox.JFXSimpleDialogBox;
import em.libs.jfxtableview.models.FilterTypeModel;
import em.libs.jfxtableview.totalField.TotalFieldTableColumnView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static em.libs.jfxtableview.Constants.TOTAL_ICON;

public abstract class JFXTableColumn<S, T> extends TableColumn<S, T> {

    private VBox vBoxFilterField;
    private String columnName;
    private Label lblTotalText;
    private JFXSimpleDialogBox dialogBox;
    private TotalFieldTableColumnView totalFieldTableColumnView;
    private GridPane gpFilter;

    private final ListChangeListener<? super S> filteredListChangeListener = lc -> {
        setTotalText(getTotalText(totalFieldTableColumnView.getSelectedTotalTypes()));
    };

    public JFXTableColumn() {
        this(null);
    }

    public JFXTableColumn(String text) {
        super();
        init(text);
    }

    private void init(String text) {
        this.columnName = text;
        initCell();
        initFilterField(text);
    }

    private void initFilterField(String text) {
        vBoxFilterField = new VBox();
        vBoxFilterField.setSpacing(10);
        vBoxFilterField.setAlignment(Pos.BOTTOM_CENTER);

        Label lblColumnTitle = new Label(text);
        lblColumnTitle.getStyleClass().add("label-bold-14");
        StackPane.setAlignment(lblColumnTitle, Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(lblColumnTitle);
        vBoxFilterField.getChildren().add(0, stackPane);

        gpFilter = new GridPane();
        gpFilter.getColumnConstraints().add(new ColumnConstraints() {
            {
                setHgrow(Priority.ALWAYS);
            }
        });
        gpFilter.getColumnConstraints().add(new ColumnConstraints() {
            {
                setHgrow(Priority.NEVER);
            }
        });
        vBoxFilterField.getChildren().add(gpFilter);

        setGraphic(vBoxFilterField);
    }

    private void initCell() {
        setCellFactory(param -> new JFXDefaultTableCell<S, T>().call(this));
    }

    public final void setIsAllowFiltering(boolean value) {
        if (value) {
            JFXFilterFieldView filterFieldView = getFilterFieldView();

            if(!gpFilter.getChildren().contains(filterFieldView)) {
                GridPane.setMargin(filterFieldView, new Insets(0, 5, 3, 0));
                GridPane.setHgrow(filterFieldView, Priority.ALWAYS);
                gpFilter.add(filterFieldView, 0, 0);
            }
        }
    }

    public final void initTotalRow() {
        initBtnSetTotalOption();
        initLblTotalText();
    }

    private void initBtnSetTotalOption() {
        JFXButton btnSetTotalOption = new JFXIconButton(TOTAL_ICON, new FontAwesome(12).getFontSolid(), Messages.getString("SHOW_TOTAL_BY_COLUMN"));
        btnSetTotalOption.getStyleClass().add("jfx-without-radius-button");
        btnSetTotalOption.setOnAction(this::setOnClickSumButton);
        GridPane.setMargin(btnSetTotalOption, new Insets(0, 0, 3, 0));

        gpFilter.add(btnSetTotalOption, 1, 0);
    }

    private void setOnClickSumButton(ActionEvent event) {
        if (dialogBox == null) {
            totalFieldTableColumnView = new TotalFieldTableColumnView(getTotalTypes());
            dialogBox = new JFXSimpleDialogBox(totalFieldTableColumnView);

            WeakListChangeListener<? super S> filteredListWeakChangeListener = new WeakListChangeListener<>(filteredListChangeListener);
            ((JFXTableView<S>) getTableView()).getFilteredList().addListener(filteredListWeakChangeListener);
        }

        dialogBox.setOnClosing(closingEvent -> {
            if (closingEvent.getResult() == ClosingResult.OK) {
                setTotalText(getTotalText(totalFieldTableColumnView.getSelectedTotalTypes()));
            }
        });

        dialogBox.show(((JFXTableView) (getTableView())).getBG(), 250, -1, Messages.getString("SELECTING_TOTAL"));
    }

    private void initLblTotalText() {
        lblTotalText = new Label();
        lblTotalText.setWrapText(true);
    }

    private void setTotalText(String value) {
        lblTotalText.setText(value);

        if (vBoxFilterField.getChildren().size() == 2 && value != null && !value.trim().isEmpty()) {
            vBoxFilterField.getChildren().add(lblTotalText);
        } else if (vBoxFilterField.getChildren().size() == 3 && value == null || (value != null && value.trim().isEmpty())) {
            vBoxFilterField.getChildren().remove(lblTotalText);
        }
    }

    public final String getColumnName() {
        return columnName;
    }

    public abstract JFXFilterFieldView getFilterFieldView();

    public abstract List<FilterTypeModel> getFilterTypes();

    public abstract Map<ObservableValue<T>, S> getValues();

    public abstract Set<TotalTypeEnum> getTotalTypes();

    public abstract String getTotalText(List<TotalTypeEnum> selectedTotalTypes);
}
