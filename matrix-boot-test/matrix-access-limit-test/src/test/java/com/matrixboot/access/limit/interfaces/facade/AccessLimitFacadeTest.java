package com.matrixboot.access.limit.interfaces.facade;

import com.matrixboot.access.limit.interfaces.vo.QueryData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * TODO
 * <p>
 * create in 2022/1/19 5:21 PM
 *
 * @author shishaodong
 * @version 0.0.1
 */

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccessLimitFacadeTest {

    @Resource
    private TestRestTemplate restTemplate;

    @RepeatedTest(10)
    @DisplayName("执行 10 次不发生报错")
    void example0() {
        String username = "123";
        QueryData response = restTemplate.getForObject("/example0?username=" + username, QueryData.class);
        Assertions.assertEquals(response.getUsername(), username);
    }

    @Nested
    @DisplayName("测试接口 example1, 测试未触发请求控制和触发请求控制")
    class Example1Test {

        @Test
        @DisplayName("测试接口 example1 ,测试未触发请求控制")
        void example11() {
            String username = RandomStringUtils.randomAlphanumeric(5);
            for (int i = 0; i < 3; i++) {
                QueryData response = restTemplate.getForObject("/example1?username=" + username, QueryData.class);
                Assertions.assertEquals(response.getUsername(), username);
            }
        }

        @Test
        @DisplayName("测试接口 example1, 测试触发请求控制")
        void example12() {
            String username = RandomStringUtils.randomAlphanumeric(5);
            ResponseEntity<QueryData> response = null;
            for (int i = 0; i < 4; i++) {
                response = restTemplate.getForEntity("/example1?username=" + username, QueryData.class);
            }
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }


    @Nested
    @DisplayName("测试接口 example2, 测试未触发请求控制和触发请求控制")
    class Example2Test {

        @Test
        @DisplayName("测试接口 example2 ,测试未触发请求控制")
        void example11() {
            String username = RandomStringUtils.randomAlphanumeric(10);
            String group = RandomStringUtils.randomAlphanumeric(10);
            for (int i = 0; i < 3; i++) {
                QueryData response = restTemplate.getForObject("/example2?username=" + username + "&group=" + group, QueryData.class);
                Assertions.assertEquals(response.getUsername(), username);
            }
        }

        @Test
        @DisplayName("测试接口 example2, 测试触发请求控制")
        void example12() {
            String username = RandomStringUtils.randomAlphanumeric(10);
            String group = RandomStringUtils.randomAlphanumeric(10);
            ResponseEntity<QueryData> response = null;
            for (int i = 0; i < 4; i++) {
                response = restTemplate.getForEntity("/example2?username=" + username + "&group=" + group, QueryData.class);
            }
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }


    @Test
    void example3() {
    }

    @Test
    @DisplayName("测试接口 example4, 前 3 次数据原路返回, 第 4 次返回的 username 为 null")
    void example4() {
        String username = RandomStringUtils.randomAlphanumeric(5);
        for (int i = 0; i < 3; i++) {
            QueryData response = restTemplate.getForObject("/example1?username=" + username, QueryData.class);
            Assertions.assertEquals(response.getUsername(), username);
        }
        QueryData response = restTemplate.getForObject("/example1?username=" + username, QueryData.class);
        Assertions.assertNull(response.getUsername());
    }

}