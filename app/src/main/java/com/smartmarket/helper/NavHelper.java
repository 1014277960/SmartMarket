package com.smartmarket.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wulinpeng
 * @datetime: 18/2/3 下午2:48
 * @description: 解决对Fragment的调度和重用问题，达到最优的fragment切换
 */
public class NavHelper {

    private FragmentManager mFragmentManager;

    private int mContainerId;

    private Context mContext;

    /**
     * 每一个Fragment对应一个tag,tag可以是对应menu的itemId
     */
    private Map<Integer, Tab> mTabs = new HashMap<>();

    private int mCurrentTag;

    private NavListener mListener;

    public interface NavListener {
        public void onFirstAdd(int tag);
        public boolean onReselect(int tag);
        public void onChange(int oldTag, int newTag);
    }

    public NavHelper(FragmentManager mFragmentManager, int mContainerId, Context mContext, NavListener mListener) {
        this.mFragmentManager = mFragmentManager;
        this.mContainerId = mContainerId;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public NavHelper(FragmentManager mFragmentManager, int mContainerId, Context mContext) {
        this(mFragmentManager, mContainerId, mContext, null);
    }

    public void setNavListener(NavListener mListener) {
        this.mListener = mListener;
    }

    public NavHelper addTab(int tag, Class<?> clazz) {
        Tab tab = mTabs.get(tag);
        if (tab == null) {
            tab = new Tab(clazz);
        } else if (tab.clazz != clazz) {
            tab.clazz = clazz;
            tab.fragment = null;
        }
        mTabs.put(tag, tab);
        return this;
    }

    public Map<Integer, Tab> getTabs() {
        return mTabs;
    }

    /**
     * Tab被点击,切换fragment
     * @param tag
     */
    public boolean onTabSelect(int tag) {
        Tab newTab = mTabs.get(tag);
        if (newTab != null) {
            if (tag == mCurrentTag) {
                return onReselect(mCurrentTag);
            } else {
                return doTabChange(mCurrentTag, tag);
            }
        }
        return false;
    }

    private boolean doTabChange(int oldTag, int newTag) {
        Tab oldTab = mTabs.get(oldTag);
        Tab newTab = mTabs.get(newTag);
        if (newTab.fragment == null) {
            newTab.fragment = Fragment.instantiate(mContext, newTab.clazz.getName());
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (oldTab == null) {
            // 第一次添加
            transaction.add(mContainerId, newTab.fragment);
            if (mListener != null) {
                mListener.onFirstAdd(newTag);
            }
        } else {
            transaction.replace(mContainerId, newTab.fragment);
            if (mListener != null) {
                mListener.onChange(oldTag, newTag);
            }
        }
        transaction.commit();
        mCurrentTag = newTag;
        return true;
    }

    /**
     * 重复点击
     * @param tag
     */
    private boolean onReselect(int tag) {
        if (mListener != null) {
            return mListener.onReselect(tag);
        }
        return false;
    }

    public static class Tab {
        public Class<?> clazz;

        public Tab(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Fragment fragment;
    }
}
