package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL33;
import tfc.renirol.api.enums.InterpolationMode;
import tfc.renirol.api.enums.RepeatMode;
import tfc.renirol.api.obj.Sampler;
import tfc.renirol.ogl.OGLEnums;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

public class OGLSampler extends Sampler {
    int id;
    OGLGraphicsSystem system;

    public OGLSampler(OGLGraphicsSystem system) {
        id = GL33.glGenSamplers();
    }

    @Override
    protected void _delete() {
        GL33.glDeleteSamplers(id);
    }

    @Override
    protected void debugSetName() {
        system.setDebugName(ObjectType.SAMPLER, id, getName());
    }

    InterpolationMode minFilter = InterpolationMode.NEAREST;
    InterpolationMode minMip = InterpolationMode.LINEAR;
    InterpolationMode magFilter = InterpolationMode.LINEAR;
    InterpolationMode magMip = InterpolationMode.LINEAR;

    @Override
    public Sampler repeatX(RepeatMode mode) {
        GL33.glSamplerParameteri(id, GL33.GL_TEXTURE_WRAP_S, OGLEnums.repeatMode(mode));
        return this;
    }

    @Override
    public Sampler repeatY(RepeatMode mode) {
        GL33.glSamplerParameteri(id, GL33.GL_TEXTURE_WRAP_T, OGLEnums.repeatMode(mode));
        return this;
    }

    @Override
    public Sampler repeatZ(RepeatMode mode) {
        GL33.glSamplerParameteri(id, GL33.GL_TEXTURE_WRAP_R, OGLEnums.repeatMode(mode));
        return this;
    }

    @Override
    public Sampler interpolateUpscale(InterpolationMode mode) {
        magFilter = mode;
        return this;
    }

    @Override
    public Sampler interpolateDownscale(InterpolationMode mode) {
        minFilter = mode;
        return this;
    }

    @Override
    public Sampler mipsUpscale(InterpolationMode mode) {
        magMip = mode;
        return this;
    }

    @Override
    public Sampler mipsDownscale(InterpolationMode mode) {
        minMip = mode;
        return this;
    }

    @Override
    public Sampler applyFilters() {
        GL33.glSamplerParameteri(id, GL33.GL_TEXTURE_MIN_FILTER, OGLEnums.interpolationMode(minFilter, minMip));
        GL33.glSamplerParameteri(id, GL33.GL_TEXTURE_MAG_FILTER, OGLEnums.interpolationMode(magFilter, magMip));
        return this;
    }

    public int id() {
        return id;
    }
}
