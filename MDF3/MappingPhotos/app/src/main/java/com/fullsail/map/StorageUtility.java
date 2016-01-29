package com.fullsail.map;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by isaiasrosario on 1/26/16.
 * MDF3 1601 Project 4
 */

public class StorageUtility {

   // Save data to internal mStorage method
   public void saveData(Context context, ArrayList _array) {

      try {
         FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), "file"));
         ObjectOutputStream oos = new ObjectOutputStream(fos);
         oos.writeObject(_array);
         oos.close();
         fos.close();

         System.out.println("FILE SAVED!");
      } catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }

   // Read saved data from internal mStorage method
   public void readData(Context context, ArrayList _array) {

      try {
         FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), "file"));
         ObjectInputStream ois = new ObjectInputStream(fis);
         _array = (ArrayList) ois.readObject();
         ois.close();
         fis.close();

         System.out.println("FILE READ!");
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } catch (ClassNotFoundException c) {
         c.printStackTrace();
      }
   }
}
