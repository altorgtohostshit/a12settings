package com.android.settings.inputmethod;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.Instrumentable;
import com.android.settingslib.core.instrumentation.VisibilityLoggerMixin;

public class UserDictionarySettings extends ListFragment implements Instrumentable, LoaderManager.LoaderCallbacks<Cursor> {
    private Cursor mCursor;
    private String mLocale;
    private VisibilityLoggerMixin mVisibilityLoggerMixin;

    public int getMetricsCategory() {
        return 514;
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void onCreate(Bundle bundle) {
        String str;
        String str2;
        super.onCreate(bundle);
        this.mVisibilityLoggerMixin = new VisibilityLoggerMixin(getMetricsCategory(), FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider());
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            str = null;
        } else {
            str = intent.getStringExtra("locale");
        }
        Bundle arguments = getArguments();
        if (arguments == null) {
            str2 = null;
        } else {
            str2 = arguments.getString("locale");
        }
        if (str2 != null) {
            str = str2;
        } else if (str == null) {
            str = null;
        }
        this.mLocale = str;
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(1, (Bundle) null, this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (FeatureFlagUtils.isEnabled(getContext(), "settings_silky_home")) {
            getActivity().setTitle(R.string.user_dict_settings_title);
        }
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.user_dict_settings_title);
            actionBar.setSubtitle(UserDictionarySettingsUtils.getLocaleDisplayName(getActivity(), this.mLocale));
        }
        return layoutInflater.inflate(17367264, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) getView().findViewById(16908292);
        textView.setText(R.string.user_dict_settings_empty_text);
        ListView listView = getListView();
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(textView);
    }

    public void onResume() {
        super.onResume();
        this.mVisibilityLoggerMixin.onResume();
        getLoaderManager().restartLoader(1, (Bundle) null, this);
    }

    private ListAdapter createAdapter() {
        return new MyAdapter(getActivity(), R.layout.user_dictionary_item, this.mCursor, new String[]{"word", "shortcut"}, new int[]{16908308, 16908309});
    }

    public void onListItemClick(ListView listView, View view, int i, long j) {
        String word = getWord(i);
        String shortcut = getShortcut(i);
        if (word != null) {
            showAddOrEditDialog(word, shortcut);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 1, 0, R.string.user_dict_settings_add_menu_title).setIcon(R.drawable.ic_add_24dp).setShowAsAction(5);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 1) {
            return false;
        }
        showAddOrEditDialog((String) null, (String) null);
        return true;
    }

    public void onPause() {
        super.onPause();
        this.mVisibilityLoggerMixin.onPause();
    }

    private void showAddOrEditDialog(String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putInt("mode", str == null ? 1 : 0);
        bundle.putString("word", str);
        bundle.putString("shortcut", str2);
        bundle.putString("locale", this.mLocale);
        new SubSettingLauncher(getContext()).setDestination(UserDictionaryAddWordFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.user_dict_settings_add_dialog_title).setSourceMetricsCategory(getMetricsCategory()).launch();
    }

    private String getWord(int i) {
        Cursor cursor = this.mCursor;
        if (cursor == null) {
            return null;
        }
        cursor.moveToPosition(i);
        if (this.mCursor.isAfterLast()) {
            return null;
        }
        Cursor cursor2 = this.mCursor;
        return cursor2.getString(cursor2.getColumnIndexOrThrow("word"));
    }

    private String getShortcut(int i) {
        Cursor cursor = this.mCursor;
        if (cursor == null) {
            return null;
        }
        cursor.moveToPosition(i);
        if (this.mCursor.isAfterLast()) {
            return null;
        }
        Cursor cursor2 = this.mCursor;
        return cursor2.getString(cursor2.getColumnIndexOrThrow("shortcut"));
    }

    public static void deleteWord(String str, String str2, ContentResolver contentResolver) {
        if (TextUtils.isEmpty(str2)) {
            contentResolver.delete(UserDictionary.Words.CONTENT_URI, "word=? AND shortcut is null OR shortcut=''", new String[]{str});
            return;
        }
        contentResolver.delete(UserDictionary.Words.CONTENT_URI, "word=? AND shortcut=?", new String[]{str, str2});
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new UserDictionaryCursorLoader(getContext(), this.mLocale);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.mCursor = cursor;
        getListView().setAdapter(createAdapter());
    }

    private static class MyAdapter extends SimpleCursorAdapter implements SectionIndexer {
        private AlphabetIndexer mIndexer;
        private final SimpleCursorAdapter.ViewBinder mViewBinder;

        public MyAdapter(Context context, int i, Cursor cursor, String[] strArr, int[] iArr) {
            super(context, i, cursor, strArr, iArr);
            C09821 r2 = new SimpleCursorAdapter.ViewBinder() {
                public boolean setViewValue(View view, Cursor cursor, int i) {
                    if (i != 2) {
                        return false;
                    }
                    String string = cursor.getString(2);
                    if (TextUtils.isEmpty(string)) {
                        view.setVisibility(8);
                    } else {
                        ((TextView) view).setText(string);
                        view.setVisibility(0);
                    }
                    view.invalidate();
                    return true;
                }
            };
            this.mViewBinder = r2;
            if (cursor != null) {
                this.mIndexer = new AlphabetIndexer(cursor, cursor.getColumnIndexOrThrow("word"), context.getString(17040243));
            }
            setViewBinder(r2);
        }

        public int getPositionForSection(int i) {
            AlphabetIndexer alphabetIndexer = this.mIndexer;
            if (alphabetIndexer == null) {
                return 0;
            }
            return alphabetIndexer.getPositionForSection(i);
        }

        public int getSectionForPosition(int i) {
            AlphabetIndexer alphabetIndexer = this.mIndexer;
            if (alphabetIndexer == null) {
                return 0;
            }
            return alphabetIndexer.getSectionForPosition(i);
        }

        public Object[] getSections() {
            AlphabetIndexer alphabetIndexer = this.mIndexer;
            if (alphabetIndexer == null) {
                return null;
            }
            return alphabetIndexer.getSections();
        }
    }
}
