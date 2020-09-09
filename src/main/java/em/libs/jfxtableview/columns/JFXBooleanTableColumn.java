package em.libs.jfxtableview.columns;

import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.enums.TotalTypeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.filterFields.filterBoolean.JFXFilterFieldBooleanViewImpl;
import em.libs.jfxtableview.models.FilterTypeModel;
import javafx.beans.value.ObservableValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JFXBooleanTableColumn<S> extends JFXTableColumn<S, Boolean> {

    private JFXFilterFieldView filterFieldView;

    public JFXBooleanTableColumn() {
        super();
    }

    public JFXBooleanTableColumn(String text) {
        super(text);
    }

    @Override
    public JFXFilterFieldView getFilterFieldView() {
        if (filterFieldView == null) {
            filterFieldView = new JFXFilterFieldBooleanViewImpl<>(this);
        }

        return filterFieldView;
    }

    @Override
    public List<FilterTypeModel> getFilterTypes() {
        throw new UnsupportedOperationException("method not allowed");
    }

    @Override
    public Map<ObservableValue<Boolean>, S> getValues() {
        Map<ObservableValue<Boolean>, S> values = new HashMap<>();

        ((JFXTableView<S>) getTableView()).getBackingList().forEach(item -> values.put(getCellObservableValue(item), item));

        return values;
    }

    @Override
    public Set<TotalTypeEnum> getTotalTypes() {
        throw new UnsupportedOperationException("method is not allowed");
    }

    @Override
    public String getTotalText(List<TotalTypeEnum> selectedTotalTypes) {
        throw new UnsupportedOperationException("method is not allowed");
    }
}
