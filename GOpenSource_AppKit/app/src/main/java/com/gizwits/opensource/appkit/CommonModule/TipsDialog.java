package com.gizwits.opensource.appkit.CommonModule;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;


public class TipsDialog extends Dialog implements
		View.OnClickListener {

	private Button btnSure;
	private TextView tvTips;

	public TipsDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.shape_button2);
		setContentView(R.layout.dialog_tips);
		initView();
		setCancelable(false);
		
	}

	public TipsDialog(Context context, String txt) {
		this(context);

		tvTips.setText(txt);
	}

	public TipsDialog(Context context, int res) {
		this(context);

		tvTips.setText(res);
	}
	
	
	

	private void initView() {
		btnSure = (Button) findViewById(R.id.btnSure);
		tvTips = (TextView) findViewById(R.id.tvTips);

		btnSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSure:
			cancel();
			break;
		}
	}
}
