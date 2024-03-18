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

@Intercepts({
	@Signature(type=Executor.class, method="update", args= {MappedStatement.class, Object.class})
})
public class MybatisInterceptor implements Interceptor {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		Object param = args[1];
		
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userName = authentication.getName();

			if (param instanceof HashMap) {
				((HashMap) param).put("_userid", userName);
			}
		}
		return invocation.proceed();
	}
}