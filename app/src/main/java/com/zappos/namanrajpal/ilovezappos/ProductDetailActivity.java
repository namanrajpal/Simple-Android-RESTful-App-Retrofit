package com.zappos.namanrajpal.ilovezappos;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.zappos.namanrajpal.ilovezappos.Model.ProductDetails;
import com.zappos.namanrajpal.ilovezappos.databinding.ProductDetailBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Naman Rajpal on 2/10/2017.
 */

public class ProductDetailActivity extends AppCompatActivity {
    public static final String PRODUCT_POSITION = "PRODUCT_POSITION";

    private ProductDetails productDetails = new ProductDetails();
    private int mProductPosition;

    @BindView(R.id.fab2)
    protected FloatingActionButton fab;
    Animation fab_rotate;
    Animation fab_rotateback;
    AnimationSet fab_animations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();

        ProductDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.product_detail);
        ButterKnife.bind(this);
        mProductPosition = getIntent().getIntExtra(PRODUCT_POSITION, 0);
        productDetails.setProduct(((DataBindingApplication) getApplication()).getProducts().get(mProductPosition));
        productDetails.setIndex(mProductPosition);
        binding.setProductDetails(productDetails);
        fab_rotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        fab_rotateback = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_back);
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


    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void onShowNextBook(View view) {
        productDetails.setIndex(++mProductPosition);
        productDetails.setProduct(((DataBindingApplication) getApplication()).getProducts().get(productDetails.getIndex()));
    }

    public void onShowPreviousBook(View view) {
        productDetails.setIndex(--mProductPosition);
        productDetails.setProduct(((DataBindingApplication) getApplication()).getProducts().get(productDetails.getIndex()));
    }

    public void addToCart(View view) {
        Snackbar.make(fab, productDetails.getProduct().getProductName() + " is added to cart", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        ((DataBindingApplication) getApplication()).addToCart(mProductPosition);
        fab.startAnimation(fab_animations);
    }
}


