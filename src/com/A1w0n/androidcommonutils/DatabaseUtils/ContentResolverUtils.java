package com.A1w0n.androidcommonutils.DatabaseUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * ContentResolver相关的一些教程例子和用法封装
 *
 * Created by A1w0n on 15/1/9.
 */
public class ContentResolverUtils {

    /**
     * 单个查询教程例子
     *
     * @param context
     */
    public static void queryFromContentResolver(Context context) {
        ContentResolver contentresolver = context.getContentResolver();

        String contactId = "22";

        // 根据id查询某个系统联系人

        // query函数第一个参数是ContentResolver的名字
        // 第二个参数是String数组，是要查询的表的哪些列
        // 第三个是查询语句，VOLUME_NAME = 'target' 注意这里要用单引号
        // 第四个参数是查询语句的一部分
        Cursor cursor = contentresolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts._ID + "='" + contactId + "'",
                null,
                null);

        // 单个查询也可以这样写：第三个参数哪里写 VOLUME_NAME = ?
        // 第四个参数弄成 new String[]{"22"}
        Cursor cursor2 = contentresolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts._ID + "=?",
                new String[] {contactId},
                null);
    }

    /**
     * 查询一个序列教程例子
     *
     * @param context
     */
    public static void queryAListFromContentResolver(Context context) {
        ContentResolver contentresolver = context.getContentResolver();

        String contactId = "22";

        ArrayList<String> contactIDs = new ArrayList<String>();

        // query函数第一个参数是ContentResolver的名字
        // 第二个参数是String数组，是要查询的表的哪些列
        // 第三个是查询语句，VOLUME_NAME = 'target' 注意这里要用单引号
        // 第四个参数是查询语句的一部分
        Cursor cursor = contentresolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts._ID + "=?",
                new String[] {contactId, contactId, contactId},
                null);
    }

    /**
     * Order by 教程例子
     *
     * @param context
     */
    public static void queryOrderedResultFromContentResolver(Context context) {
        ContentResolver contentresolver = context.getContentResolver();

        String contactId = "22";

        ArrayList<String> contactIDs = new ArrayList<String>();

        // query函数第一个参数是ContentResolver的名字
        // 第二个参数是String数组，是要查询的表的哪些列
        // 第三个是查询语句，VOLUME_NAME = 'target' 注意这里要用单引号
        // 第四个参数是查询语句的一部分
        // 第五个参数order by 降序用法如下
        Cursor cursor = contentresolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts._ID + "=?",
                new String[] {contactId, contactId, contactId},
                ContactsContract.Contacts._ID + " DESC");

        // query函数第一个参数是ContentResolver的名字
        // 第二个参数是String数组，是要查询的表的哪些列
        // 第三个是查询语句，VOLUME_NAME = 'target' 注意这里要用单引号
        // 第四个参数是查询语句的一部分
        // 第五个参数order by 升序用法如下
        Cursor cursor2 = contentresolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts._ID + "=?",
                new String[] {contactId, contactId, contactId},
                ContactsContract.Contacts._ID + " ASC");
    }

}
