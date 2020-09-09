package em.libs.jfxtableview.filterFields.filterString;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import em.libs.jfxtableview.validators.RequiredFilteredComboBoxValidator;
import javafx.scene.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JFXSettingFilterStringViewDesigner extends JFXSettingFilterViewDesigner<String> {
    private Map<FilteredJFXComboBoxWithClear<String>, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterStringViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFilteredComboBoxValidator requiredFcbItemValidator = new RequiredFilteredComboBoxValidator(Messages.getString("REQUIRED_FIELD_ERROR"));
        existErrorsChecker.addValidators(requiredFcbItemValidator);

        FilteredJFXComboBoxWithClear<String> fcbItem = new FilteredJFXComboBoxWithClear<>();
        fcbItem.setPrefWidth(200);
        fcbItem.setMaxWidth(200);
        fcbItem.setPromptText(Messages.getString("VALUE"));
        fcbItem.setObservableList(items);
        fcbItem.getValidators().add(requiredFcbItemValidator);
        fcbItem.validate();
        fcbItem.selectedValueProperty().addListener((observable, oldValue, newValue) -> {
            fcbItem.validate();
        });
        fcbItem.textProperty().addListener((observable, oldValue, newValue) -> fcbItem.validate());
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
