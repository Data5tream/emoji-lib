package com.github.data5tream.emojilib;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

/**
 * @author Hieu Rocker
 * @author data5tream
 *
 * @since 0.0.1
 */
public class EmojiMultiAutoCompleteTextView extends MultiAutoCompleteTextView {
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;

    /**
     * Constructor
     *
     * @param context the calling context
     */
    public EmojiMultiAutoCompleteTextView(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    /**
     * Constructor
     *
     * @param context calling context
     * @param attrs See @init
     */
    public EmojiMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Constructor
     *
     * @param context calling context
     * @param attrs See @init
     */
    public EmojiMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Set attributes for the emoji representation
     *
     * @param attrs AttributeSet containing:
     *              emojiconSize = Display size of the emojis in pixel
     *              emojiconAlignment = Line alignment of the emojis (bottom or baseline)
     *              emojiconUseSystemDefault = Boolean indicating if system default emojis should be used
     */
    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }

    /**
     * Set the emoji size in pixels
     *
     * @param pixels size of the emojis in px
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;

        updateText();
    }

    /**
     * Update text to show new emojis
     */
    private void updateText() {
        EmojiHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mUseSystemDefault);
    }

    /**
     * Set if system default emojis should be used
     *
     * @param useSystemDefault if true system emojis are used (default false)
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
}
