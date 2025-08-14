import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryUtil;
import tfc.renirol.api.ReniContext;
import tfc.renirol.api.ReniWindow;
import tfc.renirol.api.enums.*;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.obj.Sampler;
import tfc.renirol.api.obj.VertexAttribute;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.shader.UniformAccessor;
import tfc.renirol.api.textures.Texture2D;

import java.nio.ByteBuffer;

public class Texture {
    public static void main(String[] args) {
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_LOADER.set(true);

        ReniContext context = ReniContext.create(
                "glfw", "opengl"
        );

        ReniWindow mainWindow = context.makeWindow(
                "Title", 200, 200
        );

        mainWindow.bindContext();
        mainWindow.vsync(true);
        context.graphicsSystem.start();


        System.out.println(GL30.glGetString(GL30.GL_VENDOR));
        System.out.println(GL30.glGetString(GL30.GL_RENDER));
        System.out.println(GL30.glGetString(GL30.GL_VERSION));
        System.out.println(GL30.glGetString(GL30.GL_SHADING_LANGUAGE_VERSION));

        int numExt = GL30.glGetInteger(GL30.GL_NUM_EXTENSIONS);
        for (int i = 0; i < numExt; i++) {
            System.out.println(GL30.glGetStringi(GL30.GL_EXTENSIONS, i));
        }


        GLUtil.setupDebugMessageCallback(System.err);

        ByteBuffer dBuffer = MemoryUtil.memAlloc(6 * 3 * 4);

        dBuffer.putFloat(1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(0);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(0);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(0);

        dBuffer.putFloat(1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(0);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(0);

        dBuffer.putFloat(1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(0);

        dBuffer.position(0);

        ArrayObject ao = context.graphicsSystem.createArrayObject().setName("Mesh");
        GPUBuffer buffer = context.graphicsSystem.createBuffer().setName("Mesh Data");
        buffer.initialize(dBuffer, BufferUsage.GL_STATIC_DRAW);

        // INITIAL STATE
        context.graphicsSystem.setDrawMode(DrawMode.TRIANGLES);

        ao.activate();
        ao.bind(buffer);

        ao
                .withAttribute(0, new VertexAttribute(
                        3, NumericPrimitive.FLOAT, 3 * 4, 0, false
                ));

        ao.deactivate();

        FrameBuffer fbo = context.graphicsSystem.framebuffer();
        fbo
                .attach(
                        FrameBufferAttachment.COLOR,
                        context.graphicsSystem.textureBuilder().setFormat(TextureFormat.RGBA32)
                )
                .attach(
                        FrameBufferAttachment.DEPTH_STENCIL,
                        context.graphicsSystem.textureBuilder().setFormat(TextureFormat.DEPTH32_STENCIL8)
                )
        ;
        fbo.unbindWrite();
        fbo.setName("Main FBO");

        ShaderObject vsh = context.graphicsSystem.shader(ShaderType.VERTEX)
                .withSource("""
                        #version 330 core
                        layout (location = 0) in vec3 pos;
                        out vec2 texC;
                        void main() {
                            gl_Position = vec4(pos, 1.0);
                            texC = vec2((pos.xy + 1) / 2);
                        }
                        """)
                .compile().setName("Vertex Shader");
        ShaderObject fsh = context.graphicsSystem.shader(ShaderType.FRAGMENT)
                .withSource("""
                        #version 330 core
                        in vec2 texC;
                        out vec4 fc;
                        uniform sampler2D tex;
                        void main() {
                            fc = texture2D(tex, texC);
                        }
                        """)
                .compile().setName("Fragment Shader");
        ShaderProgram program = context.graphicsSystem.shaderProgram()
                .attach(vsh)
                .attach(fsh)
                .link().setName("Shader Program");

        UniformAccessor accessor = program
                .rootBlock().byName("tex")
                .specifySampler();

        Texture2D tex = context.graphicsSystem.tex2d(TextureFormat.RGBA8, 2, 2);
        ByteBuffer data = MemoryUtil.memAlloc(4 * 4);
        data.put((byte) -1);
        data.put((byte) 0);
        data.put((byte) -1);
        data.put((byte) -1);
        data.put((byte) 0);
        data.put((byte) 0);
        data.put((byte) 0);
        data.put((byte) -1);
        data.put((byte) 0);
        data.put((byte) 0);
        data.put((byte) 0);
        data.put((byte) -1);
        data.put((byte) -1);
        data.put((byte) 0);
        data.put((byte) -1);
        data.put((byte) -1);
        data.position(0);
        tex.setContent(data, CPUFormat.RGBA);

        Sampler sampler = context.graphicsSystem.sampler();
        sampler
                .repeatX(RepeatMode.REPEAT).repeatY(RepeatMode.REPEAT)
                .interpolate(InterpolationMode.NEAREST).mipmaps(InterpolationMode.NONE)
                .applyFilters();

        context.graphicsSystem.useShader(program);
        accessor.setSampler(sampler.forTexture(tex)).upload();
        context.graphicsSystem.clearShader();

        while (!mainWindow.shouldClose()) {
            mainWindow.pollEvents();

            int w = mainWindow.getWidth();
            int h = mainWindow.getHeight();

            if (!fbo.matches(w, h)) {
                fbo.complete(w, h);
                context.graphicsSystem.setViewport(0, 0, w, h);
                fbo.unbindWrite();
            }

            fbo.bindWrite();

            ao.activate();
            context.graphicsSystem.useShader(program);
            context.graphicsSystem.drawArrays(6);
            context.graphicsSystem.clearShader();
            ao.deactivate();

            fbo.unbindWrite();

            context.graphicsSystem.preparePresentation(fbo, w, h);
            mainWindow.present();
        }

        sampler.delete();
        tex.delete();
        fbo.delete();
        vsh.delete();
        fsh.delete();
        program.delete();
        ao.delete();
        buffer.delete();

        context.close();
    }
}
