package com.gizwits.opensource.appkit.UserModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;


public class GosSendEmailPasswordActivity extends GosBaseActivity {

    private TextView tvEmail;
    private Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_send_email_password);
        // 设置ActionBar
        setToolBar(true, R.string.forget_pass);
        initView();
        initEvent();
    }

    private static final String TAG = "GosSendEmailPasswordAct";

    private void initView() {
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btnReturn = (Button) findViewById(R.id.btnReturn);
    }

    private void initEvent() {
        String email = spf.getString("Email", "xxx@xxx.xx");
        String s = getResources().getString(R.string.send_email_pass);
        //字符串截取
        String bb = s.substring(10, 20);
        //字符串替换
        String cc = s.replace(bb, email);
        SpannableString spannableString = new SpannableString(cc);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tomato)), 10, 10 + email.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvEmail.setText(spannableString);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GosSendEmailPasswordActivity.this, GosUserLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }


}
