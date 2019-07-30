package top.mhw666.mywhiledown;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Down {

    public static int GB=0,MB=0,KB=0,B=0,count=0;
    public boolean isDown=true,isDowning=false;
    static long lastBy=0,nowBy=0,allBy=1;
    static double cent=0;
    HttpURLConnection connection=null;
    public static String getDownInfo() {
        String r=String.format("第%d次下载\t%.2f%%\t%dkb/s",
                count,cent,(nowBy-lastBy)/1024);
//        r+="%\n"+lastBy+"/"+nowBy;
        r+="\n下载流量=\t\t"+GB+"GB "+MB+"MB "+KB+"KB";
        lastBy=nowBy;
        return r;
    }
//    String getUrl="http://www.189.cn/down/CtClientL.apk";
    public void down(String url) {
//        HttpURLConnection connection=null;
        isDowning=true;
        lastBy=0;
        nowBy=0;
        allBy=1;
        try {
            URL uRL=new URL(url);
            connection=(HttpURLConnection) uRL.openConnection();
            connection.setRequestMethod("GET");
            System.out.println("状态码="+connection.getResponseCode());
            String AllLenStr=connection.getHeaderField("Content-Length");
            try {
                allBy= Long.parseLong(AllLenStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            count+=1;
            System.out.println("AllLen="+allBy+"|"+
                    allBy/1024/1024+"MB"+allBy/1024+"KB");
            InputStream in=connection.getInputStream();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len = 0;
//            long timeStampSec = System.currentTimeMillis();
            while (isDown && (len = in.read(buffer)) != -1) {
//                baos.write(buffer, 0, len);
                nowBy+=len;
                cent=100d*nowBy/allBy;
                int k=len/1024;
                KB+=k;
                if(len%1024!=0){
                    B+=(len-1024*k);
                }
                if(B>=1024){
                    B-=1024;
                    KB+=1; }
                if(KB>=1024){
                    KB-=1024;
                    MB+=1;
                }
                if(MB>=1024){
                    MB-=1024;
                    GB+=1;
                }
//                DownCent= (downl=downl+len)*100d/AllLen;
//                if(System.currentTimeMillis()-timeStampSec>333) {
//                    timeStampSec = System.currentTimeMillis();
//                    _handler.sendEmptyMessage(1);
//                }
            }
//            baos.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            if(connection!=null){
//                connection.disconnect();
//            }
        }isDowning=false;
    }//void end


}
