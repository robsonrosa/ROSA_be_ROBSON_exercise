package com.ecore.roles.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.ObjectUtils.isEmpty;

public abstract class AbstractMapper<Model, Dto> {

    private final TypeToken<Model> modelTypeToken = new TypeToken<Model>(getClass()) {};
    private final TypeToken<Dto> dtoTypeToken = new TypeToken<Dto>(getClass()) {};
    private final Class<Model> modelType = (Class<Model>) modelTypeToken.getType();
    private final Class<Dto> dtoType = (Class<Dto>) dtoTypeToken.getType();

    protected abstract ObjectMapper getMapper();

    public List<Dto> fromModelList(final List<Model> modelList) {
        return convertList(modelList, this::fromModel);
    }

    public List<Model> fromDtoList(final List<Dto> dtoList) {
        return convertList(dtoList, this::fromDto);
    }

    public Dto fromModel(Model model) {
        return from(model, this::toDto);
    }

    public Model fromDto(Dto dto) {
        return from(dto, this::toModel);
    }

    private <T, R> R from(final T object, final Function<T, R> function) {
        return ofNullable(object)
                .map(function::apply)
                .orElse(null);
    }

    private Dto toDto(final Model model) {
        return getMapper().convertValue(model, dtoType);
    }

    private Model toModel(final Dto dto) {
        return getMapper().convertValue(dto, modelType);
    }

    private <T, R> List<R> convertList(final List<T> list, final Function<T, R> function) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        return list.stream()
                .map(function::apply)
                .collect(toList());
    }

}
