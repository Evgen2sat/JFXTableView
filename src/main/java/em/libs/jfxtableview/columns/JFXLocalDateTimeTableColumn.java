package em.libs.jfxtableview.columns;

import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.Messages;
import em.libs.jfxtableview.cellFactories.JFXDefaultTableCell;
import em.libs.jfxtableview.enums.FilterTypeEnum;
import em.libs.jfxtableview.enums.TotalTypeEnum;
import em.libs.jfxtableview.filterFields.JFXFilterFieldView;
import em.libs.jfxtableview.filterFields.filterLocalDateTime.JFXFilterFieldLocalDateTimeViewImpl;
import em.libs.jfxtableview.models.FilterTypeModel;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static em.libs.jfxtableview.Constants.*;

public class JFXLocalDateTimeTableColumn<S> extends JFXTableColumn<S, LocalDateTime> {

    private JFXFilterFieldView filterFieldView;
    private List<FilterTypeModel> filterTypes;

    public JFXLocalDateTimeTableColumn() {
        this(null);
    }

    public JFXLocalDateTimeTableColumn(String text) {
        super(text, param -> new JFXDefaultTableCell<S, LocalDateTime>().call((JFXTableColumn<S, LocalDateTime>)param));
        this.setMinWidth(200);
        initTotalRow();
    }

    private void init() {
        filterTypes = new ArrayList<>();
        filterTypes.add(new FilterTypeModel(Messages.getString("EQUALS"), FilterTypeEnum.EQUALS, EQUALS_ICON, Messages.getString("EQUALS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("NOT_EQUALS"), FilterTypeEnum.NOT_EQUALS, NOT_EQUALS_ICON, Messages.getString("NOT_EQUALS")));
        filterTypes.add(new FilterTypeModel(Messages.getString("GREATHER_EQUALS_THAN"), FilterTypeEnum.GREATHER_EQUALS_THAN, GREATHER_EQUALS_THAN_ICON, Messages.getString("GREATHER_EQUALS_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("GREATHER_THAN"), FilterTypeEnum.GREATHER_THAN, GREATHER_THAN_ICON, Messages.getString("GREATHER_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("LESS_EQUALS_THAN"), FilterTypeEnum.LESS_EQUALS_THAN, LESS_EQUALS_THAN_ICON, Messages.getString("LESS_EQUALS_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("LESS_THAN"), FilterTypeEnum.LESS_THAN, LESS_THAN_ICON, Messages.getString("LESS_THAN")));
        filterTypes.add(new FilterTypeModel(Messages.getString("SETTING_FILTERING"), FilterTypeEnum.SETTING_FILTERING, SETTING_FILTERING_ICON, Messages.getString("SETTING_FILTERING")));
    }

    @Override
    public JFXFilterFieldView getFilterFieldView() {
        if (filterFieldView == null) {
            init();
            filterFieldView = new JFXFilterFieldLocalDateTimeViewImpl<>(this);
        }

        return filterFieldView;
    }

    @Override
    public List<FilterTypeModel> getFilterTypes() {
        return filterTypes;
    }

    @Override
    public Map<ObservableValue<LocalDateTime>, S> getValues() {
        Map<ObservableValue<LocalDateTime>, S> values = new HashMap<>();

        ((JFXTableView<S>) getTableView()).getBackingList().forEach(item -> values.put(getCellObservableValue(item), item));

        return values;
    }

    @Override
    public Set<TotalTypeEnum> getTotalTypes() {
        return new HashSet<>(Arrays.asList(TotalTypeEnum.MINIMUM, TotalTypeEnum.MAXIMUM));
    }

    @Override
    public String getTotalText(List<TotalTypeEnum> selectedTotalTypes) {
        if (selectedTotalTypes == null || selectedTotalTypes.isEmpty()) {
            return null;
        }

        List<LocalDateTime> items = ((JFXTableView<S>) getTableView()).getFilteredList().stream().filter(item -> getCellData(item) != null).map(this::getCellData).collect(Collectors.toList());

        if (items.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (TotalTypeEnum totalType : selectedTotalTypes) {
            switch (totalType) {
                case MINIMUM:
                    result
                            .append(Messages.getString("MINIMUM"))
                            .append(": ")
                            .append(Collections.min(items).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                            .append("; ");
                    break;
                case MAXIMUM:
                    result
                            .append(Messages.getString("MAXIMUM"))
                            .append(": ")
                            .append(Collections.max(items).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                            .append("; ");
                    break;
            }
        }

        return result.toString();
    }
}
