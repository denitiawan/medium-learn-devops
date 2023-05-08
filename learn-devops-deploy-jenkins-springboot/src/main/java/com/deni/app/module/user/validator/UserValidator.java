package com.deni.app.module.user.validator;

import com.deni.app.common.constants.Messages;
import com.deni.app.common.validator.Validator;
import com.deni.app.module.user.dto.UserDTO;
import com.deni.app.module.user.entity.User;
import com.deni.app.module.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserValidator {

    @Autowired
    UserRepo repo;

    public Validator duplicateValidator(String uniqueKey) {
        User entity = repo.findByUsername(uniqueKey);
        if (entity != null) {
            return new Validator().no(String.format(Messages.MSG_DATA_ALREADY_EXIST, entity.getUsername()));
        } else {
            return new Validator().yes(Messages.MSG_DATA_IS_AVAILABLE);
        }
    }

    public Validator requestValidator(UserDTO dto) {
        List<String> message = new ArrayList<>();

        if (dto.getUsername() == null) {
            message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_NULL, "username"));
        } else {
            if (dto.getUsername().isEmpty()) {
                message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_EMPTY, "username"));
            }
        }


        if (dto.getPassword() == null) {
            message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_NULL, "password"));
        } else {
            if (dto.getPassword().isEmpty()) {
                message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_EMPTY, "password"));
            }
        }


        if (message.isEmpty()) {
            return new Validator().yes();
        } else {
            String result = "";
            for (String str : message) {
                if (result.isEmpty()) {
                    result = str;
                } else {
                    result += ", " + str;
                }
            }
            return new Validator().no(result);
        }
    }


    public Validator requestValidatorForUpdate(UserDTO dto) {
        List<String> message = new ArrayList<>();

        if (dto.getUsername() == null) {
            message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_NULL, "username"));
        } else {
            if (dto.getUsername().isEmpty()) {
                message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_EMPTY, "username"));
            }
        }


        if (dto.getActive() == null) {
            message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_NULL, "active"));
        } else {
            if (dto.getActive() > 1) {
                message.add(String.format(Messages.MSG_FIELD_REQUIRED_ARE_1_OR_0, "active"));
            }
        }

        if (dto.getBlocked() == null) {
            message.add(String.format(Messages.MSG_FIELD_CANNOT_BE_NULL, "active"));
        } else {
            if (dto.getBlocked() > 1) {
                message.add(String.format(Messages.MSG_FIELD_REQUIRED_ARE_1_OR_0, "active"));
            }
        }


        if (message.isEmpty()) {
            return new Validator().yes();
        } else {
            String result = "";
            for (String str : message) {
                if (result.isEmpty()) {
                    result = str;
                } else {
                    result += ", " + str;
                }
            }
            return new Validator().no(result);
        }
    }

}
