package em.libs.jfxtableview.models;

import em.libs.jfxtableview.enums.FilterTypeEnum;

public class FilterTypeModel {
    private String name;
    private FilterTypeEnum type;
    private String icon;
    private String description;

    public FilterTypeModel(String name, FilterTypeEnum type, String icon, String description) {
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterTypeEnum getType() {
        return type;
    }

    public void setType(FilterTypeEnum type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
