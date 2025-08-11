package tfc.renirol.api.obj;

import tfc.renirol.api.enums.AdvanceRate;
import tfc.renirol.api.enums.NumericPrimitive;

public class VertexAttribute {
    public final AdvanceRate rate;
    public final int elements;
    public final NumericPrimitive primitive;
    public final int stride;
    public final int base;
    public final boolean normalized;

    public VertexAttribute(AdvanceRate rate, int elements, NumericPrimitive primitive, int stride, int base, boolean normalized) {
        this.rate = rate;
        this.elements = elements;
        this.primitive = primitive;
        this.stride = stride;
        this.base = base;
        this.normalized = normalized;
    }

    public VertexAttribute(int elements, NumericPrimitive primitive, int stride, int base, boolean normalized) {
        rate = AdvanceRate.PER_VERTEX;
        this.elements = elements;
        this.primitive = primitive;
        this.stride = stride;
        this.base = base;
        this.normalized = normalized;
    }
}
