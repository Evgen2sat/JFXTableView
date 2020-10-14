package em.libs.jfxtableview.filterFields.filterBigDecimal;

import em.libs.jfxtableview.enums.FilterModeEnum;
import em.libs.jfxtableview.models.FilterModel;
import em.libs.jfxtableview.models.FilterSettingModel;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JFXSettingFilterBigDecimalViewImpl extends JFXSettingFilterBigDecimalViewDesigner {

    private final FilterSettingModel viewModel;

    public JFXSettingFilterBigDecimalViewImpl(FilterSettingModel viewModel) {
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

    public void setItems(Set<ObservableValue<BigDecimal>> items) {
        super.setItems(items);
    }

    public boolean checkErrors() {
        return existErrorsChecker.checkErrors();
    }
}
