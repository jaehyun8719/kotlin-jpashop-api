package kotlinbook.jpashop.api

import kotlinbook.jpashop.domain.*
import kotlinbook.jpashop.repository.OrderRepository
import kotlinbook.jpashop.repository.order.query.OrderFlatDto
import kotlinbook.jpashop.repository.order.query.OrderItemQueryDto
import kotlinbook.jpashop.repository.order.query.OrderQueryDto
import kotlinbook.jpashop.repository.order.query.OrderQueryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.stream.Collectors

/**
 * V1. 엔티티 직접 노출
 * - 엔티티가 변하면 API 스펙이 변한다.
 * - 트랜잭션 안에서 지연 로딩 필요
 * - 양방향 연관관계 문제
 *
 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
 * - 트랜잭션 안에서 지연 로딩 필요
 * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
 * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능)
 *
 * V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
 * - 페이징 가능
 * V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
 * - 페이징 가능
 * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
 * - 페이징 불가능...
 *
 */
@RestController
class OrderApiController(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val orderQueryRepository: OrderQueryRepository,
) {

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    fun ordersV1(): List<Order> {
        val all: List<Order> = orderRepository.findAll()
        for (order in all) {
            order.member?.name //Lazy 강제 초기화
            order.delivery?.address //Lazy 강제 초기환
            val orderItems: List<OrderItem> = order.orderItems
            orderItems.stream().forEach{ it.item?.name } //Lazy 강제 초기화
        }
        return all
    }

    @GetMapping("/api/v2/orders")
    fun ordersV2(): List<OrderDto> {
        val orders: List<Order> = orderRepository.findAll()
        return orders.map{ it.toDto() }
    }

    @GetMapping("/api/v3/orders")
    fun ordersV3(): List<OrderDto> {
        val orders: List<Order> = orderRepository.findAllWithItem()
        return orders.stream()
            .map{ it.toDto() }
            .collect(Collectors.toList())
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3.1/orders")
    fun ordersV3_page(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "100") limit: Int
    ): List<OrderDto> {
        val orders: List<Order> = orderRepository.findAllWithMemberDelivery(offset, limit)
        return orders.stream()
            .map{ it.toDto() }
            .collect(Collectors.toList())
    }

    @GetMapping("/api/v4/orders")
    fun ordersV4(): List<OrderQueryDto> {
        return orderQueryRepository.findOrderQueryDtos()
    }

    @GetMapping("/api/v5/orders")
    fun ordersV5(): List<OrderQueryDto> {
        return orderQueryRepository.findAllByDto_optimization()
    }

    @GetMapping("/api/v6/orders")
    fun ordersV6(): List<OrderQueryDto> {
        val flats: List<OrderFlatDto> = orderQueryRepository.findAllByDto_flat()

        return flats.stream()
            .collect(
                Collectors.groupingBy({
                        OrderQueryDto(
                            it.orderId,
                            it.name,
                            it.orderDate,
                            it.orderStatus,
                            it.address,
                        )
                    },
                    Collectors.mapping({
                            OrderItemQueryDto(
                                it.orderId,
                                it.itemName,
                                it.orderPrice,
                                it.count,
                            )
                        }, Collectors.toList()
                    )
                )
            ).map {
                OrderQueryDto(
                    it.key.orderId,
                    it.key.name,
                    it.key.orderDate,
                    it.key.orderStatus,
                    it.key.address,
                    it.value,
                )
            }
    }

    data class OrderDto(
        val orderId: Long?,
        val name: String?,
        val orderDate: LocalDateTime?,
        val orderStatus: OrderStatus?,
        val address: Delivery?,
        val orderItems: List<OrderItemDto>? = null,
    )

    data class OrderItemDto(
        val itemName: String?, //상품 명
        val orderPrice: Int, //주문 가격
        val count: Int, //주문 수량
    )

}