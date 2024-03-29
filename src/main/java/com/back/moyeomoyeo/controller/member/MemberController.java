package com.back.moyeomoyeo.controller.member;

import com.back.moyeomoyeo.dto.member.request.MemberRequest;
import com.back.moyeomoyeo.dto.member.response.MemberDuplicateResponse;
import com.back.moyeomoyeo.dto.member.response.MemberResponse;
import com.back.moyeomoyeo.dto.tempNumber.response.SavedTempNumberResponse;
import com.back.moyeomoyeo.security.AuthorizedUser;
import com.back.moyeomoyeo.service.member.MemberService;
import com.back.moyeomoyeo.service.tempNumber.TempNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TempNumberService tempNumberService;


    @PostMapping("/new-member")
    public ResponseEntity<MemberResponse> newUser(@Valid @RequestBody MemberRequest memberRequest) {
        return new ResponseEntity<>(memberService.newUser(memberRequest), HttpStatus.CREATED);
    }

    @GetMapping("/loginid/duplicate")
    public ResponseEntity<MemberDuplicateResponse> isDuplicateLoginId(@RequestParam(name = "loginid") String loginId) {
        return new ResponseEntity<>(memberService.isLoginId(loginId), HttpStatus.OK);
    }

    @GetMapping("/nickname/duplicate")
    public ResponseEntity<MemberDuplicateResponse> isDuplicateNickname(@RequestParam(name = "nickname") String nickname) {
        return new ResponseEntity<>(memberService.isNickname(nickname), HttpStatus.OK);
    }


    @PostMapping("/passwordReset")
    public String createTempNumber(@RequestParam String reqUser) {
        SavedTempNumberResponse savedTempNumberResponse = tempNumberService.savedTempNumber(reqUser);
        return savedTempNumberResponse.getMessage();
    }

    @GetMapping("/passwordReset")
    public boolean matchesTempNumber(@RequestParam String reqUser, @RequestParam String tempNumber) {
        return tempNumberService.matchesTempNumber(reqUser, tempNumber);
    }

    @GetMapping("/")
    public String getLoginId() {
        AuthorizedUser principal = (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getUsername();

    }


}
