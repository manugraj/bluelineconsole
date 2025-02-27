package net.nhiroki.bluelineconsole.applicationMain.theming.eachTheme;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;

import net.nhiroki.bluelineconsole.R;
import net.nhiroki.bluelineconsole.applicationMain.BaseWindowActivity;
import net.nhiroki.bluelineconsole.applicationMain.theming.AppTheme;


public class OldComputerTheme implements AppTheme {
    private static final String THEME_ID = "old_computer";
    private static final @StringRes int THEME_TITLE_STRING_RES = R.string.theme_name_old_computer;


    @Override
    public void apply(Activity activity, boolean iAmHomeActivity, boolean smallWindow) {
        activity.setTheme(iAmHomeActivity ? R.style.AppThemeOldComputerHome : R.style.AppThemeOldComputer);
        activity.setContentView(R.layout.base_window_layout_old_computer);
    }

    @Override
    public void applyAccentColor(Activity activity, int color) {
    }

    @Override
    public void configureDarkMode(Context context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public String getThemeID() {
        return THEME_ID;
    }

    @Override
    public CharSequence getThemeTitle(Context context) {
        return context.getString(THEME_TITLE_STRING_RES);
    }

    @Override
    public boolean hasFooter() {
        return false;
    }

    @Override
    public void onCreateFinal(BaseWindowActivity activity) {
    }

    @Override
    public void enableWindowAnimationForElement(BaseWindowActivity activity) {
    }

    @Override
    public void disableWindowAnimationForElement(BaseWindowActivity activity) {
    }

    @Override
    public boolean supportsAccentColor() {
        return false;
    }

    @Override
    public void changeBaseWindowElementSizeForAnimation(Activity activity, boolean visible, boolean smallWindow) {
    }

    @Override
    public int getLauncherWidgetLayoutID(Context context) {
        return R.layout.widget_launcher_old_computer;
    }
}
