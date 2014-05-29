package com.nozomi.headerscrollview.activity;

import com.nozomi.headerscrollview.R;
import com.nozomi.headerscrollview.view.HeaderScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		View realView = findViewById(R.id.real);
		View dummyView = findViewById(R.id.dummy);

		HeaderScrollView scroll = (HeaderScrollView) findViewById(R.id.scroll);
		scroll.setHeaderAndFooter(realView, dummyView);

	}

}
