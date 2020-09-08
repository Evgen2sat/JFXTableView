package em.libs.jfxtableview.columns;

import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.enums.TotalTypeEnum;
import em.libs.jfxtableview.filterFields.filterLocalTime.JFXFilterFieldLocalTimeViewImpl;
import javafx.beans.value.ObservableValue;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.models.FilterTypeModel;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public class JFXLocalTimeTableColumn<S> extends JFXTableColumn<S, LocalTime> {

    private JFXFilterFieldView filterFieldView;
    private List<FilterTypeModel> filterTypes;

    public JFXLocalTimeTableColumn() {
        this(null);
    }

    public JFXLocalTimeTableColumn(String text) {
        super(text);
        initTotalRow();
    }

    private void init() {
        filterTypes = new ArrayList<>();
        filterTypes.add(new FilterTypeModel(EQUALS, FilterTypeEnum.EQUALS, EQUALS_ICON, EQUALS));
        filterTypes.add(new FilterTypeModel(NOT_EQUALS, FilterTypeEnum.NOT_EQUALS, NOT_EQUALS_ICON, NOT_EQUALS));
        filterTypes.add(new FilterTypeModel(GREATHER_EQUALS_THAN, FilterTypeEnum.GREATHER_EQUALS_THAN, GREATHER_EQUALS_THAN_ICON, GREATHER_EQUALS_THAN));
        filterTypes.add(new FilterTypeModel(GREATHER_THAN, FilterTypeEnum.GREATHER_THAN, GREATHER_THAN_ICON, GREATHER_THAN));
        filterTypes.add(new FilterTypeModel(LESS_EQUALS_THAN, FilterTypeEnum.LESS_EQUALS_THAN, LESS_EQUALS_THAN_ICON, LESS_EQUALS_THAN));
        filterTypes.add(new FilterTypeModel(LESS_THAN, FilterTypeEnum.LESS_THAN, LESS_THAN_ICON, LESS_THAN));
        filterTypes.add(new FilterTypeModel(SETTING_FILTERING, FilterTypeEnum.SETTING_FILTERING, SETTING_FILTERING_ICON, SETTING_FILTERING));
    }

    @Override
    public JFXFilterFieldView getFilterFieldView() {
        if (filterFieldView == null) {
            init();
            filterFieldView = new JFXFilterFieldLocalTimeViewImpl<>(this);
        }

        return filterFieldView;
    }

    @Override
    public List<FilterTypeModel> getFilterTypes() {
        return filterTypes;
    }

    @Override
    public Map<ObservableValue<LocalTime>, S> getValues() {
        Map<ObservableValue<LocalTime>, S> values = new HashMap<>();

        ((JFXTableView<S>) getTableView()).getBackingList().forEach(item -> values.put(getCellObservableValue(item), item));

        return values;
    }

    @Override
    public Set<TotalTypeEnum> getTotalTypes() {
        return new HashSet<>(Arrays.asList(TotalTypeEnum.MINIMUM, TotalTypeEnum.MAXIMUM));
    }

    @Override
    public String getTotalText(List<TotalTypeEnum> selectedTotalTypes) {
        if(selectedTotalTypes == null || selectedTotalTypes.isEmpty()) {
            return null;
        }

        List<LocalTime> items = ((JFXTableView<S>) getTableView()).getFilteredList().stream().filter(item -> getCellData(item) != null).map(this::getCellData).collect(Collectors.toList());

        if(items.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for(TotalTypeEnum totalType : selectedTotalTypes) {
            switch (totalType) {
                case MINIMUM:
                    result
                            .append(MINIMUM)
                            .append(": ")
                            .append(Collections.min(items))
                            .append("; ");
                    break;
                case MAXIMUM:
                    result
                            .append(MAXIMUM)
                            .append(": ")
                            .append(Collections.max(items))
                            .append("; ");
                    break;
            }
        }

        return result.toString();
    }
}
