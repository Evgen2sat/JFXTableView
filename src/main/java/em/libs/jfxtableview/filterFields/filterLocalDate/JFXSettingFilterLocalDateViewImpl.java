package em.libs.jfxtableview.filterFields.filterLocalDate;

import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JFXSettingFilterLocalDateViewImpl extends JFXSettingFilterLocalDateViewDesigner {

    private final FilterSettingModel viewModel;

    public JFXSettingFilterLocalDateViewImpl(FilterSettingModel viewModel) {
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

    public void setItems(Set<ObservableValue<LocalDate>> items) {
        super.setItems(items);
    }

    public boolean checkErrors() {
        return existErrorsChecker.checkErrors();
    }
}
