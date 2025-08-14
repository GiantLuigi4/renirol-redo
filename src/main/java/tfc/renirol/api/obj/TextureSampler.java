package tfc.renirol.api.obj;

import tfc.renirol.api.textures.BaseTexture;

public class TextureSampler {
    public final BaseTexture texture;
    public final Sampler sampler;

    public TextureSampler(BaseTexture texture, Sampler sampler) {
        this.texture = texture;
        this.sampler = sampler;
    }
}
