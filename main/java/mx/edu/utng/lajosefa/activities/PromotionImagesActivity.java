package mx.edu.utng.lajosefa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import mx.edu.utng.lajosefa.R;
import mx.edu.utng.lajosefa.adapters.PromotionImageAdapter;
import mx.edu.utng.lajosefa.entity.uploads.PromotionUpload;

public class PromotionImagesActivity extends AppCompatActivity implements PromotionImageAdapter.OnItemClickListener{

    private RecyclerView rcvPromotions;
    private PromotionImageAdapter imageAdapter;

    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    private List<PromotionUpload> lstUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_images);

        rcvPromotions = findViewById(R.id.rcv_promotion);
        rcvPromotions.setHasFixedSize(true);
        rcvPromotions.setLayoutManager(new LinearLayoutManager(this));


        lstUploads = new ArrayList<>();

        imageAdapter = new PromotionImageAdapter(PromotionImagesActivity.this, lstUploads);

        rcvPromotions.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(PromotionImagesActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("promotion_uploads");

        storage = FirebaseStorage.getInstance();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstUploads.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    PromotionUpload upload = postSnapshot.getValue(PromotionUpload.class);
                    upload.setKey(postSnapshot.getKey());
                    lstUploads.add(upload);
                }

                imageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PromotionImagesActivity.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });



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


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        PromotionUpload selectedItem = lstUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = storage.getReferenceFromUrl(selectedItem.getUrlImage());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(PromotionImagesActivity.this, R.string.success_process, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }
}
