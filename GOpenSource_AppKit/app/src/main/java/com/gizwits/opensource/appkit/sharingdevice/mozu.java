package com.gizwits.opensource.appkit.sharingdevice;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class mozu extends GosBaseActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_gos_mozu);
		setActionBar(true, true, R.string.mozhu);
		
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		
		
		 WebView web = (WebView) findViewById(R.id.web);
		 
		 web.loadUrl(GosConstant.mozu);
		 
		 
		// web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
		 web.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
		 web.getSettings().setSupportZoom(true);//是否可以缩放，默认true
		 web.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
		 web.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
		 //web.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
		 //web.getSettings().setAppCacheEnabled(true);//是否使用缓存
		 //web.getSettings().setDomStorageEnabled(true);//DOM Storage
		 // displayWebview.getSettings().setUserAgentString("User-Agent:Android");//设置用户代理，一般不用
		 
		 web.setWebViewClient(new WebViewClient());
	
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
