<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${people!=null && people.id!=0?'编辑':'添加'}人事信息</title>
<link href="css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script language="javascript" type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript">
$(document).ready(function(){
	 var sex = "${people.sex}";
	 if(sex!=''){
		 $("#sex"+sex).attr('checked','checked');
	 }else{
		 $("#sex1").attr('checked','checked');
	 }
	 
	 var num = /^\d+$/;
	 $("#addBtn").bind('click',function(){
		if($("#name").val()==''){
			alert('用户名不能为空');
			return;
		}
		if($("#password").val()==''){
			alert('密码不能为空');
			return;
		}
		if($("#realName").val()==''){
			alert('姓名不能为空');
			return;
		}
		if($("#status").val()==''){
			alert('员工状态不能为空');
			return;
		}
		$("#id").val(0);
		$("#info").attr('action','Admin_addPeople.action').submit();
		 
	 });
	 
	 $("#editBtn").bind('click',function(){
		if($("#realName").val()==''){
			alert('姓名不能为空');
			return;
		}
		if($("#status").val()==''){
			alert('员工状态不能为空');
			return;
		}
		$("#info").attr('action','Admin_savePeople.action').submit();
			 
	});
	
});
</script>
<style type="text/css">
</style>
</head>
<body>
<div class="pageTitle">
	&nbsp;&nbsp;<img src="images/right1.gif" align="middle" /> &nbsp;<span id="MainTitle" style="color:white">人事管理&gt;&gt;${people!=null && people.id!=0?'编辑':'添加'}人事</span>
</div>
<form id="info" name="info" action="Admin_addPerson.action" method="post">   
<input type="hidden"  id="id" name="id" value="${people.id}" /> 
<table width="800" align="center" cellpadding="0" cellspacing="0" style="margin-top:10px;margin-bottom:10px;">
  <tr> 
     <td height="24">
       <Table border="0" cellspacing="0" cellpadding="0" align="center" width="100%"> 
            <tr>
              <td height="24" class="edittitleleft">&nbsp;</td>
              <td class="edittitle">${people!=null && people.id!=0?'编辑':'添加'}人事</td>
              <td class="edittitleright">&nbsp;</td>
            </tr>
        </TABLE>
     </td>
   </tr>
   <tr>
     <td height="1" bgcolor="#8f8f8f"></td>
   </tr>
   <tr>
     <td >
     <table width="100%" align="center" cellpadding="1" cellspacing="1" class="editbody">
       <tr>
          <td width="12%" align="right" style="padding-right:5px"><font color="red">*</font> 用户名：</td>
          <td width="38%" >
          	<c:if test="${people!=null && people.id!=0}">${people.name}</c:if>
          	<c:if test="${people==null || people.id==0}"><input type="text" name="name" id="name" value="${people.name }"/> </c:if>
          </td>
          <td width="12%" align="right" style="padding-right:5px"><font color="red">*</font> 密码：</td>
          <td width="38%">
          	<c:if test="${people!=null && people.id!=0}">
          	<input type="password" name="password" id="password" value="" />
          	</c:if>
          	<c:if test="${people==null || people.id==0}">
          	<input type="password" name="password" id="password" value="111111" />
          	<span id="passTip" style="color:red;">初始密码：111111</span>
          	</c:if>
          </td>
        </tr> 
        <tr>
          <td align="right" style="padding-right:5px"><font color="red">*</font> 姓名：</td>
          <td>
            <input type="text"  name="realName" id="realName" value="${people.realName}"/>
          </td>
          <td align="right" style="padding-right:5px"><font color="red">*</font> 性别：</td>
          <td>
            <input type="radio" id="sex1" name="sex" value="1"/>男&nbsp;&nbsp;
            <input type="radio" id="sex2" name="sex" value="2"/>女
          </td>
        </tr>  
        <tr>
          <td align="right" style="padding-right:5px">联系方式：</td>
          <td>
            <input type="text"  name="phone" id="phone" value="${people.phone}"/>
          </td>
         <td align="right" style="padding-right:5px">身份证号：</td>
          <td>
            <input type="text"  name="idcard" id="idcard" value="${people.idcard}"/>
          </td>
        </tr> 
        <tr>
          <td align="right" style="padding-right:5px"><font color="red">*</font> 员工状态：</td>
          <td>
             <select id="status" name="status" style="width:150px;">
                 <option value="">请选择</option>
                 <option value="0" ${people.status==0?'selected':''}>实习</option>
                 <option value="1" ${people.status==1?'selected':''}>正式员工</option>
                 <option value="2" ${people.status==2?'selected':''}>离职</option>
		      </select>
          </td>
            <td align="right" style="padding-right:5px">邮箱：</td>
            <td>
                <input type="text"  name="mail" id="mail" value="${people.mail}"/>
            </td>
        </tr>  
         <%--<tr>
          <td align="right" style="padding-right:5px">合同附件：</td>
          <td colspan="3">
             <span id="peopleFile">${people.info_file}</span>
		     <span id="op" style="display:none"><img src="images/loading004.gif"  height="50" /></span>
          </td>
        </tr>
        <tr>
		  <td align="right" style="padding-right:5px">上传附件：</td>
	      <td align="left" colspan="3">
	          <iframe name="uploadPage" src="uploadImg.jsp" width="100%" height="50" marginheight="0" marginwidth="0" scrolling="no" frameborder="0"></iframe>
	       </td>
	    </tr>--%>
     </table>
     </td>
   </tr>  
   <tr>
     <td>
       <table width="100%" align="center" cellpadding="0" cellspacing="0" class="editbody">
        <tr class="editbody">
          <td align="center" height="30">
          	<c:if test="${people!=null && people.id!=0}">
          	<input type="button" id="editBtn" Class="btnstyle" value="编 辑"/> 
          	</c:if>
          	<c:if test="${people==null || people.id==0}">
          	<input type="button" id="addBtn" Class="btnstyle" value="添 加" />
          	</c:if>
            &nbsp;<label style="color:red">${tip}</label>
          </td>
        </tr>
      </table>
     </td>
   </tr>
</table>
</form>
</body>
</html>