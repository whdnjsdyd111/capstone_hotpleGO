$(function (){
    $('.selector').change(function (){
        let str = $(this).val();
        switch (str){
            case '평일':
                $('#weekday-box').show();
                $('#weekday-box').find('.selector').val('평일');
                $('#weekend-box').hide();
                $('#holiday-box').hide();

                break;
            case "주말":
                $('#weekday-box').hide();
                $('#weekend-box').show();
                $('#holiday-box').hide();
                $('#weekend-box').find('.selector').val('주말');
                $(this).val('주말');
                break;
            case "정기 휴무":
                $('#weekday-box').hide();
                $('#weekend-box').hide();
                $('#holiday-box').show();
                $('#holiday-box').find('.selector').val('정기 휴무');
                $(this).val('정기 휴무');
                break;
        }
    });
    $('.btn-primary').click(function (){
        $(this).hide();
        $(this).prev().show();
        $(this).parent().find('.form-control').attr('readonly',true);
        $(this).parent().find('.selector').attr('readonly',false);
    });

    $('.btn-info').click(function (){
        $(this).hide();
        $(this).next().show();
        $(this).parent().find('.form-control').attr('readonly',false);
    });

});


