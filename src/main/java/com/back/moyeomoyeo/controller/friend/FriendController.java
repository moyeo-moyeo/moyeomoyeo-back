package com.back.moyeomoyeo.controller.friend;

import com.back.moyeomoyeo.dto.friend.request.NewFriendReqProcessRequest;
import com.back.moyeomoyeo.dto.friend.request.NewFriendRequest;
import com.back.moyeomoyeo.dto.friend.response.FriendListResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendIsRequestResponse;
import com.back.moyeomoyeo.dto.friend.response.NewFriendResponse;
import com.back.moyeomoyeo.security.AuthorizedUser;
import com.back.moyeomoyeo.service.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/new-friend")
    public ResponseEntity<NewFriendResponse> newFriendRequest(@AuthenticationPrincipal AuthorizedUser authorizedUser, @RequestBody NewFriendRequest newFriendRequest) {
        return new ResponseEntity<>(friendService.newFriendRequest(authorizedUser, newFriendRequest), HttpStatus.OK);
    }

    @GetMapping("/new-friend/request")
    public ResponseEntity<Page<NewFriendIsRequestResponse>> getNewFriendRequest(@AuthenticationPrincipal AuthorizedUser authorizedUser, @PageableDefault(size = 10) Pageable pageable) {
        return new ResponseEntity<>(friendService.getNewFriendRequest(authorizedUser, pageable), HttpStatus.OK);
    }

    @PostMapping("/new-friend/request")
    public ResponseEntity<NewFriendResponse> newFriendRequestProcess(@AuthenticationPrincipal AuthorizedUser authorizedUser, @RequestBody NewFriendReqProcessRequest newFriendReqProcessRequest) {
        return new ResponseEntity<>(friendService.newFriendRequestProcess(authorizedUser, newFriendReqProcessRequest), HttpStatus.OK);
    }

    @GetMapping("/friend-list")
    public Slice<FriendListResponse> friends(Pageable pageable) {
        return friendService.friends(pageable);
    }
}
