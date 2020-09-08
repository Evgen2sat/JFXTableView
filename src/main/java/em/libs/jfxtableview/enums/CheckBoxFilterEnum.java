package em.libs.jfxtableview.enums;

import em.libs.jfxtableview.Messages;

public enum CheckBoxFilterEnum {
    ALL(Messages.getString("ALL")),
    TRUE(Messages.getString("TRUE")),
    FALSE(Messages.getString("FALSE"));

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
