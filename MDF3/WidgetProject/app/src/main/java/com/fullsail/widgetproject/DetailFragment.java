package com.fullsail.widgetproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by isaiasrosario on 1/19/16.
 * MDF3 1601
 */

public class DetailFragment extends Fragment {

   public static final String EXTRA_ITEM = "com.fullsail.widgetproject.EXTRA_ITEM";

   TextView makeTextView, modelTextView, colorTextView, doorsTextView;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);

      // View set up
      View view = inflater.inflate(R.layout.detail_fragment, container, false);

      // Initiate text views and set up the ids
      makeTextView = (TextView) view.findViewById(R.id.makeTextView);
      modelTextView = (TextView) view.findViewById(R.id.modelTextView);
      colorTextView = (TextView) view.findViewById(R.id.colorTextView);
      doorsTextView = (TextView) view.findViewById(R.id.doorsTextView);

      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      // Gte intent car object and update textview text
      Intent intent = getActivity().getIntent();
      CarObject car = (CarObject) intent.getSerializableExtra(EXTRA_ITEM);
      if (car == null) {
         getActivity().finish();
         return;
      }

      makeTextView.setText("Make:  " + car.getMake());
      modelTextView.setText("Model:  " + car.getModel());
      colorTextView.setText("Color:  " + car.getColor());
      doorsTextView.setText("Doors:  " + car.getDoors());

   }
}
