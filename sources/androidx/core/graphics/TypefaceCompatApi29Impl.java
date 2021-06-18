package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import com.android.settings.wifi.UseOpenWifiPreferenceController;
import java.io.IOException;
import java.io.InputStream;

public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    /* access modifiers changed from: protected */
    public FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fontInfoArr, int i) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* access modifiers changed from: protected */
    public Typeface createFromInputStream(Context context, InputStream inputStream) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005f, code lost:
        if (r3 != null) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0061, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0066, code lost:
        if ((r13 & 1) == 0) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0068, code lost:
        r11 = 700;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006b, code lost:
        r11 = com.android.settings.wifi.UseOpenWifiPreferenceController.REQUEST_CODE_OPEN_WIFI_AUTOMATICALLY;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006f, code lost:
        if ((r13 & 2) == 0) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0071, code lost:
        r1 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0086, code lost:
        return new android.graphics.Typeface.CustomFallbackBuilder(r3.build()).setStyle(new android.graphics.fonts.FontStyle(r11, r1)).build();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r10, android.os.CancellationSignal r11, androidx.core.provider.FontsContractCompat.FontInfo[] r12, int r13) {
        /*
            r9 = this;
            android.content.ContentResolver r9 = r10.getContentResolver()
            r10 = 0
            int r0 = r12.length     // Catch:{ Exception -> 0x0087 }
            r1 = 0
            r3 = r10
            r2 = r1
        L_0x0009:
            r4 = 1
            if (r2 >= r0) goto L_0x005f
            r5 = r12[r2]     // Catch:{ Exception -> 0x0087 }
            android.net.Uri r6 = r5.getUri()     // Catch:{ IOException -> 0x005c }
            java.lang.String r7 = "r"
            android.os.ParcelFileDescriptor r6 = r9.openFileDescriptor(r6, r7, r11)     // Catch:{ IOException -> 0x005c }
            if (r6 != 0) goto L_0x0020
            if (r6 == 0) goto L_0x005c
        L_0x001c:
            r6.close()     // Catch:{ IOException -> 0x005c }
            goto L_0x005c
        L_0x0020:
            android.graphics.fonts.Font$Builder r7 = new android.graphics.fonts.Font$Builder     // Catch:{ all -> 0x0052 }
            r7.<init>(r6)     // Catch:{ all -> 0x0052 }
            int r8 = r5.getWeight()     // Catch:{ all -> 0x0052 }
            android.graphics.fonts.Font$Builder r7 = r7.setWeight(r8)     // Catch:{ all -> 0x0052 }
            boolean r8 = r5.isItalic()     // Catch:{ all -> 0x0052 }
            if (r8 == 0) goto L_0x0034
            goto L_0x0035
        L_0x0034:
            r4 = r1
        L_0x0035:
            android.graphics.fonts.Font$Builder r4 = r7.setSlant(r4)     // Catch:{ all -> 0x0052 }
            int r5 = r5.getTtcIndex()     // Catch:{ all -> 0x0052 }
            android.graphics.fonts.Font$Builder r4 = r4.setTtcIndex(r5)     // Catch:{ all -> 0x0052 }
            android.graphics.fonts.Font r4 = r4.build()     // Catch:{ all -> 0x0052 }
            if (r3 != 0) goto L_0x004e
            android.graphics.fonts.FontFamily$Builder r5 = new android.graphics.fonts.FontFamily$Builder     // Catch:{ all -> 0x0052 }
            r5.<init>(r4)     // Catch:{ all -> 0x0052 }
            r3 = r5
            goto L_0x001c
        L_0x004e:
            r3.addFont(r4)     // Catch:{ all -> 0x0052 }
            goto L_0x001c
        L_0x0052:
            r4 = move-exception
            r6.close()     // Catch:{ all -> 0x0057 }
            goto L_0x005b
        L_0x0057:
            r5 = move-exception
            r4.addSuppressed(r5)     // Catch:{ IOException -> 0x005c }
        L_0x005b:
            throw r4     // Catch:{ IOException -> 0x005c }
        L_0x005c:
            int r2 = r2 + 1
            goto L_0x0009
        L_0x005f:
            if (r3 != 0) goto L_0x0062
            return r10
        L_0x0062:
            android.graphics.fonts.FontStyle r9 = new android.graphics.fonts.FontStyle     // Catch:{ Exception -> 0x0087 }
            r11 = r13 & 1
            if (r11 == 0) goto L_0x006b
            r11 = 700(0x2bc, float:9.81E-43)
            goto L_0x006d
        L_0x006b:
            r11 = 400(0x190, float:5.6E-43)
        L_0x006d:
            r12 = r13 & 2
            if (r12 == 0) goto L_0x0072
            r1 = r4
        L_0x0072:
            r9.<init>(r11, r1)     // Catch:{ Exception -> 0x0087 }
            android.graphics.Typeface$CustomFallbackBuilder r11 = new android.graphics.Typeface$CustomFallbackBuilder     // Catch:{ Exception -> 0x0087 }
            android.graphics.fonts.FontFamily r12 = r3.build()     // Catch:{ Exception -> 0x0087 }
            r11.<init>(r12)     // Catch:{ Exception -> 0x0087 }
            android.graphics.Typeface$CustomFallbackBuilder r9 = r11.setStyle(r9)     // Catch:{ Exception -> 0x0087 }
            android.graphics.Typeface r9 = r9.build()     // Catch:{ Exception -> 0x0087 }
            return r9
        L_0x0087:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatApi29Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, androidx.core.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        try {
            FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
            int length = entries.length;
            int i2 = 0;
            FontFamily.Builder builder = null;
            int i3 = 0;
            while (true) {
                int i4 = 1;
                if (i3 >= length) {
                    break;
                }
                FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = entries[i3];
                try {
                    Font.Builder weight = new Font.Builder(resources, fontFileResourceEntry.getResourceId()).setWeight(fontFileResourceEntry.getWeight());
                    if (!fontFileResourceEntry.isItalic()) {
                        i4 = 0;
                    }
                    Font build = weight.setSlant(i4).setTtcIndex(fontFileResourceEntry.getTtcIndex()).setFontVariationSettings(fontFileResourceEntry.getVariationSettings()).build();
                    if (builder == null) {
                        builder = new FontFamily.Builder(build);
                    } else {
                        builder.addFont(build);
                    }
                } catch (IOException unused) {
                }
                i3++;
            }
            if (builder == null) {
                return null;
            }
            int i5 = (i & 1) != 0 ? 700 : UseOpenWifiPreferenceController.REQUEST_CODE_OPEN_WIFI_AUTOMATICALLY;
            if ((i & 2) != 0) {
                i2 = 1;
            }
            return new Typeface.CustomFallbackBuilder(builder.build()).setStyle(new FontStyle(i5, i2)).build();
        } catch (Exception unused2) {
            return null;
        }
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        try {
            Font build = new Font.Builder(resources, i).build();
            return new Typeface.CustomFallbackBuilder(new FontFamily.Builder(build).build()).setStyle(build.getStyle()).build();
        } catch (Exception unused) {
            return null;
        }
    }
}
