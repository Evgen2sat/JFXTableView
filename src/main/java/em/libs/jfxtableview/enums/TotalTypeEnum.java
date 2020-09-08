package em.libs.jfxtableview.enums;

import em.libs.jfxtableview.Constants;

public enum TotalTypeEnum {
    AMOUNT(Constants.AMOUNT),
    AVERAGE(Constants.AVERAGE),
    MINIMUM(Constants.MINIMUM),
    MAXIMUM(Constants.MAXIMUM),
    SUM(Constants.SUM);

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
