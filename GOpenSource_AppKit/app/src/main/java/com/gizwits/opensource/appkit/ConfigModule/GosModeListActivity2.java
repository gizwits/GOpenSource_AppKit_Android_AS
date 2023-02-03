package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class GosModeListActivity2 extends GosConfigModuleBaseActivity {

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
                boolean isRemove = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == arg2) {
                        list.remove(i);
                        isRemove = true;
                    }
                }
                if (!isRemove) {
                    list.add(arg2);
                }
                modeListAdapter.notifyDataSetChanged();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolUtils.noDoubleClick()) {
                    if (list.size() == 0) {
                        Toast.makeText(GosModeListActivity2.this, getString(R.string.selece_module_type), toastTime).show();
                    } else {
                        JSONArray array = new JSONArray();
                        for (int i = 0; i < list.size(); i++) {
                            array.put(list.get(i));
                        }
                        Log.e("TAG", "onClick: " + array.toString());
                        spf.edit().putString("modulestyles", array.toString()).commit();
                        if (isAirlink) {
                            // config_airlink-false-start
                            Intent intent = new Intent(GosModeListActivity2.this, GosAirlinkReadyActivity.class);
                            startActivity(intent);
                            // config_airlink-false-end
                        } else {
                            //config_softap-false-start
                            Intent intent = new Intent(GosModeListActivity2.this, GosDeviceReadyActivity.class);
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
        String modules = spf.getString("modulestyles", null);
        if (modules != null) {
            try {
                JSONArray array = new JSONArray(modules);
                for (int i = 0; i < array.length(); i++) {
                    int type = (Integer) array.get(i);
                    list.add(type);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
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
        MenuItem menuItem = menu.findItem(R.id.module_help);
        menuItem.setIcon(ToolUtils.editIcon(getResources(), R.drawable.config_help_button));
        return super.onCreateOptionsMenu(menu);
    }

    class ModeListAdapter extends BaseAdapter {

        Context context;
        List<String> modeList;

        public ModeListAdapter(Context context, List<String> modeList) {
            super();
            this.context = context;
            this.modeList = modeList;
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
            if (list.contains(position)) {
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
                    startActivity(new Intent(GosModeListActivity2.this, GosChooseModuleHelpActivity.class));
                }
                break;
        }
        return true;
    }

}
