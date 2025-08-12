package tfc.renirol.api;

import tfc.renirol.api.enums.DrawMode;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.state.RenderPass;

public abstract class GraphicsCalls {
    public abstract void startRenderPass(RenderPass pass);

    public abstract void setDrawMode(DrawMode mode);

    public void drawArrays(int numVerts) {
        drawArrays(0, numVerts);
    }

    public abstract void drawArrays(int firstVert, int numVerts);

    public void drawElements(int numVerts, NumericPrimitive indexType) {
        drawElements(0, numVerts, indexType);
    }

    public abstract void drawElements(int firstVert, int numVerts, NumericPrimitive indexType);

    public abstract void setViewport(int x, int y, int width, int height);

    public abstract void useShader(ShaderProgram program);

    public abstract void clearShader();
}
