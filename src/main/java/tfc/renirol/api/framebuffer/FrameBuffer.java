package tfc.renirol.api.framebuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.KHRDebug;
import tfc.renirol.api.Resource;
import tfc.renirol.api.enums.FrameBufferAttachment;
import tfc.renirol.api.textures.Texture2D;
import tfc.renirol.api.textures.TextureBuilder;
import tfc.renirol.internal.GraphicsSystem;

import java.util.ArrayList;
import java.util.List;

public abstract class FrameBuffer extends Resource<FrameBuffer> {
    protected List<FBAttachment> attachments = new ArrayList<>();

    protected int currentWidth = -1;
    protected int currentHeight = -1;
    boolean dirty = false;

    public FrameBuffer() {
    }

    public FrameBuffer attach(
            FrameBufferAttachment attachment,
            TextureBuilder builder
    ) {
        attachments.add(new TexBuilderAttachment(
                attachment, builder,
                this
        ));
        dirty = true;
        return this;
    }

    public FrameBuffer attach(
            FrameBufferAttachment attachment,
            Texture2D texture
    ) {
        attachments.add(new TexAttachment(
                attachment, texture
        ));
        dirty = true;
        return this;
    }

    public FrameBuffer rebind(
            int index, FrameBufferAttachment attachment,
            TextureBuilder builder
    ) {
        attachments.get(index).release();
        attachments.set(index, new TexBuilderAttachment(
                attachment, builder,
                this
        ));
        dirty = true;
        return this;
    }

    public FrameBuffer rebind(
            int index, FrameBufferAttachment attachment,
            Texture2D texture
    ) {
        attachments.get(index).release();
        attachments.set(index, new TexAttachment(
                attachment, texture
        ));
        dirty = true;
        return this;
    }

    protected abstract void _complete(int width, int height);

    public FrameBuffer complete(int width, int height) {
        if (!matches(width, height)) {
            dirty = true;
        }

        if (dirty) {
            int number = 0;
            for (FBAttachment attachment : attachments) {
                attachment.initialize(width, height, number++);
            }

            _complete(width, height);
            currentWidth = width;
            currentHeight = height;

            dirty = false;
        } else {
            System.err.println("Reinitializing framebuffer which has not changed.");
        }

        return this;
    }

    public boolean matches(int width, int height) {
        return currentWidth == width && currentHeight == height;
    }

    public abstract void bindWrite();

    public abstract void unbindWrite();

    public int getWidth() {
        return currentWidth;
    }

    public int getHeight() {
        return currentHeight;
    }

    @Override
    public FrameBuffer setName(String name) {
        for (FBAttachment attachment : attachments) {
            attachment.setName(name);
        }
        return super.setName(name);
    }
}
