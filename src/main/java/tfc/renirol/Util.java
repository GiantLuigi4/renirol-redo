package tfc.renirol;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class Util {
    public static void bufferCopy(ByteBuffer from, ByteBuffer to) {
//        int initialPos = to.position();
        MemoryUtil.memCopy(from, to);
//        to.position(initialPos);
    }
}
