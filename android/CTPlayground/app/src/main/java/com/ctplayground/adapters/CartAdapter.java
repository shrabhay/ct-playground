package com.ctplayground.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ctplayground.R;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface CartItemListener {
        void onQtyChange(Map<String, Object> item, int newQty);
        void onRemoveItem(Map<String, Object> item);
    }

    private List<Map<String, Object>> items;
    private final CartItemListener listener;

    public CartAdapter(List<Map<String, Object>> items, CartItemListener listener) {
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
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Map<String, Object> item = items.get(position);

        String name  = (String) item.get("name");
        String brand = (String) item.get("brand");
        String emoji = (String) item.get("emoji");

        long price         = toLong(item.get("price"));
        long originalPrice = toLong(item.get("originalPrice"));
        long discount      = toLong(item.get("discount"));
        int  qty           = (int) toLong(item.get("qty"));

        h.tvEmoji.setText(emoji);
        h.tvName.setText(name);
        h.tvBrand.setText(brand);
        h.tvPrice.setText("₹" + String.format("%,d", price));
        h.tvOriginalPrice.setText("₹" + String.format("%,d", originalPrice));
        h.tvOriginalPrice.setPaintFlags(
                h.tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        h.tvDiscount.setText(discount + "% off");
        h.tvQty.setText(String.valueOf(qty));
        h.tvSubtotal.setText("₹" + String.format("%,d", price * qty));

        h.btnIncrease.setOnClickListener(v -> listener.onQtyChange(item, qty + 1));
        h.btnDecrease.setOnClickListener(v -> {
            if (qty > 1) listener.onQtyChange(item, qty - 1);
            else         listener.onRemoveItem(item);
        });
        h.btnRemove.setOnClickListener(v -> listener.onRemoveItem(item));
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
        TextView tvEmoji, tvName, tvBrand, tvPrice, tvOriginalPrice,
                tvDiscount, tvQty, tvSubtotal, btnIncrease, btnDecrease, btnRemove;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvEmoji         = v.findViewById(R.id.tvCartEmoji);
            tvName          = v.findViewById(R.id.tvCartName);
            tvBrand         = v.findViewById(R.id.tvCartBrand);
            tvPrice         = v.findViewById(R.id.tvCartPrice);
            tvOriginalPrice = v.findViewById(R.id.tvCartOriginalPrice);
            tvDiscount      = v.findViewById(R.id.tvCartDiscount);
            tvQty           = v.findViewById(R.id.tvCartQty);
            tvSubtotal      = v.findViewById(R.id.tvCartSubtotal);
            btnIncrease     = v.findViewById(R.id.btnCartIncrease);
            btnDecrease     = v.findViewById(R.id.btnCartDecrease);
            btnRemove       = v.findViewById(R.id.btnRemoveItem);
        }
    }
}
