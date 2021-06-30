package em.libs.jfxtableview.cellFactories;

import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.columns.JFXLocalDateTableColumn;
import em.libs.jfxtableview.columns.JFXLocalDateTimeTableColumn;
import em.libs.jfxtableview.columns.JFXTableColumn;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JFXDefaultTableCell<S, T> implements Callback<JFXTableColumn<S, T>, TableCell<S, T>> {

    public JFXDefaultTableCell() {
    }

    @Override
    public TableCell<S, T> call(JFXTableColumn<S, T> param) {

        final TableCell<S, T> cell = new TableCell<S, T>() {

            @Override
            protected void updateItem(T item, boolean empty) {

                super.updateItem(item, empty);

                setGraphic(null);
                setText(null);

                if (item instanceof Node) {
                    super.setGraphic((Node) item);
                } else if (item != null) {
                    final String resultText;
                    if (param instanceof JFXLocalDateTableColumn) {
                        resultText = LocalDate.parse(item.toString()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    } else if (param instanceof JFXLocalDateTimeTableColumn) {
                        resultText = LocalDateTime.parse(item.toString()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    } else {
                        resultText = item.toString();
                    }

                    super.setText(resultText);

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem copyMenu = new MenuItem(Messages.getString("COPY"));
                    contextMenu.getItems().add(copyMenu);
                    copyMenu.setOnAction((e) -> {
                        Toolkit.getDefaultToolkit()
                                .getSystemClipboard()
                                .setContents(
                                        new StringSelection(resultText),
                                        null
                                );
                    });

                    if (!resultText.isEmpty()) {
                        if (getTooltip() == null) {
                            Tooltip tooltip = new Tooltip(resultText);
                            tooltip.setMaxWidth(500);
                            tooltip.setWrapText(true);
                            setTooltip(tooltip);
                        } else {
                            getTooltip().setText(resultText);
                        }
                    }

                    super.setContextMenu(contextMenu);
                } else {
                    setTooltip(null);
                    super.setContextMenu(null);
                }
            }
        };

        return cell;
    }
}
