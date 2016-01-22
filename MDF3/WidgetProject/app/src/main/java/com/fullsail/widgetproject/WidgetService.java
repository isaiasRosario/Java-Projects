package com.fullsail.widgetproject;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by isaiasrosario on 1/20/16.
 * MDF3 1601
 */

public class WidgetService extends RemoteViewsService {
   @Override
   public RemoteViewsFactory onGetViewFactory(Intent intent) {
      return new WidgetFactory(getApplicationContext());
   }
}
