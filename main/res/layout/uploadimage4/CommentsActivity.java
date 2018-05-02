package mx.edu.utng.mmacias.uploadimage4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;
    ListView listViewCommets;
    List<Comments> commentsList;

    DatabaseReference databaseComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        databaseComments= FirebaseDatabase.getInstance().getReference("comments");
        editTextName = (EditText) findViewById(R.id.editTextName);
        listViewCommets = (ListView) findViewById(R.id.ltv_comments);
        buttonAdd = (Button) findViewById(R.id.btn_add_comments);
        commentsList= new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                addComments();
            }
        });
    }
    @Override
    protected  void onStart(){
        super.onStart();

        databaseComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentsList.clear();
                for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()){
                    Comments comments = commentSnapshot.getValue(Comments.class);
                    commentsList.add(comments);


                }
                mx.edu.utng.mmacias.uploadimage4.CommentsList adapter = new mx.edu.utng.mmacias.uploadimage4.CommentsList(mx.edu.utng.mmacias.uploadimage4.CommentsActivity.this, commentsList);
                listViewCommets.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addComments(){

        String name = editTextName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)){
            String id = databaseComments.push().getKey();
            Comments comments = new Comments(id,name);
            databaseComments.child(id).setValue(comments);
            Toast.makeText(this,"Comentario agregado",Toast.LENGTH_SHORT).show();
            editTextName.setText("");

        }else{
            Toast.makeText(this,"Debes escribir un comentario",Toast.LENGTH_SHORT).show();
        }

    }


}
