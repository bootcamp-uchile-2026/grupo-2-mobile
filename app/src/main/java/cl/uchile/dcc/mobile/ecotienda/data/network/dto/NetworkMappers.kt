package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.User

fun ProductListDto.toEntity(): Product = Product(
    id = id.toString(),
    productName = name,
    price = salePrice ?: price,
    description = name,
    productProducer = brand.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    stock = 0,
    category = "",
    producerID = "",
)

fun ProductDetailDto.toEntity(): Product = Product(
    id = id.toString(),
    productName = name,
    price = price,
    description = description.orEmpty(),
    productProducer = "",
    imageUrl = images?.firstOrNull().orEmpty(),
    stock = 0,
    category = if (!sizes.isNullOrEmpty()) "Moda Natural" else "",
    producerID = "",
)

fun ProducerDto.toEntity(): Producer = Producer(
    id = id.toString(),
    name = name,
    description = description.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    location = "",
    history = story.orEmpty(),
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
