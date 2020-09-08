package em.libs.jfxtableview.models;

import em.libs.jfxtableview.enums.FilterModeEnum;

import java.util.Objects;

public class FilterModeModel {
    private String name;
    private FilterModeEnum filterMode;

    public FilterModeModel(String name, FilterModeEnum filterMode) {
        this.name = name;
        this.filterMode = filterMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterModeEnum getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(FilterModeEnum filterMode) {
        this.filterMode = filterMode;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterModeModel that = (FilterModeModel) o;
        return filterMode == that.filterMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filterMode);
    }
}
