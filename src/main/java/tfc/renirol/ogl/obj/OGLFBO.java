package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL30;
import tfc.renirol.api.framebuffer.FBAttachment;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

public class OGLFBO extends FrameBuffer {
    int id = -1;
    OGLGraphicsSystem system;

    public OGLFBO(OGLGraphicsSystem system) {
        this.system = system;
    }

    @Override
    protected void _complete(int width, int height) {
        if (id != -1) {
            system.delFBO(this);
        }
        id = GL30.glGenFramebuffers();

        system.bindFBO(this);
        int colorAttachments = 0;
        boolean depthBound = false;
        boolean stencilBound = false;

        for (FBAttachment attachment : attachments) {
            OGLTex2D tex2D = ((OGLTex2D) attachment.getTexture());
            int texture = tex2D.id();

            int attachmentId;
            switch (attachment.attachment) {
                case COLOR -> {
                    attachmentId = GL30.GL_COLOR_ATTACHMENT0 + colorAttachments;
                    // TODO: validate max color attachments
                    colorAttachments++;
                }
                case DEPTH -> {
                    attachmentId = GL30.GL_DEPTH_ATTACHMENT;
                    if (depthBound) {
                        throw new RuntimeException(new IllegalStateException("Cannot have multiple depth attachments for a single framebuffer."));
                    }
                    depthBound = true;
                }
                case STENCIL -> {
                    attachmentId = GL30.GL_STENCIL_ATTACHMENT;
                    if (stencilBound) {
                        throw new RuntimeException(new IllegalStateException("Cannot have multiple stencil attachments for a single framebuffer."));
                    }
                    stencilBound = true;
                }
                case DEPTH_STENCIL -> {
                    attachmentId = GL30.GL_DEPTH_STENCIL_ATTACHMENT;
                    if (depthBound) {
                        throw new RuntimeException(new IllegalStateException("Cannot have multiple depth attachments for a single framebuffer."));
                    }
                    if (stencilBound) {
                        throw new RuntimeException(new IllegalStateException("Cannot have multiple stencil attachments for a single framebuffer."));
                    }
                    depthBound = true;
                    stencilBound = true;
                }
                default -> throw new RuntimeException(new IllegalStateException("Unrecognized attachment usage."));
            }

            GL30.glFramebufferTexture2D(
                    GL30.GL_FRAMEBUFFER,
                    attachmentId,
                    GL30.GL_TEXTURE_2D,
                    texture,
                    0
            );
        }

        int status;
        if ((status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            // TODO: better logging
            throw new RuntimeException(new IllegalStateException("Framebuffer incomplete: " + error(status)));
        }

        debugSetName();
    }

    public String error(int status) {
        return switch (status) {
            case GL30.GL_FRAMEBUFFER_UNDEFINED -> "GL_FRAMEBUFFER_UNDEFINED - Default framebuffer doesn't exist";
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT ->
                    "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT - Attachment incomplete";
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT ->
                    "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT - No attachments";
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER ->
                    "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER - Missing draw buffer";
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER ->
                    "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER - Missing read buffer";
            case GL30.GL_FRAMEBUFFER_UNSUPPORTED -> "GL_FRAMEBUFFER_UNSUPPORTED - Format combination unsupported";
            case GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE ->
                    "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE - Multisample mismatch";
            default -> "Unknown error (0x" + Integer.toHexString(status) + ")";
        };
    }

    @Override
    public void bindWrite() {
        system.bindFBO(this);
    }

    @Override
    public void unbindWrite() {
        system.unbindFBO();
    }

    public int id() {
        return id;
    }

    public void _delete() {
        system.delFBO(this);
        for (FBAttachment attachment : attachments) {
            attachment.release();
        }
    }

    @Override
    protected void debugSetName() {
        if (id != -1) system.setDebugName(ObjectType.FRAMEBUFFER, id, getName());
    }
}
