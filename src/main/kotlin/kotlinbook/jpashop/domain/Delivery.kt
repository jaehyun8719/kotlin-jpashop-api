package kotlinbook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Delivery(
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    var id: Long? = null,

    @Embedded
    var address: Address? = null,

    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus? = null //READY, COMP
) {
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null
}