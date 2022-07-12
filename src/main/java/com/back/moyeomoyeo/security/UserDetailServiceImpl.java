package com.back.moyeomoyeo.security;

import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member byLoginId = memberRepository.findByLoginId(username);

        /*TODO
        *  -상위 에러클래스가 있으므로, 해당 메서드 커스텀에러처리 해야됩니다.*/
        if(byLoginId == null)
            throw new UsernameNotFoundException(username);

        return new AuthorizedUser(byLoginId);
    }
}
