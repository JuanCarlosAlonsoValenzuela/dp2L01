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

		<acme:textbox code="finder.keyWord" path="keyWord"/>
		
		<acme:textbox code="finder.area" path="area"/>
		
		<acme:datebox code="finder.minimumDate" path="minDate"/>
		
		<acme:datebox code="finder.maximumDate" path="maxDate"/>
		
		<acme:submit name="save" code="member.save"/>
	
	</form:form>

	<acme:cancel url="/finder/member/list.do" code="member.cancel"/>

</security:authorize>
