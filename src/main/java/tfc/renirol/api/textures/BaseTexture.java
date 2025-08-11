package tfc.renirol.api.textures;

import tfc.renirol.api.Resource;
import tfc.renirol.api.enums.TextureFormat;

public abstract class BaseTexture extends Resource {
    TextureFormat format;

    public BaseTexture(TextureFormat format) {
        this.format = format;
    }
}
