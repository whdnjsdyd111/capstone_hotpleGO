$(function(){
    $("#tlt>:first-child").click(function(){
        window.history.back();
    });
    $("#tlt>:last-child").click(function(){
        $(location).attr("href","index.html");
    });

    /*****오른쪽 동그란화살표 토글*****/
    $('body').on('click', '.icon_arr', function(){
        if ($(this).hasClass('fa-arrow-circle-down'))
        {
            $(this).removeClass('fa-arrow-circle-down')
            $(this).addClass('fa-arrow-circle-up');
            $(this).parent().parent().next().show();
            $(this).parent().prev().children().css("color","#22892d");
        } else {
            $(this).addClass('fa-arrow-circle-down')
            $(this).removeClass('fa-arrow-circle-up');
            $(this).parent().prev().children().css("color","#6d6d6d");
            $(this).parent().parent().next().hide();
        }
    });
});

let menu;
function add_menu(obj){
    $(obj).parent().append($('#menu-div').html());
}
function add_category(){
    const tmp = $('#sample').clone();
    $('#field').append(tmp);
    tmp.show();
}
function remove_menu(obj){
    $(obj).parent().parent().parent().parent().remove();
}
function remove_category(obj){
    $(obj).parent().remove();
}
function menu_title_modify_open(obj){
    menu=$(obj).parent().parent();
    $('#category-editing').val("")
}
function submit_menu_title(){
    menu.find('.category-title').text($('#category-editing').val())
    $('#second-Modal').modal('hide');
}

function menu_modify_open(obj){
    menu=$(obj).parent();
    $('#menu-title-editing').val("")
    $('#expl-editing').val("")
    $('#price-editing').val("")
}

function submit_menu(){
    menu.find('.menu-title').text($('#menu-title-editing').val())
    menu.find('.menu-expl').text($('#expl-editing').val())
    menu.find('.menu-price').text($('#price-editing').val())
    $('#myModal').modal('hide');
}