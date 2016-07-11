package com.A1w0n.androidcommonutils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.A1w0n.androidcommonutils.GlobalApplicationUtils.GlobalApplication;

/**
 * 系统联系人相关的util
 *
 * Created by A1w0n on 15/5/19.
 */
public class SystemContactUtil {

    /**
     * 根据电话号码判断，是否是系统联系人
     */
    public static boolean checkIsSystemContact(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }

        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] mPhoneNumberProjection = {
                ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};

        Context context = GlobalApplication.getInstance();
        Cursor cur = context.getContentResolver().query(
                lookupUri, mPhoneNumberProjection, null, null, null);

        try {
            if (cur != null && cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null) cur.close();
        }

        return false;
    }
}
