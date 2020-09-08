package em.libs.jfxtableview;


import com.jfoenix.controls.JFXDatePicker;
import javafx.scene.control.TextFormatter;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

public class JFXDateControl extends JFXDatePicker {

    public JFXDateControl() {
        this(null);
    }

    public JFXDateControl(LocalDate date) {
        super(date);
        getEditor().setTextFormatter(new TextFormatter<>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if (change.isAdded()) {
                    resetValidation();
                    if (change.getControlNewText().matches("[0-9.]*")) {

                        //день не должен быть больше 31
                        if (change.getControlNewText().length() == 2) {
                            if (Integer.parseInt(change.getControlNewText().substring(0, 2)) > 31) {
                                return null;
                            }
                            change.setText(change.getText() + ".");
                            change.setCaretPosition(change.getControlNewText().length());
                            change.setAnchor(change.getControlNewText().length());
                        }

                        //месяц не может быть больше 12
                        if (change.getControlNewText().length() == 5) {
                            if (Integer.parseInt(change.getControlNewText().substring(3, 5)) > 12) {
                                return null;
                            }
                            change.setText(change.getText() + ".");
                            change.setCaretPosition(change.getControlNewText().length());
                            change.setAnchor(change.getControlNewText().length());
                        }
                        // ограничение на ввод не более 10 симоволов дд.мм.гггг
                        if (change.getControlNewText().length() > 10) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
                return change;
            }
        }));
    }
}
