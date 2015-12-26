package com.github.data5tream.emojilib;

import android.content.Context;

import com.github.data5tream.emojilib.emoji.Emojicon;

/**
 * @author Daniele Ricci
 *
 * @since 0.0.1
 */
public interface EmojiRecents {
    public void addRecentEmoji(Context context, Emojicon emojicon);
}
