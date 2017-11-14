package com.ctrl.ctrlshopmall.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.R;

/**
 * Created by ctrlc on 2017/11/7.
 */

public class CtrlToolbar extends Toolbar {
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mButton;
    private LayoutInflater mInflater;
    private View mView;
    public CtrlToolbar(Context context) {
        this(context,null);
    }

    public CtrlToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CtrlToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentInsetsRelative(10,10);

        if(attrs != null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                   R.styleable.CtrlToolbar, defStyleAttr, 0);
            final Drawable rightIcon = a.getDrawable(R.styleable.CtrlToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }
            boolean isShowSearchView = a.getBoolean(R.styleable.CtrlToolbar_isShowSearchView,false);
            if(isShowSearchView){
                showSearchView();
                hideTitleView();
            }
            CharSequence rightButtonTexxt = a.getText(R.styleable.CtrlToolbar_rightButtonText);
            if(rightButtonTexxt!=null){
                setTitle(rightButtonTexxt);
            }
        }

        initView();

    }
    private void initView(){
        if(mView==null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            mButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(mView,lp);
        }
    }

    public void setRightButtonOnclick(OnClickListener listener){
        mButton.setOnClickListener(listener);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
         initView();
        if(mTextTitle != null){
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon){
        if(mButton !=null){
            mButton.setBackground(icon);
            mButton.setVisibility(View.VISIBLE);
        }
    }

    public  void showSearchView(){

        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);

    }


    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }
    public void setRightButtonText(CharSequence text){
        mButton.setText(text);
        mButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }
}
