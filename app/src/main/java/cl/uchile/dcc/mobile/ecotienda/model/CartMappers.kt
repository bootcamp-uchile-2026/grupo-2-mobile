package cl.uchile.dcc.mobile.ecotienda.model

fun Product.toCartItem(quantity: Int = 1) = CartItem(
    productId = id,
    productName = productName,
    price = price,
    imageUrl = imageUrl,
    producerID = producerID,
    productProducer = productProducer,
    quantity = quantity
)