package em.libs.jfxtableview;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public final class ExportToCSV {

    private ExportToCSV() {}

    static <T> String writeExcel(List<List<Object>> items) throws Exception {

        if (items == null || items.isEmpty()) {
            return null;
        }

        Writer writer = null;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv")
            );
            File saveFile = fileChooser.showSaveDialog(new Stage());

            if (saveFile != null) {
                File file = new File(saveFile.getAbsolutePath());
                writer = new BufferedWriter(new FileWriter(file));

                for(List<Object> item : items) {
                    writer.write(item.stream().map(i -> i != null ? i.toString() : null).collect(Collectors.joining(";")));
                    writer.write("\n");
                }

                return saveFile.getName();
            }
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }

        return null;
    }
}
