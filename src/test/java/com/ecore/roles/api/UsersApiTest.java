package com.ecore.roles.api;

import com.ecore.roles.client.model.User;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.UserDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.ecore.roles.utils.MockUtils.mockGetUserById;
import static com.ecore.roles.utils.MockUtils.mockGetUsers;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static com.ecore.roles.utils.TestData.UUID_1;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private EasyRandom generator;

    @LocalServerPort
    private int port;

    @Autowired
    public UsersApiTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        generator = new EasyRandom();
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldFailWhenPathDoesNotExist() {
        sendRequest(when()
                .get("/v1/user")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldGetAllUsers() {
        mockGetUsers(mockServer, generator, 500);
        final UserDto[] users = getUsers()
                .extract().as(UserDto[].class);

        assertThat(users.length).isGreaterThanOrEqualTo(500);
        Arrays.stream(users).forEach(user -> {
            assertNotNull(user.getId());
            assertNotNull(user.getDisplayName());
        });
    }

    @Test
    void shouldGetUserById() {
        final User user = generator.nextObject(User.class)
                .toBuilder()
                .id(randomUUID())
                .build();
        mockGetUserById(mockServer, user.getId(), user);

        getUser(user.getId())
                .statusCode(200)
                .body("id", equalTo(user.getId().toString()))
                .body("location", equalTo(user.getLocation()))
                .body("lastName", equalTo(user.getLastName()))
                .body("firstName", equalTo(user.getFirstName()))
                .body("avatarUrl", equalTo(user.getAvatarUrl()));
    }

    @Test
    void shouldFailToGetUserByIdWhenNotFound() {
        mockGetUserById(mockServer, UUID_1, null);
        getUser(UUID_1)
                .validate(404, format("User %s not found", UUID_1));
    }

}
