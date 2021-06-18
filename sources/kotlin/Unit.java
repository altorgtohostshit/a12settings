package kotlin;

import org.jetbrains.annotations.NotNull;

/* compiled from: Unit.kt */
public final class Unit {
    @NotNull
    public static final Unit INSTANCE = new Unit();

    @NotNull
    public String toString() {
        return "kotlin.Unit";
    }

    private Unit() {
    }
}
