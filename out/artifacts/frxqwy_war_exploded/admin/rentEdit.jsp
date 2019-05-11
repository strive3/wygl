<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" >
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${rent!=null && rent.rentId!=0?'编辑':'添加'}房屋缴费信息</title>
<link href="css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script language="javascript" type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
<script charset="utf-8" src="editor/kindeditor.js"></script>
<script language="javascript" type="text/javascript">
$(document).ready(function(){
	
	 var num = /^\d+(\.\d+)?$/;
	 var num2 = /^\d+$/;
	 $("#addBtn").bind('click',function(){
		if($("#room_id").val()=='0'){
			alert('房屋号编号不能为空');
			return;
		}
		if(!num.exec($("#rentPrice").val())){
			alert('房屋费标准必须为数字');
			return;
		}
		if($("#rentStart").val()==''){
			alert('缴费开始月份不能为空');
			return;
		}
		if($("#rentEnd").val()==''){
			alert('缴费结束月份不能为空');
			return;
		}
		if(!num.exec($("#rentMoney").val())){
			alert('总金额必须为数字');
			return;
		}
		$("#rentId").val(0);
		$("#info").attr('action','Admin_addRent.action').submit();
		 
	 });
	 
	 $("#editBtn").bind('click',function(){
		if($("#room_id").val()=='0'){
			alert('房屋号编号不能为空');
			return;
		}
		if(!num.exec($("#rentPrice").val())){
			alert('房屋费标准必须为数字');
			return;
		}
		if($("#rentStart").val()==''){
			alert('缴费开始月份不能为空');
			return;
		}
		if($("#rentEnd").val()==''){
			alert('缴费结束月份不能为空');
			return;
		}
		if(!num.exec($("#rentMoney").val())){
			alert('总金额必须为数字');
			return;
		}
		$("#info").attr('action','Admin_saveRent.action').submit();
			 
	});
	
});
</script>
<style type="text/css">
</style>
</head>
<body>
<div class="pageTitle">
	&nbsp;&nbsp;<img src="images/right1.gif" align="middle" /> &nbsp;<span id="MainTitle" style="color:white">租赁房屋管理&gt;&gt;${rent!=null && rent.rentId!=0?'编辑':'添加'}房屋缴费</span>
</div>
<form id="info" name="info" action="Admin_addRent.action" method="post">   
<input type="hidden"  id="rentId" name="rentId" value="${rent.rentId}" /> 
<table width="800" align="center" cellpadding="0" cellspacing="0" style="margin-top:10px;margin-bottom:10px;">
  <tr> 
     <td height="24">
       <Table border="0" cellspacing="0" cellpadding="0" align="center" width="100%"> 
            <TR>
              <TD height="24" class="edittitleleft">&nbsp;</TD>
              <TD class="edittitle">${rent!=null && rent.rentId!=0?'编辑':'添加'}房屋缴费</TD>
              <TD class="edittitleright">&nbsp;</TD>
            </TR>
        </TABLE>
     </td>
   </tr>
   <tr>
     <td height="1" bgcolor="#8f8f8f"></td>
   </tr>
   <tr>
     <td >
     <table class="table table-bordered" width="100%" align="center" cellpadding="1" cellspacing="1" class="editbody">
        <tr>
          <td width="35%" align="right" style="padding-right:5px"><font color="red">*</font> 房屋号编号：</td>
          <td width="65%">
          	<select class="form-control" id="room_id" name="roomId" Style="width:200px;">
          		<option value="0">请选择</option>
          		<c:forEach items="${rooms}" var="room">
          			<option ${rent.roomId==room.room_id?'selected':''} value="${room.room_id}">${room.room_no}（业主：${room.real_name}）</option>
          		</c:forEach>
	        </select>
          </td>
        </tr> 
        <tr>
          <td width="35%" align="right" style="padding-right:5px"><font color="red">*</font> 房屋费标准：</td>
          <td width="65%">
          	<input class="form-control" type="text" name="rentPrice" id="rentPrice" value="${rent.rentPrice}"/> 元/㎡
          </td>
        </tr> 
        <tr>
          <td width="35%" align="right" style="padding-right:5px"><font color="red">*</font> 缴费开始月份：</td>
          <td width="65%">
          	<input class="form-control" type="text" name="rentStart" id="rentStart" value="${rent.rentStart}"
          			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM'})"/>
          </td>
        </tr> 
        <tr>
          <td width="35%" align="right" style="padding-right:5px"><font color="red">*</font> 缴费结束月份：</td>
          <td width="65%">
          	<input class="form-control" type="text" name="rentEnd" id="rentEnd" value="${rent.rentEnd}"
          			 onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM'})"/>
          </td>
        </tr> 
        <tr>
          <td width="35%" align="right" style="padding-right:5px"><font color="red">*</font> 总金额：</td>
          <td width="65%">
          	<input class="form-control" type="text" name="rentMoney" id="rentMoney" value="${rent.rentMoney}"/>
          </td>
        </tr> 
     </table>
     </td>
   </tr>  
   <tr>
     <td>
       <table width="100%" align="center" cellpadding="0" cellspacing="0" class="editbody">
        <tr class="editbody">
          <td align="center" height="30">
          	<c:if test="${rent!=null && rent.rentId!=0}">
          	<input class="btn btn-default" type="button" id="editBtn" Class="btnstyle" value="编 辑"/> 
          	</c:if>
          	<c:if test="${rent==null || rent.rentId==0}">
          	<input class="btn btn-default" type="button" id="addBtn" Class="btnstyle" value="添 加" />
          	</c:if>
            &nbsp;<label style="color:red">${tip}</label>
          </td>
        </tr>
      </table>
     </td>
   </tr>
</table>
</form>
<script>        
</script>
</body>
</html>