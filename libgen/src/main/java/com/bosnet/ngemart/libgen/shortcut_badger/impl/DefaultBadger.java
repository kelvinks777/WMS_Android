package com.bosnet.ngemart.libgen.shortcut_badger.impl;

import com.bosnet.ngemart.libgen.shortcut_badger.Badger;
import com.bosnet.ngemart.libgen.shortcut_badger.ShortcutBadgeException;
import com.bosnet.ngemart.libgen.shortcut_badger.util.BroadcastHelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.Arrays;
import java.util.List;

/**
 * @author leolin
 */
public class DefaultBadger implements Badger {
    private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        if (BroadcastHelper.canResolveBroadcast(context, intent)) {
            context.sendBroadcast(intent);
        } else {
            throw new ShortcutBadgeException("unable to resolve intent: " + intent.toString());
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "fr.neamar.kiss",
                "com.quaap.launchtime",
                "com.quaap.launchtime_official"
        );
    }

    boolean isSupported(Context context) {
        Intent intent = new Intent(INTENT_ACTION);
        return BroadcastHelper.canResolveBroadcast(context, intent);
    }
}
