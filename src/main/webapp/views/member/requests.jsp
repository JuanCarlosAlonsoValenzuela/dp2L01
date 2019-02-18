<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('MEMBER')">		 	

	<display:table pagesize="5" name="requests" id="row" class="displaytag" 
					requestURI="request/member/list.do">
					
		<display:column property="status" titleKey="request.status" />
		
		<display:column property="rowNumber" titleKey="request.rowNumber" />
		
		<display:column property="columnNumber" titleKey="request.columnNumber"/>
			
		<display:column property="reasonDescription" titleKey="request.reasonDescription"/>
				
	</display:table>
	
</security:authorize>