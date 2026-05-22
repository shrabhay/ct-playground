package com.ctplayground.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctplayground.R;
import java.util.List;
import java.util.Map;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    public interface WishlistItemListener {
        void onMoveToCart(Map<String, Object> item);
        void onRemove(Map<String, Object> item);
        void onItemClick(Map<String, Object> item);
    }

    private List<Map<String, Object>> items;
    private final WishlistItemListener listener;

    public WishlistAdapter(List<Map<String, Object>> items, WishlistItemListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    public void updateItems(List<Map<String, Object>> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Map<String, Object> item = items.get(position);

        h.tvEmoji.setText(String.valueOf(item.get("emoji")));
        h.tvName.setText(String.valueOf(item.get("name")));
        h.tvBrand.setText(String.valueOf(item.get("brand")));
        h.tvPrice.setText("₹" + String.format("%,d", toLong(item.get("price"))));
        h.tvOriginal.setText("₹" + String.format("%,d", toLong(item.get("originalPrice"))));
        h.tvOriginal.setPaintFlags(
                h.tvOriginal.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        h.tvDiscount.setText(toLong(item.get("discount")) + "% off");

        h.itemView.setOnClickListener(v -> listener.onItemClick(item));
        h.btnMoveToCart.setOnClickListener(v -> listener.onMoveToCart(item));
        h.btnRemove.setOnClickListener(v -> listener.onRemove(item));
    }

    private long toLong(Object val) {
        if (val instanceof Long)    return (Long) val;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Double)  return ((Double) val).longValue();
        return 0L;
    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvName, tvBrand, tvPrice, tvOriginal, tvDiscount;
        Button btnMoveToCart, btnRemove;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvEmoji      = v.findViewById(R.id.tvWishlistEmoji);
            tvName       = v.findViewById(R.id.tvWishlistName);
            tvBrand      = v.findViewById(R.id.tvWishlistBrand);
            tvPrice      = v.findViewById(R.id.tvWishlistPrice);
            tvOriginal   = v.findViewById(R.id.tvWishlistOriginal);
            tvDiscount   = v.findViewById(R.id.tvWishlistDiscount);
            btnMoveToCart = v.findViewById(R.id.btnMoveToCart);
            btnRemove    = v.findViewById(R.id.btnRemoveWishlist);
        }
    }
}
