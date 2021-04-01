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
       if(!+$('#cart-total-price').text()){
           $('#empty-cart').show();
       }
    });
    $(document).on('click','#reservation-btn',function (){
        console.log('whwvkf');
        if(+$('#cart-total-price').text().replace('원','')){
            console.log('tlqkf');
            $('#reservation-confirm').children().remove();
            $('#reservation-total-price').text('');
            let carts=$('.cart-item').get();
            carts.forEach(function (i){
                let div=
                    '<div class="d-flex">'+
                    '<h6>'+$(i).find('.menu-name')+'</h6>'+
                    '<h6>'+$(i).find('.order-num')+'</h6>'+
                    '<h6>'+$(i).find('.order-price')+'</h6>'+
                    '</div>'
                $('#reservation-confirm').append(div);
            });
            $('#reservation-total-price').append('총합 : '+$('#cart-total-price').text());
        }else {
                $('#reservation-modal').modal('hide')
        };
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
        '<span class="order-num " name="num">1</span>'+
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
    $('#cart-total-price').text(sum);
}
