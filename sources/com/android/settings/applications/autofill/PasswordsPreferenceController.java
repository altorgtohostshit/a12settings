package com.android.settings.applications.autofill;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.autofill.AutofillServiceInfo;
import android.service.autofill.IAutoFillService;
import android.text.TextUtils;
import android.util.IconDrawableFactory;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.IResultReceiver;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.AppPreference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PasswordsPreferenceController extends BasePreferenceController implements LifecycleObserver {
    private static final String TAG = "AutofillSettings";
    private final IconDrawableFactory mIconFactory;
    private LifecycleOwner mLifecycleOwner;
    private final PackageManager mPm;
    private final List<AutofillServiceInfo> mServices;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public PasswordsPreferenceController(Context context, String str) {
        this(context, str, AutofillServiceInfo.getAvailableServices(context, UserHandle.myUserId()));
    }

    @VisibleForTesting
    public PasswordsPreferenceController(Context context, String str, List<AutofillServiceInfo> list) {
        super(context, str);
        this.mPm = context.getPackageManager();
        this.mIconFactory = IconDrawableFactory.newInstance(this.mContext);
        for (int size = list.size() - 1; size >= 0; size--) {
            if (TextUtils.isEmpty(list.get(size).getPasswordsActivity())) {
                list.remove(size);
            }
        }
        this.mServices = list;
    }

    /* access modifiers changed from: package-private */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    public int getAvailabilityStatus() {
        return this.mServices.isEmpty() ? 2 : 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        addPasswordPreferences(preferenceScreen.getContext(), UserHandle.myUserId(), (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey()));
    }

    private void addPasswordPreferences(Context context, int i, PreferenceGroup preferenceGroup) {
        for (int i2 = 0; i2 < this.mServices.size(); i2++) {
            AutofillServiceInfo autofillServiceInfo = this.mServices.get(i2);
            AppPreference appPreference = new AppPreference(context);
            ServiceInfo serviceInfo = autofillServiceInfo.getServiceInfo();
            appPreference.setTitle(serviceInfo.loadLabel(this.mPm));
            appPreference.setIcon(Utils.getSafeIcon(this.mIconFactory.getBadgedIcon(serviceInfo, serviceInfo.applicationInfo, i)));
            appPreference.setIntent(new Intent("android.intent.action.MAIN").setClassName(serviceInfo.packageName, autofillServiceInfo.getPasswordsActivity()));
            MutableLiveData mutableLiveData = new MutableLiveData();
            mutableLiveData.observe(this.mLifecycleOwner, new PasswordsPreferenceController$$ExternalSyntheticLambda0(this, appPreference));
            requestSavedPasswordCount(autofillServiceInfo, i, mutableLiveData);
            preferenceGroup.addPreference(appPreference);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addPasswordPreferences$0(AppPreference appPreference, Integer num) {
        appPreference.setSummary((CharSequence) this.mContext.getResources().getQuantityString(R.plurals.autofill_passwords_count, num.intValue(), new Object[]{num}));
    }

    private void requestSavedPasswordCount(AutofillServiceInfo autofillServiceInfo, int i, MutableLiveData<Integer> mutableLiveData) {
        Intent component = new Intent("android.service.autofill.AutofillService").setComponent(autofillServiceInfo.getServiceInfo().getComponentName());
        AutofillServiceConnection autofillServiceConnection = new AutofillServiceConnection(this.mContext, mutableLiveData);
        if (this.mContext.bindServiceAsUser(component, autofillServiceConnection, 1, UserHandle.of(i))) {
            autofillServiceConnection.mBound.set(true);
            this.mLifecycleOwner.getLifecycle().addObserver(autofillServiceConnection);
        }
    }

    private static class AutofillServiceConnection implements ServiceConnection, LifecycleObserver {
        final AtomicBoolean mBound = new AtomicBoolean();
        final WeakReference<Context> mContext;
        final MutableLiveData<Integer> mData;

        public void onServiceDisconnected(ComponentName componentName) {
        }

        AutofillServiceConnection(Context context, MutableLiveData<Integer> mutableLiveData) {
            this.mContext = new WeakReference<>(context);
            this.mData = mutableLiveData;
        }

        public void onServiceConnected(final ComponentName componentName, IBinder iBinder) {
            IAutoFillService asInterface = IAutoFillService.Stub.asInterface(iBinder);
            Log.d(PasswordsPreferenceController.TAG, "Fetching password count from " + componentName);
            try {
                asInterface.onSavedPasswordCountRequest(new IResultReceiver.Stub() {
                    public void send(int i, Bundle bundle) {
                        Log.d(PasswordsPreferenceController.TAG, "Received password count result " + i + " from " + componentName);
                        if (i == 0 && bundle != null) {
                            AutofillServiceConnection.this.mData.postValue(Integer.valueOf(bundle.getInt("result")));
                        }
                        AutofillServiceConnection.this.unbind();
                    }
                });
            } catch (RemoteException e) {
                Log.e(PasswordsPreferenceController.TAG, "Failed to fetch password count: " + e);
            }
        }

        /* access modifiers changed from: package-private */
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void unbind() {
            Context context;
            if (this.mBound.getAndSet(false) && (context = (Context) this.mContext.get()) != null) {
                context.unbindService(this);
            }
        }
    }
}
