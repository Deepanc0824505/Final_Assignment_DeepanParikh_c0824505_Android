package com.example.fa_deepanparikh_c0824505_android;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    private Context mContext;
    private List<Place> places;

    PlaceAdapter(Context context) {
        mContext = context;
        places = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Place place = places.get(position);
        holder.tvTitle.setText(place.getAddress());
        holder.tvDate.setText(place.getDate());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.rowFG.setBackgroundColor(place.getCategory().equalsIgnoreCase("Visited") ?
                    mContext.getColor(R.color.teal_200) : mContext.getColor(R.color.purple_200));
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setFavoritePlaces(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private CardView rowFG;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_loc_title);
            tvDate = itemView.findViewById(R.id.tv_saved_date);
            rowFG = itemView.findViewById(R.id.rowFG);
        }
    }
}