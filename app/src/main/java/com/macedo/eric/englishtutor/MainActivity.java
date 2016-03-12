package com.macedo.eric.englishtutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Instanciação da atividade Activities
    public void startApplication(View view){
        Intent intent = new Intent(MainActivity.this, Activities.class);
        MainActivity.this.startActivity(intent);
    }

    //Mata a atividade quando o botão quit/sair é pressionado
    public void endApplication(View view){
        MainActivity.this.finish();
    }

    //lança uma atividade que mostra as dependencias do aplicativo
    public void launchDependenciesSettings(View view){
        Intent intent = new Intent(MainActivity.this, Dependencies.class);
        MainActivity.this.startActivity(intent);
    }
}
