package tfc.renirol.api.textures;

import tfc.renirol.api.enums.CPUFormat;
import tfc.renirol.api.enums.TextureFormat;

import java.nio.ByteBuffer;

public abstract class Texture3D extends BaseTexture {
    int width, height, depth;

    public Texture3D(TextureFormat format, int width, int height, int depth) {
        super(format);
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void setContent(ByteBuffer buffer, CPUFormat cpuFormat);
}
