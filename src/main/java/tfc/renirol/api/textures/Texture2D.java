package tfc.renirol.api.textures;

import tfc.renirol.api.enums.TextureFormat;

public abstract class Texture2D extends BaseTexture {
    int width, height;

    public Texture2D(TextureFormat format, int width, int height) {
        super(format);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
