package em.libs.jfxtableview.validators;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.FilteredJFXComboBoxWithClear;
import javafx.collections.ObservableList;
import em.libs.jfxtableview.FilteredJFXComboBox;

public class ItemInListValidator<T> extends ValidatorBase {

    protected ObservableList<T> items;

    public ItemInListValidator(ObservableList<T> items, String message) {
        super(message);
        this.items = items;
    }

    public ItemInListValidator(ObservableList<T> items) {
        this.items = items;
    }

    public ItemInListValidator(String message) {
        super(message);
    }

    public void setItems(ObservableList<T> items) {
        this.items = items;
    }

    @Override
    protected void eval() {

        T value;
        String text = null;

        if(srcControl.get() instanceof FilteredJFXComboBoxWithClear){
            FilteredJFXComboBoxWithClear comboField = (FilteredJFXComboBoxWithClear)srcControl.get();
            value = (T)comboField.getSelectedValue();
            text = comboField.getEditor().getEditor().getText();
        } else
        if(srcControl.get() instanceof FilteredJFXComboBox){
            FilteredJFXComboBox comboField = (FilteredJFXComboBox)srcControl.get();
            value = (T)comboField.getSelectedValue();
            text = comboField.getEditor().getText();
        } else if(srcControl.get() instanceof JFXTextField) {

            JFXTextField textField = (JFXTextField)srcControl.get();

            value = (T) textField.getText();
        }
        else {
            JFXComboBox comboField = (JFXComboBox) srcControl.get();
            value = (T)comboField.getValue();
        }

        if((value == null && (text == null || text.equals("")))
                || items.filtered(x -> x != null && x.equals(value)).size() > 0) {
            hasErrors.set(false);
        } else {
            hasErrors.set(true);
        }
    }
}
