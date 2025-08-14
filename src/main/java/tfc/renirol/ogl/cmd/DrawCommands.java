package tfc.renirol.ogl.cmd;

public enum DrawCommands {
    // 1 obj
    START_PASS(false, true),
    // no param
    END_PASS(false, true),

    // 1 obj
    BIND_VAO(true, false),
    // no param
    UNBIND_VAO(true, false),

    // 1 obj
    BIND_SHADER(true, false),
    // no param
    UNBIND_SHADER(true, false),
    // 2 obj
    SET_UNIFORM(false, true, true),

    SET_BUFFER_DATA(false, false),
    SET_BUFFER_SUBDATA(false, false),

    // 1 int
    SET_DRAW_MODE(true, false),
    // 4 ints
    VIEWPORT(true, false),
    SCISSOR(true, false),

    // 2 ints
    DRAW_ARRAYS(false, true),
    // 3 ints
    DRAW_ELEMENTS(false, true),
    DRAW_ARRAYS_INSTANCED(false, true, true),
    // 4 ints
    DRAW_ELEMENTS_INSTANCED(false, true, true),
    DEBUG_EVT(false, true, true),
    DEBUG_GROUP(false, true, true),
    END_GROUP(false, true, true),
    ;

    // indicates that basic deduplication is viable for this operation
    // basic deduplication meaning that, if an operation occurs multiple times between two draw calls, only the last one is meaningful
    // for example, if viewport is set multiple times in a row, the only meaningful one is the last one
    boolean canDedup;

    // indicates whether the call uses the state
    // this is used for behavior preservation
    // for example, when drawArrays is called, the state must be established in a way which is compliant with the command buffer's record
    // this however does not mean that all state setup commands must be performed
    // for example, if the viewport is set multiple times, only the last call to setViewport is meaningful, and thus earlier ones can be dropped
    boolean usesState;

    boolean modern = false;

    // if usesState = false, and canDedup = true, order is assumed to be insignificant
    // elsewise, order is assumed to have some significance
    // modern indicates that the command is a more modern command that doesn't work with legacy optimizations
    DrawCommands(boolean canDedup, boolean usesState) {
        this.canDedup = canDedup;
        this.usesState = usesState;
    }

    DrawCommands(boolean canDedup, boolean usesState, boolean modern) {
        this.canDedup = canDedup;
        this.usesState = usesState;
        this.modern = modern;
    }
}
