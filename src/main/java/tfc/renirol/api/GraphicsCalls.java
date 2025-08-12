package tfc.renirol.api;

import tfc.renirol.api.enums.DrawMode;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.state.RenderPass;

public interface GraphicsCalls {
    void startRenderPass(RenderPass pass);

    void setDrawMode(DrawMode mode);

    default void drawArrays(int numVerts) {
        drawArrays(0, numVerts);
    }

    void drawArrays(int firstVert, int numVerts);

    default void drawElements(int numVerts, NumericPrimitive indexType) {
        drawElements(0, numVerts, indexType);
    }

    void drawElements(int firstVert, int numVerts, NumericPrimitive indexType);

    void setViewport(int x, int y, int width, int height);

    void useShader(ShaderProgram program);

    void clearShader();
}
