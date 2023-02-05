package com.ecore.roles.repository;

import com.ecore.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByName(String name);

    @Query("SELECT r FROM Role r JOIN r.memberships m WHERE m.userId = :userId AND m.teamId = :teamId")
    List<Role> findByMembershipsContainingUserIdAndTeamId(
            @Param("userId") UUID userId,
            @Param("teamId") UUID teamId);

}
