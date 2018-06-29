package ateam.foodr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>
{

    private List<Restaurant> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public RestaurantAdapter(Context context, List<Restaurant> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.owner_restaurant_list_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Restaurant r = data.get(position);
        holder.nameTextbox.setText(r.getName());
        Log.d("RestaurantAdapter", r.getName());
    }

    // total number of rows
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView nameTextbox;

        ViewHolder(View itemView)
        {
            super(itemView);
            nameTextbox = itemView.findViewById(R.id.nameTextbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (clickListener != null)
                clickListener.onItemClick(view, getAdapterPosition());
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
}