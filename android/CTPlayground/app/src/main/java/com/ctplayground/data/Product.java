package com.ctplayground.data;

public class Product {
    public String id, name, brand, category, emoji, desc;
    public int price, originalPrice, discount, reviews;
    public double rating;
    public String[] tags;

    public Product(String id, String name, String brand, String category,
                   int price, int originalPrice, int discount,
                   double rating, int reviews, String emoji, String desc, String[] tags) {
        this.id            = id;
        this.name          = name;
        this.brand         = brand;
        this.category      = category;
        this.price         = price;
        this.originalPrice = originalPrice;
        this.discount      = discount;
        this.rating        = rating;
        this.reviews       = reviews;
        this.emoji         = emoji;
        this.desc          = desc;
        this.tags          = tags;
    }

    public String getFormattedPrice()         { return "₹" + String.format("%,d", price); }
    public String getFormattedOriginalPrice() { return "₹" + String.format("%,d", originalPrice); }
    public String getFormattedRating()        { return String.format("%.1f ★", rating); }
    public String getFormattedReviews()       {
        if (reviews >= 100000) return String.format("%.1fL", reviews / 100000.0);
        if (reviews >= 1000)   return String.format("%.1fK", reviews / 1000.0);
        return String.valueOf(reviews);
    }
    public String getFormattedSaving() {
        return "₹" + String.format("%,d", (originalPrice - price));
    }
}
