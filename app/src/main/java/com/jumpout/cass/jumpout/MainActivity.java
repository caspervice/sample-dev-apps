package com.jumpout.cass.jumpout;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jumpout.cass.jumpout.clientendpoint.EndpointsAsyncTask;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

            case R.id.action_settings:
                break;

//                this.getSampleLink();

            default:
                return super.onOptionsItemSelected(item);
//            case R.id.
        }

        return true;

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
}
