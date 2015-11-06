package com.fullsail.fundamentalspart1.libs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.fullsail.fundamentalspart1.R;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * Created by isaiasrosario on 10/29/14.
 */
public class FragmentOne extends Fragment implements AdapterView.OnItemClickListener{

   // Global Variables
   FragmentOneListener listener;

   public String zipLocation = "";
   public String condition = "";
   public String high = "";
   public String low = "";

   private View rootView;
   public ArrayList<String> arr = new ArrayList<String>();
   public ArrayList<String> arr2 = new ArrayList<String>();
   public ArrayList<String> arr3 = new ArrayList<String>();
   public ArrayList<String> arr4 = new ArrayList<String>();

   public ArrayList<ArrayList<String>> arraylist = new ArrayList<>();
   public ListView lv;
   public ArrayAdapter adapter;

   Context cont = null;
   private File filesDir;


   // Attach activity to click listener
   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);

      if (activity instanceof FragmentOneListener) {
         listener = (FragmentOneListener) activity;

      } else {
         throw new IllegalArgumentException("ERROR CHECK IT OUT!");
      }
   }

   public void setData(String _string){
      zipLocation = _string;
      System.out.println(zipLocation);

   }


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      super.onCreateView(inflater, container, savedInstanceState);

      // Set frag one layout
      rootView = inflater.inflate(R.layout.frag_one, container, false);

      Context cont = getActivity().getApplicationContext();

      return rootView;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      // LIst view set up
      lv = (ListView) rootView.findViewById(R.id.listView);

      lv.setOnItemClickListener(this);

      // Check if network is available
      Boolean network = ((MainActivity) getActivity()).isNetworkAvailable();

      // If not network pull saved data
      if (!network) {

         ((MainActivity) getActivity()).networkAlert();


            try {
               FileInputStream fis = new FileInputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile"));
               ObjectInputStream ois = new ObjectInputStream(fis);
               arraylist = (ArrayList) ois.readObject();
               ois.close();
               fis.close();

               FileInputStream fis2 = new FileInputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile2"));
               InputStreamReader inReader = new InputStreamReader(fis2);
               BufferedReader reader = new BufferedReader(inReader);

               StringBuilder stringBuilder = new StringBuilder();
               String text = null;

               while((text = reader.readLine()) != null) {
                  stringBuilder.append(text + "\n");
               }

               reader.close();

               zipLocation = stringBuilder.toString();

               listener.clickListener(null, zipLocation, null, null);

               adapter = new ArrayAdapter<String>(getActivity(),
                  android.R.layout.simple_list_item_1, arraylist.get(0));

               lv.setAdapter(null);
               lv.setAdapter(adapter);

               System.out.println("FILE READ!" + arraylist);

            } catch (IOException ioe) {
               ioe.printStackTrace();
               ((MainActivity) getActivity()).nodataAlert();
               return;
            } catch (ClassNotFoundException c) {
               System.out.println("Class not found");
               ((MainActivity) getActivity()).nodataAlert();
               c.printStackTrace();
               return;
            }



      }




      //When button click start async task api search
      getActivity().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            EditText et = (EditText) getActivity().findViewById(R.id.editText);

            String zip = et.getText().toString();

            boolean digitsOnly = TextUtils.isDigitsOnly(zip);
            String url = "";
            System.out.println(digitsOnly);

            Boolean network = ((MainActivity) getActivity()).isNetworkAvailable();


            // If no network pull saved data
            if (!network) {

               ((MainActivity) getActivity()).networkAlert();

               try {
                  FileInputStream fis = new FileInputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile"));
                  ObjectInputStream ois = new ObjectInputStream(fis);
                  arraylist = (ArrayList) ois.readObject();
                  ois.close();
                  fis.close();

                  FileInputStream fis2 = new FileInputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile2"));
                  InputStreamReader inReader = new InputStreamReader(fis2);
                  BufferedReader reader = new BufferedReader(inReader);

                  StringBuilder stringBuilder = new StringBuilder();
                  String text = null;

                  while ((text = reader.readLine()) != null) {
                     stringBuilder.append(text + "\n");
                  }

                  reader.close();

                  zipLocation = stringBuilder.toString();

                  listener.clickListener(null, zipLocation, null, null);

                  adapter = new ArrayAdapter<String>(getActivity(),
                     android.R.layout.simple_list_item_1, arraylist.get(0));

                  lv.setAdapter(null);
                  lv.setAdapter(adapter);

                  System.out.println("FILE READ!" + arraylist);

               } catch (IOException ioe) {
                  ioe.printStackTrace();
                  ((MainActivity) getActivity()).nodataAlert();
                  return;
               } catch (ClassNotFoundException c) {
                  System.out.println("Class not found");
                  ((MainActivity) getActivity()).nodataAlert();
                  c.printStackTrace();
                  return;
               }

            } else {

               //Set zip code and create url for AsyncTAsk
               if (digitsOnly) {
                  String zipCode = zip;
                  if (zipCode.length() == 5) {
                     zipCode = zip;
                     url = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20weather.bylocation%20WHERE%20location%3D%22" + zipCode + "%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

//                     new MyTask().execute(url);
                     new MyAsyncTask().execute(url);
                     //listener.clickListener(condition, zipLocation);

                  } else {
                     ((MainActivity) getActivity()).alert();
                     System.out.println("NOPE NO ZIPCODE!");
                  }
               } else {
                  // zipCode = data;
                  ((MainActivity) getActivity()).alert();
                  System.out.println("NOPE NO ZIPCODE!");
               }

            }
         }
      });
   }

   // Item click from the list view
   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

      Boolean network = ((MainActivity) getActivity()).isNetworkAvailable();

      //If no network pul saved data
      if (!network) {

         try
         {
            FileInputStream fis = new FileInputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            arraylist = (ArrayList) ois.readObject();
            ois.close();
            fis.close();

            FileInputStream fis2 = new FileInputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile2"));
            InputStreamReader inReader = new InputStreamReader(fis2);
            BufferedReader reader = new BufferedReader(inReader);

            StringBuilder stringBuilder = new StringBuilder();
            String text = null;

            while((text = reader.readLine()) != null) {
               stringBuilder.append(text + "\n");
            }

            reader.close();

            zipLocation = stringBuilder.toString();

            condition = "\n" + "Condition: " + arraylist.get(1).get(position);
            high = "\n" + "High: " + arraylist.get(2).get(position);
            low = "\n" + "Low: " + arraylist.get(3).get(position);

            listener.clickListener(condition, zipLocation, high, low);

            System.out.println("FILE READ!" + arraylist);

         }catch(IOException ioe){
            ioe.printStackTrace();
            return;
         }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return;
         }


      } else {

         // set text for listener to send to detail view frag two
         condition = "\n" + "Condition: " + arr2.get(position);
         high = "\n" + "High: " + arr3.get(position);
         low = "\n" + "Low: " + arr4.get(position);

         listener.clickListener(condition, zipLocation, high, low);

      }

   }

   public File getFilesDir() {
      return filesDir;
   }



   //Run Async Task
   private class MyAsyncTask extends AsyncTask<String, Void, Void> {

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

               listener.clickListener(null, zipLocation, null, null);

               arr.clear();
               arr2.clear();
               arr3.clear();
               arr4.clear();

               for (int i = 0; i < arrObj.length(); i++) {

                  JSONObject forecast = arrObj.getJSONObject(i);

                  arr.add(forecast.getString("date"));
                  arr2.add(forecast.getString("text"));
                  arr3.add(forecast.getString("high"));
                  arr4.add(forecast.getString("low"));

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
         super.onPostExecute(results);
         System.out.println(results);
         System.out.println(arr);

         adapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1, arr);

         lv.setAdapter(adapter);


         // Saving Data
         try {
            arraylist.add(0, arr);
            arraylist.add(1, arr2);
            arraylist.add(2, arr3);
            arraylist.add(3, arr4);

            FileOutputStream fos = new FileOutputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arraylist);
            oos.close();
            fos.close();

            FileOutputStream fos2 = new FileOutputStream(new File(android.os.Environment.getExternalStorageDirectory(), "myfile2"));
            fos2.write(zipLocation.getBytes());
            fos2.close();

            System.out.println("FILE SAVED!");
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }


      }
   }

}