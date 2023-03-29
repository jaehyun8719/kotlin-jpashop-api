package kotlinbook.jpashop.domain.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("M")
class Movie(
    var director: String,
    var actor: String,
) : Item()