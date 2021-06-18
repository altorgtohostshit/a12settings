package com.android.settings.inputmethod;

import android.content.Context;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.Utils;

public class UserDictionarySettingsUtils {
    public static String getLocaleDisplayName(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return context.getResources().getString(R.string.user_dict_settings_all_languages);
        }
        return Utils.createLocaleFromString(str).getDisplayName(context.getResources().getConfiguration().locale);
    }
}
