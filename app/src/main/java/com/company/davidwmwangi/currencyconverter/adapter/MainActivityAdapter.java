package com.company.davidwmwangi.currencyconverter.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

import com.company.davidwmwangi.currencyconverter.InternetChecker;
import com.company.davidwmwangi.currencyconverter.MainActivity;
import com.company.davidwmwangi.currencyconverter.R;
import com.company.davidwmwangi.currencyconverter.interfaces.OnSingleCurrencyInterface;
import com.company.davidwmwangi.currencyconverter.utils.InternetController;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by David W. Mwangi on 31/10/2017.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    public static String selecetedCurrency;
    private ProgressDialog pd;

    //to help display  progressdialog
    private Context mContext;
    public MainActivityAdapter(Context context){
        this.mContext=context;
    }
    public MainActivityAdapter(){

    }
    public static String[] majorCurrencies = new String[]{
            "USD", //us dollar
            "EUR", //euro
            "JPY", //japanese yen
            "GBP", //british pound
            "AUD", //australian dollar
            "CHF",  //swiss franc
            "CAD",  //canadian dollar
            "MXN",  //mexican peso
            "CNY",  //chinese yuan
            "NZD",  //new Zealand dollar
            "TWD",  //taiwan dollar
            "RUB",  //russian ruble
            "ZAR",  //south african rand
            "ILS",  //israel new shekel
            "MYR",  //malaysian ringgit
            "SEK",     //swedish krona
            "TRY",      //turkish lira
            "BRL",  //brazilian real
            "SGD",      //singapore dollar
            "NGN",      //nigerian naira
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        //create a new View
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_currency, parent, false);

        //wrap it in a view Holder
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Context context = viewHolder.titleView.getContext();
        viewHolder.titleView.setText("Country: "+countryNames[i]);
        viewHolder.textView5.setText("Currency Name: "+currencyName[i]);
        viewHolder.textView4.setText(" Currency Code: "+majorCurrencies[i] );
        //get the data from the api on each major currency

        InternetController net = new InternetController();
        net.get("data/pricemulti?fsyms=BTC,ETH&tsyms=" + majorCurrencies[i], null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started

                if (!(InternetChecker.netChecker(mContext))){
                    final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(mContext);
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
        showProgressDialog();
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dismissProgressDialog();
                try {


                    JSONObject jresponse = new JSONObject(String.valueOf(response));
                    //BTC currency
                    JSONObject btcObject = jresponse.getJSONObject("BTC");
                    String btc = btcObject.getString(majorCurrencies[i]);
                    viewHolder.textView2.setText("1 BTC=" + btc + " " + majorCurrencies[i]);
                    //ETH currency
                    JSONObject ethObject = jresponse.getJSONObject("ETH");
                    String eth = ethObject.getString(majorCurrencies[i]);
                    viewHolder.textView3.setText("1 ETH =" + eth + " " + majorCurrencies[i]);


                } catch (Exception e) {
                    e.printStackTrace();
                }


                //try parsing as an error
                Log.e("object", response.toString());
//                Toast.makeText(context, "Response object successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                dismissProgressDialog();
                Log.e("array", response.toString());

                viewHolder.textView2.setText(response.toString());
//                Toast.makeText(context, "Response array successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                dismissProgressDialog();
                Log.e("string", responseString);
//                Toast.makeText(context,"Response string Succesful",Toast.LENGTH_LONG).show();
                viewHolder.textView2.setText(responseString);
                //errors that are not system oriented
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dismissProgressDialog();
                Log.e("resp", throwable.toString());

                Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show();


            }


        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement opening of selected currency
                selecetedCurrency = majorCurrencies[i]; //to ensure there only exist one value all time by making it static
                ((OnSingleCurrencyInterface) context).openSingleCardActivity(i, selecetedCurrency);


            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Currency Code:" + majorCurrencies[i], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return majorCurrencies.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleView, textView2, textView3,textView4,textView5;
        ImageView imageView;

        public ViewHolder(CardView card) {
            super(card);
            cardView = card;
            titleView = (TextView) card.findViewById(R.id.text1);
            textView2 = (TextView) card.findViewById(R.id.text2);
            textView3 = (TextView) card.findViewById(R.id.text3);
            textView4 = (TextView) card.findViewById(R.id.text12);
            textView5 = (TextView) card.findViewById(R.id.text02);
        }

    }

    public static String[] countryNames = new String[]{
            "United States", //us dollar
            "Europe", //euro
            "Japan", //japanese yen
            "Britain", //british pound
            "Australia", //australian dollar
            "Switzerland",  //swiss franc
            "Canada",  //canadian dollar
            "Mexico",  //mexican peso
            "China",  //chinese yuan
            "New Zealand",  //new Zealand dollar
            "Taiwan",  //taiwan dollar
            "Russia",  //russian ruble
            "South Africa",  //south african rand
            "Israel",  //israel new shekel
            "Malaysia",  //malaysian ringgit
            "Sweden",     //swedish krona
            "Turkey",      //turkish lira
            "Brazil",  //brazilian real
            "Singapore",      //singapore dollar
            "Nigeria",      //nigerian naira
    };
    public static String[] currencyName = new String[]{
            "US Dollar", //us dollar
            "Euro", //euro
            "Japanese Yes", //japanese yen
            "British Pound", //british pound
            "Australlian Dollar", //australian dollar
            "Swiss Franc",  //swiss franc
            "Canadian Dollar",  //canadian dollar
            "Mexican Peso",  //mexican peso
            "Chinese Yuan",  //chinese yuan
            "New Zealand Dollar",  //new Zealand dollar
            "Taiwan Dollar",  //taiwan dollar
            "Russian Ruble",  //russian ruble
            "South African Rand",  //south african rand
            "Israel New Shekel",  //israel new shekel
            "Malaysian Ringgit",  //malaysian ringgit
            "Swedish Krona",     //swedish krona
            "Turkish Lira",      //turkish lira
            "Brazilian Real",  //brazilian real
            "Singapore Dollar",      //singapore dollar
            "Nigerian Naira",      //nigerian naira
    };
    public void showProgressDialog(){
        if (pd==null){
            pd=new ProgressDialog(mContext);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setMessage("Fetching Current Currency Rates Online...");
            pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pd.dismiss();
                    System.exit(1);
                }
            });
            pd.show();
        }
    }
    public void dismissProgressDialog(){
        if (pd!=null){
            pd.dismiss();
        }
    }
}