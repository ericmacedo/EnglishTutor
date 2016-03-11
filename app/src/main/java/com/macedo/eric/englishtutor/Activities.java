package com.macedo.eric.englishtutor;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        setTitle("Speech Lessons");

        String[] activities = getResources().getStringArray(R.array.frases);

        ArrayAdapter<String> mActivities =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities);

        ListView listView = (ListView) findViewById(R.id.activities_list);
        listView.setAdapter(mActivities);

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
