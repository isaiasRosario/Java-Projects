package com.fullsail.multiactivity;

/**
 * Created by isaiasrosario on 11/12/15.
 */
public class UserData {
   private String mFirstName;
   private String mLastName;
   private String mAge;

   public UserData(){
      mFirstName = mLastName = mAge = "";
   }

   public UserData(String _firstName, String _lastName, String _age){
      mFirstName = _firstName;
      mLastName = _lastName;
      mAge = _age;
   }

   public String getFirstName(){
      return mFirstName;
   }

   public String getLastName(){
      return mLastName;
   }

   public String getAge(){
      return mAge;
   }

   @Override
   public String toString(){
      return mFirstName;

   }


}
