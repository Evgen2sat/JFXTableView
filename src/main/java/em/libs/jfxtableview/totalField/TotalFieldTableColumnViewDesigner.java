package em.libs.jfxtableview.totalField;

import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import em.libs.jfxtableview.enums.TotalTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TotalFieldTableColumnViewDesigner extends StackPane {

    private VBox vBox;
    Map<TotalTypeEnum, JFXCheckBox> controls = new HashMap<>();

    TotalFieldTableColumnViewDesigner() {
        initVbox();
    }

    private void initVbox() {
        vBox = new VBox();
        vBox.setSpacing(20);
        StackPane.setMargin(vBox, new Insets(10,0,0,0));

        getChildren().add(vBox);
    }

    void initTotalTypes(Set<TotalTypeEnum> totalTypes) {
        List<TotalTypeEnum> types = totalTypes.stream().sorted().collect(Collectors.toList());
        for(TotalTypeEnum totalType : types) {
            JFXCheckBox cbTotalType = new JFXCheckBox(totalType.getText());
            controls.put(totalType, cbTotalType);
            vBox.getChildren().add(cbTotalType);
        }
    }
}
