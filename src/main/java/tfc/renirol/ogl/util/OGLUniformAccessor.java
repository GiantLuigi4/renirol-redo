package tfc.renirol.ogl.util;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.shader.UniformAccessor;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.obj.OGLShaderProgram;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class OGLUniformAccessor extends UniformAccessor {
    OGLGraphicsSystem system;
    OGLShaderProgram program;
    int location;

    ByteBuffer data;
    boolean dirty = false;

    Runnable upload;

    public OGLUniformAccessor(OGLGraphicsSystem system, OGLShaderProgram program, int location) {
        this.system = system;
        this.program = program;
        this.location = location;
    }

    public UniformAccessor specifyPrimitive(NumericPrimitive type, int count) {
        data = MemoryUtil.memAlloc(type.bytes() * count);

        switch (type) {
            case BYTE -> throw new RuntimeException("Unsupported");
            case SHORT -> throw new RuntimeException("Unsupported");
            case UBYTE -> throw new RuntimeException("Unsupported");
            case USHORT -> throw new RuntimeException("Unsupported");
            case DOUBLE -> throw new RuntimeException("Unsupported");
            case UINT, INT -> {
                IntBuffer ib = data.asIntBuffer();
                switch (count) {
                    case 1 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform1iv(location, ib);
                    };
                    case 2 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform2iv(location, ib);
                    };
                    case 3 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform3iv(location, ib);
                    };
                    case 4 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform4iv(location, ib);
                    };
                    default -> throw new RuntimeException("Unsupported");
                }
            }
            case FLOAT -> {
                FloatBuffer ib = data.asFloatBuffer();
                switch (count) {
                    case 1 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform1fv(location, ib);
                    };
                    case 2 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform2fv(location, ib);
                    };
                    case 3 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform3fv(location, ib);
                    };
                    case 4 -> upload = () -> {
                        ensureBind();
                        GL30.glUniform4fv(location, ib);
                    };
                    default -> throw new RuntimeException("Unsupported");
                }
            }
        }
        return this;
    }

    public UniformAccessor specifyMatrix(NumericPrimitive type, int width, int height, boolean transposed) {
        data = MemoryUtil.memAlloc(type.bytes() * width * height);

        switch (type) {
            case FLOAT -> {
                FloatBuffer ib = data.asFloatBuffer();
                if (width == height) {
                    switch (width) {
                        case 4 -> upload = () -> {
                            ensureBind();
                            GL30.glUniformMatrix4fv(location, transposed, ib);
                        };
                        case 3 -> upload = () -> {
                            ensureBind();
                            GL30.glUniformMatrix3fv(location, transposed, ib);
                        };
                        case 2 -> upload = () -> {
                            ensureBind();
                            GL30.glUniformMatrix2fv(location, transposed, ib);
                        };
                    }
                }

                switch (width) {
                    case 2 -> {
                        switch (height) {
                            case 4 -> upload = () -> {
                                ensureBind();
                                GL30.glUniformMatrix2x4fv(location, transposed, ib);
                            };
                            case 3 -> upload = () -> {
                                ensureBind();
                                GL30.glUniformMatrix2x3fv(location, transposed, ib);
                            };
                        }
                    }
                    case 3 -> {
                        switch (height) {
                            case 4 -> upload = () -> {
                                ensureBind();
                                GL30.glUniformMatrix3x4fv(location, transposed, ib);
                            };
                            case 2 -> upload = () -> {
                                ensureBind();
                                GL30.glUniformMatrix3x2fv(location, transposed, ib);
                            };
                        }
                    }
                    case 4 -> {
                        switch (height) {
                            case 4 -> upload = () -> {
                                ensureBind();
                                GL30.glUniformMatrix4x3fv(location, transposed, ib);
                            };
                            case 2 -> upload = () -> {
                                ensureBind();
                                GL30.glUniformMatrix4x2fv(location, transposed, ib);
                            };
                        }
                    }
                }
            }
            default -> throw new RuntimeException("Unsupported");
        }
        if (upload == null) {
            throw new RuntimeException("NYI or Unsupported.");
        }
        return this;
    }

    private void ensureBind() {
        system.checkShader(program);
    }

    public UniformAccessor setInts(int x) {
        IntBuffer ints = data.asIntBuffer();
        ints.put(0, x);
        dirty = true;
        return this;
    }

    public UniformAccessor setInts(int x, int y) {
        IntBuffer ints = data.asIntBuffer();
        ints.put(0, x);
        ints.put(1, y);
        dirty = true;
        return this;
    }

    public UniformAccessor setInts(int x, int y, int z) {
        IntBuffer ints = data.asIntBuffer();
        ints.put(0, x);
        ints.put(1, y);
        ints.put(2, z);
        dirty = true;
        return this;
    }

    public UniformAccessor setInts(int x, int y, int z, int w) {
        IntBuffer ints = data.asIntBuffer();
        ints.put(0, x);
        ints.put(1, y);
        ints.put(2, z);
        ints.put(3, w);
        dirty = true;
        return this;
    }

    public UniformAccessor setInts(IntBuffer data) {
        IntBuffer ints = this.data.asIntBuffer();
        ints.put(data);
        ints.position(0);
        dirty = true;
        return this;
    }

    public UniformAccessor setInt(int index, int value) {
        IntBuffer ints = data.asIntBuffer();
        if (ints.get(index) != value) {
            dirty = true;
            ints.put(index, value);
        }
        return this;
    }

    public UniformAccessor setFloats(float x) {
        FloatBuffer ints = data.asFloatBuffer();
        ints.put(0, x);
        dirty = true;
        return this;
    }

    public UniformAccessor setFloats(float x, float y) {
        FloatBuffer ints = data.asFloatBuffer();
        ints.put(0, x);
        ints.put(1, y);
        dirty = true;
        return this;
    }

    public UniformAccessor setFloats(float x, float y, float z) {
        FloatBuffer ints = data.asFloatBuffer();
        ints.put(0, x);
        ints.put(1, y);
        ints.put(2, z);
        dirty = true;
        return this;
    }

    public UniformAccessor setFloats(float x, float y, float z, float w) {
        FloatBuffer ints = data.asFloatBuffer();
        ints.put(0, x);
        ints.put(1, y);
        ints.put(2, z);
        ints.put(3, w);
        dirty = true;
        return this;
    }

    public UniformAccessor setFloats(FloatBuffer data) {
        FloatBuffer ints = this.data.asFloatBuffer();
        ints.put(data);
        ints.position(0);
        dirty = true;
        return this;
    }

    public UniformAccessor setFloat(int index, float value) {
        FloatBuffer ints = data.asFloatBuffer();
        if (ints.get(index) != value) {
            dirty = true;
            ints.put(index, value);
        }
        return this;
    }

    public void upload() {
        if (dirty) upload.run();
    }
}
