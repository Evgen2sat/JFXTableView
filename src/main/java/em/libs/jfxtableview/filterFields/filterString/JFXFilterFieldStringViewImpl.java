package em.libs.jfxtableview.filterFields.filterString;

import em.libs.jfxtableview.Debouncer;
import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.columns.JFXStringTableColumn;
import em.libs.jfxtableview.enums.ClosingResult;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.jfxSimpleDialogBox.JFXSimpleDialogBox;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.CONTAINS_ICON;
import static em.libs.jfxtableview.Constants.SETTING_FILTERING_ICON;

public class JFXFilterFieldStringViewImpl<T> extends JFXFilterFieldStringViewDesigner {

    private JFXStringTableColumn<T> column;
    private FilterTypeEnum currentFilterType = FilterTypeEnum.CONTAINS;
    private JFXSimpleDialogBox dialogBox;
    private JFXSettingFilterStringViewImpl settingFilterView;

    public JFXFilterFieldStringViewImpl(JFXStringTableColumn<T> column) {
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

        txtSearchField.setText(null);
    }

    @Override
    public void clearFilter() {
        if(currentFilterType == FilterTypeEnum.SETTING_FILTERING) {
            applyChangeFilterType(CONTAINS_ICON, Messages.getString("CONTAINS"), FilterTypeEnum.CONTAINS, null, null);

            return;
        }

        txtSearchField.setText(null);
    }

    @Override
    protected void txtSearchFieldChangeListener(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        new Debouncer<>(300, event -> {
            filterItems(newValue, currentFilterType, null, null);
        }).handle(null);
    }

    @Override
    protected void btnSettingsFilter_onAction(ActionEvent event) {
        popupChangeFilterType.show(btnChangeFilterType);
    }

    @Override
    protected void setFilterType(String text, FilterTypeEnum type, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        currentFilterType = type;
        filterItems(text, currentFilterType, filterMode, filterValues);
    }

    private void filterItems(String filterText, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        clearError();
        Set<T> collect = column.getValues().entrySet().stream().filter(observableValueTEntry -> {
            String cellValue = null;

            if (observableValueTEntry.getKey() != null && observableValueTEntry.getKey().getValue() != null) {
                cellValue = observableValueTEntry.getKey().getValue();
            }

            return applyFilter(cellValue, filterText, filterType, filterMode, filterValues);
        }).map(Map.Entry::getValue).collect(Collectors.toSet());

        ((JFXTableView<T>) (column.getTableView())).setFilteredItem(collect, column, this);
    }

    private boolean applyFilter(String item, String filterText, FilterTypeEnum filterType, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        if ((filterType != FilterTypeEnum.SETTING_FILTERING && (filterText == null || filterText.isEmpty()))
                || (filterType == FilterTypeEnum.SETTING_FILTERING && (filterValues == null || filterValues.isEmpty()))) {
            return true;
        }

        if(item == null) {
            return false;
        }

        switch (filterType) {
            case EQUALS:
                return item.toLowerCase().equals(filterText.toLowerCase());
            case NOT_EQUALS:
                return !item.toLowerCase().equals(filterText.toLowerCase());
            case GREATHER_EQUALS_THAN:
                return item.toLowerCase().compareTo(filterText.toLowerCase()) >= 0;
            case GREATHER_THAN:
                return item.toLowerCase().compareTo(filterText.toLowerCase()) > 0;
            case LESS_EQUALS_THAN:
                return item.toLowerCase().compareTo(filterText.toLowerCase()) <= 0;
            case LESS_THAN:
                return item.toLowerCase().compareTo(filterText.toLowerCase()) < 0;
            case START_WITH:
                return item.toLowerCase().startsWith(filterText.toLowerCase());
            case END_WITH:
                return item.toLowerCase().endsWith(filterText.toLowerCase());
            case CONTAINS:
                return item.toLowerCase().contains(filterText.toLowerCase());
            case NOT_CONTAINS:
                return !item.toLowerCase().contains(filterText.toLowerCase());
            case REGULAR_EXPRESSION:
                return applyRegExFilter(item, filterText);
            case SETTING_FILTERING:
                return applySettingFilter(item, filterMode, filterValues);
            default:
                return true;
        }
    }

    private boolean applyRegExFilter(String item, String filterText) {
        try {
            Pattern compile = Pattern.compile(filterText, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            return compile.matcher(item).matches();
        } catch (Exception e) {
            setError(Messages.getString("REGULAR_EXPRESSION_ERROR"));
            return false;
        }
    }

    private boolean applySettingFilter(String item, FilterModeEnum filterMode, List<FilterModel> filterValues) {
        switch (filterMode) {
            case ALL:
                return applySettingAllFilter(item, filterValues);
            case ANY:
                return applySettingAnyFilter(item, filterValues);
        }

        return true;
    }

    private boolean applySettingAllFilter(String item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {
            if (!applyFilter(item, filter.getText(), filter.getType().getType(), null, filterValues)) {
                return false;
            }
        }

        return true;
    }

    private boolean applySettingAnyFilter(String item, List<FilterModel> filterValues) {
        for (FilterModel filter : filterValues) {
            if (applyFilter(item, filter.getText(), filter.getType().getType(), null, filterValues)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void btnCustom_onAction(ActionEvent event) {
        //открыть контрол настройки фильтрации
        if (dialogBox == null) {
            settingFilterView = new JFXSettingFilterStringViewImpl(new FilterSettingModel(column.getFilterTypes()));
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
