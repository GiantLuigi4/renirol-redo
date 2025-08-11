package tfc.renirol.api.shader;

import tfc.renirol.api.Resource;

public abstract class ShaderProgram extends Resource<ShaderProgram> {
    public ShaderProgram() {
    }

    public abstract ShaderProgram attach(ShaderObject object);

    public abstract ShaderProgram detach(ShaderObject object);

    public abstract ShaderProgram link();

    public abstract ShaderProgram validate();

    public abstract UniformBlock rootBlock();
}
