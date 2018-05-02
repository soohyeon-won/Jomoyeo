package com.example.samsung.jomoyeo1.dbconnection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class AsyncTaskActivity extends AsyncTask<HashMap<String,String>, Integer, HttpRequestResult > {

    String charset = "UTF-8";

    @Override
    protected HttpRequestResult doInBackground(HashMap<String, String>... hashMaps) {
        HashMap<String,String> basic = hashMaps[0];
        HashMap<String,String> params = hashMaps[1];
        String url = basic.get("url");
        String method = basic.get("method");

        for( String key : params.keySet() ){
            try {
                String param = String.format(key+"=%s&", URLEncoder.encode(params.get(key), charset));
                url +=param;
            }catch (Exception e){
                e.printStackTrace();
                Log.d("test4",e.toString());
            }
        }
        url = url.substring(0,url.length()-1);
        Log.d("test",url);

        BufferedReader bufferedReader = null;
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        HttpRequestResult result = new HttpRequestResult();
        String json;

        try {
            URL urlToRequest = new URL(url);
            Log.d("test2", url);

            urlConnection = (HttpURLConnection) urlToRequest.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            //같은 내용을 쓰면 그 전 값을 cache로 가져오지 않게함.==> 캐시 기능을 꺼버림 항상 서버에 접속하도록~
            urlConnection.setUseCaches(false);
            urlConnection.setDefaultUseCaches(false);

            urlConnection.setRequestProperty("Accept", "application/json");

            switch (method) {
                case "POST":
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    break;

                case "GET":
                    urlConnection.setDoInput(true);
                    break;

                case "PUT":
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    break;
            }

            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod(method);

            //연결시도
            urlConnection.connect();

            result.setResultCode(urlConnection.getResponseCode());

            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            if(!sb.toString().equals("")){
                if (sb.toString().startsWith("[")) result.setResultJsonArray(new JSONArray(sb.toString()));
                else result.setResultJson(new JSONObject(sb.toString()));}


        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            Log.d("error",e.toString());

        }
        urlConnection.disconnect();

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(HttpRequestResult s) {
        super.onPostExecute(s);
    }

}