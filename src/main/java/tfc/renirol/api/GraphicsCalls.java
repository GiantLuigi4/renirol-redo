package tfc.renirol.api;

import tfc.renirol.api.enums.DrawMode;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.state.RenderPass;

public interface GraphicsCalls {
    void startRenderPass(RenderPass pass);

    void endRenderPass();

    void setDrawMode(DrawMode mode);

    default void drawArrays(int numVerts) {
        drawArrays(0, numVerts);
    }

    void drawArrays(int firstVert, int numVerts);

    default void drawElements(int numVerts, NumericPrimitive indexType) {
        drawElements(0, numVerts, indexType);
    }

    void drawElements(int firstVert, int numVerts, NumericPrimitive indexType);

    default void drawArraysInstanced(int numVerts, int instances) {
        drawArraysInstanced(0, numVerts, instances);
    }

    void drawArraysInstanced(int firstVert, int numVerts, int instances);

    default void drawElementsInstanced(int numVerts, int instances, NumericPrimitive indexType) {
        drawElementsInstanced(0, numVerts, instances, indexType);
    }

    void drawElementsInstanced(int firstVert, int numVerts, int instances, NumericPrimitive indexType);

    void setViewport(int x, int y, int width, int height);

    void setScissor(int x, int y, int width, int height);

    void useShader(ShaderProgram program);

    void clearShader();

    void debugEvent(String testEvent, int color);

    void debugGroup(String testEvent, int color);

    void exitGroup();
}
