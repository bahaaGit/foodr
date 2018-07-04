package ateam.foodr;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
            holder.item_rate.setVisibility(View.INVISIBLE);
        }
        desc.setText(foodItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView item_image;
        TextView item_name, item_desc;
        RatingBar item_rate;

        public ViewHolder(View itemView){
            super(itemView);

            item_image = itemView.findViewById(R.id.idMFoodImg);
            item_name = itemView.findViewById(R.id.idMFoodName);
            item_desc = itemView.findViewById(R.id.idMFoodDesc);
            item_rate = itemView.findViewById(R.id.idMFoodratingBar);
        }
    }
}
