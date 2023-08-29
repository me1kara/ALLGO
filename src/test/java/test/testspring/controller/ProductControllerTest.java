package test.testspring.controller;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import test.testspring.domain.Product;
import test.testspring.service.ProductService;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;

    @Test
    @DisplayName("product content test:")
    void getProductContent() throws Exception {
        given(productService.getProductByNo(1l)).willReturn(
                Product.builder().product_no(1l).build()
        );
        Long product_no = 1l;

        mockMvc.perform(get("/product/productContent/"+product_no))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_no").exists())
                .andDo(print());
        verify(productService).getProductByNo(1l);
    }


}
