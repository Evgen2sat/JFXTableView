package em.libs.jfxtableview.columns;

import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.enums.TotalTypeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.filterFields.filterString.JFXFilterFieldStringViewImpl;
import em.libs.jfxtableview.models.FilterTypeModel;

import java.util.*;

import static em.libs.jfxtableview.Constants.*;

public class JFXStringTableColumn<S> extends JFXTableColumn<S, String> {

    private JFXFilterFieldView filterFieldView;
    private List<FilterTypeModel> filterTypes;

    public JFXStringTableColumn() {
        this(null);
    }

    public JFXStringTableColumn(String text) {
        super(text);
    }

    private void init() {
        filterTypes = new ArrayList<>();
        filterTypes.add(new FilterTypeModel(Messages.getString("EQUALS"), FilterTypeEnum.EQUALS, EQUALS_ICON, Messages.getString("EQUALS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("NOT_EQUALS"), FilterTypeEnum.NOT_EQUALS, NOT_EQUALS_ICON, Messages.getString("NOT_EQUALS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("START_WITH"), FilterTypeEnum.START_WITH, START_WITH_ICON, Messages.getString("START_WITH")));
        filterTypes.add(new FilterTypeModel(Messages.getString("END_WITH"), FilterTypeEnum.END_WITH, END_WITH_ICON, Messages.getString("END_WITH")));
        filterTypes.add(new FilterTypeModel(Messages.getString("CONTAINS"), FilterTypeEnum.CONTAINS, CONTAINS_ICON, Messages.getString("CONTAINS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("NOT_CONTAINS"), FilterTypeEnum.NOT_CONTAINS, NOT_CONTAINS_ICON, Messages.getString("NOT_CONTAINS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("REGULAR_EXPRESSION"), FilterTypeEnum.REGULAR_EXPRESSION, REGULAR_EXPRESSION_ICON, Messages.getString("REGULAR_EXPRESSION")));
        filterTypes.add(new FilterTypeModel(Messages.getString("SETTING_FILTERING"), FilterTypeEnum.SETTING_FILTERING, SETTING_FILTERING_ICON, Messages.getString("SETTING_FILTERING")));
    }

    @Override
    public JFXFilterFieldView getFilterFieldView() {
        if (filterFieldView == null) {
            init();
            filterFieldView = new JFXFilterFieldStringViewImpl<>(this);
        }

        return filterFieldView;
    }

    @Override
    public List<FilterTypeModel> getFilterTypes() {
        return filterTypes;
    }

    @Override
    public Map<ObservableValue<String>, S> getValues() {
        Map<ObservableValue<String>, S> values = new HashMap<>();

        ((JFXTableView<S>) getTableView()).getBackingList().forEach(item -> {
            if(getCellData(item) instanceof String) {
                values.put(getCellObservableValue(item), item);
            } else {
                SimpleStringProperty simpleStringProperty = new SimpleStringProperty(((Object)getCellData(item)).toString());
                values.put(simpleStringProperty, item);
            }
        });

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
