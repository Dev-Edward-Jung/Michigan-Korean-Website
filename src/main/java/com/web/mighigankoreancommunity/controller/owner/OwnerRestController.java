package com.web.mighigankoreancommunity.controller.owner;


import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/owner")
@RestController
public class OwnerRestController {
    private final OwnerService ownerService;

    @PostMapping("/checkEmail")
    public boolean checkEmail(@RequestBody String email) {
        System.out.println(ownerService.getMemberByEmail(email));
        return ownerService.getMemberByEmail(email);
    }

    @GetMapping("/me")
    public ResponseEntity<OwnerDTO> getCurrentUser(@AuthenticationPrincipal Owner owner) {
        System.out.println(owner.getOwnerName());
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        OwnerDTO dto = ownerService.memberToDTO(owner);

        return ResponseEntity.ok(dto);
    }


}
