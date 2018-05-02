package mx.edu.utng.lajosefa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.edu.utng.lajosefa.R;
import mx.edu.utng.lajosefa.entity.uploads.UploadReserve;
import mx.edu.utng.lajosefa.activities.MainReserveActivity;

/**
 * Created by Diana Manzano on 29/03/2018.
 */

public class ImageReserveAdapter extends RecyclerView.Adapter<ImageReserveAdapter.ImageViewHolder>  {
    private Context context;
    private List<UploadReserve> lstUploads;
    private MainReserveActivity reserveActivity;

    private OnItemClickListener listener;

    public ImageReserveAdapter(Context context, List<UploadReserve> uploads){
        this.context = context;
        lstUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item_reserve,parent, false );
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        UploadReserve uploadCurrent = lstUploads.get(position);
        holder.txvReservationName.setText(uploadCurrent.getName());
        holder.txvReservationPhone.setText(uploadCurrent.getPhone());
        holder.txvReservationPeople.setText(uploadCurrent.getPeople());
        holder.txvReservationDate.setText(uploadCurrent.getDate());
        holder.txvReservationOrder.setText(uploadCurrent.getOrder());
    }



    @Override
    public int getItemCount() {
        return lstUploads.size();
    }

    public  class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView txvReservationName;
        public TextView txvReservationPhone;
        public TextView txvReservationPeople;
        public TextView txvReservationDate;
        public TextView txvReservationOrder;

        public ImageViewHolder(View itemView) {
            super(itemView);

            txvReservationName = itemView.findViewById(R.id.txv_name);
            txvReservationPhone= itemView.findViewById(R.id.txv_phone);
            txvReservationPeople= itemView.findViewById(R.id.txv_npeople);
            txvReservationDate= itemView.findViewById(R.id.txv_date);
            txvReservationOrder= itemView.findViewById(R.id.txv_order);

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
