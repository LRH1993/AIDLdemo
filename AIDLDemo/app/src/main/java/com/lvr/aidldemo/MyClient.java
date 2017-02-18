package com.lvr.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyClient extends AppCompatActivity implements View.OnClickListener{
    private Button mBinderButton;
    private Button mAddButton;
    private IBookManager mIBookManager;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //通过服务端onBind方法返回的binder对象得到IBookManager的实例，得到实例就可以调用它的方法了
            mIBookManager = IBookManager.Stub.asInterface(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManager = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinderButton = (Button) findViewById(R.id.bindService);
        mAddButton = (Button) findViewById(R.id.addBook);
        mBinderButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bindService:{
                Intent intentService = new Intent();
                intentService.setAction("com.lvr.aidldemo.MyService");
                intentService.setPackage(getPackageName());
                intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyClient.this.bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
                Toast.makeText(getApplicationContext(),"绑定了服务",Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.addBook:{
                if(mIBookManager!=null){
                    try {
                        mIBookManager.addBook(new Book(18,"新添加的书"));
                        Toast.makeText(getApplicationContext(),mIBookManager.getBookList().size()+"",Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIBookManager!=null){
            unbindService(mServiceConnection);
        }
    }
}
