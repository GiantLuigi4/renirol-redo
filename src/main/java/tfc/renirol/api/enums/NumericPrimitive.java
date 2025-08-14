package tfc.renirol.api.enums;

public enum NumericPrimitive {
    BYTE(1),
    UBYTE(1),
    SHORT(2),
    USHORT(2),
    INT(4),
    UINT(4),
    FLOAT(4),
    DOUBLE(8);

    private final int bytes;

    NumericPrimitive(int bytes) {
        this.bytes = bytes;
    }

    private static final NumericPrimitive[] VALUES = values();

    public static NumericPrimitive fromOrdinal(int arg2) {
        return VALUES[arg2];
    }

    public int bytes() {
        return bytes;
    }
}
