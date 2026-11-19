package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.data.database.Cart
import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.ui.component.CartProductRow
import cl.uchile.dcc.mobile.ecotienda.ui.component.CheckoutActionButton
import cl.uchile.dcc.mobile.ecotienda.ui.component.CheckoutCard
import cl.uchile.dcc.mobile.ecotienda.ui.component.CheckoutDivider
import cl.uchile.dcc.mobile.ecotienda.ui.component.CheckoutScreenTitle
import cl.uchile.dcc.mobile.ecotienda.ui.component.CouponSection
import cl.uchile.dcc.mobile.ecotienda.ui.component.OrderSummarySection
import cl.uchile.dcc.mobile.ecotienda.ui.component.PaymentSelector
import cl.uchile.dcc.mobile.ecotienda.ui.component.ShippingForm
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.AddressCheckStatus
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CheckoutStep
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ShippingFormState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.shippingCostFor
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CartViewModel

// Cart: carrito, envío y compra. Home o ficha de producto -> carrito.
@Composable
fun Cart(
    modifier: Modifier,
    cart: Cart,
    onBack: () -> Unit,
    onContinueShopping: () -> Unit = onBack,
    productCategories: Map<String, String> = emptyMap(),
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
) {
    val authUi by authViewModel.state.collectAsState()
    val checkoutState by cartViewModel.checkoutState.collectAsState()
    val scrollState = rememberScrollState()

    val shipping = shippingCostFor(
        subtotal = cart.total,
        deliveryType = checkoutState.shippingForm.deliveryType,
    )
    val discount = checkoutState.discountAmount

    BackHandler(
        enabled = checkoutState.orderPlaced || checkoutState.currentStep != CheckoutStep.CART,
    ) {
        if (checkoutState.orderPlaced) cartViewModel.dismissConfirmedOrder()
        else cartViewModel.previousStep()
    }

    LaunchedEffect(Unit) {
        if (!authUi.isLoggedIn) {
            authViewModel.requestLoginSheet()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
    ) {
        if (!checkoutState.orderPlaced && checkoutState.currentStep != CheckoutStep.CART) {
            TextButton(
                onClick = { cartViewModel.previousStep() },
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Volver")
            }
        }

        when {
            checkoutState.orderPlaced -> OrderPlacedContent(
                deliveryType = checkoutState.shippingForm.deliveryType,
                onContinue = {
                    cartViewModel.dismissConfirmedOrder()
                    onContinueShopping()
                },
            )
            cart.items.isEmpty() -> {
                CheckoutScreenTitle("Tu Carrito")
                EmptyCartMessage()
            }
            else -> when (checkoutState.currentStep) {
                CheckoutStep.CART -> CartStepContent(
                    items = cart.items,
                    productCategories = productCategories,
                    subtotal = cart.total,
                    shipping = shipping,
                    discount = discount,
                    couponCode = checkoutState.couponCode,
                    couponFeedback = checkoutState.couponFeedback,
                    onQuantityChange = cartViewModel::setQuantity,
                    onDecrement = cartViewModel::decrementQuantity,
                    onRemove = cartViewModel::removeFromCart,
                    onCouponChange = cartViewModel::updateCouponCode,
                    onApplyCoupon = { cartViewModel.applyCoupon(cart.total) },
                    onContinue = { cartViewModel.nextStep() },
                )
                CheckoutStep.SHIPPING -> ShippingStepContent(
                    form = checkoutState.shippingForm,
                    subtotal = cart.total,
                    shipping = shipping,
                    discount = discount,
                    addressCheck = checkoutState.addressCheck,
                    addressMessage = checkoutState.addressCheckMessage,
                    onUpdate = { newState -> cartViewModel.updateShippingForm { newState } },
                    onContinue = { cartViewModel.submitShipping() },
                )
                CheckoutStep.PAYMENT -> PaymentStepContent(
                    form = checkoutState.shippingForm,
                    paymentMethod = checkoutState.paymentMethod,
                    subtotal = cart.total,
                    shipping = shipping,
                    discount = discount,
                    mapLabel = checkoutState.addressCheckMessage.takeIf {
                        checkoutState.addressCheck == AddressCheckStatus.FOUND
                    }.orEmpty(),
                    onSelectMethod = cartViewModel::selectPaymentMethod,
                    onConfirm = { cartViewModel.confirmPurchase() },
                )
            }
        }
    }
}

@Composable
private fun CartStepContent(
    items: List<CartItem>,
    productCategories: Map<String, String>,
    subtotal: Int,
    shipping: Int,
    discount: Int,
    couponCode: String,
    couponFeedback: String,
    onQuantityChange: (String, Int) -> Unit,
    onDecrement: (String) -> Unit,
    onRemove: (String) -> Unit,
    onCouponChange: (String) -> Unit,
    onApplyCoupon: () -> Unit,
    onContinue: () -> Unit,
) {
    CheckoutScreenTitle("Tu Carrito")

    val groups = items.groupBy { item ->
        cartGroupTitle(productCategories[item.productId].orEmpty())
    }
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        groups.forEach { (title, groupItems) ->
            CheckoutCard(title = title) {
                groupItems.forEachIndexed { index, item ->
                    if (index > 0) CheckoutDivider()
                    CartProductRow(
                        item = item,
                        subtitle = item.productProducer,
                        onQuantityChange = { qty -> onQuantityChange(item.productId, qty) },
                        onDecrement = { onDecrement(item.productId) },
                        onRemove = { onRemove(item.productId) },
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    CouponSection(
        code = couponCode,
        feedback = couponFeedback,
        applied = discount > 0,
        onCodeChange = onCouponChange,
        onApply = onApplyCoupon,
    )
    Spacer(modifier = Modifier.height(22.dp))
    OrderSummarySection(
        productTotal = subtotal,
        shipping = shipping,
        discount = discount,
    )
    Spacer(modifier = Modifier.height(20.dp))
    CheckoutActionButton(
        label = "Ir a pagar",
        onClick = onContinue,
    )
}

@Composable
private fun ShippingStepContent(
    form: ShippingFormState,
    subtotal: Int,
    shipping: Int,
    discount: Int,
    addressCheck: AddressCheckStatus,
    addressMessage: String,
    onUpdate: (ShippingFormState) -> Unit,
    onContinue: () -> Unit,
) {
    var error by remember { mutableStateOf<String?>(null) }
    val checking = addressCheck == AddressCheckStatus.CHECKING
    val canContinueAnyway = addressCheck == AddressCheckStatus.NOT_FOUND ||
        addressCheck == AddressCheckStatus.UNAVAILABLE

    CheckoutScreenTitle(
        text = "Tu Envío",
        subtitle = "¿A dónde enviamos tu pedido?",
    )
    ShippingForm(
        state = form,
        onUpdate = {
            error = null
            onUpdate(it)
        },
    )
    if (!error.isNullOrBlank()) {
        Text(
            text = error.orEmpty(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
    if (canContinueAnyway && addressMessage.isNotBlank()) {
        Text(
            text = addressMessage,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
    Spacer(modifier = Modifier.height(22.dp))
    OrderSummarySection(
        productTotal = subtotal,
        shipping = shipping,
        discount = discount,
    )
    Spacer(modifier = Modifier.height(20.dp))
    CheckoutActionButton(
        label = when {
            checking -> "Revisando dirección…"
            canContinueAnyway -> "Continuar de todos modos"
            else -> "Continuar a la compra"
        },
        enabled = !checking,
        onClick = {
            if (form.isComplete()) {
                error = null
                onContinue()
            } else if (
                form.deliveryType != ShippingFormState.PICKUP &&
                form.street.isNotBlank() &&
                (form.isNoNumber || form.number.isNotBlank())
            ) {
                error = "Elige una comuna de la lista"
            } else {
                error = "Completa la dirección para continuar"
            }
        },
    )
}

@Composable
private fun PaymentStepContent(
    form: ShippingFormState,
    paymentMethod: String,
    subtotal: Int,
    shipping: Int,
    discount: Int,
    mapLabel: String,
    onSelectMethod: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    CheckoutScreenTitle(
        text = "Tu Compra",
        subtitle = "Revisa el total y elige cómo pagar",
    )
    val deliverySummary = form.summaryLine()
    if (deliverySummary.isNotBlank()) {
        CheckoutCard(title = "Entrega") {
            Text(
                text = deliverySummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (form.deliveryInstructions.isNotBlank() && form.deliveryType != ShippingFormState.PICKUP) {
                Text(
                    text = form.deliveryInstructions,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (mapLabel.isNotBlank()) {
                Text(
                    text = "En el mapa: $mapLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
    PaymentSelector(
        selectedMethod = paymentMethod,
        onSelect = onSelectMethod,
    )
    Spacer(modifier = Modifier.height(22.dp))
    OrderSummarySection(
        productTotal = subtotal,
        shipping = shipping,
        discount = discount,
    )
    Spacer(modifier = Modifier.height(20.dp))
    CheckoutActionButton(
        label = "Confirmar compra",
        enabled = paymentMethod.isNotBlank(),
        onClick = onConfirm,
    )
}

@Composable
private fun OrderPlacedContent(
    deliveryType: String,
    onContinue: () -> Unit,
) {
    CheckoutScreenTitle(
        text = "Listo",
        subtitle = "Tu compra fue confirmada",
    )
    CheckoutCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp),
            )
            Text(
                text = if (deliveryType == ShippingFormState.PICKUP) {
                    "Tu pedido queda listo para retiro."
                } else {
                    "Te enviaremos el pedido a la dirección que indicaste."
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            cart.items.isEmpty() -> {
                CheckoutScreenTitle("Tu Carrito")
                EmptyCartMessage()
            }
            else -> when (checkoutState.currentStep) {
                CheckoutStep.CART -> CartStepContent(
                    items = cart.items,
                    productCategories = productCategories,
                    quantityCaps = quantityCaps,
                    subtotal = cart.total,
                    shipping = shipping,
                    co2Kg = co2Kg,
                    greenPoints = greenPoints,
                    canPurchase = canPurchase,
                    onQuantityChange = cartViewModel::setQuantity,
                    onDecrement = cartViewModel::decrementQuantity,
                    onRemove = cartViewModel::removeFromCart,
                    onContinue = { cartViewModel.nextStep() },
                )
                CheckoutStep.SHIPPING -> ShippingStepContent(
                    form = checkoutState.shippingForm,
                    subtotal = cart.total,
                    shipping = shipping,
                    co2Kg = co2Kg,
                    greenPoints = greenPoints,
                    addressCheck = checkoutState.addressCheck,
                    addressMessage = checkoutState.addressCheckMessage,
                    onUpdate = { newState -> cartViewModel.updateShippingForm { newState } },
                    onContinue = { cartViewModel.submitShipping() },
                )
                CheckoutStep.PAYMENT -> PaymentStepContent(
                    form = checkoutState.shippingForm,
                    paymentMethod = checkoutState.paymentMethod,
                    subtotal = cart.total,
                    shipping = shipping,
                    co2Kg = co2Kg,
                    greenPoints = greenPoints,
                    canPurchase = canPurchase,
                    mapLabel = checkoutState.addressCheckMessage.takeIf {
                        checkoutState.addressCheck == AddressCheckStatus.FOUND
                    }.orEmpty(),
                    onSelectMethod = cartViewModel::selectPaymentMethod,
                    onConfirm = { cartViewModel.confirmPurchase() },
                )
            }
        }
    }
}

@Composable
private fun CartStepContent(
    items: List<CartItem>,
    productCategories: Map<String, String>,
    quantityCaps: Map<String, Int>,
    subtotal: Int,
    shipping: Int,
    co2Kg: Double,
    greenPoints: Int,
    canPurchase: Boolean,
    onQuantityChange: (String, Int) -> Unit,
    onDecrement: (String) -> Unit,
    onRemove: (String) -> Unit,
    onContinue: () -> Unit,
) {
    CheckoutScreenTitle("Tu Carrito")

    val groups = items.groupBy { item ->
        cartGroupTitle(productCategories[item.productId].orEmpty())
    }
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        groups.forEach { (title, groupItems) ->
            CheckoutCard(title = title.uppercase()) {
                groupItems.forEachIndexed { index, item ->
                    if (index > 0) CheckoutDivider()
                    CartProductRow(
                        item = item,
                        subtitle = item.productProducer,
                        maxQuantity = quantityCaps[item.productId] ?: 3,
                        onQuantityChange = { qty -> onQuantityChange(item.productId, qty) },
                        onDecrement = { onDecrement(item.productId) },
                        onRemove = { onRemove(item.productId) },
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    CheckoutActionButton(
        label = "Seguir comprando",
        onClick = onContinue,
    )
}

@Composable
private fun EmptyCartMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cuando agregues productos, vas a verlos agrupados aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
    PaymentSelector(
        selectedMethod = paymentMethod,
        onSelect = onSelectMethod,
    )
    Spacer(modifier = Modifier.height(22.dp))
    Text(
        text = "Huella Verde total",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(10.dp))
    FootprintCards(co2Kg = co2Kg, greenPoints = greenPoints)
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Los puntos se acreditan al recibir el pedido. Todavía no descuentan el total.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(22.dp))
    OrderSummarySection(
        productTotal = subtotal,
        shipping = shipping,
        discount = 0,
    )
    Spacer(modifier = Modifier.height(20.dp))
    CheckoutActionButton(
        label = "Confirmar compra",
        enabled = canPurchase && paymentMethod.isNotBlank(),
        onClick = onConfirm,
    )
}

@Composable
private fun OrderPlacedContent(
    onContinue: () -> Unit,
) {
    CheckoutScreenTitle(
        text = "Listo",
        subtitle = "Tu compra fue confirmada",
    )
    CheckoutCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp),
            )
            Text(
                text = "Te enviaremos el pedido a la dirección que indicaste. Los puntos verdes se acreditan cuando lo recibes.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    CheckoutActionButton(
        label = "Seguir comprando",
        onClick = onContinue,
    )
}

@Composable
private fun EmptyCartMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cuando agregues productos, vas a verlos agrupados aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

private fun cartGroupTitle(category: String): String = when (category.trim()) {
    "Moda Natural", "Ropa", "Ropa y Accesorios" -> "Ropa y Accesorios"
    "Cuidado del Hogar", "Limpieza del Hogar" -> "Limpieza del Hogar"
    "" -> "Otros"
    else -> category
}

private fun cartGroupTitle(category: String): String = when (category.trim()) {
    "Moda Natural", "Ropa", "Ropa y Accesorios" -> "Ropa y Accesorios"
    "Cuidado del Hogar", "Limpieza del Hogar" -> "Limpieza del Hogar"
    "" -> "Otros"
    else -> category
}

