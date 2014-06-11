package com.nozomi.headerscrollview.activity;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.nozomi.headerscrollview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 
 * @author Emil Sj�lander - sjolander.emil@gmail.com
 * 
 */
public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		View realView = findViewById(R.id.real);

		realView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "hej",
						Toast.LENGTH_SHORT).show();
			}
		});

		StickyScrollView scrollView = (StickyScrollView) findViewById(R.id.scroll);
		scrollView.setStickyView(realView, false, false);

	}
}
