package tfc.renirol.ogl.obj;

import org.lwjgl.opengl.*;
import tfc.renirol.api.enums.TextureFormat;
import tfc.renirol.api.textures.Texture2D;
import tfc.renirol.ogl.OGLEnums;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

import java.nio.ByteBuffer;

public class OGLTex2D extends Texture2D {
    int id;
    OGLGraphicsSystem graphicsSystem;

    public OGLTex2D(
            OGLGraphicsSystem graphicsSystem, TextureFormat format,
            int width, int height
    ) {
        super(format, width, height);
        id = GL20.glGenTextures();

        graphicsSystem.bindTex(this);

        // default tex params
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_LINEAR);
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_LINEAR);

        GL20.glTexImage2D(
                GL20.GL_TEXTURE_2D, 0, OGLEnums.map(format),
                width, height, 0,
                OGLEnums.bestCPU(format), OGLEnums.bestPrim(format),
                (ByteBuffer) null
        );

        this.graphicsSystem = graphicsSystem;
    }

    @Override
    public void _delete() {
        graphicsSystem.delTex(this);
    }

    public int id() {
        return id;
    }

    @Override
    protected void debugSetName() {
        if (id != -1) graphicsSystem.setDebugName(ObjectType.TEXTURE, id, getName());
    }
}
