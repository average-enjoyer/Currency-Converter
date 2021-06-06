package com.example.currencyconverter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NonCashActivityC extends AppCompatActivity {

    private ShareActionProvider shareActionProvider;

    SharedPreferences sPref;
    final String SAVED_CASH_BUY_USD = "cash_buy_usd";
    final String SAVED_CASH_SALE_USD = "cash_sale_usd";
    final String SAVED_CASH_BUY_EUR = "cash_buy_eur";
    final String SAVED_CASH_SALE_EUR = "cash_sale_eur";
    final String SAVED_CASH_BUY_RUR = "cash_buy_rur";
    final String SAVED_CASH_SALE_RUR = "cash_sale_rur";
    final String SAVED_CASH_BUY_BTC = "cash_buy_btc";
    final String SAVED_CASH_SALE_BTC = "cash_sale_btc";
    final String CASH_SAVED_DATE_LAST_REFRESHING = "cash_date_last_refreshing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_cash_c);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (isOnline(this)) {
            String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";
            DownLoadJSONTask task = new DownLoadJSONTask();
            task.execute(url);
        } else {
            sPref = getPreferences(MODE_PRIVATE);
            Intent intent = getIntent();

            Float savedBuyCurrency = 0f;
            Float savedSaleCurrency = 0f;
                if (intent.getStringExtra("currency").equals("USD")) {
                    savedBuyCurrency = sPref.getFloat(SAVED_CASH_BUY_USD, 0);
                    savedSaleCurrency = sPref.getFloat(SAVED_CASH_SALE_USD, 0);
                } else if (intent.getStringExtra("currency").equals("EUR")) {
                    savedBuyCurrency = sPref.getFloat(SAVED_CASH_BUY_EUR, 0);
                    savedSaleCurrency = sPref.getFloat(SAVED_CASH_SALE_EUR, 0);
                } else if (intent.getStringExtra("currency").equals("RUR")) {
                    savedBuyCurrency = sPref.getFloat(SAVED_CASH_BUY_RUR, 0);
                    savedSaleCurrency = sPref.getFloat(SAVED_CASH_SALE_RUR, 0);
                } else if (intent.getStringExtra("currency").equals("BTC")) {
                    savedBuyCurrency = sPref.getFloat(SAVED_CASH_BUY_BTC, 0);
                    savedSaleCurrency = sPref.getFloat(SAVED_CASH_SALE_BTC, 0);
                }
            String dateUSDSaved = sPref.getString(CASH_SAVED_DATE_LAST_REFRESHING, "");

            TextView buyUSD = (TextView) findViewById(R.id.buy_text_view);
            TextView sellUSD = (TextView) findViewById(R.id.sale_text_view);
            TextView dataUSD = (TextView) findViewById(R.id.date_text_view);
            TextView textViewInternet = (TextView) findViewById(R.id.textViewInternet);
            buyUSD.setText(String.valueOf(savedBuyCurrency));
            sellUSD.setText(String.valueOf(savedSaleCurrency));
            dataUSD.setText(String.valueOf(dateUSDSaved));
            textViewInternet.setText("Press the refresh above me ;)");
        }
        Intent intent = getIntent();
        TextView header = (TextView) findViewById(R.id.textViewLargeHeader);
        header.setText(intent.getStringExtra("currency") + " to UAH");
        boolean isPortrait = isPortrait();
        if (intent.getStringExtra("currency") != null) {
            if (intent.getStringExtra("currency").equals("USD") && isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.usd);
            } else if (intent.getStringExtra("currency").equals("EUR") && isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.euro);
            } else if (intent.getStringExtra("currency").equals("RUR") && isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.rbl);
            } else if (intent.getStringExtra("currency").equals("BTC") && isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.btc);
            } else

            if (intent.getStringExtra("currency").equals("USD") && !isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.usdh);
            } else if (intent.getStringExtra("currency").equals("EUR") && !isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.euroh);
            } else if (intent.getStringExtra("currency").equals("RUR") && !isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.rblh);
            } else if (intent.getStringExtra("currency").equals("BTC") && !isPortrait) {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.acticity_non_cash_c_layout);
                view.setBackgroundResource(R.drawable.btch);
            }
        }
    }


    private boolean isPortrait(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return true;
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return false;
        else return true;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; єлементі действий добавляються на панель приложения
        getMenuInflater().inflate(R.menu.menu, menu); //R.menu.menu - файл ресурсов меню
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        String shareContent = "";
        sPref = getPreferences(MODE_PRIVATE);
        Float savedBuy = sPref.getFloat(SAVED_CASH_BUY_USD, 0);
        Float savedSale = sPref.getFloat(SAVED_CASH_SALE_USD, 0);
        Intent intent = getIntent();
        shareContent = intent.getStringExtra("currency") + " buy " + savedBuy + "UAH" + "\n" + intent.getStringExtra("currency") + " sale " + savedSale + "UAH";
        setShareActionIntent(shareContent);
        return super.onCreateOptionsMenu(menu);
    }

    //Метод который создает интент и передаёт его провайдеру действия передачи информации при помощи его метода setShareIntent()
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculator: {
                sPref = getPreferences(MODE_PRIVATE);
                Intent intent = getIntent();

                float buy = 0f;
                float sale = 0f;
                String cname = intent.getStringExtra("currency");

                if (intent.getStringExtra("currency").equals("USD")) {
                    buy = sPref.getFloat(SAVED_CASH_BUY_USD, 0);
                    sale = sPref.getFloat(SAVED_CASH_SALE_USD, 0);
                } else if (intent.getStringExtra("currency").equals("EUR")) {
                    buy = sPref.getFloat(SAVED_CASH_BUY_EUR, 0);
                    sale = sPref.getFloat(SAVED_CASH_SALE_EUR, 0);
                } else if (intent.getStringExtra("currency").equals("RUR")) {
                    buy = sPref.getFloat(SAVED_CASH_BUY_RUR, 0);
                    sale = sPref.getFloat(SAVED_CASH_SALE_RUR, 0);
                } else if (intent.getStringExtra("currency").equals("BTC")) {
                    buy = sPref.getFloat(SAVED_CASH_BUY_BTC, 0);
                    sale = sPref.getFloat(SAVED_CASH_SALE_BTC, 0);
                }

                //Шлём данные о валюте калькулятору:
                Intent intentCalc = new Intent(this, CalculatorActivity.class);
                intentCalc.putExtra("currency", cname);
                intentCalc.putExtra("buy", buy);
                intentCalc.putExtra("sale", sale);
                startActivity(intentCalc);
                return true;
            }
            case R.id.action_refresh: {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class DownLoadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                //return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("LOGoutside", s);
            parseToDB(s);
        }

        public void parseToDB(String json) {
            Log.i("LOGInside", json);
            try {
                JSONArray jsonArray = new JSONArray(json);

                JSONObject jsonObjectUSD = jsonArray.getJSONObject(0);
                double buyUSD = jsonObjectUSD.getDouble("buy");
                double saleUSD = jsonObjectUSD.getDouble("sale");

                JSONObject jsonObjectEUR = jsonArray.getJSONObject(1);
                double buyEUR = jsonObjectEUR.getDouble("buy");
                double saleEUR = jsonObjectEUR.getDouble("sale");

                JSONObject jsonObjectRUR = jsonArray.getJSONObject(2);
                double buyRUR = jsonObjectRUR.getDouble("buy");
                double saleRUR = jsonObjectRUR.getDouble("sale");

                JSONObject jsonObjectBTC = jsonArray.getJSONObject(3);
                double buyBTC = jsonObjectBTC.getDouble("buy");
                double saleBTC = jsonObjectBTC.getDouble("sale");

                TextView textViewBuy = (TextView) findViewById(R.id.buy_text_view);
                TextView textViewSell = (TextView) findViewById(R.id.sale_text_view);

                Intent intent = getIntent();
                if (intent.getStringExtra("currency") != null) {
                    if (intent.getStringExtra("currency").equals("USD")) {
                        textViewBuy.setText(String.valueOf(buyUSD));
                        textViewSell.setText(String.valueOf(saleUSD));
                    } else if (intent.getStringExtra("currency").equals("EUR")) {
                        textViewBuy.setText(String.valueOf(buyEUR));
                        textViewSell.setText(String.valueOf(saleEUR));
                    } else if (intent.getStringExtra("currency").equals("RUR")) {
                        textViewBuy.setText(String.valueOf(buyRUR));
                        textViewSell.setText(String.valueOf(saleRUR));
                    } else if (intent.getStringExtra("currency").equals("BTC")) {
                        textViewBuy.setText(String.valueOf(buyBTC));
                        textViewSell.setText(String.valueOf(saleBTC));
                    }
                }

                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putFloat(SAVED_CASH_BUY_USD, (float) buyUSD);
                ed.putFloat(SAVED_CASH_SALE_USD, (float) saleUSD);
                ed.putFloat(SAVED_CASH_BUY_EUR, (float) buyEUR);
                ed.putFloat(SAVED_CASH_SALE_EUR, (float) saleEUR);
                ed.putFloat(SAVED_CASH_BUY_RUR, (float) buyRUR);
                ed.putFloat(SAVED_CASH_SALE_RUR, (float) saleRUR);
                ed.putFloat(SAVED_CASH_BUY_BTC, (float) buyBTC);
                ed.putFloat(SAVED_CASH_SALE_BTC, (float) saleBTC);
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                ed.putString(CASH_SAVED_DATE_LAST_REFRESHING, String.valueOf(formatForDateNow.format(new Date())));
                ed.apply();

                String savedDate = sPref.getString(CASH_SAVED_DATE_LAST_REFRESHING, "Press the refresh above me ;)");
                TextView testText = (TextView) findViewById(R.id.date_text_view);
                testText.setText("On " + savedDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
    }
}
