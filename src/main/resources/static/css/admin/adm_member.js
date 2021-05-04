$(function (){
    $(document).on('click','.member-action-btn',function (){
        const arr = $(this).parent().prevAll();
        $('#mem-role').val($(arr[0]).text());
        $('#mem-regDate').val($(arr[1]).text());
        $('#mem-email').val($(arr[2]).text());
        $('#mem-nick').val($(arr[3]).text());
    });
    $(document).on('click','.warn-authority-btn',function (){
       const role = $(this).parent().prev().prev().text();
       $('#input-role').val(role);
    });
});
