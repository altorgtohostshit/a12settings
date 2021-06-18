package com.android.settings.inputmethod;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.android.settings.R;
import com.android.settings.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

public class UserDictionaryAddWordContents {
    private static final String[] HAS_WORD_PROJECTION = {"word"};
    private String mLocale;
    private final int mMode;
    private final String mOldShortcut;
    private final String mOldWord;
    private String mSavedShortcut;
    private String mSavedWord;
    private final EditText mShortcutEditText;
    private final EditText mWordEditText;

    UserDictionaryAddWordContents(View view, Bundle bundle) {
        EditText editText = (EditText) view.findViewById(R.id.user_dictionary_add_word_text);
        this.mWordEditText = editText;
        EditText editText2 = (EditText) view.findViewById(R.id.user_dictionary_add_shortcut);
        this.mShortcutEditText = editText2;
        String string = bundle.getString("word");
        if (string != null) {
            editText.setText(string);
            editText.setSelection(editText.getText().length());
        }
        String string2 = bundle.getString("shortcut");
        if (!(string2 == null || editText2 == null)) {
            editText2.setText(string2);
        }
        this.mMode = bundle.getInt("mode");
        this.mOldWord = bundle.getString("word");
        this.mOldShortcut = bundle.getString("shortcut");
        updateLocale(bundle.getString("locale"));
    }

    UserDictionaryAddWordContents(View view, UserDictionaryAddWordContents userDictionaryAddWordContents) {
        this.mWordEditText = (EditText) view.findViewById(R.id.user_dictionary_add_word_text);
        this.mShortcutEditText = (EditText) view.findViewById(R.id.user_dictionary_add_shortcut);
        this.mMode = 0;
        this.mOldWord = userDictionaryAddWordContents.mSavedWord;
        this.mOldShortcut = userDictionaryAddWordContents.mSavedShortcut;
        updateLocale(userDictionaryAddWordContents.getCurrentUserDictionaryLocale());
    }

    /* access modifiers changed from: package-private */
    public void updateLocale(String str) {
        if (str == null) {
            str = Locale.getDefault().toString();
        }
        this.mLocale = str;
    }

    /* access modifiers changed from: package-private */
    public void saveStateIntoBundle(Bundle bundle) {
        bundle.putString("word", this.mWordEditText.getText().toString());
        bundle.putString("originalWord", this.mOldWord);
        EditText editText = this.mShortcutEditText;
        if (editText != null) {
            bundle.putString("shortcut", editText.getText().toString());
        }
        String str = this.mOldShortcut;
        if (str != null) {
            bundle.putString("originalShortcut", str);
        }
        bundle.putString("locale", this.mLocale);
    }

    /* access modifiers changed from: package-private */
    public void delete(Context context) {
        if (this.mMode == 0 && !TextUtils.isEmpty(this.mOldWord)) {
            UserDictionarySettings.deleteWord(this.mOldWord, this.mOldShortcut, context.getContentResolver());
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0039, code lost:
        if (android.text.TextUtils.isEmpty(r1) != false) goto L_0x002b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int apply(android.content.Context r5, android.os.Bundle r6) {
        /*
            r4 = this;
            if (r6 == 0) goto L_0x0005
            r4.saveStateIntoBundle(r6)
        L_0x0005:
            android.content.ContentResolver r6 = r5.getContentResolver()
            int r0 = r4.mMode
            if (r0 != 0) goto L_0x001c
            java.lang.String r0 = r4.mOldWord
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x001c
            java.lang.String r0 = r4.mOldWord
            java.lang.String r1 = r4.mOldShortcut
            com.android.settings.inputmethod.UserDictionarySettings.deleteWord(r0, r1, r6)
        L_0x001c:
            android.widget.EditText r0 = r4.mWordEditText
            android.text.Editable r0 = r0.getText()
            java.lang.String r0 = r0.toString()
            android.widget.EditText r1 = r4.mShortcutEditText
            r2 = 0
            if (r1 != 0) goto L_0x002d
        L_0x002b:
            r1 = r2
            goto L_0x003c
        L_0x002d:
            android.text.Editable r1 = r1.getText()
            java.lang.String r1 = r1.toString()
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 == 0) goto L_0x003c
            goto L_0x002b
        L_0x003c:
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x0044
            r4 = 1
            return r4
        L_0x0044:
            r4.mSavedWord = r0
            r4.mSavedShortcut = r1
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 == 0) goto L_0x0056
            boolean r3 = r4.hasWord(r0, r5)
            if (r3 == 0) goto L_0x0056
            r4 = 2
            return r4
        L_0x0056:
            com.android.settings.inputmethod.UserDictionarySettings.deleteWord(r0, r2, r6)
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 != 0) goto L_0x0062
            com.android.settings.inputmethod.UserDictionarySettings.deleteWord(r0, r1, r6)
        L_0x0062:
            java.lang.String r6 = r0.toString()
            r0 = 250(0xfa, float:3.5E-43)
            java.lang.String r3 = r4.mLocale
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L_0x0071
            goto L_0x0077
        L_0x0071:
            java.lang.String r4 = r4.mLocale
            java.util.Locale r2 = com.android.settings.Utils.createLocaleFromString(r4)
        L_0x0077:
            android.provider.UserDictionary.Words.addWord(r5, r6, r0, r1, r2)
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.inputmethod.UserDictionaryAddWordContents.apply(android.content.Context, android.os.Bundle):int");
    }

    private boolean hasWord(String str, Context context) {
        Cursor cursor;
        boolean z = true;
        if ("".equals(this.mLocale)) {
            cursor = context.getContentResolver().query(UserDictionary.Words.CONTENT_URI, HAS_WORD_PROJECTION, "word=? AND locale is null", new String[]{str}, (String) null);
        } else {
            cursor = context.getContentResolver().query(UserDictionary.Words.CONTENT_URI, HAS_WORD_PROJECTION, "word=? AND locale=?", new String[]{str, this.mLocale}, (String) null);
        }
        if (cursor == null) {
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
        try {
            if (cursor.getCount() <= 0) {
                z = false;
            }
            cursor.close();
            return z;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static class LocaleRenderer {
        private final String mDescription;
        private final String mLocaleString;

        public LocaleRenderer(Context context, String str) {
            this.mLocaleString = str;
            if (str == null) {
                this.mDescription = context.getString(R.string.user_dict_settings_more_languages);
            } else if ("".equals(str)) {
                this.mDescription = context.getString(R.string.user_dict_settings_all_languages);
            } else {
                this.mDescription = Utils.createLocaleFromString(str).getDisplayName();
            }
        }

        public String toString() {
            return this.mDescription;
        }
    }

    private static void addLocaleDisplayNameToList(Context context, ArrayList<LocaleRenderer> arrayList, String str) {
        if (str != null) {
            arrayList.add(new LocaleRenderer(context, str));
        }
    }

    public ArrayList<LocaleRenderer> getLocalesList(Activity activity) {
        TreeSet<String> userDictionaryLocalesSet = UserDictionaryListPreferenceController.getUserDictionaryLocalesSet(activity);
        userDictionaryLocalesSet.remove(this.mLocale);
        String locale = Locale.getDefault().toString();
        userDictionaryLocalesSet.remove(locale);
        userDictionaryLocalesSet.remove("");
        ArrayList<LocaleRenderer> arrayList = new ArrayList<>();
        addLocaleDisplayNameToList(activity, arrayList, this.mLocale);
        if (!locale.equals(this.mLocale)) {
            addLocaleDisplayNameToList(activity, arrayList, locale);
        }
        Iterator<String> it = userDictionaryLocalesSet.iterator();
        while (it.hasNext()) {
            addLocaleDisplayNameToList(activity, arrayList, it.next());
        }
        if (!"".equals(this.mLocale)) {
            addLocaleDisplayNameToList(activity, arrayList, "");
        }
        arrayList.add(new LocaleRenderer(activity, (String) null));
        return arrayList;
    }

    public String getCurrentUserDictionaryLocale() {
        return this.mLocale;
    }
}
