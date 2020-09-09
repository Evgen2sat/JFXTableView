package em.libs.jfxtableview.totalField;

import em.libs.jfxtableview.enums.TotalTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TotalFieldTableColumnView extends TotalFieldTableColumnViewDesigner {

    public TotalFieldTableColumnView(Set<TotalTypeEnum> totalTypes) {
        initTotalTypes(totalTypes);
    }

    public List<TotalTypeEnum> getSelectedTotalTypes() {
        List<TotalTypeEnum> result = new ArrayList<>();
        for (var item : controls.entrySet()) {
            if (item.getValue().isSelected()) {
                result.add(item.getKey());
            }
        }

        return result;
    }
}
