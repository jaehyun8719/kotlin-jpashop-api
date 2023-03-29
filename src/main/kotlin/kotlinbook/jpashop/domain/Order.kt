package kotlinbook.jpashop.domain

import kotlinbook.jpashop.api.OrderApiController
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    var id: Long? = null,

    var orderDate: LocalDateTime? = null, //주문시간

    @Enumerated(EnumType.STRING)
    var status: OrderStatus? = null, //주문상태 [ORDER, CANCEL]
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    var orderItems: MutableList<OrderItem> = ArrayList()

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "delivery_id")
    var delivery: Delivery? = null

    //==연관관계 메서드==//
    fun addOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
        orderItem.order = this
    }

    fun toDto(): OrderApiController.OrderDto {
        return OrderApiController.OrderDto(
            orderId = this.id,
            name = this.member?.name,
            orderDate = this.orderDate, //주문시간
            orderStatus = this.status,
            address = this.delivery,
            orderItems = this.orderItems.map { it.toDto() }
        )
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    fun cancel() {
        check(delivery!!.status !== DeliveryStatus.COMP) { "이미 배송완료된 상품은 취소가 불가능합니다." }
        this.status = OrderStatus.CANCEL
        for (orderItem in orderItems) {
            orderItem.cancel()
        }
    }

    //==조회 로직==//
    val totalPrice: Int
        /**
         * 전체 주문 가격 조회
         */
        get() {
            var totalPrice = 0
            for (orderItem in orderItems) {
                totalPrice += orderItem.totalPrice
            }
            return totalPrice
        }

    //==생성 메서드==//
    fun createOrder(member: Member, delivery: Delivery, vararg orderItems: OrderItem): Order {
        val order = Order()
        this.member = member
        member.orders.add(this)

        this.delivery = delivery
        delivery.order = this

        for (orderItem in orderItems) {
            order.addOrderItem(orderItem)
        }
        order.status = OrderStatus.ORDER
        order.orderDate = LocalDateTime.now()
        return order
    }
}