package tfc.renirol.api.shader;

import tfc.renirol.api.Resource;

public abstract class ShaderObject extends Resource<ShaderObject> {
    boolean compiled = false;

    public ShaderObject() {
    }

    protected abstract ShaderObject _withSource(String source);

    public ShaderObject withSource(String source) {
        compiled = false;
        return _withSource(source);
    }

    protected abstract boolean _compile();

    public ShaderObject compile() {
        compiled = _compile();
        return this;
    }

    public boolean isCompiled() {
        return compiled;
    }
}
