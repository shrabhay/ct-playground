package com.ctplayground.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctplayground.R;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }

    private final List<String[]> categories; // each entry: [emoji, name]
    private final OnCategoryClickListener listener;

    public CategoryAdapter(List<String[]> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener   = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] cat = categories.get(position);
        holder.tvIcon.setText(cat[0]);
        holder.tvName.setText(cat[1]);
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(cat[1]));
    }

    @Override
    public int getItemCount() { return categories.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tvCategoryIcon);
            tvName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
