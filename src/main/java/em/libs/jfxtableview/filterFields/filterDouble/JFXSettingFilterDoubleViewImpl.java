package em.libs.jfxtableview.filterFields.filterDouble;

import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JFXSettingFilterDoubleViewImpl extends JFXSettingFilterDoubleViewDesigner {

    private final FilterSettingModel viewModel;

    public JFXSettingFilterDoubleViewImpl(FilterSettingModel viewModel) {
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

    public void setItems(Set<ObservableValue<Double>> items) {
        super.setItems(items);
    }

    public boolean checkErrors() {
        return existErrorsChecker.checkErrors();
    }
}
