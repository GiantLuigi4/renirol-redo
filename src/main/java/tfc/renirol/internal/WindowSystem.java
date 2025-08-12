package tfc.renirol.internal;

import tfc.renirol.api.ReniWindow;

public abstract class WindowSystem {
    public abstract ReniWindow createWindow(String title, int width, int height);
    public abstract void installAPI(ReniWindow window, String api);
    public abstract void legacyOpt(boolean legacyOpt) ;
}
