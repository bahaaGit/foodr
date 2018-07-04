package ateam.foodr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResturantsAdapter extends RecyclerView.Adapter<ResturantsAdapter.ViewHolder> {

    private Context rContext;
    private ArrayList<Restaurant> rList;
    private OnItemClickListener mListener;

    //The storage reference so that the profile images can be stored on the FireBase
    private StorageReference mImageStorage;

    ResturantsAdapter(Context context, ArrayList<Restaurant> list){
        this.rContext = context;
        rContext = context;
        rList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(rContext);

        View view = layoutInflater.inflate(R.layout.resturant_item_row,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant resturantItem = rList.get(position);

        ImageView image = holder.resturantImg;
        TextView name,address;

        name = holder.resturantName;
        address = holder.resturantLoc;

        // TODO: Re-enable this after we fix it.
        name.setText(resturantItem.getName());
        address.setText(resturantItem.getAddress());

        // TODO: Re-enable this
        //image.setImageResource(foodItem.getImage());
        String url = resturantItem.getImageurl();

        mImageStorage = FirebaseStorage.getInstance().getReference();
        Picasso.with(image.getContext()).load(url).into(image);
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        ImageView resturantImg;
        TextView resturantName,resturantLoc;


        ViewHolder(View itemView) {
            super(itemView);

            resturantImg = itemView.findViewById(R.id.idResturantImg);
            resturantName = itemView.findViewById(R.id.idResturantName);
            resturantLoc = itemView.findViewById(R.id.idResturantLocation);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem edit = menu.add(Menu.NONE, 3, 3, "Edit");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            edit.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                        case 3:
                            mListener.onEditClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);

        void onEditClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}