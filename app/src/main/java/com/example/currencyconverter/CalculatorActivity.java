package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {

    private String CNAME = "";
    private float BUY = 0;
    private float SALE = 0;
    private RadioGroup radioGroup;
    private Switch switchReverse;
    private RadioButton buyRadioBtn;
    private RadioButton saleRadioBtn;
    private TextView headerView;
    private EditText editTextCurrency;
    private EditText editTextUAH;
    private DecimalFormat df;
    private TextView currency1;
    private Switch reverseSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();


        //Для округлення чисел
        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        CNAME = intent.getStringExtra("currency");
        BUY = intent.getFloatExtra("buy", 0);
        SALE = intent.getFloatExtra("sale", 0);
        headerView = findViewById(R.id.currency_header_text);
        buyRadioBtn = findViewById(R.id.radioButtonBuy);
        saleRadioBtn = findViewById(R.id.radioButtonSale);
        currency1 = findViewById(R.id.currency1);
        radioGroup = findViewById(R.id.radioGroup);
        switchReverse = findViewById(R.id.switch_reverse);
        reverseSwitch = findViewById(R.id.switch_reverse);
        editTextCurrency = findViewById(R.id.editTextCurrency1);
        editTextUAH = findViewById(R.id.editTextCurrency2);
        currency1.setText(CNAME);

        if (switchReverse.isChecked()) {
            if (buyRadioBtn.isChecked()) {
                headerView.setText("1" + CNAME + " = " + df.format(1 / BUY) + "UAH");
            } else if (saleRadioBtn.isChecked()) {
                headerView.setText("1" + CNAME + " = " + df.format(1 / SALE) + "UAH");
            }
            editTextCurrency.setEnabled(false);
            editTextUAH.setEnabled(true);
        } else {
            if (!switchReverse.isChecked()) {
                headerView.setText("1" + CNAME + " = " + df.format(SALE) + "UAH");
            } else {
                headerView.setText("1" + CNAME + " = " + df.format(SALE) + "UAH");
            }
            editTextCurrency.setEnabled(true);
            editTextUAH.setEnabled(false);
        }

        //Обработчик нажатий на радиокнопки buy и sale
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //headerView = findViewById(R.id.currency_header_text);
                Intent intent = getIntent();
                String CNAME = intent.getStringExtra("currency");

                //Для округлення чисел
                DecimalFormat df = new DecimalFormat("#.####");
                df.setRoundingMode(RoundingMode.CEILING);
                switch (checkedId) {
                    case R.id.radioButtonBuy:
                        if (reverseSwitch.isChecked()) {
                            headerView.setText("1" + CNAME + " = " + df.format(1 / BUY) + "UAH");
                        } else {
                            headerView.setText("1" + CNAME + " = " + df.format(BUY) + "UAH");
                        }
                        break;
                    case R.id.radioButtonSale:
                        if (reverseSwitch.isChecked()) {
                            headerView.setText("1" + CNAME + " = " + df.format(1 / SALE) + "UAH");
                        } else {
                            headerView.setText("1" + CNAME + " = " + df.format(SALE) + "UAH");
                        }
                        break;
                }
            }
        });

        switchReverse.setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView headerView = findViewById(R.id.currency_header_text);
                Intent intent = getIntent();
                String CNAME = intent.getStringExtra("currency");
                buyRadioBtn = findViewById(R.id.radioButtonBuy);
                saleRadioBtn = findViewById(R.id.radioButtonSale);
                EditText editTextCurrency = findViewById(R.id.editTextCurrency1);
                EditText editTextUAH = findViewById(R.id.editTextCurrency2);

                //Для округлення чисел
                DecimalFormat df = new DecimalFormat("#.####");
                df.setRoundingMode(RoundingMode.CEILING);

                if (switchReverse.isChecked()) {
                    if (buyRadioBtn.isChecked()) {
                        headerView.setText("1" + CNAME + " = " + df.format(1 / BUY) + "UAH");
                    } else if (saleRadioBtn.isChecked()) {
                        headerView.setText("1" + CNAME + " = " + df.format(1 / SALE) + "UAH");
                    }
                    editTextCurrency.setEnabled(false);
                    editTextUAH.setEnabled(true);
                } else {
                    if (!switchReverse.isChecked()) {
                        headerView.setText("1" + CNAME + " = " + df.format(SALE) + "UAH");
                    } else {
                        headerView.setText("1" + CNAME + " = " + df.format(SALE) + "UAH");
                    }
                    editTextCurrency.setEnabled(true);
                    editTextUAH.setEnabled(false);
                }
            }
        });

    }



    public void onClickConvert(View view) {
        RadioButton buyRadioBtn = findViewById(R.id.radioButtonBuy);
        RadioButton saleRadioBtn = findViewById(R.id.radioButtonSale);
        EditText editTextCurrency = findViewById(R.id.editTextCurrency1);
        EditText editTextUAH = findViewById(R.id.editTextCurrency2);
        Switch reverseSwitch = findViewById(R.id.switch_reverse);

        if (buyRadioBtn.isChecked()) {
            if (reverseSwitch.isChecked()) {
                if (editTextCurrency.getText().toString() != "") {
                    //Перевірка на число
                    if (editTextUAH.getText().toString().matches("^(?=[^\\.])\\d*\\.?((?=[^\\.])\\d*)$")) {
                        editTextCurrency.setText(Float.valueOf(editTextUAH.getText().toString()) / BUY + "");
                    } else return;
                }
            } else if (!reverseSwitch.isChecked()) {
                //Перевірка на число
                if (editTextCurrency.getText().toString().matches("^(?=[^\\.])\\d*\\.?((?=[^\\.])\\d*)$")) {
                    editTextUAH.setText(Float.valueOf(editTextCurrency.getText().toString()) * BUY + "");
                } else return;
            }
        } else if (saleRadioBtn.isChecked()) {
            if (reverseSwitch.isChecked()) {
                //Перевірка на число
                if (editTextUAH.getText().toString().matches("^(?=[^\\.])\\d*\\.?((?=[^\\.])\\d*)$")) {
                    editTextCurrency.setText(Float.valueOf(editTextUAH.getText().toString()) / SALE + "");
                } else return;
            } else if (!reverseSwitch.isChecked()) {
                //Перевірка на число
                if (editTextCurrency.getText().toString().matches("^(?=[^\\.])\\d*\\.?((?=[^\\.])\\d*)$")) {
                    editTextUAH.setText(Float.valueOf(editTextCurrency.getText().toString()) * SALE + "");
                } else return;
            }
        }
    }

    public void onClickShare(View view) {
        EditText editTextCurrency1 = findViewById(R.id.editTextCurrency1);
        EditText editTextCurrency2 = findViewById(R.id.editTextCurrency2);
        TextView currency_1 = findViewById(R.id.currency1);
        TextView currency_2 = findViewById(R.id.currency2);
        String string = editTextCurrency1.getText().toString() + " " + currency_1.getText() + " = " + editTextCurrency2.getText().toString() + " " + currency_2.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, string);
        String chooserTitle = "Share";
        Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
        startActivity(chooserIntent);
    }
}
