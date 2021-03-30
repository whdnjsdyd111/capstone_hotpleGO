$(function (){
    $(document).on('click','.menu-select-btn',function (){
        let menuTitle=$(this).find('.menu-title').text();
        let menuPrice=$(this).find('.menu-price').text();
        list_append(menuTitle,menuPrice);
        changeTotalPrice();
        $('#empty-cart').hide();
    });

    $(document).on('click','.btn-plus',function (){
        let cnt=$(this).prev().text();
        $(this).prev().text(+cnt+1);
        let price=$(this).parent().parent().find('.order-price').text().replace('원','');
        price=(price/(+cnt))*(+cnt+1);
        $(this).parent().parent().find('.order-price').text(price+'원');
        changeTotalPrice();
    });

    $(document).on('click','.btn-minus',function (){
        let cnt=$(this).next().text();
        if(cnt>1){
            $(this).next().text(+cnt-1);
            let price=$(this).parent().parent().find('.order-price').text().replace('원','');
            price=(price/(+cnt))*(+cnt-1);
            $(this).parent().parent().find('.order-price').text(price+'원');
            changeTotalPrice();
        };
    });
    $(document).on('click','.btn-delete',function (){
       $(this).parent().parent().parent().parent().remove();
       changeTotalPrice();
       if(!+$('#total-price').text()){
           $('#empty-cart').show();
       }
    });
});
function list_append(menuTitle,menuPrice){
    let div=
        '<li class="list-group-item ">'+
        ' <div class="cart-item">'+
        '<div class="menu-name ">'+menuTitle+'</div>'+
        '<div class="d-flex item-detail">'+
        '<div class="col-xs-6 pull-left flex-center">'+
        '<i class="btn btn-delete far fa-trash-alt flex-center"></i>'+
        '<span class="order-price">'+menuPrice+'</span>'+
        ' </div>'+
        '<div class="col-xs-6 pull-right flex-center">'+
        '<i class="btn btn-minus far fa-minus-square flex-center"></i>'+
        '<span class="order-num " name="num">1</span>'+
        '<i class="btn btn-plus far fa-plus-square flex-center"></i>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</li>';
    $('.list-container').append(div);
}
function changeTotalPrice(){
    let arr=$('.order-price').get();
    let sum=0;
    arr.forEach(function (i){
        console.log($(i).text().replace('원',''));
        sum+=+$(i).text().replace('원','');
    });
    $('#total-price').text(sum);
}
