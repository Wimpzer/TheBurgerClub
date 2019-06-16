package com.tdc.theburgerclub;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tdc.theburgerclub.dtos.Dip;
import com.tdc.theburgerclub.dtos.Order;
import com.tdc.theburgerclub.enums.BurgerOrMenu;
import com.tdc.theburgerclub.enums.Soda;
import com.tdc.theburgerclub.helpers.SpinnerHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseConnector {

    private static final String ORDERS = "orders";
    private static final String EVENTS = "events";
    private static final String TAG = "FirebaseConnector";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addEvent(Calendar eventDate) {
        Map<String, Object> event = new HashMap<>();
        event.put(MainActivity.EVENT_DATE, eventDate.getTime());
        db.collection(EVENTS)
                .document(eventDate.getTime().toString())
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "EventDate successfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding EventDate", e);
                    }
                });
    }

    public void populateSpinnerWithAllEvents(final Spinner spinner) {
        db.collection(EVENTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Timestamp timestamp = (Timestamp) document.getData().get(MainActivity.EVENT_DATE);
                                String eventDate = convertTimestampToString(timestamp);
                                SpinnerHelper.addTextToSpinner(spinner, eventDate);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void addOrderToEvent(final Order order, final String eventDateString) {
        db.collection(EVENTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Order> orders = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<Order> existingOrders = (List<Order>) document.getData().get(ORDERS);
                                if (existingOrders != null)
                                    orders = existingOrders;
                            }
                            Date eventDate = convertStringToDate(eventDateString);

                            orders.add(order);

                            Map<String, Object> event = new HashMap<>();
                            event.put(MainActivity.EVENT_DATE, eventDate);
                            event.put(ORDERS, orders);

                            db.collection(EVENTS)
                                    .document(eventDate.toString())
                                    .set(event)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Order successfully added");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding order", e);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "Error getting orders.", task.getException());
                        }
                    }
                });
    }

    public void getAllOrdersFromEvent(String eventDateString, final LinearLayout menusLinearLayout, final TextView classicMenuTextView, final TextView letsRumbleMenuTextView,
                                      final TextView rumbleInTheJungleMenuTextView, final TextView greenBeastMenuTextView, final TextView polloMenuTextView, final TextView gorillaMenuTextView,
                                      final TextView colaTextView, final TextView orangeTextView, final TextView sportTextView, final TextView mayoTextView,
                                      final TextView ketchupTextView, final TextView remouladeTextView, final TextView chilimayoTextView, final TextView aioliTextView,
                                      final LinearLayout burgerLinearLayout, final TextView classicTextView, final TextView letsRumbleTextView, final TextView rumbleInTheJungleTextView,
                                      final TextView greenBeastTextView, final TextView polloTextView, final TextView gorillaTextView, final LinearLayout specialLinearLayout,
                                      final TextView specialTextView) {
        Date eventDate = convertStringToDate(eventDateString);

        db.collection(EVENTS)
                .document(eventDate.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Order> orders = new ArrayList<>();
                            List<HashMap<String, Object>> ordersMap = (List<HashMap<String, Object>>) task.getResult().get("orders");
                            if(ordersMap != null) {
                                for (HashMap<String, Object> hashMap : ordersMap) {
                                    Order order = new Order(hashMap);
                                    orders.add(order);
                                }
                            }

                            if(orders.size() > 0) {
                                populateOrders(orders);
                            }
                        } else {
                            Log.w(TAG, "Error getting orders.", task.getException());
                        }
                    }

                    private void populateOrders(List<Order> orders) {
                        for(Order order : orders) {
                            if(order.isWithBacon() || order.isWithCheese()) {
                                specialLinearLayout.setVisibility(View.VISIBLE);
                                populateSpecialOrdersTextView(order);
                                continue;
                            }
                            if(order.getBurgerOrMenu() == BurgerOrMenu.STANDALONE) {
                                burgerLinearLayout.setVisibility(View.VISIBLE);
                                populateBurgerTextViews(order);
                                continue;
                            }
                            menusLinearLayout.setVisibility(View.VISIBLE);
                            populateMenuTextViews(order);
                        }
                    }

                    private void populateSpecialOrdersTextView(Order order) {
                        specialTextView.append(order.getBurgerOrMenu().toString() + ": " + order.getBurgerName().getName());

                        if(order.isWithCheese())
                            specialTextView.append(" + cheese");
                        if(order.isWithBacon())
                            specialTextView.append(" + bacon");
                        specialTextView.append("\n");
                    }

                    private void populateBurgerTextViews(Order order) {
                        switchCaseBurgerName(order.getBurgerName().getName(), classicTextView, letsRumbleTextView,
                                rumbleInTheJungleTextView, greenBeastTextView, polloTextView, gorillaTextView);
                    }

                    private void populateMenuTextViews(Order order) {
                        switchCaseBurgerName(order.getBurgerName().getName(), classicMenuTextView, letsRumbleMenuTextView,
                                rumbleInTheJungleMenuTextView, greenBeastMenuTextView, polloMenuTextView, gorillaMenuTextView);
                        switchCaseSodas(order.getSoda());
                        switchCaseDips(order.getDips());
                    }

                    private void switchCaseBurgerName(String name, TextView classicTextView, TextView letsRumbleTextView, TextView rumbleInTheJungleTextView,
                                                      TextView greenBeastTextView, TextView polloTextView, TextView gorillaTextView) {
                        String string;

                        switch(name) {
                            case "Classic":
                                string = getCountFromTextView(classicTextView, 1) + "x " + "Classic";
                                classicTextView.setText(string);
                                break;
                            case "Let\'s Rumble":
                                string = getCountFromTextView(letsRumbleTextView, 1) + "x " + "Let\'s Rumble";
                                letsRumbleTextView.setText(string);
                                break;
                            case "Rumble in the jungle":
                                string = getCountFromTextView(rumbleInTheJungleTextView, 1) + "x " + "Rumble in the jungle";
                                rumbleInTheJungleTextView.setText(string);
                                break;
                            case "Green beast Veggie":
                                string = getCountFromTextView(greenBeastTextView, 1) + "x " + "Green beast";
                                greenBeastTextView.setText(string);
                                break;
                            case "Pollo burger":
                                string = getCountFromTextView(polloTextView, 1) + "x " + "Pollo";
                                polloTextView.setText(string);
                                break;
                            case "Gorilla":
                                string = getCountFromTextView(gorillaTextView, 1) + "x " + "Gorilla";
                                gorillaTextView.setText(string);
                                break;
                        }
                    }

                    private void switchCaseSodas(Soda soda) {
                        String string;

                        switch(soda.toString()) {
                            case "COLA":
                                string = getCountFromTextView(colaTextView, 1) + "x " + "cola";
                                colaTextView.setText(string);
                                break;
                            case "ORANGE":
                                string = getCountFromTextView(orangeTextView, 1) + "x " + "orange";
                                orangeTextView.setText(string);
                                break;
                            case "SPORT":
                                string = getCountFromTextView(sportTextView, 1) + "x " + "sport";
                                sportTextView.setText(string);
                                break;
                        }
                    }

                    private void switchCaseDips(List<Dip> dips) {
                        for(Dip dip : dips) {
                            String string;
                            switch(dip.getDipEnum().toString()) {
                                case "MAYO":
                                    string = getCountFromTextView(mayoTextView, dip.getAmount()) + "x " + "mayo";
                                    mayoTextView.setText(string);
                                    break;
                                case "KETCHUP":
                                    string = getCountFromTextView(ketchupTextView, dip.getAmount()) + "x " + "ketchup";
                                    ketchupTextView.setText(string);
                                    break;
                                case "REMOULADE":
                                    string = getCountFromTextView(remouladeTextView, dip.getAmount()) + "x " + "remoulade";
                                    remouladeTextView.setText(string);
                                    break;
                                case "CHILIMAYO":
                                    string = getCountFromTextView(chilimayoTextView, dip.getAmount()) + "x " + "chilimayo";
                                    chilimayoTextView.setText(string);
                                    break;
                                case "AIOLI":
                                    string = getCountFromTextView(aioliTextView, dip.getAmount()) + "x " + "aioli";
                                    aioliTextView.setText(string);
                                    break;
                            }
                        }
                    }

                    private int getCountFromTextView(TextView textView, int increaseCount) {
                        int count;
                        String textViewString = textView.getText().toString();
                        String countString = textViewString.split("x")[0];

                        if(countString.equals("")) {
                            count = increaseCount;
                        } else {
                            count = Integer.parseInt(countString) + increaseCount;
                        }

                        textView.setVisibility(View.VISIBLE);

                        return count;
                    }
                });
    }

    public void populateDebtorsList(String eventDateString, final TextView nameTextView, final TextView debtTextView) {
        Date eventDate = convertStringToDate(eventDateString);

        db.collection(EVENTS)
                .document(eventDate.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Order> orders = new ArrayList<>();
                            List<HashMap<String, Object>> ordersMap = (List<HashMap<String, Object>>) task.getResult().get("orders");
                            if(ordersMap != null) {
                                for (HashMap<String, Object> hashMap : ordersMap) {
                                    Order order = new Order(hashMap);
                                    orders.add(order);
                                }
                            }

                            if(orders.size() > 0) {
                                populateTextViews(orders);
                            }
                        } else {
                            Log.w(TAG, "Error getting orders.", task.getException());
                        }
                    }

                    private void populateTextViews(List<Order> orders) {
                        for(Order order : orders) {
                            nameTextView.append(order.getOrdererName() + "\n");
                            debtTextView.append(order.getPrice() + ",-\n");
                        }
                    }
                });
    }

    private String convertTimestampToString(Timestamp timestamp) {
        String dateString;

        Date date = new Date(timestamp.getSeconds() * 1000);
        dateString = new SimpleDateFormat("dd-MM-yyyy").format(date);

        return dateString;
    }

    private Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
