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
                    if (!change.getControlNewText().matches("[0-9.]*")) {
                        return null;
                    }

                    if(change.getControlNewText().length() == 2 || change.getControlNewText().length() == 5) {
                        change.setText(change.getText() + ".");
                        change.setCaretPosition(change.getControlNewText().length());
                        change.setAnchor(change.getControlNewText().length());
                        validate();
                    } else if (change.getControlNewText().length() > 10 ||
                            (change.getControlNewText().length() == 10 && !change.getControlNewText().matches("^([0-2][0-9]|[3][0-1]).([0][1-9]|[1][0-2]).[0-9]{4}$"))) {
                        return null;
                    }
                }
                return change;
            }
        }));
    }
}
