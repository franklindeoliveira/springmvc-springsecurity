# Howtos

Autenticação e autorização com Spring Security: sistema de login, logout e controle de acesso.

---

pom.xml
``` xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-core</artifactId>
    <version>4.0.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
    <version>4.0.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-web</artifactId>
    <version>4.0.1.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-taglibs</artifactId>
    <version>4.0.1.RELEASE</version>
</dependency>
```
web.xml
``` xml
  <listener>
	    <listener-class>
	        org.springframework.web.context.ContextLoaderListener
	    </listener-class>
	</listener>
	
	<!-- Spring Security -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/spring-security.xml</param-value>
	</context-param>
	<filter>
		<filter-name>springSecurityFilterChain</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy
		</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>springSecurityFilterChain</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```
spring-security.xml
``` xml
<beans:beans xmlns="http://www.springframework.org/schema/security"
    xmlns:beans="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/security
	http://www.springframework.org/schema/security/spring-security.xsd">
	
	<http auto-config="true" use-expressions="true">
	    <intercept-url pattern="/login" access="permitAll" />
	    <intercept-url pattern="/**" access="isAuthenticated()" />
	    <form-login login-page="/login" authentication-failure-url="/login?error"/>
	    <logout logout-url="/logout" />
	    <csrf />
	</http>
	
	<!-- TODO: tentar retirar essa configuração que já está presente no spring-context.xml -->
	<beans:bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource">
        <beans:property name="driverClassName" value="org.postgresql.Driver"/>
        <beans:property name="url" value="jdbc:postgresql://localhost:5432/postgres"/>
        <beans:property name="username" value="postgres"/>
        <beans:property name="password" value="postgres"/>
    </beans:bean>
	
	<authentication-manager>
	    <authentication-provider>
	        <password-encoder hash="md5" />
	        <jdbc-user-service data-source-ref="dataSource" />
	    </authentication-provider>
	</authentication-manager>
	
</beans:beans>
```
Criação da base de dados:
``` sql
CREATE TABLE users
(
  username character varying(50) NOT NULL,
  "password" character varying(50) NOT NULL,
  enabled boolean NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (username)
);
 
CREATE TABLE authorities
(
  username character varying(50) NOT NULL,
  authority character varying(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username)
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 
CREATE UNIQUE INDEX ix_auth_username
  ON authorities
  USING btree
  (username, authority);
```
Criando usuário 'admin' com permissão 'ROLE_ADMIN':
``` sql
insert into users values ('admin', MD5('admin'), true);
insert into authorities values ('admin', 'ROLE_ADMIN');
```
login.jsp
``` jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form action="login" method="post">
	<c:if test="${param.error != null}">
		<p>
			Invalid username and password.
		</p>
	</c:if>
	<c:if test="${param.logout != null}">
		<p>
			You have been logged out.
		</p>
	</c:if>
	<p>
		<label for="username">Username</label>
		<input type="text" id="username" name="username"/>
	</p>
	<p>
		<label for="password">Password</label>
		<input type="password" id="password" name="password"/>
	</p>
	<input type="hidden"
		name="${_csrf.parameterName}"
		value="${_csrf.token}"/>
	<button type="submit" class="btn">Log in</button>
</form>
```
LoginController.java
``` java
package br.com.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Realiza o login no sistema.
 */
@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}
```
