package com.company.davidwmwangi.currencyconverter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.company.davidwmwangi.currencyconverter.adapter.MainActivityAdapter;
import com.company.davidwmwangi.currencyconverter.utils.InternetController;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SingleCurrencyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static  int currency_name_index;
    public static   int country_name_index;
    public static String selected_spinner_text;
   private EditText currency_value;
    private TextView display_rate,display_conversionValue,currencyValuePlaceholder;
    private Button convertButton;
    String conversionRate;
    double converting_currency_value;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_currency);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //spinner for the crypto-currency
        Spinner spinner=(Spinner)findViewById(R.id.crypto_currency_to_convert);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.crypto_currency_array,android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //apply the adapter to the spinner
        spinner.setAdapter(adapter);




        //textview that acts as placeholder for the currency value being entered by user
        currencyValuePlaceholder=(TextView)findViewById(R.id.currency_amount_placeholder);
        currencyValuePlaceholder.setText("Enter the amount of "+getCurrencyCode());



        //set action on clicking the button
        convertButton=(Button)findViewById(R.id.compute_button);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    double convert_rate_double=Double.parseDouble(conversionRate);
                    double converted_answer= doConversion(convert_rate_double);
                    String a=String.valueOf(converted_answer);
                    //display the answer
                   display_conversionValue=(TextView)findViewById(R.id.conversion_result);

                    //convert the currency value to a string for it to be displayed
                    String converting_currency_value_display=String.valueOf(converting_currency_value);
                    display_conversionValue.setText( converting_currency_value_display + " "+getCurrencyCode()+ "=" + a+" "+selected_spinner_text);

                }catch (Exception e){

                }

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView countryName=(TextView)findViewById(R.id.currency_country_name);
        countryName.setText( "Country: "+getCountryName());

        TextView currencyName=(TextView)findViewById(R.id.currency_currency_name);
        currencyName.setText("Currency Name: "+ getCurrencyName());


        TextView currencyCode=(TextView)findViewById(R.id.currency_currency_code);
        currencyCode.setText("Currency Code: "+ getCurrencyCode());


    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);


    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first



    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first


    }
    public String getCurrencyCode(){
        Intent i=getIntent();
        return  i.getStringExtra(MainActivity.selected_currency_identifier);
    }

    public String getCountryName(){

        int bab=MainActivityAdapter.majorCurrencies.length;
        int i=0;
        while(i<bab){
            if (getCurrencyCode().equals(MainActivityAdapter.majorCurrencies[i])){
                 country_name_index=i;

            }
            i++;
        }
        return MainActivityAdapter.countryNames[country_name_index];
    }
    public String getCurrencyName(){

        int bab=MainActivityAdapter.majorCurrencies.length;
        int a=0;
        while(a<bab){
            if (getCurrencyCode().equals(MainActivityAdapter.majorCurrencies[a])){

                currency_name_index=a;
            }
//            Log.e("cant", currency_code );
            a++;
        }
        return MainActivityAdapter.currencyName[currency_name_index];
    }

    //onItemSelectedListener methods override
    public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        selected_spinner_text=parent.getItemAtPosition(pos).toString();
        //selected currency to convert


        //post the cryptocurrency to get the details of the current conversion rates
        InternetController internetController=new InternetController();
        internetController.get("data/pricemulti?fsyms=BTC,ETH&tsyms="+getCurrencyCode(),null,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                // called before request is started
                if (!(InternetChecker.netChecker(SingleCurrencyActivity.this))){
                    final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(SingleCurrencyActivity.this);
                    dlgAlert.setMessage("No Internet Connection. Check Your Internet Connection");
                    dlgAlert.setTitle("Internet Connection");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog

                            System.exit(1);

                        }
                    });
                    dlgAlert.setCancelable(false);
                    dlgAlert.create().show();
                }
                //show progress dialog
                showProgressDialog();
//                Toast.makeText(SingleCurrencyActivity.this, "Started Posting", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
            super.onSuccess(statusCode,headers,response);
                    dismissProgressDialog();
//                Toast.makeText(SingleCurrencyActivity.this, "object", Toast.LENGTH_SHORT).show();
                Log.e("Object",response.toString());
                try{


                    JSONObject jresponse = new JSONObject(String.valueOf(response));

                    JSONObject btcObject=jresponse.getJSONObject(selected_spinner_text);
                    conversionRate =btcObject.getString(getCurrencyCode());
                    display_rate=(TextView)findViewById(R.id.conversion_rate_textview);
                    display_rate.setText("1"+selected_spinner_text +" =" +conversionRate +getCurrencyCode());

//                    Toast.makeText(SingleCurrencyActivity.this, currency_code, Toast.LENGTH_SHORT).show();



                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
            super.onSuccess(statusCode,headers,response);
                dismissProgressDialog();
//                Toast.makeText(SingleCurrencyActivity.this, "Array", Toast.LENGTH_SHORT).show();
                Log.e("array", response.toString() );
            }

            @Override
            public void onSuccess(int statusCode,Header[] headers,String responseString){
                super.onSuccess(statusCode,headers,responseString);
                dismissProgressDialog();
//                Toast.makeText(SingleCurrencyActivity.this, "String", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode,Header[] headers,String responseString, Throwable throwable){
                super.onFailure(statusCode,headers,responseString,throwable);
                dismissProgressDialog();
                Toast.makeText(SingleCurrencyActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                Log.e("String",responseString);
            }

        });

    }
    public void onNothingSelected(AdapterView<?> parent){

    }
    public Double doConversion(double convert_rate){
        //get the value entered by the user
        currency_value=(EditText)findViewById(R.id.value_to_convert);
        String converting_value_string=currency_value.getText().toString();



        //ensure the user has entered a value first
        if (converting_value_string.isEmpty()){
            Toast.makeText(this, "Enter a currency value to convert", Toast.LENGTH_SHORT).show();
            return null;
        }

        //converting the string to double
        try{

            converting_currency_value=Double.parseDouble(converting_value_string);

        }catch (NumberFormatException e){
            Toast.makeText(SingleCurrencyActivity.this, "Error has Occurred", Toast.LENGTH_SHORT).show();
        }
        return (converting_currency_value/convert_rate);
    }
    public void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(SingleCurrencyActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Fetching Required Data Online...");
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.dismiss();
                    finish();
                }
            });
            progressDialog.show();
        }
    }
    public void dismissProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
