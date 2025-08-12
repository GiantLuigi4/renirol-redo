package tfc.renirol.api.obj;

import tfc.renirol.api.Resource;
import tfc.renirol.api.enums.NumericPrimitive;

public abstract class ArrayObject extends Resource<ArrayObject> {
    public abstract void activate();
    public abstract void deactivate();
    public abstract void bind(GPUBuffer vertex);
    public abstract void bind(GPUBuffer vertex, GPUBuffer element);
    public abstract ArrayObject withAttribute(int slot, VertexAttribute attribute);
    public abstract ArrayObject disableAttribute(int slot);
}
