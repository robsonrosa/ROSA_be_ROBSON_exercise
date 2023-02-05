package com.ecore.roles.utils;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;

import java.util.UUID;

import static com.ecore.roles.utils.TestData.RoleEnum.DEVELOPER;
import static java.util.UUID.fromString;

public class TestData {

    private static final EasyRandom generator = new EasyRandom();

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum RoleEnum {

        DEVELOPER(fromString("1b3c333b-36e7-4b64-aa15-c22ed5908ce4"), "Developer"),

        PRODUCT_OWNER(fromString("25bbb7d2-26f3-11ec-9621-0242ac130002"), "Product Owner"),

        TESTER(fromString("37969e22-26f3-11ec-9621-0242ac130002"), "Tester");

        private final UUID id;

        private final String name;

        public Role get() {
            return Role.builder()
                    .id(id)
                    .name(name)
                    .build();
        }

    }

    public static final UUID DEFAULT_USER_UUID = fromString("fd282131-d8aa-4819-b0c8-d9e0bfb1b75c");

    public static final UUID DEFAULT_TEAM_UUID = fromString("7676a4bf-adfe-415c-941b-1739af07039b");

    public static final UUID DEFAULT_MEMBERSHIP_UUID = fromString("98de61a0-b9e3-11ec-8422-0242ac120002");

    public static Team DEFAULT_TEAM() {
        final Team team = generator.nextObject(Team.class)
                .toBuilder().id(DEFAULT_TEAM_UUID).build();
        team.getTeamMemberIds().add(DEFAULT_USER_UUID);
        return team;
    }

    public static User DEFAULT_USER() {
        return generator.nextObject(User.class)
                .toBuilder().id(DEFAULT_USER_UUID).build();
    }

    public static Membership DEFAULT_MEMBERSHIP() {
        return Membership.builder()
                .id(DEFAULT_MEMBERSHIP_UUID)
                .role(DEVELOPER.get())
                .userId(DEFAULT_USER_UUID)
                .teamId(DEFAULT_TEAM_UUID)
                .build();
    }

}
