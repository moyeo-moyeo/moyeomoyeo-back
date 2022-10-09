package com.back.moyeomoyeo.controller.promise;

import com.back.moyeomoyeo.dto.promise.reqeust.PromiseGeneratedRequest;
import com.back.moyeomoyeo.dto.promise.response.PromisePlaceResponse;
import com.back.moyeomoyeo.security.AuthorizedUser;
import com.back.moyeomoyeo.service.promise.PromiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PromiseController {

    private final PromiseService promiseService;

    @PostMapping("/promise")
    public PromisePlaceResponse promiseGenerated(@AuthenticationPrincipal AuthorizedUser authorizedUser, @RequestBody PromiseGeneratedRequest promiseGeneratedRequest) {
        return promiseService.promiseGenerated(authorizedUser, promiseGeneratedRequest);
    }

}
