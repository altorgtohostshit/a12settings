package androidx.constraintlayout.solver;

public class Cache {
    Pools$Pool<ArrayRow> arrayRowPool = new Pools$SimplePool(256);
    Pools$Pool<?> goalVariablePool = new Pools$SimplePool(64);
    SolverVariable[] mIndexedVariables = new SolverVariable[32];
    Pools$Pool<SolverVariable> solverVariablePool = new Pools$SimplePool(256);
}
