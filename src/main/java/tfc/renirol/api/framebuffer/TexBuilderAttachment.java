package tfc.renirol.api.framebuffer;

import tfc.renirol.api.enums.FrameBufferAttachment;
import tfc.renirol.api.textures.Texture2D;
import tfc.renirol.api.textures.TextureBuilder;

class TexBuilderAttachment extends FBAttachment {
    public final TextureBuilder builder;
    protected final FrameBuffer owner;

    public TexBuilderAttachment(
            FrameBufferAttachment attachment,
            TextureBuilder builder,
            FrameBuffer owner
    ) {
        super(attachment);
        this.builder = builder;
        this.owner = owner;
    }

    int number;

    @Override
    public void initialize(int width, int height, int number) {
        if (this.texture != null) {
            // resource is invalid; must be deleted
            this.texture.delete();
        }
        builder.setWidth(width).setHeight(height);
        this.texture = builder.create2D();
        this.number = number;
        this.texture.setName(owner.getName() + ".Attachment." + number);
        super.initialize(width, height, number);
    }

    @Override
    public void release() {
        if (this.texture != null) {
            // resource is invalid; must be deleted
            this.texture.delete();
        }
    }

    public void setName(String name) {
        if (this.texture != null) this.texture.setName(owner.getName() + ".Attachment." + number);
    }
}
