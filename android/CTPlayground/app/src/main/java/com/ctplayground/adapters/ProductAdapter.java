package com.ctplayground.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctplayground.R;
import com.ctplayground.data.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddToCartClick(Product product);
        void onWishlistClick(Product product);
    }

    private List<Product> products;
    private final OnProductClickListener listener;

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = products.get(position);

        holder.tvEmoji.setText(p.emoji);
        holder.tvName.setText(p.name);
        holder.tvDiscount.setText(p.discount + "% off");
        holder.tvRating.setText(p.getFormattedRating());
        holder.tvReviews.setText("(" + p.getFormattedReviews() + ")");
        holder.tvPrice.setText(p.getFormattedPrice());
        holder.tvOriginalPrice.setText(p.getFormattedOriginalPrice());
        holder.tvOriginalPrice.setPaintFlags(
                holder.tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvDiscountPercent.setText(p.discount + "% off");

        holder.itemView.setOnClickListener(v -> listener.onProductClick(p));
        holder.btnWishlist.setOnClickListener(v -> listener.onWishlistClick(p));
        holder.btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(p));
    }

    @Override
    public int getItemCount() { return products.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvDiscount, tvRating, tvReviews,
                tvPrice, tvOriginalPrice, tvDiscountPercent;
        TextView btnWishlist;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji           = itemView.findViewById(R.id.tvProductEmoji);
            tvName            = itemView.findViewById(R.id.tvProductName);
            tvDiscount        = itemView.findViewById(R.id.tvDiscount);
            tvRating          = itemView.findViewById(R.id.tvRating);
            tvReviews         = itemView.findViewById(R.id.tvReviews);
            tvPrice           = itemView.findViewById(R.id.tvPrice);
            tvOriginalPrice   = itemView.findViewById(R.id.tvOriginalPrice);
            tvDiscountPercent = itemView.findViewById(R.id.tvDiscountPercent);
            btnWishlist       = itemView.findViewById(R.id.btnWishlist);
            btnAddToCart      = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
