package com.wynnteo.ordermgmt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.wynnteo.ordermgmt.feignclient.ProductClient;

@SpringBootTest
class OrdermgmtApplicationTests {


	@MockBean
    private ProductClient productClient;
	@Test
	void contextLoads() {
	}

}
