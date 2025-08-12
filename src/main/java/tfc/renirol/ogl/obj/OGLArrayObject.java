package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.KHRDebug;
import tfc.renirol.api.enums.AdvanceRate;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.api.obj.GPUBuffer;
import tfc.renirol.api.obj.VertexAttribute;
import tfc.renirol.ogl.OGLEnums;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

public class OGLArrayObject extends ArrayObject {
    OGLGraphicsSystem system;
    int id;

    public OGLArrayObject(OGLGraphicsSystem system) {
        this.system = system;
        this.id = GL30.glGenVertexArrays();
    }

    @Override
    public void bind(GPUBuffer vertex) {
        system.bindVAO(this);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, ((OGLBuffer) vertex).id);
    }

    @Override
    public void bind(GPUBuffer vertex, GPUBuffer element) {
        system.bindVAO(this);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, ((OGLBuffer) vertex).id);
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, ((OGLBuffer) element).id);
    }

    public int id() {
        return id;
    }

    @Override
    public void activate() {
        system.bindVAO(this);
    }

    @Override
    public void deactivate() {
        system.unbindVAO();
    }

    @Override
    public ArrayObject withAttribute(int slot, VertexAttribute attribute) {
        GL20.glVertexAttribPointer(
                slot, attribute.elements,
                OGLEnums.map(attribute.primitive),
                attribute.normalized,
                attribute.stride, attribute.base
        );
        if (attribute.rate == AdvanceRate.PER_INSTANCE) {
            // per inst
            GL33.glVertexAttribDivisor(slot, 1);
        } else {
            // per vert
            GL33.glVertexAttribDivisor(slot, 0);
        }
        GL20.glEnableVertexAttribArray(slot);
        return this;
    }

    @Override
    public ArrayObject disableAttribute(int slot) {
        GL20.glDisableVertexAttribArray(slot);
        return this;
    }

    @Override
    public void _delete() {
        system.delVAO(this);
    }

    @Override
    protected void debugSetName() {
        if (id != -1) system.setDebugName(ObjectType.ARRAY_BUFFER, id, getName());
    }
}
