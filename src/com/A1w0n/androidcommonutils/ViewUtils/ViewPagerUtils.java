package com.A1w0n.androidcommonutils.ViewUtils;

import android.support.v4.view.ViewPager;

/**
 *
 *
 * Created by A1w0n on 15/1/5.
 */
public class ViewPagerUtils {

    /**
     * 设置ViewPager缓存起来的页面的数目，防止页面重新创建
     *
     * @param pager
     * @param count
     */
    public void setCachedPage(ViewPager pager, int count) {
        pager.setOffscreenPageLimit(count);
    }
}
