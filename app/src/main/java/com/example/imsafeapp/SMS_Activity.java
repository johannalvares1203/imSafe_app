package com.example.imsafeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMS_Activity extends BroadcastReceiver {

    private static final int RESULT_OK = -1; // You can use any value that suits your needs

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null && action.equals("SMS_SENT")) {
            int resultCode = getResultCode();

            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "Failed to send SMS: Generic failure", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "Failed to send SMS: No service", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "Failed to send SMS: Null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "Failed to send SMS: Radio off", Toast.LENGTH_SHORT).show();
                    break;


                //AIzaSyAlYQJnmY0bklqDfqZ5l9w069ohniV1FyA  api key
                //
            }
        }
    }
}