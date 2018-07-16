/*=====================================================================
□ INFORMATION
  ○ Data : 23.05.2018
  ○ Mail : eun1310434@naver.com
  ○ Blog : https://blog.naver.com/eun1310434
  ○ Reference
     - Do it android app Programming
     - Hello JAVA Programming
     - http://itmining.tistory.com/5

□ STUDY
  ○ BroadcastReceiver
     - Android apps can send or receive broadcast messages from the Android system and other Android apps, similar to the publish-subscribe design pattern.
       These broadcasts are sent when an event of interest occurs.
     - For example, the Android system sends broadcasts when various system events occur,
       such as when the system boots up or the device starts charging.
       Apps can also send custom broadcasts,

□ FUNCTION
  ○
=====================================================================*/

package com.eun1310434.receiver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //sms message receive
    public static final int REQUEST_CODE_SMS = 201;
    private TextView tv_Log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_Log = (TextView) findViewById(R.id.tv_Log);

        // 위험권한 : 일전에 manifast를 통해서 권한을 승인 받은 것에 추가로, 앱 실행시 권한요청 보여줌(Sdk23 이후 버젼).
        // 대표 위험권한 : 위치, 카메라, 마이크, 연락처, 전화, 문자, 일정, 센서
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {//SNS 수신 권한 이미 승인 한 경우
            Toast.makeText(this, "SMS PERMISSION_GRANTED.", Toast.LENGTH_SHORT).show();
            tv_Log.setText(tv_Log.getText()+"SMS PERMISSION_GRANTED. - SNS 수신 권한 이미 승인 한 경우\n\n");

        } else {//SNS 수신 권한 미승인 한 경우
            Toast.makeText(this, "SMS PERMISSION NOT GRANTED.", Toast.LENGTH_SHORT).show();
            tv_Log.setText(tv_Log.getText()+"SMS PERMISSION NOT GRANTED. - SNS 수신 권한 미승인 한 경우\n\n");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                Toast.makeText(this, "NEEDS TO SNS PERMISSION EXPLAIN", Toast.LENGTH_SHORT).show();
                tv_Log.setText(tv_Log.getText()+"NEEDS TO SNS PERMISSION EXPLAIN - SNS 수신 권한을 거부 하면 관련 설명 필요함을 표시\n\n");
                //SNS 수신 권한을 거부 하면 관련 설명 필요함을 표시

            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS}, 1);
                //onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) 을 호출
                //requestCode에 맞춰서 호출해야 함. 현재 이곳에서는 1로 호출

            }
        }

        Button button = (Button) findViewById(R.id.btn_goToSmsActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SmsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SMS);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //권한 승인 선택에 따른 설정
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS PERMISSION AGREE", Toast.LENGTH_SHORT).show();
                    tv_Log.setText(tv_Log.getText()+"SMS PERMISSION AGREE - SMS 권한을 사용자가 승인함.\n\n");
                    //SMS 권한을 사용자가 승인함.
                } else {
                    Toast.makeText(this, "SMS PERMISSION DISAGREE", Toast.LENGTH_SHORT).show();
                    tv_Log.setText(tv_Log.getText()+"SMS PERMISSION DISAGREE - SMS 권한 거부됨.\n\n");
                    //SMS 권한 거부됨.
                }
                return;
            }
        }
    }
}
