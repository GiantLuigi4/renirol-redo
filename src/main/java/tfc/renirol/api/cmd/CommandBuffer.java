package tfc.renirol.api.cmd;

import tfc.renirol.api.GraphicsCalls;
import tfc.renirol.api.Resource;
import tfc.renirol.api.obj.ArrayObject;

/**
 * In a command buffer, a draw mode MUST be set before any render calls
 */
public abstract class CommandBuffer extends Resource<CommandBuffer> implements GraphicsCalls {
    public abstract void bindVAO(ArrayObject obj);

    public abstract void unbindVAO();

    /**
     * Compiles the command buffer when done building
     * Internal optimizations may be performed when compile is called
     */
    public abstract void compile();

    /**
     * Invokes the command buffer, causing its effect to be performed
     */
    public abstract void dispatch();
}
