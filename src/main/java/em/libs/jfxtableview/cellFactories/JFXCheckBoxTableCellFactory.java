package em.libs.jfxtableview.cellFactories;

import em.libs.jfxtableview.columns.JFXTableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class JFXCheckBoxTableCellFactory<S, T> implements Callback<JFXTableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(JFXTableColumn<S, T> param) {
        return new CheckBoxTableCell<S, T>() {
            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
            }
        };
    }
}
