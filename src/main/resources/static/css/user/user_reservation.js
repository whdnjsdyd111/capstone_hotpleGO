$(function(){
    $(document).on('click','.doing-hotple',function (){
        $('.doing-reservation').show();
        $('.done-reservation').hide();
        let done = $('.done-hotple');
        $(this).removeClass('wonyong-btn-outline');
        $(this).addClass('wonyong-btn');

        done.removeClass('wonyong-btn');
        done.addClass('wonyong-btn-outline');

        $(this).removeClass('w-btn-indigo-outline');
        $(this).addClass('w-btn-indigo');

        done.removeClass('w-btn-skin');
        done.addClass('w-btn-skin-outline');
    });
    $(document).on('click','.done-hotple',function (){
        $('.doing-reservation').hide();
        $('.done-reservation').show();
        let doing = $('.doing-hotple');
        $(this).removeClass('wonyong-btn-outline');
        $(this).addClass('wonyong-btn');

        doing.removeClass('wonyong-btn');
        doing.addClass('wonyong-btn-outline');

        $(this).removeClass('w-btn-skin-outline');
        $(this).addClass('w-btn-skin');

        doing.removeClass('w-btn-indigo');
        doing.addClass('w-btn-indigo-outline');
    });

    $('.doing-hotple').html("진행중인 장소<br>" + $('.doing-reservation').length + "건");
    $('.done-hotple').html("완료된 장소<br>" + $('.done-reservation').length + "건");

    $('.modal-menu').click(function() {
        let riCode = $(this).next().val();
        let str = "";
        let total = 0;
        rs_map[riCode].forEach(i => {
            str += "<tr><td>" + i.meName + "</td><td>" + i.mePrice.toLocaleString() + "</td><td>" + i.rsMeNum + "</td></tr>";
            total += i.mePrice;
        });
        $('#menu-info').html(str);
        $('#total').text(total.toLocaleString());
        let time = new Date(rs_map[riCode][0].riTime);
        $('#riTime').text(time.toLocaleDateString() + " " + time.toLocaleTimeString());
    });

    $('.submit-review').click(function() {
        let riCode = $(this).next().val();
        requestBody({
            riCode : riCode,
            rvRating : $('input[name=rating' + riCode + ']:checked').val(),
            rvCont : $(this).parent().prev().children('textarea').val()
        }, "/submit-review", function(data) {
            swal("리뷰 감사합니다!", data, "success").then(() => self.location.reload());
        }, basicErrorFunc);
    });

    $('.update-review').click(function() {
        let riCode = $(this).next().val();
        requestBody({
            riCode : riCode,
            rvRating : $('input[name=rating' + riCode + ']:checked').val(),
            rvCont : $(this).parent().prev().children('textarea').val()
        }, "/update-review", function(data) {
            swal("수정 완료!", data, "success").then(() => self.location.reload());
        }, basicErrorFunc);
    });

    $('.cancel-reservation').click(function() {
        requestServlet({
            riCode : $(this).prev().val()
        }, "/refund", function(data) {
            swal("환불 성공!", data, "success").then(() => { location.reload(); });
        }, basicErrorFunc);
    })
});

function readURL(input) {
    if (input.files && input.files[0]) {

        var reader = new FileReader();

        reader.onload = function(e) {
            $('.image-upload-wrap').hide();

            $('.file-upload-image').attr('src', e.target.result);
            $('.file-upload-content').show();

            $('.image-title').html(input.files[0].name);
        };

        reader.readAsDataURL(input.files[0]);

    } else {
        removeUpload();
    }
}

function removeUpload() {
    $('.file-upload-input').replaceWith($('.file-upload-input').clone());
    $('.file-upload-content').hide();
    $('.image-upload-wrap').show();
}
$('.image-upload-wrap').bind('dragover', function () {
    $('.image-upload-wrap').addClass('image-dropping');
});
$('.image-upload-wrap').bind('dragleave', function () {
    $('.image-upload-wrap').removeClass('image-dropping');
});