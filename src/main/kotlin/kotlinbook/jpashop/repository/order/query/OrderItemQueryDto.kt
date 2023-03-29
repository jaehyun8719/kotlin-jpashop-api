package kotlinbook.jpashop.repository.order.query

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderItemQueryDto(
    @JsonIgnore
    val orderId: Long, //주문번호
    val itemName: String, //상품 명 //주문 가격
    val orderPrice: Int,  //주문 가격
    val count: Int //주문 수량
)