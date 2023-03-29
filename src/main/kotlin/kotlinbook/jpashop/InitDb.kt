package kotlinbook.jpashop

import kotlinbook.jpashop.domain.*
import kotlinbook.jpashop.domain.item.Book
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager


/**
 * 종 주문 2개
 * * userA
 * * JPA1 BOOK
 * * JPA2 BOOK
 * * userB
 * * SPRING1 BOOK
 * * SPRING2 BOOK
 */
@Component
class InitDb(
    private val initService: InitService
) {
    @PostConstruct
    fun init() {
        initService!!.dbInit1()
        initService.dbInit2()
    }

    @Component
    @Transactional
    class InitService(
        private val em: EntityManager
    ) {
        fun dbInit1() {
            println("Init1" + this.javaClass)
            val member: Member = createMember("userA", "서울", "1", "1111")
            em.persist(member)
            val book1: Book = createBook("JPA1 BOOK", 10000, 100)
            em.persist(book1)
            val book2: Book = createBook("JPA2 BOOK", 20000, 100)
            em.persist(book2)
            val orderItem1: OrderItem = OrderItem().createOrderItem(book1, 10000, 1)
            val orderItem2: OrderItem = OrderItem().createOrderItem(book2, 20000, 2)
            val delivery: Delivery = createDelivery(member)
            val order: Order = Order().createOrder(member, delivery, orderItem1, orderItem2)
            em.persist(order)
        }

        fun dbInit2() {
            val member: Member = createMember("userB", "진주", "2", "2222")
            em.persist(member)
            val book1: Book = createBook("SPRING1 BOOK", 20000, 200)
            em.persist(book1)
            val book2: Book = createBook("SPRING2 BOOK", 40000, 300)
            em.persist(book2)
            val orderItem1: OrderItem = OrderItem().createOrderItem(book1, 20000, 3)
            val orderItem2: OrderItem = OrderItem().createOrderItem(book2, 40000, 4)
            val delivery: Delivery = createDelivery(member)
            val order: Order = Order().createOrder(member, delivery, orderItem1, orderItem2)
            em.persist(order)
        }

        private fun createMember(name: String, city: String, street: String, zipcode: String): Member {
            return Member(
                name = name,
                address = Address(city, street, zipcode)
            )
        }

        private fun createBook(name: String, price: Int, stockQuantity: Int): Book {
            val book1 = Book()
            book1.name = name
            book1.price = price
            book1.stockQuantity = stockQuantity
            return book1
        }

        private fun createDelivery(member: Member): Delivery {
            return Delivery(
                address = member.address
            )
        }
    }
}