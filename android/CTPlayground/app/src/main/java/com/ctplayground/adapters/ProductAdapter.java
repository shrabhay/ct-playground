package com.ctplayground.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctplayground.R;
import com.ctplayground.data.Product;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddToCartClick(Product product);
        void onCartQtyChange(Product product, int newQty); // 0 = remove
        void onWishlistClick(Product product, boolean isCurrentlyWishlisted, int position);
    }

    private List<Product> products;
    private final OnProductClickListener listener;
    private Set<String> wishlistedIds = new HashSet<>();
    private Map<String, Integer> cartState = new HashMap<>(); // productId → qty

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener  = listener;
    }

    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    public void updateWishlistState(Set<String> ids) {
        this.wishlistedIds = ids;
        notifyDataSetChanged();
    }

    public void updateCartState(Map<String, Integer> state) {
        this.cartState = state;
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

        // Basic fields
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

        // Wishlist state
        boolean wishlisted = wishlistedIds.contains(p.id);
        holder.btnWishlist.setText(wishlisted ? "♥" : "♡");
        holder.btnWishlist.setTextColor(holder.itemView.getContext().getColor(
                wishlisted ? R.color.bazario_orange : R.color.bazario_dark));

        // Cart state — show button or qty selector
        int qty = cartState.containsKey(p.id) ? cartState.get(p.id) : 0;
        updateCartUI(holder, qty);

        // Click listeners
        holder.itemView.setOnClickListener(v -> listener.onProductClick(p));

        holder.btnWishlist.setOnClickListener(v -> {
            boolean currently = wishlistedIds.contains(p.id);
            if (currently) wishlistedIds.remove(p.id);
            else           wishlistedIds.add(p.id);
            notifyItemChanged(holder.getAdapterPosition());
            listener.onWishlistClick(p, currently, holder.getAdapterPosition());
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            cartState.put(p.id, 1);
            notifyItemChanged(holder.getAdapterPosition());
            listener.onAddToCartClick(p);
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int current = cartState.containsKey(p.id) ? cartState.get(p.id) : 0;
            int newQty = current + 1;
            cartState.put(p.id, newQty);
            notifyItemChanged(holder.getAdapterPosition());
            listener.onCartQtyChange(p, newQty);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int current = cartState.containsKey(p.id) ? cartState.get(p.id) : 0;
            int newQty = current - 1;
            if (newQty <= 0) {
                cartState.remove(p.id);
                newQty = 0;
            } else {
                cartState.put(p.id, newQty);
            }
            notifyItemChanged(holder.getAdapterPosition());
            listener.onCartQtyChange(p, newQty);
        });
    }

    private void updateCartUI(ViewHolder holder, int qty) {
        if (qty > 0) {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.layoutQty.setVisibility(View.VISIBLE);
            holder.tvQty.setText(String.valueOf(qty));
        } else {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.layoutQty.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return products.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvDiscount, tvRating, tvReviews,
                tvPrice, tvOriginalPrice, tvDiscountPercent;
        TextView btnWishlist, tvQty, btnIncrease, btnDecrease;
        Button btnAddToCart;
        LinearLayout layoutQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji          = itemView.findViewById(R.id.tvProductEmoji);
            tvName           = itemView.findViewById(R.id.tvProductName);
            tvDiscount       = itemView.findViewById(R.id.tvDiscount);
            tvRating         = itemView.findViewById(R.id.tvRating);
            tvReviews        = itemView.findViewById(R.id.tvReviews);
            tvPrice          = itemView.findViewById(R.id.tvPrice);
            tvOriginalPrice  = itemView.findViewById(R.id.tvOriginalPrice);
            tvDiscountPercent = itemView.findViewById(R.id.tvDiscountPercent);
            btnWishlist      = itemView.findViewById(R.id.btnWishlist);
            btnAddToCart     = itemView.findViewById(R.id.btnAddToCart);
            layoutQty        = itemView.findViewById(R.id.layoutQty);
            tvQty            = itemView.findViewById(R.id.tvQty);
            btnIncrease      = itemView.findViewById(R.id.btnIncrease);
            btnDecrease      = itemView.findViewById(R.id.btnDecrease);
        }
    }
}
