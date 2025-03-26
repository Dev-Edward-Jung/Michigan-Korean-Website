package com.web.mighigankoreancommunity.service.member;


import com.web.mighigankoreancommunity.dto.MemberDTO;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    Register Method
    public Long saveMember(Member member) {
        String rawPassword = member.getMemberPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        member.setMemberPassword(encodedPassword);
        memberRepository.save(member);
        return member.getId();
    }

//    check Email
    public boolean getMemberByEmail(String email) {
        return memberRepository.existsByMemberEmail(email);
    }


    public MemberDTO memberToDTO(Member member) {
        MemberDTO dto = new MemberDTO(
                member.getId(),
                member.getMemberName(),
                member.getMemberEmail()
        );
        return dto;
    }






}
