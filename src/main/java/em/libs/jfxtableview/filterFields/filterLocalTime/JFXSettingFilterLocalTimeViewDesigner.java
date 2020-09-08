package em.libs.jfxtableview.filterFields.filterLocalTime;

import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static em.libs.jfxtableview.Constants.REQUIRED_FIELD_ERROR;
import static em.libs.jfxtableview.Constants.VALUE;

public abstract class JFXSettingFilterLocalTimeViewDesigner extends JFXSettingFilterViewDesigner<LocalTime> {
    private Map<JFXTimePicker, List<ValidatorBase>> validatorsMap;

    public JFXSettingFilterLocalTimeViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        RequiredFieldValidator requiredItemValidator = new RequiredFieldValidator(REQUIRED_FIELD_ERROR);
        existErrorsChecker.addValidators(requiredItemValidator);

        JFXTimePicker dpItem = new JFXTimePicker();
        HBox.setMargin(dpItem, new Insets(5, 0, 0, 0));
        dpItem.set24HourView(true);
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
