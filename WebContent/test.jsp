<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<sql:query var="rs" dataSource="jdbc/TransactionsDB">
	select * from Batch
</sql:query>

<html>
	<head>
		<title>DB Test</title>	
	</head>
	<body>
	
		<h2>Results</h2>
		<c:forEach var="row" items="${rs.rows}">
		    BatchPK ${row.BatchPK}<br />
		    TotalNumberOfRows ${row.TotalNumberOfRows}<br />
		    NumberOfNewTrans ${row.NumberOfNewTrans}<br/>
		    CreationDate ${row.CreationDate}<br/>
		    Filename ${row.Filename}<br/>
		</c:forEach>
	
	</body>
</html>