package app.revanced.integrations.youtube.settingsmenu;

import static app.revanced.integrations.youtube.utils.StringRef.str;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import app.revanced.integrations.youtube.settings.SettingsEnum;
import app.revanced.integrations.youtube.settings.SharedPrefCategory;
import app.revanced.integrations.youtube.utils.LogHelper;
import app.revanced.integrations.youtube.utils.ReVancedUtils;

/**
 * Shows what thumbnails will be used based on the current settings.
 * @noinspection ALL
 */
public class AlternativeThumbnailsStatusPreference extends Preference {

    private final SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, str) -> {
        // Because this listener may run before the ReVanced settings fragment updates SettingsEnum,
        // this could show the prior config and not the current.
        //
        // Push this call to the end of the main run queue,
        // so all other listeners are done and SettingsEnum is up to date.
        ReVancedUtils.runOnMainThread(this::updateUI);
    };

    public AlternativeThumbnailsStatusPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public AlternativeThumbnailsStatusPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public AlternativeThumbnailsStatusPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AlternativeThumbnailsStatusPreference(Context context) {
        super(context);
    }

    private void addChangeListener() {
        LogHelper.printDebug(() -> "addChangeListener");
        SharedPrefCategory.REVANCED.preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void removeChangeListener() {
        LogHelper.printDebug(() -> "removeChangeListener");
        SharedPrefCategory.REVANCED.preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        updateUI();
        addChangeListener();
    }

    @Override
    protected void onPrepareForRemoval() {
        super.onPrepareForRemoval();
        removeChangeListener();
    }

    private void updateUI() {
        LogHelper.printDebug(() -> "updateUI");
        final boolean usingDeArrow = SettingsEnum.ALT_THUMBNAIL_DEARROW.getBoolean();
        final boolean usingVideoStills = SettingsEnum.ALT_THUMBNAIL_STILLS.getBoolean();

        final String summaryTextKey;
        if (usingDeArrow && usingVideoStills) {
            summaryTextKey = "revanced_alt_thumbnail_about_status_dearrow_stills";
        } else if (usingDeArrow) {
            summaryTextKey = "revanced_alt_thumbnail_about_status_dearrow";
        } else if (usingVideoStills) {
            summaryTextKey = "revanced_alt_thumbnail_about_status_stills";
        } else {
            summaryTextKey = "revanced_alt_thumbnail_about_status_disabled";
        }

        setSummary(str(summaryTextKey));
    }
}
