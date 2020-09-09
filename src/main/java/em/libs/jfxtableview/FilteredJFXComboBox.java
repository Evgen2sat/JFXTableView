package em.libs.jfxtableview;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class FilteredJFXComboBox<T> extends JFXComboBox<T> {


    public interface AutoCompleteComparator<T> {
        boolean matches(String typedText, T objectToCompare);
    }


    @Override
    public boolean validate() {
        return super.validate();
    }

    private final ObjectProperty<T> selectedValueProperty = new SimpleObjectProperty<>();
    private ObservableList<T> data;

    public void setObservableList(ObservableList<T> data) {
        this.data = data;
    }

    public FilteredJFXComboBox() {
        this(
                (typedText, itemToCompare)
                        -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase())
        );
    }

    public FilteredJFXComboBox(ObservableList<T> observableList) {
        this(
                (typedText, itemToCompare)
                        -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase())
        );
        setObservableList(observableList);
    }

    public FilteredJFXComboBox(String promptText) {
        this(
                (typedText, itemToCompare)
                        -> itemToCompare.toString().toLowerCase().startsWith(typedText.toLowerCase())
        );
        setPromptText(promptText);
    }

    public FilteredJFXComboBox(AutoCompleteComparator<T> comparatorMethod, ObservableList<T> observableList) {
        this(comparatorMethod);
        setObservableList(observableList);
    }

    public FilteredJFXComboBox(AutoCompleteComparator<T> comparatorMethod) {
        data = FXCollections.observableArrayList();
        selectedValueProperty.addListener((observable, oldValue, newValue) -> {
            getSelectionModel().select(newValue);
        });
        setEditable(true);
        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                setValue(null);
            }
        });
        setOnHidden(event -> {
            if (getSelectionModel().getSelectedIndex() >= 0) {
                selectedValueProperty.setValue(getSelectionModel().getSelectedItem());
                getEditor().setText(getSelectionModel().getSelectedItem() != null ? getSelectionModel().getSelectedItem().toString() : getSelectedValue().toString());
            } else {
                setSelectedValue(null);
                //getEditor().setText(null);
            }
        });
        this.setOnShowing(event -> {
            T activeItem = getSelectedValue();
            if (activeItem != null) {
                setItems(data);
                getSelectionModel().select(activeItem);
                getEditor().selectAll();
            } else if (getEditor().getText() == null || getEditor().getText().equals("")) {
                this.setItems(data);
            }
        });
        //comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        this.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    if (getEditor().getText() != null) {
                        moveCaret(getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!isShowing()) {
                        show();
                    }
                    caretPos = -1;
                    if (getEditor().getText() != null) {
                        moveCaretToPos = true;
                        moveCaret(getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.DELETE) {
                    if (getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    /*if(getSelectionModel().getSelectedIndex() >= 0) {
                        selectedValueProperty.setValue(getSelectionModel().getSelectedItem());
                        getEditor().setText(getSelectionModel().getSelectedItem().toString());
                    }
                    else {
                        setSelectedValue(null);
                        getEditor().setText(null);
                    }*/
                    return;
                } else if (event.getCode() == KeyCode.SPACE) {
                    getEditor().setText(getEditor().getText().trim());
                    if (getEditor().getText() != null && getSelectedValue() == null) {
                        moveCaretToPos = true;
                        moveCaret(getEditor().getText().length());
                    }
                    return;
                }
                ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {
                    if (aData != null && getEditor().getText() != null && comparatorMethod.matches(getEditor().getText(), aData)) {
                        list.add(aData);
                    }
                }
                String t = "";
                if (getEditor().getText() != null) {
                    t = getEditor().getText();
                }
                setItems(list);
                if (!list.isEmpty()) {
                    if (!isShowing())
                        show();
                    //выбираем первую строку в списке по умолчанию
                    getSelectionModel().selectFirst();
                }
                getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    getEditor().positionCaret(textLength);
                } else {
                    getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public T getSelectedValue() {
        return selectedValueProperty.getValue();
    }

    public void setSelectedValue(T selectedValue) {
        setValue(selectedValue);
        selectedValueProperty.setValue(selectedValue);
    }

    public ObjectProperty<T> selectedValueProperty() {
        return selectedValueProperty;
    }

    public StringProperty textProperty() {
        return getEditor().textProperty();
    }

    public void setListViewPrefWidth(double prefWidth) {
        setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        getListView().setPrefWidth(prefWidth);
                        if (!empty) {
                            setText(item.toString());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }
}