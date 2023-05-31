//package test.testspring.repository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import test.testspring.domain.Member;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//import java.util.Optional;
//
//public class JpaMemberRepository implements MemberRepository{
//
//
//    //spring이 알아서 넣어줌.jpa-data를 넣기때문
//    private final EntityManager em;
//
//    @Autowired
//    public JpaMemberRepository(EntityManager em) {
//        this.em = em;
//    }
//
//    @Override
//    public Member save(Member member) {
//        em.persist(member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findById(String Id) {
//        Member member = em.find(Member.class, Id);
//        return Optional.ofNullable(member);
//    }
//
//    @Override
//    public Optional<Member> findByName(String name) {
//
//        List<Member> result = em.createQuery("select m from Member m where m.name =:name", Member.class)
//                .setParameter("name",name)
//                .getResultList();
//        return result.stream().findAny();
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return em.createQuery("select m from Member m", Member.class).getResultList();
//    }
//}
