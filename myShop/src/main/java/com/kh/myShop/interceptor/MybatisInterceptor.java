package com.kh.myShop.interceptor;

import java.util.HashMap;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kh.myShop.login.LoginEntity;

/**
 * MyBatis 쿼리 메서드를 인터셉트하여 보안 및 사용자 관련 추가 기능을 제공하는 Interceptor
 * 주로 사용자 인증 정보를 파라미터에 추가하는 역할을 수행
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, 
		Object.class, 
		RowBounds.class, 
		ResultHandler.class})}) // Executor의 query 메서드 시그니처에 RowBounds 및 ResultHandler 추가
public class MybatisInterceptor implements Interceptor {

	/**
	 * MyBatis의 query 메서드를 인터셉트하여 보안 및 사용자 관련 추가 기능을 수행
	 * 보안 컨텍스트에서 사용자 인증 정보를 가져와서 _userid 및 _shopId 파라미터를 추가
	 *
	 * @param invocation 호출된 메서드와 관련된 정보를 포함하는 Invocation 객체
	 * @return 메서드의 반환값
	 * @throws Throwable 예외 발생 시
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		Object param = args[1];

		// 보안 컨텍스트에서 사용자 인증 정보를 가져와서 _userid 및 _shopId 파라미터를 추가
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			LoginEntity user = (LoginEntity) authentication.getPrincipal();
			String userName = authentication.getName();

			if (param instanceof HashMap) {
				((HashMap) param).put("_userid", userName);
				((HashMap) param).put("_shopId", user.getShop_id());
			}
		}
		return invocation.proceed(); // 메서드를 계속 진행
	}
}
