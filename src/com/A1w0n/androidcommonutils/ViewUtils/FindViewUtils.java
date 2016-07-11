package com.A1w0n.androidcommonutils.ViewUtils;

import android.app.Activity;
import android.view.View;

/**
 * 每次findViewById都要作类型强转是不是很麻烦?
 * 用这个util类就不用担心类型强转了,会帮你自动转换类型
 *
 * Created by A1w0n on 15/1/5.
 */
public class FindViewUtils {

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T extends View> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }
}
