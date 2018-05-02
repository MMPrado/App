package mx.edu.utng.lajosefa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import mx.edu.utng.lajosefa.entity.uploads.UploadEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesEventFragment extends Fragment implements ImageEventAdapterFragment.OnItemClickListener{

    private RecyclerView rcvRecyclerView;
    private ImageEventAdapterFragment mAdapter;

    private ProgressBar pgbCircle;

    private FirebaseStorage firebaseStorage;

    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;

    private List<UploadEvent> lsvUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_images_event, container, false);
        rcvRecyclerView = v.findViewById(R.id.rcv_recycler);
        rcvRecyclerView.setHasFixedSize(true);
        rcvRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pgbCircle = v.findViewById(R.id.pgb_circle);

        lsvUpload = new ArrayList<>();
        mAdapter = new ImageEventAdapterFragment(getActivity(), lsvUpload);
        rcvRecyclerView.setAdapter(mAdapter);

        //mAdapter.setOnItemClickListener(getActivity());

        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("events");


        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lsvUpload.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    UploadEvent upload = postSnapshot.getValue(UploadEvent.class);
                    upload.setKey(postSnapshot.getKey());
                    lsvUpload.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                pgbCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                pgbCircle.setVisibility(View.INVISIBLE);
            }
        });


        return v;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        UploadEvent selectedItem = lsvUpload.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), R.string.success_process, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }


}
