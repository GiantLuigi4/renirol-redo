package tfc.renirol.api.obj;

import tfc.renirol.api.Resource;
import tfc.renirol.api.enums.InterpolationMode;
import tfc.renirol.api.enums.RepeatMode;
import tfc.renirol.api.textures.BaseTexture;

public abstract class Sampler extends Resource<Sampler> {
    public abstract Sampler repeatX(RepeatMode mode);

    public abstract Sampler repeatY(RepeatMode mode);

    public abstract Sampler repeatZ(RepeatMode mode);

    public abstract Sampler interpolateUpscale(InterpolationMode mode);

    public abstract Sampler interpolateDownscale(InterpolationMode mode);

    public Sampler interpolate(InterpolationMode mode) {
        return this.interpolateUpscale(mode).interpolateDownscale(mode);
    }

    public abstract Sampler mipsUpscale(InterpolationMode mode);

    public abstract Sampler mipsDownscale(InterpolationMode mode);

    public Sampler mipmaps(InterpolationMode mode) {
        return this.mipsUpscale(mode).mipsDownscale(mode);
    }

    public abstract Sampler applyFilters();

    public TextureSampler forTexture(BaseTexture texture) {
        return new TextureSampler(texture, this);
    }
}
