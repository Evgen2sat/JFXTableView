package em.libs.jfxtableview.filterFields.filterLong;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.scene.Node;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import em.libs.jfxtableview.filterFields.commands.ConvertToValidLongFilterCommand;
import em.libs.jfxtableview.validators.LongRangeValidator;
import em.libs.jfxtableview.validators.RequiredFilteredComboBoxValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static em.libs.jfxtableview.Constants.*;

public abstract class JFXSettingFilterLongViewDesigner extends JFXSettingFilterViewDesigner<Long> {
    private Map<FilteredJFXComboBoxWithClear<Long>, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterLongViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFilteredComboBoxValidator requiredFcbItemValidator = new RequiredFilteredComboBoxValidator(REQUIRED_FIELD_ERROR);
        LongRangeValidator valueLongRangeValidator = new LongRangeValidator(LONG_VALUE_FROM_TO_ERROR);
        existErrorsChecker.addValidators(requiredFcbItemValidator, valueLongRangeValidator);

        FilteredJFXComboBoxWithClear<Long> fcbItem = new FilteredJFXComboBoxWithClear<>();
        fcbItem.setPrefWidth(200);
        fcbItem.setMaxWidth(200);
        fcbItem.setPromptText(VALUE);
        fcbItem.setObservableList(items);
        fcbItem.getValidators().addAll(requiredFcbItemValidator, valueLongRangeValidator);
        fcbItem.validate();
        fcbItem.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            fcbItem.validate();
        });
        fcbItem.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()) {
                String resultText = new ConvertToValidLongFilterCommand(newValue).execute();
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

        validatorsMap.put(fcbItem, Arrays.asList(requiredFcbItemValidator, valueLongRangeValidator));

        return fcbItem;
    }

    @Override
    protected void removeValueControlValidators(Node filterControl) {
        List<ValidatorBase> validators = validatorsMap.get(filterControl);
        validators.forEach(validator -> existErrorsChecker.removeValidators(validator));
        validatorsMap.remove(filterControl);
    }
}
