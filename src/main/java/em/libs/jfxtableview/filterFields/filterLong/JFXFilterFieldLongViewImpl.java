package em.libs.jfxtableview.filterFields.filterLong;

import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.columns.JFXLongTableColumn;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import em.libs.jfxtableview.Debouncer;
import em.libs.jfxtableview.enums.ClosingResult;
import em.libs.jfxtableview.filterFields.commands.ConvertToValidLongFilterCommand;
import em.libs.jfxtableview.jfxSimpleDialogBox.JFXSimpleDialogBox;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public class JFXFilterFieldLongViewImpl<T> extends JFXFilterFieldLongViewDesigner {

    private JFXLongTableColumn<T> column;
    private FilterTypeEnum currentFilterType = FilterTypeEnum.EQUALS;
    private JFXSimpleDialogBox dialogBox;
    private JFXSettingFilterLongViewImpl settingFilterView;

    public JFXFilterFieldLongViewImpl(JFXLongTableColumn<T> column) {
        super(column.getFilterTypes());
        this.column = column;
    }

    @Override
    public void updateFilterField() {
        if(currentFilterType == FilterTypeEnum.SETTING_FILTERING) {
            if(!settingFilterView.checkErrors()) {
                applyChangeFilterType(SETTING_FILTERING_ICON, Messages.getString("SETTING_FILTERING"), FilterTypeEnum.SETTING_FILTERING,
                        settingFilterView.getFilterMode(), settingFilterView.getFilteringValues());
            }

            return;
        }

        String text = txtSearchField.getText();
        txtSearchField.clear();
        txtSearchField.setText(text);
    }

    @Override
    protected void txtSearchFieldChangeListener(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        new Debouncer<>(300, event -> {
            String resultText = newValue;
            if (newValue != null && !newValue.isEmpty()) {
                resultText = new ConvertToValidLongFilterCommand(newValue).execute();
                if (!resultText.equals(newValue)) {
                    txtSearchField.setText(resultText);
                }
            }

            Long filterValue = null;
            if (resultText != null && !resultText.isEmpty()) {
                try {
                    filterValue = Long.valueOf(resultText);
                } catch (Exception e) {
                    setError(Messages.getString("LONG_VALUE_FROM_TO_ERROR"));
                    return;
                }
            }

            filterItems(filterValue, currentFilterType, null, null);
        }).handle(null);
    }

    @Override
    protected void btnSettingsFilter_onAction(ActionEvent event) {
        popupChangeFilterType.show(btnChangeFilterType);
    }

    @Override
    protected void setFilterType(Long value, FilterTypeEnum type, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        currentFilterType = type;
        filterItems(value, currentFilterType, filterMode, filterValues);
    }

    private void filterItems(Long filterValue, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        clearError();
        Set<T> collect = column.getValues().entrySet().stream().filter(observableValueTEntry -> {
            return observableValueTEntry.getKey() == null ||
                    observableValueTEntry.getKey().getValue() == null ||
                    applyFilter(observableValueTEntry.getKey().getValue(), filterValue, filterType, filterMode, filterValues);
        }).map(Map.Entry::getValue).collect(Collectors.toSet());

        ((JFXTableView<T>) (column.getTableView())).setFilteredItem(collect, column, this);
    }

    private boolean applyFilter(Long item, Long filterValue, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
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

    private boolean applySettingFilter(Long item, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        switch (filterMode) {
            case ALL:
                return applySettingAllFilter(item, filterValues);
            case ANY:
                return applySettingAnyFilter(item, filterValues);
        }

        return true;
    }

    private boolean applySettingAllFilter(Long item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {

            Long filterValue = null;
            if (filter.getText() != null && !filter.getText().isEmpty()) {
                filterValue = Long.valueOf(filter.getText());
            }

            if (!applyFilter(item, filterValue, filter.getType().getType(), null, filterValues)) {
                return false;
            }
        }

        return true;
    }

    private boolean applySettingAnyFilter(Long item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {

            Long filterValue = null;
            if (filter.getText() != null && !filter.getText().isEmpty()) {
                filterValue = Long.valueOf(filter.getText());
            }

            if (applyFilter(item, filterValue, filter.getType().getType(), null, filterValues)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void btnCustom_onAction(ActionEvent event) {
        //открыть контрол настройки фильтрации
        if (dialogBox == null) {
            settingFilterView = new JFXSettingFilterLongViewImpl(new FilterSettingModel(column.getFilterTypes()));
            dialogBox = new JFXSimpleDialogBox(settingFilterView);
        }

        settingFilterView.setItems(new HashSet<>(column.getValues().keySet()));

        dialogBox.setOnClosing(closingEvent -> {
            if (closingEvent.getResult() == ClosingResult.OK) {
                if (settingFilterView.checkErrors()) {
                    closingEvent.setCancel(true);
                    return;
                }

                applyChangeFilterType(SETTING_FILTERING_ICON, Messages.getString("SETTING_FILTERING"), FilterTypeEnum.SETTING_FILTERING,
                        settingFilterView.getFilterMode(), settingFilterView.getFilteringValues());
            }
        });

        dialogBox.show(((JFXTableView<T>) (column.getTableView())).getBG(), 510, -1, Messages.getString("SETTINGS_FILTERING") + column.getColumnName());
    }
}
