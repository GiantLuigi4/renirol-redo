package tfc.renirol.api.textures;

import tfc.renirol.api.enums.CPUFormat;
import tfc.renirol.api.enums.TextureFormat;

import java.nio.ByteBuffer;

public abstract class Texture1D extends BaseTexture {
    int length;

    public Texture1D(TextureFormat format, int length) {
        super(format);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public abstract void setContent(ByteBuffer buffer, CPUFormat cpuFormat);
}
