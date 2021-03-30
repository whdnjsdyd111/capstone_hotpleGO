$(()=>{

    $('#submit').click(()=>{
        $('.form-control').attr('readonly',true);
        $('#submit').hide();
        $('#modify').show();
    });
    $('#modify').click(()=>{
        $('.form-control').attr('readonly',false);
        $('#modify').hide();
        $('#submit').show();
    });
    $('#delete').click(()=>{
        if(confirm('삭제하시겠습니까?')){
            $('.form-control').attr('readonly',false);
            $('.form-control').val('');
        }

    });
    $('.datetimepicker').datetimepicker({

        step:5,
        lang:'ko',
        format:'Y-m-d H:i',
        theme:'dark',
    })
});