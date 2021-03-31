const fa = {
    '먹거리' : 'fa fa-cutlery fa-5x',
    '디저트' : 'fas fa-ice-cream fa-5x',
    '놀이' : 'fas fa-praying-hands fa-5x',
    '음주' : 'fas fa-beer fa-5x',
    '보기' : 'far fa-eye fa-5x',
    '걷기' : 'fas fa-shoe-prints fa-5x'
};

$(function() {
    $(document).on('click', '.plus-cource', function() {
        let num = $('.cource-index').length;
        if (num >= 9) {
            alert("코스의 장소는 9개 까지만 가능합니다.")
            return;
        }
        let cource = '<div class="d-flex flex-column cource-index">' +
            '<i class="fa fa-cutlery fa-5x"></i>' +
            '<div class="form-group my-3">' +
            '<select class="form-control cource-kind">' +
            '<option value="먹거리" selected>먹거리</option>' +
            '<option value="디저트">디저트</option>' +
            '<option value="놀이">놀이/취미</option>' +
            '<option value="음주">음주</option>' +
            '<option value="보기">보기</option>' +
            '<option value="걷기">걷기</option>' +
            '</select>' +
            '</div>' +
            '</div>'
        let next = '<i class="fa fa-arrow-circle-right fa-5x my-auto mx-5" aria-hidden="true"></i>' +
            '<i class="fa fa-plus-square fa-5x my-auto mx-5 plus-cource" aria-hidden="true"></i>';
        if (num < 8){
            $(this).after(cource + next);
        } else {
            $(this).after(cource);
        }
        $(this).remove();
    });

    $(document).on("change", ".cource-kind", function() {
        $(this).parent().prev().removeClass().addClass(fa[$(this).val()]);
    })
});