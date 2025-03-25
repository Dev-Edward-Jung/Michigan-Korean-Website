package com.web.mighigankoreancommunity.service;


import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

//    Register Method
    public Long saveMember(Member member) {
        memberRepository.save(member);
        return member.getId();
    }
}
