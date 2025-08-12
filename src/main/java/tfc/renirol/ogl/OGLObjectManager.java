package tfc.renirol.ogl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import tfc.renirol.api.cmd.CommandBuffer;
import tfc.renirol.api.enums.ShaderType;
import tfc.renirol.api.enums.TextureFormat;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.textures.Texture2D;
import tfc.renirol.api.textures.TextureBuilder;
import tfc.renirol.internal.GraphicsSystem;
import tfc.renirol.ogl.cmd.DisplayListCmdBuffer;
import tfc.renirol.ogl.cmd.GLCmdBuffer;
import tfc.renirol.ogl.debug.*;
import tfc.renirol.ogl.obj.*;

abstract class OGLObjectManager extends GraphicsSystem {
    public OGLObjectManager() {
    }

    public DebugMechanism debug = new NoDebugMechanism();
    boolean supportsDisplayLists = false;

    @Override
    public void start() {
        int numExt = GL30.glGetInteger(GL30.GL_NUM_EXTENSIONS);
        boolean khr_debug = GL.getCapabilities().GL_KHR_debug;
        boolean ext_label = GL.getCapabilities().GL_EXT_debug_label;
        boolean ext_marker = GL.getCapabilities().GL_EXT_debug_marker;
        if (!khr_debug) {
            for (int i = 0; i < numExt; i++) {
                String ext = GL30.glGetStringi(GL30.GL_EXTENSIONS, i);
                if (ext.equals("GL_KHR_debug")) {
                    khr_debug = true;
                    debug = new KHRMechanism();
                    break;
                } else if (ext.equals("GL_EXT_debug_label")) {
                    ext_label = true;
                }
                // TODO: can I... detect the ext debug mechanisms this way..?
            }

            if (!khr_debug)
                debug = new EXTMechanism(
                        ext_label || GL.getCapabilities().glLabelObjectEXT != 0,
                        ext_marker || GL.getCapabilities().glPushGroupMarkerEXT != 0
                );
        } else {
            debug = new KHRMechanism();
        }

        supportsDisplayLists = GL.getCapabilities().glCallLists != 0;
    }

    @Override
    public GPUBuffer createBuffer() {
        return bindObject(new OGLBuffer((OGLGraphicsSystem) this));
    }

    @Override
    public ArrayObject createArrayObject() {
        return bindObject(new OGLArrayObject((OGLGraphicsSystem) this));
    }

    OGLBuffer ARRAY_BUFFER;

    public OGLBuffer boundArray() {
        return ARRAY_BUFFER;
    }

    public void bindArrayBuffer(OGLBuffer buffer) {
        if (ARRAY_BUFFER == buffer) return;
        if (ARRAY_OBJECT != null)
            throw new IllegalStateException("Cannot access vertex buffer states while a VAO is bound.");

        ARRAY_BUFFER = buffer;
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, buffer.id());
    }

    public void unbindArrayBuffer() {
        ARRAY_BUFFER = null;
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }

    public void delBuffer(OGLBuffer buffer) {
        if (buffer == ARRAY_BUFFER) ARRAY_BUFFER = null;
        GL30.glDeleteBuffers(buffer.id());
    }

    OGLArrayObject ARRAY_OBJECT;

    public void bindVAO(OGLArrayObject arrayObject) {
        if (arrayObject == ARRAY_OBJECT) return;

        ARRAY_OBJECT = arrayObject;
        GL30.glBindVertexArray(arrayObject.id());
    }

    public void unbindVAO() {
        ARRAY_OBJECT = null;
        GL30.glBindVertexArray(0);
    }

    public void delVAO(OGLArrayObject vao) {
        if (vao == ARRAY_OBJECT) ARRAY_OBJECT = null;
        GL30.glDeleteTextures(vao.id());
    }

    OGLTex2D TEXTURE_2D;

    public void bindTex(OGLTex2D tex2D) {
        if (tex2D == TEXTURE_2D) return;

        TEXTURE_2D = tex2D;
        GL30.glBindTexture(GL20.GL_TEXTURE_2D, tex2D.id());
    }

    public void unbindTex() {
        TEXTURE_2D = null;
        GL30.glBindTexture(GL20.GL_TEXTURE_2D, 0);
    }

    public void delTex(OGLTex2D tex) {
        if (tex == TEXTURE_2D) TEXTURE_2D = null;
        GL30.glDeleteTextures(tex.id());
    }

    OGLFBO GENERAL_FBO;

    public void bindFBO(OGLFBO glfbo) {
        if (glfbo == GENERAL_FBO) return;

        GENERAL_FBO = glfbo;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, glfbo.id());
    }

    public void unbindFBO() {
        GENERAL_FBO = null;
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void delFBO(OGLFBO fb) {
        if (fb == GENERAL_FBO) GENERAL_FBO = null;
        GL30.glDeleteFramebuffers(fb.id());
    }

    @Override
    public TextureBuilder textureBuilder() {
        return new TextureBuilder(this);
    }

    @Override
    public Texture2D tex2d(TextureFormat format, int width, int height) {
        return bindObject(new OGLTex2D((OGLGraphicsSystem) this, format, width, height));
    }

    @Override
    public FrameBuffer framebuffer() {
        return bindObject(new OGLFBO((OGLGraphicsSystem) this));
    }

    @Override
    public ShaderObject shader(ShaderType type) {
        return bindObject(new OGLShaderObject(type, (OGLGraphicsSystem) this));
    }

    @Override
    public ShaderProgram shaderProgram() {
        return bindObject(new OGLShaderProgram((OGLGraphicsSystem) this));
    }

    public void setDebugName(ObjectType type, int id, String name) {
        debug.setDebugName(type, id, name);
    }

    @Override
    public CommandBuffer commandBuffer() {
        if (supportsDisplayLists) {
            return new DisplayListCmdBuffer((OGLGraphicsSystem) this);
        } else {
            return new GLCmdBuffer((OGLGraphicsSystem) this);
        }
    }
}
