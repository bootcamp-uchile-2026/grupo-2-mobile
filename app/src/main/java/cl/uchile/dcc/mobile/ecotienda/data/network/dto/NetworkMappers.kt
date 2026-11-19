package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import cl.uchile.dcc.mobile.ecotienda.data.catalog.ProductCategories
import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.User
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository

fun ProductListDto.toEntity(): Product = Product(
    id = id.toString(),
    productName = name,
    price = finalPrice,
    listPrice = price,
    discountPercentage = discountPercentage,
    description = name,
    productProducer = producerName.orEmpty(),
    imageUrl = ApiRepository.fileContentUrl(primaryImageFileId),
    stock = stock,
    category = ProductCategories.nameOf(categoryId),
    producerID = producerId.toString(),
    co2Avoided = co2Avoided,
    greenPoints = greenPoints,
    available = available && stock > 0,
)

fun ProductDetailDto.toEntity(): Product = Product(
    id = id.toString(),
    productName = name,
    price = finalPrice,
    listPrice = price,
    discountPercentage = discountPercentage,
    description = description.orEmpty(),
    productProducer = producerName.orEmpty(),
    imageUrl = ApiRepository.fileContentUrl(primaryImageFileId ?: imageFileIds?.firstOrNull()),
    stock = stock,
    category = categoryName?.takeIf { it.isNotBlank() } ?: ProductCategories.nameOf(categoryId),
    producerID = producerId.toString(),
    co2Avoided = co2Avoided,
    greenPoints = greenPoints,
    available = available && stock > 0,
)

fun ProducerDto.toEntity(): Producer = Producer(
    id = id.toString(),
    name = businessName,
    description = description.orEmpty(),
    imageUrl = ApiRepository.fileContentUrl(logoFileId),
    location = location.orEmpty(),
    history = "",
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
