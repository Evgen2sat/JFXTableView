package em.libs.jfxtableview.jfxSimpleDialogBox;

import com.jfoenix.controls.JFXButton;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.jfxDialogBox.JFXDialogBox;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class JFXSimpleDialogBox extends JFXDialogBox {

    protected final JFXButton btnOk = new JFXButton(Messages.getString("OK"));

    protected JFXSimpleDialogBox() {
        super();
        List<Button> actions = new ArrayList<>();
        actions.add(btnOk);
        btnOk.getStyleClass().add("jfx-without-radius-button");
        setActions(actions);
        btnOk.setOnAction(e -> {
            ok();
        });

        if (!btnOk.isFocused()) {
            btnOk.requestFocus();
        }
    }

    protected void ok() {
        close();
    }

    public JFXSimpleDialogBox(Node body) {
        this();
        setBody(body);
    }

    public void setButtonText(String text) {
        btnOk.setText(text.toUpperCase());
    }
}
