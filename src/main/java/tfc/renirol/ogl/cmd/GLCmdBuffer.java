package tfc.renirol.ogl.cmd;

import tfc.renirol.api.GraphicsCalls;
import tfc.renirol.api.cmd.CommandBuffer;
import tfc.renirol.api.enums.DrawMode;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.state.RenderPass;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.obj.OGLArrayObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GLCmdBuffer extends CommandBuffer {
    protected List<Consumer<GraphicsCalls>> commands = new ArrayList<>();
    protected OGLGraphicsSystem system;
    protected boolean compiled = false;
    protected ArrayObject finalObj;

    public GLCmdBuffer(OGLGraphicsSystem system) {
        this.system = system;
    }

    @Override
    public void startRenderPass(RenderPass pass) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.startRenderPass(pass);
        });
    }

    @Override
    public void bindVAO(ArrayObject obj) {
        validateUncompiled();
        commands.add((calls) -> {
            system.bindVAO((OGLArrayObject) obj);
        });
        finalObj = obj;
    }

    @Override
    public void unbindVAO() {
        validateUncompiled();
        commands.add((calls) -> {
            system.unbindVAO();
        });
    }

    @Override
    public void setDrawMode(DrawMode mode) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.setDrawMode(mode);
        });
    }

    @Override
    public void drawArrays(int firstVert, int numVerts) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.drawArrays(firstVert, numVerts);
        });
    }

    @Override
    public void drawElements(int firstVert, int numVerts, NumericPrimitive indexType) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.drawElements(firstVert, numVerts, indexType);
        });
    }

    @Override
    public void drawArraysInstanced(int firstVert, int numVerts, int instances) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.drawArraysInstanced(firstVert, numVerts, instances);
        });
    }

    @Override
    public void drawElementsInstanced(int firstVert, int numVerts, int instances, NumericPrimitive indexType) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.drawElementsInstanced(firstVert, numVerts, instances, indexType);
        });
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.setViewport(x, y, width, height);
        });
    }

    @Override
    public void useShader(ShaderProgram program) {
        validateUncompiled();
        commands.add((calls) -> {
            calls.useShader(program);
        });
    }

    @Override
    public void clearShader() {
        validateUncompiled();
        commands.add((calls) -> {
            calls.clearShader();
        });
    }

    @Override
    public void compile() {
        // TODO: internal optimizations?
        //       i.e. multiple drawArrays/drawElements->multiDrawIndirect
        //            probably use a flag for this one, since this can potentially break stuff depending on shader
        //       i.e. redundancy deduplication (i.e. state is set multiple times in a row -> take only last set)
        //       i.e. fuse commands if possible
        // TODO: immutable list
        compiled = true;
    }

    @Override
    public void dispatch() {
        if (!compiled) throw new RuntimeException("Cannot dispatch a command buffer before compiling it.");
        for (Consumer<GraphicsCalls> command : commands) {
            command.accept(system);
        }
    }

    private void validateUncompiled() {
        if (compiled) throw new RuntimeException("Cannot build in a compiled command buffer.");
    }

    @Override
    protected void _delete() {
        // no-op
    }

    @Override
    protected void debugSetName() {
        // no-op
    }
}
