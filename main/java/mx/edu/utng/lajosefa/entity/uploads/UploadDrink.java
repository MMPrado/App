package mx.edu.utng.lajosefa.entity.uploads;

import com.google.firebase.database.Exclude;

/**
 * Created by Diana Manzano on 02/04/2018.
 */

public class UploadDrink {
    private String name;
    private String description;
    private String url;
    private String Key;

    public UploadDrink(){
        //Empty constructor needed
    }

    public UploadDrink(String name, String description, String url) {
        if(name.trim().equals("") && description.trim().equals("")){
            name = "No especificado";
            description = "No especificado";
        }
        this.name = name;
        this.description = description;
        this.url = url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    @Exclude
    public String getKey(){
        return Key;
    }
    @Exclude
    public  void setKey(String key){
        this.Key = key;
    }
}
