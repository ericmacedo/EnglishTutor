package com.macedo.eric.englishtutor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe que faz reconhencimento de voz totalmente funcional. Depende de conexão com a internet e
 * faz uso da API SpeechRecognizer do Google. A interface RecognitionListener é usada como intermediador
 * da comunicação com a API.
 * @see {<a href=http://developer.android.com/intl/pt-br/reference/android/speech/SpeechRecognizer.html>SpeechRecognizer</a> }
 *
 */
public class Recognizer
        extends Activity
        implements RecognitionListener {

    private TextView text;
    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    private final String LANG = "en";
    private String word;

    /**
     * No metodo OnCreate - metodo herdado de Actitivity - todos os componentes da API são setados.
     * @param savedInstanceState Bundle - Referencia para os estados atual da aplicação
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        word = getIntent().getExtras().getString("sentence");

        text = (TextView) findViewById(R.id.word);
        text.setText(word);

        returnedText = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        this.progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);

        //Configuração do helper de reconhecimento de voz
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                this.LANG); //Seta a linguagem
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); //Modo de input do metodo de entrada
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        //Listener do toggleButton de reconhecimento de voz
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
            }
        });

    }

    /**
     * Metodo que é chamado quando o ButtonListener capta um evento quando a interface está no estado 'parado'
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Metodo que é chamado quando o ButtonListener capta um evento quando a interface está no estado 'reconhecendo'
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }

    }

    /**
     * Metodo que é chamado quando a interface começa a reconhecer input sonoro
     */
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    /**
     * Metodo que é chamado quando a interface recebe input sonoro e seu tradutor já conseguiu traduzir algo parcial
     */
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + Arrays.toString(buffer));
    }

    /**
     * Metodo chamado quando a interface passa do estado 'reconhecendo' para o estado 'parado', ou seja, terminou o reconhecimento
     */
    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    /**
     * Metodo chamado quando a interface encontra um erro no reconhecimento
     * @param errorCode int - Codigo interno de erro
     */
    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);
    }

    /**
     * Metodo da interface
     * @param arg0
     * @param arg1
     */
    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    /**
     *
     * @param arg0 Bundle - Bundle com resultados parciais do reconhecimento de voz
     */
    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    /**
     * Metodo da interface usado quando se deseja fazer algo antes de começar a reconhecer voz
     * @param arg0 Bundle
     */
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    /**
     * Recebe um bundle com os resultados do reconhecimento de voz e passa esses valores para a view
     * correta.
     * @param results Bundle - Bundle com os resultados obtidos pelo reconhecimento de voz
     */
    @Override
    public void onResults(Bundle results) {
        //Metodo que é chamado quando o reconhecimento é terminado
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); //passa os valores retornados pelo bundle para um ArrayList
        String text = matches.get(0); //Pega apenas o primeiro valor(de maior relevancia)
        text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();//primeira letra maiuscula(O reconhecedor costuma modificar os cases)

        returnedText.setText(text); //apresenta o valor retornado na view

        if(text.equals(this.word)) {
            /*
            Caso o usuário tenha acertado
             */
            Intent returnIntent = new Intent();
            returnIntent.putExtra("parentView_id", Recognizer.this.getIntent().getExtras().getInt("parentView_id"));
            returnIntent.putExtra("parentView_pos", Recognizer.this.getIntent().getExtras().getInt("parentView_pos"));

            this.returnedText.setTextColor(Color.GREEN);

            returnIntent.putExtra("equals", true);

            setResult(Activity.RESULT_OK, returnIntent);
            Toast.makeText(Recognizer.this, R.string.congratulations, Toast.LENGTH_SHORT).show();

            //Timer
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Recognizer.this.finish();
                }
            }, 2000);
        } else {
            /*
            Caso o usuário erre
             */
            this.returnedText.setTextColor(Color.RED);
        }

    }

    /**
     * Recebe como parametro um valor referente à frequencia sonora sendo recebida pela interface e
     * passa esse parametro para o log da aplicação e para o progressbar.
     * @param rmsdB float - valor em ponto flutuante referente à frequencia sonora sendo recebida pela interface
     */
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    /**
     * Traduz um codigo de erro para um output verbal
     * @param errorCode int - Codigo interno de erro
     * @return String - Retorna um correspondente verbal ao codigo de erro interno
     */
    public static String getErrorText(int errorCode) {
        /*
        metodo para fins de logs
         */
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}