<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><spring:message code="procession.create" /></p>


<form:form action="procession/brotherhood/create.do" modelAttribute="formObjectProcessionFloat" >

<fieldset>
  <legend> <spring:message code="procession.data" /> </legend>

	<acme:textbox code="procession.title" path="titleProcession"/>	
	<br />
	
	<acme:textbox code="procession.description" path="descriptionProcession"/>	
	<br />
		
	<acme:datebox code="procession.moment" path="moment"/>	
	<br />
	
	<acme:boolean code="procession.isDraftMode" trueCode="procession.true" falseCode="procession.false" path="isDraftMode"/>	
	<br />
	
	<acme:input code="procession.rowNumber" path="rowNumber"/>	
	<br />
	
	<acme:input code="procession.columnNumber" path="columnNumber"/>	
	<br />
	
</fieldset>

<fieldset>
  <legend> <spring:message code="float.data" /> </legend>
  
	<acme:textbox code="float.title" path="title"/>	
	<br />
	
	<acme:textbox code="float.description" path="description"/>	
	<br />
</fieldset>
	<br />
	
	<acme:submit code="float.createButton" name="save" />
	<br />
	
</form:form> 


<acme:cancel url="/procession/brotherhood/list.do" code="float.cancel" /> 