package tfc.renirol.ogl.cmd;

public class CallArgs {
    public final DrawCommands command;

    protected CallArgs(DrawCommands command) {
        this.command = command;
    }

    public static class NoArg extends CallArgs {
        public NoArg(DrawCommands command) {
            super(command);
        }
    }

    public static class Obj1 extends CallArgs {
        public final Object arg0;

        public Obj1(DrawCommands command, Object arg0) {
            super(command);
            this.arg0 = arg0;
        }
    }

    public static class Obj2 extends CallArgs {
        public final Object arg0, arg1;

        public Obj2(DrawCommands command, Object arg0, Object arg1) {
            super(command);
            this.arg0 = arg0;
            this.arg1 = arg1;
        }
    }

    public static class Obj2Int1 extends CallArgs {
        public final Object arg0, arg1;
        public final int arg2;

        public Obj2Int1(DrawCommands command, Object arg0, Object arg1, int arg2) {
            super(command);
            this.arg0 = arg0;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    public static class Int1 extends CallArgs {
        public final int arg0;

        public Int1(DrawCommands command, int arg0) {
            super(command);
            this.arg0 = arg0;
        }
    }

    public static class Int2 extends CallArgs {
        public final int arg0, arg1;

        public Int2(DrawCommands command, int arg0, int arg1) {
            super(command);
            this.arg0 = arg0;
            this.arg1 = arg1;
        }
    }

    public static class Int3 extends CallArgs {
        public final int arg0, arg1, arg2;

        public Int3(DrawCommands command, int arg0, int arg1, int arg2) {
            super(command);
            this.arg0 = arg0;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    public static class Int4 extends CallArgs {
        public final int arg0, arg1, arg2, arg3;

        public Int4(DrawCommands command, int arg0, int arg1, int arg2, int arg3) {
            super(command);
            this.arg0 = arg0;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }
    }
}
