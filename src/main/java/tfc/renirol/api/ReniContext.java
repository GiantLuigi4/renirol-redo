package tfc.renirol.api;

import tfc.renirol.glfw.GlfwWindowSystem;
import tfc.renirol.internal.GraphicsSystem;
import tfc.renirol.internal.WindowSystem;
import tfc.renirol.ogl.OGLGraphicsSystem;

public class ReniContext {
    public final WindowSystem windowSystem;
    public final GraphicsSystem graphicsSystem;
    public final String graphicsApi;

    public ReniContext(WindowSystem windowSystem, GraphicsSystem graphicsSystem, String graphicsApi) {
        this.windowSystem = windowSystem;
        this.graphicsSystem = graphicsSystem;
        this.graphicsApi = graphicsApi;
    }

    public static ReniContext create(String windowing, String graphics) {
        WindowSystem system = switch (windowing) {
            // TODO: auto (detect what windowing "plugins" are present)
            case "glfw" -> GlfwWindowSystem.INSTANCE;
            case "none" -> null;
            default -> throw new RuntimeException("Unknown window system: " + windowing);
        };

        GraphicsSystem graphicsSys = switch (graphics) {
            case "opengl" -> new OGLGraphicsSystem();
            default -> throw new RuntimeException("Unrecognized graphics system: " + graphics);
        };

        return new ReniContext(system, graphicsSys, graphics);
    }

    public ReniWindow makeWindow(String title) {
        return makeWindow(title, 100, 100);
    }

    public ReniWindow makeWindow(String title, int width, int height) {
        ReniWindow window = windowSystem.createWindow(title, width, height);
        windowSystem.installAPI(window, graphicsApi);
        return window;
    }

    public void close() {
        graphicsSystem.close();
    }
}
