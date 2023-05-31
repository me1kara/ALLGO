package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import test.testspring.domain.Member;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, String>, MemberRepository {
    @Override
    Optional<Member> findByName(String name);
    @Override
    Optional<Member> findById(String id);
    @Override
    Optional<Member> findByEmail(String email);
    @Override
    Optional<Member> findByPhone(String phone);


}
