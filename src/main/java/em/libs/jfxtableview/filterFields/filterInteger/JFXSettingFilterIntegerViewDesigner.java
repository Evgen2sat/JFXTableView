package em.libs.jfxtableview.filterFields.filterInteger;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.Constants;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.scene.Node;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import em.libs.jfxtableview.filterFields.commands.ConvertToValidIntegerFilterCommand;
import em.libs.jfxtableview.validators.IntegerRangeValidator;
import em.libs.jfxtableview.validators.RequiredFilteredComboBoxValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JFXSettingFilterIntegerViewDesigner extends JFXSettingFilterViewDesigner<Integer> {
    private Map<FilteredJFXComboBoxWithClear<Integer>, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterIntegerViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFilteredComboBoxValidator requiredFcbItemValidator = new RequiredFilteredComboBoxValidator(Constants.REQUIRED_FIELD_ERROR);
        IntegerRangeValidator valueIntegerRangeValidator = new IntegerRangeValidator(Constants.INTEGER_VALUE_FROM_TO_ERROR);
        existErrorsChecker.addValidators(requiredFcbItemValidator, valueIntegerRangeValidator);

        FilteredJFXComboBoxWithClear<Integer> fcbItem = new FilteredJFXComboBoxWithClear<>();
        fcbItem.setPrefWidth(200);
        fcbItem.setMaxWidth(200);
        fcbItem.setPromptText(Constants.VALUE);
        fcbItem.setObservableList(items);
        fcbItem.getValidators().addAll(requiredFcbItemValidator, valueIntegerRangeValidator);
        fcbItem.validate();
        fcbItem.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            fcbItem.validate();
        });
        fcbItem.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()) {
                String resultText = new ConvertToValidIntegerFilterCommand(newValue).execute();
                if(!resultText.equals(newValue)) {
                    fcbItem.textProperty().set(resultText);
                }
            }
            fcbItem.validate();
        });
        fcbItem.textProperty().bindBidirectional(filter.textProperty());

        if(validatorsMap == null) {
            validatorsMap = new HashMap<>();
        }

        validatorsMap.put(fcbItem, Arrays.asList(requiredFcbItemValidator, valueIntegerRangeValidator));

        return fcbItem;
    }

    @Override
    protected void removeValueControlValidators(Node filterControl) {
        List<ValidatorBase> validators = validatorsMap.get(filterControl);
        validators.forEach(validator -> existErrorsChecker.removeValidators(validator));
        validatorsMap.remove(filterControl);
    }
}
