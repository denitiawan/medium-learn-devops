package com.deni.app.module.user.service;

import com.deni.app.common.constants.Messages;
import com.deni.app.common.controller.ResponseHandler;
import com.deni.app.common.validator.Validator;
import com.deni.app.module.user.dto.UserDTO;
import com.deni.app.module.user.entity.User;
import com.deni.app.module.user.repo.UserRepo;
import com.deni.app.module.user.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserValidator userValidator;

    // for unitest (mocking)
    public UserServiceImpl(UserValidator userValidator, UserRepo userRepo) {
        this.userValidator = userValidator;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public ResponseEntity save(UserDTO requestDTO) {
        Validator validator = userValidator.requestValidator(requestDTO);
        if (validator.isSuccess()) {
            validator = userValidator.duplicateValidator(requestDTO.getUsername());
            if (validator.isSuccess()) {

                User entity = User.builder()
                        .username(requestDTO.getUsername())
                        .password(requestDTO.getPassword())
                        .permissions(requestDTO.getPermissions())
                        .roles(requestDTO.getRoles())
                        .active(1)
                        .blocked(0)
                        .build();

                User save = userRepo.save(entity);

                // OK
                return ResponseHandler.createHttpResponse(
                        Messages.MSG_SAVE_SUCCESS,
                        new UserDTO().of(save),
                        HttpStatus.OK);

            } else {
                return ResponseHandler.createHttpResponse(
                        validator.getMessage(),
                        requestDTO,
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseHandler.createHttpResponse(
                    validator.getMessage(),
                    requestDTO,
                    HttpStatus.BAD_REQUEST);
        }


    }

    @Override
    @Transactional
    public ResponseEntity update(Long id, UserDTO requestDTO) {
        Validator validator = userValidator.requestValidatorForUpdate(requestDTO);
        if (validator.isSuccess()) {
            Optional<User> optional = userRepo.findById(id);
            if (optional.isPresent()) {


                User update = (User) optional.get();
                update.setUsername(requestDTO.getUsername());
                // password will update on change password api
                update.setPermissions(requestDTO.getPermissions());
                update.setRoles(requestDTO.getRoles());
                update.setBlocked(requestDTO.getBlocked());
                update.setActive(requestDTO.getActive());
                update = userRepo.save(update);


                // OK
                return ResponseHandler.createHttpResponse(
                        Messages.MSG_UPDATE_SUCCESS,
                        new UserDTO().of(update),
                        HttpStatus.OK);

            } else {
                return ResponseHandler.createHttpResponse(
                        Messages.MSG_DATA_NOT_FOUND,
                        requestDTO,
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return ResponseHandler.createHttpResponse(
                    validator.getMessage(),
                    requestDTO,
                    HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    @Transactional
    public ResponseEntity getAll() {
        List<User> list = userRepo.findAll();
        if (!list.isEmpty()) {

            // OK
            return ResponseHandler.createHttpResponse(
                    Messages.MSG_DATA_FOUND,
                    new UserDTO().listOf(list),
                    HttpStatus.OK);
        } else {
            return ResponseHandler.createHttpResponse(
                    Messages.MSG_DATA_NOT_FOUND,
                    "",
                    HttpStatus.NOT_FOUND);
        }
    }


    @Override
    @Transactional
    public ResponseEntity findById(Long id) {
        Optional<User> optional = userRepo.findById(id);
        if (optional.isPresent()) {

            // OK
            return ResponseHandler.createHttpResponse(
                    Messages.MSG_DATA_FOUND,
                    new UserDTO().of(optional.get()),
                    HttpStatus.OK);

        } else {
            return ResponseHandler.createHttpResponse(
                    Messages.MSG_DATA_NOT_FOUND,
                    "",
                    HttpStatus.NOT_FOUND);
        }
    }


    @Override
    @Transactional
    public ResponseEntity delete(Long id) {
        Optional<User> optional = userRepo.findById(id);
        if (optional.isPresent()) {
            userRepo.delete(optional.get());

            // OK
            return ResponseHandler.createHttpResponse(
                    Messages.MSG_DELETE_SUCCESS,
                    optional.get(),
                    HttpStatus.OK);

        } else {
            return ResponseHandler.createHttpResponse(
                    Messages.MSG_DATA_NOT_FOUND,
                    "",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteAll() {
        userRepo.deleteAll();

        // OK
        return ResponseHandler.createHttpResponse(
                Messages.MSG_DELETE_SUCCESS,
                "",
                HttpStatus.OK);
    }
}
