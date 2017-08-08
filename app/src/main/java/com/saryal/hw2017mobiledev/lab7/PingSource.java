package com.saryal.hw2017mobiledev.lab7;

/**
 * Created by saryal on 8/7/17.
 */

import android.content.Context;
import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PingSource {

    public interface PingListener {
        void onPingsReceived(List<Ping> pingList);
    }

    private static PingSource sNewsSource;

    private Context mContext;

    public static PingSource get(Context context) {
        if (sNewsSource == null) {
            sNewsSource = new PingSource(context);
        }
        return sNewsSource;
    }

    private PingSource(Context context) {
        mContext = context;
    }

    // Firebase methods for you to implement.

    public void getPings(final PingListener pingListener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pingsRef = databaseReference.child("pings");
        Query last50PingsQuery = pingsRef.limitToLast(50);
        last50PingsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ping> pingsList = new ArrayList<>();
                Iterable<DataSnapshot> pingSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot snaps: pingSnapshots){
                    Ping ping = new Ping(snaps);
                    pingsList.add(ping);
                }
                pingListener.onPingsReceived(pingsList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getPingsForUserId(String userId, final PingListener pingListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pingsRef = databaseReference.child("pings");

        Query userQuery = pingsRef.orderByChild("userId").equalTo(userId).limitToLast(50);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ping> pingsList = new ArrayList<>();
                Iterable<DataSnapshot> pingSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot snaps: pingSnapshots){
                    Ping ping = new Ping(snaps);
                    pingsList.add(ping);

                }
                pingListener.onPingsReceived(pingsList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendPing(Ping ping) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pingsRef = databaseReference.child("pings");
        DatabaseReference newPingRef = pingsRef.push();
        Map<String,Object> pingValMap = new HashMap<>();
        pingValMap.put("userName",ping.getUserName());
        pingValMap.put("userId",ping.getUserId());
        pingValMap.put("timestamp", ServerValue.TIMESTAMP);
        newPingRef.setValue(pingValMap);
    }
}


