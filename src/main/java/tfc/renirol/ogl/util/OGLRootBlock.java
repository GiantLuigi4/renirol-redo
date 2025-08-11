package tfc.renirol.ogl.util;

import org.lwjgl.opengl.GL30;
import tfc.renirol.api.shader.UniformAccessor;
import tfc.renirol.api.shader.UniformBlock;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.obj.OGLShaderProgram;

import java.util.HashMap;
import java.util.Map;

public class OGLRootBlock extends UniformBlock {
    OGLGraphicsSystem system;
    OGLShaderProgram program;
    Map<String, UniformAccessor> uniforms = new HashMap<>();

    public OGLRootBlock(OGLGraphicsSystem system, OGLShaderProgram program) {
        this.system = system;
        this.program = program;
    }

    @Override
    public UniformAccessor byName(String name) {
        UniformAccessor accessor = uniforms.get(name);
        if (accessor == null) {
            int id = program.id();
            int loc = GL30.glGetUniformLocation(id, name);
            accessor = new OGLUniformAccessor(system, program, loc);
        }
        return accessor;
    }
}
