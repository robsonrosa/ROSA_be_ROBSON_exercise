package com.ecore.roles.api;

import com.ecore.roles.mapper.RoleMapper;
import com.ecore.roles.model.Role;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.RoleDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.ecore.roles.utils.RestAssuredHelper.*;
import static com.ecore.roles.utils.TestData.DEFAULT_TEAM_UUID;
import static com.ecore.roles.utils.TestData.DEFAULT_USER_UUID;
import static com.ecore.roles.utils.TestData.RoleEnum.*;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Sql(scripts = "/cleanup-data.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RolesApiTest {

    private final EasyRandom generator = new EasyRandom();

    private final RestTemplate restTemplate;

    private final RoleMapper mapper;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public RolesApiTest(RestTemplate restTemplate, RoleMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldFailWhenPathDoesNotExist() {
        sendRequest(when()
                .get("/v1/role")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldCreateNewRole() {
        final RoleDto expectedRole = build();

        final RoleDto actualRole = createRole(expectedRole)
                .statusCode(201)
                .extract().as(RoleDto.class);

        assertThat(actualRole.getName()).isEqualTo(expectedRole.getName());
    }

    @Test
    void shouldFailToCreateNewRoleWhenNull() {
        createRole(null)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenMissingName() {
        createRole(buildWithName(null))
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenBlankName() {
        createRole(buildWithName(""))
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenNameAlreadyExists() {
        createRole(buildWithName(DEVELOPER.getName()))
                .validate(400, "Role already exists");
    }

    @Test
    void shouldGetAllRoles() {
        final RoleDto[] roles = getRoles()
                .extract().as(RoleDto[].class);

        assertThat(roles.length).isGreaterThanOrEqualTo(3);
        assertThat(roles).contains(mapper.fromModel(DEVELOPER.get()));
        assertThat(roles).contains(mapper.fromModel(PRODUCT_OWNER.get()));
        assertThat(roles).contains(mapper.fromModel(TESTER.get()));
    }

    @Test
    void shouldGetRoleById() {
        Role expectedRole = DEVELOPER.get();

        searchRole(expectedRole.getId())
                .statusCode(200)
                .body("name", equalTo(expectedRole.getName()));
    }

    @Test
    void shouldFailToGetRoleByIdWhenNotFound() {
        final UUID id = randomUUID();
        searchRole(id)
                .validate(404, format("Role %s not found", id));
    }

    @Test
    void shouldGetRolesByUserIdAndTeamId() {
        final RoleDto[] roles = searchRole(DEFAULT_USER_UUID, DEFAULT_TEAM_UUID)
                .statusCode(200)
                .extract().as(RoleDto[].class);

        assertThat(roles.length).isGreaterThanOrEqualTo(1);
        assertThat(roles).contains(mapper.fromModel(DEVELOPER.get()));
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenMissingUserId() {
        searchRole(null, DEFAULT_TEAM_UUID)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenMissingTeamId() {
        searchRole(DEFAULT_USER_UUID, null)
                .validate(400, "Bad Request");
    }

    private RoleDto build() {
        return generator.nextObject(RoleDto.class)
                .toBuilder()
                .id(null)
                .build();
    }

    private RoleDto buildWithName(final String name) {
        return build()
                .toBuilder()
                .name(name)
                .build();
    }

}
