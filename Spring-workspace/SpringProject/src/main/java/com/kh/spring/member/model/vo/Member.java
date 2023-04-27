package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//import lombok.Data;
//import lombok.AllArgsConstructor;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;

//@NoArgsConstructor  //매개변수가 없는 기본생성자
//@AllArgsConstructor //모든 필드를 매개변수로 갖는 생성자
//@Setter 		    //각각의 필드에 대한 setter메소드를 자동으로 만들어줌
//@Getter				//각각의 필드에 대한 getter메소드를 자동으로 만들어줌
//@ToString			//ToString 자동으로 만들어줌
//@EqualsAndHashCode	//equals, hashcode자동으로 만들어줌

/*
 * lombok
 * -자동으로 코드 생성해주는 라이브러리
 * -반복되는 getter, setter, toString, 생성자 메소드 작성등을 줄여주는 역할의 코들 ㅏ이브러리
 * 
 * lombok의 설치방법
 * -1. 라이브러리 다운 후 pom.xml에 추가
 * -2. 다운로드된 jar파일을 찾아서 실행(ide가 켜져있으면 안된다.)
 * -3. ide재실행
 * 
 * lombok사용시 주의 사항
 * -uName, bTitle등 과 같이 앞을자가 소문자가 외자인 필드명은 만들면 안됨
 * -필드명 작성시 소문자 두글자 이상으로 시작해야함
 * -el표기법 사용시 내부적으로 getter메소드를 찾게되는데 이때 getuName(), getbTitle()이라는 이름으로 메소드를 호출하기 때문에
 * 
 */
@Getter				
@Setter 		    
@ToString	
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
	private int userNo;
	private String userId;
	private String userPwd;
	private String nickName;
	private String phone;
	private String address;
	private Date enrollDate;
	private Date modifyDate;
	private String status;
	private String profileImage;

	
	
	
}
