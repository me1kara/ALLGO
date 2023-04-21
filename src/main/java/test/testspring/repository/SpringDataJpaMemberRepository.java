package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import test.testspring.domain.Member;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, String>, MemberRepository {

    @Override
    Optional<Member> findByName(String name);

    @Override
    Optional<Member> findById(String id);
}
