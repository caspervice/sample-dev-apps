package com.jumpout.cass.jumpout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jumpout.cass.jumpout.clientendpoint.EndpointsAsyncTask;
import com.jumpout.cass.jumpout.spenny.SpennyCoinsBeta;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeechSynthesis;
    protected static final int REQUEST_OK = 1; //used for successful request on TTS...
    private static final int REQUEST_UNSUCCESSFUL = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView spennyCoinsLogo = (ImageView) findViewById(R.id.spennycoinsImageView);

        final FadeAnimationInteractiveHandler fadeAnimationInteractiveHandler =
                new FadeAnimationInteractiveHandler(spennyCoinsLogo);
        fadeAnimationInteractiveHandler.fadeIn(spennyCoinsLogo);

        this.setupSpeechSythesis();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        final int itemId = item.getItemId();

        switch (itemId) {

            case R.id.endpoint_trigger:
                this.triggerEndpointRequest();
                break;

            case R.id.request_spenny_coins:
                this.requestBetaSpennyCoins();

            case R.id.action_settings:
                break;

            case R.id.voice_search:
                this.invokeTextToSpeechRequest();

//                this.getSampleLink();

            default:
                return super.onOptionsItemSelected(item);
//            case R.id.
        }

        return true;

    }

    private void setupSpeechSythesis() {

        textToSpeechSynthesis = new TextToSpeech(getApplicationContext(),
                                                        new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    MainActivity.this.textToSpeechSynthesis.setLanguage(Locale.UK);
                }
            }
        });
    }

    private boolean firstCheck;

    /**
     * Invoke TTS mic functionality.
     */
    private void invokeTextToSpeechRequest() {

        //TODO -->> only make it talk the unchecked messaages quote once (counter on instance / runtime)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (!firstCheck) {

                this.textToSpeechSynthesis.speak("Hi Casper Vice, how can I help today? You have 3 unchecked updates.",
                        TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, null);
                this.firstCheck = true;66
            }
            else {

                this.textToSpeechSynthesis.speak("What would you like to find?",
                        TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, null);
            }

        }

        //invoke mic functionality
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //specifies the language type
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try{
            //linked to request code at top of class

            startActivityForResult(i, REQUEST_OK);

        }catch(Exception e){

            //if error occurs when speech button pressed
            Toast.makeText(getApplicationContext(), "Error initializing speech to text engine. Error being: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            startActivityForResult(i, REQUEST_UNSUCCESSFUL);
        }

    }

    private void requestBetaSpennyCoins() {

        final SpennyCoinsBeta spennyCoinsBeta = new SpennyCoinsBeta(this);
        spennyCoinsBeta.requestSpennyCoinsAsync();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.textToSpeechSynthesis.speak("Sorry. Spenny Coins currently unavailable.",
                    TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, null);
        }
        else {

            //TODO -->> do something ot handle an attempt on TTS from an order API phone...

        }

    }

    /**
     * Sample launch of a request to the Cloud End Point Backend Module...
     */
    private void triggerEndpointRequest() {

        final String USER_TEXT1 = "Cassvice";
        //TODO -->> rather than hardcoding the value, provide input dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter EndPoint Server Name");

        // Set up the input
        final EditText userName = new EditText(this);

        //set the max lenth for the initials
        int maxLength = 3;

        userName.setHint(USER_TEXT1);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

        userName.setInputType(InputType.TYPE_CLASS_TEXT);


        /**
         * linear layout to allow for multiple view additions (Seekbar, ImageView...)
         */
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(userName);

        /**
         * Set seekbar & current shade...
         */
        //builder.setView(seekShadeComplexity);
        //builder.setView(image);
        builder.setView(linearLayout);

        builder.setPositiveButton("Push Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String desiredName = userName.getText().toString();
                MainActivity.this.pushEndPointRequestAsync(desiredName);

            }
        });
        builder.show();

        //provide instance of Activity (to provide callback access to Async task..)

    }

    /**
     *
     * @param desiredName
     */
    private void pushEndPointRequestAsync(final String desiredName) {

        new EndpointsAsyncTask(this).execute(new Pair<Context, String>(this, desiredName));
    }

    /**
     * Provide Ui notification of completed task...
     *
     * @param result
     */
    public void notifyEndPointRequestResult(final String result) {
        Toast.makeText(getApplicationContext(), "Endpoint req successful: " + result, Toast.LENGTH_SHORT).show();
    }

    public void notifyBetaSpennyCoinsResult(final String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    /**
     * CORE-FEATURE >
     * Result of TTS. Speech generated into String, then used as search params...
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        //take passed param & pass to superclass
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_OK && resultCode == RESULT_OK){

            /** TODO -->> expand this, to search through obtained string of text. **/
            ArrayList<String> thingsYouSaid =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //assign what user said to the textview (uses concatenation with textView)
            //((TextView)findViewById(R.id.tts_text)).setText(thingsYouSaid.get(0));

            /**
             * Holds what user stated (1st index).
             */
            final String quote = thingsYouSaid.get(0);

            Toast.makeText(getApplicationContext(), quote, Toast.LENGTH_SHORT).show();

            //also toast what user said
//            Toast.makeText(getApplicationContext(), "Looking for: "
//                    + thingsYouSaid.get(0), Toast.LENGTH_SHORT).show();

            //attempt to search for restaurant stated...
            //queryRestaurantMarker(collected_MicInput);

            /**
             * Perform query against desired restaurant...
             */
//            this.queryMatchingRestaurants(restaurantNameMicResult);
//
//            //induce TTS functionality
//            //MicCommands tts_Mic = new MicCommands(this);
//            //tts_Mic.TTS_TalkBack(collected_MicInput);
//
//            /**
//             * Repeat what user desires to look for.
//             */
//            this.speechInvokedTTStalkback(restaurantNameMicResult);

        }
//        else

    }
}
