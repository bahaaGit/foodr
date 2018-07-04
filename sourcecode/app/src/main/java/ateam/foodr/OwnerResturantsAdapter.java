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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OwnerResturantsAdapter extends RecyclerView.Adapter<OwnerResturantsAdapter.OwnerResturantsViewHolder> {

    @NonNull
    private Context orContext;
    private ArrayList<Restaurant> data;
    private ArrayList<Restaurant> orList;
    private ItemClickListener clickListener;
    private OnItemClickListener mListener;


    //The storage reference so that the profile images can be stored on the FireBase
    public StorageReference mmImageStorage;

    public OwnerResturantsAdapter(Context context, ArrayList<Restaurant> list){
        this.orContext = context;
        orContext = context;
        orList = list;
    }

    @Override
    public OwnerResturantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(orContext);

        View view = layoutInflater.inflate(R.layout.owner_resturant_list_rows,parent,false);

        OwnerResturantsViewHolder viewHolder = new OwnerResturantsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OwnerResturantsViewHolder holder, int position) {

        Restaurant resturantItem = orList.get(position);

        ImageView image = holder.oResturantImg;
        TextView name,address;

        name = holder.oResturantName;
        address = holder.oResturantLoc;


        // TODO: Re-enable this after we fix it.
        name.setText(resturantItem.getName());
        address.setText(resturantItem.getAddress());

        // TODO: Re-enable this
        //image.setImageResource(foodItem.getImage());
        String url = resturantItem.getImageurl();

        mmImageStorage = FirebaseStorage.getInstance().getReference();
        if (url != null && !url.equals("empty")){
        Picasso.with(image.getContext()).load(url).into(image);
        }
    }

    @Override
    public int getItemCount() {
        return orList.size();
    }

    public class OwnerResturantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        ImageView oResturantImg;
        TextView oResturantName,oResturantLoc;
        public OwnerResturantsViewHolder(View itemView){
            super(itemView);

            oResturantImg = itemView.findViewById(R.id.idOResturantImg);
            oResturantName = itemView.findViewById(R.id.idOResturantName);
            oResturantLoc = itemView.findViewById(R.id.idOResturantLocation);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (clickListener != null)
                clickListener.onItemClick(view, getAdapterPosition());

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
    // convenience method for getting data at click position
    Restaurant getItem(int id)
    {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }



    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);

        void onEditClick(int position);

    }

    public void setOnItemClickListener(OwnerResturantsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}

/*
    private Context rContext;
    private ArrayList<Restaurant> rList;
    private ResturantsAdapter.OnItemClickListener mListener;

    //The storage reference so that the profile images can be stored on the FireBase
    private StorageReference mImageStorage;

    ResturantsAdapter(Context context, ArrayList<Restaurant> list){
        this.rContext = context;
        rContext = context;
        rList = list;
    }
    @Override
    public ResturantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(rContext);

        View view = layoutInflater.inflate(R.layout.resturant_item_row,parent,false);

        ResturantsAdapter.ViewHolder viewHolder = new ResturantsAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ResturantsAdapter.ViewHolder holder, int position) {
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

    public void setOnItemClickListener(ResturantsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}*/