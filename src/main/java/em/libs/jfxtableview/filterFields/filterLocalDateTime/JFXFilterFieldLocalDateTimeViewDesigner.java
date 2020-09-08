package em.libs.jfxtableview.filterFields.filterLocalDateTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import em.libs.jfxtableview.JFXDateControl;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.models.FilterModel;
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
import em.libs.jfxtableview.JFXTimeControl;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.font.FontAwesome;
import em.libs.jfxtableview.models.FilterTypeModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static em.libs.jfxtableview.Constants.*;

public abstract class JFXFilterFieldLocalDateTimeViewDesigner extends JFXFilterFieldView {

    private StackPane background;
    private HBox hBoxFilterPane;
    protected JFXDateControl dpSearchField;
    protected JFXTimeControl tpSearchField;
    private JFXButton btnLocalDateClear;
    private JFXButton btnLocalTimeClear;
    protected HBox hBoxSearchField;
    protected JFXButton btnChangeFilterType;
    protected JFXPopup popupChangeFilterType;
    private VBox vBoxFilterType;
    private Label lblError;
    private StackPane spLocalDate;
    private StackPane spLocalTime;

    public JFXFilterFieldLocalDateTimeViewDesigner(List<FilterTypeModel> filterTypes) {
        background = new StackPane();

        initPopupChangeFilterType(filterTypes);
        initHBoxFilterPane();

        spLocalDate.setOnMouseEntered(event -> btnLocalDateClear.setStyle("-fx-text-fill: #9D9D9D"));
        spLocalDate.setOnMouseExited(event -> btnLocalDateClear.setStyle("-fx-text-fill: TRANSPARENT"));
        spLocalTime.setOnMouseEntered(event -> btnLocalTimeClear.setStyle("-fx-text-fill: #9D9D9D"));
        spLocalTime.setOnMouseExited(event -> btnLocalTimeClear.setStyle("-fx-text-fill: TRANSPARENT"));

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
        clearError();

        if (filterType == FilterTypeEnum.SETTING_FILTERING) {
            dpSearchField.setValue(null);
            hBoxSearchField.setDisable(true);
        } else {
            hBoxSearchField.setDisable(false);
        }

        btnChangeFilterType.setText(icon);
        btnChangeFilterType.getTooltip().setText(tooltipText);

        setFilterType(getLocalDateTime(dpSearchField.getValue(), tpSearchField.getValue()), filterType, filterMode, filterValues);
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
        hBoxSearchField = new HBox();

        initDpSearchField();
        initTpSearchField();

        hBoxFilterPane.getChildren().add(hBoxSearchField);
    }

    private void initDpSearchField() {
        spLocalDate = new StackPane();

        dpSearchField = new JFXDateControl();
        dpSearchField.setMinWidth(10);
        dpSearchField.valueProperty().addListener((observable, oldValue, newValue) -> {
            clearError();
            searchFieldChangeListener(getLocalDateTime(newValue, tpSearchField.getValue()));
        });

        spLocalDate.getChildren().add(dpSearchField);

        initBtnLocalDateClear(spLocalDate);

        hBoxSearchField.getChildren().add(spLocalDate);
    }

    private void initBtnLocalDateClear(StackPane spLocalDate) {
        btnLocalDateClear = new JFXIconButton(CLOSE_ICON, new FontAwesome(12).getFontSolid(), CLEAR_FILTER);
        btnLocalDateClear.getStyleClass().add("jfx-without-radius-button");
        btnLocalDateClear.setStyle("-fx-text-fill: TRANSPARENT");
        StackPane.setAlignment(btnLocalDateClear, Pos.CENTER_RIGHT);
        StackPane.setMargin(btnLocalDateClear, new Insets(0, 36, 0, 0));
        btnLocalDateClear.setOnAction(event -> dpSearchField.setValue(null));

        spLocalDate.getChildren().add(btnLocalDateClear);
    }

    private void initTpSearchField() {
        spLocalTime = new StackPane();

        tpSearchField = new JFXTimeControl();
        tpSearchField.set24HourView(true);
        tpSearchField.setMinWidth(10);
        tpSearchField.valueProperty().addListener((observable, oldValue, newValue) -> {
            clearError();
            searchFieldChangeListener(getLocalDateTime(dpSearchField.getValue(), newValue));
        });

        spLocalTime.getChildren().add(tpSearchField);

        initBtnLocalTimeClear(spLocalTime);

        hBoxSearchField.getChildren().add(spLocalTime);
    }

    private void initBtnLocalTimeClear(StackPane spLocalTime) {
        btnLocalTimeClear = new JFXIconButton(CLOSE_ICON, new FontAwesome(12).getFontSolid(), CLEAR_FILTER);
        btnLocalTimeClear.getStyleClass().add("jfx-without-radius-button");
        btnLocalTimeClear.setStyle("-fx-text-fill: TRANSPARENT");
        StackPane.setAlignment(btnLocalTimeClear, Pos.CENTER_RIGHT);
        StackPane.setMargin(btnLocalTimeClear, new Insets(0, 36, 0, 0));
        btnLocalTimeClear.setOnAction(event -> tpSearchField.setValue(null));

        spLocalTime.getChildren().add(btnLocalTimeClear);
    }

    private void initBtnChangeFilterType() {
        btnChangeFilterType = new JFXIconButton("\uF52C", new FontAwesome(12).getFontSolid(), EQUALS);
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

    private LocalDateTime getLocalDateTime(LocalDate date, LocalTime time) {
        if (date == null && time == null) {
            return null;
        }

        try {
            return LocalDateTime.of(date, time);
        } catch (Exception e) {
            setError(REQUIRED_DATE_AND_TIME);
        }

        return null;
    }

    protected abstract void btnSettingsFilter_onAction(ActionEvent event);

    protected abstract void searchFieldChangeListener(LocalDateTime newValue);

    protected abstract void setFilterType(LocalDateTime value, FilterTypeEnum type, FilterModeEnum filterMode, List<FilterModel> filterValues);

    protected abstract void btnCustom_onAction(ActionEvent event);
}
