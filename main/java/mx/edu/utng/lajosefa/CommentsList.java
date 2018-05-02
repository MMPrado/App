package mx.edu.utng.lajosefa;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.edu.utng.lajosefa.entity.Comments;

public class CommentsList extends ArrayAdapter {

    private Activity context;
    private List<Comments> lstComments;

    public CommentsList (Activity context, List<Comments> commentsList){
        super (context, R.layout.activity_comments_list,commentsList);
        this.context = context;
        this.lstComments = commentsList;
    }
    @Override
    public View getView (int position, View convertirView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_comments_list,null,true);

        TextView textViewName = listViewItem.findViewById(R.id.txv_name);

        Comments comments = lstComments.get(position);
        textViewName.setText(comments.getCommentsName());

        return listViewItem;


    }
}
