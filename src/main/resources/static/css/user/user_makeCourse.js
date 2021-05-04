const fa = {
    '0' : 'fa fa-cutlery fa-5x',
    '1' : 'fas fa-ice-cream fa-5x',
    '2' : 'fas fa-praying-hands fa-5x',
    '3' : 'fas fa-beer fa-5x',
    '4' : 'far fa-eye fa-5x',
    '5' : 'fas fa-shoe-prints fa-5x'
};

$(function() {
    $(document).on('click', '.plus-course', function() {
        let num = $('.course-index').length;
        if (num >= 9) {
            alert("코스의 장소는 9개 까지만 가능합니다.")
            return;
        }
        let course = '<div class="d-flex flex-column course-index">' +
            '<i class="fa fa-cutlery fa-5x"></i>' +
            '<div class="form-group my-3">' +
            '<select class="form-control course-kind">' +
            '<option value="0" selected>먹거리</option>' +
            '<option value="1">디저트</option>' +
            '<option value="2">놀이/취미</option>' +
            '<option value="3">음주</option>' +
            '<option value="4">보기</option>' +
            '<option value="5">걷기</option>' +
            '</select>' +
            '</div>' +
            '</div>'
        let next = '<i class="fa fa-arrow-circle-right fa-5x my-auto mx-5" aria-hidden="true"></i>' +
            '<i class="fa fa-plus-square fa-5x my-auto mx-5 plus-course" aria-hidden="true"></i>';
        if (num < 8){
            $(this).after(course + next);
        } else {
            $(this).after(course);
        }
        $(this).remove();
    });

    $(document).on("change", ".course-kind", function() {
        $(this).parent().prev().removeClass().addClass(fa[$(this).val()]);
    });

    $('#recommend').click(function() {
        let indexes = "";
        $('.course-index').each(v => {
            indexes += $($('.course-index')[v]).find('.course-kind').val();
        });
        let form = $('#form');
        form.find('input[name=lat]').val(setLat);
        form.find('input[name=lng]').val(setLng);
        form.find('input[name=area]').val($('#area').val());
        form.find('input[name=index]').val(indexes);
        form.submit();
    });
});