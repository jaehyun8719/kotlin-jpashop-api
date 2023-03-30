package kotlinbook.jpashop.api

import kotlinbook.jpashop.domain.Member
import kotlinbook.jpashop.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.Valid

@RestController
class MemberApiController(
    @Autowired private val memberService: MemberService,
) {

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
     * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 모든 요청 요구사항을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * 결론
     * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @PostMapping("/api/v1/members")
    fun saveMemberV1(@RequestBody member: @Valid Member): CreateMemberResponse {
        val id: Long = memberService.join(member)
        return CreateMemberResponse(id)
    }

    @PostMapping("/api/v2/members")
    fun saveMemberV2(@RequestBody request: @Valid CreateMemberRequest): CreateMemberResponse {
        val member = Member(
            name = request.name
        )

        val id: Long = memberService.join(member)
        return CreateMemberResponse(id)
    }

    /**
     * 수정 API
     */
    @PutMapping("/api/v2/members/{id}")
    fun updateMemberV2(
        @PathVariable("id") id: Long,
        @RequestBody request: @Valid UpdateMemberRequest
    ): UpdateMemberResponse {
        memberService.update(id, request.name)
        val findMember = memberService.findOne(id)
        return UpdateMemberResponse(findMember.id!!, findMember.name)
    }

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 기본적으로 엔티티의 모든 값이 노출된다.
     * - 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)
     * - 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 프레젠테이션 응답 로직을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변한다.
     * - 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
     * 결론
     * - API 응답 스펙에 맞추어 별도의 DTO를 반환한다.
     */
    //조회 V1: 안 좋은 버전, 모든 엔티티가 노출, @JsonIgnore -> 이건 정말 최악, api가 이거 하나인가! 화면에 종속적이지 마라!
    @GetMapping("/api/v1/members")
    fun membersV1(): List<Member> {
        return memberService.findMembers()
    }

    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다.
     */
    @GetMapping("/api/v2/members")
    fun membersV2(): Result<List<MemberDto>> {
        val findMembers = memberService.findMembers()
        //엔티티 -> DTO 변환
        val collect = findMembers.map { MemberDto(it.name) }
        return Result(collect)
    }

    data class Result<T>(
        val data: T
    )

    data class MemberDto(
        val name: String
    )

    data class UpdateMemberRequest(
        val name: String
    )

    data class UpdateMemberResponse(
        val id: Long,
        val name: String,
    )

    data class CreateMemberRequest(
        val name: String
    )

    data class CreateMemberResponse(
        val id: Long
    )
}