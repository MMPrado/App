package mx.edu.utng.lajosefa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.edu.utng.lajosefa.R;
import mx.edu.utng.lajosefa.entity.uploads.UploadDrink;

/**
 * Created by Diana Manzano on 06/03/2018.
 */

public class ImageDrinkAdapter extends RecyclerView.Adapter<ImageDrinkAdapter.ImageViewHolder> {
    private Context context;
    private List<UploadDrink> lstUploads;

    private OnItemClickListener listener;



    public ImageDrinkAdapter(Context context, List<UploadDrink> uploads){
        this.context = context;
        lstUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item_drink, parent, false);
        return new ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        UploadDrink uploadCurrent = lstUploads.get(position);
        holder.txvDrinkName.setText(uploadCurrent.getName());
        holder.txvDrinkDescription.setText(uploadCurrent.getDescription());
        Picasso.with(context)
                .load(uploadCurrent.getUrl())
                .placeholder(R.drawable.la_josefa2)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return lstUploads.size();
    }

    public  class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView txvDrinkName;
        public  TextView txvDrinkDescription;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            txvDrinkName = itemView.findViewById(R.id.txv_drink_name);
            txvDrinkDescription = itemView.findViewById(R.id.txv_drink_description);
            imageView = itemView.findViewById(R.id.imv_photo);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                int position= getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem delete = menu.add(Menu.NONE,1,1,R.string.delete);
            delete.setOnMenuItemClickListener(this);
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listener != null){
                int position= getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){

                    switch (item.getItemId()){
                        case 1:
                            listener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public  interface OnItemClickListener{
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
