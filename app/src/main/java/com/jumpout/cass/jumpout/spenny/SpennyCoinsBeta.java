package com.jumpout.cass.jumpout.spenny;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.cassvice.sampleapp.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.jumpout.cass.jumpout.MainActivity;
import com.jumpout.cass.jumpout.SpennyCoinsProgress;
import com.jumpout.cass.jumpout.clientendpoint.EndpointsAsyncTask;

import java.io.IOException;

/**
 * Created by aramide on 09/03/2017.
 */

public class SpennyCoinsBeta {

    private MainActivity activity;

    public SpennyCoinsBeta(final MainActivity act) {
        this.activity = act;
    }

    public void requestSpennyCoinsAsync() {

        new SpennyCoinsAsyncTask().execute(new Pair<Context, String>(this.activity, ""));

    }

    /**
     *
     * @param result
     */
    private void notifySpennyCoinsRequestResult(final String result) {
        this.activity.notifyBetaSpennyCoinsResult(result);
    }

    public class SpennyCoinsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

        public SpennyCoinsAsyncTask() {

        }

        private Context cTx;

        /**
         *
         * @param params
         * @return - Picked up as a method param in onPostExecute(... -->> result
         */
        @Override
        protected String doInBackground(Pair<Context, String>... params) {

            final String sampleResult = "Spenny coins beta currently unavailable," +
                    "\nInflation's a bitch...";
            return sampleResult;

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
                SpennyCoinsProgress progress = new SpennyCoinsProgress(SpennyCoinsBeta.this.activity);
                progress.invokeProgressDialogLoading();

                SpennyCoinsBeta.this.notifySpennyCoinsRequestResult(result);

            }
            else
            {
                SpennyCoinsProgress progress = new SpennyCoinsProgress(SpennyCoinsBeta.this.activity);
                progress.invokeProgressDialogLoading();

                SpennyCoinsBeta.this.notifySpennyCoinsRequestResult(result);
            }

        }
    }

}