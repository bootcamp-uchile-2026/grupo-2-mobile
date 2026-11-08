package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.data.database.CartItemDAO
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository
import cl.uchile.dcc.mobile.ecotienda.data.network.CartApiService
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.AddToCartRequest
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.UpdateQuantityRequest
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.toEntity

class CartRepository(
    private val cartItemDao: CartItemDAO,
    private val apiService: CartApiService = ApiRepository.cartApiService
) {
    suspend fun getCartItems(): List<CartItem> {
        return try {
            val cartDto = apiService.getCart()
            val remoteItems = cartDto.items.map { it.toEntity() }
            if (remoteItems.isNotEmpty()) {
                cartItemDao.clearCart()
                for (item in remoteItems) {
                    cartItemDao.addCartItem(item)
                }
                remoteItems
            } else {
                cartItemDao.getAllCartItems()
            }
        } catch (_: Exception) {
            cartItemDao.getAllCartItems()
        }
    }

    suspend fun addCartItem(item: CartItem) {
        val existing = cartItemDao.getCartItemByProductId(item.productId)
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + item.quantity)
            cartItemDao.updateCartItem(updated)
        } else {
            cartItemDao.addCartItem(item)
        }

        try {
            apiService.addToCart(AddToCartRequest(productId = item.productId, quantity = item.quantity))
        } catch (_: Exception) {
            // Persistido en Room
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
            try {
                apiService.updateQuantity(productId, UpdateQuantityRequest(quantity = quantity))
            } catch (_: Exception) {
                // Persistido en Room
            }
        }
    }

    suspend fun removeCartItem(productId: String) {
        cartItemDao.deleteByProductId(productId)
        try {
            apiService.removeFromCart(productId)
        } catch (_: Exception) {
            // Eliminado de Room
        }
    }

    suspend fun clearCart() {
        cartItemDao.clearCart()
        try {
            apiService.clearCart()
        } catch (_: Exception) {
            // Vaciado de Room
        }
    }
}
