<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--fullcalendar日历控件--%>
<script type="text/javascript" src="${ctx}/resources/js/fullcalendar/moment.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/fullcalendar/fullcalendar.min.js"></script>
<link href="${ctx}/resources/js/fullcalendar/fullcalendar.min.css" rel="stylesheet">
<style>

    .fc-body td:not(.fc-other-month){
        cursor: pointer;
    }

    .fc-day-number {
        font-size: 24px;
    }

    .fc-day-top.fc-other-month {
        opacity: 0.1;
    }
    .fc-time {
        display: none;
    }
    .fc-content{
        text-align: center;
    }
</style>
<script>

    // 更新日历 视图显示
    function updateByPMSCalendars() {
        var resource = $("#calendar").fullCalendar('clientEvents');
        if (resource != null) {
            $.each(resource, function (index, obj) {
            	var someDay = moment(obj.someDay).format('YYYY-MM-DD');
                //var someDay = new Date(obj.someDay).format("yyyy-MM-dd");
                if (obj.type == '1') {/*节假日*/
                    $(".fc-body [data-date=" + someDay + "] .fc-day-number").css("color","red");
                    $(".fc-body [data-date=" + someDay + "] .fc-day-number").css("font-weight","bolder");
                } else {
                    $(".fc-body [data-date=" + someDay + "] .fc-day-number").css("opacity", 1);
                }
            });
        }
    }

    function decimalToPercent(decimal) {
        decimal = decimal.toFixed(4);
        decimal = decimal.slice(2, 4) + "." + decimal.slice(4, 6) + "%";
        return decimal;
    }

    var calendarWindow;
    $(function () {
        calendarWindow = $("#calendar").parent();

        // 更新日历数据源
        calendarWindow.updatePMSDate = function (pmsDate, dateType) {
          //  font-weight
            $(".fc-body [data-date=" + pmsDate + "] .fc-day-number").css("color","black");
            $(".fc-body [data-date=" + pmsDate + "] .fc-day-number").css("font-weight","normal");
           // $(".fc-body [data-date=" + pmsDate + "] .fc-day-number").css("opacity", 1);
            var eventsSource = $("#calendar").fullCalendar('getEventSources');
            if (eventsSource) {
                $("#calendar").fullCalendar('refetchEventSources', eventsSource);
            }
        }

        // 初始化日历插件
        $("#calendar").fullCalendar({
            header: {
                left: 'prev next',
                center: 'title',
                right: 'today'
            },
            buttonText: {
                prev: "上个月",
                next: '下个月',
                prevYear: '上一年',
                nextYear: '下一年',
                today: '返回当月',
                month: '月',
                week: '周',
                day: '日'
            },
            events:{
                url: ctx.rootPath() + "/timeManage/getCalendar",
                type: "post",
                data:function(){
                    var date =$('#calendar').fullCalendar('getDate');
                    return {date: $.fullCalendar.formatDate(date, 'YYYY-MM-DD')};
                },
                success: function (txt) {
                }
            },
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
            firstDay: 1,
            dayNames: ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
            dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],

            loading: function (bLoading, view) {
               if(!bLoading){
                   updateByPMSCalendars();
               }
            }
        });
    })
</script>
<div id="calendar" style="margin:10px 0 0 10px;width: 800px;" >
</div>
