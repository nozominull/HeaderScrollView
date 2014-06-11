package com.emilsjolander.components.StickyScrollViewItems;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;

/**
 * 
 * @author Emil Sj�lander - sjolander.emil@gmail.com
 * 
 */
public class StickyScrollView extends ScrollView {

	private View currentlyStickingView;
	private int stickyViewLeftOffset;
	private boolean redirectTouchesToStickyView;
	private boolean clippingToPadding;
	private boolean clipToPaddingHasBeenSet;

	private View stickyView = null;
	/**
	 * Flag for views that should stick and have non-constant drawing. e.g.
	 * Buttons, ProgressBars etc
	 */
	private boolean nonConstant = false;
	/**
	 * Flag for views that have aren't fully opaque
	 */
	private boolean hastransparency = false;

	private boolean hasNotDoneActionDown = true;

	private final Runnable invalidateRunnable = new Runnable() {

		@Override
		public void run() {
			if (currentlyStickingView != null) {
				int l = getLeftForViewRelativeOnlyChild(currentlyStickingView);
				int t = getBottomForViewRelativeOnlyChild(currentlyStickingView);
				int r = getRightForViewRelativeOnlyChild(currentlyStickingView);
				int b = (int) (getScrollY() + currentlyStickingView.getHeight());
				invalidate(l, t, r, b);
			}
			postDelayed(this, 16);
		}
	};

	public StickyScrollView(Context context) {
		this(context, null);
	}

	public StickyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.scrollViewStyle);
	}

	public StickyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setStickyView(View stickyView, boolean nonConstant,
			boolean hastransparency) {
		this.stickyView = stickyView;
		this.nonConstant = nonConstant;
		this.hastransparency = hastransparency;
	}

	private int getLeftForViewRelativeOnlyChild(View v) {
		int left = v.getLeft();
		while (v.getParent() != getChildAt(0)) {
			v = (View) v.getParent();
			left += v.getLeft();
		}
		return left;
	}

	private int getTopForViewRelativeOnlyChild(View v) {
		int top = v.getTop();
		while (v.getParent() != getChildAt(0)) {
			v = (View) v.getParent();
			top += v.getTop();
		}
		return top;
	}

	private int getRightForViewRelativeOnlyChild(View v) {
		int right = v.getRight();
		while (v.getParent() != getChildAt(0)) {
			v = (View) v.getParent();
			right += v.getRight();
		}
		return right;
	}

	private int getBottomForViewRelativeOnlyChild(View v) {
		int bottom = v.getBottom();
		while (v.getParent() != getChildAt(0)) {
			v = (View) v.getParent();
			bottom += v.getBottom();
		}
		return bottom;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!clipToPaddingHasBeenSet) {
			clippingToPadding = true;
		}
		notifyHierarchyChanged();
	}

	@Override
	public void setClipToPadding(boolean clipToPadding) {
		super.setClipToPadding(clipToPadding);
		clippingToPadding = clipToPadding;
		clipToPaddingHasBeenSet = true;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (currentlyStickingView != null) {
			canvas.save();
			canvas.translate(getPaddingLeft() + stickyViewLeftOffset,
					getScrollY() + (clippingToPadding ? getPaddingTop() : 0));

			canvas.clipRect(0, 0, getWidth(), currentlyStickingView.getHeight());
			if (hastransparency) {
				showView(currentlyStickingView);
				currentlyStickingView.draw(canvas);
				hideView(currentlyStickingView);
			} else {
				currentlyStickingView.draw(canvas);
			}
			canvas.restore();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			redirectTouchesToStickyView = true;
		}

		if (redirectTouchesToStickyView) {
			redirectTouchesToStickyView = currentlyStickingView != null;
			if (redirectTouchesToStickyView) {
				redirectTouchesToStickyView = ev.getY() <= currentlyStickingView
						.getHeight()
						&& ev.getX() >= getLeftForViewRelativeOnlyChild(currentlyStickingView)
						&& ev.getX() <= getRightForViewRelativeOnlyChild(currentlyStickingView);
			}
		} else if (currentlyStickingView == null) {
			redirectTouchesToStickyView = false;
		}
		if (redirectTouchesToStickyView) {
			ev.offsetLocation(
					0,
					-1
							* (getScrollY() - getTopForViewRelativeOnlyChild(currentlyStickingView)));
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (redirectTouchesToStickyView) {
			ev.offsetLocation(
					0,
					(getScrollY() - getTopForViewRelativeOnlyChild(currentlyStickingView)));
		}

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			hasNotDoneActionDown = false;
		}

		if (hasNotDoneActionDown) {
			MotionEvent down = MotionEvent.obtain(ev);
			down.setAction(MotionEvent.ACTION_DOWN);
			super.onTouchEvent(down);
			hasNotDoneActionDown = false;
			down.recycle();
		}

		if (ev.getAction() == MotionEvent.ACTION_UP
				|| ev.getAction() == MotionEvent.ACTION_CANCEL) {
			hasNotDoneActionDown = true;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		doTheStickyThing();
	}

	private void doTheStickyThing() {
		View viewThatShouldStick = null;

		int viewTop = getTopForViewRelativeOnlyChild(stickyView) - getScrollY()
				+ (clippingToPadding ? 0 : getPaddingTop());
		if (viewTop <= 0) {
			viewThatShouldStick = stickyView;
		}

		if (viewThatShouldStick != null) {
			if (viewThatShouldStick != currentlyStickingView) {
				// only compute the left offset when we start sticking.
				stickyViewLeftOffset = getLeftForViewRelativeOnlyChild(viewThatShouldStick);
				startStickingView(viewThatShouldStick);
			}
		} else if (currentlyStickingView != null) {
			stopStickingCurrentlyStickingView();
		}
	}

	private void startStickingView(View viewThatShouldStick) {
		currentlyStickingView = viewThatShouldStick;
		if (hastransparency) {
			hideView(currentlyStickingView);
		}
		if (nonConstant) {
			post(invalidateRunnable);
		}
	}

	private void stopStickingCurrentlyStickingView() {
		if (hastransparency) {
			showView(currentlyStickingView);
		}
		currentlyStickingView = null;
		removeCallbacks(invalidateRunnable);
	}

	private void notifyHierarchyChanged() {
		if (currentlyStickingView != null) {
			stopStickingCurrentlyStickingView();
		}
		doTheStickyThing();
		invalidate();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void hideView(View v) {
		if (Build.VERSION.SDK_INT >= 11) {
			v.setAlpha(0);
		} else {
			AlphaAnimation anim = new AlphaAnimation(1, 0);
			anim.setDuration(0);
			anim.setFillAfter(true);
			v.startAnimation(anim);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void showView(View v) {
		if (Build.VERSION.SDK_INT >= 11) {
			v.setAlpha(1);
		} else {
			AlphaAnimation anim = new AlphaAnimation(0, 1);
			anim.setDuration(0);
			anim.setFillAfter(true);
			v.startAnimation(anim);
		}
	}

}
