package ateam.foodr;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Food> mList;

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
        TextView name,desc,rate;

        name = holder.item_name;
        //rate = holder.item_rate;
        desc = holder.item_desc;

        image.setImageResource(foodItem.getImage());

        name.setText(foodItem.getName());
        //rate.setText(foodItem.getRate());
        desc.setText(foodItem.getDesc());


    }

//    @Override
//    public void onClick(View v) {
//
//        final Intent intent;
//        switch (){
//            case 0:
//                intent =  new Intent(mContext, FirstActivity.class);
//                break;
//
//            case 1:
//                intent =  new Intent(context, SecondActivity.class);
//                break;
//
//            default:
//                intent =  new Intent(context, DefaultActivity.class);
//                break;
//        }
//        mContext.startActivity(intent);
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView item_image;
        TextView item_name, item_desc,item_rate;

        public ViewHolder(View itemView){
            super(itemView);

            item_image = itemView.findViewById(R.id.idMFoodImg);
            item_name = itemView.findViewById(R.id.idMFoodName);
            item_desc = itemView.findViewById(R.id.idMFoodDesc);
            //item_rate = itemView.findViewById(R.id.idMFoodratingBar);
        }
    }
}
