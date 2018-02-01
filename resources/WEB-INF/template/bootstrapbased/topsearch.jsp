<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="/mediadesk" prefix="mediadesk" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="imagesearchTop" class="<%= (request.getAttribute("isSearch")!=null) ? "imagesearch searchInSearch":"imagesearch searchNormal" %>">
    <div id="imagesearchCaTop">
        <div id="topSearchDiv"></div>
    <form id="formSearch" method="post" action="<c:url value="/${lng}/search"/>">
        <p>

            <input name="q" type="text" class="textField" id="sTextKeyword" placeholder="<spring:message code="imagesearch.imagesearch"/>" accesskey="s"/>
            <input name="SuchKeyword" type="submit" class="buttonTopSearchSubmit" id="submitKeywordButton" value="<spring:message code="imagesearch.searchbutton"/>"/>



        </p>

            <%
                if (request.getAttribute("isSearch")!=null) {
            %>
        <p>
            <input type="checkbox" id="sCbxRequery" name="requery" value="1"/>
            <label for="sCbxRequery" id="sLabRequery"><spring:message code="imagesearch.inresults"/></label>
        </p>
            <%
                }
            %>
    </form>
    </div>
</div>

<c:if test="${config.advancedSearchEnabled}">
<div id="searchLinks">
    <ul>
        <li><a href="<c:url value="/${lng}/advancedsearch"/>"><spring:message code="imagesearch.advanced"/></a></li>
    </ul>
</div>
</c:if>

<script type="text/javascript">

    function myTriggerFn(data, e, f) {
        alert('test'+data+" "+e);
    }

    function topSearch() {

        var trigger = new Ext.form.TriggerField({
            emptyText: '<spring:message code="imagesearch.imagesearch"/>',
            triggerClass: 'x-form-search-trigger',
            listeners: {
                specialkey: function(field, e) {
                    if(e.getKey() == e.ENTER)
                    {
                        field.onTriggerClick();
                    }
                }
            }
        });
        trigger.onTriggerClick = myTriggerFn;
        //trigger.render('topSearchDiv');

    }

</script>