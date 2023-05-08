package com.deni.app.module.user.service;

import com.deni.app.module.user.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface UserService {


    public ResponseEntity save(UserDTO requestDTO);


    public ResponseEntity update(Long id, UserDTO requestDTO);

    public ResponseEntity delete(Long id);


    public ResponseEntity getAll();


    public ResponseEntity findById(Long id);


    public ResponseEntity deleteAll();


}
