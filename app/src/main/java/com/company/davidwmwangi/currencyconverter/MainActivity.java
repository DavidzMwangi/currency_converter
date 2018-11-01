package com.company.davidwmwangi.currencyconverter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.widget.SwipeRefreshLayout;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.activeandroid.query.Delete;
import com.company.davidwmwangi.currencyconverter.adapter.MainActivityAdapter;
import com.company.davidwmwangi.currencyconverter.interfaces.OnSingleCurrencyInterface;


public class MainActivity extends AppCompatActivity implements OnSingleCurrencyInterface{
    RecyclerView recyclerView;
    MainActivityAdapter adapter;
    SwipeRefreshLayout swipe_refresh;
    public static String selected_currency_identifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipe_refresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView=(RecyclerView)findViewById(R.id.recycler);
                adapter=new MainActivityAdapter(MainActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                if ((InternetChecker.netChecker(MainActivity.this))) {
                    swipe_refresh.setRefreshing(false);

                }
            }
        });


        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new MainActivityAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new MainActivityAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
        @Override
    public void openSingleCardActivity(long card_id,String selected_id){
            Intent i=new Intent(MainActivity.this, SingleCurrencyActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(selected_currency_identifier,selected_id);

            startActivity(i);
        }



}
