package com.gizwits.opensource.appkit.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gizwits.opensource.appkit.R;


/**
 * Created by xud on 2017/3/9.
 */

public class KeyboardWithSearchView extends LinearLayout {


    private Context mContext;


    private BaseKeyboardView mBaseKeyboardView;

    private LinearLayout mKeyboadViewContainer;

    //private OnSizeChangedListener mOnSizeChangedListener;


    public KeyboardWithSearchView(Context context) {
        super(context);
        init(context);
    }

    public KeyboardWithSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyboardWithSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KeyboardWithSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

//    protected RecyclerView getRecyclerView() {
//        return mRecyclerView;
//    }

    public BaseKeyboardView getBaseKeyboardView() {
        return mBaseKeyboardView;
    }


//    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
//        mOnSizeChangedListener = onSizeChangedListener;
//    }

    public LinearLayout getKeyboadViewContainer() {
        return mKeyboadViewContainer;
    }


    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_keyboard_view, this, true);
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_view);
        mBaseKeyboardView = (BaseKeyboardView) view.findViewById(R.id.keyboard_view);
        mKeyboadViewContainer = (LinearLayout) view.findViewById(R.id.keyboard_container);
    }

//    public void initRecyclerView(KeyboardSearchBaseAdapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration itemDecoration) {
//        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.addItemDecoration(itemDecoration);
//    }
//
//    public void setSearchResult(List list, boolean hasFixedSize) {
//        if(mRecyclerView.getAdapter() == null) {
//            throw new RuntimeException("this view has not invoked init method");
//        }
//        mRecyclerView.getLayoutManager().scrollToPosition(0);
//        if(list == null || list.size() ==0) {
//            mRecyclerView.setVisibility(GONE);
//        } else {
//            int height = AssetsUtils.diptopx(mContext, Math.min(3,list.size()) * 49) +
//                    Math.min(3,list.size());
//            ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
//            if(params != null) {
//                params.height = height;
//            }else {
//                LinearLayout.LayoutParams newParams =
//                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                height);
//                mRecyclerView.setLayoutParams(newParams);
//            }
//            mRecyclerView.setVisibility(VISIBLE);
//        }
//        mRecyclerView.setHasFixedSize(hasFixedSize);
//
//        KeyboardSearchBaseAdapter adapter = (KeyboardSearchBaseAdapter) mRecyclerView.getAdapter();
//        adapter.setAdapterData(list);
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if(mOnSizeChangedListener != null) {
//            mOnSizeChangedListener.sizeChanged(w,h,oldw,oldh);
//        }
//    }
//
//    protected interface OnSizeChangedListener {
//        void sizeChanged(int w, int h, int oldw, int oldh);
//    }
}
