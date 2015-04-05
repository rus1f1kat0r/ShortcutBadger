package me.leolin.shortcutbadger.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import me.leolin.shortcutbadger.util.ImageUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author Leo Lin
 */
public class AndroidHomeBadger extends ShortcutBadger {
    private static final String CONTENT_URI = "content://com.android.launcher2.settings/favorites?notify=true";

    public AndroidHomeBadger(Context context) {
        super(context);
    }

    @Override
    protected void executeBadge(int badgeCount) throws ShortcutBadgeException {
        byte[] bytes = ImageUtil.drawBadgeOnAppIcon(getContext(), badgeCount);
        String appName = getContext().getResources().getText(getContext().getResources().getIdentifier("app_name",
                "string", getContextPackageName())).toString();

        Uri mUri = Uri.parse(CONTENT_URI);
        ContentResolver contentResolver = getContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("iconType", 1);
        contentValues.put("itemType", 1);
        contentValues.put("icon", bytes);
        contentResolver.update(mUri, contentValues, "title=?", new String[]{appName});
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.android.launcher",
                "com.android.launcher2",
                "com.google.android.googlequicksearchbox"
        );
    }
}
