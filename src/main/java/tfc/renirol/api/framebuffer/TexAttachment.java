package tfc.renirol.api.framebuffer;

import tfc.renirol.api.enums.FrameBufferAttachment;
import tfc.renirol.api.textures.Texture2D;

class TexAttachment extends FBAttachment {
    public TexAttachment(FrameBufferAttachment attachment, Texture2D texture) {
        super(attachment);
        this.texture = texture;
    }

    @Override
    public void release() {
        // no-op
    }
}
