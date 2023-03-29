package kotlinbook.jpashop.repository.order.query

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kotlinbook.jpashop.domain.Address
import kotlinbook.jpashop.domain.OrderStatus
import java.time.LocalDateTime

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderQueryDto (
    val orderId: Long,
    val name: String,
    val orderDate: LocalDateTime, //주문시간
    val orderStatus: OrderStatus,
    val address: Address,
    var orderItems: List<OrderItemQueryDto>? = null,
)