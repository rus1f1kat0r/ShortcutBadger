package me.leolin.shortcutbadger.impl;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by k.kharkov on 05.04.2015.
 */
public class StubShortcutBadger extends ShortcutBadger {

    public StubShortcutBadger(Context context) {
        super(context);
    }

    @Override
    protected void executeBadge(int badgeCount) throws ShortcutBadgeException {
        // empty
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Collections.emptyList();
    }
}
