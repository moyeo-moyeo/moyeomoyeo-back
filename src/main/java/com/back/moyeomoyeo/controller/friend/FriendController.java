package com.back.moyeomoyeo.controller.friend;

import com.back.moyeomoyeo.dto.friend.request.NewFriendReqProcessRequest;
import com.back.moyeomoyeo.dto.friend.request.NewFriendRequest;
import com.back.moyeomoyeo.dto.friend.response.NewFriendIsRequestResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendResponse;
import com.back.moyeomoyeo.service.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/new-friend")
    public ResponseEntity<NewFriendResponse> newFriendRequest(@RequestBody NewFriendRequest newFriendRequest) {
        return new ResponseEntity<>(friendService.newFriendRequest(newFriendRequest), HttpStatus.OK);
    }

    @GetMapping("/new-friend/request")
    public ResponseEntity<Page<NewFriendIsRequestResponse>> getNewFriendRequest(@PageableDefault(size = 10) Pageable pageable) {
        return new ResponseEntity<>(friendService.getNewFriendRequest(pageable), HttpStatus.OK);
    }

    @PostMapping("/new-friend/request")
    public ResponseEntity<NewFriendResponse> newFriendRequestProcess(@RequestBody NewFriendReqProcessRequest newFriendReqProcessRequest) {
        return new ResponseEntity<>(friendService.newFriendRequestProcess(newFriendReqProcessRequest), HttpStatus.OK);
    }
}
