<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
	<body>
		<h2>Spring MVC!</h2>
		<ul>
			<li><a href="http://localhost:8080/springmvc/repository">GO</a> - Exemplo de acesso ao banco de dados utilizando injeção de dependência.</li>
		</ul>
		<form action="logout"
	        method="post">
	      <input type="submit" value="Log out" />
	      <input type="hidden"
	        name="${_csrf.parameterName}"
	        value="${_csrf.token}"/>
	    </form>
	</body>
</html>