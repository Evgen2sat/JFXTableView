package em.libs.jfxtableview.font;

import javafx.scene.text.Font;

public class FontAwesome {

    private static final String SOLID = "/fonts/fa-solid-900.ttf";
    private static final String REGULAR = "/fonts/fa-regular-400.ttf";
    private static final String LIGHT = "/fonts/fa-light-300.ttf";
    private final Font fontSolid;
    private final Font fontRegular;
    private final Font fontLight;

    public FontAwesome() {
        this(18);
    }

    public FontAwesome(int size) {
        fontSolid = Font.loadFont(FontAwesome.class.getResource(SOLID).toExternalForm(), size);
        fontRegular = Font.loadFont(FontAwesome.class.getResource(REGULAR).toExternalForm(), size);
        fontLight = Font.loadFont(FontAwesome.class.getResource(LIGHT).toExternalForm(), size);
    }

    public Font getFontSolid() {
        return fontSolid;
    }

    public Font getFontRegular() {
        return fontRegular;
    }

    public Font getFontLight() {
        return fontLight;
    }
}
