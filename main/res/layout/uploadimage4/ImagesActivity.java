package mx.edu.utng.lajosefa.activities.;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class ImagesActivity extends AppCompatActivity implements mx.edu.utng.mmacias.uploadimage4.ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private mx.edu.utng.mmacias.uploadimage4.ImageAdapter mAdapter;
    private Uri mImageUri;

    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef;

    private ValueEventListener mBDListener;
    private List<mx.edu.utng.mmacias.uploadimage4.Upload> mUploads;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);


        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.HORIZONTAL,false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mx.edu.utng.mmacias.uploadimage4.ImageAdapter imageAdapter = new mx.edu.utng.mmacias.uploadimage4.ImageAdapter(mx.edu.utng.mmacias.uploadimage4.ImagesActivity.this, mUploads);
        mRecyclerView.setAdapter(imageAdapter);

        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads= new ArrayList<>();

        mAdapter = new mx.edu.utng.mmacias.uploadimage4.ImageAdapter(mx.edu.utng.mmacias.uploadimage4.ImagesActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(mx.edu.utng.mmacias.uploadimage4.ImagesActivity.this);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference(getString(R.string.uploads));

        mBDListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();

                for(DataSnapshot postSnapshop : dataSnapshot.getChildren()){
                    mx.edu.utng.mmacias.uploadimage4.Upload upload = postSnapshop.getValue(mx.edu.utng.mmacias.uploadimage4.Upload.class);
                    upload.setKey(postSnapshop.getKey());
                    mUploads.add(upload);
                }


                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mx.edu.utng.mmacias.uploadimage4.ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
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
        mx.edu.utng.mmacias.uploadimage4.Upload selectedItem = mUploads.get(position);
        final String selectedkey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getmImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedkey).removeValue();
                Toast.makeText(mx.edu.utng.mmacias.uploadimage4.ImagesActivity.this, R.string.removed, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mBDListener);
    }

}
