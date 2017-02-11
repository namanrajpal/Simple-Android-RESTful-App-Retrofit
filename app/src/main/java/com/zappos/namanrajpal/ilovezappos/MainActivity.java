package com.zappos.namanrajpal.ilovezappos;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;


import com.zappos.namanrajpal.ilovezappos.Utils.ReturnedProducts;
import com.zappos.namanrajpal.ilovezappos.Utils.ZapposAPI;
import com.zappos.namanrajpal.ilovezappos.databinding.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
Author: Naman Rajpal
www.rajpalnaman.net
* */
public class MainActivity extends AppCompatActivity implements ProductsRecyclerAdapter.OnItemClickListener,ProductsRecyclerAdapter.OnItemLongClickListener{

    //saved results to be used for handling configs
    ReturnedProducts savedResults;

    @BindView(R.id.searchView)
    protected SearchView searchBar;

    @BindView(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    @BindView(R.id.search_results_rview)
    protected RecyclerView mSearchResultsRecyclerView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    private ZapposAPI zapposAPI;

    private ProductsRecyclerAdapter pAdapter;

    //Animation to be used for FAB
    Animation fab_rotate;
    AnimationSet fab_animations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentMainBinding Abinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab_rotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);

        fab_animations = new AnimationSet(false);//false means don't share interpolators
        fab_animations.addAnimation(fab_rotate);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numofItems = ((DataBindingApplication) getApplication()).getCartSize();
                float total = ((DataBindingApplication) getApplication()).getTotal();
                fab.startAnimation(fab_animations);
                Snackbar.make(view, "Yor cart has "+numofItems+" items. Your total: "+String.format("%.2f",total), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);

        //Handling orientation changes for Layout
        int col = 2;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            col =3;
        }


        mSearchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this,col));


        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setIconified(false);
            }
        });
        searchBar.setQueryHint("What are you looking for?");
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                //Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(query)) {
                    searchBar.setQueryHint(getString(R.string.required));
                } else {
                    searchBar.clearFocus();
                    searchForProducts(query.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (savedInstanceState != null) {

            savedResults = (ReturnedProducts) savedInstanceState.getSerializable("MyProducts");
            if(savedResults!=null)
            displayResults(savedResults);
            hideKeyboard();
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

            View view = this.getCurrentFocus();

            if (view == null) {
                view = new View(this);
            }
            inputMethodManager.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            //searchBar.clearFocus();

        }
        //Initializing API and RETROFIT
        init();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        // Saving state now
        outState.putSerializable("MyProducts", savedResults);

    }

    private void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.zappos.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
       zapposAPI = retrofit.create(ZapposAPI.class);
    }


    private void searchForProducts(String query) {
        hideKeyboard();
        displayProgress(true);
        zapposAPI.loadProducts(query).enqueue(new Callback<ReturnedProducts>() {
            @Override
            public void onResponse(Call<ReturnedProducts> call, Response<ReturnedProducts> response) {
                displayResults(response.body());
            }

            @Override
            public void onFailure(Call<ReturnedProducts> call, Throwable t) {
                displayError(t);
            }
        });
    }


    private void displayResults(ReturnedProducts searchResults) {
        Log.d("Total search results: ", searchResults.getResults().get(0).getProductName());


        displayProgress(false);

        if (pAdapter == null) {
            //Creaating an Adapter
            //Adapter containes Listner interfaces
            pAdapter = new ProductsRecyclerAdapter(searchResults.getResults(),this);
            mSearchResultsRecyclerView.setAdapter(pAdapter);
            savedResults = searchResults;
            ((DataBindingApplication) getApplication()).setProducts(searchResults.getResults());
        } else {
            ((DataBindingApplication) getApplication()).getProducts().clear();
            ((DataBindingApplication) getApplication()).getProducts().addAll(searchResults.getResults());
            pAdapter.notifyDataSetChanged();
        }

    }

    private void displayError(Throwable t) {

        Log.e("Search", t.getMessage());

        displayProgress(false);

        if (((DataBindingApplication) getApplication()).getProducts() != null) {
            ((DataBindingApplication) getApplication()).getProducts().clear();
            if (pAdapter != null) {
                pAdapter.notifyDataSetChanged();
            }
        }
        Toast.makeText(this, "Error in Connection", Toast.LENGTH_LONG).show();


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
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_load) {
            //setProgressBarIndeterminateVisibility(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private void displayProgress(boolean show) {
        if (show) {
            mSearchResultsRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void hideKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = this.getCurrentFocus();

        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onItemClicked(int position) {
        //Toast.makeText(getBaseContext(), "touch" + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODUCT_POSITION, position);
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClicked(int position) {
        //Toast.makeText(getBaseContext(), savedResults.results.get(position).getProductName() + " is added to cart", Toast.LENGTH_SHORT).show();
        Snackbar.make(fab, savedResults.getResults().get(position).getProductName() + " is added to cart", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        ((DataBindingApplication) getApplication()).addToCart(position);
        fab.startAnimation(fab_animations);
        return false;
    }

    public void animateFAB()
    {

    }
}
