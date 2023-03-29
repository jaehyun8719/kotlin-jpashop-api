package kotlinbook.jpashop.service

import kotlinbook.jpashop.domain.Member
import kotlinbook.jpashop.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
) {

    /**
     * 회원 가입
     */
    @Transactional
    fun join(member: Member): Long {
        validateDuplicateMember(member) //중복 회원 검증
        memberRepository.save(member)
        return member.id!!
    }

    private fun validateDuplicateMember(member: Member) {
        val findMembers: List<Member> = memberRepository.findByName(member.name)
        check(findMembers.isEmpty()) { "이미 존재하는 회원입니다." }
    }

    //회원 전체 조회
    fun findMembers(): List<Member> {
        return memberRepository.findAll()
    }

    fun findOne(memberId: Long): Member {
        return memberRepository.findOne(memberId)
    }

    /**
     * 회원 수정
     */
    @Transactional
    fun update(id: Long, name: String) {
        val member = memberRepository.findOne(id)
        member.name = name
    }
}