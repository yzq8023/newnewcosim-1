<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>委托加工订单管理</title>
<%@include file="/commons/include/get.jsp" %>

<script type="text/javascript">
	$(function() {
		$("a.apply").click(function() {
			$.ajax({
				type : "POST",
				url : 'apply.ht?id='+$('#userVacateId').val(),
				success : function(res) {
					var result = eval('('+res+')');
					$.ligerMessageBox.success('提示信息',result.message);
					window.location.href = "list.ht";
				},
				error : function(res) {
					$.ligerMessageBox.error('提示信息',result.message);
				}
			});
		});
	});
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">委托加工订单管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link add" href="dir_manufEnquiryorder.ht">添加</a></div>
					<div class="l-bar-separator"></div>
					<!-- 
					<div class="group"><a class="link update" id="btnUpd" action="edit.ht">修改</a></div>
					<div class="l-bar-separator"></div>
					<div class="group"><a class="link del"  action="del.ht">删除</a></div>
					 -->
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.ht">
					<div class="row">
						<table>
						<tr>
							<td>单证号:</td>
							<td><input type="text" name="Q_code_SL"  class="inputText" /></td>
							<td>制单人:</td>
							<td><input type="text" name="Q_operatorName_SL"  class="inputText" /></td>
							<td>制单日期:</td>
							<td colspan=3><input  name="Q_beginoperateDate_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endoperateDate_DG" class="inputText date" /></td>
						</tr>
					</table>
						<!-- <span class="label">单证号:</span><input type="text" name="Q_code_SL"  class="inputText" />
						<span class="label">制单人ID:</span><input type="text" name="Q_operatorId_SL"  class="inputText" />
						<span class="label">制单人姓名:</span><input type="text" name="Q_operatorName_SL"  class="inputText" />
						<span class="label">制单日期 从:</span> <input  name="Q_beginoperateDate_DL"  class="inputText date" />
						<span class="label">至: </span><input  name="Q_endoperateDate_DG" class="inputText date" />
						<span class="label">受托加工企业ID:</span><input type="text" name="Q_selectedenterpId_SL"  class="inputText" />
						<span class="label">受托加工企业名:</span><input type="text" name="Q_selectedenterpName_SL"  class="inputText" />
						<span class="label">受托加工联系人ID:</span><input type="text" name="Q_selectedenterpUserid_SL"  class="inputText" />
						<span class="label">受托加工联系人姓名:</span><input type="text" name="Q_selectedenterpUsername_SL"  class="inputText" />
						<span class="label">运费承担方:</span><input type="text" name="Q_freightBearer_SL"  class="inputText" />
						<span class="label">运输方式:</span><input type="text" name="Q_transportWay_SL"  class="inputText" />
						<span class="label">币种:</span><input type="text" name="Q_currencyType_SL"  class="inputText" />
						<span class="label">总金额:</span><input type="text" name="Q_sumPrice_SL"  class="inputText" />
						<span class="label">预付款:</span><input type="text" name="Q_advancePay_SL"  class="inputText" />
						<span class="label">预付款说明:</span><input type="text" name="Q_advancepayNote_SL"  class="inputText" />
						<span class="label">是否带料:</span><input type="text" name="Q_isBringmaterial_SN"  class="inputText" />
						<span class="label">加工类型:</span><input type="text" name="Q_manufacturingType_SL"  class="inputText" />
						<span class="label">来源单据类型:</span><input type="text" name="Q_sourceformType_SL"  class="inputText" />
						<span class="label">来源单据ID:</span><input type="text" name="Q_sourceformId_SL"  class="inputText" />
						<span class="label">来源单据编码:</span><input type="text" name="Q_sourceformCode_SL"  class="inputText" />
						<span class="label">委托企业ID:</span><input type="text" name="Q_enterpriseId_SL"  class="inputText" />
						<span class="label">委托企业名称:</span><input type="text" name="Q_enterpriseName_SL"  class="inputText" />
						<span class="label">委托企业联系人ID:</span><input type="text" name="Q_enterpriseUserid_SL"  class="inputText" />
						<span class="label">委托企业联系人名:</span><input type="text" name="Q_enterpriseUsername_SL"  class="inputText" />
						<span class="label">是否汇报加工进度:</span><input type="text" name="Q_isReport_SN"  class="inputText" />
						<span class="label">收货仓库ID:</span><input type="text" name="Q_receivewarehouseId_SL"  class="inputText" />
						<span class="label">收货仓库名:</span><input type="text" name="Q_receivewarehouseName_SL"  class="inputText" />
						<span class="label">收货仓库地址:</span><input type="text" name="Q_receivewarehouseAddress_SL"  class="inputText" />
						<span class="label">收货联系人ID:</span><input type="text" name="Q_receivewarehouseUserid_SL"  class="inputText" />
						<span class="label">收货联系人名:</span><input type="text" name="Q_receivewarehouseUsername_SL"  class="inputText" />
						<span class="label">备注:</span><input type="text" name="Q_comments_SL"  class="inputText" /> -->
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="manufEnquiryorderList" id="manufEnquiryorderItem" requestURI="list.ht" sort="external" cellpadding="1" cellspacing="1" export="true"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${manufEnquiryorderItem.id}">
				</display:column>
				<display:column property="code" title="单证号" sortable="true" sortName="CODE"></display:column>
<%-- 				<display:column property="operatorId" title="制单人ID" sortable="true" sortName="OPERATOR_ID"></display:column>
 --%>				<display:column property="operatorName" title="制单人" sortable="true" sortName="OPERATOR_NAME" style="width:50px"></display:column>
				<display:column  title="制单日期" sortable="true" sortName="OPERATE_DATE" style="width:70px">
					<fmt:formatDate value="${manufEnquiryorderItem.operateDate}" pattern="yyyy-MM-dd"/>
				</display:column>
<%-- 				<display:column property="selectedenterpId" title="受托加工企业ID" sortable="true" sortName="selectedenterp_id"></display:column>
 --%>				<display:column property="selectedenterpName" title="企业名" sortable="true" sortName="selectedenterp_name" style="width:30px"></display:column>
<%-- 				<display:column property="selectedenterpUserid" title="受托加工联系人ID" sortable="true" sortName="selectedenterp_userid"></display:column>
 --%>				<display:column property="selectedenterpUsername" title="联系人姓名" sortable="true" sortName="selectedenterp_username"></display:column>
			<%-- 	<display:column property="freightBearer" title="运费承担方" sortable="true" sortName="FREIGHT_BEARER"></display:column>
				<display:column property="transportWay" title="运输方式" sortable="true" sortName="TRANSPORT_WAY"></display:column>
				<display:column property="currencyType" title="币种" sortable="true" sortName="CURRENCY_TYPE"></display:column>
				<display:column property="sumPrice" title="总金额" sortable="true" sortName="SUM_PRICE"></display:column>
 				<display:column property="advancePay" title="预付款" sortable="true" sortName="ADVANCE_PAY"></display:column>
				<display:column property="advancepayNote" title="预付款说明" sortable="true" sortName="ADVANCEPAY_NOTE" maxLength="80"></display:column>
 				<display:column property="isBringmaterial" title="是否带料" sortable="true" sortName="IS_BRINGMATERIAL"></display:column>
				<display:column property="manufacturingType" title="加工类型" sortable="true" sortName="MANUFACTURING_TYPE"></display:column>
				<display:column property="sourceformType" title="来源单据类型" sortable="true" sortName="SOURCEFORM_TYPE"></display:column>
				<display:column property="sourceformId" title="来源单据ID" sortable="true" sortName="SOURCEFORM_ID"></display:column>
 --%>				<display:column property="sourceformCode" title="来源单据编码" sortable="true" sortName="SOURCEFORM_CODE"></display:column>
<%--  				<display:column property="enterpriseId" title="委托企业ID" sortable="true" sortName="ENTERPRISE_ID"></display:column>
 --%>				<display:column property="enterpriseName" title="委托企业名称" sortable="true" sortName="ENTERPRISE_NAME"></display:column>
<%-- 				<display:column property="enterpriseUserid" title="委托企业联系人ID" sortable="true" sortName="ENTERPRISE_USERID"></display:column>
 --%>				<display:column property="enterpriseUsername" title="委托企业联系人名" sortable="true" sortName="ENTERPRISE_USERNAME"></display:column>
				<%-- <display:column property="isReport" title="是否汇报加工进度" sortable="true" sortName="IS_REPORT"></display:column>
 				<display:column property="receivewarehouseId" title="收货仓库ID" sortable="true" sortName="receivewarehouse_id"></display:column>
			<display:column property="receivewarehouseName" title="收货仓库名" sortable="true" sortName="receivewarehouse_name" maxLength="80"></display:column>
				<display:column property="receivewarehouseAddress" title="收货仓库地址" sortable="true" sortName="receivewarehouse_address" maxLength="80"></display:column>
 				<display:column property="receivewarehouseUserid" title="收货联系人ID" sortable="true" sortName="receivewarehouse_userid"></display:column>
 				<display:column property="receivewarehouseUsername" title="收货联系人名" sortable="true" sortName="receivewarehouse_username"></display:column>
				<display:column property="comments" title="备注" sortable="true" sortName="comments" maxLength="80"></display:column> --%>
				<display:column title="管理" media="html" style="white-space:nowrap;">
					<c:if test="${userVacateItem.runStatus==0}">
						<a href="del.ht?id=${manufEnquiryorderItem.id}" class="link del">删除</a>
						<a href="edit.ht?id=${manufEnquiryorderItem.id}" class="link edit">编辑</a>
						<a class="link apply" id="">申请</a>
						<input type="hidden" id="userVacateId" value="${manufEnquiryorderItem.id}">
					</c:if>
					<c:if test="${manufEnquiryorderItem.sourceformType == '无' }">
						<a href="${ctx}/platform/bpm/processRun/userImage.ht?action=process&action=process&runId=${manufEnquiryorderItem.runId}" class="link detail">流程示意图</a>					
					</c:if>
					<a href="get.ht?id=${manufEnquiryorderItem.id}&runid=${manufEnquiryorderItem.runid}&status=${manufEnquiryorderItem.runStatus}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<hotent:paging tableId="manufEnquiryorderItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


