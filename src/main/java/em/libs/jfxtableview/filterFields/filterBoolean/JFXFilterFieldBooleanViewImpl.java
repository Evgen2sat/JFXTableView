package em.libs.jfxtableview.filterFields.filterBoolean;

import em.libs.jfxtableview.JFXTableView;
import em.libs.jfxtableview.columns.JFXBooleanTableColumn;
import em.libs.jfxtableview.enums.CheckBoxFilterEnum;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JFXFilterFieldBooleanViewImpl<T> extends JFXFilterFieldBooleanViewDesigner {

    private final JFXBooleanTableColumn<T> column;

    public JFXFilterFieldBooleanViewImpl(JFXBooleanTableColumn<T> column) {
        this.column = column;
        initItems();
    }

    private void initItems() {
        ObservableList<CheckBoxFilterEnum> items = FXCollections.observableArrayList();
        items.add(CheckBoxFilterEnum.ALL);
        items.add(CheckBoxFilterEnum.TRUE);
        items.add(CheckBoxFilterEnum.FALSE);

        setItems(items);
    }

    @Override
    public void updateFilterField() {
        CheckBoxFilterEnum selectedItem = chbFilter.getSelectionModel().getSelectedItem();
        chbFilter.getSelectionModel().clearSelection();
        chbFilter.getSelectionModel().select(selectedItem);
    }

    @Override
    protected void chbFilter_onSelectedChanged(ObservableValue<? extends CheckBoxFilterEnum> observable, CheckBoxFilterEnum oldValue, CheckBoxFilterEnum newValue) {
        Set<T> collect = column.getValues().entrySet().stream().filter(observableValueTEntry -> {
            return observableValueTEntry.getKey() == null ||
                    observableValueTEntry.getKey().getValue() == null ||
                    applyFilter(observableValueTEntry.getKey().getValue(), newValue);
        }).map(Map.Entry::getValue).collect(Collectors.toSet());

        ((JFXTableView<T>) (column.getTableView())).setFilteredItem(collect, column, this);
    }

    private boolean applyFilter(Boolean item, CheckBoxFilterEnum filterValue) {
        switch (filterValue) {
            case ALL:
                return true;
            case TRUE:
                return item;
            case FALSE:
                return !item;
        }

        return true;
    }
}
