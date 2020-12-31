package com.hjd.applib.custom.clipimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author king
 */
public class ClipView extends View {

	/**
	 * 框框宽度
	 */
	public static final int BORDERDISTANCE = 100;

	private Context context;

	public ClipView(Context context) {
		this(context, null);
		this.context = context;
	}

	public ClipView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;
	}

	// 画圆形区域
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();
		int borderlength = width - BORDERDISTANCE * 2;
		int x = (int) Math.sqrt((width / 2) * (width / 2) + (height / 2)
				* (height / 2));
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.BLACK);
		paint.setAlpha(150);
		paint.setStrokeWidth(x);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(width / 2, height / 2, (borderlength + x) / 2 - 2,
				paint);

		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(5);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(width / 2, height / 2, borderlength / 2, mPaint);

	}

}
