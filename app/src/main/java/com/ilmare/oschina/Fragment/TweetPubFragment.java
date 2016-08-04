package com.ilmare.oschina.Fragment;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.os.Bundle;
import com.ilmare.oschina.Base.BaseFragment;
import com.ilmare.oschina.Beans.Tweet;
import com.ilmare.oschina.Inter.OnEmojiClickListener;
import com.ilmare.oschina.R;
import com.ilmare.oschina.Service.ServerTaskUtils;
import com.ilmare.oschina.UI.AppContext;
import com.ilmare.oschina.Utils.StringUtils;
import com.ilmare.oschina.Utils.TDevice;
import com.ilmare.oschina.Utils.UIHelper;
import com.ilmare.oschina.Widget.SimpleTextWatcher;
import com.ilmare.oschina.emoji.EmojiKeyboardFragment;
import com.ilmare.oschina.emoji.Emojicon;
import com.ilmare.oschina.emoji.InputHelper;

import java.io.File;

import butterknife.InjectView;

/**
 * ===============================
 * 作者: ilmare:
 * 创建时间：8/3/2016 9:35 PM
 * 版本号： 1.0
 * 版权所有(C) 8/3/2016
 * 描述：
 * ===============================
 */

public class TweetPubFragment extends BaseFragment implements
        OnEmojiClickListener {

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    public static final int ACTION_TYPE_RECORD = 2; // 录音
    public static final int ACTION_TYPE_TOPIC = 3; // 录音

    public static final String FROM_IMAGEPAGE_KEY = "from_image_page";

    public static final String ACTION_TYPE = "action_type";

    private static final int MAX_TEXT_LENGTH = 160;

    private static final String TEXT_ATME = "@请输入用户名 ";

    private static final String TEXT_SOFTWARE = "#请输入软件名#";

    EmojiKeyboardFragment keyboardFragment = new EmojiKeyboardFragment();
    @InjectView(R.id.et_content)
    EditText etContent;
    @InjectView(R.id.iv_img)
    ImageView ivImg;
    @InjectView(R.id.iv_clear_img)
    ImageView ivClearImg;
    @InjectView(R.id.rl_img)
    RelativeLayout rlImg;
    @InjectView(R.id.tv_clear)
    TextView tvClear;
    @InjectView(R.id.bottom)
    RelativeLayout bottom;
    @InjectView(R.id.ib_picture)
    ImageButton ibPicture;
    @InjectView(R.id.ib_mention)
    ImageButton ibMention;
    @InjectView(R.id.ib_trend_software)
    ImageButton ibTrendSoftware;
    @InjectView(R.id.ib_emoji_keyboard)
    ImageButton ibEmojiKeyboard;
    @InjectView(R.id.emoji_keyboard_fragment)
    FrameLayout emojiKeyboardFragment;

    private String fromSharedTextContent = "";

    private MenuItem mSendMenu;

    private File imgFile=null;

    private boolean mIsKeyboardVisible;
    //初始化view
    @Override
    protected void init() {
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getActivity().getWindow().setSoftInputMode(mode);


        setHasOptionsMenu(true);

        ibEmojiKeyboard.setOnClickListener(this);
        ibPicture.setOnClickListener(this);
        ibMention.setOnClickListener(this);
        ibTrendSoftware.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        ivClearImg.setOnClickListener(this);
        tvClear.setText(String.valueOf(MAX_TEXT_LENGTH));

        // 获取保存的tweet草稿
        etContent.setText(AppContext.getTweetDraft());
        etContent.setSelection(etContent.getText().toString().length());

        //获取保存的tweet草稿
        String content = fromSharedTextContent;
        if (StringUtils.isEmpty(fromSharedTextContent)) {
            content = AppContext.getTweetDraft();
        }
        etContent.setText(content);

        etContent.setSelection(etContent.getText().toString().length());
        etContent.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
                updateMenuState();
            }
        });

        //输入
        getFragmentManager().beginTransaction()
                .replace(R.id.emoji_keyboard_fragment, keyboardFragment)
                .commit();
        keyboardFragment.setOnEmojiClickListener(new OnEmojiClickListener() {
            @Override
            public void onEmojiClick(Emojicon v) {
                InputHelper.input2OSC(etContent, v);
            }

            @Override
            public void onDeleteButtonClick(View v) {
                InputHelper.backspace(etContent);
            }
        });

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pub_topic_menu, menu);
        mSendMenu = menu.findItem(R.id.public_menu_send);
        updateMenuState();
    }

    private void updateMenuState() {
        if (mSendMenu == null) {
            return;
        }
        if (etContent.getText().length() == 0) {
            mSendMenu.setEnabled(false);
            mSendMenu.setIcon(R.drawable.actionbar_unsend_icon);
        } else {
            mSendMenu.setEnabled(true);
            mSendMenu.setIcon(R.drawable.actionbar_send_icon);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                handleSubmit();
                break;
            default:
                break;
        }
        return true;
    }


    private void handleSubmit() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }

        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            etContent.requestFocus();
            AppContext.showToastShort(R.string.tip_content_empty);
            return;
        }
        if (content.length() > MAX_TEXT_LENGTH) {
            AppContext.showToastShort(R.string.tip_content_too_long);
            return;
        }

        Tweet tweet = new Tweet();
        tweet.setAuthorid(AppContext.getInstance().getLoginUid());
        tweet.setBody(content);
        if (imgFile != null && imgFile.exists()) {
            tweet.setImageFilePath(imgFile.getAbsolutePath());
        }

        ServerTaskUtils.pubTweet(getActivity(), tweet);
        if (mIsKeyboardVisible) {
            TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
        }
        AppContext.showToastShort("发送成功");
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_emoji_keyboard:
                AppContext.showToastShort("表情包");
                break;
            case R.id.ib_mention:
                AppContext.showToastShort("@某人");
                break;
            case R.id.ib_picture:
                AppContext.showToastShort("选择图片");
                break;
            case R.id.ib_trend_software:
                AppContext.showToastShort("发送链接");
                break;
        }
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int action_type = bundle.getInt(ACTION_TYPE, -1);
//            goToSelectPicture(action_type);
//            final String imgUrl = bundle.getString(FROM_IMAGEPAGE_KEY);
//            handleImageUrl(imgUrl);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tweet_pub;
    }

    @Override
    protected void onLoadSuccess(String content) {

    }

    public void setContentText(String sharedText) {

    }

    public void setContentImage(String absoluteImagePath) {

    }

    @Override
    public void onDeleteButtonClick(View v) {

    }

    @Override
    public void onEmojiClick(Emojicon v) {

    }


}
