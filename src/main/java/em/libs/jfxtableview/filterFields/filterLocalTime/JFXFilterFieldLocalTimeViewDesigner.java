package em.libs.jfxtableview.filterFields.filterLocalTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTimePicker;
import em.libs.jfxtableview.Constants;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.models.FilterModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import em.libs.jfxtableview.JFXIconButton;
import em.libs.jfxtableview.font.FontAwesome;
import em.libs.jfxtableview.models.FilterTypeModel;

import java.time.LocalTime;
import java.util.List;

import static em.libs.jfxtableview.Constants.*;

public abstract class JFXFilterFieldLocalTimeViewDesigner extends JFXFilterFieldView {

    private StackPane background;
    private HBox hBoxFilterPane;
    protected JFXTimePicker tpSearchField;
    private JFXButton btnClear;
    protected StackPane spSearchField;
    protected JFXButton btnChangeFilterType;
    protected JFXPopup popupChangeFilterType;
    private VBox vBoxFilterType;
    private Label lblError;

    public JFXFilterFieldLocalTimeViewDesigner(List<FilterTypeModel> filterTypes) {
        background = new StackPane();

        initPopupChangeFilterType(filterTypes);
        initHBoxFilterPane();

        spSearchField.setOnMouseEntered(event -> btnClear.setStyle("-fx-text-fill: #9D9D9D"));
        spSearchField.setOnMouseExited(event -> btnClear.setStyle("-fx-text-fill: TRANSPARENT"));

        this.getChildren().add(background);
    }

    private void initPopupChangeFilterType(List<FilterTypeModel> filterTypes) {
        popupChangeFilterType = new JFXPopup();

        initVBoxFilterType(filterTypes);
    }

    private void initVBoxFilterType(List<FilterTypeModel> filterTypes) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        vBoxFilterType = new VBox();
        vBoxFilterType.setSpacing(10);
        vBoxFilterType.setPrefHeight(200);
        vBoxFilterType.setPrefWidth(215);

        for (FilterTypeModel filterType : filterTypes) {
            JFXButton changeFilterTypeButton = createChangeFilterTypeButton(filterType.getName(), filterType.getIcon(), filterType.getDescription(), filterType.getType());
            vBoxFilterType.getChildren().add(changeFilterTypeButton);
        }

        scrollPane.setContent(vBoxFilterType);

        popupChangeFilterType.setPopupContent(scrollPane);
    }

    private JFXButton createChangeFilterTypeButton(String text, String icon, String textTooltip, FilterTypeEnum type) {
        JFXButton button = new JFXButton(text);
        button.setPrefWidth(200);
        button.setAlignment(Pos.CENTER_LEFT);
        button.getStyleClass().add("jfx-without-radius-button");
        button.setTooltip(new Tooltip(textTooltip));

        if (type == FilterTypeEnum.SETTING_FILTERING) {
            button.setOnAction(event -> {
                popupChangeFilterType.hide();
                btnCustom_onAction(event);
            });
        } else {
            button.setOnAction(event -> applyChangeFilterType(icon, text, type, null, null));
        }

        Label label = new Label(icon);
        label.setFont(new FontAwesome(12).getFontSolid());
        label.setStyle("-fx-text-fill: -primary-color");

        button.setGraphic(label);

        return button;
    }

    protected final void applyChangeFilterType(String icon, String tooltipText, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        if (filterType == FilterTypeEnum.SETTING_FILTERING) {
            tpSearchField.setValue(null);
            spSearchField.setDisable(true);
        } else {
            spSearchField.setDisable(false);
        }

        btnChangeFilterType.setText(icon);
        btnChangeFilterType.getTooltip().setText(tooltipText);
        setFilterType(tpSearchField.getValue(), filterType, filterMode, filterValues);
        popupChangeFilterType.hide();
    }

    private void initHBoxFilterPane() {
        hBoxFilterPane = new HBox();
        hBoxFilterPane.setAlignment(Pos.CENTER);

        initBtnChangeFilterType();
        initSpSearchField();

        background.getChildren().add(hBoxFilterPane);
    }

    private void initSpSearchField() {
        spSearchField = new StackPane();

        initDpSearchField();
        initBtnClear();

        hBoxFilterPane.getChildren().add(spSearchField);
    }

    private void initDpSearchField() {
        tpSearchField = new JFXTimePicker();
        tpSearchField.set24HourView(true);
        tpSearchField.setMinWidth(10);
        tpSearchField.valueProperty().addListener(this::dpSearchFieldChangeListener);

        spSearchField.getChildren().add(tpSearchField);
    }

    private void initBtnClear() {
        btnClear = new JFXIconButton(CLOSE_ICON, new FontAwesome(12).getFontSolid(), Messages.getString("CLEAR_FILTER"));
        btnClear.getStyleClass().add("jfx-without-radius-button");
        btnClear.setStyle("-fx-text-fill: TRANSPARENT");
        StackPane.setAlignment(btnClear, Pos.CENTER_RIGHT);
        StackPane.setMargin(btnClear, new Insets(0, 36, 0, 0));
        btnClear.setOnAction(event -> tpSearchField.setValue(null));

        spSearchField.getChildren().add(btnClear);
    }

    private void initBtnChangeFilterType() {
        btnChangeFilterType = new JFXIconButton("\uF52C", new FontAwesome(12).getFontSolid(), Messages.getString("EQUALS"));
        btnChangeFilterType.getStyleClass().add("jfx-without-radius-button");
        btnChangeFilterType.setOnAction(this::btnSettingsFilter_onAction);
        HBox.setMargin(btnChangeFilterType, new Insets(3));

        hBoxFilterPane.getChildren().add(btnChangeFilterType);
    }

    private void initLblError() {
        lblError = new Label(ERROR_ICON);
        HBox.setMargin(lblError, new Insets(5));
        lblError.setFont(new FontAwesome(12).getFontSolid());
        lblError.setStyle("-fx-text-fill: RED");
        lblError.setTooltip(new Tooltip());
    }

    protected void setError(String errorText) {
        if(lblError == null) {
            initLblError();
        }

        lblError.getTooltip().setText(errorText);
        if(!hBoxFilterPane.getChildren().get(hBoxFilterPane.getChildren().size() - 1).equals(lblError)) {
            hBoxFilterPane.getChildren().add(lblError);
        }
    }

    protected void clearError() {
        if (lblError != null) {
            lblError.getTooltip().setText(null);
            hBoxFilterPane.getChildren().remove(lblError);
        }
    }

    protected abstract void btnSettingsFilter_onAction(ActionEvent event);

    protected abstract void dpSearchFieldChangeListener(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue);

    protected abstract void setFilterType(LocalTime value, FilterTypeEnum type, FilterModeEnum filterMode, List<FilterModel> filterValues);

    protected abstract void btnCustom_onAction(ActionEvent event);
}
