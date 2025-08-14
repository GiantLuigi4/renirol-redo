package tfc.renirol.api.obj;

import tfc.renirol.api.Resource;
import tfc.renirol.api.enums.BufferUsage;

import java.nio.ByteBuffer;

public abstract class GPUBuffer extends Resource<GPUBuffer> {
    public abstract void initialize(long size, BufferUsage usage);

    public abstract void initialize(ByteBuffer data, BufferUsage usage);

    public abstract void setData(ByteBuffer buffer);

    public abstract void setSubData(ByteBuffer buffer, int start);
}
