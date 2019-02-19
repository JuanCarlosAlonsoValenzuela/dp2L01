<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<p><spring:message code="member.finder" /></p>

<security:authorize access="hasRole('MEMBER')">

	<form:form action="finder/member/clean.do">
	
		<acme:submit name="save" code="finder.cleanFilter"/>
			
	</form:form>
	
	<spring:url var="finderUrl" value="/finder/member/edit.do" />
	
	<a href="${finderUrl}">
	<button type="button" ><spring:message code="finder.edit" /></button>	
	</a>

	<display:table pagesize="5" name="processions" id="row" class="displaytag" 
					requestURI="/finder/member/list.do">
					
	<display:column titleKey="procession.request" >
	
	</display:column> 
		
	<display:column property="title" titleKey="procession.title" /> 
	
	<display:column property="description" titleKey="procession.description" /> 
	
	<display:column property="moment" titleKey="procession.moment" /> 
	
	<display:column titleKey="procession.float" > 
	
	
	<jstl:set var="floatsize" value="${row.coachs.size()}" />
			<spring:url var="floatsUrl" value="/float/member/list.do">
						<spring:param name="procession" value="${row.id}" />
			</spring:url>
			
			<a href="${floatsUrl}">
							<spring:message var="seeFloats" code="procession.seeFloats"/> 	
							<jstl:out value="${seeFloats}(${floatsize})" />
						</a>
	</display:column>
	</display:table>

</security:authorize>
