package com.example.tartangastore;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private OnItemClickListener listener;

    // Interfaz para manejar clics
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor
    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    // Constructor con listener
    public ItemAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = items.get(position);

        holder.titleView.setText(item.getTitle());
        holder.descriptionView.setText(item.getDescription());
        holder.imageView.setImageResource(item.getImageResId());

        // Configurar click listener si existe
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Método para actualizar datos
    public void updateData(List<Item> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    // ViewHolder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
           // titleView = itemLayout.findViewById(R.id.itemTitle);
            descriptionView = itemView.findViewById(R.id.itemDescription);
        }
    }
}