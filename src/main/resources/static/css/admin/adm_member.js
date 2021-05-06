$(function () {
    $(document).on('click', '.member-action-btn',async function () {
        let ele = this;
            $('#content_modal').load('/admin/content_modal?uCode=' + $(ele).next().val(),function (){
                const arr = $(ele).parent().prevAll();
                $('#mem-role').val($(arr[0]).text());
                $('#mem-regDate').val($(arr[1]).text());
                $('#mem-email').val($(arr[2]).text());
                $('#mem-nick').val($(arr[3]).text());


                $('#user-action').modal('toggle');
            });
    });
    $(document).on('click', '.warn-authority-btn', function () {
        const arr = $(this).parent().prevAll();
        $('#input-role').val($(arr[1]).text());
        $('#mem-nick-sec').val($(arr[4]).text());
        $('#mem-email-sec').val($(arr[3]).text());
        $('#mem-regDate-sec').val($(arr[2]).text());
    });
    $(document).on('click','.show-board',function (){
    /*$('.show-board').click(function() {*/
        location.href = '/board/view/' + $(this).text().replaceAll('/','');

    })
    /*$(document).on('click', '#modal-close', function () {
        $('#user-action').modal('hide');
        $('#content_modal').html('');
    });*/
});

