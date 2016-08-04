package com.ilmare.oschina.Inter;

import android.view.View;

import com.ilmare.oschina.emoji.Emojicon;

public interface OnEmojiClickListener {
    void onDeleteButtonClick(View v);

    void onEmojiClick(Emojicon v);
}