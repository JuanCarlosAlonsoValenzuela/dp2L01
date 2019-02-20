<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="procession.create" /></p>

<security:authorize access="hasRole('BROTHERHOOD')"> 

	<form:form modelAttribute="procession" action="procession/brotherhood/create.do">
    <!--Hidden Attributes -->
	<form:hidden path ="id"/>
	<form:hidden path ="version"/>

	<form:hidden path ="ticker"/>
	<form:hidden path ="floats"/>
	<form:hidden path ="requests"/>
	
	<acme:textbox code="procession.title" path="title" />
	
	<acme:textarea code="procession.description" path="description" />
	
	<acme:date code="procession.moment" path="moment" />
	
 	<acme:input code="procession.rowNumber" path="rowNumber" />
	
	<acme:input code="procession.columnNumber" path="columnNumber" />
	
	<acme:boolean code="procession.isDraftMode" path="isDraftMode" trueCode="procession.draftMode" falseCode="procession.finalMode" /> 
	
 	<acme:submit name="save" code="procession.save" />  
	
	</form:form>
	
	<acme:cancel url="/procession/brotherhood/list.do" code="procession.cancel" />  
	
</security:authorize>