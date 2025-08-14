package tfc.renirol.ogl;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import tfc.renirol.api.enums.DrawMode;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.state.RenderPass;
import tfc.renirol.ogl.obj.OGLFBO;
import tfc.renirol.ogl.obj.OGLShaderProgram;

public class OGLGraphicsSystem extends OGLObjectManager {
    @Override
    public void startRenderPass(RenderPass pass) {
        throw new RuntimeException("TODO");
    }

    @Override
    public void endRenderPass() {
        throw new RuntimeException("TODO");
    }

    int drawMode;
    ShaderProgram activeProg;

    @Override
    public void setDrawMode(DrawMode mode) {
        drawMode = switch (mode) {
            case POINTS -> GL20.GL_POINT;
            case LINES -> GL20.GL_LINES;
            case LINE_STRIP -> GL20.GL_LINE_STRIP;
            case TRIANGLES -> GL20.GL_TRIANGLES;
            case TRIANGLE_FAN -> GL20.GL_TRIANGLE_FAN;
            case TRIANGLE_STRIP -> GL20.GL_TRIANGLE_STRIP;
        };
    }

    @Override
    public void drawArrays(int firstVert, int numVerts) {
        GL20.glDrawArrays(drawMode, firstVert, numVerts);
    }

    @Override
    public void drawElements(int numVerts, NumericPrimitive indexType) {
        GL20.glDrawElements(drawMode, numVerts, switch (indexType) {
            case SHORT, USHORT -> GL30.GL_UNSIGNED_SHORT;
            case INT, UINT -> GL30.GL_UNSIGNED_INT;
            case BYTE, UBYTE -> GL30.GL_UNSIGNED_BYTE;
            default -> throw new RuntimeException("Unsupported index type");
        }, 0);
    }

    @Override
    public void drawElements(int firstVert, int numVerts, NumericPrimitive indexType) {
        GL20.glDrawRangeElements(drawMode, firstVert, firstVert + numVerts, numVerts, switch (indexType) {
            case SHORT, USHORT -> GL30.GL_UNSIGNED_SHORT;
            case INT, UINT -> GL30.GL_UNSIGNED_INT;
            case BYTE, UBYTE -> GL30.GL_UNSIGNED_BYTE;
            default -> throw new RuntimeException("Unsupported index type");
        }, 0);
    }

    @Override
    public void drawArraysInstanced(int firstVert, int numVerts, int instances) {
        GL33.glDrawArraysInstanced(drawMode, firstVert, instances, numVerts);
    }

    @Override
    public void drawElementsInstanced(int numVerts, int instances, NumericPrimitive indexType) {
        GL33.glDrawElementsInstanced(drawMode, numVerts, switch (indexType) {
            case SHORT, USHORT -> GL30.GL_UNSIGNED_SHORT;
            case INT, UINT -> GL30.GL_UNSIGNED_INT;
            case BYTE, UBYTE -> GL30.GL_UNSIGNED_BYTE;
            default -> throw new RuntimeException("Unsupported index type");
        }, 0, instances);
    }

    @Override
    public void drawElementsInstanced(int firstVert, int numVerts, int instances, NumericPrimitive indexType) {
        GL33.glDrawElementsInstancedBaseVertex(drawMode, numVerts, switch (indexType) {
            case SHORT, USHORT -> GL30.GL_UNSIGNED_SHORT;
            case INT, UINT -> GL30.GL_UNSIGNED_INT;
            case BYTE, UBYTE -> GL30.GL_UNSIGNED_BYTE;
            default -> throw new RuntimeException("Unsupported index type");
        }, 0, instances, firstVert);
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        GL20.glViewport(x, y, width, height);
    }

    @Override
    public void setScissor(int x, int y, int width, int height) {
        GL20.glScissor(x, y, width, height);
    }

    @Override
    public void useShader(ShaderProgram program) {
        if (activeProg != program) {
            GL30.glUseProgram(((OGLShaderProgram) program).id());
            activeProg = program;
        }
    }

    @Override
    public void clearShader() {
        GL30.glUseProgram(0);
        activeProg = null;
    }

    @Override
    public void preparePresentation(FrameBuffer fbo, int w, int h) {
        GL30.glBindFramebuffer(GL33.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer(GL33.GL_READ_FRAMEBUFFER, ((OGLFBO) fbo).id());

        GL30.glBlitFramebuffer(
                0, 0, fbo.getWidth(), fbo.getHeight(),
                0, 0, w, h,
                GL30.GL_COLOR_BUFFER_BIT,
                GL30.GL_NEAREST
        );

        GL30.glBindFramebuffer(GL33.GL_FRAMEBUFFER, 0);
    }

    public void checkShader(OGLShaderProgram program) {
        if (activeProg != program) {
            throw new RuntimeException("Incorrect shader bound: " + activeProg + " expected " + program);
        }
    }
}
