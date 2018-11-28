<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>通知-员工队列数不一致</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no" />
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <style type="text/css">
        .highlight{
            color: red;
            font-weight:bold;
        }
    </style>
</head>
<h4>环境：${env}</h4>
<HR>
<body class="table table-hover">
<table>
    <thead>
        <tr>
            <th scope="col">序号</th>
            <th scope="col">工号</th>
            <th scope="col">姓名</th>
            <th scope="col">任务节点</th>
            <th scope="col">当前正常队列数</th>
            <th scope="col">更正正常队列数</th>
            <th scope="col">当前优先队列数</th>
            <th scope="col">更正优先队列数</th>
            <th scope="col">当前挂起队列数</th>
            <th scope="col">更正挂起队列数</th>
        </tr>
    </thead>
    <tbody>
    <#list list! as item>
    <tr>
        <td scope="row">${item?counter}</td>
        <td>${item.staffCode}</td>
        <td>${item.name}</td>
        <td>${item.taskDef}</td>
        <td>${item.activiyNum}</td>
        <td <#if item.activiyFlag> class="highlight"</#if>>${item.activiyCorrNum}</td>
        <td>${item.priorityNum}</td>
        <td <#if item.priorityFlag> class="highlight"</#if>>${item.priorityCorrNum}</td>
        <td>${item.inactivyNum}</td>
        <td <#if item.inactiviyFlag> class="highlight"</#if>>${item.inactivyCorrNum}</td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>