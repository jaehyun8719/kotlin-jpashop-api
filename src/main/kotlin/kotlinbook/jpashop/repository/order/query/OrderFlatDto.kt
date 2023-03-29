package kotlinbook.jpashop.repository.order.query

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import kotlinbook.jpashop.domain.Address
import kotlinbook.jpashop.domain.OrderStatus
import java.time.LocalDateTime

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderFlatDto(
    val orderId: Long,
    val name: String, //주문시간
    val orderDate: LocalDateTime,
    val orderStatus: OrderStatus,
    val address: Address,
    val itemName: String,
    val orderPrice: Int,
    val count: Int
)