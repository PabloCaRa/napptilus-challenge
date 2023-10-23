package com.napptilus.challenge;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.ResourceUtils.getFile;

@SpringBootTest
@AutoConfigureMockMvc
class NapptilusCodingChallengeApplicationTests {

    private static final String API_PATH = "/api/v1/product/{productId}/price";
    private static final String PRODUCT_ID = "35455";
    private static final String BRAND_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = """
            Given a product id 35455
            And brand id 1
            And date {0}
            When the request is processed
            Then the status is ok
            And the body is the expected one
            """)
    @MethodSource("case1Arguments")
    void case1(LocalDateTime date, String expectedResponsePath) throws Exception {
        String expectedResponse = readString(
                getFile(expectedResponsePath).toPath(),
                StandardCharsets.UTF_8
        );

        mockMvc.perform(get(API_PATH, PRODUCT_ID)
                        .queryParam("date", date.toString())
                        .queryParam("brandId", BRAND_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @ParameterizedTest(name = """
            Given a product id {0}
            And brand id {1}
            And date {2}
            And one of the filters does not exist in the database
            When the request is processed
            Then the status is 404
            """)
    @CsvSource({
            "1, 1, 2020-06-14T10:00:00",
            "35455, 2, 2020-06-14T10:00:00",
            "35455, 1, 2020-06-10T10:00:00"
    })
    void case2(String productId, String brandId, String date) throws Exception {
        mockMvc.perform(get(API_PATH, productId)
                        .queryParam("date", date)
                        .queryParam("brandId", brandId))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest(name = """
            Given a product id {0}
            And brand id {1}
            And date {2}
            And one of the parameters is not valid
            When the request is processed
            Then the status is 400
            """)
    @CsvSource({
            "A, 1, 2020-06-14T10:00:00",
            "35455, A, 2020-06-14T10:00:00",
            "35455, 1, 2020-06-10-10:00:00"
    })
    void case3(String productId, String brandId, String date) throws Exception {
        mockMvc.perform(get(API_PATH, productId)
                        .queryParam("date", date)
                        .queryParam("brandId", brandId))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> case1Arguments() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                        "classpath:product_price_1_response.json"
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 16, 0, 0),
                        "classpath:product_price_2_response.json"
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 21, 0, 0),
                        "classpath:product_price_1_response.json"
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 15, 10, 0, 0),
                        "classpath:product_price_3_response.json"
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 16, 21, 0, 0),
                        "classpath:product_price_4_response.json"
                )
        );
    }
}
