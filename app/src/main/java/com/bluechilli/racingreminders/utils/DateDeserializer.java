package com.bluechilli.racingreminders.utils;

/**
 * Created by monishi on 15/01/15.
 */

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public final class DateDeserializer implements JsonDeserializer<Date> {

    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd"
    };

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        for (String format : DATE_FORMATS) {
            try {
                String date = json.getAsString();

                if(!TextUtils.isEmpty(date)) {
                    return new SimpleDateFormat(format, Locale.US).parse(json.getAsString());
                }

                return null;

            } catch (ParseException e) {

            }
        }
        throw new JsonParseException("Unparseable date: \"" + json.getAsString()
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}