package com.google.android.settings.survey;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.overlay.SurveyFeatureProvider;
import com.android.settingslib.utils.AsyncLoader;
import com.google.android.libraries.hats20.HatsClient;
import com.google.android.libraries.hats20.HatsDownloadRequest;
import com.google.android.libraries.hats20.HatsShowRequest;
import com.google.android.settings.experiments.GServicesProxy;
import com.google.android.settings.support.PsdValuesLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class SurveyFeatureProviderImpl implements SurveyFeatureProvider, LoaderManager.LoaderCallbacks<HatsDownloadRequest> {
    private Context mContext;

    public void onLoaderReset(Loader<HatsDownloadRequest> loader) {
    }

    public SurveyFeatureProviderImpl(Context context) {
        HatsClient.installCookieHandlerIfNeeded();
        this.mContext = context.getApplicationContext();
    }

    public void downloadSurvey(Activity activity, String str, String str2) {
        Bundle bundle = new Bundle(2);
        bundle.putString("survey_id", str);
        bundle.putString("data", str2);
        if (activity != null && str != null) {
            activity.getLoaderManager().initLoader(20, bundle, this);
        }
    }

    public boolean showSurveyIfAvailable(Activity activity, String str) {
        if (activity != null) {
            return HatsClient.showSurveyIfAvailable(HatsShowRequest.builder(activity).forSiteId(str).build());
        }
        return false;
    }

    public String getSurveyId(Context context, String str) {
        return GServicesProxy.getString(context.getContentResolver(), String.format("settingsgoogle:%s_site_id", new Object[]{str}), (String) null);
    }

    public long getSurveyExpirationDate(Context context, String str) {
        return HatsClient.getSurveyExpirationDate(str, context);
    }

    public BroadcastReceiver createAndRegisterReceiver(Activity activity) {
        if (activity != null) {
            SurveyBroadcastReceiver surveyBroadcastReceiver = new SurveyBroadcastReceiver();
            surveyBroadcastReceiver.setActivity(activity);
            LocalBroadcastManager.getInstance(activity).registerReceiver(surveyBroadcastReceiver, new IntentFilter("com.google.android.libraries.hats20.SURVEY_DOWNLOADED"));
            return surveyBroadcastReceiver;
        }
        throw new IllegalStateException("Cannot register receiver if activity is null.");
    }

    public Loader<HatsDownloadRequest> onCreateLoader(int i, Bundle bundle) {
        return new SurveyProviderLoader(this.mContext, bundle);
    }

    public void onLoadFinished(Loader<HatsDownloadRequest> loader, HatsDownloadRequest hatsDownloadRequest) {
        if (hatsDownloadRequest != null) {
            HatsClient.downloadSurvey(hatsDownloadRequest);
        }
    }

    public static class SurveyProviderLoader extends AsyncLoader<HatsDownloadRequest> {
        private static final Uri PROXY_AUTHORITY = new Uri.Builder().scheme("content").authority("com.google.android.settings.intelligence.provider.adsclientid").build();
        private String mData;
        private String mSurveyId;

        /* access modifiers changed from: protected */
        public void onDiscardResult(HatsDownloadRequest hatsDownloadRequest) {
        }

        public SurveyProviderLoader(Context context, Bundle bundle) {
            super(context);
            this.mSurveyId = bundle.getString("survey_id", (String) null);
            this.mData = bundle.getString("data", (String) null);
        }

        /* access modifiers changed from: package-private */
        @VisibleForTesting
        public String getPayload() {
            StringBuilder sb = new StringBuilder();
            Context context = getContext();
            if (GServicesProxy.getBoolean(context.getContentResolver(), "settingsgoogle:survey_payloads_enabled", false)) {
                for (String append : PsdValuesLoader.makePsdBundle(context, 1).getValues()) {
                    sb.append(append);
                    sb.append(",");
                }
                String str = this.mData;
                if (str != null) {
                    sb.append(str);
                }
                if (sb.length() > 1000) {
                    sb.setLength(1000);
                }
            }
            return sb.toString();
        }

        public HatsDownloadRequest loadInBackground() {
            String adsId = getAdsId();
            if (adsId == null || this.mSurveyId == null) {
                return null;
            }
            return HatsDownloadRequest.builder(getContext().getApplicationContext()).forSiteId(this.mSurveyId).withAdvertisingId(adsId).withSiteContext(getPayload()).build();
        }

        private String getAdsId() {
            try {
                FutureTask futureTask = new FutureTask(new C1870x8c472864(this));
                Executors.newSingleThreadExecutor().submit(futureTask);
                return ((Bundle) futureTask.get(100, TimeUnit.MILLISECONDS)).getString("value", (String) null);
            } catch (Exception e) {
                Log.e("SurveyFeatureProvider", "Failed to query ads id provider", e);
                return null;
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Bundle lambda$getAdsId$0() throws Exception {
            ContentProviderClient acquireUnstableContentProviderClient = getContext().getContentResolver().acquireUnstableContentProviderClient(PROXY_AUTHORITY);
            try {
                Bundle call = acquireUnstableContentProviderClient.call("getAdsClientId", (String) null, (Bundle) null);
                acquireUnstableContentProviderClient.close();
                return call;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }
    }
}
