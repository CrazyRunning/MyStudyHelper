package com.my_swpu.mystudyhelper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.my_swpu.mystudyhelper.R;


/**
 * 
 * @Desc: 自带清除功能的输入框
 */
public class EditTextWithFrame extends EditText implements TextWatcher, OnFocusChangeListener
{
	private Paint framePaint;
	private int mFrameColor = 0xFFD7D6DC;
	private Drawable del_btn;// 点击按钮默认
	private boolean isClearIconInShown = false;
	/**
	 * 是否获取焦点，默认没有焦点
	 */
	private boolean hasFocus = false;
	/**
	 * 手指抬起时的X坐标
	 */
	private int xUp = 0;

	public EditTextWithFrame(Context context)
	{
		super(context);
		initFramePaint(context);
	}

	public EditTextWithFrame(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initFramePaint(context);
	}

	public EditTextWithFrame(Context context, AttributeSet attrs, int defStryle)
	{
		super(context, attrs, defStryle);
		initFramePaint(context);
	}

	private void initFramePaint(Context context)
	{
		this.framePaint = new Paint();
		this.framePaint.setColor(mFrameColor);
		this.framePaint.setStyle(Style.FILL);
		// setStatus(status);
		del_btn = context.getResources().getDrawable(R.drawable.search_clean);
		// del_btn_down =
		// context.getResources().getDrawable(R.drawable.search_clean);
		addListeners();
		setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}

	// 处理删除事件
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (del_btn != null && event.getAction() == MotionEvent.ACTION_UP)
		{
			// 获取点击时手指抬起的X坐标
			xUp = (int) event.getX();
			Log.e("xUp", xUp + "");
			/*
			 * Rect rect = new Rect(); getGlobalVisibleRect(rect); rect.left =
			 * rect.right - 50;
			 */
			// 当点击的坐标到当前输入框右侧的距离小于等于getCompoundPaddingRight()的距离时，则认为是点击了删除图标
			if ((getWidth() - xUp) <= getCompoundPaddingRight())
			{
				if (!TextUtils.isEmpty(getText().toString()))
				{
					setText("");
				}
			}
		}
		// else if (del_btn != null && event.getAction() ==
		// MotionEvent.ACTION_DOWN && getText().length() != 0)
		// {
		// // setCompoundDrawablesWithIntrinsicBounds(null, null, del_btn,
		// // null);
		// // setCompoundDrawablesWithIntrinsicBounds(null, null, del_btn_down,
		// // null);
		// }
		else if (getText().length() != 0)
		{
			setCompoundDrawablesWithIntrinsicBounds(null, null, del_btn, null);
		}
		return super.onTouchEvent(event);
	}

	// public void setStatus(int status)
	// {
	// this.status = status;
	//
	// // if (status == STATUS_ERROR)
	// // {
	// // setColor(Color.parseColor("#f57272"));
	// // } else if (status == STATUS_FOCUSED)
	// // {
	// // setColor(Color.parseColor("#5e99f3"));
	// // } else
	// // {
	// // setColor(Color.parseColor("#bfbfbf"));
	// // }
	// postInvalidate();
	// }

	// public void setLeftDrawable(int focusedDrawableId, int
	// unfocusedDrawableId, int errorDrawableId)
	// {
	// setStatus(status);
	// }

	private void addListeners()
	{
		try
		{
			setOnFocusChangeListener(this);
			addTextChangedListener(this);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
	{
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		this.hasFocus = focused;
		// if (focused)
		// {
		// setStatus(STATUS_FOCUSED);
		// } else
		// {
		// setStatus(STATUS_UNFOCUSED);
		// setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		// }
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	};

	public void setColor(int color)
	{
		this.setTextColor(color);
		invalidate();
	}

	@Override
	public void afterTextChanged(Editable arg0)
	{
		// TODO Auto-generated method stub
		postInvalidate();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(arg0))
		{
			// 如果为空，则不显示删除图标
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			isClearIconInShown = false;
		} else if (!isClearIconInShown)
		{
			// 如果非空，则要显示删除图标
			setCompoundDrawablesWithIntrinsicBounds(null, null, del_btn, null);
			isClearIconInShown = true;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int after)
	{
		if (hasFocus)
		{
			if (TextUtils.isEmpty(s))
			{
				// 如果为空，则不显示删除图标
				setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				isClearIconInShown = false;
			} else if (!isClearIconInShown)
			{
				// 如果非空，则要显示删除图标
				setCompoundDrawablesWithIntrinsicBounds(null, null, del_btn, null);
				isClearIconInShown = true;
			}
		}
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1)
	{
		// TODO Auto-generated method stub
		try
		{
			this.hasFocus = arg1;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}