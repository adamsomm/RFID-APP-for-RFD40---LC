package com.zebra.rfid.demo.sdksample;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private static ArrayList<ListData> listData;
    private static RecyclerViewClickListener listener;


    public RecyclerAdapter(ArrayList<ListData> listData, RecyclerViewClickListener listener) {
        RecyclerAdapter.listData = listData;
        RecyclerAdapter.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView RSSItext;
        private TextView TagIDtext;

        public MyViewHolder(final View itemView) {
            super(itemView);
            TagIDtext = itemView.findViewById(R.id.ListTagID);
            RSSItext = itemView.findViewById(R.id.ListRSSI);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
    public void addItem(String tagID, String rssi) {
        listData.add(new ListData(tagID, rssi));
        notifyItemInserted(listData.size() - 1);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        listData.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String tagID = listData.get(position).getTagID();
        String RSSI = listData.get(position).getRssi();
        holder.TagIDtext.setText(tagID);
        holder.RSSItext.setText(RSSI);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
