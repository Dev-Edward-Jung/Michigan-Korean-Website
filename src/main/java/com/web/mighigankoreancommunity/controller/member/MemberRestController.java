package com.web.mighigankoreancommunity.controller.member;


import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberRestController {
    private final MemberRepository memberRepository;

    @PostMapping("/checkEmail")
    public boolean checkEmail(@RequestBody String email) {

        boolean result = memberRepository.existsByMemberEmail(email);
        return result;
    }

}
