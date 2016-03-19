package com.macedo.eric.englishtutor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Classe que extends AppCompatActivity.
 * Lista uma série de frases definidas nos recursos da aplicação.
 * {@link com.macedo.eric.englishtutor.R.string}
 */
public class Activities extends AppCompatActivity {

    /**
     * Seta o Layout com uma ListView populada com valores definidos nos recursos da aplicação
     * @param savedInstanceState Bundle - Referencia à atividade atual
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        setTitle(R.string.speech_lessons);

        //Pega valores padrões setados nos resources
        String[] activities = getResources().getStringArray(R.array.frases);

        ArrayAdapter<String> mActivities =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);

        //passa os valores do adapter para a view
        ListView listView = (ListView) findViewById(R.id.activities_list);
        listView.setAdapter(mActivities);

        /**
         Listener do list view. Cria um intent que vai chamar uma atividade e esperar por um intent
         que será retornado e pode ser acessado pelo metodo onActivityResult

         O metodo putExtra insere dados no intent
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Activities.this, Recognizer.class);
                intent.putExtra("sentence", adapterView.getAdapter().getItem(i).toString());
                intent.putExtra("parentView_id", i);
                Activities.this.startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * Metodo que recebe o intent resultante do startActivityForResult.
     * @param requestCode int - Request code usado como indice de todas as atividades que implementam startActivityForResult
     * @param resultCode int - codigo de resultado do startActivityForResult
     * @param data Intent - Referencia do estado da atividade 'filha' da atividade que a instanciou
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ListView listView = (ListView) findViewById(R.id.activities_list);
                TextView textView = (TextView) listView.getChildAt(data.getExtras().getInt("parentView_id"));
                textView.setTextColor(Color.GREEN);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //do nothing
            }
        }
    }

}
