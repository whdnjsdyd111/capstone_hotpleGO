$(function (){
    $(document).on('click','.member-action-btn',function (){
        const arr = $(this).parent().prevAll();
        $('#mem-role').val($(arr[0]).text());
        $('#mem-regDate').val($(arr[1]).text());
        $('#mem-email').val($(arr[2]).text());
        $('#mem-nick').val($(arr[3]).text());
    });
    $(document).on('click','.warn-authority-btn',function (){
       const arr = $(this).parent().prevAll();
       $('#input-role').val($(arr[1]).text());
       $('#mem-nick-sec').val($(arr[4]).text());
        $('#mem-email-sec').val($(arr[3]).text());
        $('#mem-regDate-sec').val($(arr[2]).text());
    });
});
