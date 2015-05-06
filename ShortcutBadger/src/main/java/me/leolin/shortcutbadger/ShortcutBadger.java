package me.leolin.shortcutbadger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;


import me.leolin.shortcutbadger.impl.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Leo Lin
 */
public abstract class ShortcutBadger {

    private static final List<BadgerFactory> BADGERS = new ArrayList<BadgerFactory>();

    static {
        BADGERS.add(new AdwHomeBadgerFactory());
        BADGERS.add(new AndroidHomeBadgerFactory());
        BADGERS.add(new ApexHomeBadgerFactory());
        BADGERS.add(new LgBadgerFactory());
        BADGERS.add(new HtcBadgerFactory());
        BADGERS.add(new NovaBadgerFactory());
        BADGERS.add(new SamsungBadgerFactory());
        BADGERS.add(new SolidBadgerFactory());
        BADGERS.add(new SonyBadgerFactory());
    }

    private static final int MIN_BADGE_COUNT = 0;
    private static final int MAX_BADGE_COUNT = 99;

    private static ShortcutBadger sInstance;

    private Context mContext;

    protected ShortcutBadger(Context context) {
        this.mContext = context;
    }

    protected abstract void executeBadge(int badgeCount) throws ShortcutBadgeException;

    public static void setBadge(Context context, int badgeCount) throws ShortcutBadgeException {
        //badgeCount should between 0 to 99
        badgeCount = Math.max(badgeCount, MIN_BADGE_COUNT);
        badgeCount = Math.min(badgeCount, MAX_BADGE_COUNT);
        //find the home launcher Package
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        try {
            ShortcutBadger shortcutBadger = getShortcutBadger(currentHomePackage, context);
            shortcutBadger.executeBadge(badgeCount);
        } catch (Throwable e) {
            throw new ShortcutBadgeException("Unable to execute badge:" + e.getMessage());
        }

    }

    private static ShortcutBadger getShortcutBadger(String currentHomePackage, Context context) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (sInstance != null) {
            return sInstance;
        }
        // Workaround for Meizu:
        // Meizu declare 'com.android.launcher', but hold something else
        // Icons get duplicated on restart after badge change
        if (Build.MANUFACTURER.toLowerCase().contains("meizu")) {
            return new StubShortcutBadger(context);
        }
        for (BadgerFactory factory : BADGERS) {
            ShortcutBadger shortcutBadger = factory.createBadger(context);
            if (shortcutBadger.getSupportLaunchers().contains(currentHomePackage)) {
                sInstance = shortcutBadger;
                break;
            }
        }
        return new StubShortcutBadger(context);
    }

    public abstract List<String> getSupportLaunchers();

    protected String getEntryActivityName() {
        PackageManager packageManager = mContext.getPackageManager();
        Intent launchIntent = packageManager == null ? null : packageManager.getLaunchIntentForPackage(mContext.getPackageName());
        ComponentName componentName = launchIntent == null ? null : launchIntent.getComponent();
        return componentName == null ? "" : componentName.getClassName();
    }

    protected String getContextPackageName() {
        return mContext.getPackageName();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public interface BadgerFactory {
        ShortcutBadger createBadger(Context context);
    }

    private static class AdwHomeBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new AdwHomeBadger(context);
        }
    }

    private static class AndroidHomeBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new AndroidHomeBadger(context);
        }
    }

    private static class ApexHomeBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new ApexHomeBadger(context);
        }
    }

    private static class LgBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new LGHomeBadger(context);
        }
    }

    private static class HtcBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new NewHtcHomeBadger(context);
        }
    }

    private static class NovaBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new NovaHomeBadger(context);
        }
    }

    private static class SamsungBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new SamsungHomeBadger(context);
        }
    }

    private static class SolidBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new SolidHomeBadger(context);
        }
    }

    private static class SonyBadgerFactory implements BadgerFactory {
        @Override
        public ShortcutBadger createBadger(Context context) {
            return new SonyHomeBadger(context);
        }
    }
}
