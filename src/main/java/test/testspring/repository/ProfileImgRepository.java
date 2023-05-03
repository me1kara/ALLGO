package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.Product;
import test.testspring.domain.ProfileImg;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {


}
