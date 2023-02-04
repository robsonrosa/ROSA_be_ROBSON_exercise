package com.ecore.roles.api;

import com.ecore.roles.mapper.MembershipMapper;
import com.ecore.roles.model.Membership;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.MembershipDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.RestAssuredHelper.createMembership;
import static com.ecore.roles.utils.RestAssuredHelper.getMemberships;
import static com.ecore.roles.utils.TestData.*;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MembershipsApiTests {

    private final MembershipRepository membershipRepository;

    private final RestTemplate restTemplate;

    private final MembershipMapper mapper;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public MembershipsApiTests(
            MembershipRepository membershipRepository,
            RestTemplate restTemplate,
            MembershipMapper mapper) {
        this.membershipRepository = membershipRepository;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
        membershipRepository.deleteAll();
    }

    @Test
    void shouldCreateRoleMembership() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();

        MembershipDto actualMembership = createDefaultMembership();

        assertThat(actualMembership.getId()).isNotNull();
        assertThat(actualMembership).isEqualTo(mapper.fromModel(expectedMembership));
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenBodyIsNull() {
        createMembership(randomUUID(), null)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenUserIdIsNull() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        expectedMembership.setUserId(null);

        createMembership(randomUUID(), mapper.fromModel(expectedMembership))
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenTeamIdISNull() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        expectedMembership.setTeamId(null);

        createMembership(randomUUID(), mapper.fromModel(expectedMembership))
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenMembershipAlreadyExists() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        createDefaultMembership();

        createMembership(DEVELOPER_ROLE_UUID, mapper.fromModel(expectedMembership))
                .validate(400, "Membership already exists");
    }

    @Test
    void shouldFailToCreateRoleMembershipWhenRoleDoesNotExist() {
        final UUID id = randomUUID();
        Membership expectedMembership = DEFAULT_MEMBERSHIP();

        createMembership(id, mapper.fromModel(expectedMembership))
                .validate(404, format("Role %s not found", id));
    }

    // TODO: remove @Disabled -> implementation required
    @Disabled
    @Test
    void shouldFailToCreateRoleMembershipWhenTeamDoesNotExist() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        mockGetTeamById(mockServer, expectedMembership.getTeamId(), null);

        createMembership(DEVELOPER_ROLE_UUID, mapper.fromModel(expectedMembership))
                .validate(404, format("Team %s not found", expectedMembership.getTeamId()));
    }

    // TODO: remove @Disabled -> implementation required
    @Disabled
    @Test
    void shouldFailToAssignRoleWhenMembershipIsInvalid() {
        Membership expectedMembership = INVALID_MEMBERSHIP();
        mockGetTeamById(mockServer, expectedMembership.getTeamId(), ORDINARY_CORAL_LYNX_TEAM());

        createMembership(DEVELOPER_ROLE_UUID, mapper.fromModel(expectedMembership))
                .validate(400,
                        "Invalid 'Membership' object. The provided user doesn't belong to the provided team.");
    }

    @Test
    void shouldGetAllMemberships() {
        createDefaultMembership();
        Membership expectedMembership = DEFAULT_MEMBERSHIP();

        MembershipDto[] actualMemberships = getMemberships(expectedMembership.getRole().getId())
                .statusCode(200)
                .extract().as(MembershipDto[].class);

        assertThat(actualMemberships.length).isEqualTo(1);
        assertThat(actualMemberships[0].getId()).isNotNull();
        assertThat(actualMemberships[0]).isEqualTo(mapper.fromModel(expectedMembership));
    }

    @Test
    void shouldGetAllMembershipsButReturnsEmptyList() {
        MembershipDto[] actualMemberships = getMemberships(DEVELOPER_ROLE_UUID)
                .statusCode(200)
                .extract().as(MembershipDto[].class);

        assertThat(actualMemberships.length).isEqualTo(0);
    }

    private MembershipDto createDefaultMembership() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        mockGetTeamById(mockServer, expectedMembership.getTeamId(), ORDINARY_CORAL_LYNX_TEAM());

        return createMembership(DEVELOPER_ROLE_UUID, mapper.fromModel(expectedMembership))
                .statusCode(201)
                .extract().as(MembershipDto.class);
    }

}
