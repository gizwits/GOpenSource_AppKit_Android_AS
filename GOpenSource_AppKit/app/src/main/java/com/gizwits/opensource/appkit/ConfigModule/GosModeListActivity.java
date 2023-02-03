package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GosModeListActivity extends GosConfigModuleBaseActivity {

    /**
     * The lv Mode
     */
    ListView lvMode;

    /**
     * The data
     */
    List<String> modeList;

    /**
     * The Adapter
     */
    ModeListAdapter modeListAdapter;

    List<Integer> list = new ArrayList<Integer>();

    private Button btnOk;

    private boolean isAirlink = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_modelist);
        // 设置ActionBar
        setToolBar(true, R.string.choose_mode_start);
        final Drawable add = getResources().getDrawable(R.drawable.config_help_button);
        int color = GosDeploy.appConfig_Contrast();
        add.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mToolbar.setOverflowIcon(add);
        initData();
        initView();
        initEvent();
    }

    private void initView() {
        lvMode = (ListView) findViewById(R.id.lvMode);

        lvMode.setAdapter(modeListAdapter);// 初始化

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnOk.setTextColor(GosDeploy.appConfig_Contrast());

    }

    private void initEvent() {

        lvMode.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                modeListAdapter.selectIndex = arg2;
                modeListAdapter.notifyDataSetChanged();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolUtils.noDoubleClick()) {
                    if (modeListAdapter.selectIndex == 100) {
                        Toast.makeText(GosModeListActivity.this, getString(R.string.selece_module_type), toastTime).show();
                    } else {
                        JSONArray array = new JSONArray();
                        array.put(modeListAdapter.selectIndex);
                        spf.edit().putString("modulestyles", array.toString()).commit();
                        if (isAirlink) {
                            // config_airlink-false-start
                            Intent intent = new Intent(GosModeListActivity.this, GosAirlinkReadyActivity.class);
                            startActivity(intent);
                            // config_airlink-false-end
                        } else {
                            //config_softap-false-start
                            Intent intent = new Intent(GosModeListActivity.this, GosDeviceReadyActivity.class);
                            startActivity(intent);
                            //config_softap-false-end
                        }
                    }
                }

            }
        });
    }

    private void initData() {
        isAirlink = getIntent().getBooleanExtra("isAirlink", true);
        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
        modeListAdapter = new ModeListAdapter(this, modeList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_style, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class ModeListAdapter extends BaseAdapter {

        Context context;
        List<String> modeList;
        int selectIndex = 100;

        public ModeListAdapter(Context context, List<String> modeList) {
            super();
            this.context = context;
            this.modeList = modeList;
            if (spf.getString("modulestyles", null) != null) {
                try {
                    JSONArray array = new JSONArray(spf.getString("modulestyles", null));
                    for (int i = 0; i < array.length(); i++) {
                        selectIndex = Integer.parseInt(array.get(i).toString());
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getCount() {
            return modeList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(context, R.layout.item_gos_mode_list, null);
            }

            TextView tvModeText = (TextView) convertView.findViewById(R.id.tvModeText);

            String modeText = modeList.get(position);
            tvModeText.setText(modeText);

            ImageView ivChoosed = (ImageView) convertView.findViewById(R.id.ivChoosed);
            if (selectIndex == position) {
                ivChoosed.setVisibility(View.VISIBLE);
            } else {
                ivChoosed.setVisibility(View.GONE);
            }


            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.module_help:
                if (ToolUtils.noDoubleClick()) {
                    startActivity(new Intent(GosModeListActivity.this, GosChooseModuleHelpActivity.class));
                }
                break;
        }
        return true;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
//        startActivity(intent);
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        this.finish();
//        return true;
//    }

}
