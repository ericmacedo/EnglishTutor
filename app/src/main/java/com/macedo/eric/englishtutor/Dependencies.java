package com.macedo.eric.englishtutor;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Dependencies extends ListActivity {

    public static boolean recognition;

    public static boolean connectivity;

    private CustomAdapter adaptador = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        recognition = SpeechRecognizer.isRecognitionAvailable(this);
        connectivity = hasInternetConnection();

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dependencies); desnecessário dado que o setAdapter já instancia uma view

        displayListView();

    }

    //Listener do valores da ListView
    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        Intent intent;

        switch (position){
            case 0: // Se escolher "Reconhecedor de Voz"
                intent = new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS); //Lança uma chamada para a uma action de input settings
                startActivity(intent);
                break;
            case 1: // Se escolher "Checagem de componentes"
                intent = new Intent(Settings.ACTION_WIFI_SETTINGS); //lança uma chamada para uma action de wifi settings
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

        }

    }


    //Checa se há conexão com a internet
    public boolean hasInternetConnection() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public void displayListView(){
        //Cria um array list para passar os parametros para o adapter da list view
        ArrayList<Component> mComponents = new ArrayList<>();

        String recognitiontxt;
        if(recognition)
             recognitiontxt = getResources().getString(R.string.available);
        else
            recognitiontxt = getResources().getString(R.string.unavailable);

        String internettxt;
        if(connectivity)
            internettxt = getResources().getString(R.string.available);
        else
            internettxt = getResources().getString(R.string.unavailable);

        String[] componentsList = getResources().getStringArray(R.array.dependencias);

        mComponents.add(new Component(componentsList[0], recognitiontxt));
        mComponents.add(new Component(componentsList[1], internettxt));


        adaptador = new CustomAdapter(Dependencies.this, R.layout.custom_view, mComponents);

        //passa os valores do array list para o adapter para este instanciar os layouts populados com os valores
        this.setListAdapter(adaptador);
    }

    //Classe auxiliar do CustomAdapter
    public class Component{

        private String name;
        private String state;

        public Component(String name, String state){
            this.name = name;
            this.state = state;
        }

        public String getName(){
            return this.name;
        }

        public String getState(){
            return this.state;
        }
    }

    //Modificação do ArrayAdapter para receber customViews
    private class CustomAdapter extends ArrayAdapter<Component> {

        public ArrayList<Component> componentList;

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<Component> componentList) {

            super(context, textViewResourceId, componentList);
            this.componentList = new ArrayList<>();
            this.componentList.addAll(componentList);
        }

        public class ViewHolder {

            public TextView name;
            public TextView state;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.custom_view, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.component_name);
                holder.state = (TextView) convertView.findViewById(R.id.component_state);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Component component = componentList.get(position);
            holder.name.setText(component.getName());
            holder.state.setText(component.getState());

            if( component.getState().equals(getResources().getString(R.string.available)) )
                holder.state.setTextColor(Color.GREEN);
            else if ( component.getState().equals(getResources().getString(R.string.unavailable)) )
                holder.state.setTextColor(Color.RED);

            return convertView;
        }
    }
}
