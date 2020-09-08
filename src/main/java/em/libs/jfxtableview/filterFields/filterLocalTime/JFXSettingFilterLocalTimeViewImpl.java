package em.libs.jfxtableview.filterFields.filterLocalTime;

import javafx.beans.value.ObservableValue;
import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JFXSettingFilterLocalTimeViewImpl extends JFXSettingFilterLocalTimeViewDesigner {

    private final FilterSettingModel viewModel;

    public JFXSettingFilterLocalTimeViewImpl(FilterSettingModel viewModel) {
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

    public void setItems(Set<ObservableValue<LocalTime>> items) {
        super.setItems(items);
    }

    public boolean checkErrors() {
        return existErrorsChecker.checkErrors();
    }
}
