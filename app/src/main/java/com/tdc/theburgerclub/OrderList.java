package com.tdc.theburgerclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class OrderList extends AppCompatActivity {

    FirebaseConnector firebaseConnector = new FirebaseConnector();

    TextView orderListClassicMenuTextView;
    TextView orderListLetsRumbleMenuTextView;
    TextView orderListRumbleInTheJungleMenuTextView;
    TextView orderListGreenBeastMenuTextView;
    TextView orderListPolloMenuTextView;
    TextView orderListGorillaMenuTextView;
    TextView orderListColaTextView;
    TextView orderListOrangeTextView;
    TextView orderListSportTextView;
    TextView orderListMayoTextView;
    TextView orderListKetchupTextView;
    TextView orderListRemouladeTextView;
    TextView orderListChilimayoTextView;
    TextView orderListAioliTextView;

    TextView orderListClassicTextView;
    TextView orderListLetsRumbleTextView;
    TextView orderListRumbleInTheJungleTextView;
    TextView orderListGreenBeastTextView;
    TextView orderListPolloTextView;
    TextView orderListGorillaTextView;

    TextView orderListSpecialTextView;

    String eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Intent intent = getIntent();
        eventDate = intent.getStringExtra(MainActivity.EVENT_DATE);

        TextView orderListHeadlineTextView = findViewById(R.id.orderListHeadlineTextView);
        orderListHeadlineTextView.setText(eventDate);

        setupTextViews();
        firebaseConnector.getAllOrdersFromEvent(eventDate, orderListClassicMenuTextView, orderListLetsRumbleMenuTextView,
                orderListRumbleInTheJungleMenuTextView, orderListGreenBeastMenuTextView, orderListPolloMenuTextView,
                orderListGorillaMenuTextView, orderListColaTextView, orderListOrangeTextView, orderListSportTextView,
                orderListMayoTextView, orderListKetchupTextView, orderListRemouladeTextView,
                orderListChilimayoTextView, orderListAioliTextView, orderListClassicTextView,
                orderListLetsRumbleTextView, orderListLetsRumbleTextView, orderListGreenBeastTextView,
                orderListPolloTextView, orderListGorillaTextView, orderListSpecialTextView);
    }

    private void setupTextViews() {
        orderListClassicMenuTextView = findViewById(R.id.orderListClassicMenuTextView);
        orderListLetsRumbleMenuTextView = findViewById(R.id.orderListLetsRumbleMenuTextView);
        orderListRumbleInTheJungleMenuTextView = findViewById(R.id.orderListRumbleInTheJungleMenuTextView);
        orderListGreenBeastMenuTextView = findViewById(R.id.orderListGreenBeastMenuTextView);
        orderListPolloMenuTextView = findViewById(R.id.orderListPolloMenuTextView);
        orderListGorillaMenuTextView = findViewById(R.id.orderListGorillaMenuTextView);
        orderListColaTextView = findViewById(R.id.orderListColaTextView);
        orderListOrangeTextView = findViewById(R.id.orderListOrangeTextView);
        orderListSportTextView = findViewById(R.id.orderListSportTextView);
        orderListMayoTextView = findViewById(R.id.orderListMayoTextView);
        orderListKetchupTextView = findViewById(R.id.orderListKetchupTextView);
        orderListRemouladeTextView = findViewById(R.id.orderListRemouladeTextView);
        orderListChilimayoTextView = findViewById(R.id.orderListChilimayoTextView);
        orderListAioliTextView = findViewById(R.id.orderListAioliTextView);

        orderListClassicTextView = findViewById(R.id.orderListClassicTextView);
        orderListLetsRumbleTextView = findViewById(R.id.orderListLetsRumbleTextView);
        orderListRumbleInTheJungleTextView = findViewById(R.id.orderListRumbleInTheJungleTextView);
        orderListGreenBeastTextView = findViewById(R.id.orderListGreenBeastTextView);
        orderListPolloTextView = findViewById(R.id.orderListPolloTextView);
        orderListGorillaTextView = findViewById(R.id.orderListGorillaTextView);

        orderListSpecialTextView = findViewById(R.id.orderListSpecialTextView);
    }
}
