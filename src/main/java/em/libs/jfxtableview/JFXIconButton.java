package em.libs.jfxtableview;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class JFXIconButton extends JFXButton {

    public JFXIconButton(String icon, Font font, String tooltipText) {
        this.setText(icon);
        this.setFont(font);

        if (tooltipText != null) {
            this.setTooltip(new Tooltip(tooltipText));
        }
    }

    public void setIcon(String icon) {
        this.setText(icon);
    }

    public void setIcon(String icon, Font font) {
        this.setText(icon);
        this.setFont(font);
    }

    public void setIconFont(Font font) {
        this.setFont(font);
    }
}
