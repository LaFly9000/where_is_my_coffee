package com.thedock.whereismycoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Coffee extends AppCompatActivity {
    private static final String PREFS_NAME = "CoffeeTrackerPrefs";
    private static final String KEY_COFFEE_COUNT = "coffee_count";

    private EditText etCoffeeCount;
    private TextView tvPrediction;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coffee);

        etCoffeeCount = findViewById(R.id.etCoffeeCount);
        tvPrediction = findViewById(R.id.tvPrediction);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnSubtract = findViewById(R.id.btnSubtract);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedCoffeeCount = sharedPreferences.getInt(KEY_COFFEE_COUNT, 0);
        etCoffeeCount.setText(String.valueOf(savedCoffeeCount));
        updatePrediction(savedCoffeeCount);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int coffeeCount = getCoffeeCount();
                coffeeCount++;
                etCoffeeCount.setText(String.valueOf(coffeeCount));
                updatePrediction(coffeeCount);
                saveCoffeeCount(coffeeCount);
            }
        });

        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int coffeeCount = getCoffeeCount();
                if (coffeeCount > 0) {
                    coffeeCount--;
                    etCoffeeCount.setText(String.valueOf(coffeeCount));
                    updatePrediction(coffeeCount);
                    saveCoffeeCount(coffeeCount);
                }
            }
        });

        etCoffeeCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int coffeeCount = getCoffeeCount();
                updatePrediction(coffeeCount);
                saveCoffeeCount(coffeeCount);
            }
        });
    }

    private int getCoffeeCount() {
        String countStr = etCoffeeCount.getText().toString();
        if (countStr.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updatePrediction(int count) {
        tvPrediction.setText("預測日期：" + predictEndDate(count));
    }

    private String predictEndDate(int count) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int daysToAdd = 0;
        while (count > 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                count--;
            }
            daysToAdd++;
        }
        return sdf.format(calendar.getTime());
    }

    private void saveCoffeeCount(int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COFFEE_COUNT, count);
        editor.apply();
    }
}