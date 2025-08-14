package tfc.renirol.ogl.debug;

public class NoDebugMechanism extends DebugMechanism {
    @Override
    public void setDebugName(ObjectType type, int id, String name) {
    }

    @Override
    public void debugSection(String name, int color) {
    }

    @Override
    public void exitSection() {
    }

    @Override
    public void debugEvent(String name, int color) {
    }
}
