const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) { xhr.setRequestHeader(header, token); });

$(function() {
    $('.reply-content').each(function() {
        if ($(this).text() !== "")
            init(this);
    })
})

function reply_mode(obj){
    if ($(obj).hasClass('reply-edit-off')) {
        $(obj).removeClass('reply-edit-off');
        $(obj).addClass('reply-edit-on');
        $(obj).parent().parent().find('.reply-edit').show();
    } else {
        $(obj).addClass('reply-edit-off');
        $(obj).removeClass('reply-edit-on');
        $(obj).parent().parent().find('.reply-edit').hide();
    }
}

function init(obj) {
    $(obj).parent().parent().find('.reply-edit').hide();
    $(obj).parent().parent().find('.reply-text').show();
    $(obj).parent().parent().find('.reply-edit-off').hide();
}

function hide_reply(obj, text) {
    $(obj).parent().find('.form-control').val("");
    $(obj).parent().parent().find('.reply-edit').hide();
    $(obj).parent().parent().find('.reply-content').text(text);
    $(obj).parent().parent().find('.reply-text').show();
    $(obj).parent().parent().find('.reply-edit-on').hide();
    $(obj).parent().parent().find('.reply-edit-on').addClass('reply-edit-off');
    $(obj).parent().parent().find('.reply-edit-on').remove('reply-edit-on');
}

function reply_submit(obj){
    const text = $(obj).parent().find('.form-control').val();
    const riCode = $(obj).next().val();
    $.ajax({
        type: "POST",
        url: "/manager/rest/save-reviewReply",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            rvOwnCont : text,
            riCode : riCode
        }),
        success: function(data) {
            alert(data);
            hide_reply(obj, text);
        },
        error: function (xhr, status, err) {
            alert(xhr.responseText);
        }
    });
}
function reply_modify(obj){
    const text= $(obj).parent().parent().find('.reply-content').text();
    $(obj).parent().parent().parent().find('.reply-edit').show();
    $(obj).parent().parent().parent().find('.form-control').val(text);
    $(obj).parent().parent().parent().find('.reply-text').hide();
}
function reply_delete(obj){
    $.ajax({
        type: "POST",
        url: "/manager/rest/delete-reviewReply",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            riCode : $(obj).next().val()
        }),
        success: function(data) {
            alert(data);
            $(obj).parent().parent().parent().find('.reply-content').text("");
            $(obj).parent().parent().parent().find('.reply-text').hide();
            $(obj).parent().parent().parent().find('.reply-edit-off').show();
        },
        error: function (xhr, status, err) {
            alert(xhr.responseText);
        }
    });
}