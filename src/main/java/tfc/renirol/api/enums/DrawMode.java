package tfc.renirol.api.enums;

public enum DrawMode {
    POINTS,
    LINES,
    LINE_STRIP,
    TRIANGLES,
    TRIANGLE_STRIP,
    TRIANGLE_FAN,
    ;

    private static final DrawMode[] VALUES = values();

    public static DrawMode fromOrdinal(int arg0) {
        return VALUES[arg0];
    }
}
