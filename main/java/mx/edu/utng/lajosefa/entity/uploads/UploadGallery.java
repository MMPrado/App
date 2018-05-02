package mx.edu.utng.lajosefa.entity.uploads;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class UploadGallery implements Serializable{
    private String mImageUrl;
    private String key;

    public UploadGallery(){

    }

    public UploadGallery(String imageUrl){
        mImageUrl = imageUrl;
    }


    public String getmImageUrl (){
        return  mImageUrl;
    }


    public void setmImageUrl (String imageUrl){
        mImageUrl = imageUrl;
    }
@Exclude
    public String getKey(){
        return key;
    }
@Exclude
    public void setKey (String key){
        this.key = key;
    }
}


