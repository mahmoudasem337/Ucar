package com.asem.ucar.Auth.mapper;

import com.asem.ucar.Auth.dto.RegisterRequest;
import com.asem.ucar.User.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "ROLE_USER")
    User toUser(RegisterRequest request);
}