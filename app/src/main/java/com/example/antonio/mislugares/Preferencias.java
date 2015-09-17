package com.example.antonio.mislugares;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Antonio on 29/04/2015.
 */
public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
