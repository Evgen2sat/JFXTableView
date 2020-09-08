package em.libs.jfxtableview.filterFields.filterLong;

import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;
import em.libs.jfxtableview.enums.FilterModeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JFXSettingFilterLongViewImpl extends JFXSettingFilterLongViewDesigner {

    private final FilterSettingModel viewModel;

    public JFXSettingFilterLongViewImpl(FilterSettingModel viewModel) {
        super(viewModel);
        this.viewModel = viewModel;
        bindFields();
    }

    public List<FilterModel> getFilteringValues() {
        return new ArrayList<>(filterMap.values());
    }

    public FilterModeEnum getFilterMode() {
        return viewModel.getSelectedFilterMode().getFilterMode();
    }

    public void setItems(Set<ObservableValue<Long>> items) {
        super.setItems(items);
    }

    public boolean checkErrors() {
        return existErrorsChecker.checkErrors();
    }
}
