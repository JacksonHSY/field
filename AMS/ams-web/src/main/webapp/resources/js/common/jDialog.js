/**
 * ams自定义弹窗类库
 * @author wulj
 */
;(function (define) {
    define(['jquery', 'underscore'], function ($, _) {
        return (function () {
            // 窗口默认选项
            var defaultOptions = {
                instanceName: '',
                url: ctx.rootPath(),    // 窗口url
                target: '_blank',       // 窗口打开方式(_blank：在新窗口中打开, _self：在相同的框架中打开, _parent：在父框架集中打开, _top：在整个窗口中打开)
                toolbar: 'no',          //
                location: 'no',         //
                directories: 'no',      //
                status: 'no',           //
                scrollbars: 1,          //
                resizable: 0,           //
                fullscreen: false,      // 是否全屏
                width: 1400,            // 窗口宽度
                height: 800,            // 窗口高度
                left: null,             // 坐标x
                top: null,              // 坐标y
                data: {},               // 附加数据
                onDestroy: onDestroy    // 窗口关闭的回调函数
            };

            // WindowDialog对象
            var jDialog = {
                openedWindows: {},
                defaultOptions: defaultOptions,
                open: open ,
                close: close,
                closeAll: closeAll,
                loginTimeout:  loginTimeout
            };

            /**
             * 打开新窗口
             * @param options
             * @returns {*}
             */
            function open(options) {
                if(loginTimeout()){      // 如果session超时，则刷新当前页面
                    if(window.opener){
                        if(window.opener.jDialog){
                            window.opener.jDialog.closeAll();
                        }
                        window.opener.location = ctx.rootPath();
                        window.close();
                    }else{
                        if(window.jDialog){
                            window.jDialog.closeAll();
                        }
                        window.location = ctx.rootPath();
                    }

                    return;
                }

                var targetId;
                if(jDialog.openedWindows[options.url] && !jDialog.openedWindows[options.url].closed){
                    jDialog.openedWindows[options.url].focus();	// exists, focus the window
                    targetId = jDialog.openedWindows[options.url];
                }else{
                    options = options || {};
                    options = $.extend({},jDialog.defaultOptions , options);

                    var left = options.left || (window.screen.width - options.width) / 2;
                    var top = options.top || (window.screen.height - options.height)/7;

                    var params = "toolbar=" + options.toolbar + ",location=" + options.location + ",directories=" + options.directories + ",status=" + options.status + ",scrollbars=" + options.scrollbars + ",resizable=" + options.resizable + ",top=" + top + ",left=" + left + ",width=" + options.width + ",height=" + options.height;
                    targetId = window.open(options.url, "_blank", params);

                    // 窗口关闭时，执行函数
                    targetId.addEventListener("beforeunload", function (e) {
                        targetId.jDialog.closeAll();                // 关闭当前窗口的所有子窗口
                        options.onDestroy();                        // 执行回调函数
                        delete jDialog.openedWindows[options.url];  // 删除属性
                        var confirmationMessage = null;
                        //(e || window.event).returnValue = confirmationMessage; //Gecko + IE
                        return confirmationMessage;                            //Webkit, Safari, Chrome
                    });

                    jDialog.openedWindows[options.url] = targetId;
                }

                return targetId;
            }

            /**
             * 关闭窗口
             * @param url
             */
            function close(url) {
                if (jDialog.openedWindows[url]) {
                    jDialog.openedWindows[url].close(); // 关闭窗口
                    delete jDialog.openedWindows[url];  // 删除属性
                }
            }

            /**
             * 关闭所有窗口
             */
            function closeAll() {
                var dialogs = _.values(jDialog.openedWindows);
                $.each(dialogs, function (index, element) {
                    element.close();
                });
            }

            /**
             * 检查登录是否超时
             * @returns {boolean}
             */
            function loginTimeout(){
                var timeout = false;
                $.ajax({
                    url: ctx.rootPath() + '/sessionExpired',
                    dataType: 'json',
                    data: {},
                    async : false,
                    success: function (result) {
                        if(result.type == 'FAILURE'){
                            timeout = true;
                        }
                    }
                });

                return timeout;
            }

            /**
             * 窗口关闭回调函数
             * @param e
             */
            function onDestroy(e){

            }

            return jDialog;
        })();
    });
}(typeof define === 'function' && define.amd ? define : function (deps, factory) {
    if (typeof module !== 'undefined' && module.exports) { //Node
        module.exports = factory(require('jquery'), require('underscore'));
    } else {
        window['jDialog'] = factory(jQuery, _);
    }
}));