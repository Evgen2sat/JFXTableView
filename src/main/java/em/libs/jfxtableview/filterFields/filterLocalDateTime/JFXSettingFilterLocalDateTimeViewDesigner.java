package em.libs.jfxtableview.filterFields.filterLocalDateTime;

import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.JFXDateControl;
import em.libs.jfxtableview.JFXTimeControl;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.filterFields.JFXSettingFilterViewDesigner;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JFXSettingFilterLocalDateTimeViewDesigner extends JFXSettingFilterViewDesigner<LocalDateTime> {
    private Map<Node, List<ValidatorBase>> validatorsMap;
    private JFXDateControl dpItem;
    private JFXTimeControl tpItem;

    public JFXSettingFilterLocalDateTimeViewDesigner(FilterSettingModel viewModel) {
        super(viewModel);
    }

    @Override
    protected Node initFilterValueControl(FilterModel filter) {
        HBox hBoxFilterValueControl = new HBox();

        initDpItem(filter, hBoxFilterValueControl);
        initTpItem(filter, hBoxFilterValueControl);

        return hBoxFilterValueControl;
    }

    private void initDpItem(FilterModel filter, HBox hBoxFilterValueControl) {
        RequiredFieldValidator requiredItemValidator = new RequiredFieldValidator(Messages.getString("REQUIRED_FIELD_ERROR"));
        existErrorsChecker.addValidators(requiredItemValidator);

        dpItem = new JFXDateControl();
        HBox.setMargin(dpItem, new Insets(5, 0, 0, 0));
        dpItem.setPrefWidth(150);
        dpItem.setMaxWidth(150);
        dpItem.setPromptText(Messages.getString("DATE"));
        dpItem.getValidators().add(requiredItemValidator);
        dpItem.validate();
        dpItem.valueProperty().addListener((observable, oldValue, newValue) -> {
            dpItem.validate();
            filter.setValue(getLocalDateTime(newValue, tpItem.getValue()));
        });

        if (validatorsMap == null) {
            validatorsMap = new HashMap<>();
        }

        validatorsMap.put(dpItem, Arrays.asList(requiredItemValidator));

        hBoxFilterValueControl.getChildren().add(dpItem);
    }

    private void initTpItem(FilterModel filter, HBox hBoxFilterValueControl) {
        RequiredFieldValidator requiredItemValidator = new RequiredFieldValidator(Messages.getString("REQUIRED_FIELD_ERROR"));
        existErrorsChecker.addValidators(requiredItemValidator);

        tpItem = new JFXTimeControl();
        tpItem.set24HourView(true);
        HBox.setMargin(tpItem, new Insets(5, 0, 0, 0));
        tpItem.setPrefWidth(150);
        tpItem.setMaxWidth(150);
        tpItem.setPromptText(Messages.getString("TIME"));
        tpItem.getValidators().add(requiredItemValidator);
        tpItem.validate();
        tpItem.valueProperty().addListener((observable, oldValue, newValue) -> {
            tpItem.validate();
            filter.setValue(getLocalDateTime(dpItem.getValue(), newValue));
        });

        if (validatorsMap == null) {
            validatorsMap = new HashMap<>();
        }

        validatorsMap.put(tpItem, Arrays.asList(requiredItemValidator));

        hBoxFilterValueControl.getChildren().add(tpItem);
    }

    @Override
    protected void removeValueControlValidators(Node filterControl) {
        Node dp = ((HBox) filterControl).getChildren().get(0);
        List<ValidatorBase> validatorsDate = validatorsMap.get(dp);
        validatorsDate.forEach(validator -> existErrorsChecker.removeValidators(validator));

        Node tp = ((HBox) filterControl).getChildren().get(1);
        List<ValidatorBase> validatorsTime = validatorsMap.get(tp);
        validatorsTime.forEach(validator -> existErrorsChecker.removeValidators(validator));

        validatorsMap.remove(filterControl);
    }

    private LocalDateTime getLocalDateTime(LocalDate date, LocalTime time) {
        if (date == null && time == null) {
            return null;
        }

        try {
            return LocalDateTime.of(date, time);
        } catch (Exception e) {
            return null;
        }
    }
}
