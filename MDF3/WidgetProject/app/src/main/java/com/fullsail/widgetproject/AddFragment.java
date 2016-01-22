package com.fullsail.widgetproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by isaiasrosario on 1/19/16.
 * MDF3 1601
 */

public class AddFragment extends android.app.Fragment implements View.OnClickListener {

   EditText mMake, mModel, mColor, mDoors;
   String extra;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      super.onCreateView(inflater, container, savedInstanceState);

      // View set up
      View view = inflater.inflate(R.layout.add_fragment, container, false);

      // Initiate button set up
      Button b = (Button) view.findViewById(R.id.addButton);
      b.setOnClickListener(this);

      // Initiate edit text view set up
      mMake = (EditText) view.findViewById(R.id.makeEditText);
      mModel = (EditText) view.findViewById(R.id.modelEditText);
      mColor = (EditText) view.findViewById(R.id.colorEditText);
      mDoors = (EditText) view.findViewById(R.id.doorsEditText);


      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      //set key to see if open was from widget or app
      Intent intent = getActivity().getIntent();
      extra = intent.getStringExtra("key");

   }

   @Override
   public void onClick(View v) {

      // Get string entered in the edit text view
      String make = mMake.getText().toString();
      String model = mModel.getText().toString();
      String color = mColor.getText().toString();
      String doors = mDoors.getText().toString();

      // Check to see if any data was entered
      if (make.isEmpty() || model.isEmpty() || color.isEmpty() || doors.isEmpty()) {

         // if no data display message
         Snackbar.make(v, "Please add data!", Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show();

      } else {

         // Go back to List acticvity after new user was added
         Intent intent = new Intent(getActivity(), MainActivity.class);
         intent.putExtra("make", make);
         intent.putExtra("model", model);
         intent.putExtra("color", color);
         intent.putExtra("doors", doors);
         intent.putExtra("key", extra);
         getActivity().setResult(Activity.RESULT_OK);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
         getActivity().finish();
         Toast.makeText(getActivity(), "Car Added", Toast.LENGTH_SHORT).show();

      }

   }
}

