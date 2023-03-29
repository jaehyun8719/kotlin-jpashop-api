package kotlinbook.jpashop.repository

import kotlinbook.jpashop.domain.item.Item
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ItemRepository(
    @Autowired private val em: EntityManager,
) {
    fun save(item: Item) {
        if (item.id == null) {
            em.persist(item)
        } else {
            em.merge<Any>(item)
        }
    }

    fun findOne(id: Long?): Item {
        return em.find(Item::class.java, id)
    }

    fun findAll(): List<Item> {
        return em.createQuery("select i from Item i", Item::class.java)
            .resultList
    }
}