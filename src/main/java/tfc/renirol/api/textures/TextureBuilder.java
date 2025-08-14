package tfc.renirol.api.textures;

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

    public Texture1D create1D() {
        return system.tex1d(format, width);
    }

    public Texture2D create2D() {
        return system.tex2d(format, width, height);
    }

    public Texture3D create3D() {
        return system.tex3d(format, width, height, depth);
    }
}
