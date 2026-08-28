package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.model.Cart
import cl.uchile.dcc.mobile.ecotienda.ui.component.*
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CheckoutStep
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Sage
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CartViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@Composable
fun Cart(
    modifier: Modifier,
    cart: Cart,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val ui by authViewModel.state.collectAsState()
    val authUi by authViewModel.state.collectAsState()
    val checkoutState by cartViewModel.checkoutState.collectAsState()

    // Para realizar scroll por la pantalla
    val scrollState = rememberScrollState()


    BackHandler(enabled = checkoutState.currentStep != CheckoutStep.CART) {
        cartViewModel.previousStep()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FigureIconButton(
                label = "Volver",
                callBack = {
                    if (checkoutState.currentStep == CheckoutStep.CART) onBack()
                    else cartViewModel.previousStep()
                },
                icon = FeatherIcons.ArrowLeft,
                enabled = true,
            )
            
            Text(
                text = when(checkoutState.currentStep) {
                    CheckoutStep.CART -> "1. Carrito"
                    CheckoutStep.SHIPPING -> "2. Envío"
                    CheckoutStep.PAYMENT -> "3. Pago"
                },
                style = MaterialTheme.typography.labelLarge,
                color = Sage
            )
        }

        Text(
            text = when(checkoutState.currentStep) {
                CheckoutStep.CART -> "Resumen de tu compra"
                CheckoutStep.SHIPPING -> "Datos de Envío"
                CheckoutStep.PAYMENT -> "Método de Pago"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (cart.items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                when (checkoutState.currentStep) {
                    CheckoutStep.CART -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(cart.items) { item ->
                                CartItemRow(
                                    item = item,
                                    onIncrement = { cartViewModel.incrementQuantity(item.productId) },
                                    onDecrement = { cartViewModel.decrementQuantity(item.productId) },
                                    onRemove = { cartViewModel.removeFromCart(item.productId) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Total:", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = "$${cart.total}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Sage,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = { cartViewModel.nextStep() },
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Sage),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Continuar al Envío", color = Color.White)
                        }
                    }

                    CheckoutStep.SHIPPING -> {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            item {
                                ShippingForm(
                                    state = checkoutState.shippingForm,
                                    onUpdate = { newState -> 
                                        cartViewModel.updateShippingForm { newState } 
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OrderSummaryCard(
                                    subtotal = cart.total,
                                    discount = checkoutState.discountAmount,
                                    total = cart.total - checkoutState.discountAmount,
                                    onConfirm = { cartViewModel.nextStep() },
                                    confirmLabel = "Continuar al Pago"
                                )
                            }
                        }
                    }

                    CheckoutStep.PAYMENT -> {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            item {
                                PaymentSelector(
                                    selectedMethod = checkoutState.paymentMethod,
                                    onSelect = { cartViewModel.selectPaymentMethod(it) }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OrderSummaryCard(
                                    subtotal = cart.total,
                                    discount = checkoutState.discountAmount,
                                    total = cart.total - checkoutState.discountAmount,
                                    onConfirm = { /* Finalizar compra */ },
                                    confirmLabel = "Confirmar y Pagar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

