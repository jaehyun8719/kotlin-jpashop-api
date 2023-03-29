package kotlinbook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinbook.jpashop.api.OrderApiController
import kotlinbook.jpashop.domain.item.Item
import javax.persistence.*

@Entity
class OrderItem(
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    var id: Long? = null,

    var orderPrice: Int = 0, //주문 가격

    var count: Int = 0, //주문 수량
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item: Item? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null

    fun toDto(): OrderApiController.OrderItemDto{
        return OrderApiController.OrderItemDto(
            itemName = this.item?.name,
            orderPrice = this.orderPrice,
            count = this.count,
        )
    }

    //==비즈니스 로직==//
    fun cancel() {
        this.item?.addStock(this.count)
    }

    //==조회 로직==//
    val totalPrice: Int
        /**
         * 주문상품 전체 가격 조회
         */
        get() = this.orderPrice * this.count

    //==생성 메서드==//
    fun createOrderItem(item: Item, orderPrice: Int, count: Int): OrderItem {
        val orderItem = OrderItem(
            orderPrice = orderPrice,
            count = count,
        )
        orderItem.item = item
        item.removeStock(count)
        return orderItem
    }

}