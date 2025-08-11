package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL30;
import tfc.renirol.api.shader.ShaderObject;
import tfc.renirol.api.shader.ShaderProgram;
import tfc.renirol.api.shader.UniformBlock;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;
import tfc.renirol.ogl.util.OGLRootBlock;

public class OGLShaderProgram extends ShaderProgram {
    int id;

    OGLGraphicsSystem system;
    OGLRootBlock rootBlock ;

    public OGLShaderProgram(OGLGraphicsSystem system) {
        id = GL30.glCreateProgram();
        this.system = system;
        rootBlock = new OGLRootBlock(system, this);
    }

    @Override
    public ShaderProgram attach(ShaderObject object) {
        if (!object.isCompiled()) {
            throw new RuntimeException(new IllegalStateException("Shader object being attached is not compiled."));
        }
        GL30.glAttachShader(id, ((OGLShaderObject) object).id());
        return this;
    }

    @Override
    public ShaderProgram detach(ShaderObject object) {
        GL30.glDetachShader(id, ((OGLShaderObject) object).id());
        return this;
    }

    @Override
    public ShaderProgram link() {
        GL30.glLinkProgram(id);
        if (GL30.glGetProgrami(id, GL30.GL_LINK_STATUS) != GL30.GL_TRUE) {
            System.err.println(GL30.glGetProgramInfoLog(id));
            throw new RuntimeException(new IllegalStateException("Failed to link shader program."));
        }
        return this;
    }

    @Override
    public ShaderProgram validate() {
        GL30.glValidateProgram(id);
        if (GL30.glGetProgrami(id, GL30.GL_VALIDATE_STATUS) != GL30.GL_TRUE) {
            String log = GL30.glGetProgramInfoLog(id);
            System.err.println(log);
        }
        return this;
    }

    @Override
    public void _delete() {
        GL30.glDeleteProgram(id);
    }

    public int id() {
        return id;
    }

    @Override
    protected void debugSetName() {
        if (id != -1) system.setDebugName(ObjectType.SHADER_PROGRAM, id, getName());
    }

    @Override
    public UniformBlock rootBlock() {
        return rootBlock;
    }
}
