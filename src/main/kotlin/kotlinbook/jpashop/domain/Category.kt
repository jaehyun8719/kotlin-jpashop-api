package kotlinbook.jpashop.domain

import kotlinbook.jpashop.domain.item.Item
import javax.persistence.*

@Entity
class Category(
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    var id: Long,

    var name: String,
) {
    @ManyToMany
    @JoinTable(
        name = "category_item",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    var items: MutableList<Item> = arrayListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null

    @OneToMany(mappedBy = "parent")
    var child: MutableList<Category> = ArrayList()

    //==연관관계 메서드==//
    fun addChildCategory(child: Category) {
        this.child.add(child)
        child.parent = this
    }
}