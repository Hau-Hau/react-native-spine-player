package com.spineplayer.spine;

public enum Physics {
    /** Physics are not updated or applied. */
    Physics_None,

    /** Physics are reset to the current pose. */
    Physics_Reset,

    /** Physics are updated and the pose from physics is applied. */
    Physics_Update,

    /** Physics are not updated but the pose from physics is applied. */
    Physics_Pose;

    private static final Physics[] VALUES = values();

    public static Physics getValue(int i) {
        if (i < 0 || i >= VALUES.length) {
            return null;
        }

        return VALUES[i];
    }
}
