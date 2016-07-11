package com.tencent.midas.oversea.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 如果item少于4个，则会有几个item，就显示几个item的高度，如果item数目大于等于4个
 * 则只会显示4个item的高度
 *
 * Created by A1w0n on 15/6/18.
 */
public class APHeightWrapContentListView extends ListView {

    private int mHeightMaxItem = 4;

    public APHeightWrapContentListView(Context context) {
        super(context);
    }

    public APHeightWrapContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public APHeightWrapContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setHeightMaxItem(int max) {
        if (max <= 0) {
            return;
        }

        mHeightMaxItem = 4;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        setHeightWrapContent();
    }

    private void setHeightWrapContent() {
        ListAdapter listAdapter = getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        int size = listAdapter.getCount();
        int itemCount;

        if (size <= 0) {
            return;
        }

        if (size >= mHeightMaxItem) {
            itemCount = mHeightMaxItem;
        } else {
            itemCount = size;
        }

        for (int i = 0; i < itemCount; i++) {
            View listItem = listAdapter.getView(i, null, this);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = this.getLayoutParams();

        params.height = totalHeight
                + (this.getDividerHeight() * (itemCount - 1));
        this.setLayoutParams(params);
    }
}
