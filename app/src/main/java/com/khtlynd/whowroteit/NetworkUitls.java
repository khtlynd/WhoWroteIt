package com.khtlynd.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUitls {
    private static final String LOG_TAG = NetworkUitls.class.getSimpleName();

    //Base URL for Books API
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    //parameter for the search string
    private static final String QUERY_PARAM = "q";
    //Paarameter that limits search results
    private static final String MAX_RESULTS = "maxResults";
    //parameter to filter by print type
    private static final String PRINT_TYPE = "printType";

    static String getBookInfo(String queryString) {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try {
            Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            //convert from uri to URL
            URL requestURL = new URL(builtUri.toString());

            //open the URL connection and make req
            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //get the InputStream
            InputStream inputStream = urlConnection.getInputStream();

            //create a buffered reader from that input dtream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //use a stringbuilder to hold tge incoming response
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null; //cause the stream was empty, no point in parsing
            }

            bookJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }
}
