<%--
  Created by IntelliJ IDEA.
  User: dodo
  Date: 2016/12/25
  Time: 下午9:57
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" uri="http://www.jee-soft.cn/functions" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="hotent" uri="http://www.jee-soft.cn/paging" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html lang="zh-CN">
<head>
    <title>办理任务</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/styles/slide/css/default.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/styles/slide/css/component.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/newtable/bootstrap.css"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/styles/wizard/bootstro.min.css"/>
    <link rel="stylesheet" href="${ctx}/jqwidgets/styles/jqx.base.css" type="text/css"/>
    <link rel="stylesheet" href="${ctx}/jqwidgets/styles/jqx.bootstrap.css" type="text/css"/>
    <!-- include the style of alertify-->
    <link rel="stylesheet" href="${ctx}/js/alertifyjs/css/alertify.min.css"/>
    <link rel="stylesheet" href="${ctx}/js/alertifyjs/css/themes/default.min.css"/>
    <script src="${ctx}/newtable/jquery.js"></script>
    <script src="${ctx}/newtable/bootstrap.js"></script>

    <script type="text/javascript" src="${ctx}/jqwidgets/jqx-all.js"></script>

    <%--<script type="text/javascript" src="${ctx}/timeselect/bootstrap-datetimepicker.min.js"></script>--%>

    <script type="text/javascript" src="${ctx}/js/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery/additional-methods.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery/jquery.validate.ext.js"></script>
    <script type="text/javascript" src="${ctx}/js/util/util.js"></script>
    <script type="text/javascript" src="${ctx}/js/util/form.js"></script>
    <script type="text/javascript" src="${ctx}/js/hotent/CustomValid.js"></script>
    <script type="text/javascript" src="${ctx}/js/hotent/formdata.js"></script>
    <script type="text/javascript" src="${ctx}/js/hotent/subform.js"></script>

    <script type="text/javascript" src="${ctx}/styles/wizard/bootstro.min.js"></script>
    <script type="text/javascript" src="${ctx}/cookie/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctx}/styles/wizard/bootwizard.js"></script>
    <!-- include the script alertify-->
    <script src="${ctx}/js/alertifyjs/alertify.min.js"></script>
    <%--<script type="text/javascript" src="${ctx}/jqwidgets/jqxmenu.js"></script>--%>
    <style>
        html, body {
            height: 100%;
        }
    </style>
    <script>
        $(document).ready(function () {
            var isClient;
            try {
                isClient = JSInteraction.isme();
                $("#dataList").css({"width": "100%", "height": "100%", "margin-top": "12px"});
            } catch (e) {
                console.log(e)
            }
        });
    </script>
    <style>
        #loading{
            position: absolute;
            top:45%;
            left:55%;
            margin: -25px -150px;
            background-color: rgba(0,0,0,0);
            text-align: center;
            padding:20px;
            z-index: 9999                                                                                                   ;
        }

    </style>
</head>
<body id="jqxWidget">
<div id="loading" style="display: none">
    <img src="${ctx}/images/onLoad.gif"/> 仿真中...
</div>
<div id="knowledge_frame">

        <div id="windowHeader">
                    <span>
                        <img src="" alt="" style="margin-right: 15px"/>仿真结果
                    </span>
        </div>
        <div style="overflow: hidden;" id="windowContent">
            <div id="tab">
                <ul style="margin-left: 30px;">
                    <li>图1</li>
                    <li>图2</li>
                    <li>图3</li>
                    <li>图4</li>
                    <li>图5</li>
                    <li>图6</li>
                    <li>图7</li>
                    <li>图8</li>
                    <li>图9</li>
                    <li>图10</li>
                    <li>图11</li>
                    <li>图12</li>
                </ul>
                <%--<div>--%>
                    <%--<div>--%>
                        <%--<strong  style="display: inline">末制导精度</strong><div style="display: inline">123123123</div>|<strong style="display: inline">飞行时间</strong><div style="display: inline">123123123</div>|--%>
                        <%--<strong style="display: inline">末制导精度</strong><div style="display: inline">123123123</div>|<strong style="display: inline">飞行时间</strong><div style="display: inline">123123123</div>--%>
                    <%--</div>--%>

                    <%--<img src="${ctx}/images/fangzhen/m2.jpg" style="margin: 2px;" alt=""/>--%>

                <%--</div>--%>
                <%--<div>--%>
                    <%--<div>--%>
                        <%--<strong  style="display: inline">末制导精度</strong><div style="display: inline">123123123</div>|<strong style="display: inline">飞行时间</strong><div style="display: inline">123123123</div>|--%>
                        <%--<strong style="display: inline">末制导精度</strong><div style="display: inline">123123123</div>|<strong style="display: inline">飞行时间</strong><div style="display: inline">123123123</div>--%>
                    <%--</div>--%>

                    <%--<img src="${ctx}/images/fangzhen/m2.jpg" style="margin: 2px;" alt=""/>--%>

                <%--</div>--%>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国 &nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度：1223   m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间：123213  s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1234  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123321 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国 &nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度：1223   m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间：123213  s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1234  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123321 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1233  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123321 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1231  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1321  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 132123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1233  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1231  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1231  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1231  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
                <div>
                    <p style="float: left">1.武器名称：歼——05&nbsp&nbsp&nbsp</p>
                    <p style="float: left">2.生产国家：中国&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.末制导精度： 1231  m&nbsp&nbsp&nbsp</p>
                    <p style="float: left">1.飞行时间： 123123 s</p>
                    <img src="" style="float: left; margin: 10px;" alt="歼——05"/>

                </div>
            </div>
        </div>

</div>


<div class="container" id="dataList">
    <div class="row">
        <div class="col-xs-9">
            <ol class="breadcrumb">
                <li><a href="list.ht"><span class="glyphicon glyphicon-book"></span> ${TaskInfo.ddTaskProjectName}</a>
                </li>
                <li class="active">${TaskInfo.ddTaskName}</li>
            </ol>
        </div>
        <div class="col-xs-3">
            <div class="pull-right">
                <a class="btn btn-success" href="javascript:void(0)" id="submit_btn"><span
                        class="glyphicon glyphicon-ok"></span> 提交审核</a>
                <a class="btn btn-default" href="javascript:void(0)" id="knowledge_btn"><span
                        class="glyphicon glyphicon-book"></span> 开始仿真</a>
            </div>
        </div>
    </div>
    <div class="row">
        <ul class="nav nav-tabs" role="tablist" id="myTab">
            <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">
                    ${TaskInfo.ddTaskName} <span class="caret"></span>
                </a>
                <ul class="dropdown-menu" style="overflow: auto">
                    <c:forEach var="taskInfoListItem" items="${taskInfoList}">
                        <li>
                            <a href="todotask.ht?id=${taskInfoListItem.ddTaskId}&projectId=${taskInfoListItem.ddTaskProjectId}">${taskInfoListItem.ddTaskName}
                                <small> (${taskInfoListItem.ddTaskProjectName})</small>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </li>
            <li role="presentation" class="active reponav-item" id="switch_attr_publish"><a href="#data"
                                                                                            data-toggle="tab"
                                                                                            role="tab"><span
                    class="glyphicon glyphicon-log-out"></span> 输出数据</a>
            </li>
            <li role="presentation" class="reponav-item" id="switch_attr_order"><a href="#publish" data-toggle="tab"
                                                                                   role="tab"><span
                    class="glyphicon glyphicon-log-in"></span> 输入数据</a>
            </li>

            <li role="presentation" class="reponav-item" id="switch_attr_index"><a href="#index" data-toggle="tab"
                                                                                   role="tab"><span
                    class="glyphicon glyphicon-compressed"></span> 任务约束</a>
            </li>
        </ul>
    </div>


    <div class="tab-content board-view">
        <div role="tabpanel" class="tab-pane active board-scrum-view" id="data" style="height: 100%">
        </div>
        <div role="tabpanel" class="tab-pane board-scrum-view" id="index" style="height: 100%">
        </div>
        <div role="tabpanel" class="tab-pane board-scrum-view" id="publish" style="height: 100%">
        </div>
    </div>

    <%--统计--%>
    <div class="modal fade" id="statis" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">

            </div>
        </div>
    </div>

    <%--文件上传--%>
    <div class="modal fade" id="fileupload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content" style="overflow: auto" id="fileuploadcontent" style="overflow: auto">

            </div>
        </div>
    </div>
    <%--全部删除--%>
    <div class="modal fade" id="delAllData" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content" style="overflow: auto" id="delAllContent" style="overflow: auto">

            </div>
        </div>
    </div>

</div>

</body>
<script src="${ctx}/styles/slide/js/classie.js"></script>
<script type="text/javascript">
    //@ sourceURL=todotask.ht
    var knowledgeWindow = (function () {
                var currentWidth = $(window).width();

                //Adding event listeners
                function _addEventListeners() {
                    $('#knowledge_frame').jqxWindow('resizable', true);
                    $('#knowledge_frame').jqxWindow('draggable', true);

                    $('#knowledge_btn').click(function () {
                        $('#loading').show();
                        setTimeout(function(){
                            $('#loading').hide();
                            $('#knowledge_frame').jqxWindow('open');

                        }, 3000);
                    });

                }
                ;

                //Creating the window
                function _createWindow() {
                    var jqxWidget = $('#jqxWidget');
                    var offset = jqxWidget.offset();
                    $('#knowledge_frame').jqxWindow({
                        // position: {x: offset.left + (currentWidth - 360), y: offset.top},
                        showCollapseButton: true,
                        height: 800,
                        width: 600,
                        autoOpen: false,
                        initContent: function () {
                            $('#tab').jqxTabs({ height: '100%', width:  '100%' });
                            $('#knowledge_frame').jqxWindow('focus');
                        }
                    });
                };
                return {
                    config: {
                        dragArea: null
                    },
                    init: function () {
                        //Attaching event listeners
                        _addEventListeners();
                        //Adding jqxWindow
                        _createWindow();
                    }
                };
            }
            ()
        )
    ;

    $(document).ready(function () {
        $.get("showdata.ht?id=${TaskInfo.ddTaskId}&projectId=${TaskInfo.ddTaskProjectId}&type=${type}", function (data) {
            $('#data').html(data);
        });
        knowledgeWindow.init();
    });

    var switch_attr_index = document.getElementById('switch_attr_index'),
        switch_attr_publish = document.getElementById('switch_attr_publish'),
        switch_attr_order = document.getElementById('switch_attr_order'),
        submit_btn = document.getElementById('submit_btn');


    $("#statis").on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
    });

    submit_btn.onclick = function () {
        $.get("submittask.ht?id=${TaskInfo.ddTaskId}", function (data) {
            window.location.href = 'list.ht';
        });
    }
    //tab切换操作
    switch_attr_publish.onclick = function () {
        $.get("showdata.ht?id=${TaskInfo.ddTaskId}&projectId=${TaskInfo.ddTaskProjectId}&type=${type}", function (data) {
            $('#data').html(data);
        });
    }
    switch_attr_index.onclick = function () {
        $.get("${ctx}/datadriver/index/readonly.ht?id=${TaskInfo.ddTaskProjectId}", function (data) {
            $('#index').html(data);
        });
    }
    switch_attr_order.onclick = function () {
        $.get("showorder.ht?id=${TaskInfo.ddTaskId}&projectId=${TaskInfo.ddTaskProjectId}&type=${type}", function (data) {
            $('#publish').html(data);
        });
    }


</script>
</html>
