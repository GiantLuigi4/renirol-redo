package tfc.renirol.internal;

import tfc.renirol.api.GraphicsCalls;
import tfc.renirol.api.Resource;
import tfc.renirol.api.cmd.CommandBuffer;
import tfc.renirol.api.enums.ShaderType;
import tfc.renirol.api.enums.TextureFormat;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.obj.Sampler;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.textures.Texture1D;
import tfc.renirol.api.textures.Texture2D;
import tfc.renirol.api.textures.Texture3D;
import tfc.renirol.api.textures.TextureBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * All objects have an auto-binding contract when operations which require binding are called
 */
public abstract class GraphicsSystem implements GraphicsCalls {
    List<Resource> resources = new ArrayList<>();

    public abstract void start();

    public abstract GPUBuffer createBuffer();

    public abstract ArrayObject createArrayObject();

    public abstract TextureBuilder textureBuilder();

    public abstract Texture1D tex1d(TextureFormat format, int length);

    public abstract Texture2D tex2d(TextureFormat format, int width, int height);

    public abstract Texture3D tex3d(TextureFormat format, int width, int height, int depth);

    public abstract Sampler sampler();

    public abstract FrameBuffer framebuffer();

    public abstract ShaderObject shader(ShaderType type);

    public abstract ShaderProgram shaderProgram();

    public abstract CommandBuffer commandBuffer();

    /**
     * Unbinds any active framebuffer and prepares the given framebuffer's contents for presentation (display)
     *
     * @param fbo the framebuffer to display
     */
    public abstract void preparePresentation(FrameBuffer fbo, int w, int h);

    public <T extends Resource> T bindObject(T resource) {
        resources.add(resource);
        resource.setUnbind(this.resources::remove);
        return resource;
    }

    public void close() {
        List<Resource> copy = new ArrayList<>(resources);
        for (Resource resource : copy) {
            resource.logDeletion();
            resource.delete();
        }
    }
}
