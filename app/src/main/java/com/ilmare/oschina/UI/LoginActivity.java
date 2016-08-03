package com.ilmare.oschina.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.ilmare.oschina.Beans.LoginUserBean;
import com.ilmare.oschina.Beans.Result;
import com.ilmare.oschina.Utils.XmlUtils;

import com.ilmare.oschina.Base.BaseActivity;
import com.ilmare.oschina.Net.OSChinaApi;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.TDevice;
import com.ilmare.oschina.Widget.SimpleTextWatcher;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：7/27/2016 1:36 PM
 * 版本号： 1.0
 * 版权所有(C) 7/27/2016
 * 描述：
 * ===============================
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    @InjectView(R.id.et_username)
    AutoCompleteTextView etUsername;
    @InjectView(R.id.iv_clear_username)
    ImageView ivClearUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.iv_clear_password)
    ImageView ivClearPassword;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    private String mUserName;
    private String mPassword;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.inject(this);
        ivClearUsername.setOnClickListener(this);
        ivClearPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        etUsername.addTextChangedListener(mUserNameWatcher);
        etPassword.addTextChangedListener(mPassswordWatcher);
    }


    private final TextWatcher mUserNameWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            ivClearUsername
                    .setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE
                            : View.VISIBLE);
        }
    };


    private final TextWatcher mPassswordWatcher = new SimpleTextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            ivClearPassword
                    .setVisibility(TextUtils.isEmpty(s) ? View.INVISIBLE
                            : View.VISIBLE);
        }
    };


    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.login;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_clear_username:
                etUsername.getText().clear();
                etUsername.requestFocus();
                break;
            case R.id.iv_clear_password:
                etPassword.getText().clear();
                etPassword.requestFocus();
                break;
            case R.id.btn_login:
                handleLogin();
                break;
            default:
                break;
        }
    }

    private void handleLogin() {

        if (!prepareForLogin()) {
            return;
        }
        // if the data has ready
        mUserName = etUsername.getText().toString();
        mPassword = etPassword.getText().toString();

        showWaitDialog(R.string.progress_login);
        OSChinaApi.login(mUserName, mPassword, mHandler);
    }


    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            LoginUserBean user = XmlUtils.toBean(LoginUserBean.class,
                        content.getBytes());
                Result res = user.getResult();
                if (res.OK()) {
                    // 保存登录信息
                    user.getUser().setAccount(mUserName);
                    user.getUser().setPwd(mPassword);
                    user.getUser().setRememberMe(true);
                    AppContext.getInstance().saveUserInfo(user.getUser());

                    handleLoginSuccess();
                } else {
                    AppContext.getInstance().cleanLoginInfo();
                    AppContext.showToast(res.getErrorMessage());
                }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            AppContext.showToast(R.string.tip_login_error_for_network);
        }



    };

    private void handleLoginSuccess() {
          //TODO 发广播
//        Intent data = new Intent();
//        data.putExtra(BUNDLE_KEY_REQUEST_CODE, requestCode);
//        setResult(RESULT_OK, data);
//        this.sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
        finish();
    }

    private void showWaitDialog(int progress_login) {
        AppContext.showToastShort(R.string.progress_login);
        //TODO 显示一个进度对话框
    }


    private boolean prepareForLogin() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return false;
        }

        String uName = etUsername.getText().toString();
        if (StringUtils.isEmpty(uName)) {
            AppContext.showToastShort(R.string.tip_please_input_username);
            etUsername.requestFocus();
            return false;
        }

        String pwd = etPassword.getText().toString();
        if (StringUtils.isEmpty(pwd)) {
            AppContext.showToastShort(R.string.tip_please_input_password);
            etPassword.requestFocus();
            return false;
        }
        return true;
    }






}
