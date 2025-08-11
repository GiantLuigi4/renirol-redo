package tfc.renirol.glfw;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import tfc.renirol.api.ReniWindow;
import tfc.renirol.internal.WindowSystem;

public class GlfwWindowSystem extends WindowSystem {
    public static final WindowSystem INSTANCE = new GlfwWindowSystem();

    static {
        GLFW.glfwInit();
    }

    @Override
    public ReniWindow createWindow(String title, int width, int height) {
        return new GlfwWindow(this, title, width, height);
    }

    @Override
    public void installAPI(ReniWindow window, String api) {
        switch (api) {
            case "opengl" -> {
                GLFW.glfwMakeContextCurrent(((GlfwWindow) window).handle);
                GL.createCapabilities();
            }
            default -> throw new RuntimeException("NYI: " + api);
        }
    }

    public void setupHints() {
        // TODO
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE); // Essential!
    }
}
