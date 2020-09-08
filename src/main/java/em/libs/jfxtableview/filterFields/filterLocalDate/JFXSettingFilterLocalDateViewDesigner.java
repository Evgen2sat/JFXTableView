package em.libs.jfxtableview.filterFields.filterLocalDate;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static em.libs.jfxtableview.Constants.REQUIRED_FIELD_ERROR;
import static em.libs.jfxtableview.Constants.VALUE;

public abstract class JFXSettingFilterLocalDateViewDesigner extends JFXSettingFilterViewDesigner<LocalDate> {
    private Map<JFXDatePicker, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterLocalDateViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFieldValidator requiredItemValidator = new RequiredFieldValidator(REQUIRED_FIELD_ERROR);
        existErrorsChecker.addValidators(requiredItemValidator);

        JFXDatePicker dpItem = new JFXDatePicker();
        HBox.setMargin(dpItem, new Insets(5,0,0,0));
        dpItem.setPrefWidth(200);
        dpItem.setMaxWidth(200);
        dpItem.setPromptText(VALUE);
        dpItem.getValidators().add(requiredItemValidator);
        dpItem.validate();
        dpItem.valueProperty().addListener((observable, oldValue, newValue) -> {
            dpItem.validate();
        });
        dpItem.valueProperty().bindBidirectional(filter.valueProperty());

        if (validatorsMap == null) {
            validatorsMap = new HashMap<>();
        }

        validatorsMap.put(dpItem, Arrays.asList(requiredItemValidator));

        return dpItem;
    }

    @Override
    protected void removeValueControlValidators(Node filterControl) {
        List<ValidatorBase> validators = validatorsMap.get(filterControl);
        validators.forEach(validator -> existErrorsChecker.removeValidators(validator));
        validatorsMap.remove(filterControl);
    }
}
