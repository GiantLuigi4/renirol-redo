package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;
import tfc.renirol.api.enums.CPUFormat;
import tfc.renirol.api.enums.TextureFormat;
import tfc.renirol.api.textures.Texture1D;
import tfc.renirol.ogl.OGLEnums;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

import java.nio.ByteBuffer;

public class OGLTex1D extends Texture1D implements TexID {
    int id;
    int myFormat;
    TextureFormat reniFormat;
    OGLGraphicsSystem graphicsSystem;

    public OGLTex1D(
            OGLGraphicsSystem graphicsSystem, TextureFormat format,
            int length
    ) {
        super(format, length);
        id = GL20.glGenTextures();

        graphicsSystem.bindTex(this);

        // default tex params
        GL20.glTexParameteri(GL20.GL_TEXTURE_1D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
        GL20.glTexParameteri(GL20.GL_TEXTURE_1D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
        GL20.glTexParameteri(GL20.GL_TEXTURE_1D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        GL20.glTexParameteri(GL20.GL_TEXTURE_1D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);

        GL20.glTexImage1D(
                GL20.GL_TEXTURE_1D, 0, myFormat = OGLEnums.map(reniFormat = format),
                length, 0,
                OGLEnums.bestCPU(format), OGLEnums.bestPrim(format),
                (ByteBuffer) null
        );

        this.graphicsSystem = graphicsSystem;

        graphicsSystem.restoreTex();
    }

    @Override
    public void setContent(ByteBuffer buffer, CPUFormat cpuFormat) {
        graphicsSystem.bindTex(this);
        GL33.glBindTexture(GL33.GL_TEXTURE_1D, id);
        GL33.glTexImage1D(
                GL33.GL_TEXTURE_1D,
                0, myFormat,
                getLength(), 0,
                OGLEnums.cpuFormat(cpuFormat),OGLEnums.bestPrim(reniFormat),
                buffer
        );
        graphicsSystem.restoreTex();
    }

    @Override
    public void _delete() {
        graphicsSystem.delTex(this);
    }

    public int id() {
        return id;
    }

    @Override
    public int target() {
        return GL33.GL_TEXTURE_1D;
    }

    @Override
    protected void debugSetName() {
        if (id != -1) graphicsSystem.setDebugName(ObjectType.TEXTURE, id, getName());
    }
}
