package tfc.renirol.api.cmd;

import tfc.renirol.api.GraphicsCalls;
import tfc.renirol.api.Resource;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.shader.UniformAccessor;

import java.nio.ByteBuffer;

public abstract class CommandBuffer extends Resource<CommandBuffer> implements GraphicsCalls {
    public abstract void bindVAO(ArrayObject obj);

    public abstract void unbindVAO();

    /**
     * Copies the data from a uniform accessor for sake of allowing it to be set later
     * This can either copy to a CPU buffer or a GPU buffer, dependent on backend+configuration
     * It should however, default to using a CPU buffer
     *
     * @param accessor the uniform accessor to store the data from
     */
    // TODO: fill uniform range?
    //       set individual value?
    //       set range from buffer?
    public abstract void setUniform(UniformAccessor accessor);

    public abstract void setBufferData(GPUBuffer buffer, ByteBuffer data);

    public abstract void setBufferSubData(GPUBuffer buffer, ByteBuffer data, int start);

    /**
     * Compiles the command buffer when done building
     * Internal optimizations may be performed when compile is called
     */
    public abstract void compile();

    /**
     * Invokes the command buffer, causing its effect to be performed
     */
    public abstract void dispatch();

    /**
     * Introduces a sort of graph break
     * This may or may not have overhead depending on backend and configuration
     * It also may or may not allow for more intent-aware internal optimizations, or be required in order for some configurations to work
     * Backends should attempt to optimize out any necessary segmenting
     */
    public void segment() {
    }
}
