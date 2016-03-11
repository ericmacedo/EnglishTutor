package com.macedo.eric.englishtutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startApplication(View view){
        Intent intent = new Intent(MainActivity.this, Activities.class);
        MainActivity.this.startActivity(intent);
    }

    public void endApplication(View view){
        MainActivity.this.finish();
    }

    public void launchDependenciesSettings(View view){
        Intent intent = new Intent(MainActivity.this, Dependencies.class);
        MainActivity.this.startActivity(intent);
    }
}
