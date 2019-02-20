<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p><spring:message code="brotherhood.coach.list" /></p>

<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:choose>
	
	<jstl:when test="${!hasArea}">
		<b>	<spring:message code="coach.selectArea"/>	</b>
	</jstl:when>
	
	<jstl:otherwise>
	
	<display:table
	pagesize="5" name="allCoachs" id="row"
	requestURI="${requestURI}" >
	
	<display:column property="title" titleKey="coach.title" />
	
	<display:column property="description" titleKey="coach.description" />
		
	<display:column titleKey="coach.pictures">
        <jstl:set var="picturesSize" value="${row.pictures.size()}" />
        <spring:url var="picturesURL" value="/coach/brotherhood/picture/list.do?coachId={coachId}">
              <spring:param name="coachId" value="${row.id}"/>
        </spring:url>
        <a href="${picturesURL}">
              <spring:message var ="viewPictures1" code="coach.viewPictures" />
              <jstl:out value="${viewPictures1}(${picturesSize})" />    
        </a>
    </display:column>
  	
  	<display:column>
			<a href="coach/brotherhood/edit.do?coachId=${row.id}">
				<spring:message code="coach.edit" />
			</a>
	</display:column>
	
												
</display:table>

	</jstl:otherwise>

 </jstl:choose>
<br />
	
	<jstl:if test="${hasArea}">
		<a href="coach/brotherhood/create.do"><spring:message code="coach.create" /></a>
	</jstl:if>
	
</security:authorize>