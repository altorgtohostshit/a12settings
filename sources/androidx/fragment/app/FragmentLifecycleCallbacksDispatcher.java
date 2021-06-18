package androidx.fragment.app;

import androidx.fragment.app.FragmentManager;
import java.util.concurrent.CopyOnWriteArrayList;

class FragmentLifecycleCallbacksDispatcher {
    private final FragmentManager mFragmentManager;
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<>();

    private static final class FragmentLifecycleCallbacksHolder {
        final FragmentManager.FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;
    }

    FragmentLifecycleCallbacksDispatcher(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0029  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentPreAttached(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.FragmentHostCallback r0 = r0.getHost()
            r0.getContext()
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x001d
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentPreAttached(r3, r1)
        L_0x001d:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x0023:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x003a
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x0036
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x0036
            goto L_0x0023
        L_0x0036:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x003a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentPreAttached(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0029  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentAttached(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.FragmentHostCallback r0 = r0.getHost()
            r0.getContext()
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x001d
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentAttached(r3, r1)
        L_0x001d:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x0023:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x003a
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x0036
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x0036
            goto L_0x0023
        L_0x0036:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x003a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentAttached(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentPreCreated(androidx.fragment.app.Fragment r3, android.os.Bundle r4, boolean r5) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentPreCreated(r3, r4, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r5 == 0) goto L_0x002d
            boolean r4 = r3.mRecursive
            if (r4 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentPreCreated(androidx.fragment.app.Fragment, android.os.Bundle, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentCreated(androidx.fragment.app.Fragment r3, android.os.Bundle r4, boolean r5) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentCreated(r3, r4, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r5 == 0) goto L_0x002d
            boolean r4 = r3.mRecursive
            if (r4 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentCreated(androidx.fragment.app.Fragment, android.os.Bundle, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentActivityCreated(androidx.fragment.app.Fragment r3, android.os.Bundle r4, boolean r5) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentActivityCreated(r3, r4, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r5 == 0) goto L_0x002d
            boolean r4 = r3.mRecursive
            if (r4 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(androidx.fragment.app.Fragment, android.os.Bundle, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentViewCreated(androidx.fragment.app.Fragment r3, android.view.View r4, android.os.Bundle r5, boolean r6) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentViewCreated(r3, r4, r5, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r6 == 0) goto L_0x002d
            boolean r4 = r3.mRecursive
            if (r4 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(androidx.fragment.app.Fragment, android.view.View, android.os.Bundle, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentStarted(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentStarted(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentStarted(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentResumed(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentResumed(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentResumed(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentPaused(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentPaused(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentPaused(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentStopped(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentStopped(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentStopped(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentSaveInstanceState(androidx.fragment.app.Fragment r3, android.os.Bundle r4, boolean r5) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentSaveInstanceState(r3, r4, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r5 == 0) goto L_0x002d
            boolean r4 = r3.mRecursive
            if (r4 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentSaveInstanceState(androidx.fragment.app.Fragment, android.os.Bundle, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentViewDestroyed(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentViewDestroyed(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentDestroyed(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentDestroyed(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentDestroyed(androidx.fragment.app.Fragment, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchOnFragmentDetached(androidx.fragment.app.Fragment r3, boolean r4) {
        /*
            r2 = this;
            androidx.fragment.app.FragmentManager r0 = r2.mFragmentManager
            androidx.fragment.app.Fragment r0 = r0.getParent()
            if (r0 == 0) goto L_0x0014
            androidx.fragment.app.FragmentManager r0 = r0.getParentFragmentManager()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r0.getLifecycleCallbacksDispatcher()
            r1 = 1
            r0.dispatchOnFragmentDetached(r3, r1)
        L_0x0014:
            java.util.concurrent.CopyOnWriteArrayList<androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder> r2 = r2.mLifecycleCallbacks
            java.util.Iterator r2 = r2.iterator()
        L_0x001a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0031
            java.lang.Object r3 = r2.next()
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher$FragmentLifecycleCallbacksHolder r3 = (androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder) r3
            if (r4 == 0) goto L_0x002d
            boolean r0 = r3.mRecursive
            if (r0 != 0) goto L_0x002d
            goto L_0x001a
        L_0x002d:
            androidx.fragment.app.FragmentManager$FragmentLifecycleCallbacks r2 = r3.mCallback
            r2 = 0
            throw r2
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentLifecycleCallbacksDispatcher.dispatchOnFragmentDetached(androidx.fragment.app.Fragment, boolean):void");
    }
}
