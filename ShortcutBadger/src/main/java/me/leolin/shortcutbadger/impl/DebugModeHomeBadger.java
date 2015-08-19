package me.leolin.shortcutbadger.impl;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author Ildar Galimbikov
 */
public class DebugModeHomeBadger extends ShortcutBadger {

    private static final String INTENT_ACTION = "ru.mail.mailapp.BADGER_COUNT";
    private static final String INTENT_EXTRA_COUNT = "ru.mail.mailapp.extra.COUNT";


    public DebugModeHomeBadger(Context context) {
        super(context);
    }

    @Override
    protected void executeBadge(int badgeCount) {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_COUNT, badgeCount);
        getContext().sendBroadcast(intent);
    }

    @Override
    public List<String> getSupportLaunchers() {
        return new ArrayList<String>();
    }
}
