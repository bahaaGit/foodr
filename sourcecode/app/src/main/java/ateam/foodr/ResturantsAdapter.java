package ateam.foodr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResturantsAdapter extends RecyclerView.Adapter<ResturantsAdapter.ViewHolder> {

    private Context rContext;
    private ArrayList<Restaurant> rList;

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


        image.setImageResource(resturantItem.getImage());
        name.setText(resturantItem.getName());
        address.setText(resturantItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView resturantImg;
        TextView resturantName,resturantLoc;


        ViewHolder(View itemView) {
            super(itemView);

            resturantImg = itemView.findViewById(R.id.idResturantImg);
            resturantName = itemView.findViewById(R.id.idResturantName);
            resturantLoc = itemView.findViewById(R.id.idResturantLocation);

        }
    }
}