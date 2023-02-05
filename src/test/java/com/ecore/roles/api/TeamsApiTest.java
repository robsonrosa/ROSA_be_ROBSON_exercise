package com.ecore.roles.api;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.TeamDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.UUID;

import static com.ecore.roles.utils.MockUtils.*;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamsApiTest {

    private final RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private EasyRandom generator;

    @LocalServerPort
    private int port;

    @Autowired
    public TeamsApiTest(RestTemplate restTemplate) {
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
                .get("/v1/team")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldGetAllTeams() {
        mockGetTeams(mockServer, generator, 500);
        final TeamDto[] teams = getTeams()
                .extract().as(TeamDto[].class);

        assertThat(teams.length).isGreaterThanOrEqualTo(500);
        Arrays.stream(teams).forEach(team -> {
            assertNotNull(team.getId());
            assertNotNull(team.getName());
        });
    }

    @Test
    void shouldGetTeamById() {
        final Team team = generator.nextObject(Team.class)
                .toBuilder()
                .id(randomUUID())
                .build();
        mockGetTeamById(mockServer, team.getId(), team);

        getTeam(team.getId())
                .statusCode(200)
                .body("id", equalTo(team.getId().toString()))
                .body("teamLeadId", equalTo(team.getTeamLeadId().toString()))
                .body("name", equalTo(team.getName()));
    }

    @Test
    void shouldFailToGetTeamByIdWhenNotFound() {
        final UUID id = randomUUID();
        mockGetTeamById(mockServer, id, null);
        getTeam(id)
                .validate(404, format("Team %s not found", id));
    }

}
