package com.ecore.roles.service.impl;

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
class UsersServiceImpl implements UsersService {

    private final UsersClient client;

    public User getUser(final UUID id) {
        return ofNullable(client.getUser(id).getBody())
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

    public List<User> getUsers() {
        return client.getUsers().getBody();
    }
}
