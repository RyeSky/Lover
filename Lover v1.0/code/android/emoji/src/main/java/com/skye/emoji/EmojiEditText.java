package com.skye.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EmojiEditText extends EditText {
	private int emojiSize;

	public EmojiEditText(final Context context) {
		super(context);
		init(null);
	}

	public EmojiEditText(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public EmojiEditText(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(final AttributeSet attrs) {
		if (attrs == null) {
			emojiSize = (int) getTextSize();
		} else {
			final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.emoji);

			try {
				emojiSize = (int) a.getDimension(R.styleable.emoji_emojiSize, getTextSize());
			} finally {
				a.recycle();
			}
		}

		setText(getText());
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore,
			final int lengthAfter) {
		try {
			EmojiHandler.addEmojis(getContext(), getText(), emojiSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setEmojiSize(final int pixels) {
		emojiSize = pixels;
	}

	public void backspace() {
		final KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
		dispatchKeyEvent(event);
	}
}
