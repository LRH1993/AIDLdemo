package com.lvr.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvr on 2017/2/18.
 */

public class MyService extends Service {
    private List<Book> mBookList = new ArrayList<>();
    //实现了AIDL的抽象函数
    private IBookManager.Stub mbinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            //什么也不做
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            //添加书本
            if(!mBookList.contains(book)){
                mBookList.add(book);
            }
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        for (int i = 1; i < 6; i++) {
            Book book = new Book();
            book.bookName = "book#" + i;
            book.bookId = i;
            mBookList.add(book);
        }
    }
}
