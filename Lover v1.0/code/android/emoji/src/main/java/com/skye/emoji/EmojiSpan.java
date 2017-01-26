package com.skye.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

final class EmojiSpan extends DynamicDrawableSpan {
	private final Context context;
	private final int resourceId;
	private final int size;

	private Drawable drawable;

	EmojiSpan(final Context context, final int resourceId, final int size) {
		this.context = context;
		this.resourceId = resourceId;
		this.size = size;
	}

	@Override
	public Drawable getDrawable() {
		if (drawable == null) {
			drawable = context.getResources().getDrawable(resourceId);
			drawable.setBounds(0, 0, size, size);
		}
		return drawable;
	}

	public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
		Drawable d = getDrawable();
		Rect rect = d.getBounds();
		if (fm != null) {
			FontMetricsInt fmPaint = paint.getFontMetricsInt();
			int fontHeight = fmPaint.bottom - fmPaint.top;
			int drHeight = rect.bottom - rect.top;
			int top = drHeight / 2 - fontHeight / 4;
			int bottom = drHeight / 2 + fontHeight / 4;
			fm.ascent = -bottom;
			fm.top = -bottom;
			fm.bottom = top;
			fm.descent = top;
		}
		return rect.right;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
			Paint paint) {
		// Drawable b = getDrawable();
		// canvas.save();
		// int transY = 0;
		// transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
		// canvas.translate(x, transY);
		// b.draw(canvas);
		// canvas.restore();

		Drawable b = getDrawable();
		FontMetricsInt fm = paint.getFontMetricsInt();
		int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;
		canvas.save();
		canvas.translate(x, transY);
		b.draw(canvas);
		canvas.restore();
	}

	// �ο�http://www.cnblogs.com/withwind318/p/5541267.html
}
