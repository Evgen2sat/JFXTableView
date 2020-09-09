package em.libs.jfxtableview.cellFactories;

import com.jfoenix.controls.JFXButton;
import em.libs.jfxtableview.JFXIconButton;
import em.libs.jfxtableview.font.FontAwesome;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class JFXButtonTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private final String iconCode;
    private Font font = new FontAwesome(18).getFontSolid();

    public JFXButtonTableCellFactory(String iconCode, Font font) {
        this.iconCode = iconCode;
        this.font = font;
    }

    public JFXButtonTableCellFactory(String iconCode) {
        this.iconCode = iconCode;
    }

    private EventHandler<ActionEvent> action;

    public void setAction(EventHandler<ActionEvent> action) {
        this.action = action;
    }

    public TableCell<S, T> call(TableColumn<S, T> param) {

        JFXButton btn = new JFXIconButton(iconCode, font, param.getText());
        btn.getStyleClass().add("jfx-without-radius-button");

        final TableCell<S, T> cell = new TableCell<S, T>() {

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        };

        if (action != null) {
            btn.setOnAction(event -> {
                param.getTableView().getSelectionModel().select(cell.getTableRow().getIndex());
                action.handle(event);
            });
        }

        return cell;
    }
}
