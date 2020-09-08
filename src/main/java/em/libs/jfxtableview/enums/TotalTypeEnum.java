package em.libs.jfxtableview.enums;

import em.libs.jfxtableview.Constants;
import em.libs.jfxtableview.Messages;

public enum TotalTypeEnum {
    AMOUNT(Messages.getString("AMOUNT")),
    AVERAGE(Messages.getString("AVERAGE")),
    MINIMUM(Messages.getString("MINIMUM")),
    MAXIMUM(Messages.getString("MAXIMUM")),
    SUM(Messages.getString("SUM"));

    private final String text;

    TotalTypeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
