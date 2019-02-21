<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p>
	<spring:message code="brotherhood.pending.enrolments" />
</p>

<security:authorize access="hasRole('BROTHERHOOD')">

	<display:table pagesize="5" name="enrolments" id="row"
		class="displaytag" requestURI="enrolment/brotherhood/list.do">

		<display:column property="member.name" titleKey="member.name" />

		<display:column property="member.middleName"
			titleKey="member.middleName" />

		<display:column property="member.surname" titleKey="member.surname" />

		<display:column property="creationMoment" titleKey="enrolment.moment"
			sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />

	</display:table>

</security:authorize>