package com.tdc.theburgerclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DebtorsActivity extends AppCompatActivity {

    FirebaseConnector firebaseConnector = new FirebaseConnector();

    private String eventDate;

    private TextView headlineTextView;
    private TextView nameTextView;
    private TextView debtTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtors);

        Intent intent = getIntent();
        eventDate = intent.getStringExtra(MainActivity.EVENT_DATE);

        headlineTextView = findViewById(R.id.debtorsHeadlineTextView);
        headlineTextView.setText(eventDate);

        nameTextView = findViewById(R.id.debtorsNameTextView);
        debtTextView = findViewById(R.id.debtorsDebtTextView);

        firebaseConnector.populateDebtorsList(eventDate, nameTextView, debtTextView);
    }
}
