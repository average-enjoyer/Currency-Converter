package com.example.currencyconverter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NonCashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_cash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickNonCashUSD(View view) {
        Intent intent = new Intent(NonCashActivity.this, NonCashActivityC.class);
        intent.putExtra("currency", "USD");
        startActivity(intent);
    }

    public void onClickNonCashEUR(View view) {
        Intent intent = new Intent(NonCashActivity.this, NonCashActivityC.class);
        intent.putExtra("currency", "EUR");
        startActivity(intent);
    }

    public void onClickNonCashRUR(View view) {
        Intent intent = new Intent(NonCashActivity.this, NonCashActivityC.class);
        intent.putExtra("currency", "RUR");
        startActivity(intent);
    }

    public void onClickNonCashBTC(View view) {
        Intent intent = new Intent(NonCashActivity.this, NonCashActivityC.class);
        intent.putExtra("currency", "BTC");
        startActivity(intent);
    }
}
