package com.moutamid.onlinestore.lister;

import com.moutamid.onlinestore.models.CartModel;

public interface CartListner {
    void onDeleteClick(CartModel cartModel, int pos);
}
