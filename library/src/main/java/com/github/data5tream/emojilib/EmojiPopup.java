package com.github.data5tream.emojilib;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.github.data5tream.emojilib.emoji.Emojicon;
import com.github.data5tream.emojilib.emoji.Nature;
import com.github.data5tream.emojilib.emoji.Objects;
import com.github.data5tream.emojilib.emoji.People;
import com.github.data5tream.emojilib.emoji.Places;
import com.github.data5tream.emojilib.emoji.Symbols;

/**
 * @author Ankush Sachdeva
 * @author data5tream
 *
 * @since 0.0.1
 */
public class EmojiPopup extends PopupWindow implements ViewPager.OnPageChangeListener, EmojiRecents {

    private int mEmojiTabLastSelectedIndex = -1;
    private ImageButton[] mEmojiTabs;
    private PagerAdapter mEmojisAdapter;
    private EmojiRecentsManager mRecentsManager;
    private int keyBoardHeight = 0;
    private int iconColor;
    private int highlightColor;

    private Boolean pendingOpen = false;
    private Boolean isOpened = false;

    EmojiGridView.OnEmojiconClickedListener onEmojiconClickedListener;
    OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener;
    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListener;

    View rootView;
    Context context;

    private ViewPager emojisPager;

    /**
     * Constructor
     *
     * @param rootView The top most layout in your view hierarchy. The difference of this view and the screen height will be used to calculate the keyboard height.
     * @param context The context of current activity.
     */
    public EmojiPopup(View rootView, Context context) {
        super(context);

        this.context = context;
        this.rootView = rootView;

        highlightColor = ContextCompat.getColor(context, R.color.emojicons_selected_tab);
        View customView = createCustomView();
        setContentView(customView);
        setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setSize((int) context.getResources().getDimension(R.dimen.keyboard_height), LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * Constructor
     *
     * @param rootView The top most layout in your view hierarchy. The difference of this view and the screen height will be used to calculate the keyboard height.
     * @param context The context of current activity.
     * @param color The accent color to be used
     */
    public EmojiPopup(View rootView, Context context, int color) {
        super(context);

        this.context = context;
        this.rootView = rootView;

        highlightColor = color;
        View customView = createCustomView();
        setContentView(customView);
        setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setSize((int) context.getResources().getDimension(R.dimen.keyboard_height), LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * Set the listener for the event of keyboard opening or closing.
     *
     * @param listener the listener to be used
     */
    public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener) {
        this.onSoftKeyboardOpenCloseListener = listener;
    }

    /**
     * Set the listener triggered when any of the emojis is clicked.
     *
     * @param listener the listener to be used
     */
    public void setOnEmojiconClickedListener(EmojiGridView.OnEmojiconClickedListener listener) {
        this.onEmojiconClickedListener = listener;
    }

    /**
     * Set the listener for the event when the backspace on the emoji-keyboard is clicked.
     *
     * @param listener the listener to be used
     */
    public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener listener) {
        this.onEmojiconBackspaceClickedListener = listener;
    }

    /**
     * Function to show the emoji-keyboard.
     *
     * NOTE: Since, the soft keyboard sizes are variable on different android devices, the
     * library needs you to open the soft keyboard at least once before calling this function.
     */
    public void showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * Use this function if the soft keyboard hasn't been opened when the emoji-keyboard is triggered.
     */
    public void showAtBottomPending() {
        if(isKeyBoardOpen())
            showAtBottom();
        else
            pendingOpen = true;
    }

    /**
     *
     * @return Returns true if the soft keyboard is open, false otherwise.
     */
    public Boolean isKeyBoardOpen() {
        return isOpened;
    }

    /**
     * Dismiss the popup
     */
    @Override
    public void dismiss() {
        super.dismiss();
        EmojiRecentsManager.getInstance(context).saveRecents();
    }

    /**
     * Call this function to resize the emoji popup according to your soft keyboard size
     */
    public void setSizeForSoftKeyboard() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int screenHeight = getUsableScreenHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    heightDifference -= context.getResources().getDimensionPixelSize(resourceId);
                }
                if (heightDifference > 100) {

                    keyBoardHeight = heightDifference;
                    setSize(LayoutParams.MATCH_PARENT, keyBoardHeight);

                    if (!isOpened &&onSoftKeyboardOpenCloseListener!=null) {
                        onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);
                    }
                    isOpened = true;
                    if (pendingOpen) {
                        showAtBottom();
                        pendingOpen = false;
                    }
                }
                else{
                    isOpened = false;
                    if (onSoftKeyboardOpenCloseListener!=null) {
                        onSoftKeyboardOpenCloseListener.onKeyboardClose();
                    }
                }
            }
        });
    }

    private int getUsableScreenHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            return metrics.heightPixels;
        } else {
            return rootView.getRootView().getHeight();
        }
    }

    /**
     * Manually set the popup window size.
     *
     * @param width Width of the popup
     * @param height Height of the popup
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    private View createCustomView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.emojicons, null, false);

        iconColor = ContextCompat.getColor(context, R.color.emojilib_emojikeyboard_icons);

        emojisPager = (ViewPager) view.findViewById(R.id.emojis_pager);
        emojisPager.addOnPageChangeListener(this);
        EmojiRecents recents = this;
        mEmojisAdapter = new EmojisPagerAdapter(Arrays.asList(
                new EmojiRecentsGridView(context, null, null, this),
                new EmojiGridView(context, People.DATA, recents, this),
                new EmojiGridView(context, Nature.DATA, recents, this),
                new EmojiGridView(context, Objects.DATA, recents, this),
                new EmojiGridView(context, Places.DATA, recents, this),
                new EmojiGridView(context, Symbols.DATA, recents, this)
            ));
        emojisPager.setAdapter(mEmojisAdapter);

        mEmojiTabs = new ImageButton[6];
        mEmojiTabs[0] = (ImageButton) view.findViewById(R.id.emojis_tab_0_recents);
        mEmojiTabs[1] = (ImageButton) view.findViewById(R.id.emojis_tab_1_people);
        mEmojiTabs[2] = (ImageButton) view.findViewById(R.id.emojis_tab_2_nature);
        mEmojiTabs[3] = (ImageButton) view.findViewById(R.id.emojis_tab_3_objects);
        mEmojiTabs[4] = (ImageButton) view.findViewById(R.id.emojis_tab_4_cars);
        mEmojiTabs[5] = (ImageButton) view.findViewById(R.id.emojis_tab_5_punctuation);

        for (int i = 0; i < mEmojiTabs.length; i++) {
            final int position = i;
            mEmojiTabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojisPager.setCurrentItem(position);
                }
            });
            mEmojiTabs[i].setColorFilter(iconColor);
        }

        ImageButton backspaceBtn = (ImageButton) view.findViewById(R.id.emojis_backspace);

        backspaceBtn.setOnTouchListener(new RepeatListener(1000, 50, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEmojiconBackspaceClickedListener != null) {
                    onEmojiconBackspaceClickedListener.onEmojiconBackspaceClicked(v);
                }
            }
        }));
        backspaceBtn.setColorFilter(iconColor);

        mRecentsManager = EmojiRecentsManager.getInstance(view.getContext());
        int page = mRecentsManager.getRecentPage();
        if (page == 0 && mRecentsManager.size() == 0) {
            page = 1;
        }

        if (page == 0) {
            onPageSelected(page);
        }
        else {
            emojisPager.setCurrentItem(page, false);
        }
        return view;
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        EmojiRecentsGridView fragment = ((EmojisPagerAdapter)emojisPager.getAdapter()).getRecentFragment();
        fragment.addRecentEmoji(context, emojicon);
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }

        if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
            mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
            mEmojiTabs[mEmojiTabLastSelectedIndex].setColorFilter(iconColor);
        }
        mEmojiTabs[i].setSelected(true);
        mEmojiTabs[i].setColorFilter(highlightColor);
        mEmojiTabLastSelectedIndex = i;
        mRecentsManager.setRecentPage(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private static class EmojisPagerAdapter extends PagerAdapter {
        private List<EmojiGridView> views;

        public EmojiRecentsGridView getRecentFragment(){
            for (EmojiGridView it : views) {
                if(it instanceof EmojiRecentsGridView) {
                    return (EmojiRecentsGridView) it;
                }
            }
            return null;
        }

        public EmojisPagerAdapter(List<EmojiGridView> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position).rootView;
            (container).addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            (container).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View view, Object key) {
            return key == view;
        }
    }

    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
     * click is fired immediately, next before initialInterval, and subsequent before
     * normalInterval.
     */
    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {

            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click
         *                        events
         * @param clickListener   The OnClickListener, that will be called
         *                        periodically
         */
        public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
            if (clickListener == null) {
                throw new IllegalArgumentException("null runnable");
            }
            if (initialInterval < 0 || normalInterval < 0) {
                throw new IllegalArgumentException("negative interval");
            }

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackspaceClicked(View v);
    }

    public interface OnSoftKeyboardOpenCloseListener{
        void onKeyboardOpen(int keyBoardHeight);
        void onKeyboardClose();
    }
}