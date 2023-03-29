package kotlinbook.jpashop.repository

import kotlinbook.jpashop.domain.Order
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class OrderRepository(
    @PersistenceContext private val em: EntityManager
) {
    fun save(order: Order) {
        em.persist(order)
    }

    fun findOne(id: Long): Order {
        return em.find(Order::class.java, id)
    }

    fun findAll(): List<Order> {
        return em.createQuery("select o from Order o", Order::class.java)
            .resultList
    }

    fun findAllByString(orderSearch: OrderSearch): List<Order> {
        var jpql = "select o from Order o join o.member m"
        var isFirstCondition = true

        //주문 상태 검색
        if (orderSearch.orderStatus != null) {
            if (isFirstCondition) {
                jpql += " where"
                isFirstCondition = false
            } else {
                jpql += " and"
            }
            jpql += " o.status = :status"
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.memberName)) {
            if (isFirstCondition) {
                jpql += " where"
                isFirstCondition = false
            } else {
                jpql += " and"
            }
            jpql += " m.name like :name"
        }
        var query: TypedQuery<Order> = em.createQuery(jpql, Order::class.java)
            .setMaxResults(1000)
        if (orderSearch.orderStatus != null) {
            query = query.setParameter("status", orderSearch.orderStatus)
        }
        if (StringUtils.hasText(orderSearch.memberName)) {
            query = query.setParameter("name", orderSearch.memberName)
        }
        return query.resultList
    }

    /**
     * JPA Criteria
     */
    fun findAllByCriteria(orderSearch: OrderSearch): List<Order> {
        val cb: CriteriaBuilder = em.criteriaBuilder
        val cq: CriteriaQuery<Order> = cb.createQuery(Order::class.java)
        val o = cq.from(Order::class.java)
        val m = o.join<Any, Any>("member", JoinType.INNER)
        val criteria: MutableList<Predicate> = arrayListOf()

        //주문 상태 검색
        if (orderSearch.orderStatus != null) {
            val status: Predicate = cb.equal(o.get<OrderSearch>("status"), orderSearch.orderStatus)
            criteria.add(status)
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.memberName)) {
            val name: Predicate = cb.like(m.get("name"), "%" + orderSearch.memberName + "%")
            criteria.add(name)
        }
        cq.where(cb.and(*criteria.toTypedArray()))
        val query: TypedQuery<Order> = em.createQuery(cq).setMaxResults(1000)
        return query.resultList
    }

    fun findAllWithMemberDelivery(): List<Order> {
        return em.createQuery(
            "select o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d", Order::class.java
        ).resultList
    }

    fun findAllWithItem(): List<Order> {
        return em.createQuery(
            "select distinct o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d" +
                    " join fetch o.orderItems oi" +
                    " join fetch oi.item i", Order::class.java
        ).resultList
    }

    fun findAllWithMemberDelivery(offset: Int, limit: Int): List<Order> {
        return em.createQuery(
            "select o from Order o" +
                    " join fetch o.member m" +
                    " join fetch o.delivery d", Order::class.java
        ).setFirstResult(offset)
            .setMaxResults(limit)
            .resultList
    }
}