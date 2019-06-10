package com.tdc.theburgerclub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.tdc.theburgerclub.helpers.SpinnerHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    FirebaseConnector firebaseConnector = new FirebaseConnector();

    PopupWindow menuCardPopupWindow;
    PopupWindow eventPopupWindow;

    Spinner eventSpinner;

    public static final String EVENT_DATE = "eventDate";
    public static final String BURGER_NAME = "burgerName";
    public static final String BURGER_PRICE = "burgerPrice";
    public static final String MENU_PRICE = "menuPrice";

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupEventSpinner();
        setImageViewOnTouchEffects();
        setupPopupWindow();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setImageViewOnTouchEffects() {
        final ImageView classicImageView = findViewById(R.id.classicImageView);
        final ImageView letsRumbleImageView = findViewById(R.id.letsRumbleImageView);
        final ImageView rumbleInTheJungleImageView = findViewById(R.id.rumbleInTheJungleImageView);
        final ImageView greenBeastImageView = findViewById(R.id.greenBeastImageView);
        final ImageView polloImageView = findViewById(R.id.polloImageView);
        final ImageView gorillaImageView = findViewById(R.id.gorillaImageView);

        classicImageView.setOnTouchListener(onTouch(classicImageView, R.drawable.burger_classic_open, R.drawable.burger_classic_closed));
        letsRumbleImageView.setOnTouchListener(onTouch(letsRumbleImageView, R.drawable.burger_letsrumble_open, R.drawable.burger_letsrumble_closed));
        rumbleInTheJungleImageView.setOnTouchListener(onTouch(rumbleInTheJungleImageView, R.drawable.burger_rumbleinthejungle_open, R.drawable.burger_rumbleinthejungle_closed));
        greenBeastImageView.setOnTouchListener(onTouch(greenBeastImageView, R.drawable.burger_greenbeast_open, R.drawable.burger_greenbeast_closed));
        polloImageView.setOnTouchListener(onTouch(polloImageView, R.drawable.burger_pollo_open, R.drawable.burger_pollo_closed));
        gorillaImageView.setOnTouchListener(onTouch(gorillaImageView, R.drawable.burger_gorilla_open, R.drawable.burger_gorilla_closed));
    }

    private View.OnTouchListener onTouch(final ImageView imageView, final int closedMenu, final int openMenu) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), closedMenu));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), openMenu));
                }
                return true;
            }
        };
    }

    private void setupPopupWindow() {
        menuCardPopupWindow = new PopupWindow(this);
        final View menucardLayout = LayoutInflater.from(this).inflate(R.layout.popup_menucard, null);
        FloatingActionButton but = findViewById(R.id.addFloatingActionButton);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!menuCardPopupWindow.isShowing()) {
                    menuCardPopupWindow.showAtLocation(menucardLayout, Gravity.CENTER, 0, 0);
                } else {
                    menuCardPopupWindow.dismiss();
                }
            }
        });
        menuCardPopupWindow.setContentView(menucardLayout);

        setupMenucardPopupButtons(menucardLayout);
    }

    private void setupMenucardPopupButtons(View menucardLayout) {
        Button classicOrderButton = menucardLayout.findViewById(R.id.classicOrderButton);
        classicOrderButton.setOnClickListener(openOrderActivityonClickListener(R.string.name_classic, R.string.burgerprice_classic, R.string.menuprice_classic));

        Button letsRumbleOrderButton = menucardLayout.findViewById(R.id.letsRumbleOrderButton);
        letsRumbleOrderButton.setOnClickListener(openOrderActivityonClickListener(R.string.name_letsrumble, R.string.burgerprice_letsrumble, R.string.menuprice_letsrumble));

        Button rumbleInTheJungleOrderButton = menucardLayout.findViewById(R.id.rumbleInTheJungleOrderButton);
        rumbleInTheJungleOrderButton.setOnClickListener(openOrderActivityonClickListener(R.string.name_rumbleinthejungle, R.string.burgerprice_rumbleinthejungle, R.string.menuprice_rumbleinthejungle));

        Button greenBeastOrderButton = menucardLayout.findViewById(R.id.greenBeastOrderButton);
        greenBeastOrderButton.setOnClickListener(openOrderActivityonClickListener(R.string.name_greenbeast, R.string.burgerprice_greenbeast, R.string.menuprice_greenbeast));

        Button polloOrderButton = menucardLayout.findViewById(R.id.polloOrderButton);
        polloOrderButton.setOnClickListener(openOrderActivityonClickListener(R.string.name_pollo, R.string.burgerprice_pollo, R.string.menuprice_pollo));

        Button gorillaOrderButton = menucardLayout.findViewById(R.id.gorillaOrderButton);
        gorillaOrderButton.setOnClickListener(openOrderActivityonClickListener(R.string.name_gorilla, R.string.burgerprice_gorilla, R.string.menuprice_gorilla));
    }

    private View.OnClickListener openOrderActivityonClickListener(final int burgerNameId, final int burgerPriceId, final int menuPriceId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventSpinner.getSelectedItem() != null) {
                    String burgerName = getResources().getString(burgerNameId);
                    String burgerPriceFull = getResources().getString(burgerPriceId);
                    String burgerPrice = burgerPriceFull.substring(0, burgerPriceFull.length() - 2);
                    String menuPriceFull = getResources().getString(menuPriceId);
                    String menuPrice = menuPriceFull.substring(0, menuPriceFull.length() - 2);
                    openOrderActivity(burgerName, burgerPrice, menuPrice);
                }
            }
        };
    }

    private void openOrderActivity(String burgerName, String burgerPrice, String menuPrice) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(EVENT_DATE, eventSpinner.getSelectedItem().toString());
        intent.putExtra(BURGER_NAME, burgerName);
        intent.putExtra(BURGER_PRICE, burgerPrice);
        intent.putExtra(MENU_PRICE, menuPrice);
        startActivity(intent);
        menuCardPopupWindow.dismiss();
    }

    private void setupEventSpinner() {
        eventSpinner = findViewById(R.id.eventSpinner);
        List<String> emptyList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, emptyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);
        firebaseConnector.populateSpinnerWithAllEvents(eventSpinner);
    }

    @Override
    public void onBackPressed() {
        if (menuCardPopupWindow.isShowing()) {
            menuCardPopupWindow.dismiss();
        } else if (eventPopupWindow.isShowing()) {
            eventPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_AddEvent) {
            setupEventPopup();
            return true;
        } else if (id == R.id.action_SeeEvent) {
            setupSeeEvent();
            return true;
        } else if (id == R.id.action_SeeDebtors) {
            setupSeeDebtors();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupEventPopup() {
        eventPopupWindow = new PopupWindow(this);
        final View eventLayout = LayoutInflater.from(this).inflate(R.layout.popup_event, null);
        eventPopupWindow.setContentView(eventLayout);

        Button createEventButton = eventLayout.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker eventDatePicker = eventLayout.findViewById(R.id.popupEventDatePicker);
                Calendar eventDate = getDateFromDatePicker(eventDatePicker);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String eventDateString = sdf.format(eventDate.getTime());

                SpinnerHelper.addTextToSpinner(eventSpinner, eventDateString);
                firebaseConnector.addEvent(eventDate);
                eventPopupWindow.dismiss();
            }
        });

        eventPopupWindow.showAtLocation(eventLayout, Gravity.CENTER, 0, 0);
    }

    private Calendar getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);

        return calendar;
    }

    private void setupSeeEvent() {
        if (eventSpinner.getSelectedItem() != null) {
            Intent intent = new Intent(this, OrderList.class);
            intent.putExtra(EVENT_DATE, eventSpinner.getSelectedItem().toString());
            startActivity(intent);
        }
    }

    private void setupSeeDebtors() {
        if (eventSpinner.getSelectedItem() != null) {
            Intent intent = new Intent(this, DebtorsActivity.class);
            intent.putExtra(EVENT_DATE, eventSpinner.getSelectedItem().toString());
            startActivity(intent);
        }
    }
}
