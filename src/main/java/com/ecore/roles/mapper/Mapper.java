package com.ecore.roles.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.ObjectUtils.isEmpty;

@FunctionalInterface
public interface Mapper<T, R> extends Function<T, R> {

    default List<R> applyList(final List<T> userDtoList) {
        if (isEmpty(userDtoList)) {
            return new ArrayList<>();
        }

        return userDtoList.stream()
                .map(this::apply)
                .collect(toList());
    }

}
