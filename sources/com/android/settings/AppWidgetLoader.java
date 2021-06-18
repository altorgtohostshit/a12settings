package com.android.settings;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.android.settings.AppWidgetLoader.LabelledItem;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppWidgetLoader<Item extends LabelledItem> {
    private AppWidgetManager mAppWidgetManager;
    private Context mContext;
    ItemConstructor<Item> mItemConstructor;

    public interface ItemConstructor<Item> {
        Item createItem(Context context, AppWidgetProviderInfo appWidgetProviderInfo, Bundle bundle);
    }

    interface LabelledItem {
        CharSequence getLabel();
    }

    public AppWidgetLoader(Context context, AppWidgetManager appWidgetManager, ItemConstructor<Item> itemConstructor) {
        this.mContext = context;
        this.mAppWidgetManager = appWidgetManager;
        this.mItemConstructor = itemConstructor;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003f, code lost:
        r4 = null;
        r5 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void putCustomAppWidgets(java.util.List<Item> r10, android.content.Intent r11) {
        /*
            r9 = this;
            java.lang.String r0 = "customInfo"
            java.util.ArrayList r0 = r11.getParcelableArrayListExtra(r0)
            java.lang.String r1 = "AppWidgetAdapter"
            r2 = 0
            if (r0 == 0) goto L_0x00a0
            int r3 = r0.size()
            if (r3 != 0) goto L_0x0013
            goto L_0x00a0
        L_0x0013:
            int r3 = r0.size()
            r4 = 0
            r5 = r4
        L_0x0019:
            if (r5 >= r3) goto L_0x0043
            java.lang.Object r6 = r0.get(r5)
            android.os.Parcelable r6 = (android.os.Parcelable) r6
            if (r6 == 0) goto L_0x002b
            boolean r6 = r6 instanceof android.appwidget.AppWidgetProviderInfo
            if (r6 != 0) goto L_0x0028
            goto L_0x002b
        L_0x0028:
            int r5 = r5 + 1
            goto L_0x0019
        L_0x002b:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r0 = "error using EXTRA_CUSTOM_INFO index="
            r11.append(r0)
            r11.append(r5)
            java.lang.String r11 = r11.toString()
            android.util.Log.e(r1, r11)
        L_0x003f:
            r4 = r2
            r5 = r4
            goto L_0x00a7
        L_0x0043:
            java.lang.String r5 = "customExtras"
            java.util.ArrayList r11 = r11.getParcelableArrayListExtra(r5)
            if (r11 != 0) goto L_0x0053
            java.lang.String r0 = "EXTRA_CUSTOM_INFO without EXTRA_CUSTOM_EXTRAS"
            android.util.Log.e(r1, r0)
            r5 = r11
            r4 = r2
            goto L_0x00a7
        L_0x0053:
            int r5 = r11.size()
            if (r3 == r5) goto L_0x0076
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r0 = "list size mismatch: EXTRA_CUSTOM_INFO: "
            r11.append(r0)
            r11.append(r3)
            java.lang.String r0 = " EXTRA_CUSTOM_EXTRAS: "
            r11.append(r0)
            r11.append(r5)
            java.lang.String r11 = r11.toString()
            android.util.Log.e(r1, r11)
            goto L_0x003f
        L_0x0076:
            if (r4 >= r5) goto L_0x009d
            java.lang.Object r3 = r11.get(r4)
            android.os.Parcelable r3 = (android.os.Parcelable) r3
            if (r3 == 0) goto L_0x0088
            boolean r3 = r3 instanceof android.os.Bundle
            if (r3 != 0) goto L_0x0085
            goto L_0x0088
        L_0x0085:
            int r4 = r4 + 1
            goto L_0x0076
        L_0x0088:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r0 = "error using EXTRA_CUSTOM_EXTRAS index="
            r11.append(r0)
            r11.append(r4)
            java.lang.String r11 = r11.toString()
            android.util.Log.e(r1, r11)
            goto L_0x003f
        L_0x009d:
            r5 = r11
            r4 = r0
            goto L_0x00a7
        L_0x00a0:
            java.lang.String r11 = "EXTRA_CUSTOM_INFO not present."
            android.util.Log.i(r1, r11)
            r4 = r0
            r5 = r2
        L_0x00a7:
            r7 = 0
            r8 = 1
            r3 = r9
            r6 = r10
            r3.putAppWidgetItems(r4, r5, r6, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.AppWidgetLoader.putCustomAppWidgets(java.util.List, android.content.Intent):void");
    }

    /* access modifiers changed from: package-private */
    public void putAppWidgetItems(List<AppWidgetProviderInfo> list, List<Bundle> list2, List<Item> list3, int i, boolean z) {
        if (list != null) {
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                AppWidgetProviderInfo appWidgetProviderInfo = list.get(i2);
                if (z || (appWidgetProviderInfo.widgetCategory & i) != 0) {
                    list3.add((LabelledItem) this.mItemConstructor.createItem(this.mContext, appWidgetProviderInfo, list2 != null ? list2.get(i2) : null));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<Item> getItems(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("customSort", true);
        ArrayList arrayList = new ArrayList();
        putInstalledAppWidgets(arrayList, intent.getIntExtra("categoryFilter", 1));
        if (booleanExtra) {
            putCustomAppWidgets(arrayList, intent);
        }
        Collections.sort(arrayList, new Comparator<Item>() {
            Collator mCollator = Collator.getInstance();

            public int compare(Item item, Item item2) {
                return this.mCollator.compare(item.getLabel(), item2.getLabel());
            }
        });
        if (!booleanExtra) {
            ArrayList arrayList2 = new ArrayList();
            putCustomAppWidgets(arrayList2, intent);
            arrayList.addAll(arrayList2);
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void putInstalledAppWidgets(List<Item> list, int i) {
        putAppWidgetItems(this.mAppWidgetManager.getInstalledProviders(i), (List<Bundle>) null, list, i, false);
    }
}
