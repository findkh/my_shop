package com.kh.myShop.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.kh.myShop.interceptor.MybatisInterceptor;


@Configuration //Bean들을 생성하여 스프링 컨테이너에 등록
@MapperScan(basePackages="com.kh.myShop") // MyBatis Mapper 인터페이스가 위치한 패키지를 스캔하여 빈으로 등록
@EnableTransactionManagement // 트랜잭션 관리를 활성화
public class DatabaseConfig {

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		//DataSource: 데이터베이스 연결을 위한 DataSource를 설정.
		//데이터베이스와의 연결 정보가 설정된 DataSource를 SqlSessionFactory에 주입하여 데이터베이스와의 연결을 설정한다.
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource); // 데이터베이스 연결을 위한 DataSource를 설정
		sessionFactory.setPlugins(new MybatisInterceptor()); // MyBatis에서 발생하는 SQL 실행 전후의 동작을 제어하기 위한 인터셉터를 설정
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources("mapper/*.xml")); // Mapper XML 파일의 위치를 설정
		// -> MyBatis에서 SQL 매핑 정보를 XML 파일로 관리하기 때문에, XML 파일이 위치한 경로를 설정

		return sessionFactory.getObject(); // SqlSessionFactory 빈을 반환
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory); // SqlSession을 포함하는 템플릿을 설정
		return sqlSessionTemplate; // SqlSessionTemplate 빈을 반환
	}
}