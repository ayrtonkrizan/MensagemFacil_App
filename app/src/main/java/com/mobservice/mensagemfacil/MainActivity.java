package com.mobservice.mensagemfacil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("/toSend/channel1");
    //private Query query = myRef.equalTo();
    private boolean executing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read from the database
        myRef.addChildEventListener(new ChildEventListener() {
            //myRef.addValueEventListener(new ValueEventListener() {
            /*@Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Messages> mList = new ArrayList<>();
                boolean taken = false;
                for (DataSnapshot unit : dataSnapshot.getChildren()){
                    Messages value = unit.getValue(Messages.class);
                    value.setKey(unit.getKey());
                    mList.add(value);
                    break;
                }
                Log.d("TAG", mList.toString());

                for (Messages msg : mList) {
                    for (String phone : msg.getPhones()) {
                        sendSMS(phone, msg.getText());
                    }
                    myRef.child(msg.getKey()).removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FBError", "Failed to read value.", error.toException());
            }
            */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages msg = dataSnapshot.getValue(Messages.class);
                msg.setKey(dataSnapshot.getKey());
                Log.d("Added", msg.toString());

                for (String phone : msg.getPhones()) {
                    sendSMS(phone, msg.getText());
                }
                myRef.child(msg.getKey()).removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        Button sms =  (Button) findViewById(R.id.btsms);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myRef.push().setValue("Novo!");
                Toast.makeText(getBaseContext(), "Uhul!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSMS(String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(this.getBaseContext(), 0,
                new Intent(this.getBaseContext(), SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this.getBaseContext(), 0,
                new Intent(this.getBaseContext(), SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SMS sending failed...",Toast.LENGTH_SHORT).show();
        }

    }
}

