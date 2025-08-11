package tfc.renirol.ogl.debug;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.KHRDebug;

public abstract class DebugMechanism {
    public abstract void setDebugName(ObjectType type, int id, String name);
}
