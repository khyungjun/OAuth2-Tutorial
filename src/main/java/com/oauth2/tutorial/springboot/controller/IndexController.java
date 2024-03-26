package com.oauth2.tutorial.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.oauth2.tutorial.springboot.dto.SessionUser;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final HttpSession httpSession;
	
	@GetMapping("/")
	public String index(Model model) {

		SessionUser user = (SessionUser) httpSession.getAttribute("user"); // 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성했다. 즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있다.
		
		if(user != null) { // 세션에 저장된 값이 있을 때만 model에 userName으로 등록한다. 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보이게 된다.
			model.addAttribute("userName", user.getName());
		}
		
		return "index";
	}
	
}
