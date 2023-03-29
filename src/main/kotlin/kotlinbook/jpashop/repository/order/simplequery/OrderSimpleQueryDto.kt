package kotlinbook.jpashop.repository.order.simplequery

import kotlinbook.jpashop.domain.Address
import kotlinbook.jpashop.domain.OrderStatus
import java.time.LocalDateTime

data class OrderSimpleQueryDto(
    val orderId: Long,
    val name: String, //주문시간
    val orderDate: LocalDateTime,
    val orderStatus: OrderStatus,
    val address: Address,
)