<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:choose>
	<jstl:when test="${hasArea}">
		<display:table pagesize="5" name="area" id="row">
		
			<display:column property="name" titleKey="area.name">
			</display:column>
			
			<display:column titleKey="area.pictures">
				<jstl:set var="picturesSize" value="${row.pictures.size()}" />
				<spring:url var="picturesUrl" value="area/brotherhood/showPictures.do">
					<spring:param name="areaId" value="${row.id}"/>
				</spring:url>
				<a href="${picturesUrl}">
					<spring:message var ="viewPic" code="area.pictures" />
					<jstl:out value="${viewPic}(${picturesSize})" />		
				</a>
			</display:column>

		</display:table>
	
	</jstl:when><jstl:otherwise>
	
		<spring:url var="selectArea" value="area/brotherhood/selectArea.do"/>
		<a href="${selectArea}">
			<spring:message var ="select" code="area.select" />
			<jstl:out value="${select}" />		
		</a>


	</jstl:otherwise>
</jstl:choose>
