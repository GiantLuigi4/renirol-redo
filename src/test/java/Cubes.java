import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryUtil;
import tfc.renirol.api.ReniContext;
import tfc.renirol.api.ReniWindow;
import tfc.renirol.api.cmd.CommandBuffer;
import tfc.renirol.api.enums.*;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.obj.VertexAttribute;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.shader.UniformAccessor;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Cubes {
    public static void main(String[] args) {
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_LOADER.set(true);

        ReniContext context = ReniContext.create(
                "glfw", "opengl"
        );
        context.allowLegacyOptimizations();

        ReniWindow mainWindow = context.makeWindow(
                "Title", 200, 200
        );

        mainWindow.bindContext();
        mainWindow.vsync(true);
        context.graphicsSystem.start();


//        System.out.println(GL30.glGetString(GL30.GL_VENDOR));
//        System.out.println(GL30.glGetString(GL30.GL_RENDER));
//        System.out.println(GL30.glGetString(GL30.GL_VERSION));
//        System.out.println(GL30.glGetString(GL30.GL_SHADING_LANGUAGE_VERSION));
//
//        int numExt = GL30.glGetInteger(GL30.GL_NUM_EXTENSIONS);
//        for (int i = 0; i < numExt; i++) {
//            System.out.println(GL30.glGetStringi(GL30.GL_EXTENSIONS, i));
//        }


        GLUtil.setupDebugMessageCallback(System.err);

        ByteBuffer dBuffer = MemoryUtil.memAlloc(8 * 3 * 4);
        ByteBuffer iBuffer = MemoryUtil.memAlloc(36 * 2);

        dBuffer.putFloat(1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(1);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(1);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(1);

        dBuffer.putFloat(1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(1);

        dBuffer.putFloat(1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(-1);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(-1);

        dBuffer.putFloat(-1);
        dBuffer.putFloat(-1);
        dBuffer.putFloat(-1);

        dBuffer.putFloat(1);
        dBuffer.putFloat(1);
        dBuffer.putFloat(-1);

        dBuffer.position(0);

        // Front face
        iBuffer.putShort((short) 0);
        iBuffer.putShort((short) 1);
        iBuffer.putShort((short) 2);
        iBuffer.putShort((short) 0);
        iBuffer.putShort((short) 3);
        iBuffer.putShort((short) 1);

        // Back face
        iBuffer.putShort((short) 4);
        iBuffer.putShort((short) 5);
        iBuffer.putShort((short) 6);
        iBuffer.putShort((short) 4);
        iBuffer.putShort((short) 7);
        iBuffer.putShort((short) 5);

        // Left face
        iBuffer.putShort((short) 2);
        iBuffer.putShort((short) 6);
        iBuffer.putShort((short) 5);
        iBuffer.putShort((short) 2);
        iBuffer.putShort((short) 1);
        iBuffer.putShort((short) 5);

        // Right face
        iBuffer.putShort((short) 0);
        iBuffer.putShort((short) 4);
        iBuffer.putShort((short) 3);
        iBuffer.putShort((short) 3);
        iBuffer.putShort((short) 4);
        iBuffer.putShort((short) 7);

        // Top face
        iBuffer.putShort((short) 3);
        iBuffer.putShort((short) 5);
        iBuffer.putShort((short) 1);
        iBuffer.putShort((short) 3);
        iBuffer.putShort((short) 7);
        iBuffer.putShort((short) 5);

        // Bottom face
        iBuffer.putShort((short) 0);
        iBuffer.putShort((short) 2);
        iBuffer.putShort((short) 6);
        iBuffer.putShort((short) 0);
        iBuffer.putShort((short) 6);
        iBuffer.putShort((short) 4);

        iBuffer.position(0);

        ArrayObject ao = context.graphicsSystem.createArrayObject().setName("Mesh");
        GPUBuffer buffer = context.graphicsSystem.createBuffer().setName("Mesh Data");
        buffer.initialize(dBuffer, BufferUsage.GL_STATIC_DRAW);
        GPUBuffer indices = context.graphicsSystem.createBuffer().setName("Mesh Indices");
        indices.initialize(iBuffer, BufferUsage.GL_STATIC_DRAW);

        // INITIAL STATE
        context.graphicsSystem.setDrawMode(DrawMode.TRIANGLES);

        ao.activate();
        ao.bind(buffer, indices);

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
                        uniform mat4 proj;
                        uniform mat4 model;
                        void main() {
                            vec3 _pos = pos + vec3((gl_InstanceID - 1) * 4, 0, 0);
                            gl_Position = proj * model * vec4(_pos, 1.0);
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
        UniformAccessor matrProjAccess = program
                .rootBlock().byName("proj")
                .specifyMatrix(NumericPrimitive.FLOAT, 4, 4, false);
        UniformAccessor matrModelAccess = program
                .rootBlock().byName("model")
                .specifyMatrix(NumericPrimitive.FLOAT, 4, 4, false);

        context.graphicsSystem.useShader(program);
        accessor.setFloats(1, 1, 0.5f, 1)
                .upload();
        context.graphicsSystem.clearShader();

        FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);

        CommandBuffer drawCube = context.graphicsSystem.commandBuffer().setName("Draw Cube");
        {
            drawCube.setDrawMode(DrawMode.TRIANGLES);
            drawCube.bindVAO(ao);
            drawCube.drawElementsInstanced(36, 3, NumericPrimitive.SHORT);
            drawCube.clearShader();
            drawCube.unbindVAO();
        }
        drawCube.compile();

        while (!mainWindow.shouldClose()) {
            mainWindow.pollEvents();

            int w = mainWindow.getWidth();
            int h = mainWindow.getHeight();

            if (!fbo.matches(w, h)) {
                fbo.complete(w, h);
                context.graphicsSystem.setViewport(0, 0, w, h);
                fbo.unbindWrite();
            }

            Matrix4f projection = new Matrix4f().perspective(
                    (float) Math.toRadians(60.0f), // Field of View (in radians)
                    (float) w / h, // Aspect Ratio
                    0.1f,    // Near plane
                    100.0f   // Far plane
            );
            Matrix4f view = new Matrix4f().lookAt(
                    new Vector3f(5, 5, 5),  // Camera position
                    new Vector3f(0, 0, 0),  // Look at (target point)
                    new Vector3f(0, 1, 0)   // Up direction
            );

            fbo.bindWrite();

            context.graphicsSystem.useShader(program);
            projection.get(matrixBuffer);
            matrProjAccess.setFloats(matrixBuffer).upload();
            view.get(matrixBuffer);
            matrModelAccess.setFloats(matrixBuffer).upload();
            drawCube.dispatch();

//            context.graphicsSystem.useShader(program);
//            context.graphicsSystem.setDrawMode(DrawMode.TRIANGLES);
//            ao.activate();
//            context.graphicsSystem.drawElements(36, NumericPrimitive.SHORT);
//            context.graphicsSystem.drawElementsInstanced(36, 3, NumericPrimitive.SHORT);
//            ao.deactivate();

            fbo.unbindWrite();

            context.graphicsSystem.preparePresentation(fbo, w, h);
            mainWindow.present();
        }

        drawCube.delete();
        fbo.delete();
        vsh.delete();
        fsh.delete();
        program.delete();
        ao.delete();
        buffer.delete();
        indices.delete();

        context.close();
    }
}
