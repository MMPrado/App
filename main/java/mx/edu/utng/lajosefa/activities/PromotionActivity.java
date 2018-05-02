package mx.edu.utng.lajosefa.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import mx.edu.utng.lajosefa.Common;
import mx.edu.utng.lajosefa.R;
import mx.edu.utng.lajosefa.entity.MyResponse;
import mx.edu.utng.lajosefa.entity.Notification;
import mx.edu.utng.lajosefa.entity.Sender;
import mx.edu.utng.lajosefa.remote.APIService;
import mx.edu.utng.lajosefa.entity.uploads.PromotionUpload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PromotionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnChooseImage;
    private Button btnUpload;
    private Button btnShow;
    private EditText edtDescription;
    private ImageView imvPromotionImage;
    private ProgressBar pgbLoading;

    private Uri uri;
    private StorageReference reference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;

    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        btnChooseImage = findViewById(R.id.btn_choose_file_prom);
        btnUpload = findViewById(R.id.btn_upload_prom);
        btnShow = findViewById(R.id.btn_show_prom);
        edtDescription = findViewById(R.id.edt_promotion);
        imvPromotionImage = findViewById(R.id.imv_promotion_image);
        pgbLoading = findViewById(R.id.pgb_loading_prom);

        reference = FirebaseStorage.getInstance().getReference("promotion_uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("promotion_uploads");

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(PromotionActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagesActivity();
            }
        });

        //message
        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("MY TOKEN", ""+Common.currentToken);
        apiService = Common.getFCMClient();

        FirebaseMessaging.getInstance().subscribeToTopic("MyTopic");




        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            uri = data.getData();

            Picasso.with(this).load(uri).into(imvPromotionImage);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
       if(uri != null){
           StorageReference fileReference = reference.child(System.currentTimeMillis()
                   + "." + getFileExtension(uri));
           storageTask = fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           pgbLoading.setProgress(0);
                       }
                   }, 5000);

                   Toast.makeText(PromotionActivity.this, R.string.success_process,
                           Toast.LENGTH_LONG).show();
                   PromotionUpload upload = new PromotionUpload(edtDescription.getText()
                           .toString().trim(), taskSnapshot.getDownloadUrl().toString());
                   String uploadId = databaseReference.push().getKey();
                   databaseReference.child(uploadId).setValue(upload);
                   sendMessage();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(PromotionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                   double progress = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                   pgbLoading.setProgress((int) progress);
               }
           });
       }else{
           Toast.makeText(this, R.string.do_not_selected, Toast.LENGTH_SHORT).show();
       }
    }

    private void openImagesActivity(){
        Intent intent = new Intent(this, PromotionImagesActivity.class);
        startActivity(intent);
    }

    private void sendMessage(){
        //send request
        Notification notification = new Notification(getString(R.string.bar_la_josefa),getString(R.string.new_promotion));
        //Notification notification = new Notification(edtDrinkName.getText().toString(),edtDrinkDescription.getText().toString());
        Sender sender = new Sender(notification, "/topics/MyTopic");
        apiService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        System.out.println("Success: "+response.isSuccessful());
                        if(response.isSuccessful()){
                            Toast.makeText(PromotionActivity.this, R.string.success, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(PromotionActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.e(getString(R.string.sending_error), t.getMessage());
                    }
                });

    }
}
