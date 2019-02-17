<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<p><spring:message code="member.finder" /></p>

<security:authorize access="hasRole('MEMBER')">

	<form:form action="finder/member/edit.do" modelAttribute="finder">
	<!-- Hidden Attributes -->
		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="lastEdit"/>
		
		<acme:textbox code="finder.keyWord" path="keyWord"/>
		
		<acme:textbox code="finder.area" path="area"/>
		
		<acme:datebox code="finder.minimumDate" path="minDate"/>
		
		<acme:datebox code="finder.maximumDate" path="maxDate"/>
	
	</form:form>

</security:authorize>