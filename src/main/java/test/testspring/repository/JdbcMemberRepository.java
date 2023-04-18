package test.testspring.repository;

import org.springframework.stereotype.Repository;
import test.testspring.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;


public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql ="insert into member(name) values(?)";

        Hashtable<String, Boolean> params = new Hashtable<>();

        params.put("asd",true);
        params.get("asd");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;

        try {
            conn = dataSource.getConnection();

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                member.setId(rs.getString(1));
            }

            return member;
        }catch(Exception e){
            try {
                throw new SQLException("id 조회실패");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }finally {

        }



    }

    @Override
    public Optional<Member> findById(String Id) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        String sql ="select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs =null;
        List<Member> members = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                Member member = new Member();
                member.setName(rs.getString("name"));
                member.setId(rs.getString("id"));
                members.add(member);
            }
        return members;
        }catch (Exception e){
            e.getMessage();
            return null;
        }
    }
}
