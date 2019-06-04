package com.tdc.theburgerclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrderList extends AppCompatActivity {

    FirebaseConnector firebaseConnector = new FirebaseConnector();

    String eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Intent intent = getIntent();
        eventDate = intent.getStringExtra(MainActivity.EVENT_DATE);

        TextView orderListHeadlineTextView = findViewById(R.id.orderListHeadlineTextView);
        orderListHeadlineTextView.setText(eventDate);

        firebaseConnector.getAllOrdersFromEvent(eventDate);
    }
}
