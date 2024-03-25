package com.oauth2.tutorial.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users") // User라는 Entity명을 사용했는데 User라는 단어가 예약어라서 발생하던 예외였습니다. 따라서 @Table 주석으로 테이블 이름을 변경하는 방법으로 해결하였습니다.
@Entity
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
    private Long id;

	@Column(updatable = false, unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String name;

    @Column
    private String picture;

    private String nickname;
    
    @Enumerated(EnumType.STRING) // JPA로 데이터베이스로 저장할 때 Enum 값을 어떤 형태로 저장할지를 결정한다. 기본적으로는 int로 된 숫자가 저장된다. 숫자로 저장되면 데이터베이스로 확인할 때 그 값이 무슨 코드를 의미하는지 알 수가 없다. 그래서 문자열(EnumType.STRING)로 저장될 수 있도록 선언한다.
    @Column(nullable = false)
    private Role role;
    
	public User update(String name, String picture) {
	    this.name = name;
	    this.picture = picture;
	
	    return this;
	}
}
