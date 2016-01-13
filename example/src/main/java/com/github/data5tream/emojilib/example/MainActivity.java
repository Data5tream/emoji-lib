package com.github.data5tream.emojilib.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;

import com.github.data5tream.emojilib.EmojiEditText;
import com.github.data5tream.emojilib.EmojiGridView;
import com.github.data5tream.emojilib.EmojiPopup;
import com.github.data5tream.emojilib.emoji.Emojicon;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EmojiEditText emojiEditText = (EmojiEditText) findViewById(R.id.emojicon_edit_text);
        final View rootView = findViewById(R.id.root_view);
        final ImageButton emojiButton = (ImageButton) findViewById(R.id.emoji_btn);
        final EmojiPopup popup = new EmojiPopup(rootView, this);

        emojiEditText.requestFocus();

        popup.setSizeForSoftKeyboard();

        popup.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                changeEmojiKeyboardIcon(emojiButton, R.drawable.emoji_2328);
            }
        });

        popup.setOnSoftKeyboardOpenCloseListener(new EmojiPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        popup.setOnEmojiconClickedListener(new EmojiGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (emojiEditText == null || emojicon == null) {
                    return;
                }

                int start = emojiEditText.getSelectionStart();
                int end = emojiEditText.getSelectionEnd();
                if (start < 0) {
                    emojiEditText.append(emojicon.getEmoji());
                } else {
                    emojiEditText.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });

        popup.setOnEmojiconBackspaceClickedListener(new EmojiPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                emojiEditText.dispatchKeyEvent(event);
            }
        });

        emojiButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!popup.isShowing()){

                    if(popup.isKeyBoardOpen()){
                        popup.showAtBottom();
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_emoji_symbols_light_activated);
                    }

                    else{
                        emojiEditText.setFocusableInTouchMode(true);
                        emojiEditText.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(emojiEditText, InputMethodManager.SHOW_IMPLICIT);
                        changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_emoji_people_light_activated);
                    }
                }

                else{
                    popup.dismiss();
                }
            }
        });
    }

    private void changeEmojiKeyboardIcon(ImageButton iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }

}
