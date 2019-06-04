package com.tdc.theburgerclub;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Spinner;

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
import com.tdc.theburgerclub.enums.DipEnum;
import com.tdc.theburgerclub.helpers.SpinnerHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void getAllOrdersFromEvent(String eventDateString) {
        final List<Order> standAloneOrders = new ArrayList<>();
        final List<Order> menuOrders = new ArrayList<>();
        final List<Order> ordersWithBacon = new ArrayList<>();
        final List<Order> ordersWithCheese = new ArrayList<>();
        final List<Order> ordersWithBaconAndCheese = new ArrayList<>();
        final List<Dip> dips = new ArrayList<>();
        final Dip mayoDip = new Dip(DipEnum.MAYO);
        final Dip ketchupDip = new Dip(DipEnum.KETCHUP);
        final Dip remouladeDip = new Dip(DipEnum.REMOULADE);
        final Dip chiliMayoDip = new Dip(DipEnum.CHILIMAYO);
        final Dip aioliDip = new Dip(DipEnum.AIOLI);

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
                            for(HashMap<String, Object> hashMap : ordersMap) {
                                Order order = new Order(hashMap);
                                orders.add(order);
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
                            if(order.isWithBacon()) {
                                if(order.isWithCheese()) {
                                    ordersWithBaconAndCheese.add(order);
                                    continue;
                                }
                                ordersWithBacon.add(order);
                                continue;
                            }
                            if(order.isWithCheese()) {
                                ordersWithCheese.add(order);
                                continue;
                            }
                            if(order.getBurgerOrMenu() == BurgerOrMenu.STANDALONE) {
                                standAloneOrders.add(order);
                                continue;
                            }
                            menuOrders.add(order);
                            populateDips(order);
                        }
                        dips.add(mayoDip);
                        dips.add(ketchupDip);
                        dips.add(remouladeDip);
                        dips.add(chiliMayoDip);
                        dips.add(aioliDip);
                    }

                    private void populateDips(Order order) {
                        if(!order.getDips().isEmpty()) {
                            for(Dip dip : order.getDips()) {
                                DipEnum dipEnum = dip.getDipEnum();
                                int amount = dip.getAmount();
                                switch (dipEnum) {
                                    case MAYO:
                                        mayoDip.setAmount(mayoDip.getAmount() + amount);
                                        break;
                                    case KETCHUP:
                                        ketchupDip.setAmount(mayoDip.getAmount() + amount);
                                        break;
                                    case REMOULADE:
                                        remouladeDip.setAmount(mayoDip.getAmount() + amount);
                                        break;
                                    case CHILIMAYO:
                                        chiliMayoDip.setAmount(mayoDip.getAmount() + amount);
                                        break;
                                    case AIOLI:
                                        aioliDip.setAmount(mayoDip.getAmount() + amount);
                                        break;
                                }
                            }
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
