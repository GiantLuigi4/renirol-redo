package tfc.renirol.ogl;

import org.lwjgl.opengl.GL30;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.enums.TextureFormat;

@SuppressWarnings("DuplicateBranchesInSwitch")
public class OGLEnums {
    // internal format param
    public static int map(TextureFormat format) {
        return switch (format) {
            case RGBA8 -> GL30.GL_RGBA8;
            case RGBA16 -> GL30.GL_RGBA16;
            case RGBA32 -> GL30.GL_RGBA32F;
            case RGBA32I -> GL30.GL_RGBA32I;
            case RGBA32UI -> GL30.GL_RGBA32UI;

            case DEPTH16 -> GL30.GL_DEPTH_COMPONENT16;
            case DEPTH24 -> GL30.GL_DEPTH_COMPONENT24;
            case DEPTH32 -> GL30.GL_DEPTH_COMPONENT32;

            case DEPTH24_STENCIL8 -> GL30.GL_DEPTH24_STENCIL8;
            case DEPTH32_STENCIL8 -> GL30.GL_DEPTH32F_STENCIL8;

            case STENCIL1 -> GL30.GL_STENCIL_INDEX1;
            case STENCIL4 -> GL30.GL_STENCIL_INDEX4;
            case STENCIL8 -> GL30.GL_STENCIL_INDEX8;
            case STENCIL16 -> GL30.GL_STENCIL_INDEX16;
        };
    }

    // format param
    public static int bestCPU(TextureFormat format) {
        return switch (format) {
            case RGBA8 -> GL30.GL_RGBA;
            case RGBA16 -> GL30.GL_RGBA;
            case RGBA32 -> GL30.GL_RGBA;
            case RGBA32I -> GL30.GL_RGBA;
            case RGBA32UI -> GL30.GL_RGBA;

            case DEPTH16 -> GL30.GL_DEPTH_COMPONENT;
            case DEPTH24 -> GL30.GL_DEPTH_COMPONENT;
            case DEPTH32 -> GL30.GL_DEPTH_COMPONENT;

            case DEPTH24_STENCIL8 -> GL30.GL_DEPTH_STENCIL;
            case DEPTH32_STENCIL8 -> GL30.GL_DEPTH_STENCIL;

            case STENCIL1 -> GL30.GL_STENCIL_INDEX;
            case STENCIL4 -> GL30.GL_STENCIL_INDEX;
            case STENCIL8 -> GL30.GL_STENCIL_INDEX;
            case STENCIL16 -> GL30.GL_STENCIL_INDEX;
        };
    }

    // type param
    public static int bestPrim(TextureFormat format) {
        return switch (format) {
            case RGBA8 -> GL30.GL_UNSIGNED_BYTE;
            case RGBA16 -> GL30.GL_UNSIGNED_SHORT;
            case RGBA32 -> GL30.GL_FLOAT;
            case RGBA32I -> GL30.GL_INT;
            case RGBA32UI -> GL30.GL_UNSIGNED_INT;

            case DEPTH16 -> GL30.GL_UNSIGNED_SHORT;
            case DEPTH24 -> GL30.GL_UNSIGNED_INT;
            case DEPTH32 -> GL30.GL_UNSIGNED_INT;

            case DEPTH24_STENCIL8 -> GL30.GL_UNSIGNED_INT_24_8;
            case DEPTH32_STENCIL8 -> GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV;

            case STENCIL1 -> GL30.GL_BOOL;
            case STENCIL4 -> GL30.GL_UNSIGNED_BYTE;
            case STENCIL8 -> GL30.GL_UNSIGNED_BYTE;
            case STENCIL16 -> GL30.GL_UNSIGNED_SHORT;
        };
    }

    public static int map(NumericPrimitive primitive) {
        return switch (primitive) {
            case BYTE -> GL30.GL_BYTE;
            case UBYTE -> GL30.GL_UNSIGNED_BYTE;
            case SHORT -> GL30.GL_SHORT;
            case USHORT -> GL30.GL_UNSIGNED_SHORT;
            case INT -> GL30.GL_INT;
            case UINT -> GL30.GL_UNSIGNED_INT;

            case FLOAT -> GL30.GL_FLOAT;
            case DOUBLE -> GL30.GL_DOUBLE;
        };
    }
}
