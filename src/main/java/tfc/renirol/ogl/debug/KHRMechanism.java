package tfc.renirol.ogl.debug;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.KHRDebug;

public class KHRMechanism extends DebugMechanism {
    @Override
    public void setDebugName(ObjectType type, int id, String name) {
        org.lwjgl.opengl.KHRDebug.glObjectLabel(switch (type) {
            case FRAMEBUFFER -> GL30.GL_FRAMEBUFFER;
            case TEXTURE -> GL30.GL_TEXTURE;
            case ARRAY_BUFFER -> GL30.GL_ARRAY_BUFFER;
            case SHADER_OBJECT -> KHRDebug.GL_SHADER;
            case SHADER_PROGRAM -> KHRDebug.GL_PROGRAM;
            case GPU_BUFFER -> KHRDebug.GL_BUFFER;
            case GL_DISPLAY_LIST -> KHRDebug.GL_DISPLAY_LIST;
        }, id, name);
    }
}
