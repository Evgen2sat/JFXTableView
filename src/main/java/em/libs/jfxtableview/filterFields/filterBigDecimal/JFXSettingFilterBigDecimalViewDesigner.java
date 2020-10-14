package em.libs.jfxtableview.filterFields.filterBigDecimal;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;
import em.libs.jfxtableview.filterFields.commands.ConvertToValidBigDecimalFilterCommand;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import em.libs.jfxtableview.validators.RequiredFilteredComboBoxValidator;
import javafx.scene.Node;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JFXSettingFilterBigDecimalViewDesigner extends JFXSettingFilterViewDesigner<BigDecimal> {
    private Map<FilteredJFXComboBoxWithClear<BigDecimal>, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterBigDecimalViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFilteredComboBoxValidator requiredFcbItemValidator = new RequiredFilteredComboBoxValidator(Messages.getString("REQUIRED_FIELD_ERROR"));
        existErrorsChecker.addValidators(requiredFcbItemValidator);

        FilteredJFXComboBoxWithClear<BigDecimal> fcbItem = new FilteredJFXComboBoxWithClear<>();
        fcbItem.setPrefWidth(200);
        fcbItem.setMaxWidth(200);
        fcbItem.setPromptText(Messages.getString("VALUE"));
        fcbItem.setObservableList(items);
        fcbItem.getValidators().addAll(requiredFcbItemValidator);
        fcbItem.validate();
        fcbItem.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            fcbItem.validate();
        });
        fcbItem.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String resultText = new ConvertToValidBigDecimalFilterCommand(newValue).execute();
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

        validatorsMap.put(fcbItem, Arrays.asList(requiredFcbItemValidator));

        return fcbItem;
    }

    @Override
    protected void removeValueControlValidators(Node filterControl) {
        List<ValidatorBase> validators = validatorsMap.get(filterControl);
        validators.forEach(validator -> existErrorsChecker.removeValidators(validator));
        validatorsMap.remove(filterControl);
    }
}
