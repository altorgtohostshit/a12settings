package com.google.android.libraries.hats20;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.libraries.hats20.network.GcsRequest;
import com.google.android.libraries.hats20.network.GcsResponse;
import com.google.android.libraries.hats20.storage.HatsDataStore;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.atomic.AtomicBoolean;

public class HatsClient {
    private static final AtomicBoolean isSurveyRunning = new AtomicBoolean(false);

    public static void installCookieHandlerIfNeeded() {
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
            Log.d("HatsLibClient", "Installed cookie handler.");
            return;
        }
        Log.d("HatsLibClient", "Attempted to install cookie handler but one was already installed; skipping the install.");
    }

    public static void downloadSurvey(final HatsDownloadRequest hatsDownloadRequest) {
        if ("-1".equals(hatsDownloadRequest.getSiteId())) {
            Log.d("HatsLibClient", "No Site ID set, ignoring download request.");
            return;
        }
        AtomicBoolean atomicBoolean = isSurveyRunning;
        synchronized (atomicBoolean) {
            if (!atomicBoolean.get()) {
                final HatsDataStore buildFromContext = HatsDataStore.buildFromContext(hatsDownloadRequest.getContext());
                buildFromContext.removeSurveyIfExpired(hatsDownloadRequest.getSiteId());
                if (!buildFromContext.surveyExists(hatsDownloadRequest.getSiteId())) {
                    if (!hasInternetPermission(hatsDownloadRequest.getContext())) {
                        Log.e("HatsLibClient", "Application does not have internet permission. Cannot make network request.");
                    } else if (CookieHandler.getDefault() == null) {
                        Log.e("HatsLibClient", "Invalid configuration: Application does not have a cookie jar installed.");
                    } else {
                        buildFromContext.restoreCookiesFromPersistence();
                        final GcsRequest gcsRequest = new GcsRequest(new GcsRequest.ResponseListener() {
                            public void onSuccess(GcsResponse gcsResponse) {
                                Log.d("HatsLibClient", String.format("Site ID %s downloaded with response code: %s", new Object[]{HatsDownloadRequest.this.getSiteId(), Integer.valueOf(gcsResponse.getResponseCode())}));
                                buildFromContext.saveSuccessfulDownload(gcsResponse.getResponseCode(), gcsResponse.expirationDateUnix(), gcsResponse.getSurveyJson(), HatsDownloadRequest.this.getSiteId());
                                HatsClient.sendBroadcast(HatsDownloadRequest.this.getContext(), HatsDownloadRequest.this.getSiteId(), gcsResponse.getResponseCode());
                            }

                            public void onError(Exception exc) {
                                Log.w("HatsLibClient", String.format("Site ID %s failed to download with error: %s", new Object[]{HatsDownloadRequest.this.getSiteId(), exc.toString()}));
                                buildFromContext.saveFailedDownload(HatsDownloadRequest.this.getSiteId());
                            }
                        }, hatsDownloadRequest.computeDownloadUri(), buildFromContext);
                        NetworkExecutor.getNetworkExecutor().execute(new Runnable() {
                            public void run() {
                                GcsRequest.this.send();
                            }
                        });
                    }
                }
            }
        }
    }

    public static long getSurveyExpirationDate(String str, Context context) {
        HatsDataStore buildFromContext = HatsDataStore.buildFromContext(context);
        buildFromContext.removeSurveyIfExpired(str);
        return buildFromContext.getSurveyExpirationDate(str, 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0108, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean showSurveyIfAvailable(com.google.android.libraries.hats20.HatsShowRequest r12) {
        /*
            java.lang.String r0 = "-1"
            java.lang.String r1 = r12.getSiteId()
            boolean r0 = r0.equals(r1)
            r1 = 0
            if (r0 == 0) goto L_0x0015
            java.lang.String r12 = "HatsLibClient"
            java.lang.String r0 = "No Site ID set, ignoring show request."
            android.util.Log.d(r12, r0)
            return r1
        L_0x0015:
            java.util.concurrent.atomic.AtomicBoolean r0 = isSurveyRunning
            monitor-enter(r0)
            boolean r2 = r0.get()     // Catch:{ all -> 0x0166 }
            if (r2 == 0) goto L_0x0027
            java.lang.String r12 = "HatsLibClient"
            java.lang.String r2 = "Attempted to show a survey while another one was already running, bailing out."
            android.util.Log.d(r12, r2)     // Catch:{ all -> 0x0166 }
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r1
        L_0x0027:
            android.app.Activity r2 = r12.getClientActivity()     // Catch:{ all -> 0x0166 }
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0166 }
            r4 = 17
            if (r3 < r4) goto L_0x0036
            boolean r3 = r2.isDestroyed()     // Catch:{ all -> 0x0166 }
            goto L_0x0037
        L_0x0036:
            r3 = r1
        L_0x0037:
            if (r2 == 0) goto L_0x015d
            boolean r4 = r2.isFinishing()     // Catch:{ all -> 0x0166 }
            if (r4 != 0) goto L_0x015d
            if (r3 == 0) goto L_0x0043
            goto L_0x015d
        L_0x0043:
            java.lang.String r5 = r12.getSiteId()     // Catch:{ all -> 0x0166 }
            java.lang.Integer r8 = r12.getRequestCode()     // Catch:{ all -> 0x0166 }
            android.app.Activity r3 = r12.getClientActivity()     // Catch:{ all -> 0x0166 }
            com.google.android.libraries.hats20.storage.HatsDataStore r3 = com.google.android.libraries.hats20.storage.HatsDataStore.buildFromContext(r3)     // Catch:{ all -> 0x0166 }
            r3.removeSurveyIfExpired(r5)     // Catch:{ all -> 0x0166 }
            boolean r4 = r3.validSurveyExists(r5)     // Catch:{ all -> 0x0166 }
            if (r4 != 0) goto L_0x005e
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r1
        L_0x005e:
            java.lang.String r4 = r3.getSurveyJson(r5)     // Catch:{ all -> 0x0166 }
            r11 = 1
            if (r4 == 0) goto L_0x014c
            boolean r6 = r4.isEmpty()     // Catch:{ all -> 0x0166 }
            if (r6 == 0) goto L_0x006d
            goto L_0x014c
        L_0x006d:
            android.content.res.Resources r6 = r2.getResources()     // Catch:{ MalformedSurveyException -> 0x0140, JSONException -> 0x0118 }
            com.google.android.libraries.hats20.model.SurveyController r6 = com.google.android.libraries.hats20.model.SurveyController.initWithSurveyFromJson(r4, r6)     // Catch:{ MalformedSurveyException -> 0x0140, JSONException -> 0x0118 }
            markSurveyRunning()     // Catch:{ all -> 0x0166 }
            r3.removeSurvey(r5)     // Catch:{ all -> 0x0166 }
            com.google.android.libraries.hats20.answer.AnswerBeacon r1 = new com.google.android.libraries.hats20.answer.AnswerBeacon     // Catch:{ all -> 0x0166 }
            r1.<init>()     // Catch:{ all -> 0x0166 }
            java.lang.String r3 = r6.getPromptParams()     // Catch:{ all -> 0x0166 }
            com.google.android.libraries.hats20.answer.AnswerBeacon r7 = r1.setPromptParams(r3)     // Catch:{ all -> 0x0166 }
            boolean r1 = r6.showInvitation()     // Catch:{ all -> 0x0166 }
            if (r1 == 0) goto L_0x0109
            com.google.android.libraries.hats20.util.LayoutDimensions r1 = new com.google.android.libraries.hats20.util.LayoutDimensions     // Catch:{ all -> 0x0166 }
            android.content.res.Resources r3 = r2.getResources()     // Catch:{ all -> 0x0166 }
            r1.<init>(r3)     // Catch:{ all -> 0x0166 }
            boolean r1 = r1.shouldDisplayPrompt()     // Catch:{ all -> 0x0166 }
            if (r1 != 0) goto L_0x009e
            goto L_0x0109
        L_0x009e:
            boolean r1 = r2 instanceof androidx.fragment.app.FragmentActivity     // Catch:{ all -> 0x0166 }
            if (r1 == 0) goto L_0x00d6
            androidx.fragment.app.FragmentActivity r2 = (androidx.fragment.app.FragmentActivity) r2     // Catch:{ all -> 0x0166 }
            androidx.fragment.app.FragmentManager r1 = r2.getSupportFragmentManager()     // Catch:{ all -> 0x0166 }
            java.lang.String r2 = "com.google.android.libraries.hats20.PromptDialogFragment"
            androidx.fragment.app.Fragment r2 = r1.findFragmentByTag(r2)     // Catch:{ all -> 0x0166 }
            if (r2 != 0) goto L_0x00ce
            java.lang.Integer r9 = r12.getMaxPromptWidth()     // Catch:{ all -> 0x0166 }
            boolean r10 = r12.isBottomSheet()     // Catch:{ all -> 0x0166 }
            com.google.android.libraries.hats20.PromptDialogFragment r2 = com.google.android.libraries.hats20.PromptDialogFragment.newInstance(r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x0166 }
            androidx.fragment.app.FragmentTransaction r1 = r1.beginTransaction()     // Catch:{ all -> 0x0166 }
            int r12 = r12.getParentResId()     // Catch:{ all -> 0x0166 }
            java.lang.String r3 = "com.google.android.libraries.hats20.PromptDialogFragment"
            androidx.fragment.app.FragmentTransaction r12 = r1.add((int) r12, (androidx.fragment.app.Fragment) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0166 }
            r12.commitAllowingStateLoss()     // Catch:{ all -> 0x0166 }
            goto L_0x0107
        L_0x00ce:
            java.lang.String r12 = "HatsLibClient"
            java.lang.String r1 = "PromptDialog was already open, bailing out."
            android.util.Log.w(r12, r1)     // Catch:{ all -> 0x0166 }
            goto L_0x0107
        L_0x00d6:
            android.app.FragmentManager r1 = r2.getFragmentManager()     // Catch:{ all -> 0x0166 }
            java.lang.String r2 = "com.google.android.libraries.hats20.PromptDialogFragment"
            android.app.Fragment r2 = r1.findFragmentByTag(r2)     // Catch:{ all -> 0x0166 }
            if (r2 != 0) goto L_0x0100
            java.lang.Integer r9 = r12.getMaxPromptWidth()     // Catch:{ all -> 0x0166 }
            boolean r10 = r12.isBottomSheet()     // Catch:{ all -> 0x0166 }
            com.google.android.libraries.hats20.PlatformPromptDialogFragment r2 = com.google.android.libraries.hats20.PlatformPromptDialogFragment.newInstance(r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x0166 }
            android.app.FragmentTransaction r1 = r1.beginTransaction()     // Catch:{ all -> 0x0166 }
            int r12 = r12.getParentResId()     // Catch:{ all -> 0x0166 }
            java.lang.String r3 = "com.google.android.libraries.hats20.PromptDialogFragment"
            android.app.FragmentTransaction r12 = r1.add(r12, r2, r3)     // Catch:{ all -> 0x0166 }
            r12.commitAllowingStateLoss()     // Catch:{ all -> 0x0166 }
            goto L_0x0107
        L_0x0100:
            java.lang.String r12 = "HatsLibClient"
            java.lang.String r1 = "PromptDialog was already open, bailing out."
            android.util.Log.w(r12, r1)     // Catch:{ all -> 0x0166 }
        L_0x0107:
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r11
        L_0x0109:
            boolean r12 = r12.isBottomSheet()     // Catch:{ all -> 0x0166 }
            r1 = r2
            r2 = r5
            r3 = r6
            r4 = r7
            r5 = r8
            r6 = r12
            com.google.android.libraries.hats20.SurveyPromptActivity.startSurveyActivity(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x0166 }
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r11
        L_0x0118:
            r12 = move-exception
            java.lang.String r2 = "HatsLibClient"
            java.lang.String r3 = java.lang.String.valueOf(r5)     // Catch:{ all -> 0x0166 }
            int r3 = r3.length()     // Catch:{ all -> 0x0166 }
            int r3 = r3 + 46
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0166 }
            r4.<init>(r3)     // Catch:{ all -> 0x0166 }
            java.lang.String r3 = "Failed to parse JSON for survey with site ID "
            r4.append(r3)     // Catch:{ all -> 0x0166 }
            r4.append(r5)     // Catch:{ all -> 0x0166 }
            java.lang.String r3 = "."
            r4.append(r3)     // Catch:{ all -> 0x0166 }
            java.lang.String r3 = r4.toString()     // Catch:{ all -> 0x0166 }
            android.util.Log.e(r2, r3, r12)     // Catch:{ all -> 0x0166 }
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r1
        L_0x0140:
            r12 = move-exception
            java.lang.String r2 = "HatsLibClient"
            java.lang.String r12 = r12.getMessage()     // Catch:{ all -> 0x0166 }
            android.util.Log.e(r2, r12)     // Catch:{ all -> 0x0166 }
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r1
        L_0x014c:
            java.lang.String r12 = "HatsLibClient"
            java.lang.String r2 = "Attempted to start survey with site ID %s, but the json in the shared preferences was not found or was empty."
            java.lang.Object[] r3 = new java.lang.Object[r11]     // Catch:{ all -> 0x0166 }
            r3[r1] = r5     // Catch:{ all -> 0x0166 }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ all -> 0x0166 }
            android.util.Log.e(r12, r2)     // Catch:{ all -> 0x0166 }
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r1
        L_0x015d:
            java.lang.String r12 = "HatsLibClient"
            java.lang.String r2 = "Cancelling show request, activity was null, destroyed or finishing."
            android.util.Log.w(r12, r2)     // Catch:{ all -> 0x0166 }
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            return r1
        L_0x0166:
            r12 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0166 }
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.hats20.HatsClient.showSurveyIfAvailable(com.google.android.libraries.hats20.HatsShowRequest):boolean");
    }

    static void markSurveyFinished() {
        AtomicBoolean atomicBoolean = isSurveyRunning;
        synchronized (atomicBoolean) {
            if (!atomicBoolean.get()) {
                Log.e("HatsLibClient", "Notified that survey was destroyed when it wasn't marked as running.");
            }
            atomicBoolean.set(false);
        }
    }

    static void sendBroadcast(Context context, String str, int i) {
        if (Log.isLoggable("HatsLibClient", 3)) {
            Log.d("HatsLibClient", "Hats survey is downloaded. Sending broadcast with action ACTION_BROADCAST_SURVEY_DOWNLOADED");
        }
        Intent intent = new Intent("com.google.android.libraries.hats20.SURVEY_DOWNLOADED");
        intent.putExtra("SiteId", str);
        intent.putExtra("ResponseCode", i);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    static void markSurveyRunning() {
        AtomicBoolean atomicBoolean = isSurveyRunning;
        synchronized (atomicBoolean) {
            atomicBoolean.set(true);
        }
    }

    private static boolean hasInternetPermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.INTERNET") == 0;
    }

    public static void forTestingInjectSurveyIntoStorage(Context context, String str, String str2, int i, long j) {
        HatsDataStore.buildFromContext(context).forTestingInjectSurveyIntoStorage(str, str2, i, j);
    }

    public static void forTestingClearAllData(Context context) {
        HatsDataStore.buildFromContext(context).forTestingClearAllData();
    }
}
