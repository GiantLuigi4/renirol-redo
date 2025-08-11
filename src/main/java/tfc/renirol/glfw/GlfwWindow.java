package tfc.renirol.glfw;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;
import tfc.renirol.api.ReniWindow;
import tfc.renirol.api.framebuffer.FrameBuffer;
import tfc.renirol.internal.WindowSystem;

public class GlfwWindow extends ReniWindow {
    long handle;

    public GlfwWindow(
            GlfwWindowSystem system,
            String title,
            int width,
            int height
    ) {
        GLFW.glfwDefaultWindowHints();
        system.setupHints();
        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
    }

    @Override
    public void bindContext() {
        GLFW.glfwMakeContextCurrent(handle);
    }

    @Override
    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    @Override
    public void present() {
        GLFW.glfwSwapBuffers(handle);
    }

    @Override
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    @Override
    public int getWidth() {
        int[] w = new int[1];
        GLFW.glfwGetWindowSize(handle, w, null);
        return w[0];
    }

    @Override
    public int getHeight() {
        int[] w = new int[1];
        GLFW.glfwGetWindowSize(handle, null, w);
        return w[0];
    }

    @Override
    public void vsync(boolean b) {
        GLFW.glfwSwapInterval(b ? 1 : 0);
    }
}
