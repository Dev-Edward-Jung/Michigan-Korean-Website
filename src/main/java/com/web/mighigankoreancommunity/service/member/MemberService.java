package com.web.mighigankoreancommunity.service.member;


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


//    Login Method
    public boolean login(Member member) {
        boolean result = false;
        String inputEmail = member.getMemberEmail();
        String inputPassword = member.getMemberPassword();


        Member loginMember = memberRepository.findMemberByMemberEmail(inputEmail);
        String passwordFromDB = loginMember.getMemberPassword();

        System.out.println(loginMember.getMemberEmail());

        if (loginMember != null) {
            if (passwordEncoder.matches(inputPassword, passwordFromDB)) {
                result = true;
            }
        }

        return result;
    }


}
