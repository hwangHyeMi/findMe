package mif.prunus.com.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mif.prunus.com.mbr.service.MbrService;
import mif.prunus.com.mbr.vo.CustomUserDetails;
import mif.prunus.com.mbr.vo.MbrVO;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

	@Autowired
	private MbrService mbrService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MbrVO vo = new MbrVO();
		vo.setMbrId(username);
		CustomUserDetails userDetails = (CustomUserDetails) mbrService.selectMbrChk(vo).orElseThrow(() -> new UsernameNotFoundException("없는 회원 입니다..."));
		// return User.builder().username(member.getUsername()).password(member.getPassword()).roles(member.getRole().name()).build();
		return userDetails;
	}


}