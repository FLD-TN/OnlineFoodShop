package com.sinhvien.onlinefoodshop;

import com.sinhvien.onlinefoodshop.Model.CartModel; // Hoặc CartItem tùy theo model bạn dùng
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartModel> cartItems;
    private OnCartChangeListener cartChangeListener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    public void addToCart(CartModel item) {
        for (CartModel cartItem : cartItems) {
            if (cartItem.getProductID().equals(item.getProductID())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                notifyCartChanged();
                return;
            }
        }
        cartItems.add(item);
        notifyCartChanged();
    }

    public List<CartModel> getCartItems() {
        return cartItems;
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            notifyCartChanged();
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartModel item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
        notifyCartChanged();
    }

    private void notifyCartChanged() {
        if (cartChangeListener != null) {
            cartChangeListener.onCartChanged();
        }
    }
}