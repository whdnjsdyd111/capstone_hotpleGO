const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
$(function() {
    $(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });
});

const requestBody = function(data, url, success_callback, error_callback) {
    $.ajax({
        type: "post",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(data),
        async : true, //동기, 비동기 여부
        cache : false, // 캐시 여부
        url: url,
        success: function(data, status, xhr) {
            success_callback(data, status, xhr);
        },
        error: function (xhr, status, err) {
            error_callback(xhr, status, err);
        }
    });
}

const requestParams = function(data, url, success_callback, error_callback) {
    $.ajax({
        type : "post",
        url : url,
        data : data,
        async : true,
        success : function(data, status, xhr) {
            success_callback(data, status, xhr);
        },
        error: function (xhr, status, err) {
            error_callback(xhr, status, err);
        }
    })
}

const requestServlet = function(data, url, success_callback, error_callback) {
    $.ajax({
        type: "post",
        url: url,
        data: data,
        success: function(data, status, xhr) {
            success_callback(data, status, xhr);
        },
        error: function (xhr, status, err) {
            error_callback(xhr, status, err);
        }
    });
}

const requestGet = function(data, url, success_callback, error_callback) {
    $.ajax({
        type: "get",
        url: url,
        data: data,
        success: function(data, status, xhr) {
            success_callback(data, status, xhr);
        },
        error: function (xhr, status, err) {
            error_callback(xhr, status, err);
        }
    })
}

const basicErrorFunc = function(xhr, status, err) {
    swal("실패하였습니다.", xhr.responseText, "error");
}