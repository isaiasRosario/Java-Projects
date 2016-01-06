package com.fullsail.fundamentalspart1.libs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullsail.fundamentalspart1.R;

/**
 * Created by isaiasrosario on 10/29/15.
 */
public class FragmentTwo extends Fragment {


   //setting arguments for fragment listener
   public static FragmentTwo instancof (String data, String data1, String data2, String data3){
      FragmentTwo frag = new FragmentTwo();

      Bundle args = new Bundle();
      args.putString("Data", data);
      args.putString("Data1", data1);
      args.putString("Data2", data2);
      args.putString("Data3", data3);


      frag.setArguments(args);

      return frag;
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

      super.onCreateView(inflater, container, savedInstanceState);

      //Set frag layout
      return inflater.inflate(R.layout.frag_two, container, false);
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState){
      super.onActivityCreated(savedInstanceState);

      //Set text values passed from list view click
      Bundle args = getArguments();
      TextView tv = (TextView) getActivity().findViewById(R.id.textView);
      TextView tv2 = (TextView) getActivity().findViewById(R.id.textView2);
      TextView tv3 = (TextView) getActivity().findViewById(R.id.textView3);
      TextView tv4 = (TextView) getActivity().findViewById(R.id.textView4);

      //Check if values are empty
      if (args !=null && args.containsKey("Data1")){
         tv.setText(args.getString("Data1"));
         tv2.setText(args.getString("Data"));
         tv3.setText(args.getString("Data2"));
         tv4.setText(args.getString("Data3"));
      }else {
         tv.setText("ENTER YOUR ZIP CODE!");
      }


   }

}
