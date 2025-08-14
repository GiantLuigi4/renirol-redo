package tfc.renirol.ogl.cmd;

import org.lwjgl.system.MemoryUtil;
import tfc.renirol.Util;
import tfc.renirol.api.cmd.CommandBuffer;
import tfc.renirol.api.enums.DrawMode;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.shader.UniformAccessor;
import tfc.renirol.api.state.RenderPass;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.obj.OGLArrayObject;
import tfc.renirol.ogl.obj.OGLBuffer;
import tfc.renirol.ogl.obj.OGLShaderProgram;
import tfc.renirol.ogl.util.OGLUniformAccessor;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GLCmdBuffer extends CommandBuffer {
    protected List<CallArgs> commands = new ArrayList<>();
    protected OGLGraphicsSystem system;
    protected boolean compiled = false;
    List<ByteBuffer> __resources = new ArrayList<>();

    public GLCmdBuffer(OGLGraphicsSystem system) {
        this.system = system;
    }

    @Override
    public void startRenderPass(RenderPass pass) {
        validateUncompiled();
        commands.add(new CallArgs.Obj1(DrawCommands.START_PASS, pass));
    }

    @Override
    public void endRenderPass() {
        validateUncompiled();
        commands.add(new CallArgs.NoArg(DrawCommands.END_PASS));
    }

    @Override
    public void bindVAO(ArrayObject obj) {
        validateUncompiled();
        commands.add(new CallArgs.Obj1(DrawCommands.BIND_VAO, obj));
    }

    @Override
    public void unbindVAO() {
        validateUncompiled();
        commands.add(new CallArgs.NoArg(DrawCommands.UNBIND_VAO));
    }

    @Override
    public void setDrawMode(DrawMode mode) {
        validateUncompiled();
        commands.add(new CallArgs.Int1(DrawCommands.SET_DRAW_MODE, mode.ordinal()));
    }

    @Override
    public void drawArrays(int firstVert, int numVerts) {
        validateUncompiled();
        commands.add(new CallArgs.Int2(DrawCommands.DRAW_ARRAYS, firstVert, numVerts));
    }

    @Override
    public void drawElements(int firstVert, int numVerts, NumericPrimitive indexType) {
        validateUncompiled();
        commands.add(new CallArgs.Int3(DrawCommands.DRAW_ELEMENTS, firstVert, numVerts, indexType.ordinal()));
    }

    @Override
    public void drawArraysInstanced(int firstVert, int numVerts, int instances) {
        validateUncompiled();
        commands.add(new CallArgs.Int3(DrawCommands.DRAW_ARRAYS_INSTANCED, firstVert, numVerts, instances));
    }

    @Override
    public void drawElementsInstanced(int firstVert, int numVerts, int instances, NumericPrimitive indexType) {
        validateUncompiled();
        commands.add(new CallArgs.Int4(DrawCommands.DRAW_ELEMENTS_INSTANCED, firstVert, numVerts, instances, indexType.ordinal()));
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        validateUncompiled();
        commands.add(new CallArgs.Int4(DrawCommands.VIEWPORT, x, y, width, height));
    }

    @Override
    public void setScissor(int x, int y, int width, int height) {
        validateUncompiled();
        commands.add(new CallArgs.Int4(DrawCommands.SCISSOR, x, y, width, height));
    }

    @Override
    public void useShader(ShaderProgram program) {
        validateUncompiled();
        commands.add(new CallArgs.Obj1(DrawCommands.BIND_SHADER, program));
    }

    @Override
    public void clearShader() {
        validateUncompiled();
        commands.add(new CallArgs.NoArg(DrawCommands.UNBIND_SHADER));
    }

    @Override
    public void setUniform(UniformAccessor accessor) {
        validateUncompiled();
        ByteBuffer raw = ((OGLUniformAccessor) accessor).getRaw();
        ByteBuffer cpy = MemoryUtil.memAlloc(raw.capacity());
        Util.bufferCopy(raw, cpy);
        __resources.add(cpy);
        commands.add(new CallArgs.Obj2(DrawCommands.SET_UNIFORM, accessor, cpy));
    }

    @Override
    public void setBufferData(GPUBuffer buffer, ByteBuffer data) {
        validateUncompiled();
        ByteBuffer cpy = MemoryUtil.memAlloc(data.limit() - data.position());
        Util.bufferCopy(data, cpy);
        __resources.add(cpy);
        commands.add(new CallArgs.Obj2(DrawCommands.SET_BUFFER_DATA, buffer, cpy));
    }

    @Override
    public void setBufferSubData(GPUBuffer buffer, ByteBuffer data, int start) {
        validateUncompiled();
        ByteBuffer cpy = MemoryUtil.memAlloc(data.limit() - data.position());
        Util.bufferCopy(data, cpy);
        __resources.add(cpy);
        commands.add(new CallArgs.Obj2Int1(DrawCommands.SET_BUFFER_SUBDATA, buffer, cpy, start));
    }

    @Override
    public void compile() {
        // TODO: internal optimizations?
        //       i.e. multiple drawArrays/drawElements->multiDrawIndirect
        //            probably use a flag for this one, since this can potentially break stuff depending on shader
        //       i.e. redundancy deduplication (i.e. state is set multiple times in a row -> take only last set)
        //       i.e. fuse commands if possible
        // TODO: immutable list
        // TODO: ARB bindless texture
        // TODO: pre-evaluate shader bindings for uniform data access

        compiled = true;
    }

    @Override
    public void dispatch() {
        if (!compiled) throw new RuntimeException("Cannot dispatch a command buffer before compiling it.");

        ArrayObject ao = null;

        for (CallArgs command : commands) {
            switch (command.command) {
                case START_PASS -> {
                    system.startRenderPass((RenderPass) (((CallArgs.Obj1) command).arg0));
                }
                case END_PASS -> {
                    system.endRenderPass();
                }

                case BIND_VAO -> {
                    system.bindVAO((OGLArrayObject) (((CallArgs.Obj1) command).arg0));
                }
                case UNBIND_VAO -> {
                    system.unbindVAO();
                }

                case BIND_SHADER -> {
                    system.useShader((OGLShaderProgram) (((CallArgs.Obj1) command).arg0));
                }
                case UNBIND_SHADER -> {
                    system.clearShader();
                }
                case SET_UNIFORM -> {
                    CallArgs.Obj2 obj2 = ((CallArgs.Obj2) command);
                    OGLUniformAccessor uniform = ((OGLUniformAccessor) obj2.arg0);
                    Util.bufferCopy(
                            (ByteBuffer) obj2.arg1,
                            uniform.getRaw()
                    );
                    uniform.upload();
                }

                // TODO: compile should create a "STORE_ARRAY" and "RESTORE_ARRAY" operation to avoid excess checking
                // TODO: utilize DSA if available
                // NOTE: for display list backed command buffers, any situation where the binding state of the array object might be inherited needs to be uncompiled
                case SET_BUFFER_DATA -> {
                    ao = system.getArrayObject();
                    system.unbindVAO();

                    CallArgs.Obj2 obj2 = ((CallArgs.Obj2) command);
                    OGLBuffer buffer = (OGLBuffer) obj2.arg0;
                    buffer.setData((ByteBuffer) obj2.arg1);
                }
                case SET_BUFFER_SUBDATA -> {
                    ao = system.getArrayObject();
                    system.unbindVAO();

                    CallArgs.Obj2Int1 obj2int1 = ((CallArgs.Obj2Int1) command);
                    OGLBuffer buffer = (OGLBuffer) obj2int1.arg0;
                    buffer.setSubData((ByteBuffer) obj2int1.arg1, obj2int1.arg2);
                }

                case DRAW_ARRAYS -> {
                    if (ao != null) {
                        ao.activate();
                        ao = null;
                    }
                    CallArgs.Int2 int2 = ((CallArgs.Int2) command);
                    system.drawArrays(int2.arg0, int2.arg1);
                }
                case DRAW_ELEMENTS -> {
                    if (ao != null) {
                        ao.activate();
                        ao = null;
                    }
                    CallArgs.Int3 int3 = ((CallArgs.Int3) command);
                    system.drawElements(int3.arg0, int3.arg1, NumericPrimitive.fromOrdinal(int3.arg2));
                }
                case DRAW_ARRAYS_INSTANCED -> {
                    if (ao != null) {
                        ao.activate();
                        ao = null;
                    }
                    CallArgs.Int3 int3 = ((CallArgs.Int3) command);
                    system.drawArraysInstanced(int3.arg0, int3.arg1, int3.arg2);
                }
                case DRAW_ELEMENTS_INSTANCED -> {
                    if (ao != null) {
                        ao.activate();
                        ao = null;
                    }
                    CallArgs.Int4 int4 = ((CallArgs.Int4) command);
                    system.drawElementsInstanced(int4.arg0, int4.arg1, int4.arg2, NumericPrimitive.fromOrdinal(int4.arg3));
                }

                case SET_DRAW_MODE -> {
                    system.setDrawMode(DrawMode.fromOrdinal(((CallArgs.Int1) command).arg0));
                }
                case SCISSOR -> {
                    CallArgs.Int4 int4 = ((CallArgs.Int4) command);
                    system.setScissor(int4.arg0, int4.arg1, int4.arg2, int4.arg3);
                }
                case VIEWPORT -> {
                    CallArgs.Int4 int4 = ((CallArgs.Int4) command);
                    system.setViewport(int4.arg0, int4.arg1, int4.arg2, int4.arg3);
                }
            }
        }
    }

    private void validateUncompiled() {
        if (compiled) throw new RuntimeException("Cannot build in a compiled command buffer.");
    }

    @Override
    protected void _delete() {
        for (ByteBuffer resource : __resources) {
            MemoryUtil.memFree(resource);
        }
    }

    @Override
    protected void debugSetName() {
        // no-op
    }
}
