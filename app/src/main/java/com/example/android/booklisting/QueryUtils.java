package com.example.android.booklisting;

import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by rabum_alal on 11/17/17.
 */

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //Object instance of QueryUtils should not be needed.
    private QueryUtils(){
    }

    /**
     *
     * @param bookJson
     * @return a list of books objects built up from parsing a JSON response
     */
    public static List<Book> extractBooks(String bookJson){

        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(bookJson);

            if (jsonResponse.getInt("totalItems") == 0){
                return books;
            }
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            for(int i = 0; i < jsonArray.length(); i++){
               JSONObject currentBook = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                String authors = volumeInfo.getString("authors");

                Book book = new Book(authors, title);
                books.add(book);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
       return books;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        if (url == null){
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //timeouts set to milliseconds
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1000);

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error Code " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "problem retrieving book Json results", e);;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
