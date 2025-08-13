package tfc.renirol.ogl.cmd;

import org.lwjgl.opengl.GL11;
import tfc.renirol.api.GraphicsCalls;
import tfc.renirol.api.enums.NumericPrimitive;
import tfc.renirol.api.obj.ArrayObject;
import tfc.renirol.ogl.OGLGraphicsSystem;
import tfc.renirol.ogl.debug.ObjectType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// TODO: some funny business needs to be done for proper final state tracking
public class DisplayListCmdBuffer extends GLCmdBuffer {
    List<Section> lists = new ArrayList<>();
    boolean clearsShader = false;
    boolean unbindsArray = false;
    // it appears that compiling a display list with no draw calls is undefined behavior?
    boolean containsDrawCall = false;

    public DisplayListCmdBuffer(OGLGraphicsSystem system) {
        super(system);
    }

    @Override
    public void drawArrays(int firstVert, int numVerts) {
        super.drawArrays(firstVert, numVerts);
        containsDrawCall = true;
    }

    @Override
    public void drawElements(int firstVert, int numVerts, NumericPrimitive indexType) {
        super.drawElements(firstVert, numVerts, indexType);
        containsDrawCall = true;
    }

    @Override
    public void drawArraysInstanced(int numVerts, int instances) {
        if (!commands.isEmpty()) lists.add(new Section(commands, containsDrawCall, finalObj));
        finalObj = null;
        commands = new ArrayList<>();
        super.drawArraysInstanced(numVerts, instances);
        lists.add(new Section(commands, false, finalObj));
        commands = new ArrayList<>();
        containsDrawCall = false;
    }

    @Override
    public void drawElementsInstanced(int numVerts, int instances, NumericPrimitive indexType) {
        if (!commands.isEmpty()) lists.add(new Section(commands, containsDrawCall, finalObj));
        finalObj = null;
        commands = new ArrayList<>();
        super.drawElementsInstanced(numVerts, instances, indexType);
        lists.add(new Section(commands, false, finalObj));
        commands = new ArrayList<>();
        containsDrawCall = false;
    }

    @Override
    public void clearShader() {
        super.clearShader();
        clearsShader = true;
    }

    @Override
    public void unbindVAO() {
        super.unbindVAO();
        unbindsArray = true;
    }

    @Override
    public void compile() {
        if (!commands.isEmpty()) lists.add(new Section(commands, containsDrawCall, finalObj));
        finalObj = null;
        containsDrawCall = false;

        int count = lists.size();
        for (int i = 0; i < count - 1; i++) {
            Section section = lists.get(i);
            if (!section.compilable) {
                Section section1 = lists.get(i + 1);
                if (!section1.compilable) {
                    lists.remove(i + 1);
                    section.runnables.addAll(section1.runnables);
                }
                count--;
            }
        }

        for (Section list : lists) {
            list.compile();
        }
    }

    @Override
    public void dispatch() {
        if (!compiled) throw new RuntimeException("Cannot dispatch a command buffer before compiling it.");
        for (Section list : lists) {
            list.dispatch();
        }
        if (unbindsArray) {
            system.unbindVAO();
        }
        if (clearsShader) {
            system.clearShader();
        }
    }

    @Override
    protected void _delete() {
        for (Section list : lists) {
            list.delete();
        }
    }

    @Override
    protected void debugSetName() {
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).setName(i);
        }
    }

    class Section {
        List<Consumer<GraphicsCalls>> runnables;
        boolean compilable;
        int list;
        ArrayObject objOut;

        public Section(List<Consumer<GraphicsCalls>> runnables, boolean compilable, ArrayObject objOut) {
            this.runnables = runnables;
            this.compilable = compilable;
            this.objOut = objOut;
            // no point compiling a group of calls so small
            if (runnables.size() < 2) this.compilable = false;
        }

        public void compile() {
            if (compilable) {
                list = GL11.glGenLists(1);
                GL11.glNewList(list, GL11.GL_COMPILE);
                DisplayListCmdBuffer.super.commands = runnables;
                DisplayListCmdBuffer.super.compiled = false;
                DisplayListCmdBuffer.super.compile();
                DisplayListCmdBuffer.super.dispatch();
                GL11.glEndList();
            } else {
                DisplayListCmdBuffer.super.commands = runnables;
                DisplayListCmdBuffer.super.compiled = false;
                DisplayListCmdBuffer.super.compile();
            }
        }

        public void dispatch() {
            if (compilable) {
                // TODO: glCallLists exists???
                //       not necessarily applicable though
                GL11.glCallList(list);
            } else {
                DisplayListCmdBuffer.super.commands = runnables;
                DisplayListCmdBuffer.super.dispatch();
            }
        }

        public void delete() {
            GL11.glDeleteLists(list, 1);
        }

        public void setName(int i) {
            system.debug.setDebugName(ObjectType.GL_DISPLAY_LIST, list, getName() + " (#" + i + ")");
        }
    }
}
