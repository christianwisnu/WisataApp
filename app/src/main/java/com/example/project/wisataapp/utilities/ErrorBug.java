package com.example.project.wisataapp.utilities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.project.wisataapp.R;

/**
 * Created by christian on 04/01/17.
 */

public class ErrorBug extends Activity {
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_bug);

        tvError     = (TextView)findViewById(R.id.TvErorr);
        tvError.setText(getIntent().getStringExtra("error"));

    }
}
