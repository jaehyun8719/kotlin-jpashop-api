package kotlinbook.jpashop.controller

import javax.validation.constraints.NotEmpty

class MemberForm(
    val name: @NotEmpty(message = "회원 이름은 필수 입니다") String? = null,
    val city: String? = null,
    val street: String? = null,
    val zipcode: String? = null,
)