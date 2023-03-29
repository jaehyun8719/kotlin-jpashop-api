package kotlinbook.jpashop.domain.item

import kotlinbook.jpashop.domain.Category
import kotlinbook.jpashop.exception.NotEnoughStockException
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long? = null

    var name: String? = null

    var price: Int? = null

    var stockQuantity: Int? = null

    @ManyToMany(mappedBy = "items")
    val categories: MutableList<Category> = arrayListOf()

    //==비즈니스 로직==//
    /**
     * stock 증가
     */
    fun addStock(quantity: Int) {
        stockQuantity = stockQuantity?.plus(quantity)
    }

    /**
     * stock 감소
     */
    fun removeStock(quantity: Int) {
        val restStock = stockQuantity?.minus(quantity)
        if (restStock != null) {
            if (restStock < 0) {
                throw NotEnoughStockException("need more stock")
            }
        }
        stockQuantity = restStock
    }
}