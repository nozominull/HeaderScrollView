package com.nozomi.headerscrollview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class HeaderScrollView extends ScrollView {

	private View realView = null;
	private View dummyView = null;
	private int lastT = 0;

	public HeaderScrollView(Context context) {
		super(context);
	}

	public HeaderScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeaderScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setHeaderAndFooter(final View realView, final View dummyView) {
		this.realView = realView;
		this.dummyView = dummyView;

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

						ViewGroup.LayoutParams vglp = dummyView
								.getLayoutParams();
						vglp.height = realView.getHeight();
						dummyView.setLayoutParams(vglp);

						vglp = realView.getLayoutParams();
						if (vglp instanceof RelativeLayout.LayoutParams) {
							((RelativeLayout.LayoutParams) vglp).topMargin = dummyView
									.getTop();
							realView.setLayoutParams(vglp);
						}

					}
				});
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (lastT != t) {
			if (t<dummyView.getTop()) {
				ViewGroup.LayoutParams vglp = realView.getLayoutParams();
				if (vglp instanceof RelativeLayout.LayoutParams) {
					int topMargin = dummyView.getTop() - t;
					((RelativeLayout.LayoutParams) vglp).topMargin = topMargin;
					realView.setLayoutParams(vglp);
				}
			}else{
				ViewGroup.LayoutParams vglp = (ViewGroup.LayoutParams) realView
						.getLayoutParams();
				if (vglp instanceof RelativeLayout.LayoutParams) {
					int topMargin = ((RelativeLayout.LayoutParams) vglp).topMargin;
					topMargin -= t - lastT;
					if (topMargin < -realView.getHeight()) {
						topMargin = -realView.getHeight();
					} else if (topMargin > 0) {
						topMargin = 0;
					}
					((RelativeLayout.LayoutParams) vglp).topMargin = topMargin;
					realView.setLayoutParams(vglp);
				}
			}
			lastT = t;
		}

	}
}
