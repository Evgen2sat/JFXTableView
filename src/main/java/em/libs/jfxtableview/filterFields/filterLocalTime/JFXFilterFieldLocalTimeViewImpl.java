package em.libs.jfxtableview.filterFields.filterLocalTime;

import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.columns.JFXLocalTimeTableColumn;
import em.libs.jfxtableview.enums.ClosingResult;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.jfxSimpleDialogBox.JFXSimpleDialogBox;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public class JFXFilterFieldLocalTimeViewImpl<T> extends JFXFilterFieldLocalTimeViewDesigner {

    private JFXLocalTimeTableColumn<T> column;
    private FilterTypeEnum currentFilterType = FilterTypeEnum.EQUALS;
    private JFXSimpleDialogBox dialogBox;
    private JFXSettingFilterLocalTimeViewImpl settingFilterView;

    public JFXFilterFieldLocalTimeViewImpl(JFXLocalTimeTableColumn<T> column) {
        super(column.getFilterTypes());
        this.column = column;
    }

    @Override
    public void updateFilterField() {
        if(currentFilterType == FilterTypeEnum.SETTING_FILTERING) {
            if(!settingFilterView.checkErrors()) {
                applyChangeFilterType(SETTING_FILTERING_ICON, SETTING_FILTERING, FilterTypeEnum.SETTING_FILTERING,
                        settingFilterView.getFilterMode(), settingFilterView.getFilteringValues());
            }

            return;
        }

        LocalTime value = tpSearchField.getValue();
        tpSearchField.setValue(null);
        tpSearchField.setValue(value);
    }

    @Override
    protected void dpSearchFieldChangeListener(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
        filterItems(newValue, currentFilterType, null, null);
    }

    @Override
    protected void btnSettingsFilter_onAction(ActionEvent event) {
        popupChangeFilterType.show(btnChangeFilterType);
    }

    @Override
    protected void setFilterType(LocalTime value, FilterTypeEnum type, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        currentFilterType = type;
        filterItems(value, currentFilterType, filterMode, filterValues);
    }

    private void filterItems(LocalTime filterValue, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        clearError();
        Set<T> collect = column.getValues().entrySet().stream().filter(observableValueTEntry -> {
            return observableValueTEntry.getKey() == null ||
                    observableValueTEntry.getKey().getValue() == null ||
                    applyFilter(observableValueTEntry.getKey().getValue(), filterValue, filterType, filterMode, filterValues);
        }).map(Map.Entry::getValue).collect(Collectors.toSet());

        ((JFXTableView<T>) (column.getTableView())).setFilteredItem(collect, column, this);
    }

    private boolean applyFilter(LocalTime item, LocalTime filterValue, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        if ((filterType != FilterTypeEnum.SETTING_FILTERING && filterValue == null)
                || (filterType == FilterTypeEnum.SETTING_FILTERING && (filterValues == null || filterValues.isEmpty()))) {
            return true;
        }

        switch (filterType) {
            case EQUALS:
                return item.equals(filterValue);
            case NOT_EQUALS:
                return !item.equals(filterValue);
            case GREATHER_EQUALS_THAN:
                return item.compareTo(filterValue) >= 0;
            case GREATHER_THAN:
                return item.compareTo(filterValue) > 0;
            case LESS_EQUALS_THAN:
                return item.compareTo(filterValue) <= 0;
            case LESS_THAN:
                return item.compareTo(filterValue) < 0;
            case SETTING_FILTERING:
                return applySettingFilter(item, filterMode, filterValues);
            default:
                return true;
        }
    }

    private boolean applySettingFilter(LocalTime item, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        switch (filterMode) {
            case ALL:
                return applySettingAllFilter(item, filterValues);
            case ANY:
                return applySettingAnyFilter(item, filterValues);
        }

        return true;
    }

    private boolean applySettingAllFilter(LocalTime item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {
            if (!applyFilter(item, (LocalTime)filter.getValue(), filter.getType().getType(), null, filterValues)) {
                return false;
            }
        }

        return true;
    }

    private boolean applySettingAnyFilter(LocalTime item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {
            if (applyFilter(item, (LocalTime)filter.getValue(), filter.getType().getType(), null, filterValues)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void btnCustom_onAction(ActionEvent event) {
        //открыть контрол настройки фильтрации
        if (dialogBox == null) {
            settingFilterView = new JFXSettingFilterLocalTimeViewImpl(new FilterSettingModel(column.getFilterTypes()));
            dialogBox = new JFXSimpleDialogBox(settingFilterView);
        }

        settingFilterView.setItems(new HashSet<>(column.getValues().keySet()));

        dialogBox.setOnClosing(closingEvent -> {
            if (closingEvent.getResult() == ClosingResult.OK) {
                if (settingFilterView.checkErrors()) {
                    closingEvent.setCancel(true);
                    return;
                }

                applyChangeFilterType(SETTING_FILTERING_ICON, SETTING_FILTERING, FilterTypeEnum.SETTING_FILTERING,
                        settingFilterView.getFilterMode(), settingFilterView.getFilteringValues());
            }
        });

        dialogBox.show(((JFXTableView<T>) (column.getTableView())).getBG(), 510, -1, SETTINGS_FILTERING + column.getColumnName());
    }
}
