package em.libs.jfxtableview.font;

import javafx.scene.text.Font;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static em.libs.jfxtableview.font.FontResources.*;

public class FontAwesome {
    private static final Map<FontParams, Font> cachedFonts = new ConcurrentHashMap<>();

    private final double size;

    public FontAwesome() {
        this(18);
    }

    public FontAwesome(double size) {
        this.size = size;
    }

    public static Font loadFont(String name, double size) {
        return cachedFonts.computeIfAbsent(
                new FontParams(size, name),
                params -> Font.loadFont(
                        FontResources.class.getResource(params.getName()).toExternalForm(),
                        params.getSize()
                ));
    }

    public Font getFontSolid() {
        return getFontSolid(this.size);
    }

    public static Font getFontSolid(double size) {
        return loadFont(SOLID, size);
    }

    public Font getFontRegular() {
        return getFontRegular(this.size);
    }

    public static Font getFontRegular(double size) {
        return loadFont(REGULAR, size);
    }

    public Font getFontLight() {
        return getFontLight(this.size);
    }

    public Font getFontLight(double size) {
        return loadFont(LIGHT, size);
    }

    private static class FontParams {
        private final double size;
        private final String name;

        public FontParams(double size, String name) {
            this.size = size;
            this.name = name;
        }

        public double getSize() {
            return size;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FontParams that = (FontParams) o;
            return size == that.size &&
                    name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(size, name);
        }
    }
}
