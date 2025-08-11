package tfc.renirol.api;

import tfc.renirol.internal.GraphicsSystem;

import java.util.function.Consumer;

public abstract class Resource<T> {
    String name;
    Consumer<Resource<T>> unbind;

    public String getName() {
        return name;
    }

    public T setName(String name) {
        this.name = name;
        debugSetName();
        return (T) this;
    }

    boolean deleted = false;

    protected abstract void _delete();

    public final void delete() {
        if (!deleted) {
            _delete();
            unbind.accept(this);
            deleted = true;
        }
    }

    public void logDeletion() {
        if (!deleted) {
            String dispName = name;
            if (dispName == null) dispName = getClass().getName();
            System.err.println("Resource \"" + dispName + "\" wasn't freed before leaving scope.");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        logDeletion();
    }

    public void setUnbind(Consumer<Resource<T>> unbind) {
        if (this.unbind != null) throw new RuntimeException();
        this.unbind = unbind;
    }

    protected abstract void debugSetName();

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) + ":" + name;
    }
}
