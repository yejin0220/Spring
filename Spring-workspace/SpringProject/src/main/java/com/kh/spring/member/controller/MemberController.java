package com.kh.spring.member.controller;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.service.MemberServiceImpl;
import com.kh.spring.member.model.vo.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller		//생성된 bean객체가 Controller임을 명시
@RequestMapping("/member")	//localhost:8081/spring/member이하의 url요청을 처리하는 컨트롤러

//로그인, 회원가입 기능 완료 후 실행될 코드
@SessionAttributes({"loginMember"})

public class MemberController {
	
	private MemberService ms = new MemberServiceImpl();
	//기존 객체 생성방식 . 서비스가 동시에 많은 횟수의 요청이 들어오면 그만큼의 객체가 생성
	//객체간의 결합도가 올라감.
	
	//spring의 DI(Dependency Injection) => 객체를 스프링에서 생성해서 주입을 해주는 개념
	//new연산자를 쓰지 않고 필드선언만 한 후 @Autowried라는 어노테이션을 붙여서 내가 필요로 하는 객체를 스프링 컨테이너로부터 주입받을 수 있음.
	
	/*
	 * Autowried를 통한 객체 주입방법
	 * 1. 필드방식 의존성 주입(필드위에 바로 어노테이션붙이는것)
	 * 2. 생성자방식 의존성 주입
	 * 3. setter방식 의존성 주입
	 * 
	 * 
	 * 1)필드방식 의존성
	 * 필드방식 의존성 주입 장점 : 이해하기 편하다, 사용하기 편하다
	 * 
	 * 필드방식 의존성 주입 단점 1)순환 의존성 문제가 발생할 수 있다.
	 * 					  2)무분별한 주입시 의존관계 확인이 어렵다.
	 */
	@Autowired	//bean으로 등록된 객체 중 타입이 같거나, 상속관계인 bean을 자동으로 주입해주는 역할
	private MemberService memberService;
	
	
	/*
	 * 2)생성자방식 의존성 주입
	 * 생성자를 통한 의존성 주입 : 생성자에 참조할 클래스를 인자로 받아 필드에 매핑시킴
	 * 
	 * 장점 : 1. 현재 클래스에서 내가 주입시킬 객체들을 모아서 관리할 수 있기 때문에 한눈에 알아보기 편하다
	 * 	 	 2. 코드분석과 테스트에 유리하며, 객체주입시 가장 권장하는 방법
	 * 
	 */
	/* @Autowired => 생성자가 하나일 경우 생략 가능, 여러개라면 반드시 Autowried 어노테이션을 추가해야함*/
	public MemberController(MemberService memberService) {
		this.memberService=memberService;
	}
	
	public MemberController() {
		
	}
	
	/*
	 * 3)setter방식의존성 주입
	 * Setter주입방식 : setter메서드로 빈을 주입받는 방식
	 * 
	 * 생성자에 너무 많은 의존성을 주입하게되면 알아보기 힘들다라는 단점이 있어서 보완하기 위해 사용
	 * 필요할때마다 의존성을 주입받아서 사용할때, 즉 의존성 주입이 필수가 아닌 선택사항일때 사용
	 */
	@Autowired
	public void setMemberService(MemberService memberService) {
		this.memberService=memberService;
	}
	
	/*
	 * 스프링에는 Argument Resolver라는 매개변수를 유연하게 처리해주는 해결사클래스가 내장되어있음
	 * Argument Resolver : 클라이언트가 전달한 파라미터 중 매개변수의 조건과 일치하는 파라미터가 있다면 해당 객체를 매개변수로 바인딩해준다.
	 * 
	 * 스프링에서 parameter(요청시 전달값)를 받는 방법
	 * 1)HttpServletRequest를 이용해서 전달받기(jsp프로젝트에서 하던 방식 그대로)
	 * 해당 메소드의 매개변수로 HttpServletRequest를 작성해놓으면 스프링컨테이너(정확히는 Argument Resolver)가 
	 * 해당 메소드를 호출할떄 자동으로 request객체를 생성해서 매개변수로 주입해준다.
	 * 
	 */
//	@RequestMapping("/login")
//	public String loginMember(HttpServletRequest request) {
//		String userId = request.getParameter("userId");
//		String userPwd = request.getParameter("userPwd");
//		
//		System.out.println(userId);
//		System.out.println(userPwd);
//		
//		return "main";
//	}
	
	
	
	/*
	 * 2. @RequestParam 어노테이션을 이용하는 방법.
	 * requset.getParameter("키")로 뽑는 역할을 대신 수행해주는 어노테이션
	 * jsp에서 작성했던 input태그의 name속성값을 value로 입력해주면, 알아서 매개변수로 담아온다.
	 * 만약 넘어온 값이 비어있다면 defaultValue로 기본값 설정 가능
	 * 
	 * 사용가능속성
	 * value : input태그의 name속성값으로 다른 속성을 작성하지 않은 경우 기본값으로 활용된다.
	 * 		   @RequestParam("url"), @RequestParam(vlaue="url")
	 * required : 입력된 name속성값이 필수적으로 파라미터에 포함되어야 하는 지를 지정(required = true /기본값)
	 * 			  required가 true일때 파라미터가 없으면 400에러를 반환(잘못된 요청=> bad-request)
	 * 			  required가 false일때 파라미터가 없으면 그냥 null값이 들어감
	 * 
	 * defaultValue : required가 false인 상태에서 파라미터가 존재하지 않을 경우의 값을 지정
	 * 
	 */
//	@RequestMapping("/login")
//	public String loginMember(@RequestParam(value="userId", defaultValue="!!!") String userId,
//							  @RequestParam(value="userPwd") String userPwd)                  {
//		
//		System.out.println(userId);
//		System.out.println(userPwd);
//		return "main";
//	}
	
	
	
	/*
	 * 3. @RequestParam 어노테이션을 생략하는 방법
	 * 단, 매개변수명을 jsp의 name속성값(요청시 전달한 키값)과 동일하게 작성해줘야 하낟.
	 * 또한, 위에서 작성했던 나머지 속성들 사용 불가
	 * 
	 */
//	@RequestMapping("/login")
//	public String loginMember(String userId, String userPwd) {
//		System.out.println(userId);
//		System.out.println(userPwd);
//		return "main";
//	}
//	
	
	/*
	 * 4. @ModelAttribute 어노테이션을 사용하는 방법
	 * [작성법]
	 * @ModelAttribute VO타입 변수명
	 * 매개변수로 @ModelAttribute사용시 파라미터 중 name속성 값이 vo클래스의 필드와 일치하면 해당 VO클래스의 필드에 값을 셋팅함
	 * 
	 * [작동원리]
	 * -스프링컨테이너가 해당 객체를 (기본생성자)로 생성 후 내부적으로(stter메서드)를 찾아서 요청시 전달한 값중 name값과 일치하는 필드에 name속성값을 담아줌
	 * -따라서 @ModelAttribute를 사용하기 위해선 기본생성자와+setter메서드는 반드시 필요함
	 * 
	 * @ModelAttribute => 생략 가능
	 * @ModelAttribute생략시 해당 객체를 커맨드객체라고 부른다
	 */
//	@RequestMapping("/login")
//	public String loginMember(Member m, Model model) {
//		
//		//요청처리 후 응답데이터를 담고, 응답페이지로 포워딩 또는 url재요청하는 법
//		//1)Model객체를 이용하는 방법
//		//포워딩할 응답뷰로 전달하고자하는 데이터를 맵형식으로 담을 수 있는 영역 -> Model객체 (requestScope)
//		//Model : 데이터를 맵형식(key:value)으로 담아 전달하는 용도의 객체
//		//request, session을 대체하는 객체
//
//		//model클래스안의 addAttribute()메서드를 이용하는 방법
//		model.addAttribute("errorMsg","오류발생"); // ==  request.setAttribute("errorMsg","오류발생");
//		
//		//Model의 기본 scope는 request scope임/단 session scope로 변환하고 싶은 경우
//		//클래스 레벨로 @SessionAttributes를 작성하면 된다.
//		model.addAttribute("loginMember",m);	//== request.getSession().setAttribute()
//		//단 SessionAttributes에 loginMember가 있을 경우에만 이동가능
//		
//		System.out.println(m);
//		return "main";
//	}
	
	//2)ModelAndView 객체 이용
	//ModelAndView에서 Model은 데이터를 key-value담을 수 있는 Model객체를 의미
	//View는 응답뷰에 대한 정보를 담을 수 있다. 이때는 리턴 타입이 String이 아닌 modelAndView로 전달해야함
	//Model + View가 결합된 형태의 객체 Model객체는 단독사용이 가능하지만, View는 불가능함
	//mv.addObject("errorMsg","로그인실패");
	//mv.setViewName("common/errorPage");
	//return mv;
	
	/*
	 * RequestMapping?
	 * 클라이언트의 요청(url)에 맞는 클래스 or 메서드를 연결시켜주는 어노테이션
	 * 해당 어노테이션이 붙은 클래스/메서드를 스프링 컨테이너가 handlerMapping으로 등록해둔다
	 * 
	 * HandlerMapping ?
	 * 사용자가 지정한 url정보들을 모아둔 저장소
	 * -> 클래스레벨에서 사용된 경우 : 공통주소로 활용됨(만약 현재 클래스의 공통주소인 member로 요청이 들어오면 현재 MemberController가 직접 요청처리를 하게 됨)
	 * -> 메서드레벨에서 사용된 경우 : 공통주소 외 나머지 주소
	 * 							ex)localhost:8081/spring/member(공통주소, 클래스레벨에서 선언)/login(그외주소, 메소드레벨에서 선언)
	 * 단, 클래스레벨에 @RequestMapping이 존재하지 않는다면 메서드레벨에서 단독으로 요청을 처리한다(jsp에서 하던방식)
	 * 
	 * [작성법]
	 * 1) @RequestMapping("url") -> url주소만 붙이는 경우 요청방식(get/post)과 관계없이 일치하는 url에 대하여 요청처리한다.
	 * 2) @RequestMapping(value="url", method=RequestMethod.GET/POST) => 일치하는 URL+요청방식을 함께 검사하여 요청처리
	 * 
	 * 단, 일반적으로 메서드 레벨에서는 GET/POST방식을 구분할때
	 * @GetMapping("url") / @PostMapping("url")을 사용하는게 일반적임.
	 */
	@PostMapping("/login")
	public ModelAndView loginMember(ModelAndView mv ,  Member m , HttpSession session, RedirectAttributes ra, HttpServletResponse resp, HttpServletRequest req, 
									@RequestParam(value="saveId", required=false)String saveId) {
		
		Member loginUser = memberService.loginMember(m);
		if(loginUser == null) {
			//실패
			mv.addObject("errorMsg","로그인 실패");
			mv.setViewName("common/errorPage");
		}else {
			//성공
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/");
			//메인페이지로 url재요청
			//== response.sendRedirect(request.getContextPath());
			//== return "main";
		}
		
		return mv;
	}
	
	
	
	
	

}
