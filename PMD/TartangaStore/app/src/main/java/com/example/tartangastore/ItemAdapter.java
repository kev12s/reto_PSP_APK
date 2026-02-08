package com.example.tartangastore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tartangastore.model.Apk;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private List<Apk> apkList;
    private OnItemClickListener listener;

    // Interfaz SOLO con onItemClick
    public interface OnItemClickListener {
        void onItemClick(Apk apk);
    }

    // Constructor
    public ItemAdapter(Context context, List<Apk> apkList, OnItemClickListener listener) {
        this.context = context;
        this.apkList = apkList;
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
        Apk apk = apkList.get(position);

        // Configurar los datos del APK
        holder.titleView.setText(apk.getNombre());
        holder.descriptionView.setText(apk.getDescripcion());

        // Cargar imagen usando Glide directamente desde la URL
        String imageUrl = "http://192.168.41.128:8080/apks/imagenAPK/" + apk.getId();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

        // Configurar clic
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(apk);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apkList != null ? apkList.size() : 0;
    }

    public void updateData(List<Apk> newApkList) {
        apkList = newApkList;
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
            titleView = itemView.findViewById(R.id.itemTitle);
            descriptionView = itemView.findViewById(R.id.itemDescription);
        }
    }
}