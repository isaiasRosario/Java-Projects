package com.fullsail.fundamentalspart1.libs;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by isaiasrosario on 11/2/15.
 */
public class MyTask extends AsyncTask<String, Void, Void>{

   String zipLocation = "";


   @Override
   protected void onPreExecute() {


   }

   @Override
   protected Void doInBackground(String... params) {


      try {
         HttpClient client = new DefaultHttpClient();
         HttpPost post = new HttpPost(params[0]);
         HttpResponse response = client.execute(post);

         int status = response.getStatusLine().getStatusCode();
         if (status == 200) {

            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);

            JSONObject obj1 = new JSONObject(data);
            JSONObject obj2 = obj1.getJSONObject("query").getJSONObject("results");
            JSONObject obj3 = obj2.getJSONObject("weather").getJSONObject("rss");
            JSONObject obj4 = obj3.getJSONObject("channel").getJSONObject("item");

            JSONObject location = obj3.getJSONObject("channel").getJSONObject("location");
            String city = location.getString("city");
            String state = location.getString("region");
            String country = location.getString("country");
            zipLocation = city + ", " + state + " " + country;

            JSONArray arrObj = obj4.getJSONArray("forecast");
            System.out.println(arrObj);


            for (int i = 0; i < arrObj.length(); i++) {

               JSONObject forecast = arrObj.getJSONObject(i);


               System.out.println("Condition: " + forecast.getString("text"));
               System.out.println("High: " + forecast.getString("high"));
               System.out.println("Low: " + forecast.getString("low"));
               System.out.println("Day: " + forecast.getString("day"));
               System.out.println("Date: " + forecast.getString("date"));
            }

         }
      } catch (ClientProtocolException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (JSONException e) {
         e.printStackTrace();
      }
      return null;
   }


   //After async task is done populate list view
   @Override
   protected void onPostExecute(Void results) {
      System.out.println(results);

      FragmentOne fragObj = new FragmentOne();
      fragObj.setData(zipLocation);

   }


}
