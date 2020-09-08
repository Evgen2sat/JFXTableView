package em.libs.jfxtableview.models;

import em.libs.jfxtableview.Messages;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.enums.FilterTypeEnum;

import java.util.List;
import java.util.stream.Collectors;

public class FilterSettingModel {
    private ObservableList<FilterModeModel> filterModes = FXCollections.observableArrayList();
    private final SimpleObjectProperty<FilterModeModel> selectedFilterMode;
    private ObservableList<FilterTypeModel> filterTypes = FXCollections.observableArrayList();

    public FilterSettingModel(List<FilterTypeModel> filterTypes) {
        this.filterTypes.addAll(filterTypes.stream().filter(item -> item.getType() != FilterTypeEnum.SETTING_FILTERING).collect(Collectors.toList()));

        FilterModeModel mode1 = new FilterModeModel(Messages.getString("FOR_ANY_OPTIONS"), FilterModeEnum.ANY);
        FilterModeModel mode2 = new FilterModeModel(Messages.getString("FOR_ALL_OPTIONS"), FilterModeEnum.ALL);

        filterModes.add(mode1);
        filterModes.add(mode2);

        selectedFilterMode = new SimpleObjectProperty<>(mode1);
    }

    public FilterModeModel getSelectedFilterMode() {
        return selectedFilterMode.get();
    }

    public SimpleObjectProperty<FilterModeModel> selectedFilterModeProperty() {
        return selectedFilterMode;
    }

    public void setSelectedFilterMode(FilterModeModel selectedFilterMode) {
        this.selectedFilterMode.set(selectedFilterMode);
    }

    public ObservableList<FilterModeModel> getFilterModes() {
        return filterModes;
    }

    public ObservableList<FilterTypeModel> getFilterTypes() {
        return filterTypes;
    }
}
