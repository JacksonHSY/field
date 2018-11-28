/**
 * Created by YM10106 on 2018/4/20.
 */

// 所有搜索页面的搜索按钮绑定Enter快捷键
hotkeys('enter', function (event, handler) {
    var tab = $layoutContainer.tabs("getSelected");	// 获取选中的tab页面
    var $searchBtn = $('.fa-search', tab);
    var $linkbutton = $searchBtn.closest('.easyui-linkbutton');
    if($linkbutton){
        $linkbutton.trigger('click');	// 获取tab页面的搜索按钮
    }
});

// 所有tab页面，绑定F5刷新页面的快捷键
hotkeys('f5,ctrl+r', function(event, handler){
    var tab = $layoutContainer.tabs("getSelected");
    if(tab){
        event.preventDefault();					// 阻止浏览器默认的刷新
        tab.panel('open').panel('refresh');		// 刷新选中的tab页面
    }
});

// 禁止页面ctrl + S 保存页面
hotkeys("ctrl+s",function(event){
    event.preventDefault();					// 阻止浏览器默认的刷新
});
// 禁止页面 右键
$(document).bind("contextmenu",function(e){
    return false;
});