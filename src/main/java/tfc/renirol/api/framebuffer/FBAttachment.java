package tfc.renirol.api.framebuffer;

import tfc.renirol.api.enums.FrameBufferAttachment;
import tfc.renirol.api.textures.Texture2D;

public abstract class FBAttachment {
    public final FrameBufferAttachment attachment;
    protected Texture2D texture;

    public FBAttachment(FrameBufferAttachment attachment) {
        this.attachment = attachment;
    }

    public void initialize(int width, int height, int number) {
        if (width != texture.getWidth() || height != texture.getHeight()) {
            throw new RuntimeException(new IllegalStateException("Attempted to initialize framebuffer with incorrectly sized texture."));
        }
    }

    public Texture2D getTexture() {
        return texture;
    }

    public abstract void release();

    public void setName(String name) {
        // no-op
    }
}
