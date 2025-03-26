package com.web.mighigankoreancommunity.controller.member;


import com.web.mighigankoreancommunity.dto.MemberDTO;
import com.web.mighigankoreancommunity.dto.RestaurantDTO;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import com.web.mighigankoreancommunity.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberRestController {
    private final MemberService memberService;

    @PostMapping("/checkEmail")
    public boolean checkEmail(@RequestBody String email) {
    return memberService.getMemberByEmail(email);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberDTO> getCurrentUser(@AuthenticationPrincipal Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDTO dto = memberService.memberToDTO(member);

        return ResponseEntity.ok(dto);
    }


}
