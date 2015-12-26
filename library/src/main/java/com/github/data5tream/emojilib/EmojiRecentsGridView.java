package com.github.data5tream.emojilib;

import android.content.Context;
import android.widget.GridView;

import com.github.data5tream.emojilib.emoji.Emojicon;

/**
 * @author Hieu Rocker
 *
 * @since 0.0.1
 */
public class EmojiRecentsGridView extends EmojiGridView implements EmojiRecents {
    EmojiAdapter mAdapter;

    public EmojiRecentsGridView(Context context, Emojicon[] emojicons, EmojiRecents recents, EmojiPopup emojiPopup) {
        super(context, emojicons, recents, emojiPopup);
        EmojiRecentsManager recents1 = EmojiRecentsManager.getInstance(rootView.getContext());
        mAdapter = new EmojiAdapter(rootView.getContext(),  recents1);
        mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (mEmojiconPopup.onEmojiconClickedListener != null) {
                    mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
                }
            }
        });
        GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
        gridView.setAdapter(mAdapter);
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiRecentsManager recents = EmojiRecentsManager.getInstance(context);
        recents.push(emojicon);

        // notify dataset changed
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

}