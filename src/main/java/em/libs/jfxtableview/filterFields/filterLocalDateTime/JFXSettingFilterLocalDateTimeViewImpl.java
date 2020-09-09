package em.libs.jfxtableview.filterFields.filterLocalDateTime;

import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JFXSettingFilterLocalDateTimeViewImpl extends JFXSettingFilterLocalDateTimeViewDesigner {

    private final FilterSettingModel viewModel;

    public JFXSettingFilterLocalDateTimeViewImpl(FilterSettingModel viewModel) {
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

    public void setItems(Set<ObservableValue<LocalDateTime>> items) {
        super.setItems(items);
    }

    public boolean checkErrors() {
        return existErrorsChecker.checkErrors();
    }
}
