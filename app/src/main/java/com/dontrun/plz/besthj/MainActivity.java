package com.dontrun.plz.besthj;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.dontrun.plz.besthj.Cal.Generate;
import com.dontrun.plz.besthj.Utils.*;
import com.geetest.gt3unbindsdk.Bind.GT3GeetestBindListener;
import com.geetest.gt3unbindsdk.Bind.GT3GeetestUtilsBind;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    //http://gxapp.iydsj.com/api/v20/security/geepreprocess?osType=0&uid=15884s&uuid=19aa7ec6-8f2d-4df2-8518-b6cae10f5a47&
    // Geetest Start
    private  String captchaURL = "http://gxapp.iydsj.com/api/v20/security/geepreprocess?osType=0&uid=";
    // 设置二次验证的URL，需替换成自己的服务器URL
    private static final String validateURL = "http://gxapp.iydsj.com/api/v20/security/geevalidate";
    private GT3GeetestUtilsBind gt3GeetestUtils;
    // Geetest End

    private UserLogoutTask mLogoutTask=null;
    private RunningTask mRunningTask = null;
    private ImageView mPersonHead;
    private Button mLogoutButton;
    private Button mRunButton;
    private TextView mLogsView;
    private UserInfo mUserInfo;
    private TextView mName;
    private TextView mSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPersonHead = findViewById(R.id.person_head);
        mLogoutButton = findViewById(R.id.logout_btn);
        mRunButton = findViewById(R.id.start_run);
        mName =findViewById(R.id.person_name);
        mSchool=findViewById(R.id.person_school);
        mLogsView = findViewById(R.id.logs_view);
        mLogsView.setMovementMethod(new ScrollingMovementMethod());
        mUserInfo = (UserInfo)(getIntent().getSerializableExtra("UserInfo"));
        mUserInfo.setUuid(UUID.randomUUID().toString());


        if (!mUserInfo.getIcon().isEmpty()) {
            Glide.with(this).load(mUserInfo.getIcon()).into(mPersonHead);
        }
        mName.setText(mUserInfo.getName());
        mSchool.setText(mUserInfo.getCampusName());
        addMessage("登陆成功");
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLogoutTask==null){
                    mLogoutTask = new UserLogoutTask();
                    mLogoutTask.execute((Void) null);
                }

            }
        });

//        mRunButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addMessage(mUserInfo.getName());
//                addMessage(mUserInfo.getToken());
//                addMessage(mUserInfo.getCampusName());
//                mRunningTask = new RunningTask();
//            }
//        });
        captchaURL = captchaURL + mUserInfo.getUid() + "&uuid=" + mUserInfo.getUuid();
        addMessage("初始化中");
        init();
        addMessage("初始化完成");
    }

    void addMessage(String msg){
        mLogsView.append("\n"+msg);
        int offset=mLogsView.getLineCount()*mLogsView.getLineHeight();
        if(offset>mLogsView.getHeight()){
            mLogsView.scrollTo(0,offset-mLogsView.getHeight());
        }
    }

    private class RunningTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mRunningTask = null;
        }

        @Override
        protected void onPreExecute() {
            mLogoutButton.setEnabled(false);
            mRunButton.setEnabled(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Boolean doInBackground(Void... voids) {
            Map<String, Location> testPoints = UserAction.getTestPoints(mUserInfo);
            PLTuple<Set<String>, JSONArray> a= Generate.getUsefulRount( mUserInfo,testPoints);
            JSONArray fivePointJson = Generate.getFivePointJSON(mUserInfo,testPoints,a.passPoints);
            return UserAction.uploadRecord(mUserInfo,a.allLocJson,fivePointJson);
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            mRunningTask = null;
            mLogoutButton.setEnabled(true);
            mRunButton.setEnabled(true);
            if(succeed){
                addMessage("跑步完成");
            }else {
                mLogoutButton.setEnabled(true);
                addMessage("跑步失败");
            }
        }

    }


    private class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mLogoutButton.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return UserAction.logout(mUserInfo);
        }

        @Override
        protected void onPostExecute(Boolean succeed) {

            if(succeed){
                addMessage("注销成功");
                mUserInfo.setLogined(false);
                SaveInfo.save("user_info_file","user_info",mUserInfo,getApplicationContext() );
                mRunButton.setEnabled(false);
            }else {
                mLogoutTask = null;
                mLogoutButton.setEnabled(true);
                addMessage("注销失败,建议等待一个小时后重新登陆");
            }
        }
        @Override
        protected void onCancelled() {
            mLogoutTask = null;
            mLogoutButton.setEnabled(true);
        }
    }


    private void init() {
        /**
         * 初始化
         * 务必放在onCreate方法里面执行
         */
        gt3GeetestUtils = new GT3GeetestUtilsBind(MainActivity.this);

        /**
         * 点击调起验证
         */
        mRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage("验证中");
                gt3GeetestUtils.getGeetest(MainActivity.this, captchaURL, validateURL, null, new GT3GeetestBindListener() {
                    /**
                     * num 1 点击验证码的关闭按钮来关闭验证码
                     * num 2 点击屏幕关闭验证码
                     * num 3 点击返回键关闭验证码
                     */
                    @Override
                    public void gt3CloseDialog(int  num) {
                        Log.d("gtttttt","gt3CloseDialog");
                    }


                    /**
                     * 验证码加载准备完成
                     * 此时弹出验证码
                     */
                    @Override
                    public void gt3DialogReady() {
                        Log.d("gtttttt","gt3DialogReady");

                    }


                    /**
                     * 拿到第一个url（API1）返回的数据
                     */
                    @Override
                    public void gt3FirstResult(JSONObject jsonObject) {
                    }


                    /**
                     * 往API1请求中添加参数
                     * 添加数据为Map集合
                     * 添加的数据以get形式提交
                     */
                    @Override
                    public Map<String, String> gt3CaptchaApi1() {
                        Map<String, String> map = new HashMap<String, String>();
                        return map;
                    }

                    /**
                     * 设置是否自定义第二次验证ture为是 默认为false(不自定义)
                     * 如果为false这边的的完成走gt3GetDialogResult(String result)
                     * 如果为true这边的的完成走gt3GetDialogResult(boolean a, String result)
                     * result为二次验证所需要的数据
                     */
                    @Override
                    public boolean gt3SetIsCustom() {
                        return false;
                    }

                    /**
                     * 拿到二次验证需要的数据
                     */
                    @Override
                    public void gt3GetDialogResult(String result) {
                    }


                    /**
                     * 自定义二次验证，当gtSetIsCustom为ture时执行这里面的代码
                     */
                    @Override
                    public void gt3GetDialogResult(boolean status, String result) {

                        if (status) {

                             // 利用异步进行解析这result进行二次验证，结果成功后调用gt3GeetestUtils.gt3TestFinish()方法调用成功后的动画，然后在gt3DialogSuccess执行成功之后的结果
                            JSONObject res_json = null;
                            try {
                                res_json = new JSONObject(result);
                                Map<String, String> validateParams = new HashMap<>();
                                validateParams.put("geetest_challenge", res_json.getString("geetest_challenge"));
                                validateParams.put("geetest_validate", res_json.getString("geetest_validate"));
                                validateParams.put("geetest_seccode", res_json.getString("geetest_seccode"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                             //  二次验证成功调用 gt3GeetestUtils.gt3TestFinish();
                             //  二次验证失败调用 gt3GeetestUtils.gt3TestClose();

                        }
                    }


                    /**
                     * 需要做验证统计的可以打印此处的JSON数据
                     * JSON数据包含了极验每一步的运行状态和结果
                     */
                    @Override
                    public void gt3GeetestStatisticsJson(JSONObject jsonObject) {
                    }

                    /**
                     * 往二次验证里面put数据
                     * put类型是map类型
                     * 注意map的键名不能是以下三个：geetest_challenge，geetest_validate，geetest_seccode
                     */
                    @Override
                    public Map<String, String> gt3SecondResult() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid",String.valueOf(mUserInfo.getUid()));
                        map.put("uuid",mUserInfo.getUuid());
                        map.put("osType","0");
                        return map;

                        }
                    /**
                     * 二次验证完成的回调
                     * result为验证后的数据
                     * 根据二次验证返回的数据判断此次验证是否成功
                     * 二次验证成功调用 gt3GeetestUtils.gt3TestFinish();
                     * 二次验证失败调用 gt3GeetestUtils.gt3TestClose();
                     */
                    @Override
                    public void gt3DialogSuccessResult(String result) {
                        addMessage("验证结束");
                        if(!TextUtils.isEmpty(result)) {
                            addMessage(result.toString());
                        }
                        addMessage("创建跑步任务");
                        mRunningTask = new RunningTask();
                        addMessage("创建完成");
                        addMessage("开始跑步进程");
                        mRunningTask.execute((Void) null);
                        addMessage("运行结束");

                        if(!TextUtils.isEmpty(result)) {
                            try {
                                JSONObject jobj = new JSONObject(result);
                                String sta = jobj.getString("status");

                                if ("success".equals(sta)) {
                                    addMessage("验证成功");
                                    gt3GeetestUtils.gt3TestFinish();
                                    addMessage("创建跑步任务");
                                    mRunningTask = new RunningTask();
                                    addMessage("创建完成");
                                    addMessage("开始跑步进程");
                                    mRunningTask.execute((Void) null);
                                    addMessage("运行结束");

                                } else {
                                    addMessage("验证失败");
                                    gt3GeetestUtils.gt3TestClose();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else
                        {
                            gt3GeetestUtils.gt3TestClose();
                            addMessage("结果为空，验证失败");

                        }
                    }

                    /**
                     * 验证过程错误
                     * 返回的错误码为判断错误类型的依据
                     */

                    @Override
                    public void gt3DialogOnError(String error) {
                        Log.i("dsd","gt3DialogOnError"+error);
                        gt3GeetestUtils.cancelAllTask();
                    }
                });
                //设置是否可以点击屏幕边缘关闭验证码
                gt3GeetestUtils.setDialogTouch(true);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gt3GeetestUtils.cancelUtils();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gt3GeetestUtils.changeDialogLayout();
    }

}
