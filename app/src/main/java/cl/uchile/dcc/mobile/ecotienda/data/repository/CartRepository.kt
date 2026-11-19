package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.data.database.CartItemDAO

class CartRepository(
    private val cartItemDao: CartItemDAO,
) {
    suspend fun getCartItems(): List<CartItem> {
        return cartItemDao.getAllCartItems()
    }

    suspend fun addCartItem(item: CartItem, maxQuantity: Int = MAX_UNITS_PER_PRODUCT): Int {
        val cap = maxQuantity.coerceIn(0, MAX_UNITS_PER_PRODUCT)
        if (cap <= 0) return 0
        val existing = cartItemDao.getCartItemByProductId(item.productId)
        val merged = (existing?.quantity ?: 0) + item.quantity.coerceAtLeast(1)
        val stored = merged.coerceAtMost(cap)
        if (existing != null) {
            cartItemDao.updateCartItem(
                existing.copy(
                    quantity = stored,
                    price = item.price,
                    productName = item.productName,
                    imageUrl = item.imageUrl,
                    productProducer = item.productProducer,
                )
            )
        } else {
            cartItemDao.addCartItem(item.copy(quantity = stored))
        }
        return stored
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

    companion object {
        const val MAX_UNITS_PER_PRODUCT = 3
    }
}
