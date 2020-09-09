package em.libs.jfxtableview.filterFields.filterBoolean;

import com.jfoenix.controls.JFXComboBox;
import em.libs.jfxtableview.enums.CheckBoxFilterEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public abstract class JFXFilterFieldBooleanViewDesigner extends JFXFilterFieldView {

    protected JFXComboBox<CheckBoxFilterEnum> chbFilter;

    public JFXFilterFieldBooleanViewDesigner() {
        initChbFilter();
    }

    private void initChbFilter() {
        chbFilter = new JFXComboBox<>();
        chbFilter.getSelectionModel().selectedItemProperty().addListener(this::chbFilter_onSelectedChanged);

        getChildren().add(chbFilter);
    }

    protected void setItems(ObservableList<CheckBoxFilterEnum> items) {
        chbFilter.setItems(items);
        chbFilter.getSelectionModel().select(CheckBoxFilterEnum.ALL);
    }

    protected abstract void chbFilter_onSelectedChanged(ObservableValue<? extends CheckBoxFilterEnum> observable, CheckBoxFilterEnum oldValue, CheckBoxFilterEnum newValue);
}
