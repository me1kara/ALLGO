package test.testspring.repository;

import test.testspring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(String Id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

}
