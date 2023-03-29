package kotlinbook.jpashop.domain.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("B")
class Book(
    var author: String? = null,
    var isbn: String? = null,
) : Item()