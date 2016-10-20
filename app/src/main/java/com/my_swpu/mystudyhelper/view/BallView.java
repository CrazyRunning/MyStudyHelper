package com.my_swpu.mystudyhelper.view;



import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class BallView extends View implements AnimatorUpdateListener{
	private float BALL_SIZE=30f;
	private final AnimatorSet set=new AnimatorSet();
	private BallShape ball;
	private float mViewHeight;
	private float mViewWidth;

	public BallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public BallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		final ViewTreeObserver view=this.getViewTreeObserver();
		view.addOnPreDrawListener(new OnPreDrawListener() {	
			@Override
			public boolean onPreDraw() {
				BallView.this.getViewTreeObserver().removeOnPreDrawListener(this);
				mViewHeight = BallView.this.getLayoutParams().height;
				mViewWidth = BallView.this.getLayoutParams().width;
                play();
				return true; 
			}
		});

	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	
	}
	private void play() {
		Log.i("ych", "height"+mViewHeight+"width"+mViewWidth);
		// TODO Auto-generated method stub
		ball=createBall(mViewWidth/2,BALL_SIZE/2);
		float startY=ball.getY();
		float endY=(float) (mViewHeight-BALL_SIZE);
	////fall
		ValueAnimator fallAnim=ObjectAnimator.ofFloat(ball,"y",startY,endY);
		fallAnim.setDuration(600);
		fallAnim.setInterpolator(new AccelerateInterpolator());
		fallAnim.addUpdateListener(this);
		
		////x����
		ValueAnimator xLeftAnim=ObjectAnimator.ofFloat(ball,"x",ball.getX(),ball.getX()-BALL_SIZE/2);
		xLeftAnim.setDuration(200);
		xLeftAnim.addUpdateListener(this);
		xLeftAnim.setRepeatCount(1);
		xLeftAnim.setRepeatMode(ValueAnimator.REVERSE);
	////ѹ��
		ValueAnimator wAnim=ObjectAnimator.ofFloat(ball,"width",ball.getWidth(),ball.getWidth()*2);
		wAnim.setDuration(200);
		wAnim.setInterpolator(new DecelerateInterpolator());
		wAnim.addUpdateListener(this);
		wAnim.setRepeatCount(1);
		wAnim.setRepeatMode(ValueAnimator.REVERSE);
		ValueAnimator yAnim=ObjectAnimator.ofFloat(ball,"height",ball.getHeight(),ball.getHeight()/2);
		yAnim.setDuration(200);
		yAnim.setInterpolator(new AccelerateInterpolator());
		yAnim.addUpdateListener(this);
		yAnim.setRepeatCount(1);
		yAnim.setRepeatMode(ValueAnimator.REVERSE);
		ValueAnimator ysAnim=ObjectAnimator.ofFloat(ball,"y",endY,endY+BALL_SIZE/2);
		ysAnim.setDuration(200);
		ysAnim.setInterpolator(new AccelerateInterpolator());
		ysAnim.addUpdateListener(this);
		ysAnim.setRepeatCount(1);
		ysAnim.setRepeatMode(ValueAnimator.REVERSE);
		///����
		ValueAnimator tanAnim=ObjectAnimator.ofFloat(ball,"y",endY,startY);
		tanAnim.setDuration(1000);
		tanAnim.setInterpolator(new DecelerateInterpolator());
		tanAnim.addUpdateListener(this);
		
		
		set.play(fallAnim).before(xLeftAnim);
		set.play(xLeftAnim).with(wAnim);
		set.play(wAnim).with(yAnim);
		set.play(wAnim).with(ysAnim);
		set.play(tanAnim).after(xLeftAnim);
		set.start();
		set.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				set.start();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		// TODO Auto-generated method stub
		this.invalidate();
	}
	private BallShape createBall(float x,float y) {
		// TODO Auto-generated method stub
	OvalShape circle=new OvalShape();
	circle.resize(BALL_SIZE, BALL_SIZE);
	ShapeDrawable drawable=new ShapeDrawable(circle);
	BallShape ball=new BallShape(drawable);
	ball.setX(x-BALL_SIZE/2);
	ball.setY(y-BALL_SIZE/2);
	Paint paint=drawable.getPaint();
	paint.setColor(0xff38B7ED);
	return ball;
	}
	public void animCancel(){
		set.cancel();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		if(ball!=null){
		canvas.save();

		canvas.translate(ball.getX(),ball.getY());
		ball.getShape().draw(canvas);
		canvas.restore();
		}
	}
}
/**�����*/
class BallShape {
	/**������*/
	private float x, y;
	/**�����״*/
	private ShapeDrawable shape;
	/**�����ɫ*/
	private int color;
	/**����Ļ���*/
	private Paint paint;

	public BallShape(ShapeDrawable shape) {
		// TODO Auto-generated constructor stub
		this.shape = shape;
	}

	public float getWidth() {
		return shape.getShape().getWidth();
	}

	public void setWidth(float width) {
		Shape s = shape.getShape();
		s.resize(width, s.getHeight());
	}

	public ShapeDrawable getShape() {
		return shape;
	}

	public float getHeight() {
		return shape.getShape().getHeight();
	}

	public void setHeight(float height) {
		Shape s = shape.getShape();
		s.resize(s.getWidth(), height);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

}