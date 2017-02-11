package com.zappos.namanrajpal.ilovezappos.Utils;

import com.zappos.namanrajpal.ilovezappos.Model.Product;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naman Rajpal on 2/2/2017.
 */

public class ReturnedProducts implements Serializable {

    public List<Product> getResults() {
        return results;
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }

    List<Product> results;


}
