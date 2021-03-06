package com.hyeok.kangnamunivtimetable.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyeok.kangnamunivtimetable.CustomViews.IButton;
import com.hyeok.kangnamunivtimetable.R;
import com.hyeok.kangnamunivtimetable.Utils.ControlSharedPref;
import com.hyeok.kangnamunivtimetable.Utils.appUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class login extends ActionBarActivity {
    String TAG = "KangNam.Univ.TB";
    private ProgressDialog dialog;
    private ImageView iconLogo;
    private LinearLayout loginMain;
    public static String jsession, idno, mjco, name, name_e, pass, auto, gubn;
    public static String MIDDLE_EXAM_PREF = "Middle_exam";
    public static String FINAL_EXAM_PREF = "Final_exam";
    private boolean m_close_flag = false;

    @Override
    public void onConfigurationChanged(Configuration cfg) {
        super.onConfigurationChanged(cfg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //noinspection ConstantConditions
        getActionBar().hide();
        final EditText tvid, tvpw;
        final IButton btn;
        tvid = (EditText) findViewById(R.id.login_id);
        tvpw = (EditText) findViewById(R.id.login_pw);
        btn = (IButton) findViewById(R.id.login_loginbtn1);
        RelativeLayout login_layout = (RelativeLayout) findViewById(R.id.login_layout);
        setBackGround(login_layout, R.drawable.loginback);
        tvid.setNextFocusDownId(R.id.login_pw);
        tvpw.setNextFocusDownId(R.id.login_loginbtn1);
        Drawable ic_st_symbol = getResources().getDrawable(R.drawable.ic_st_login);
        Drawable ic_pw_symbol = getResources().getDrawable(R.drawable.ic_pw_login);
        Drawable ic_login_symbol = getResources().getDrawable(R.drawable.ic_login_login);
        ic_st_symbol.setBounds(0, 0, 80, 80);
        ic_pw_symbol.setBounds(0, 0, 80, 80);
        ic_login_symbol.setBounds(0, 0, 80, 80);
        tvid.setCompoundDrawables(ic_st_symbol, null, null, null);
        tvpw.setCompoundDrawables(ic_pw_symbol, null, null, null);
        viewAnimation();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String id = tvid.getText().toString();
                String pw = tvpw.getText().toString();
                if (id.equals("") || pw.equals("")) {
                    LoginBlankDialog();
                } else {
                    dialog = new ProgressDialog(login.this);
                    dialog.setMessage(login.this.getResources().getString(R.string.login_login_working));
                    dialog.setTitle(login.this.getResources().getString(R.string.login_btn_msg));
                    dialog.setCancelable(false);
                    dialog.setIndeterminate(true);
                    dialog.show();
                    Work loginwork = new Work(id, pw);
                    loginwork.start();
                }
            }
        });
    }

    Handler backhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            m_close_flag = false;
        }
    };

    private void viewAnimation() {
        iconLogo = (ImageView) findViewById(R.id.lgoin_imageView1);
        loginMain = (LinearLayout) findViewById(R.id.login_linearLayout1);
        final Drawable drawable = getResources().getDrawable(R.drawable.ic_kangnam_logo);
        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_kangnam_logo_2);
        iconLogo.setImageDrawable(drawable);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Animation fade = AnimationUtils.loadAnimation(login.this, R.anim.anim_login_fade);
//                fade.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        iconLogo.setImageDrawable(drawable);
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                iconLogo.startAnimation(fade);
//            }
//        }, 1000);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ControlSharedPref pref = new ControlSharedPref(login.this, null);
                if (pref.getValue("name", null) != null) {
                    finish();
                    startActivity(new Intent(login.this, TimeTableMain.class));
                    return;
                }
                Animation transition = AnimationUtils.loadAnimation(login.this, R.anim.anim_login_translate);
                transition.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation fade = AnimationUtils.loadAnimation(login.this, R.anim.anim_login_fade);
                        loginMain.setVisibility(View.VISIBLE);
                        loginMain.startAnimation(fade);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iconLogo.startAnimation(transition);
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        if (!m_close_flag) {
            Toast.makeText(this,
                    getResources().getString(R.string.DOUBLE_BACK_KILL_APP),
                    Toast.LENGTH_SHORT).show();
            m_close_flag = true;
            backhandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            super.onBackPressed();
        }
    }

    private void setBackGround(RelativeLayout layout, int resid) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 2;
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid, options);
        if (Build.VERSION.SDK_INT >= 16) {
            layout.setBackground(new BitmapDrawable(getResources(), bitmap));
        } else {
            //noinspection deprecation
            layout.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
    }

    private void LoginBlankDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(getResources().getString(R.string.login_blank_msg)).setCancelable(
                false).setPositiveButton(getResources().getString(R.string.login_blank_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }
        );
        AlertDialog alert = alt_bld.create();
        alert.setTitle(getResources().getString(R.string.login_blank_title));
        alert.show();
    }


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(login.this);
            alt_bld.setMessage(getResources().getString(R.string.login_fail_msg)).setCancelable(
                    false).setPositiveButton(getResources().getString(R.string.login_blank_confirm),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }
            );
            AlertDialog alert = alt_bld.create();
            alert.setTitle(getResources().getString(R.string.login_blank_title));
            alert.show();
        }
    };

    class Work extends Thread {
        String id, pw;

        public Work(String id, String pw) {
            this.id = id;
            this.pw = pw;
        }

        @Override
        public void run() {
            LoginWork(id, pw);
        }

        private void LoginWork(String id, String pw) {
            /**
             * Login part
             */
            try {
                String param = "user_id=" + id + "&user_pwd=" + pw;
                String url = "https://m.kangnam.ac.kr/knusmart/c/c001.do?";
                url = url + param;
                URL targetURL = new URL(url);
                URLConnection urlConn = targetURL.openConnection();
                HttpsURLConnection request = (HttpsURLConnection) urlConn;
                //request.addRequestProperty("User-Agent", "");
                //request.addRequestProperty("Host", "");
                request.setUseCaches(false);
                request.setDoOutput(true);
                request.setDoInput(true);
                HttpURLConnection.setFollowRedirects(true);
                request.setInstanceFollowRedirects(true);
                request.setRequestMethod("GET");
                OutputStream opstrm = request.getOutputStream();
                opstrm.write(param.getBytes());
                opstrm.flush();
                opstrm.close();
                List<String> cookies = request.getHeaderFields().get("Set-Cookie");
                idno = cookies.get(0);
                gubn = cookies.get(1);
                name = cookies.get(2);
                pass = cookies.get(3);
                auto = cookies.get(4);
                mjco = cookies.get(5);
                name_e = cookies.get(6);
                jsession = cookies.get(7);
                final ControlSharedPref pref = new ControlSharedPref(login.this, null);
                pref.clearAll();
                pref.put("idno", idno);
                pref.put("gubn", gubn);
                pref.put("name", name);
                pref.put("pass", pass);
                pref.put("auto", auto);
                pref.put("mjco", mjco);
                pref.put("name_e", name_e);
                pref.put("jsessiion", jsession);
                pref.put("id", id);
                /**
                 * Get Timetable Part
                 */
                try {
                    ControlSharedPref timetablepref = new ControlSharedPref(login.this, "timetable.pref");
                    URL siganpyo = new URL("https://m.kangnam.ac.kr/knusmart/s/s251.do");
                    URLConnection urlConn1 = siganpyo.openConnection();
                    HttpsURLConnection request1 = (HttpsURLConnection) urlConn1;
                    timetablepref.clearAll();
                    String idno = pref.getValue("idno", null).replaceAll("&quot;", "\"").split(";")[0];
                    String gubn = pref.getValue("gubn", null).replaceAll("&quot;", "\"").split(";")[0];
                    String name = pref.getValue("name", null).replaceAll("&quot;", "\"").split(";")[0];
                    String pass = pref.getValue("pass", null).replaceAll("&quot;", "\"").split(";")[0];
                    String auto = pref.getValue("auto", null).replaceAll("&quot;", "\"").split(";")[0];
                    String mjco = pref.getValue("mjco", null).replaceAll("&quot;", "\"").split(";")[0];
                    String name_e = pref.getValue("name_e", null).replaceAll("&quot;", "\"").split(";")[0];
                    String jsession = pref.getValue("jsessiion", null).replaceAll("&quot;", "\"").split(";")[0];
                    request1.addRequestProperty("Cookie", jsession + ";" + name + ";" + mjco + ";" + auto + ";" + pass + ";" + name_e + ";" + gubn + ";" + idno + ";");
                    request1.setUseCaches(false);
                    request1.setDoOutput(true);
                    request1.setDoInput(true);
                    HttpURLConnection.setFollowRedirects(true);
                    request1.setInstanceFollowRedirects(true);
                    request1.setRequestMethod("GET");
                    System.out.println(request1.getConnectTimeout());
                    InputStreamReader reader = new InputStreamReader(request1.getInputStream());
                    JSONParser jp = new JSONParser();
                    JSONObject jo = (JSONObject) jp.parse(reader);
                    JSONArray ja = (JSONArray) jo.get("data");
                    for (int rep = 0; rep < ja.size(); rep++) {
                        JSONObject jo2 = (JSONObject) ja.get(rep);
                        timetablepref.put("mon_" + rep, "" + jo2.get("time_day1"));
                        timetablepref.put("tues" + rep, "" + jo2.get("time_day2"));
                        timetablepref.put("wends" + rep, "" + jo2.get("time_day3"));
                        timetablepref.put("thur" + rep, "" + jo2.get("time_day4"));
                        timetablepref.put("fri" + rep, "" + jo2.get("time_day5"));
                        timetablepref.put("time" + rep, "" + jo2.get("real_time").toString().replaceAll(" ", ""));
                    }
                    /**
                     * Get Exam Time Part
                     */
                    ArrayList<Element> al = new ArrayList<Element>();
                    Document a = Jsoup.connect("http://web.kangnam.ac.kr/edu/edu_schedule/edu_schedule.jsp").get();
                    int size = a.getElementsByClass("contTable").size();
                    Calendar clr = Calendar.getInstance();
                    if (clr.get(Calendar.MONTH) + 1 < 7) {
                        for (int i = 0; i < size; i++) {
                            al.add(a.getElementsByClass("contTable").get(i));
                            if (i == 5) break;
                        }
                    } else {
                        for (int i = 6; i < size; i++) {
                            al.add(a.getElementsByClass("contTable").get(i));
                        }
                    }

                    for (Element anAl : al) {
                        Elements Month = anAl.getElementsByTag("tr");
                        for (Element aMonth : Month) {
                            String ExamTime = aMonth.getElementsByTag("td").html();
                            if (ExamTime.contains("중간시험")) {
                                pref.put(MIDDLE_EXAM_PREF, aMonth.getElementsByTag("th").html());
                            } else if (ExamTime.contains("기말시험")) {
                                pref.put(FINAL_EXAM_PREF, aMonth.getElementsByTag("th").html());
                            }
                        }
                    }

                    /**
                     * Shuttle Bus Part
                     */

                    final ControlSharedPref bus_pref = new ControlSharedPref(login.this, "shuttlebus.pref");
                    JSONParser parser = new JSONParser();
                    URL bus_url = new URL("https://m.kangnam.ac.kr/knusmart/p/p128.do");
                    InputStream is = bus_url.openStream();
                    Scanner sc = new Scanner(is);
                    String json = "";
                    while (sc.hasNext()) {
                        json += sc.next();
                    }
                    JSONObject job = (JSONObject) parser.parse(json);
                    JSONArray jar = (JSONArray) job.get("data");
                    for (Object aJar : jar) {
                        JSONObject jar_2 = (JSONObject) aJar;
                        long idx = (Long) jar_2.get("idx");
                        String kh_start = (String) jar_2.get("kh_start");
                        String ek_start = (String) jar_2.get("ek_start");
                        bus_pref.put("kh_start_" + idx, kh_start);
                        bus_pref.put("ek_start_" + idx, ek_start);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (appUtils.FIRST_LOGIN == 1) {
                    finish();
                    startActivity(new Intent(login.this, TimeTableMain.class));
                    dialog.cancel();
                } else {
                    finish();
                    startActivity(new Intent(login.this, TimeTableMain.class));
                    dialog.cancel();
                }
            } catch (IOException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                dialog.cancel();
                Log.d(TAG, exceptionAsString);
            } catch (NullPointerException e) {
                handler.sendEmptyMessage(0);
                dialog.cancel();
            }
        }

    }
}

