package com.example.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import timber.log.Timber;

public class Internet {

    public boolean hasInternetAccess(Context context) {
        InternetAsyncTask asyncTask=new InternetAsyncTask();

        if (isNetworkAvailable(context)) {
            try {
                return asyncTask.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
       Timber.e("No network available!" );
        return false;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );

        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private class InternetAsyncTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute( aBoolean );
            Timber.d(String.valueOf( aBoolean ) );
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection urlc = (HttpsURLConnection) (new URL("https://google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return true;
            } catch (IOException e) {
                Timber.e(e.getMessage() );
                return false;
            } }


    }

}
