package em.libs.jfxtableview.validators;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.FilteredJFXComboBox;

public class RequiredFilteredComboBoxValidator extends ValidatorBase {

    public RequiredFilteredComboBoxValidator(String message) {
        super(message);
    }

    public RequiredFilteredComboBoxValidator() {
    }

    @Override
    protected void eval() {
        FilteredJFXComboBox comboField = (FilteredJFXComboBox) srcControl.get();
        Object value = comboField.getSelectedValue();
        String textValue = comboField.textProperty().get();

        if (value == null && (textValue == null || textValue.isEmpty())) {
            hasErrors.set(true);
        } else {
            hasErrors.set(false);
        }
    }
}
