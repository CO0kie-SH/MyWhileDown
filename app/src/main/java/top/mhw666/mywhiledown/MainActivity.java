package top.mhw666.mywhiledown;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import android.provider.Settings;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    long runSd=0;
    Button btn;
    TextView tv;
    CheckBox cbF;
    Down down=new Down();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.btn);
        tv=findViewById(R.id.tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setEnabled(down.isDown);
                rb2.setEnabled(down.isDown);
                rb3.setEnabled(down.isDown);
                down.isDown=!down.isDown;
                btn.setText(down.isDown?"停止下载":"开始下载");
            }
        });
        rb1=findViewById(R.id.rB1);rb1.setEnabled(false);
        rb2=findViewById(R.id.rB2);rb2.setEnabled(false);
        rb3=findViewById(R.id.rB3);rb3.setEnabled(false);
        cbF=findViewById(R.id.checkBox);
        cbF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(">>"+Build.VERSION.SDK_INT+">>"+isChecked);
                if (Build.VERSION.SDK_INT >= 23) {
                    if(!Settings.canDrawOverlays(getApplicationContext())) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        //方法2 跳转手选方式
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, 1);
                        cbF.setChecked(false);
                        return;
                    }
                }
                if(isChecked){
                    if (MyFloat.isStarted)return;
                    else if(intent==null) {
                        intent=new Intent(MainActivity.this, MyFloat.class);
                        startService(intent);
                        bindService(intent, MainActivity.this, Context.BIND_AUTO_CREATE);
                    }
                }
            }
        });
        handler.postDelayed(runing,1);
    }

    RadioButton rb1,rb2,rb3;
    private String getMode() {
        String getUrl="";
        if(rb1.isChecked()) getUrl="http://www.189.cn/down/CtClientL.apk";
        if(rb2.isChecked()) getUrl="http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";
        if(rb3.isChecked()) getUrl="https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip";
        return  getUrl;
    }

    Runnable runing=new Runnable() {
        @Override
        public void run() {
            runSd+=1;
            handler.sendEmptyMessage(1);

            if(down.isDown && !down.isDowning){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        down.down(getMode());
                    }
                }).start();
            }
            handler.postDelayed(runing,1000);
        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String s=down.getDownInfo();
                    tv.setText("运 行 时 长 = "+runSd+" 秒\n"+s);
                    if (myBinder != null)
                        myBinder.setData(s);//③
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runing);
    }
    //一旦绑定成功就会执行该函数
    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        myBinder = (MyFloat.Binder) iBinder;//②
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    private MyFloat.Binder myBinder = null;//①
}
