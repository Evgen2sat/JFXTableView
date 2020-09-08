package em.libs.jfxtableview.filterFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import em.libs.jfxtableview.FilteredJFXComboBox;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import em.libs.jfxtableview.JFXIconButton;
import em.libs.jfxtableview.font.FontAwesome;
import em.libs.jfxtableview.models.FilterModeModel;
import em.libs.jfxtableview.models.FilterTypeModel;
import em.libs.jfxtableview.validators.ExistErrorsChecker;
import em.libs.jfxtableview.validators.ItemInListValidator;
import em.libs.jfxtableview.validators.RequiredFilteredComboBoxValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public abstract class JFXSettingFilterViewDesigner<T> extends StackPane {

    private VBox vBoxMain;
    private int countItems = 0;
    protected ObservableList<T> items = FXCollections.observableArrayList();
    protected Map<HBox, FilterModel> filterMap = new HashMap<>();

    private ScrollPane scrollPane;
    private FilteredJFXComboBox<FilterModeModel> cmbFilterMode;
    private final FilterSettingModel viewModel;

    private final RequiredFilteredComboBoxValidator requiredFilterModeValidator = new RequiredFilteredComboBoxValidator(REQUIRED_FIELD_ERROR);
    protected final ExistErrorsChecker existErrorsChecker = new ExistErrorsChecker();

    public JFXSettingFilterViewDesigner(FilterSettingModel viewModel) {
        this.viewModel = viewModel;
        initScrollPane();
    }

    private void initScrollPane() {
        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxHeight(300);

        initVBoxMain();

        getChildren().add(scrollPane);
    }

    private void initVBoxMain() {
        vBoxMain = new VBox();
        vBoxMain.setPadding(new Insets(10, 0, 0, 0));

        initCmbFilterMode();
        initHboxItem();

        scrollPane.setContent(vBoxMain);
    }

    private void initCmbFilterMode() {
        cmbFilterMode = new FilteredJFXComboBox<>();
        cmbFilterMode.setLabelFloat(true);
        cmbFilterMode.setPromptText(FILTER_MODE);
        cmbFilterMode.setPrefWidth(400);
        cmbFilterMode.getValidators().add(requiredFilterModeValidator);
        cmbFilterMode.validate();
        cmbFilterMode.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            cmbFilterMode.validate();
        });
        cmbFilterMode.textProperty().addListener((observable, oldValue, newValue) -> cmbFilterMode.validate());
        VBox.setMargin(cmbFilterMode, new Insets(15,0,20,0));

        vBoxMain.getChildren().add(cmbFilterMode);
    }

    private void initHboxItem() {
        HBox hBoxItem = new HBox();
        hBoxItem.setSpacing(5);
        VBox.setMargin(hBoxItem, new Insets(15,0,20,0));

        FilterModel filter = new FilterModel();
        initFcbFilterType(hBoxItem, filter);
        hBoxItem.getChildren().add(initFilterValueControl(filter));
        countItems++;
        filterMap.put(hBoxItem, filter);

        if(countItems == 1) {
            initBtnAddItem(hBoxItem);
        } else {
            initBtnDeleteItem(hBoxItem);
        }

        vBoxMain.getChildren().add(hBoxItem);
    }

    private void initFcbFilterType(HBox hBoxItem, FilterModel filter) {
        RequiredFilteredComboBoxValidator requiredItemValidator = new RequiredFilteredComboBoxValidator(REQUIRED_FIELD_ERROR);
        ItemInListValidator<FilterTypeModel> filterTypeItemInListValidator = new ItemInListValidator<>(viewModel.getFilterTypes(), ITEM_NOT_CONTAINS_IN_LIST);
        existErrorsChecker.addValidators(requiredItemValidator, filterTypeItemInListValidator);

        FilteredJFXComboBoxWithClear<FilterTypeModel> fcbFilterType = new FilteredJFXComboBoxWithClear<>();
        fcbFilterType.setPrefWidth(200);
        fcbFilterType.setPromptText(COMPARISON_OPTION);
        fcbFilterType.setObservableList(viewModel.getFilterTypes());
        fcbFilterType.getValidators().addAll(requiredItemValidator, filterTypeItemInListValidator);
        fcbFilterType.validate();
        fcbFilterType.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            fcbFilterType.validate();
        });
        fcbFilterType.textProperty().addListener((observable, oldValue, newValue) -> fcbFilterType.validate());
        fcbFilterType.selectedValueProperty().bindBidirectional(filter.typeProperty());

        hBoxItem.getChildren().add(fcbFilterType);
    }

    private void initBtnAddItem(HBox hBoxItem) {
        JFXButton btnAddItem = new JFXIconButton(ADD_ICON, new FontAwesome().getFontSolid(), ADD_ITEM_FILTERING);
        btnAddItem.getStyleClass().add("jfx-without-radius-button");
        btnAddItem.setOnAction(event -> {
            initHboxItem();
        });

        hBoxItem.getChildren().add(btnAddItem);
    }

    private void initBtnDeleteItem(HBox hBoxItem) {
        JFXButton btnDeleteItem = new JFXIconButton(DELETE_ICON, new FontAwesome().getFontSolid(), DELETE_ITEM_FILTERING);
        btnDeleteItem.getStyleClass().add("jfx-without-radius-button");
        btnDeleteItem.setOnAction(event -> {
            FilteredJFXComboBoxWithClear<FilterTypeModel> cmbType = (FilteredJFXComboBoxWithClear<FilterTypeModel>) hBoxItem.getChildren().get(0);
            filterMap.remove(hBoxItem);
            cmbType.getValidators().forEach(validator -> existErrorsChecker.removeValidators((ValidatorBase) validator));

            removeValueControlValidators(hBoxItem.getChildren().get(1));
            vBoxMain.getChildren().remove(hBoxItem);
            countItems--;
        });

        hBoxItem.getChildren().add(btnDeleteItem);
    }

    protected void setItems(Set<ObservableValue<T>> items) {
        this.items.clear();

        if(items != null && !items.isEmpty()) {
             this.items.addAll(items.stream()
                     .filter(item -> item != null)
                     .filter(item -> item.getValue() != null)
                     .map(ObservableValue::getValue)
                     .distinct()
                     .collect(Collectors.toList()));
        }
    }

    public void bindFields() {
        ItemInListValidator<FilterModeModel> filterModeItemInListValidator = new ItemInListValidator<>(viewModel.getFilterModes(), ITEM_NOT_CONTAINS_IN_LIST);

        cmbFilterMode.setObservableList(viewModel.getFilterModes());
        cmbFilterMode.selectedValueProperty().bindBidirectional(viewModel.selectedFilterModeProperty());
        cmbFilterMode.getValidators().add(filterModeItemInListValidator);

        existErrorsChecker.addValidators(requiredFilterModeValidator, filterModeItemInListValidator);
    }

    protected abstract Node initFilterValueControl(FilterModel filter);

    protected abstract void removeValueControlValidators(Node filterControl);
}
