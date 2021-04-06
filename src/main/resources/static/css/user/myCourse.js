$(function(){
    $(document).on('click','.doing-hotple',function (){
        $('.doing-reservation').show();
        $('.done-reservation').hide();
        $(this).removeClass('btn-outline-primary');
        $(this).addClass('btn-primary');
        $('.done-hotple').addClass('btn-outline-success');
        $('.done-hotple').removeClass('btn-success');
    });
    $(document).on('click','.done-hotple',function (){
        $('.doing-reservation').hide();
        $('.done-reservation').show();
        $(this).removeClass('btn-outline-success');
        $(this).addClass('btn-success');
        $('.doing-hotple').addClass('btn-outline-primary');
        $('.doing-hotple').removeClass('btn-primary');
    });
});
