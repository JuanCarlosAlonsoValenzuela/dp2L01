<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<p>
	<spring:message code="member.enrolments" />
</p>

<security:authorize access="hasRole('MEMBER')">

	<display:table pagesize="5" name="enrolments" id="row"
		class="displaytag" requestURI="enrolment/member/list.do">

		<display:column property="statusEnrolment" titleKey="enrolment.status" />

		<display:column property="position" titleKey="enrolment.position" />

		<display:column property="creationMoment" titleKey="enrolment.moment"
			sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />

		<display:column property="dropOutDate"
			titleKey="enrolment.dropOutDate" sortable="true"
			format="{0,date,dd/MM/yyyy HH:mm}" />

		<display:column>
			<spring:url var="dropOutUrl"
				value="/enrolment/member/dropout.do?enrolmentId={enrolmentId}">
				<spring:param name="enrolmentId" value="${row.id}" />
			</spring:url>
			<jstl:if test="${row.statusEnrolment.toString()=='ACCEPTED'}">
				<form:form action="${dropOutUrl}">
					<acme:submit code="enrolment.dropOut" name="save" />
				</form:form>
			</jstl:if>
		</display:column>

	</display:table>

</security:authorize>