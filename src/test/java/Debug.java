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
import tfc.renirol.api.obj.VertexAttribute;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.shader.UniformAccessor;

import java.nio.ByteBuffer;

public class Debug {
    public static void main(String[] args) {
        System.out.println(ProcessHandle.current().pid());
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
                        out vec3 color;
                        void main() {
                            gl_Position = vec4(pos, 1.0);
                            color = (pos + 1) / 2;
                            color.b = 1 - max(color.r, color.g);
                        }
                        """)
                .compile().setName("Vertex Shader");
        ShaderObject fsh = context.graphicsSystem.shader(ShaderType.FRAGMENT)
                .withSource("""
                        #version 330 core
                        #extension GL_ARB_gpu_shader_int64 : require
                        #extension GL_AMD_gpu_shader_int64 : enable
                        #extension GL_ARB_shader_clock : require
                        in vec3 color;
                        out vec4 fc;
                        uniform vec4 mult;
                        void main() {
                            uint64_t t = clockARB();
                            fc = vec4(round(color), 1.0);
                            uint64_t at = clockARB();
                            float tm = (float(at - t) - 51) / 10.0;
                            fc = vec4(tm, tm, tm, 1.0) * mult;
                        }
                        """)
                .compile().setName("Fragment Shader");
        ShaderProgram program = context.graphicsSystem.shaderProgram()
                .attach(vsh)
                .attach(fsh)
                .link().setName("Shader Program");

        UniformAccessor accessor = program
                .rootBlock().byName("mult")
                .specifyPrimitive(NumericPrimitive.FLOAT, 4);

        context.graphicsSystem.useShader(program);
        accessor.setFloats(1, 1, 0.5f, 1)
                .upload();
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
            context.graphicsSystem.debugGroup("Test Group", 0);
            context.graphicsSystem.debugEvent("Test Event", 0);
            context.graphicsSystem.drawArrays(6);
            context.graphicsSystem.exitGroup();
            context.graphicsSystem.clearShader();
            ao.deactivate();

            fbo.unbindWrite();

            context.graphicsSystem.preparePresentation(fbo, w, h);
            mainWindow.present();
        }

        fbo.delete();
        vsh.delete();
        fsh.delete();
        program.delete();
        ao.delete();
        buffer.delete();

        context.close();
    }
}
