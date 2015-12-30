package com.github.data5tream.emojilib;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.github.data5tream.emojilib.emoji.Emojicon;
import com.github.data5tream.emojilib.emoji.People;

/**
 * @author Hieu Rocker
 * @author Ankush Sachdeva
 * @author data5tream
 *
 * @since 0.0.1
 */
public class EmojiGridView {
    public View rootView;

    EmojiPopup mEmojiconPopup;
    EmojiRecents mRecents;
    Emojicon[] mData;

    public EmojiGridView(Context context, Emojicon[] emojicons, EmojiRecents recents, EmojiPopup emojiconPopup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mEmojiconPopup = emojiconPopup;
        rootView = inflater.inflate(R.layout.emojicon_grid, null);
        setRecents(recents);
        GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);

        if (emojicons== null) {
            mData = People.DATA;
        } else {
            mData = Arrays.asList(emojicons).toArray(new Emojicon[emojicons.length]);
        }
        EmojiAdapter mAdapter = new EmojiAdapter(rootView.getContext(), mData);
        mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (mEmojiconPopup.onEmojiconClickedListener != null) {
                    mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
                }
                if (mRecents != null) {
                    mRecents.addRecentEmoji(rootView.getContext(), emojicon);
                }
            }
        });
        gridView.setAdapter(mAdapter);
    }

    private void setRecents(EmojiRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }

}
