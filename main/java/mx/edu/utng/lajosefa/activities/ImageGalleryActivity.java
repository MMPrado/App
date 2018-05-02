package mx.edu.utng.lajosefa.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import mx.edu.utng.lajosefa.adapters.ImageGalleryAdapter;
import mx.edu.utng.lajosefa.entity.uploads.UploadGallery;

public class ImageGalleryActivity extends AppCompatActivity implements ImageGalleryAdapter.OnItemClickListener  {
    private RecyclerView mRcvRecyclerView;
    private ImageGalleryAdapter mAdapter;
    private Uri mImageUri;

    private ProgressBar mPgbCircle;
    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef;

    private ValueEventListener mBDListener;
    private List<UploadGallery> mUploads;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);


        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.HORIZONTAL,false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcvRecyclerView = findViewById(R.id.rcv_recycler);
        mRcvRecyclerView.setHasFixedSize(true);
        mRcvRecyclerView.setLayoutManager(gridLayoutManager);

        ImageGalleryAdapter imageAdapter = new ImageGalleryAdapter(ImageGalleryActivity.this, mUploads);
        mRcvRecyclerView.setAdapter(imageAdapter);

        mPgbCircle = findViewById(R.id.pgb_progress_circle);
        mUploads= new ArrayList<>();

        mAdapter = new ImageGalleryAdapter(ImageGalleryActivity.this, mUploads);
        mRcvRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ImageGalleryActivity.this);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference(getString(R.string.uploads));

        mBDListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();

                for(DataSnapshot postSnapshop : dataSnapshot.getChildren()){
                    UploadGallery upload = postSnapshop.getValue(UploadGallery.class);
                    upload.setKey(postSnapshop.getKey());
                    mUploads.add(upload);
                }


                mAdapter.notifyDataSetChanged();
                mPgbCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImageGalleryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mPgbCircle.setVisibility(View.INVISIBLE);
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
        //Intent intent = new Intent(this, SecondActivity.class );
        //intent.putExtra("image",mUploads.get(position).getmImageUrl()); // put image data in Intent
        //startActivity(intent); // start Intent
    }


    @Override
    public void onWhatEverClick(int position) {
        //Toast.makeText(this,"Whatever click at position: "+ position,Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onDeleteClick(int position) {
        UploadGallery selectedItem = mUploads.get(position);
        final String selectedkey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getmImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedkey).removeValue();
                Toast.makeText(ImageGalleryActivity.this, R.string.removed, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mBDListener);
    }

}
