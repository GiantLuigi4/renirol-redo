package tfc.renirol.api.shader;

import tfc.renirol.api.enums.NumericPrimitive;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class UniformAccessor {
    public abstract UniformAccessor specifyPrimitive(NumericPrimitive type, int count);

    public abstract UniformAccessor specifyMatrix(NumericPrimitive type, int width, int height, boolean transposed);

    public abstract void upload();

    public abstract UniformAccessor setInt(int index, int value);

    public abstract UniformAccessor setInts(int x);

    public abstract UniformAccessor setInts(int x, int y);

    public abstract UniformAccessor setInts(int x, int y, int z);

    public abstract UniformAccessor setInts(int x, int y, int z, int w);

    public abstract UniformAccessor setInts(IntBuffer data);

    public abstract UniformAccessor setFloat(int index, float value);

    public abstract UniformAccessor setFloats(float x);

    public abstract UniformAccessor setFloats(float x, float y);

    public abstract UniformAccessor setFloats(float x, float y, float z);

    public abstract UniformAccessor setFloats(float x, float y, float z, float w);

    public abstract UniformAccessor setFloats(FloatBuffer data);
}
