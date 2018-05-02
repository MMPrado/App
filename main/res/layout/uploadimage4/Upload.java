package mx.edu.utng.;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Upload implements Serializable{
    private String mImageUrl;
    private String mKey;

    public Upload(){

    }

    public Upload(String imageUrl){
       // if (name.trim().equals("")){
          //  name= "no name";
        //}
        //mName=name;
        mImageUrl = imageUrl;
    }

   /* public String  getmName(){
        return  mName;
    }*/

    public String getmImageUrl (){
        return  mImageUrl;
    }


   /* public void setmName (String name){
        name = name;
    }*/

    public void setmImageUrl (String imageUrl){
        mImageUrl = imageUrl;
    }
@Exclude
    public String getKey(){
        return mKey;
    }
@Exclude
    public void setKey (String key){
        mKey = key;
    }
}


