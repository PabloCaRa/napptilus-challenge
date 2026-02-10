package com.napptilus.challenge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ProductPriceControllerIT {

    private static final String BASE_URL = "/api/v1/brands/%s/products/%s/prices";
    private static final String QUERY_PARAM_APPLICATION_DATE = "?application_date=%s";
    private static final String RESOURCES_BASE_PATH = "src/testIntegration/resources/integration_tests/";

    @Autowired
    private WebTestClient webTestClient;

    @ParameterizedTest
    @DisplayName("""
            Given a valid request
            When the request is processed
            Then the response status is 200
            And the body is the expected
            """)
    @MethodSource("case1Arguments")
    void case1(LocalDateTime applicationDate, Path expectedResponsePath) throws IOException {
        String brandId = "1";
        String productId = "35455";

        webTestClient.get()
                .uri(BASE_URL.formatted(brandId, productId) + QUERY_PARAM_APPLICATION_DATE.formatted(applicationDate))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(Files.readString(expectedResponsePath));
    }

    @Test
    @DisplayName("""
            Given a valid request
            And the combination of brandId, productId and applicationDate does not exist in the database
            When the request is processed
            Then the response status is 404
            And the body is empty
            """)
    void case2() {
        Integer brandId = 2;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        webTestClient.get()
                .uri(BASE_URL.formatted(brandId, productId) + QUERY_PARAM_APPLICATION_DATE.formatted(applicationDate))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .isEmpty();
    }

    @Test
    @DisplayName("""
            Given an invalid request without application_date
            When the request is processed
            Then the response status is 400
            And the body is the expected
            """)
    void case3() throws IOException {
        String brandId = "1";
        String productId = "35455";
        Path expectedResponsePath = Paths.get(RESOURCES_BASE_PATH + "case3/response.json");

        webTestClient.get()
                .uri(BASE_URL.formatted(brandId, productId))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json(Files.readString(expectedResponsePath));
    }

    @ParameterizedTest
    @DisplayName("""
            Given an request with an invalid parameter
            When the request is processed
            Then the response status is 400
            And the body is the expected
            """)
    @CsvSource({
            "abc,35455,2020-06-14T16:00:00,invalid_brand_id_response.json",
            "1,abc,2020-06-14T16:00:00,invalid_product_id_response.json",
            "1,35455,2020-06-14 16:00:00,invalid_application_date_response.json"
    })
    void case4(String brandId, String productId, String applicationDate, String expectedResponseFile) throws IOException {
        webTestClient.get()
                .uri(BASE_URL.formatted(brandId, productId) + QUERY_PARAM_APPLICATION_DATE.formatted(applicationDate))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json(Files.readString(Paths.get(RESOURCES_BASE_PATH + "case4/" + expectedResponseFile)));
    }

    @Test
    @DisplayName("""
            Given a request to a non-existent URL
            When the request is processed
            Then the status is 400
            And the body is the expected
            """)
    void case5() throws IOException {
        String brandId = "";
        String productId = "35455";
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        Path expectedResponsePath = Paths.get(RESOURCES_BASE_PATH + "case5/response.json");

        webTestClient.get()
                .uri(BASE_URL.formatted(brandId, productId) + QUERY_PARAM_APPLICATION_DATE.formatted(applicationDate))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json(Files.readString(expectedResponsePath));
    }

    private static Stream<Arguments> case1Arguments() {
        String responsePath = RESOURCES_BASE_PATH + "case1/%s-response.json";
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                        Paths.get(responsePath.formatted("2020-06-14-10-00-00"))
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 16, 0, 0),
                        Paths.get(responsePath.formatted("2020-06-14-16-00-00"))
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 21, 0, 0),
                        Paths.get(responsePath.formatted("2020-06-14-21-00-00"))
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 15, 10, 0, 0),
                        Paths.get(responsePath.formatted("2020-06-15-10-00-00"))
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 16, 21, 0, 0),
                        Paths.get(responsePath.formatted("2020-06-16-21-00-00"))
                )
        );
    }
}
