package androidx.slice;

import android.app.slice.Slice;
import android.app.slice.SliceItem;
import android.app.slice.SliceSpec;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import androidx.collection.ArraySet;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import java.util.Set;

public class SliceConvert {
    public static Slice unwrap(Slice slice) {
        if (slice == null || slice.getUri() == null) {
            return null;
        }
        Slice.Builder builder = new Slice.Builder(slice.getUri(), unwrap(slice.getSpec()));
        builder.addHints(slice.getHints());
        for (SliceItem sliceItem : slice.getItemArray()) {
            String format = sliceItem.getFormat();
            format.hashCode();
            char c = 65535;
            switch (format.hashCode()) {
                case -1422950858:
                    if (format.equals("action")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1377881982:
                    if (format.equals("bundle")) {
                        c = 1;
                        break;
                    }
                    break;
                case 104431:
                    if (format.equals("int")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3327612:
                    if (format.equals("long")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3556653:
                    if (format.equals("text")) {
                        c = 4;
                        break;
                    }
                    break;
                case 100313435:
                    if (format.equals("image")) {
                        c = 5;
                        break;
                    }
                    break;
                case 100358090:
                    if (format.equals("input")) {
                        c = 6;
                        break;
                    }
                    break;
                case 109526418:
                    if (format.equals("slice")) {
                        c = 7;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    builder.addAction(sliceItem.getAction(), unwrap(sliceItem.getSlice()), sliceItem.getSubType());
                    break;
                case 1:
                    builder.addBundle((Bundle) sliceItem.mObj, sliceItem.getSubType(), sliceItem.getHints());
                    break;
                case 2:
                    builder.addInt(sliceItem.getInt(), sliceItem.getSubType(), sliceItem.getHints());
                    break;
                case 3:
                    builder.addLong(sliceItem.getLong(), sliceItem.getSubType(), sliceItem.getHints());
                    break;
                case 4:
                    builder.addText(sliceItem.getText(), sliceItem.getSubType(), sliceItem.getHints());
                    break;
                case 5:
                    builder.addIcon(sliceItem.getIcon().toIcon(), sliceItem.getSubType(), sliceItem.getHints());
                    break;
                case 6:
                    builder.addRemoteInput(sliceItem.getRemoteInput(), sliceItem.getSubType(), sliceItem.getHints());
                    break;
                case 7:
                    builder.addSubSlice(unwrap(sliceItem.getSlice()), sliceItem.getSubType());
                    break;
            }
        }
        return builder.build();
    }

    private static SliceSpec unwrap(SliceSpec sliceSpec) {
        if (sliceSpec == null) {
            return null;
        }
        return new SliceSpec(sliceSpec.getType(), sliceSpec.getRevision());
    }

    static Set<SliceSpec> unwrap(Set<SliceSpec> set) {
        ArraySet arraySet = new ArraySet();
        if (set != null) {
            for (SliceSpec unwrap : set) {
                arraySet.add(unwrap(unwrap));
            }
        }
        return arraySet;
    }

    public static Slice wrap(Slice slice, Context context) {
        if (slice == null || slice.getUri() == null) {
            return null;
        }
        Slice.Builder builder = new Slice.Builder(slice.getUri());
        builder.addHints(slice.getHints());
        builder.setSpec(wrap(slice.getSpec()));
        for (SliceItem next : slice.getItems()) {
            String format = next.getFormat();
            format.hashCode();
            char c = 65535;
            switch (format.hashCode()) {
                case -1422950858:
                    if (format.equals("action")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1377881982:
                    if (format.equals("bundle")) {
                        c = 1;
                        break;
                    }
                    break;
                case 104431:
                    if (format.equals("int")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3327612:
                    if (format.equals("long")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3556653:
                    if (format.equals("text")) {
                        c = 4;
                        break;
                    }
                    break;
                case 100313435:
                    if (format.equals("image")) {
                        c = 5;
                        break;
                    }
                    break;
                case 100358090:
                    if (format.equals("input")) {
                        c = 6;
                        break;
                    }
                    break;
                case 109526418:
                    if (format.equals("slice")) {
                        c = 7;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    builder.addAction(next.getAction(), wrap(next.getSlice(), context), next.getSubType());
                    break;
                case 1:
                    builder.addItem(new SliceItem((Object) next.getBundle(), next.getFormat(), next.getSubType(), next.getHints()));
                    break;
                case 2:
                    builder.addInt(next.getInt(), next.getSubType(), next.getHints());
                    break;
                case 3:
                    builder.addLong(next.getLong(), next.getSubType(), next.getHints());
                    break;
                case 4:
                    builder.addText(next.getText(), next.getSubType(), next.getHints());
                    break;
                case 5:
                    try {
                        builder.addIcon(IconCompat.createFromIcon(context, next.getIcon()), next.getSubType(), next.getHints());
                        break;
                    } catch (IllegalArgumentException e) {
                        Log.w("SliceConvert", "The icon resource isn't available.", e);
                        break;
                    } catch (Resources.NotFoundException e2) {
                        Log.w("SliceConvert", "The icon resource isn't available.", e2);
                        break;
                    }
                case 6:
                    builder.addRemoteInput(next.getRemoteInput(), next.getSubType(), next.getHints());
                    break;
                case 7:
                    builder.addSubSlice(wrap(next.getSlice(), context), next.getSubType());
                    break;
            }
        }
        return builder.build();
    }

    private static SliceSpec wrap(SliceSpec sliceSpec) {
        if (sliceSpec == null) {
            return null;
        }
        return new SliceSpec(sliceSpec.getType(), sliceSpec.getRevision());
    }

    public static Set<SliceSpec> wrap(Set<SliceSpec> set) {
        ArraySet arraySet = new ArraySet();
        if (set != null) {
            for (SliceSpec wrap : set) {
                arraySet.add(wrap(wrap));
            }
        }
        return arraySet;
    }
}
