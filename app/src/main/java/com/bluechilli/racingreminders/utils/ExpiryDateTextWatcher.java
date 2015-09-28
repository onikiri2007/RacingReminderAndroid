package com.bluechilli.racingreminders.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by monishi on 16/02/15.
 */
public class ExpiryDateTextWatcher implements TextWatcher {

    private final EditText text;
    private String current = "";
    private String mmyyyy = "MMYYYY";
    private Calendar cal = Calendar.getInstance();


    public ExpiryDateTextWatcher(EditText text) {

        this.text = text;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d.]", "");
            String cleanC = current.replaceAll("[^\\d.]", "");

            int cl = clean.length();
            int sel = cl;

            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }

            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + mmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int mon  = Integer.parseInt(clean.substring(0, 2));
                int year  = Integer.parseInt(clean.substring(2, 4));

                if(mon > 12) mon = 12;

                cal.set(Calendar.MONTH, mon-1);
                year = year;
                clean = String.format("%02d%02d", mon, year);
            }

            clean = String.format("%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            text.setText(current);
            text.setSelection(sel < current.length() ? sel : current.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
