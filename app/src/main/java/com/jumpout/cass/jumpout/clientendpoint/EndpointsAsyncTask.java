package com.jumpout.cass.jumpout.clientendpoint;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.cassvice.sampleapp.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.jumpout.cass.jumpout.MainActivity;
import com.jumpout.cass.jumpout.SpennyCoinsProgress;

import java.io.IOException;

/**
 * Created by aramide on 07/03/2017.
 */

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    //TODO -->> design architecture for this data flow, consistent across features
    /** Use instance of the acitivty to push completed result back to UI... **/
    private MainActivity mainActivity;

    /**
     * Async Constructor.
     *
     * @param act - instance of activity (to provide handle from completed async task back to Ui.)
     *
     */
    public EndpointsAsyncTask(final MainActivity act) {
        this.mainActivity = act;
    }

    //TODO -->> direct call to backend module Cloud EndPoint object...
    private static MyApi myApiService = null;
    private Context cTx;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        if (myApiService == null) //only do this once...
        {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)

                    //options for runn againt local devappServer...
                    // - turn off compression when running against local devappserver
//                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("https://micro-elysium-160822.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        this.cTx = params[0].first;
        String name = params[0].second;

        try {

            return myApiService.sayHi(name).execute().getData();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * Executed post doInBackground completion...
     *
     * @param result - produced result
     */
    @Override
    protected void onPostExecute(final String result) {

        //execute callback to Ui...
        if (result != null)
        {

            this.mainActivity.notifyEndPointRequestResult(result);
//            Toast.makeText(this.cTx, result, Toast.LENGTH_LONG).show();
        }
        else
        {
            this.mainActivity.notifyEndPointRequestResult(result);
        }

    }
}
