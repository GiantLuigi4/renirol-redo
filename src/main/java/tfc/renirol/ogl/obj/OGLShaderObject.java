package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.KHRDebug;
import tfc.renirol.api.enums.ShaderType;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.internal.GraphicsSystem;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

public class OGLShaderObject extends ShaderObject {
    int id;
    OGLGraphicsSystem system;

    public OGLShaderObject(ShaderType type, OGLGraphicsSystem system) {
        int itype = switch (type) {
            case VERTEX -> GL30.GL_VERTEX_SHADER;
            case FRAGMENT -> GL30.GL_FRAGMENT_SHADER;
            case GEOMETRY -> GL32.GL_GEOMETRY_SHADER;
        };
        id = GL30.glCreateShader(itype);
        this.system = system;
    }

    @Override
    public ShaderObject _withSource(String source) {
        GL30.glShaderSource(id, source);
        return this;
    }

    @Override
    public boolean _compile() {
        GL30.glCompileShader(id);
        if (GL30.glGetShaderi(id, GL30.GL_COMPILE_STATUS) != GL30.GL_TRUE) {
            String log = GL30.glGetShaderInfoLog(id);
            System.err.println(log);
            return false;
        }
        return true;
    }

    @Override
    public void _delete() {
        GL30.glDeleteShader(id);
    }

    public int id() {
        return id;
    }

    @Override
    protected void debugSetName() {
        if (id != -1) system.setDebugName(ObjectType.SHADER_OBJECT, id, getName());
    }
}
