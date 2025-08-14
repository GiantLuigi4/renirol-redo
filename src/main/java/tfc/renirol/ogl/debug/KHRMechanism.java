package tfc.renirol.ogl.debug;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.KHRDebug;

import java.nio.IntBuffer;

public class KHRMechanism extends DebugMechanism {
    public KHRMechanism() {
        // turn off logging for these
        KHRDebug.glDebugMessageControl(KHRDebug.GL_DEBUG_SOURCE_APPLICATION, KHRDebug.GL_DEBUG_TYPE_MARKER, GL33.GL_DONT_CARE, (IntBuffer) null, false);
        KHRDebug.glDebugMessageControl(KHRDebug.GL_DEBUG_SOURCE_APPLICATION, KHRDebug.GL_DEBUG_TYPE_PUSH_GROUP, GL33.GL_DONT_CARE, (IntBuffer) null, false);
        KHRDebug.glDebugMessageControl(KHRDebug.GL_DEBUG_SOURCE_APPLICATION, KHRDebug.GL_DEBUG_TYPE_POP_GROUP, GL33.GL_DONT_CARE, (IntBuffer) null, false);
    }

    @Override
    public void setDebugName(ObjectType type, int id, String name) {
        org.lwjgl.opengl.KHRDebug.glObjectLabel(switch (type) {
            case FRAMEBUFFER -> GL30.GL_FRAMEBUFFER;
            case TEXTURE -> GL30.GL_TEXTURE;
            case ARRAY_BUFFER -> KHRDebug.GL_BUFFER;
            case SHADER_OBJECT -> KHRDebug.GL_SHADER;
            case SHADER_PROGRAM -> KHRDebug.GL_PROGRAM;
            case GPU_BUFFER -> KHRDebug.GL_BUFFER;
            case GL_DISPLAY_LIST -> KHRDebug.GL_DISPLAY_LIST;
            case SAMPLER -> KHRDebug.GL_SAMPLER;
        }, id, name);
    }

    @Override
    public void debugSection(String name, int color) {
        KHRDebug.glPushDebugGroup(
                KHRDebug.GL_DEBUG_SOURCE_APPLICATION,
                1,
                name
        );
    }

    @Override
    public void exitSection() {
        KHRDebug.glPopDebugGroup();
    }

    @Override
    public void debugEvent(String name, int color) {
        KHRDebug.glDebugMessageInsert(
                KHRDebug.GL_DEBUG_SOURCE_APPLICATION,
                KHRDebug.GL_DEBUG_TYPE_MARKER,
                100,
                KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION,
                name
        );
    }
}
