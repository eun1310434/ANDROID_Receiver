/*=====================================================================
□ INFORMATION
  ○ Data : 23.05.2018
  ○ Mail : eun1310434@naver.com
  ○ Blog : https://blog.naver.com/eun1310434
  ○ Reference
     - Do it android app Programming
     - Hello JAVA Programming
     - http://itmining.tistory.com/5

□ Study
  ○ BroadcastReceiver
     - Android apps can send or receive broadcast messages from the Android system and other Android apps, similar to the publish-subscribe design pattern.
       These broadcasts are sent when an event of interest occurs.
     - For example, the Android system sends broadcasts when various system events occur,
       such as when the system boots up or the device starts charging.
       Apps can also send custom broadcasts,

□ Function
  ○
=====================================================================*/

package com.eun1310434.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    //SMS 메시지를 받아 처리하기 위한 수신자
    public static final String TAG = "SmsReceiver";

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive()");

        Bundle bundle = intent.getExtras();// 메세지 수신 시 내용 Bundle 로 파싱
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages != null && messages.length > 0) {
            String sender = messages[0].getOriginatingAddress();// SMS 발신 번호 확인
            Log.i(TAG, "SMS sender : " + sender);

            String contents = messages[0].getMessageBody().toString();// SMS 메시지 확인
            Log.i(TAG, "SMS contents : " + contents);

            Date receivedDate = new Date(messages[0].getTimestampMillis());// SMS 수신 시간 확인
            Log.i(TAG, "SMS received date : " + receivedDate.toString());

            sendToActivity(context, sender, contents, receivedDate);
        }

    }


    private SmsMessage[] parseSmsMessage(Bundle bundle) {//SMS 메시지 파싱

        Object[] objs = (Object[]) bundle.get("pdus");// 국제표준 토콜 : pdus

        SmsMessage[] messages = new SmsMessage[objs.length];
        for (int i = 0; i < objs.length; i++) {
            // PDU 포맷 - 메시지 복원
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // API 23 이상 시 format Setting 필요
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);

            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }
        return messages;
    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate) {
        Intent myIntent = new Intent(context, SmsActivity.class);

        myIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK|
                Intent.FLAG_ACTIVITY_SINGLE_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        myIntent.putExtra("sender", sender);
        myIntent.putExtra("contents", contents);
        myIntent.putExtra("receivedDate", format.format(receivedDate));

        context.startActivity(myIntent);
    }

}
