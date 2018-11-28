function post(api, params, dataType, callback, error) {
    var timestamp = Date.parse(new Date());
    $.ajax({
        url : api + "?timestamp=" + timestamp,
        dataType : dataType,
        method : 'post',
        data : params,
        success : callback,
        error : error || function(data) {
            $.info("警告", data.responseText, "warning");
        }
    });
}

function get(api, params, dataType, callback, error) {
    var timestamp = Date.parse(new Date());
    $.ajax({
        url :  api + "?timestamp=" + timestam,
        dataType : dataType,
        method : 'get',
        data : params,
        success : callback,
        error : error || function(data) {
            $.info("警告", data.responseText, "warning");
        }
    });
}
