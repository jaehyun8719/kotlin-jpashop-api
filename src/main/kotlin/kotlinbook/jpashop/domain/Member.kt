package kotlinbook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
class Member(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null,

    @NotEmpty(message = "회원 이름은 필수 입니다")
    var name: String,

    @Embedded
    var address: Address? = null,
) {
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    var orders: MutableList<Order> = arrayListOf()
}