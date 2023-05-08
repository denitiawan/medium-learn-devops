package com.deni.app.common.service;

import org.springframework.http.ResponseEntity;

public interface CallBackService {

    public void success(ResponseEntity responseFailed);

    public void failed(ResponseEntity responseFailed);
}
