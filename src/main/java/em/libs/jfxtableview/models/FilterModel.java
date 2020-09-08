package em.libs.jfxtableview.models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class FilterModel {
    private SimpleObjectProperty<FilterTypeModel> type = new SimpleObjectProperty<>();
    private SimpleStringProperty text = new SimpleStringProperty();
    private SimpleObjectProperty value = new SimpleObjectProperty();

    public FilterTypeModel getType() {
        return type.get();
    }

    public SimpleObjectProperty<FilterTypeModel> typeProperty() {
        return type;
    }

    public void setType(FilterTypeModel type) {
        this.type.set(type);
    }

    public String getText() {
        return text.get();
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Object getValue() {
        return value.get();
    }

    public SimpleObjectProperty valueProperty() {
        return value;
    }

    public void setValue(Object value) {
        this.value.set(value);
    }
}
