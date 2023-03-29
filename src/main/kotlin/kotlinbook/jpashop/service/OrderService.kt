package kotlinbook.jpashop.service

import kotlinbook.jpashop.domain.*
import kotlinbook.jpashop.domain.item.Item
import kotlinbook.jpashop.repository.ItemRepository
import kotlinbook.jpashop.repository.MemberRepository
import kotlinbook.jpashop.repository.OrderRepository
import kotlinbook.jpashop.repository.OrderSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderService(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val itemRepository: ItemRepository,
) {
    /**
     * 주문
     */
    @Transactional
    fun order(memberId: Long, itemId: Long, count: Int): Long? {

        //엔티티 조회
        val member: Member = memberRepository.findOne(memberId)
        val item: Item = itemRepository.findOne(itemId)

        //배송정보 생성
        val delivery = Delivery(
            address = member.address,
            status = DeliveryStatus.READY,
        )

        //주문상품 생성
        val orderItem: OrderItem = OrderItem().createOrderItem(item, item.price!!, count)

        //주문 생성
        val order: Order = Order().createOrder(member, delivery, orderItem)

        //주문 저장
        orderRepository.save(order)
        return order.id
    }

    /**
     * 주문 취소
     */
    @Transactional
    fun cancelOrder(orderId: Long) {
        //주문 엔티티 조회
        val order: Order = orderRepository.findOne(orderId)
        //주문 취소
        order.cancel()
    }

    //검색
    fun findOrders(orderSearch: OrderSearch): List<Order> {
        return orderRepository.findAllByString(orderSearch)
    }
}