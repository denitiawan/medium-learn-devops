package com.deni.app.module.user.dto;

import com.deni.app.module.user.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    Long id;
    String username;
    String password;
    String roles;
    String permissions;
    Integer blocked;
    Integer active;


    public List<UserDTO> listOf(List<User> list) {

        List<UserDTO> result = new ArrayList<>();
        for (User entity : list) {
            result.add(of(entity));
        }
        return result;
    }

    public UserDTO of(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .permissions(entity.getPermissions())
                .roles(entity.getRoles())
                .active(entity.getActive())
                .blocked(entity.getBlocked())
                .build();
    }


}
