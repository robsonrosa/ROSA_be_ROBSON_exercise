package com.ecore.roles.utils;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.web.dto.TeamDto;
import com.ecore.roles.web.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class MockUtils {

    public static void mockGetUserById(
            final MockRestServiceServer mockServer,
            final UUID userId,
            final User user) {
        try {
            mockServer.expect(requestTo("http://test.com/users/" + userId))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(user)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void mockGetTeamById(
            final MockRestServiceServer mockServer,
            final UUID teamId,
            final Team team) {
        try {
            mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://test.com/teams/" + teamId))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(
                            withStatus(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ObjectMapper().writeValueAsString(team)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void mockGetUsers(
            final MockRestServiceServer mockServer,
            final EasyRandom generator,
            int size) {
        final List<UserDto> users = generator.objects(UserDto.class, size).collect(toList());
        mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://test.com/users"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ObjectMapper().writeValueAsString(users)));
    }

    @SneakyThrows
    public static void mockGetTeams(
            final MockRestServiceServer mockServer,
            final EasyRandom generator,
            int size) {
        final List<TeamDto> teams = generator.objects(TeamDto.class, size).collect(toList());
        mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://test.com/teams"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ObjectMapper().writeValueAsString(teams)));
    }
}
