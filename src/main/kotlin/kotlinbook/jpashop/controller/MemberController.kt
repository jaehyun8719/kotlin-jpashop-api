package kotlinbook.jpashop.controller

import kotlinbook.jpashop.domain.Address
import kotlinbook.jpashop.domain.Member
import kotlinbook.jpashop.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class MemberController(
    @Autowired private val memberService: MemberService
) {
    @GetMapping("/members/new")
    fun createForm(model: Model): String {
        model.addAttribute("memberForm", MemberForm())
        return "members/createMemberForm"
    }

    @PostMapping("/members/new")
    fun create(form: @Valid MemberForm?, result: BindingResult): String {
        if (result.hasErrors()) {
            return "members/createMemberForm"
        }
        val address = form?.let { Address(it.city!!, it.street!!, it.zipcode!! ) }
        val member = Member(
            name = form?.name!!,
            address = address
        )
        memberService.join(member)
        return "redirect:/"
    }

    @GetMapping("/members")
    fun list(model: Model): String {
        val members: List<Member> = memberService.findMembers()
        model.addAttribute("members", members)
        return "members/memberList"
    }
}