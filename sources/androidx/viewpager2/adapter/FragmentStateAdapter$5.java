package androidx.viewpager2.adapter;

import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

class FragmentStateAdapter$5 implements LifecycleEventObserver {
    final /* synthetic */ Handler val$handler;
    final /* synthetic */ Runnable val$runnable;

    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.val$handler.removeCallbacks(this.val$runnable);
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
    }
}
