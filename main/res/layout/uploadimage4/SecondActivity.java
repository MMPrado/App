package mx.edu.utng.mmacias.uploadimage4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    ImageView selectedImage;
    private List<Upload> mUploads;
    private FirebaseStorage mStorage;
    private Button btnShow;


    private DatabaseReference mDatabaseRef;
    private ValueEventListener mBDListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectedImage = findViewById(R.id.ivw_selectedImage);


        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent(); // get Intent which we set from Previous Activity
                Upload selectedItem = mUploads.get(intent.getIntExtra(getString(R.string.imag), 0));
                StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getmImageUrl());

                Picasso.with(getApplicationContext())
                        .load(selectedItem.getmImageUrl())
                        .placeholder(R.mipmap.josefabar)
                        .fit()
                        .centerCrop()
                        .into(selectedImage);
            }
        });
        mUploads = new ArrayList<>();


        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(getString(R.string.uploads));


        mBDListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();

                for (DataSnapshot postSnapshop : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshop.getValue(Upload.class);
                    upload.setKey(postSnapshop.getKey());
                    mUploads.add(upload);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void Comment(View view) {
        startActivity(new Intent(this, mx.edu.utng.mmacias.uploadimage4.CommentsActivity.class));

    }
}
