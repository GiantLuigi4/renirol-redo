package tfc.renirol.ogl.debug;

public abstract class DebugMechanism {
    public abstract void setDebugName(ObjectType type, int id, String name);

    public abstract void debugSection(String name, int color);

    public abstract void exitSection();

    public abstract void debugEvent(String name, int color);

    public abstract boolean supportsMarkers();

    public abstract boolean supportsGroups();
}
