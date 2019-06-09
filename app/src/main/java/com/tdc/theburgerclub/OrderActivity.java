package com.tdc.theburgerclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tdc.theburgerclub.dtos.Dip;
import com.tdc.theburgerclub.dtos.Order;
import com.tdc.theburgerclub.enums.Burger;
import com.tdc.theburgerclub.enums.BurgerOrMenu;
import com.tdc.theburgerclub.enums.DipEnum;
import com.tdc.theburgerclub.enums.Soda;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    FirebaseConnector firebaseConnector = new FirebaseConnector();

    String eventDate;

    private static final int CHEESE_PRICE = 7;
    private static final int BACON_PRICE = 10;
    private static final int DIPS_PRICE = 7;
    String burgerName;
    String burgerPrice;
    String menuPrice;

    View accessoriesLinearLayout;

    TextView orderHeadlineTextView;
    TextView priceTextView;

    RadioButton standAloneRadioButton;
    RadioButton menuRadioButton;

    RadioButton colaRadioButton;
    RadioButton orangeRadioButton;
    RadioButton sportRadioButton;

    CheckBox cheeseCheckBox;
    CheckBox baconCheckBox;

    TextView mayoNumberTextView;
    TextView ketchupNumberTextView;
    TextView remouladeNumberTextView;
    TextView chilimayoNumberTextView;
    TextView aioliNumberTextView;

    Button mayoMinusButton;
    Button mayoPlusButton;
    Button ketchupMinusButton;
    Button ketchupPlusButton;
    Button remouladeMinusButton;
    Button remouladePlusButton;
    Button chilimayoMinusButton;
    Button chilimayoPlusButton;
    Button aioliMinusButton;
    Button aioliPlusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        eventDate = intent.getStringExtra(MainActivity.EVENT_DATE);
        burgerName = intent.getStringExtra(MainActivity.BURGER_NAME);
        burgerPrice = intent.getStringExtra(MainActivity.BURGER_PRICE);
        menuPrice = intent.getStringExtra(MainActivity.MENU_PRICE);

        orderHeadlineTextView = findViewById(R.id.orderHeadlineTextView);
        orderHeadlineTextView.setText(burgerName);

        accessoriesLinearLayout = findViewById(R.id.accessoriesLinearLayout);

        priceTextView = findViewById(R.id.popupPriceTextView);
        priceTextView.setText(burgerPrice + ",-");

        setupBurgerOrMenuRadioButtons();
        setupSodaRadioButtons();
        setupCheckBoxes();
        setupCounters();
        setupOrderButton();
    }

    private void setupBurgerOrMenuRadioButtons() {
        standAloneRadioButton = findViewById(R.id.popupStandAloneRadioButton);
        standAloneRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked) {
                    menuRadioButton.setChecked(false);
                    calculatePrice();
                    resetDipCounters();
                    accessoriesLinearLayout.setVisibility(LinearLayout.INVISIBLE);
                }
            }
        });

        menuRadioButton = findViewById(R.id.popupMenuRadioButton);
        menuRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked) {
                    standAloneRadioButton.setChecked(false);
                    calculatePrice();
                    accessoriesLinearLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });
    }

    private void resetDipCounters() {
        mayoNumberTextView.setText("0");
        ketchupNumberTextView.setText("0");
        remouladeNumberTextView.setText("0");
        chilimayoNumberTextView.setText("0");
        aioliNumberTextView.setText("0");
    }

    private void setupSodaRadioButtons() {
        colaRadioButton = findViewById(R.id.colaRadioButton);
        colaRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked) {
                    orangeRadioButton.setChecked(false);
                    sportRadioButton.setChecked(false);
                }
            }
        });
        orangeRadioButton = findViewById(R.id.orangeRadioButton);
        orangeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked) {
                    colaRadioButton.setChecked(false);
                    sportRadioButton.setChecked(false);
                }
            }
        });
        sportRadioButton = findViewById(R.id.sportRadioButton);
        sportRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked) {
                    colaRadioButton.setChecked(false);
                    orangeRadioButton.setChecked(false);
                }
            }
        });
    }

    private void setupCheckBoxes() {
        cheeseCheckBox = findViewById(R.id.cheeseCheckBox);
        calculatePriceOnClickListener(cheeseCheckBox);
        baconCheckBox = findViewById(R.id.baconCheckBox);
        calculatePriceOnClickListener(baconCheckBox);
    }

    private void calculatePriceOnClickListener(CheckBox checkBox) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePrice();
            }
        });
    }

    private void setupCounters() {
        mayoNumberTextView = findViewById(R.id.mayoNumberTextView);
        mayoMinusButton = findViewById(R.id.mayoMinusButton);
        mayoPlusButton = findViewById(R.id.mayoPlusButton);
        mayoMinusButton.setOnClickListener(dipsMinusOnClickListener(mayoNumberTextView));
        mayoPlusButton.setOnClickListener(dipsPlusOnClickListener(mayoNumberTextView));

        ketchupNumberTextView = findViewById(R.id.ketchupNumberTextView);
        ketchupMinusButton = findViewById(R.id.ketchupMinusButton);
        ketchupPlusButton = findViewById(R.id.ketchupPlusButton);
        ketchupMinusButton.setOnClickListener(dipsMinusOnClickListener(ketchupNumberTextView));
        ketchupPlusButton.setOnClickListener(dipsPlusOnClickListener(ketchupNumberTextView));

        remouladeNumberTextView = findViewById(R.id.remouladeNumberTextView);
        remouladeMinusButton = findViewById(R.id.remouladeMinusButton);
        remouladePlusButton = findViewById(R.id.remouladePlusButton);
        remouladeMinusButton.setOnClickListener(dipsMinusOnClickListener(remouladeNumberTextView));
        remouladePlusButton.setOnClickListener(dipsPlusOnClickListener(remouladeNumberTextView));

        chilimayoNumberTextView = findViewById(R.id.chilimayoNumberTextView);
        chilimayoMinusButton = findViewById(R.id.chilimayoMinusButton);
        chilimayoPlusButton = findViewById(R.id.chilimayoPlusButton);
        chilimayoMinusButton.setOnClickListener(dipsMinusOnClickListener(chilimayoNumberTextView));
        chilimayoPlusButton.setOnClickListener(dipsPlusOnClickListener(chilimayoNumberTextView));

        aioliNumberTextView = findViewById(R.id.aioliNumberTextView);
        aioliMinusButton = findViewById(R.id.aioliMinusButton);
        aioliPlusButton = findViewById(R.id.aioliPlusButton);
        aioliMinusButton.setOnClickListener(dipsMinusOnClickListener(aioliNumberTextView));
        aioliPlusButton.setOnClickListener(dipsPlusOnClickListener(aioliNumberTextView));
    }

    private View.OnClickListener dipsMinusOnClickListener(final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(textView.getText().toString()) - 1;
                if (counter >= 0) {
                    String counterString = counter + "";
                    textView.setText(counterString);
                    calculatePrice();
                }
            }
        };
    }

    private View.OnClickListener dipsPlusOnClickListener(final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String counter = Integer.parseInt(textView.getText().toString()) + 1 + "";
                textView.setText(counter);
                calculatePrice();
            }
        };
    }

    private void calculatePrice() {
        int calculatedPrice = 0;
        if(standAloneRadioButton.isChecked())
            calculatedPrice += Integer.parseInt(burgerPrice);
        if(menuRadioButton.isChecked())
            calculatedPrice += Integer.parseInt(menuPrice);

        if(cheeseCheckBox.isChecked())
            calculatedPrice += CHEESE_PRICE;
        if(baconCheckBox.isChecked())
            calculatedPrice += BACON_PRICE;

        if(!mayoNumberTextView.getText().equals("0"))
            calculatedPrice += Integer.parseInt(mayoNumberTextView.getText().toString()) * DIPS_PRICE;
        if(!ketchupNumberTextView.getText().equals("0"))
            calculatedPrice += Integer.parseInt(ketchupNumberTextView.getText().toString()) * DIPS_PRICE;
        if(!remouladeNumberTextView.getText().equals("0"))
            calculatedPrice += Integer.parseInt(remouladeNumberTextView.getText().toString()) * DIPS_PRICE;
        if(!chilimayoNumberTextView.getText().equals("0"))
            calculatedPrice += Integer.parseInt(chilimayoNumberTextView.getText().toString()) * DIPS_PRICE;
        if(!aioliNumberTextView.getText().equals("0"))
            calculatedPrice += Integer.parseInt(aioliNumberTextView.getText().toString()) * DIPS_PRICE;

        String priceString = calculatedPrice + ",-";
        priceTextView.setText(priceString);
    }

    private void setupOrderButton() {
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ordererName = getOrdererName();
                if(!ordererName.equals("")) {
                    Burger burger = Burger.fromString(burgerName);
                    BurgerOrMenu burgerOrMenu = isBurgerOrMenu();
                    Soda soda = getChosenSoda();
                    boolean withCheese = isWithCheese();
                    boolean withBacon = isWithBacon();
                    List<Dip> dips = getDips();
                    String priceString = priceTextView.getText().toString();
                    int price = Integer.parseInt(priceString.substring(0, priceString.length() - 2));
                    Order order = new Order(ordererName, burger, burgerOrMenu, soda, withCheese, withBacon, dips, price);
                    firebaseConnector.addOrderToEvent(order, eventDate);
                    finish();
                }
            }

            private String getOrdererName() {
                EditText nameEditText = findViewById(R.id.nameEditText);
                return nameEditText.getText().toString();
            }

            private BurgerOrMenu isBurgerOrMenu() {
                if(standAloneRadioButton.isChecked()) {
                    return BurgerOrMenu.STANDALONE;
                } else if(menuRadioButton.isChecked()) {
                    return BurgerOrMenu.MENU;
                }

                return null;
            }

            private Soda getChosenSoda() {
                Soda chosenSoda = null;

                if(menuRadioButton.isChecked()) {
                    if (colaRadioButton.isChecked())
                        chosenSoda = Soda.COLA;
                    if (orangeRadioButton.isChecked())
                        chosenSoda = Soda.ORANGE;
                    if (sportRadioButton.isChecked())
                        chosenSoda = Soda.SPORT;
                }

                return chosenSoda;
            }

            private boolean isWithCheese() {
                if(cheeseCheckBox.isChecked()) {
                    return true;
                }
                return false;
            }

            private boolean isWithBacon() {
                if(baconCheckBox.isChecked()) {
                    return true;
                }
                return false;
            }

            private List<Dip> getDips() {
                List<Dip> dips = null;

                if(menuRadioButton.isChecked()) {
                    dips = new ArrayList<>();
                    if (!mayoNumberTextView.getText().equals("0")) {
                        Dip dip = new Dip();
                        dip.setDipEnum(DipEnum.MAYO);
                        dip.setAmount(Integer.parseInt(mayoNumberTextView.getText().toString()));
                        dips.add(dip);
                    }
                    if (!ketchupNumberTextView.getText().equals("0")) {
                        Dip dip = new Dip();
                        dip.setDipEnum(DipEnum.KETCHUP);
                        dip.setAmount(Integer.parseInt(ketchupNumberTextView.getText().toString()));
                        dips.add(dip);
                    }
                    if (!remouladeNumberTextView.getText().equals("0")) {
                        Dip dip = new Dip();
                        dip.setDipEnum(DipEnum.REMOULADE);
                        dip.setAmount(Integer.parseInt(remouladeNumberTextView.getText().toString()));
                        dips.add(dip);
                    }
                    if (!chilimayoNumberTextView.getText().equals("0")) {
                        Dip dip = new Dip();
                        dip.setDipEnum(DipEnum.CHILIMAYO);
                        dip.setAmount(Integer.parseInt(chilimayoNumberTextView.getText().toString()));
                        dips.add(dip);
                    }
                    if (!aioliNumberTextView.getText().equals("0")) {
                        Dip dip = new Dip();
                        dip.setDipEnum(DipEnum.AIOLI);
                        dip.setAmount(Integer.parseInt(aioliNumberTextView.getText().toString()));
                        dips.add(dip);
                    }
                }

                return dips;
            }
        });
    }
}
