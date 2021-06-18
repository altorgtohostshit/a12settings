package com.android.settingslib.inputmethod;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InputMethodAndSubtypeUtilCompat {
    private static final TextUtils.SimpleStringSplitter sStringInputMethodSplitter = new TextUtils.SimpleStringSplitter(':');
    private static final TextUtils.SimpleStringSplitter sStringInputMethodSubtypeSplitter = new TextUtils.SimpleStringSplitter(';');

    public static String buildInputMethodsAndSubtypesString(HashMap<String, HashSet<String>> hashMap) {
        StringBuilder sb = new StringBuilder();
        for (String next : hashMap.keySet()) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(next);
            Iterator it = hashMap.get(next).iterator();
            while (it.hasNext()) {
                sb.append(';');
                sb.append((String) it.next());
            }
        }
        return sb.toString();
    }

    private static String buildInputMethodsString(HashSet<String> hashSet) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = hashSet.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(next);
        }
        return sb.toString();
    }

    private static int getInputMethodSubtypeSelected(ContentResolver contentResolver) {
        try {
            return Settings.Secure.getInt(contentResolver, "selected_input_method_subtype");
        } catch (Settings.SettingNotFoundException unused) {
            return -1;
        }
    }

    private static boolean isInputMethodSubtypeSelected(ContentResolver contentResolver) {
        return getInputMethodSubtypeSelected(contentResolver) != -1;
    }

    private static void putSelectedInputMethodSubtype(ContentResolver contentResolver, int i) {
        Settings.Secure.putInt(contentResolver, "selected_input_method_subtype", i);
    }

    static HashMap<String, HashSet<String>> getEnabledInputMethodsAndSubtypeList(ContentResolver contentResolver) {
        return parseInputMethodsAndSubtypesString(Settings.Secure.getString(contentResolver, "enabled_input_methods"));
    }

    public static HashMap<String, HashSet<String>> parseInputMethodsAndSubtypesString(String str) {
        HashMap<String, HashSet<String>> hashMap = new HashMap<>();
        if (TextUtils.isEmpty(str)) {
            return hashMap;
        }
        sStringInputMethodSplitter.setString(str);
        while (true) {
            TextUtils.SimpleStringSplitter simpleStringSplitter = sStringInputMethodSplitter;
            if (!simpleStringSplitter.hasNext()) {
                return hashMap;
            }
            String next = simpleStringSplitter.next();
            TextUtils.SimpleStringSplitter simpleStringSplitter2 = sStringInputMethodSubtypeSplitter;
            simpleStringSplitter2.setString(next);
            if (simpleStringSplitter2.hasNext()) {
                HashSet hashSet = new HashSet();
                String next2 = simpleStringSplitter2.next();
                while (true) {
                    TextUtils.SimpleStringSplitter simpleStringSplitter3 = sStringInputMethodSubtypeSplitter;
                    if (!simpleStringSplitter3.hasNext()) {
                        break;
                    }
                    hashSet.add(simpleStringSplitter3.next());
                }
                hashMap.put(next2, hashSet);
            }
        }
    }

    private static HashSet<String> getDisabledSystemIMEs(ContentResolver contentResolver) {
        HashSet<String> hashSet = new HashSet<>();
        String string = Settings.Secure.getString(contentResolver, "disabled_system_input_methods");
        if (TextUtils.isEmpty(string)) {
            return hashSet;
        }
        sStringInputMethodSplitter.setString(string);
        while (true) {
            TextUtils.SimpleStringSplitter simpleStringSplitter = sStringInputMethodSplitter;
            if (!simpleStringSplitter.hasNext()) {
                return hashSet;
            }
            hashSet.add(simpleStringSplitter.next());
        }
    }

    public static void saveInputMethodSubtypeList(PreferenceFragmentCompat preferenceFragmentCompat, ContentResolver contentResolver, List<InputMethodInfo> list, boolean z) {
        boolean z2;
        Iterator<InputMethodInfo> it;
        PreferenceFragmentCompat preferenceFragmentCompat2 = preferenceFragmentCompat;
        ContentResolver contentResolver2 = contentResolver;
        String string = Settings.Secure.getString(contentResolver2, "default_input_method");
        int inputMethodSubtypeSelected = getInputMethodSubtypeSelected(contentResolver);
        HashMap<String, HashSet<String>> enabledInputMethodsAndSubtypeList = getEnabledInputMethodsAndSubtypeList(contentResolver);
        HashSet<String> disabledSystemIMEs = getDisabledSystemIMEs(contentResolver);
        Iterator<InputMethodInfo> it2 = list.iterator();
        boolean z3 = false;
        while (it2.hasNext()) {
            InputMethodInfo next = it2.next();
            String id = next.getId();
            Preference findPreference = preferenceFragmentCompat2.findPreference(id);
            if (findPreference != null) {
                if (findPreference instanceof TwoStatePreference) {
                    z2 = ((TwoStatePreference) findPreference).isChecked();
                } else {
                    z2 = enabledInputMethodsAndSubtypeList.containsKey(id);
                }
                boolean equals = id.equals(string);
                boolean isSystem = next.isSystem();
                if ((z || !InputMethodSettingValuesWrapper.getInstance(preferenceFragmentCompat.getActivity()).isAlwaysCheckedIme(next)) && !z2) {
                    it = it2;
                    enabledInputMethodsAndSubtypeList.remove(id);
                    if (equals) {
                        string = null;
                    }
                } else {
                    if (!enabledInputMethodsAndSubtypeList.containsKey(id)) {
                        enabledInputMethodsAndSubtypeList.put(id, new HashSet());
                    }
                    HashSet hashSet = enabledInputMethodsAndSubtypeList.get(id);
                    int subtypeCount = next.getSubtypeCount();
                    it = it2;
                    int i = 0;
                    boolean z4 = false;
                    while (i < subtypeCount) {
                        InputMethodSubtype subtypeAt = next.getSubtypeAt(i);
                        int i2 = subtypeCount;
                        String valueOf = String.valueOf(subtypeAt.hashCode());
                        boolean z5 = z3;
                        TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceFragmentCompat2.findPreference(id + valueOf);
                        if (twoStatePreference != null) {
                            if (!z4) {
                                hashSet.clear();
                                z4 = true;
                                z5 = true;
                            }
                            if (!twoStatePreference.isEnabled() || !twoStatePreference.isChecked()) {
                                hashSet.remove(valueOf);
                            } else {
                                hashSet.add(valueOf);
                                if (equals && inputMethodSubtypeSelected == subtypeAt.hashCode()) {
                                    z3 = false;
                                    i++;
                                    subtypeCount = i2;
                                }
                            }
                        }
                        z3 = z5;
                        i++;
                        subtypeCount = i2;
                    }
                    boolean z6 = z3;
                }
                if (isSystem && z) {
                    if (disabledSystemIMEs.contains(id)) {
                        if (z2) {
                            disabledSystemIMEs.remove(id);
                        }
                    } else if (!z2) {
                        disabledSystemIMEs.add(id);
                    }
                }
                it2 = it;
            }
        }
        String buildInputMethodsAndSubtypesString = buildInputMethodsAndSubtypesString(enabledInputMethodsAndSubtypeList);
        String buildInputMethodsString = buildInputMethodsString(disabledSystemIMEs);
        if (z3 || !isInputMethodSubtypeSelected(contentResolver)) {
            putSelectedInputMethodSubtype(contentResolver2, -1);
        }
        Settings.Secure.putString(contentResolver2, "enabled_input_methods", buildInputMethodsAndSubtypesString);
        if (buildInputMethodsString.length() > 0) {
            Settings.Secure.putString(contentResolver2, "disabled_system_input_methods", buildInputMethodsString);
        }
        if (string == null) {
            string = "";
        }
        Settings.Secure.putString(contentResolver2, "default_input_method", string);
    }

    public static void loadInputMethodSubtypeList(PreferenceFragmentCompat preferenceFragmentCompat, ContentResolver contentResolver, List<InputMethodInfo> list, Map<String, List<Preference>> map) {
        HashMap<String, HashSet<String>> enabledInputMethodsAndSubtypeList = getEnabledInputMethodsAndSubtypeList(contentResolver);
        for (InputMethodInfo id : list) {
            String id2 = id.getId();
            Preference findPreference = preferenceFragmentCompat.findPreference(id2);
            if (findPreference instanceof TwoStatePreference) {
                boolean containsKey = enabledInputMethodsAndSubtypeList.containsKey(id2);
                ((TwoStatePreference) findPreference).setChecked(containsKey);
                if (map != null) {
                    for (Preference enabled : map.get(id2)) {
                        enabled.setEnabled(containsKey);
                    }
                }
                setSubtypesPreferenceEnabled(preferenceFragmentCompat, list, id2, containsKey);
            }
        }
        updateSubtypesPreferenceChecked(preferenceFragmentCompat, list, enabledInputMethodsAndSubtypeList);
    }

    private static void setSubtypesPreferenceEnabled(PreferenceFragmentCompat preferenceFragmentCompat, List<InputMethodInfo> list, String str, boolean z) {
        PreferenceScreen preferenceScreen = preferenceFragmentCompat.getPreferenceScreen();
        for (InputMethodInfo next : list) {
            if (str.equals(next.getId())) {
                int subtypeCount = next.getSubtypeCount();
                for (int i = 0; i < subtypeCount; i++) {
                    InputMethodSubtype subtypeAt = next.getSubtypeAt(i);
                    TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceScreen.findPreference(str + subtypeAt.hashCode());
                    if (twoStatePreference != null) {
                        twoStatePreference.setEnabled(z);
                    }
                }
            }
        }
    }

    private static void updateSubtypesPreferenceChecked(PreferenceFragmentCompat preferenceFragmentCompat, List<InputMethodInfo> list, HashMap<String, HashSet<String>> hashMap) {
        PreferenceScreen preferenceScreen = preferenceFragmentCompat.getPreferenceScreen();
        for (InputMethodInfo next : list) {
            String id = next.getId();
            if (hashMap.containsKey(id)) {
                HashSet hashSet = hashMap.get(id);
                int subtypeCount = next.getSubtypeCount();
                for (int i = 0; i < subtypeCount; i++) {
                    String valueOf = String.valueOf(next.getSubtypeAt(i).hashCode());
                    TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceScreen.findPreference(id + valueOf);
                    if (twoStatePreference != null) {
                        twoStatePreference.setChecked(hashSet.contains(valueOf));
                    }
                }
            }
        }
    }

    public static void removeUnnecessaryNonPersistentPreference(Preference preference) {
        SharedPreferences sharedPreferences;
        String key = preference.getKey();
        if (!preference.isPersistent() && key != null && (sharedPreferences = preference.getSharedPreferences()) != null && sharedPreferences.contains(key)) {
            sharedPreferences.edit().remove(key).apply();
        }
    }
}
