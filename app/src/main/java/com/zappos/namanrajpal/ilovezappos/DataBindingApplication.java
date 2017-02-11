package com.zappos.namanrajpal.ilovezappos;

import android.app.Application;

import com.zappos.namanrajpal.ilovezappos.Model.Product;

import java.util.ArrayList;
import java.util.List;


public class DataBindingApplication extends Application {
    //model
    private List<Product> products;
    private List<Product> cart;

    public List<Product> getCart() {
        return cart;
    }

    public int getCartSize()
    {
        if(cart!=null)return cart.size();
        else return 0;
    }

    public float getTotal()
    {
        if(cart==null) return 0;
        float sum = 0;
        for(Product p : cart)
        {
            sum += Float.parseFloat(p.getPrice().substring(1,p.getPrice().length()));
        }
        return sum;
    }

    public void addToCart(int index)
    {
        if(index>products.size())
            return;
        if(cart!=null)
        {
            cart.add(products.get(index));
        }else
        {
            cart = new ArrayList<Product>();
            cart.add(products.get(index));
        }
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> mResultsist) {
        this.products = mResultsist;
    }
}