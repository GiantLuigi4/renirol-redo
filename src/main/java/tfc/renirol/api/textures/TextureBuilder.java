package tfc.renirol.api.textures;

import tfc.renirol.api.ReniContext;
import tfc.renirol.api.enums.TextureFormat;
import tfc.renirol.internal.GraphicsSystem;

public class TextureBuilder {
    TextureFormat format = TextureFormat.RGBA16;
    int width, height, depth;
    GraphicsSystem system;

    public TextureBuilder(GraphicsSystem system) {
        this.system = system;
    }

    public TextureBuilder setFormat(TextureFormat format) {
        this.format = format;
        return this;
    }

    public TextureBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public TextureBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public TextureBuilder setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public Texture2D create2D() {
        return system.tex2d(format, width, height);
    }
}
