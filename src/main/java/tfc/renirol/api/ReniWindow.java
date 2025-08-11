package tfc.renirol.api;

public abstract class ReniWindow {
    public abstract void pollEvents();

    public abstract void present();

    public abstract void bindContext();

    public abstract boolean shouldClose();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void vsync(boolean b);
}
