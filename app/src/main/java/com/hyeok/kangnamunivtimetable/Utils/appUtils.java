package com.hyeok.kangnamunivtimetable.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.hyeok.kangnamunivtimetable.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;

@SuppressWarnings("unused")
public class appUtils {
    public static int FIRST_LOGIN = 1;
    public static final String GEO_EGONG = "geo:37.2770829,127.1341592";
    public static final String GEO_GYEONCHEON = "geo:37.2765152,127.1339019";
    public static final String GEO_HUSENG = "geo:37.2769126,127.1335131";
    public static final String GEO_CHEONN = "geo:37.2757035,127.1341903";
    public static final String GEO_GOYUK = "geo:37.275306,127.1332509";
    public static final String GEO_SEUNGLEE = "geo:37.274445,127.1323785";
    public static final String GEO_SHALROM = "geo:37.2749354,127.1300127";
    public static final String GEO_MOKYANG = "geo:37.2741456,127.1319862";
    public static final String GEO_WUWON = "geo:37.2757826,127.1316117";
    public static final String GEO_INSA = "geo:37.2752595,127.1307101";
    public static final String GEO_YESUL = "geo:37.27607,127.1309304";
    public static final String GEO_UNDONG = "geo:37.2746936,127.1313567";

    public static String getURLDecode(String content) {
        try {
            return URLDecoder.decode(content, "utf-8");   // UTF-8
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getName(String content) {
        return new String(Base64.decode(content, 0));
    }

    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String TIME(Context mContext, int tmp, int i) {
        ControlSharedPref pref = new ControlSharedPref(mContext, "timetable.pref");
        String Starttime = pref.getValue("time" + tmp, "").split("-")[0];
        String Lasttime = pref.getValue("time" + i, "").split("-")[1];
        return Starttime + "-" + Lasttime;
    }

    public static boolean NetWorkState(Context context) {
        ConnectivityManager cManager;
        NetworkInfo mobile;
        NetworkInfo wifi;
        boolean state = false;
        cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            state = true;
        }
        return state;
    }

    private float pxFromDp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String getDay(Context context) {
        String yoil = "null";
        Calendar mCalendar = Calendar.getInstance();
        int id_yoil = mCalendar.get(Calendar.DAY_OF_WEEK);
        switch (id_yoil) {
            case 0:
                yoil = context.getResources().getString(R.string.DAY_saturday);
                break;
            case 1:
                yoil = context.getResources().getString(R.string.DAY_sunday);
                break;
            case 2:
                yoil = context.getResources().getString(R.string.DAY_monday);
                break;
            case 3:
                yoil = context.getResources().getString(R.string.DAY_tuesday);
                break;
            case 4:
                yoil = context.getResources().getString(R.string.DAY_wendsday);
                break;
            case 5:
                yoil = context.getResources().getString(R.string.DAY_thursday);
                break;
            case 6:
                yoil = context.getResources().getString(R.string.DAY_friday);
                break;
        }
        return yoil;
    }

    public static String getFullClassName(String tmp_msg) {
        if (tmp_msg.contains("경")) {
            tmp_msg = tmp_msg.replace("경", "경천관");
        } else if (tmp_msg.contains("이")) {
            tmp_msg = tmp_msg.replace("이", "이공관");
        } else if (tmp_msg.contains("천")) {
            tmp_msg = tmp_msg.replace("천", "천은관");
        } else if (tmp_msg.contains("교")) {
            tmp_msg = tmp_msg.replace("교", "교육관");
        } else if (tmp_msg.contains("후")) {
            tmp_msg = tmp_msg.replace("후", "후생관");
        } else if (tmp_msg.contains("우")) {
            tmp_msg = tmp_msg.replace("우", "우원관");
        } else if (tmp_msg.contains("예")) {
            tmp_msg = tmp_msg.replace("예", "예술관");
        } else if (tmp_msg.contains("목")) {
            tmp_msg = tmp_msg.replace("목", "목양관");
        } else if (tmp_msg.contains("인")) {
            tmp_msg = tmp_msg.replace("인", "인사관");
        } else if (tmp_msg.contains("승")) {
            tmp_msg = tmp_msg.replace("승", "승리관");
        } else if (tmp_msg.contains("샬")) {
            tmp_msg = tmp_msg.replace("샬", "샬롬관");
        } else if (tmp_msg.contains("운")) {
            tmp_msg = tmp_msg.replace("운", "운동장");
        }
        return tmp_msg;
    }
}
