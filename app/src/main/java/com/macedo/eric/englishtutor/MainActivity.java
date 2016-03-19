package com.macedo.eric.englishtutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Atividade principal da aplicação, consiste de um botão de depencencias, um botão que inicia a aplicação,
 * e um botão que fecha a aplicação.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Metodo chamado a partir do layout com o metodo OnClick. Inicia a aplicação.
     * @param view View - Referencia da view que está acessando o metodo
     */
    public void startApplication(View view){
        Intent intent = new Intent(MainActivity.this, Activities.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * Metodo chamado a partir do layout com o metodo OnClick. Fecha a aplicação.
     * @param view View - Referencia da view que está acessando o metodo
     */
    public void endApplication(View view){
        MainActivity.this.finish();
    }

    /**
     * Metodo chamado a partir do layout com o metodo OnClick. Lança a atividade Dependencies. {@link Dependencies}
     * @param view View - Referencia da view que está acessando o metodo
     */
    public void launchDependenciesSettings(View view){
        Intent intent = new Intent(MainActivity.this, Dependencies.class);
        MainActivity.this.startActivity(intent);
    }
}
