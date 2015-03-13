package com.maalaang.waltz.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.maalaang.waltz.R;

/**
 * Created by User on 2015-03-05.
 */
public class SelectCountryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkboxlist);
    /*
        Intent intent = getIntent();
        intent.putExtra("countrycode", countrycode.toString());
        setResult(RESULT_OK, intent);
        finish();
    */

    }
}
