package com.web.mighigankoreancommunity.repository.member;


import com.web.mighigankoreancommunity.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    find Member By Id
    Member findMemberById(Long id);

//    Searching by Email
    boolean existsByMemberEmail(String memberEmail);

//  login with Email
    Member findMemberByMemberEmail(String memberEmail);


}
