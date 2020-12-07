package em.libs.jfxtableview.columns;

import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.cellFactories.JFXDefaultTableCell;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.enums.TotalTypeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.filterFields.filterInteger.JFXFilterFieldIntegerViewImpl;
import em.libs.jfxtableview.models.FilterTypeModel;
import javafx.beans.value.ObservableValue;

import java.util.*;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public class JFXIntegerTableColumn<S> extends JFXTableColumn<S, Integer> {

    private JFXFilterFieldView filterFieldView;
    private List<FilterTypeModel> filterTypes;

    public JFXIntegerTableColumn() {
        this(null);
    }

    public JFXIntegerTableColumn(String text) {
        super(text, param -> new JFXDefaultTableCell<S, Integer>().call((JFXTableColumn<S, Integer>)param));
        initTotalRow();
    }

    private void init() {
        filterTypes = new ArrayList<>();
        filterTypes.add(new FilterTypeModel(Messages.getString("EQUALS"), FilterTypeEnum.EQUALS, EQUALS_ICON, Messages.getString("EQUALS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("NOT_EQUALS"), FilterTypeEnum.NOT_EQUALS, NOT_EQUALS_ICON, Messages.getString("NOT_EQUALS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("GREATHER_EQUALS_THAN"), FilterTypeEnum.GREATHER_EQUALS_THAN, GREATHER_EQUALS_THAN_ICON, Messages.getString("GREATHER_EQUALS_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("GREATHER_THAN"), FilterTypeEnum.GREATHER_THAN, GREATHER_THAN_ICON, Messages.getString("GREATHER_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("LESS_EQUALS_THAN"), FilterTypeEnum.LESS_EQUALS_THAN, LESS_EQUALS_THAN_ICON, Messages.getString("LESS_EQUALS_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("LESS_THAN"), FilterTypeEnum.LESS_THAN, LESS_THAN_ICON, Messages.getString("LESS_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("SETTING_FILTERING"), FilterTypeEnum.SETTING_FILTERING, SETTING_FILTERING_ICON, Messages.getString("SETTING_FILTERING")));
    }

    @Override
    public JFXFilterFieldView getFilterFieldView() {
        if (filterFieldView == null) {
            init();
            filterFieldView = new JFXFilterFieldIntegerViewImpl<>(this);
        }

        return filterFieldView;
    }

    @Override
    public List<FilterTypeModel> getFilterTypes() {
        return filterTypes;
    }

    @Override
    public Map<ObservableValue<Integer>, S> getValues() {
        Map<ObservableValue<Integer>, S> values = new HashMap<>();

        ((JFXTableView<S>) getTableView()).getBackingList().forEach(item -> values.put(getCellObservableValue(item), item));

        return values;
    }

    @Override
    public Set<TotalTypeEnum> getTotalTypes() {
        return new HashSet<>(Arrays.asList(TotalTypeEnum.AVERAGE, TotalTypeEnum.MINIMUM, TotalTypeEnum.MAXIMUM, TotalTypeEnum.SUM));
    }

    @Override
    public String getTotalText(List<TotalTypeEnum> selectedTotalTypes) {
        if (selectedTotalTypes == null || selectedTotalTypes.isEmpty()) {
            return null;
        }

        List<Integer> items = ((JFXTableView<S>) getTableView()).getFilteredList().stream().filter(item -> getCellData(item) != null).map(this::getCellData).collect(Collectors.toList());

        if (items.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (TotalTypeEnum totalType : selectedTotalTypes) {
            switch (totalType) {
                case AVERAGE:
                    result
                            .append(Messages.getString("AVERAGE"))
                            .append(": ")
                            .append(items.stream().mapToInt(Integer::intValue).average().orElse(0))
                            .append("; ");
                    break;
                case MINIMUM:
                    result
                            .append(Messages.getString("MINIMUM"))
                            .append(": ")
                            .append(Collections.min(items))
                            .append("; ");
                    break;
                case MAXIMUM:
                    result
                            .append(Messages.getString("MAXIMUM"))
                            .append(": ")
                            .append(Collections.max(items))
                            .append("; ");
                    break;
                case SUM:
                    result
                            .append(Messages.getString("SUM"))
                            .append(": ")
                            .append(items.stream().mapToInt(Integer::intValue).sum())
                            .append("; ");
                    break;
            }
        }

        return result.toString();
    }
}
