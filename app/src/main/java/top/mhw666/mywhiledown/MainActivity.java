package top.mhw666.mywhiledown;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    long runSd=0;
    Button btn;
    TextView tv;
    Down down=new Down();
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
        handler.postDelayed(runing,1);
    }

    RadioButton rb1,rb2,rb3;
    private String getMode() {
        String getUrl="https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip";
        if(rb1.isChecked()) getUrl="http://www.189.cn/down/CtClientL.apk";
        if(rb2.isChecked()) getUrl="http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";
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
                    tv.setText("运 行 时 长 = "+runSd+" 秒\n"+
                            down.getDownInfo());
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runing);
    }
}
