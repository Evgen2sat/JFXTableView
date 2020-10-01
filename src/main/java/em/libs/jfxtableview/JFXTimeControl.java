package em.libs.jfxtableview;

import com.jfoenix.controls.JFXTimePicker;
import javafx.scene.control.TextFormatter;

import java.time.LocalTime;
import java.util.function.UnaryOperator;

public class JFXTimeControl extends JFXTimePicker {

    public JFXTimeControl() {
        this(null);
    }

    public JFXTimeControl(LocalTime time) {
        super(time);
        getEditor().setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {

                if (change.isAdded()) {
                    resetValidation();

                    if (!change.getControlNewText().matches("[0-9:]*")) {
                        return null;
                    }

                    if(change.getControlNewText().length() == 2 && change.getControlNewText().matches("\\d*")) {
                        change.setText(change.getText() + ":");
                        change.setCaretPosition(change.getControlNewText().length());
                        change.setAnchor(change.getControlNewText().length());
                    } else if (change.getControlNewText().length() > 5 ||
                            (change.getControlNewText().length() == 5 && !change.getControlNewText().matches("([0-1][0-9]|[2][0-3]):([0-5][0-9])"))) {
                        return null;
                    }
                }
                return change;
            }
        }));
    }
}
