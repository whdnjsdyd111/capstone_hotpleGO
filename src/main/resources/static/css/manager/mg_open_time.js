$(()=>{
    $('.selector').change(()=>{
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
                $('#weekend-box').find('.selector').val('주말');
                $('#holiday-box').hide();
                break;
            case "정기 휴무":
                $('#weekday-box').hide();
                $('#weekend-box').hide();
                $('#holiday-box').show();
                $('#holiday-box').find('.selector').val('정기 휴무');
                break;
        }
    });
    $('.btn-primary').click(()=>{
        $(this).find('.form-control').attr('readonly',true);
    });

    $('.btn-info').click(()=>{
        $(this).find('.form-control').attr('readonly',false);
    });
})
