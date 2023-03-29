package kotlinbook.jpashop.api

import kotlinbook.jpashop.domain.Order
import kotlinbook.jpashop.repository.OrderRepository
import kotlinbook.jpashop.repository.OrderSearch
import kotlinbook.jpashop.repository.order.simplequery.OrderSimpleQueryDto
import kotlinbook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
class OrderSimpleApiController(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val orderSimpleQueryRepository: OrderSimpleQueryRepository //의존관계 주입
) {
    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    fun ordersV1(): List<Order> {
        val all: List<Order> = orderRepository.findAllByString(OrderSearch())
        for (order in all) {
            order.member?.name //Lazy 강제 초기화
            order.delivery?.address//Lazy 강제 초기화
        }
        return all
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    fun ordersV2(): List<OrderApiController.OrderDto> {
        val orders: List<Order> = orderRepository.findAll()
        return orders.map{ it.toDto() }
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     */
    @GetMapping("/api/v3/simple-orders")
    fun ordersV3(): List<OrderApiController.OrderDto> {
        val orders: List<Order> = orderRepository.findAllWithMemberDelivery()
        return orders.map{ it.toDto() }
    }

    @GetMapping("/api/v4/simple-orders")
    fun ordersV4(): List<OrderSimpleQueryDto> {
        return orderSimpleQueryRepository.findOrderDtos()
    }
}