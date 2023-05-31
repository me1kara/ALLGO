package test.testspring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.testspring.domain.ProductCategory;
import test.testspring.repository.CategoryRepository;
import test.testspring.service.ProductService;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
class TestSpringApplicationTests {



}
