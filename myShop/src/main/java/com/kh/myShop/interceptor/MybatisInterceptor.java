package com.kh.myShop.interceptor;

import java.util.HashMap;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Signature 어노테이션은 인터셉터가 적용될 대상을 정의
 * 이 인터셉터는 Executor의 update 메서드 호출 시에 동작
 * update 메서드는 SQL의 INSERT, UPDATE, DELETE 등의 DML(데이터 조작 언어)문을 실행할 때 호출된다.
 */

@Intercepts({@Signature(type=Executor.class, method="update", args= {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

	/**
	 * MyBatis의 인터셉트 메서드
	 * 특정 메서드가 호출될 때 추가적인 로직을 수행합니다.
	 *
	 * @param invocation 호출된 메서드와 관련된 정보를 포함하는 Invocation 객체
	 * @return 메서드의 반환값
	 * @throws Throwable 예외 발생 시
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		Object param = args[1];
		
		// 보안 컨텍스트에서 사용자 인증 정보를 가져와서 _userid 파라미터로 추가합니다.
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userName = authentication.getName();
			
			// 파라미터가 HashMap인 경우 _userid 파라미터를 추가합니다.
			if (param instanceof HashMap) {
				((HashMap) param).put("_userid", userName);
			}
		}
		return invocation.proceed(); // 메서드를 계속 진행합니다.
	}
}