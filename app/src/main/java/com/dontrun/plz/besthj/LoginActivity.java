package com.dontrun.plz.besthj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dontrun.plz.besthj.Utils.SaveInfo;
import com.dontrun.plz.besthj.Utils.UserInfo;
import com.dontrun.plz.besthj.Utils.UserAction;


public class LoginActivity extends AppCompatActivity {


    private UserLoginTask mLoginTask = null;

    // TODO  pro btn
    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private Button mLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UserInfo userInfo = (UserInfo)SaveInfo.get("user_info_file","user_info",this);

        if(userInfo == null||!userInfo.isLogined()){
            setContentView(R.layout.activity_login);
            // Set up the login form
            mUserNameView = findViewById(R.id.login_username);
            mPasswordView = findViewById(R.id.login_password);
            mLoginButton = findViewById(R.id.login_btn);

            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("UserInfo",userInfo);
            startActivity(intent);
            finish();
        }



    }

    private void attemptLogin() {
        if (mLoginTask != null) {
            return;
        }

        mUserNameView.setError(null);
        mPasswordView.setError(null);

        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!isUserNameValid(userName)) {
            mPasswordView.setError(getString(R.string.error_invalid_email));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mLoginTask = new UserLoginTask(userName, password);
            mLoginTask.execute((Void) null);
        }
    }

    private boolean isUserNameValid(String userName) {
        return userName != null && userName.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 3;
    }


    private class UserLoginTask extends AsyncTask<Void, Void, UserInfo> {

        private final String mUserName;
        private final String mPassword;


        UserLoginTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            mPasswordView.setEnabled(false);
            mUserNameView.setEnabled(false);
            mLoginButton.setEnabled(false);

        }

        @Override
        protected UserInfo doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2);
                // TODO cut //
                return UserAction.login(mUserName,mPassword);

                //
            } catch (InterruptedException e) {
                return null;
            }
        }



        @Override
        protected void onPostExecute(UserInfo userInfo) {
            mLoginTask = null;
            mPasswordView.setEnabled(true);
            mUserNameView.setEnabled(true);
            mLoginButton.setEnabled(true);
            // TODO cut !
            if(userInfo == null){
                Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                mUserNameView.requestFocus();
            }else {
                Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                SaveInfo.save("user_info_file","user_info",userInfo,getApplicationContext() );
                intent.putExtra("UserInfo",userInfo);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
            mPasswordView.setEnabled(true);
            mUserNameView.setEnabled(true);
            mLoginButton.setEnabled(true);
        }
    }
}

