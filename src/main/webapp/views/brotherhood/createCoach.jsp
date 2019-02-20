<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="coach.create" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

	<form:form modelAttribute="coach" action="coach/brotherhood/edit.do">
    <!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>


	<acme:textbox code="coach.title" path="title" />
	
	<acme:textarea code="coach.description" path="description" /> 
	
 	<acme:submit name="save" code="coach.save" />  
 	
 	<jstl:if test="${coach.id != 0 }">
 		<acme:submit name="delete" code="coach.delete" />
 	</jstl:if> 
 	
 	
 	
	</form:form>
	
	<acme:cancel url="/coach/brotherhood/list.do" code="coach.cancel" />  
	
</security:authorize>