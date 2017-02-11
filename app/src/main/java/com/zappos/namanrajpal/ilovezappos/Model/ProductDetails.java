package com.zappos.namanrajpal.ilovezappos.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.zappos.namanrajpal.ilovezappos.BR;

import butterknife.BindView;

/**
 * Created by Naman Rajpal on 2/10/2017.
 */

public class ProductDetails extends BaseObservable {

private Product product;
    private int index;

    @Bindable
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyPropertyChanged(BR.index);
    }

    @Bindable
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        notifyPropertyChanged(BR.product);
    }
}
