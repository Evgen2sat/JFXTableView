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
                    if (change.getControlNewText().matches("[0-9:]*")) {
                        // 24 часа..
                        if (change.getControlNewText().length() == 2) {
                            if (Integer.parseInt(change.getControlNewText().substring(0, 2)) > 23) {
                                return null;
                            }
                            change.setText(change.getText() + ":");
                            change.setCaretPosition(change.getControlNewText().length());
                            change.setAnchor(change.getControlNewText().length());
                        }
                        // ограничение на ввод не более 5 симловов 12:00, больше ввести нельзя.
                        if (change.getControlNewText().length() > 5) {
                            return null;
                        }
                        //час.. с 3 начинаться не может
                        if (change.getControlNewText().length() == 1) {
                            if (Integer.parseInt(change.getControlNewText()) > 2) {
                                return null;
                            }
                        }
                        // минуты.. может начинаться max с 5
                        if (change.getControlNewText().length() == 4 && change.getControlNewText().indexOf(":") == 2) {
                            if (Integer.parseInt(change.getControlNewText().substring(3)) > 5) {
                                return null;
                            }
                        }
                    } else {
                        return null;
                    }
                }
                return change;
            }
        }));
    }

    private String getValidStringForLocalTime(String text) {
        if(text == null || text.trim().isEmpty()) {
            return null;
        }

        StringBuilder resultText = new StringBuilder();
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isDigit(chars[i])) {
                continue;
            }

            if(resultText.length() == 5) {
                break;
            }

            resultText.append(chars[i]);

            if(resultText.length() == 2) {
                if(Integer.valueOf(resultText.substring(0, 2)) > 24) {
                    resultText = resultText.replace(0, 2, "24");
                }
                resultText.append(":");
            } else if(resultText.length() == 5) {
                if(Integer.valueOf(resultText.substring(3, 5)) > 59) {
                    resultText = resultText.replace(3, 5, "59");
                }
            }
        }

        return resultText.toString();
    }
}
