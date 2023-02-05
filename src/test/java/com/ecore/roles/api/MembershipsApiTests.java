package com.ecore.roles.api;

import com.ecore.roles.client.model.User;
import com.ecore.roles.mapper.MembershipMapper;
import com.ecore.roles.model.Membership;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.MembershipDto;
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

import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.MockUtils.mockGetUserById;
import static com.ecore.roles.utils.RestAssuredHelper.createMembership;
import static com.ecore.roles.utils.RestAssuredHelper.getMemberships;
import static com.ecore.roles.utils.TestData.*;
import static com.ecore.roles.utils.TestData.RoleEnum.DEVELOPER;
import static com.ecore.roles.utils.TestData.RoleEnum.PRODUCT_OWNER;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Sql(scripts = "/cleanup-data.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MembershipsApiTests {

    private static final EasyRandom generator = new EasyRandom();

    private final RestTemplate restTemplate;

    private final MembershipMapper mapper;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public MembershipsApiTests(final RestTemplate restTemplate, final MembershipMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    void shouldCreateRoleMembership() {

        final MembershipDto model = modelWithRole(DEVELOPER.getId());
        final MembershipDto expected = model.toBuilder().id(randomUUID()).build();

        mockGetUserById(mockServer, model.getUserId(), DEFAULT_USER());
        mockGetTeamById(mockServer, model.getTeamId(), DEFAULT_TEAM());

        final MembershipDto actualMembership = createMembership(model.getRoleId(), model)
                .statusCode(201)
                .extract().as(MembershipDto.class);

        assertThat(actualMembership.getId()).isNotNull();
        assertThat(actualMembership).isEqualTo(expected);
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenBodyIsNull() {
        final UUID roleId = randomUUID();

        createMembership(roleId, null)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenUserIdIsNull() {
        final UUID roleId = randomUUID();
        final Membership expectedMembership = DEFAULT_MEMBERSHIP().toBuilder()
                .userId(null).build();

        createMembership(roleId, mapper.fromModel(expectedMembership))
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamIdISNull() {
        final UUID roleId = randomUUID();
        final Membership expectedMembership = DEFAULT_MEMBERSHIP().toBuilder()
                .teamId(null).build();

        createMembership(roleId, mapper.fromModel(expectedMembership))
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenMembershipAlreadyExistsById() {
        final MembershipDto model = mapper.fromModel(DEFAULT_MEMBERSHIP());

        createMembership(model.getRoleId(), model)
                .validate(400, "Membership already exists");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenMembershipAlreadyExists() {
        final MembershipDto model = mapper.fromModel(DEFAULT_MEMBERSHIP())
                .toBuilder().id(null).build();

        mockGetUserById(mockServer, model.getUserId(), DEFAULT_USER());
        mockGetTeamById(mockServer, model.getTeamId(), DEFAULT_TEAM());

        createMembership(model.getRoleId(), model)
                .validate(400, "Membership already exists");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleDoesNotExist() {
        final UUID roleId = randomUUID();
        final MembershipDto model = modelWithRole(roleId);

        createMembership(roleId, model)
                .validate(404, format("Role %s not found", roleId));
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenUserDoesNotExist() {
        final MembershipDto model = modelWithRole(DEVELOPER.getId());

        mockGetUserById(mockServer, model.getUserId(), null);
        mockGetTeamById(mockServer, model.getTeamId(), DEFAULT_TEAM());

        createMembership(model.getRoleId(), model)
                .validate(404, format("User %s not found", model.getUserId()));
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamDoesNotExist() {
        final MembershipDto model = modelWithRole(DEVELOPER.getId());

        mockGetUserById(mockServer, model.getUserId(), DEFAULT_USER());
        mockGetTeamById(mockServer, model.getTeamId(), null);

        createMembership(model.getRoleId(), model)
                .validate(404, format("Team %s not found", model.getTeamId()));
    }

    @Test
    void shouldFailToAssignRoleWhenMembershipWhenUserDoesNotBelongTeam() {
        final MembershipDto model = modelWithRole(DEVELOPER.getId());

        mockGetUserById(mockServer, model.getUserId(), User.builder().build());
        mockGetTeamById(mockServer, model.getTeamId(), DEFAULT_TEAM());

        createMembership(model.getRoleId(), model)
                .validate(400,
                        "Invalid 'Membership' object. The provided user doesn't belong to the provided team.");
    }

    @Test
    void shouldGetAllMemberships() {
        final UUID roleId = DEVELOPER.getId();
        final MembershipDto expected = mapper.fromModel(DEFAULT_MEMBERSHIP());

        final MembershipDto[] actualMemberships = getMemberships(roleId)
                .statusCode(200)
                .extract().as(MembershipDto[].class);

        assertThat(actualMemberships.length).isEqualTo(1);
        assertThat(actualMemberships[0].getId()).isNotNull();
        assertThat(actualMemberships[0]).isEqualTo(expected);
    }

    @Test
    void shouldFailGetAllMembershipsWhenRoleIsNotFound() {
        final UUID roleId = randomUUID();

        getMemberships(roleId)
                .validate(404,
                        format("Role %s not found", roleId));
    }

    @Test
    void shouldGetAllMembershipsButReturnsEmptyList() {
        final UUID roleId = PRODUCT_OWNER.getId();

        final MembershipDto[] actual = getMemberships(roleId)
                .statusCode(200)
                .extract().as(MembershipDto[].class);

        assertThat(actual.length).isEqualTo(0);
    }

    private static MembershipDto modelWithRole(final UUID roleId) {
        return generator.nextObject(MembershipDto.class)
                .toBuilder().id(null).roleId(roleId).build();
    }

}
