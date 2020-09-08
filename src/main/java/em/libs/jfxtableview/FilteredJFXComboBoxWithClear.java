package em.libs.jfxtableview;

import com.jfoenix.validation.base.ValidatorBase;
import em.libs.jfxtableview.font.FontAwesome;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class FilteredJFXComboBoxWithClear<T> extends BorderPane {

    private final Label lblClear = new Label(CLEAR_ICON);
    private FilteredJFXComboBox<T> editor;

    private static final String CLEAR_ICON = Constants.CLOSE_ICON;
    private static final String TOOLTIP_STYLE = "-fx-fonts-size: 13";

    public FilteredJFXComboBoxWithClear() {
        this (null, null);
    }

    public FilteredJFXComboBoxWithClear(FilteredJFXComboBox.AutoCompleteComparator<T> comparatorMethod, ObservableList<T> observableList){

        lblClear.setFont(new FontAwesome(14).getFontSolid());
        lblClear.setStyle("-fx-text-fill: TRANSPARENT");
        lblClear.setOnMouseClicked(this::lblClear_onMouseClicked);
        lblClear.setTooltip(new Tooltip(Constants.CLEAR_FIELD));
        lblClear.getTooltip().setStyle(TOOLTIP_STYLE);
        lblClear.setCursor(Cursor.HAND);

        StackPane.setAlignment(lblClear, Pos.CENTER_RIGHT);
        StackPane.setMargin(lblClear, new Insets(0,30,0,0));

        setOnMouseEntered(event -> lblClear.setStyle("-fx-text-fill: #9D9D9D"));
        setOnMouseExited(event -> lblClear.setStyle("-fx-text-fill: TRANSPARENT"));

        if (comparatorMethod != null && observableList != null) {
            editor = new FilteredJFXComboBox<T>(comparatorMethod, observableList);
        } else {
            editor = new FilteredJFXComboBox<>();
        }
        editor.setPrefWidth(Double.MAX_VALUE);
        setCenter(new StackPane(editor, lblClear));
    }

    public void setObservableList(ObservableList<T> data) {
        editor.setObservableList(data);
    }

    public void setLabelFloat(boolean value) {
        editor.setLabelFloat(value);
    }

    public void setPromptText(String text) {
        editor.setPromptText(text);
    }

    public boolean validate() {
        return editor.validate();
    }

    public ObservableList<ValidatorBase> getValidators() {
        return editor.getValidators();
    }

    public FilteredJFXComboBox<T> getEditor() {
        return editor;
    }

    public List<T> getItems() {
        return editor.getItems();
    }

    private void lblClear_onMouseClicked(MouseEvent event) {
        setSelectedValue(null);
    }

    public T getSelectedValue(){
        return editor.getSelectedValue();
    }
    public void setSelectedValue(T selectedValue) {
        editor.setSelectedValue(selectedValue);
    }
    public ObjectProperty<T> selectedValueProperty() {
        return editor.selectedValueProperty();
    }
    public StringProperty textProperty() {
        return editor.getEditor().textProperty();
    }
}