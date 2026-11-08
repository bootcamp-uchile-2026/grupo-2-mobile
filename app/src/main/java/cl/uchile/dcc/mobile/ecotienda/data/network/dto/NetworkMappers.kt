package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.User

fun ProductDto.toEntity(): Product = Product(
    id = id,
    productName = productName,
    price = price,
    description = description,
    productProducer = productProducer,
    imageUrl = imageUrl,
    stock = stock,
    category = category,
    producerID = producerId
)

fun ProducerDto.toEntity(): Producer = Producer(
    id = id,
    name = producerName,
    description = producerDescription,
    imageUrl = producerImageUrl,
    location = producerLocation,
    history = producerHistory
)

fun UserDto.toEntity(): User = User(
    id = id.toLongOrNull() ?: 0L,
    email = email,
    password = password,
    name = if (firstName.isNotBlank()) "$firstName $lastName".trim() else username
)

fun CartItemDto.toEntity(): CartItem = CartItem(
    productId = productId,
    productName = productName,
    price = price,
    imageUrl = imageUrl ?: "",
    producerID = producerId ?: "",
    productProducer = productProducer ?: "",
    quantity = quantity
)
