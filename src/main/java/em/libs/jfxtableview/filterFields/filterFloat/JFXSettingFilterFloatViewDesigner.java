package em.libs.jfxtableview.filterFields.filterFloat;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;
import em.libs.jfxtableview.filterFields.commands.ConvertToValidFloatFilterCommand;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import em.libs.jfxtableview.validators.FloatRangeValidator;
import em.libs.jfxtableview.validators.RequiredFilteredComboBoxValidator;
import javafx.scene.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JFXSettingFilterFloatViewDesigner extends JFXSettingFilterViewDesigner<Float> {
    private Map<FilteredJFXComboBoxWithClear<Float>, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterFloatViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFilteredComboBoxValidator requiredFcbItemValidator = new RequiredFilteredComboBoxValidator(Messages.getString("REQUIRED_FIELD_ERROR"));
        FloatRangeValidator valueFloatRangeValidator = new FloatRangeValidator(Messages.getString("FLOAT_VALUE_FROM_TO_ERROR"));

        existErrorsChecker.addValidators(requiredFcbItemValidator, valueFloatRangeValidator);

        FilteredJFXComboBoxWithClear<Float> fcbItem = new FilteredJFXComboBoxWithClear<>();
        fcbItem.setPrefWidth(200);
        fcbItem.setMaxWidth(200);
        fcbItem.setPromptText(Messages.getString("VALUE"));
        fcbItem.setObservableList(items);
        fcbItem.getValidators().addAll(requiredFcbItemValidator, valueFloatRangeValidator);
        fcbItem.validate();
        fcbItem.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            fcbItem.validate();
        });
        fcbItem.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String resultText = new ConvertToValidFloatFilterCommand(newValue).execute();
                if (!resultText.equals(newValue)) {
                    fcbItem.textProperty().set(resultText);
                }
            }
            fcbItem.validate();
        });
        fcbItem.textProperty().bindBidirectional(filter.textProperty());

        if (validatorsMap == null) {
            validatorsMap = new HashMap<>();
        }

        validatorsMap.put(fcbItem, Arrays.asList(requiredFcbItemValidator, valueFloatRangeValidator));

        return fcbItem;
    }

    @Override
    protected void removeValueControlValidators(Node filterControl) {
        List<ValidatorBase> validators = validatorsMap.get(filterControl);
        validators.forEach(validator -> existErrorsChecker.removeValidators(validator));
        validatorsMap.remove(filterControl);
    }
}
