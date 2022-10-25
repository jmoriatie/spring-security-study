package hello.security.controller;

import hello.security.dto.UserInfoDto;
import hello.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(@ModelAttribute UserInfoDto infoDto){
        userService.save(infoDto);
        return "redirect:/login";
    }

//    Spring Security 설정 파일에서 로그아웃 관련 설정을 하긴 했지만,
//    이 로그아웃은 POST요청에 csrf를 보내야 하기 때문에 직접 패스를 치면 404 에러가 뜹니다.
//    그렇기 때문에 GET 요청으로 로그아웃을 해도 로그아웃이 가능하게 구현해보겠습니다.
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
//        기본으로 제공해주는 SecurityContextLogoutHandler()의 logout()을 사용해서 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";

    }
}
