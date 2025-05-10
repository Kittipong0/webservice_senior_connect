package org.itsci.senior_connect.repository;

import org.itsci.senior_connect.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByMemberUserName(String memberUserName);
    Optional<Member> findByMemberUID(String memberUID);
}
