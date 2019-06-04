package com.tdc.theburgerclub.helpers;

import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tdc.theburgerclub.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpinnerHelper {

    public static void addTextToSpinner(Spinner spinner, String textToBeAdded) {
        List<String> existingStrings = retrieveAllSpinnerItems(spinner);
        existingStrings.add(textToBeAdded);
        sortArray(existingStrings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getContext(), android.R.layout.simple_spinner_item, existingStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static List<String> retrieveAllSpinnerItems(Spinner spinner) {
        Adapter adapter = spinner.getAdapter();
        int n = adapter.getCount();
        List<String> strings = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            String string = (String) adapter.getItem(i);
            strings.add(string);
        }
        return strings;
    }

    private static void sortArray(List<String> existingStrings) {
        Collections.sort(existingStrings, new Comparator<String>() {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return dateFormat.parse(o1).compareTo(dateFormat.parse(o2));
                } catch(ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }
}
