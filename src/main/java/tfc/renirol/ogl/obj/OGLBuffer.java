package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL20;
import tfc.renirol.api.enums.BufferUsage;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

import java.nio.ByteBuffer;

public class OGLBuffer extends GPUBuffer {
    boolean initialized = false;
    int id = -1;
    long size;
    int usage;
    OGLGraphicsSystem system;

    public OGLBuffer(OGLGraphicsSystem system) {
        this.system = system;
    }

    public void initialize(long size, BufferUsage usage) {
        if (initialized) {
            // no-op for opengl
        } else {
            id = GL20.glGenBuffers();
        }

        system.bindArrayBuffer(this);

        this.size = size;

        int ord = usage.ordinal();
        if (ord > 5) ord += 2;
        else if (ord > 2) ord += 1;
        this.usage = ord;

        GL20.glBufferData(
                GL20.GL_ARRAY_BUFFER, size, ord + GL20.GL_STREAM_DRAW
        );
    }

    @Override
    public void initialize(ByteBuffer data, BufferUsage usage) {
        if (initialized) {
            // no-op for opengl
        } else {
            id = GL20.glGenBuffers();
        }

        system.bindArrayBuffer(this);

        this.size = data.capacity();

        int ord = usage.ordinal();
        if (ord > 5) ord += 2;
        else if (ord > 2) ord += 1;
        this.usage = ord;

        GL20.glBufferData(
                GL20.GL_ARRAY_BUFFER, data, ord + GL20.GL_STREAM_DRAW
        );
    }

    public void setData(ByteBuffer buffer) {
        system.bindArrayBuffer(this);

        GL20.glBufferData(
                GL20.GL_ARRAY_BUFFER, buffer, usage
        );
    }

    @Override
    public void setSubData(ByteBuffer data, int start) {
        system.bindArrayBuffer(this);
        GL20.glBufferSubData(GL20.GL_ARRAY_BUFFER, start, data);
    }

    public int id() {
        return id;
    }

    @Override
    public void _delete() {
        system.delBuffer(this);
    }

    @Override
    protected void debugSetName() {
        if (id != -1) system.setDebugName(ObjectType.GPU_BUFFER, id, getName());
    }
}
