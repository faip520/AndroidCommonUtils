package com.A1w0n.androidcommonutils.FragmentUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Aiwan on 2014/5/24 0024.
 * 所有Fragment的基类，configuration change的时候不重建。
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }
}
