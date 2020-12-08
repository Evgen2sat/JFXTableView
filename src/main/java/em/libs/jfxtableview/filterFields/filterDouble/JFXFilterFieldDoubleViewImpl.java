package em.libs.jfxtableview.filterFields.filterDouble;

import em.libs.jfxtableview.Debouncer;
import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.columns.JFXDoubleTableColumn;
import em.libs.jfxtableview.enums.ClosingResult;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.filterFields.commands.ConvertToValidDoubleFilterCommand;
import em.libs.jfxtableview.jfxSimpleDialogBox.JFXSimpleDialogBox;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public class JFXFilterFieldDoubleViewImpl<T> extends JFXFilterFieldDoubleViewDesigner {

    private JFXDoubleTableColumn<T> column;
    private FilterTypeEnum currentFilterType = FilterTypeEnum.EQUALS;
    private JFXSimpleDialogBox dialogBox;
    private JFXSettingFilterDoubleViewImpl settingFilterView;

    public JFXFilterFieldDoubleViewImpl(JFXDoubleTableColumn<T> column) {
        super(column.getFilterTypes());
        this.column = column;
    }

    @Override
    public void updateFilterField() {
        if (currentFilterType == FilterTypeEnum.SETTING_FILTERING) {
            if (!settingFilterView.checkErrors()) {
                applyChangeFilterType(SETTING_FILTERING_ICON, Messages.getString("SETTING_FILTERING"), FilterTypeEnum.SETTING_FILTERING,
                        settingFilterView.getFilterMode(), settingFilterView.getFilteringValues());
            }

            return;
        }

        filterItems(Double.valueOf(txtSearchField.getText()), currentFilterType, null, null);
    }

    @Override
    public void clearFilter() {
        if(currentFilterType == FilterTypeEnum.SETTING_FILTERING) {
            applyChangeFilterType(EQUALS_ICON, Messages.getString("EQUALS"), FilterTypeEnum.EQUALS, null, null);

            return;
        }

        txtSearchField.setText(null);
    }

    @Override
    protected void txtSearchFieldChangeListener(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        new Debouncer<>(300, event -> {
            String resultText = newValue;
            if (newValue != null && !newValue.isEmpty()) {
                resultText = new ConvertToValidDoubleFilterCommand(newValue).execute();
                if (!resultText.equals(newValue)) {
                    txtSearchField.setText(resultText);
                }
            }

            Double filterValue = null;
            if (resultText != null && !resultText.isEmpty()) {
                try {
                    filterValue = Double.valueOf(resultText);
                } catch (Exception e) {
                    setError(Messages.getString("DOUBLE_VALUE_FROM_TO_ERROR"));
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
    protected void setFilterType(Double value, FilterTypeEnum type, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        currentFilterType = type;
        filterItems(value, currentFilterType, filterMode, filterValues);
    }

    private void filterItems(Double filterValue, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        clearError();
        Set<T> collect = column.getValues().entrySet().stream().filter(observableValueTEntry -> {
            Double cellValue = null;

            if (observableValueTEntry.getKey() != null && observableValueTEntry.getKey().getValue() != null) {
                cellValue = observableValueTEntry.getKey().getValue();
            }

            return applyFilter(cellValue, filterValue, filterType, filterMode, filterValues);
        }).map(Map.Entry::getValue).collect(Collectors.toSet());

        ((JFXTableView<T>) (column.getTableView())).setFilteredItem(collect, column, this);
    }

    private boolean applyFilter(Double item, Double filterValue, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        if ((filterType != FilterTypeEnum.SETTING_FILTERING && filterValue == null)
                || (filterType == FilterTypeEnum.SETTING_FILTERING && (filterValues == null || filterValues.isEmpty()))) {
            return true;
        }

        if(item == null) {
            return false;
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

    private boolean applySettingFilter(Double item, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        switch (filterMode) {
            case ALL:
                return applySettingAllFilter(item, filterValues);
            case ANY:
                return applySettingAnyFilter(item, filterValues);
        }

        return true;
    }

    private boolean applySettingAllFilter(Double item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {

            Double filterValue = null;
            if (filter.getText() != null && !filter.getText().isEmpty()) {
                filterValue = Double.valueOf(filter.getText());
            }

            if (!applyFilter(item, filterValue, filter.getType().getType(), null, filterValues)) {
                return false;
            }
        }

        return true;
    }

    private boolean applySettingAnyFilter(Double item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {

            Double filterValue = null;
            if (filter.getText() != null && !filter.getText().isEmpty()) {
                filterValue = Double.valueOf(filter.getText());
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
            settingFilterView = new JFXSettingFilterDoubleViewImpl(new FilterSettingModel(column.getFilterTypes()));
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
