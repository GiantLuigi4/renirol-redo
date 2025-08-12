package tfc.renirol.ogl.cmd;

import org.lwjgl.opengl.GL11;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

public class DisplayListCmdBuffer extends GLCmdBuffer {
    int list;
    boolean clearsShader = false;
    boolean unbindsArray = false;

    public DisplayListCmdBuffer(OGLGraphicsSystem system) {
        super(system);
    }

    @Override
    public void clearShader() {
        super.clearShader();
        clearsShader = true;
    }

    @Override
    public void unbindVAO() {
        super.unbindVAO();
        unbindsArray = true;
    }

    @Override
    public void compile() {
        list = GL11.glGenLists(1);
        GL11.glNewList(list, GL11.GL_COMPILE);
        super.compile();
        super.dispatch();
        GL11.glEndList();
    }

    @Override
    public void dispatch() {
        if (!compiled) throw new RuntimeException("Cannot dispatch a command buffer before compiling it.");
        // TODO: glCallLists exists???
        GL11.glCallList(list);
        if (unbindsArray) {
            system.unbindVAO();
        }
        if (clearsShader) {
            system.clearShader();
        }
    }

    @Override
    protected void _delete() {
        GL11.glDeleteLists(list, 1);
    }

    @Override
    protected void debugSetName() {
        system.debug.setDebugName(ObjectType.GL_DISPLAY_LIST, list, getName());
    }
}
