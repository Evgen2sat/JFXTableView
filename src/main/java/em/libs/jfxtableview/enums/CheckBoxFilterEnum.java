package em.libs.jfxtableview.enums;

import em.libs.jfxtableview.Constants;

public enum CheckBoxFilterEnum {
    ALL(Constants.ALL),
    TRUE(Constants.TRUE),
    FALSE(Constants.FALSE);

    private final String text;

    CheckBoxFilterEnum(String text) {
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
