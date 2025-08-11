package tfc.renirol.ogl.debug;

import org.lwjgl.opengl.EXTDebugLabel;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.KHRDebug;

public class EXTMechanism extends DebugMechanism {
    boolean label, marker;

    public EXTMechanism(boolean label, boolean marker) {
        this.label = label;
        this.marker = marker;
    }

    @Override
    public void setDebugName(ObjectType type, int id, String name) {
        if (!label) return;

        EXTDebugLabel.glLabelObjectEXT(switch (type) {
            case FRAMEBUFFER -> GL30.GL_FRAMEBUFFER;
            case TEXTURE -> GL30.GL_TEXTURE;
            case ARRAY_BUFFER -> EXTDebugLabel.GL_VERTEX_ARRAY_OBJECT_EXT;
            case SHADER_OBJECT -> EXTDebugLabel.GL_SHADER_OBJECT_EXT;
            case SHADER_PROGRAM -> EXTDebugLabel.GL_PROGRAM_OBJECT_EXT;
            case GPU_BUFFER -> EXTDebugLabel.GL_BUFFER_OBJECT_EXT;
        }, id, name);
    }
}
