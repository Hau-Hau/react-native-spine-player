package com.spineplayer.spine;

import java.util.Objects;

public final class AnimationNamePair {
    private final String a1;
    private final String a2;

    public AnimationNamePair(String a1, String a2) {
        this.a1 = a1;
        this.a2 = a2;
    }

    public String getA1() {
        return a1;
    }

    public String getA2() {
        return a2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimationNamePair that = (AnimationNamePair) o;

        if (!Objects.equals(a1, that.a1)) return false;
        return Objects.equals(a2, that.a2);
    }

    @Override
    public int hashCode() {
        int result = a1 != null ? a1.hashCode() : 0;
        result = 31 * result + (a2 != null ? a2.hashCode() : 0);
        return result;
    }
}
