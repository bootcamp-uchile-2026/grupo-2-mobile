package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.data.database.CartItemDAO

class CartRepository(
    private val cartItemDao: CartItemDAO
) {
    suspend fun getCartItems(): List<CartItem> {
        return cartItemDao.getAllCartItems()
    }

    suspend fun addCartItem(item: CartItem) {
        val existing = cartItemDao.getCartItemByProductId(item.productId)
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + item.quantity)
            cartItemDao.updateCartItem(updated)
        } else {
            cartItemDao.addCartItem(item)
        }
    }

    suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeCartItem(productId)
        } else {
            val existing = cartItemDao.getCartItemByProductId(productId)
            if (existing != null) {
                cartItemDao.updateCartItem(existing.copy(quantity = quantity))
            }
        }
    }

    suspend fun removeCartItem(productId: String) {
        cartItemDao.deleteByProductId(productId)
    }

    suspend fun clearCart() {
        cartItemDao.clearCart()
    }
}
