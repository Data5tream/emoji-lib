package com.github.data5tream.emojilib;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.data5tream.emojilib.emoji.Emojicon;

import java.util.List;

/**
 * @author Hieu Rocker
 * @author Ankush Sachdeva
 * @author data5tream
 *
 * @since 0.0.1
 */
class EmojiAdapter extends ArrayAdapter<Emojicon> {
    EmojiGridView.OnEmojiconClickedListener emojiClickListener;
    public EmojiAdapter(Context context, List<Emojicon> data) {
        super(context, R.layout.emojicon_item, data);
    }

    public EmojiAdapter(Context context, Emojicon[] data) {
        super(context, R.layout.emojicon_item, data);
    }

    /**
     * Set listener to be triggered when an emoji is clicked
     *
     * @param listener the listener to be used
     */
    public void setEmojiClickListener(EmojiGridView.OnEmojiconClickedListener listener){
        this.emojiClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.emojicon_item, null);
            ViewHolder holder = new ViewHolder();
            holder.icon = (TextView) v.findViewById(R.id.emojicon_icon);
            v.setTag(holder);
        }
        Emojicon emoji = getItem(position);
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.icon.setText(emoji.getEmoji());
        holder.icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiClickListener.onEmojiconClicked(getItem(position));
            }
        });
        return v;
    }

    static class ViewHolder {
        TextView icon;
    }
}