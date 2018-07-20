package ateam.foodr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Food> mList;
    private FoodAdapter.OnItemClickListener mListener;

    //The storage reference so that the profile images can be stored on the FireBase
    private StorageReference mImageStorage;

    FoodAdapter(Context context, ArrayList<Food> list){
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_food_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Food foodItem = mList.get(position);

        ImageView image = holder.item_image;
        TextView name,desc;
        RatingBar  rate;

        name = holder.item_name;
        rate = holder.item_rate;
        desc = holder.item_desc;

        // TODO: Re-enable this
        //image.setImageResource(foodItem.getImage());
        String url = foodItem.getImageurl();
        rate.setRating(foodItem.getRate());
        mImageStorage = FirebaseStorage.getInstance().getReference();
        Picasso.with(image.getContext()).load(url).into(image);


        name.setText(foodItem.getName());
        //rate.setText(foodItem.getRate());

        if (SimplerLoginActivity.user.getUser_type().equals("admin"))
        {
            holder.item_rate.setEnabled(false);
        }
        desc.setText(foodItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        ImageView item_image, editButton, RemoveButton;
        TextView item_name, item_desc;
        RatingBar item_rate;

        public ViewHolder(View itemView){
            super(itemView);

            item_image = itemView.findViewById(R.id.idMFoodImg);
            item_name = itemView.findViewById(R.id.idMFoodName);
            item_desc = itemView.findViewById(R.id.idMFoodDesc);
            item_rate = itemView.findViewById(R.id.idMFoodratingBar);

            itemView.setOnClickListener(this);
            //itemView.setOnCreateContextMenuListener(this);

            editButton = itemView.findViewById(R.id.foodEditBtn);
            RemoveButton = itemView.findViewById(R.id.rmvFoodBtn);

            itemView.setOnClickListener(this);
            //itemView.setOnCreateContextMenuListener(this);
            editButton.setOnClickListener((View v) ->  mListener.onEditClick(getAdapterPosition()));

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to Delete?");

            builder.setPositiveButton("YES", (dialog, which) -> {
                dialog.dismiss();
                mListener.onDeleteClick(getAdapterPosition());
            });

            builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

            AlertDialog alert = builder.create();

            RemoveButton.setOnClickListener((View v) -> alert.show());

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

    public void setOnItemClickListener(FoodAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
