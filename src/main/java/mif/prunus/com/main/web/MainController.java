package mif.prunus.com.main.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mif.prunus.com.util.LoginUtil;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {

	@RequestMapping("/")
	public Map<String, Object> main() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("index", "메인");
		map.put("isLogin", LoginUtil.isLogin());
		map.put("userDetails", LoginUtil.getUserDetails());

		List<Map<String, String>> urlList = new ArrayList<Map<String, String>>();
		Map<String, String> urlMap = new HashMap<String, String>();
		urlMap.put("1. url", "/user/loginProc");
		urlMap.put("2. parameters", "userId=&userPwd=");
		urlMap.put("3. Method", "POST");
		urlMap.put("4. direction", "로그인 처리");
		urlMap.put("5. direction1", "원칙적으로 post 방식만 지원");
		urlMap.put("6. direction2", "추가 parameters 협의, 변수 명 변경 가능");
		urlList.add(urlMap);

		urlMap = new HashMap<String, String>();
		urlMap.put("1. url", "/logout");
		urlMap.put("2. parameters", "");
		urlMap.put("3. Method", "POST");
		urlMap.put("4. direction", "로그아웃 처리");
		urlMap.put("5. direction1", "원칙적으로 post 방식만 지원");
		urlMap.put("6. direction2", "");
		urlList.add(urlMap);

		urlMap = new HashMap<String, String>();
		urlMap.put("1. url", "/logout");
		urlMap.put("2. parameters", "");
		urlMap.put("3. Method", "POST");
		urlMap.put("4. direction", "로그아웃 처리");
		urlMap.put("5. direction1", "원칙적으로 post 방식만 지원");
		urlMap.put("6. direction2", "");
		urlList.add(urlMap);

		urlMap = new HashMap<String, String>();
		urlMap.put("1. url", "/front/board/list2");
		urlMap.put("2. parameters", "");
		urlMap.put("3. Method", "GET");
		urlMap.put("4. direction", "샘플-리스트형");
		urlMap.put("5. direction1", "상시 접근 가능");
		urlMap.put("6. direction2", "");

		urlMap = new HashMap<String, String>();
		urlMap.put("1. url", "/user/board/list2");
		urlMap.put("2. parameters", "");
		urlMap.put("3. Method", "GET");
		urlMap.put("4. direction", "샘플-리스트형");
		urlMap.put("5. direction1", "USER Role 로그인 후에 접근 가능");
		urlMap.put("6. direction2", "user/test");
		urlList.add(urlMap);

		urlMap = new HashMap<String, String>();
		urlMap.put("1. url", "/admin/**");
		urlMap.put("2. parameters", "");
		urlMap.put("3. Method", "미구현");
		urlMap.put("4. direction", "샘플-미구현");
		urlMap.put("5. direction1", "ADMIN Role 로그인 후에 접근 가능");
		urlMap.put("6. direction2", "admin/test");
		urlList.add(urlMap);

		map.put("urlList", urlList);
		return map;
	}

	@GetMapping("/expired")
	public Map<String, Object> expired() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("session", "expired");
		map.put("isLogin", LoginUtil.isLogin());
		return map;
	}

	@GetMapping("/invalid")
	public Map<String, Object> invalid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("session", "invalid");
		map.put("isLogin", LoginUtil.isLogin());
		return map;
	}
}
