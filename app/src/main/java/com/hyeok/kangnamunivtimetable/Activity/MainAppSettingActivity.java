package com.hyeok.kangnamunivtimetable.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hyeok.kangnamunivtimetable.CustomViews.DialogSeekBarPreference;
import com.hyeok.kangnamunivtimetable.CustomViews.SeekBarPreference;
import com.hyeok.kangnamunivtimetable.R;
import com.hyeok.kangnamunivtimetable.Utils.ControlSharedPref;
import com.hyeok.kangnamunivtimetable.Utils.GetTimetableUtils;
import com.hyeok.kangnamunivtimetable.Utils.appUtils;
import com.hyeok.kangnamunivtimetable.widget.TimeTableWidget;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

@SuppressWarnings({"deprecation", "ConstantConditions", "FieldCanBeLocal"})
public class MainAppSettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public static String UPDATE_INTERVAL_data = "updateinterval";
    public static String TTB_COLOR_MON_data = "ttbmoncolor";
    public static String TTB_COLOR_TUE_data = "ttbtuecolor";
    public static String TTB_COLOR_WEN_data = "ttbwencolor";
    public static String TTB_COLOR_THUR_data = "ttbthurcolor";
    public static String TTB_COLOR_FRI_data = "ttbfricolor";
    public static String TTB_TIME_SIZE_data = "ttbtimesize";
    public static String TTB_CLASS_SIZE_data = "ttbclasssize";
    public static String TTB_SUBJECT_SIZE_data = "ttbsubjectsize";
    public static String GONGGANG_MESSAGE_data = "gonggangmsg";
    public static String WIDGET_TEXT_SIZE = "widgettextsize";
    public static String WIDGET_TEXT_COLOR = "widgettextcolor";
    public static String WIDGET_BACKGROUND_COLOR = "widgetbgcolor";
    public static String WIDGET_UPDATE_TIME = "widgetupdatetime";
    public static String TTB_THEME = "ttbtheme";
    public static String TTB_TABLE_TEXT_SIZE = "tabletextsize";

    private final String KEY_ACCOUT_NAME = "key_accout_name";
    private final String KEY_ACCOUT_LOGOUT = "key_accout_logout";
    private final String KEY_WIDGET_INTERVAL = "key_widget_interval";
    private final String KEY_REFRESH = "key_refresh";
    private final String KEY_TTB_COLOR_MON = "key_color_mon";
    private final String KEY_TTB_COLOR_TUE = "key_color_tue";
    private final String KEY_TTB_COLOR_WEN = "key_color_wen";
    private final String KEY_TTB_COLOR_THUR = "key_color_thur";
    private final String KEY_TTB_COLOR_FRI = "key_color_fri";
    private final String KEY_GONGGANG_MESSAGE = "key_gonggang_message";
    private final String KEY_WIDGET_TEXT_SIZE = "key_widget_text";
    private final String KEY_WIDGET_TEXT_COLOR = "key_widget_text_color";
    private final String KEY_WIDGET_UPDATE_TIME = "key_widget_update_time";
    private final String KEY_TTB_TIME_SIZE = "key_time_text_size";
    private final String KEY_TTB_SUBJECT_SIZE = "key_class_text_size";
    private final String KEY_TTB_CLASS_SIZE = "key_subject_text_size";
    private final String KEY_DEVELOPER_SCREEN = "Developer_screen_pref";
    private final String KEY_TTB_THEME = "key_time_table_theme";
    private final String KEY_BUGREPORT = "Developer_feedback";
    private final String KEY_TABLE_TEXT_SIZE = "key_table_text_size";
    private final String KEY_WIDGET_BG_COLOR = "key_widget_background_color";

    ListPreference mintervalpreference, mttbtheme;
    ColorPickerPreference mColorpickerpref_mon, mColorpickerpref_tue, mColorpickerpref_wen, mColorpickerpref_thur, mColorpickerpref_fri, mColorpicker_widget_text_color, mColorpicker_widget_bg_color;
    EditTextPreference mgonggangmsg;
    SeekBarPreference mwidgettext;
    DialogSeekBarPreference mttbTimeSize, mttbClassSize, mttbSubjectSize, mttbTableSize;
    Preference mDeveloperScreen, mBugReport;

    private ControlSharedPref pref = new ControlSharedPref(this, null);
    private ControlSharedPref timetablepref = new ControlSharedPref(this, "timetable.pref");
    private ControlSharedPref settingpref = new ControlSharedPref(this, "Setting.pref");
    private ControlSharedPref realpref = new ControlSharedPref(this, "com.hyeok.kangnamunivtimetable_preferences");
    private ControlSharedPref bus_pref = new ControlSharedPref(this, "shuttlebus.pref");
    private ControlSharedPref MemoDate = new ControlSharedPref(this, "MemoDate");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainsetting);
        // 액션바 아이콘 제거
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xFF666666));
        try {
            String tmp_realname = appUtils.getName(pref.getValue("name_e", null).split(";")[0].substring(12).replaceAll("\"", ""));
            String realname = appUtils.getURLDecode(tmp_realname);
//            String id = pref.getValue("id", null).substring(2, 4);
            SetSummary(KEY_ACCOUT_NAME, realname);
        } catch (NullPointerException ignored) {
        }

        /**
         * Set Developer Screen Click
         */
        mDeveloperScreen = findPreference(KEY_DEVELOPER_SCREEN);
        mBugReport = findPreference(KEY_BUGREPORT);

        /**
         * Time table Text Size
         */
        mttbTimeSize = (DialogSeekBarPreference) findPreference(KEY_TTB_TIME_SIZE);
        mttbClassSize = (DialogSeekBarPreference) findPreference(KEY_TTB_CLASS_SIZE);
        mttbSubjectSize = (DialogSeekBarPreference) findPreference(KEY_TTB_SUBJECT_SIZE);
        mttbTableSize = (DialogSeekBarPreference) findPreference(KEY_TABLE_TEXT_SIZE);
        int currenttimesize = settingpref.getValue(TTB_TIME_SIZE_data, 16);
        int currentclasssize = settingpref.getValue(TTB_CLASS_SIZE_data, 14);
        int currentsubjectsize = settingpref.getValue(TTB_SUBJECT_SIZE_data, 18);
        int currenttablesize = settingpref.getValue(TTB_TABLE_TEXT_SIZE, 14);
        mttbTimeSize.setProgress(currenttimesize);
        mttbClassSize.setProgress(currentclasssize);
        mttbSubjectSize.setProgress(currentsubjectsize);
        mttbTableSize.setProgress(currenttablesize);
        mttbTimeSize.setOnPreferenceChangeListener(this);
        mttbClassSize.setOnPreferenceChangeListener(this);
        mttbSubjectSize.setOnPreferenceChangeListener(this);
        mttbTableSize.setOnPreferenceChangeListener(this);

        /**
         * Widget UpdateTime Summary
         */
        String updatetimesummary = settingpref.getValue(WIDGET_UPDATE_TIME, getResources().getString(R.string.SETTING_WIDGET_UPDATE_TIME_NODATA));
        SetSummary(KEY_WIDGET_UPDATE_TIME, updatetimesummary);

        /**
         * Widget Text Size
         */
        mwidgettext = (SeekBarPreference) findPreference(KEY_WIDGET_TEXT_SIZE);
        mwidgettext.setOnPreferenceChangeListener(this);
        int defaultwidgettext = settingpref.getValue(WIDGET_TEXT_SIZE, 12);
        mwidgettext.setValue(defaultwidgettext);

        /**
         * Widget Text Color
         */
        mColorpicker_widget_text_color = (ColorPickerPreference) findPreference(KEY_WIDGET_TEXT_COLOR);
        mColorpicker_widget_text_color.setHexValueEnabled(true);
        mColorpicker_widget_text_color.setAlphaSliderEnabled(true);
        mColorpicker_widget_text_color.setOnPreferenceChangeListener(this);

        /**
         * Widget Background Color
         */
//        mColorpicker_widget_bg_color = (ColorPickerPreference) findPreference(KEY_WIDGET_BG_COLOR);
//        mColorpicker_widget_bg_color.setHexValueEnabled(true);
//        mColorpicker_widget_bg_color.setAlphaSliderEnabled(true);
//        mColorpicker_widget_bg_color.setOnPreferenceChangeListener(this);

        /**
         *  WIDGET UPDATE INVERVAL
         */
        mintervalpreference = (ListPreference) findPreference(KEY_WIDGET_INTERVAL);
        int current_updateinterval_value = settingpref.getValue(UPDATE_INTERVAL_data, 1);
        int current_updateinterval;
        switch (current_updateinterval_value) {
            case 1:
                current_updateinterval = 0;
                break;
            case 6:
                current_updateinterval = 1;
                break;
            case 12:
                current_updateinterval = 2;
                break;
            case 24:
                current_updateinterval = 3;
                break;
            default:
                current_updateinterval = 1;
                break;
        }
        mintervalpreference.setValueIndex(current_updateinterval);
        mintervalpreference.setOnPreferenceChangeListener(this);
        mintervalpreference.setSummary(mintervalpreference.getEntry());

        /**
         * GONGGANG MESSAGE
         */
        mgonggangmsg = (EditTextPreference) findPreference(KEY_GONGGANG_MESSAGE);
        String currentmsg = settingpref.getValue(GONGGANG_MESSAGE_data, getResources().getString(R.string.DAY_GONGGANG));
        mgonggangmsg.setText(currentmsg);
        mgonggangmsg.setSummary(currentmsg);
        mgonggangmsg.setOnPreferenceChangeListener(this);

        /**
         *  TIMETABLE ACTIONBAR COLOR
         */
        mColorpickerpref_mon = (ColorPickerPreference) findPreference(KEY_TTB_COLOR_MON);
        mColorpickerpref_tue = (ColorPickerPreference) findPreference(KEY_TTB_COLOR_TUE);
        mColorpickerpref_wen = (ColorPickerPreference) findPreference(KEY_TTB_COLOR_WEN);
        mColorpickerpref_thur = (ColorPickerPreference) findPreference(KEY_TTB_COLOR_THUR);
        mColorpickerpref_fri = (ColorPickerPreference) findPreference(KEY_TTB_COLOR_FRI);

        mColorpickerpref_mon.setHexValueEnabled(true);
        mColorpickerpref_tue.setHexValueEnabled(true);
        mColorpickerpref_wen.setHexValueEnabled(true);
        mColorpickerpref_thur.setHexValueEnabled(true);
        mColorpickerpref_fri.setHexValueEnabled(true);

        mColorpickerpref_mon.setAlphaSliderEnabled(true);
        mColorpickerpref_tue.setAlphaSliderEnabled(true);
        mColorpickerpref_wen.setAlphaSliderEnabled(true);
        mColorpickerpref_thur.setAlphaSliderEnabled(true);
        mColorpickerpref_fri.setAlphaSliderEnabled(true);

        mColorpickerpref_mon.setOnPreferenceChangeListener(this);
        mColorpickerpref_tue.setOnPreferenceChangeListener(this);
        mColorpickerpref_wen.setOnPreferenceChangeListener(this);
        mColorpickerpref_thur.setOnPreferenceChangeListener(this);
        mColorpickerpref_fri.setOnPreferenceChangeListener(this);

        /**
         * TIMETABLE THEME
         */
        mttbtheme = (ListPreference) findPreference(KEY_TTB_THEME);
        mttbtheme.setValueIndex(settingpref.getValue(TTB_THEME, 0));
        mttbtheme.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals(KEY_ACCOUT_LOGOUT)) {
            AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
            dialog_builder.setTitle(MainAppSettingActivity.this.getResources().getString(R.string.menu_logout));
            dialog_builder.setMessage(MainAppSettingActivity.this.getResources().getString(R.string.SETTING_LOGOUT_MESSAGE));
            dialog_builder.setPositiveButton(MainAppSettingActivity.this.getResources().getString(R.string.SETTING_LOTOUT_OK), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pref.clearAll();
                    timetablepref.clearAll();
                    bus_pref.clearAll();
                    MemoDate.clearAll();
                    Intent intent = new Intent(MainAppSettingActivity.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            dialog_builder.setNegativeButton(MainAppSettingActivity.this.getResources().getString(R.string.SETTING_LOGOUT_CANCEL), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog_builder.create();
            dialog_builder.show();
            return true;
        } else if (preference.getKey().equals(KEY_REFRESH)) {
            resetDialog();
            return true;
        } else if (preference.getKey().equals(KEY_DEVELOPER_SCREEN)) {
            startActivity(new Intent(this, DeveloperInformTab.class));
        } else if (preference.getKey().equals(KEY_BUGREPORT)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:kh4975@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.SETTING_DEVELOPER_FEEDBACK_SUBJECT));
            startActivity(Intent.createChooser(intent, null));
        }
        return false;

    }

    public void SetSummary(String key, String value) {
        findPreference(key).setSummary(value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingreset, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                class getttb extends AsyncTask<String, Integer, String> {
                    ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        dialog = new ProgressDialog(MainAppSettingActivity.this);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage(getResources().getString(R.string.SETTING_TIMETABLE_REFRESH_DOING));
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        dialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        dialog.dismiss();
                        super.onPostExecute(result);
                    }

                    @Override
                    protected String doInBackground(String... arg0) {
                        GetTimetableUtils.RefreshTimeTable(getContext());
                        return null;
                    }
                }
                getttb ttb = new getttb();
                ttb.execute();
                break;
        }
        return false;
    }

    private Context getContext() {
        return this;
    }

    private void reset() {
        realpref.clearAll();
        settingpref.clearAll();
        finish();
        startActivity(new Intent(this, MainAppSettingActivity.class));
    }

    private void resetDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(getResources().getString(R.string.SETTING_RESET_MSG)).setCancelable(
                false).setPositiveButton(getResources().getString(R.string.SETTING_OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        reset();
                    }
                }
        ).setNegativeButton(getResources().getString(R.string.SETTING_NO), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.setTitle(getResources().getString(R.string.SETTING_RESET_TITLE));
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newvalue) {
        if (preference == mintervalpreference) {
            int index = mintervalpreference.findIndexOfValue(newvalue.toString());
            CharSequence[] entries = mintervalpreference.getEntries();
            mintervalpreference.setSummary(entries[index]);
            int time = Integer.parseInt((String) newvalue);
            settingpref.put(UPDATE_INTERVAL_data, time);
            Intent intent = new Intent(this, TimeTableWidget.class);
            sendBroadcast(intent);
            String updatetimesummary = settingpref.getValue(WIDGET_UPDATE_TIME, getResources().getString(R.string.SETTING_WIDGET_UPDATE_TIME_NODATA));
            SetSummary(KEY_WIDGET_UPDATE_TIME, updatetimesummary);
            return true;
        } else if (preference == mColorpickerpref_mon) {
            settingpref.put(TTB_COLOR_MON_data, Integer.parseInt(newvalue.toString()));
            return true;
        } else if (preference == mColorpickerpref_tue) {
            settingpref.put(TTB_COLOR_TUE_data, Integer.parseInt(newvalue.toString()));
            return true;
        } else if (preference == mColorpickerpref_wen) {
            settingpref.put(TTB_COLOR_WEN_data, Integer.parseInt(newvalue.toString()));
            return true;
        } else if (preference == mColorpickerpref_thur) {
            settingpref.put(TTB_COLOR_THUR_data, Integer.parseInt(newvalue.toString()));
            return true;
        } else if (preference == mColorpickerpref_fri) {
            settingpref.put(TTB_COLOR_FRI_data, Integer.parseInt(newvalue.toString()));
            return true;
        } else if (preference == mgonggangmsg) {
            if (newvalue.toString().equals(""))
                newvalue = (getResources().getString(R.string.DAY_GONGGANG));
            settingpref.put(GONGGANG_MESSAGE_data, newvalue.toString());
            mgonggangmsg.setSummary(newvalue.toString());
            return true;
        } else if (preference == mwidgettext) {
            settingpref.put(WIDGET_TEXT_SIZE, Integer.parseInt(newvalue.toString()));
            Intent intent = new Intent(this, TimeTableWidget.class);
            sendBroadcast(intent);
            String updatetimesummary = settingpref.getValue(WIDGET_UPDATE_TIME, getResources().getString(R.string.SETTING_WIDGET_UPDATE_TIME_NODATA));
            SetSummary(KEY_WIDGET_UPDATE_TIME, updatetimesummary);
            return true;
        } else if (preference == mColorpicker_widget_text_color) {
            settingpref.put(WIDGET_TEXT_COLOR, Integer.parseInt(newvalue.toString()));
            Intent intent = new Intent(this, TimeTableWidget.class);
            sendBroadcast(intent);
            String updatetimesummary = settingpref.getValue(WIDGET_UPDATE_TIME, getResources().getString(R.string.SETTING_WIDGET_UPDATE_TIME_NODATA));
            SetSummary(KEY_WIDGET_UPDATE_TIME, updatetimesummary);
            return true;
        } else if (preference == mColorpicker_widget_bg_color) {
            settingpref.put(WIDGET_BACKGROUND_COLOR, Integer.parseInt(newvalue.toString()));
            Intent intent = new Intent(this, TimeTableWidget.class);
            sendBroadcast(intent);
            String updatetimesummary = settingpref.getValue(WIDGET_UPDATE_TIME, getResources().getString(R.string.SETTING_WIDGET_UPDATE_TIME_NODATA));
            SetSummary(KEY_WIDGET_UPDATE_TIME, updatetimesummary);
            return true;
        } else if (preference == mttbTimeSize) {
            settingpref.put(TTB_TIME_SIZE_data, Integer.parseInt(newvalue.toString()));
            //mttbTimeSize.setProgress(Integer.parseInt(newvalue.toString()));
        } else if (preference == mttbClassSize) {
            settingpref.put(TTB_CLASS_SIZE_data, Integer.parseInt(newvalue.toString()));
            //mttbClassSize.setProgress(Integer.parseInt(newvalue.toString()));
        } else if (preference == mttbSubjectSize) {
            settingpref.put(TTB_SUBJECT_SIZE_data, Integer.parseInt(newvalue.toString()));
            //mttbSubjectSize.setProgress(Integer.parseInt(newvalue.toString()));
        } else if (preference == mttbtheme) {
            settingpref.put(TTB_THEME, Integer.parseInt(newvalue.toString()));
            setResult(0);
        } else if (preference == mttbTableSize) {
            settingpref.put(TTB_TABLE_TEXT_SIZE, Integer.parseInt(newvalue.toString()));
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
