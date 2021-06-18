package com.android.settings.privacy;

import android.content.Context;
import android.content.DialogInterface;
import android.os.UserHandle;
import androidx.preference.Preference;
import java.util.ArrayList;

/* renamed from: com.android.settings.privacy.EnableContentCaptureWithServiceSettingsPreferenceController$ProfileSelectDialog$$ExternalSyntheticLambda0 */
public final /* synthetic */ class C1239xde084dd1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ Preference f$1;
    public final /* synthetic */ Context f$2;

    public /* synthetic */ C1239xde084dd1(ArrayList arrayList, Preference preference, Context context) {
        this.f$0 = arrayList;
        this.f$1 = preference;
        this.f$2 = context;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$2.startActivityAsUser(this.f$1.getIntent().addFlags(32768), (UserHandle) this.f$0.get(i));
    }
}
