$(function(){
    $(document).on('click','.doing-hotple',function (){
        $('.doing-reservation').show();
        $('.done-reservation').hide();
        $(this).removeClass('btn-outline-primary');
        $(this).addClass('btn-primary');
        let done = $('.done-hotple');
        done.addClass('btn-outline-success');
        done.removeClass('btn-success');
    });
    $(document).on('click','.done-hotple',function (){
        $('.doing-reservation').hide();
        $('.done-reservation').show();
        $(this).removeClass('btn-outline-success');
        $(this).addClass('btn-success');
        let doing = $('.doing-hotple');
        doing.addClass('btn-outline-primary');
        doing.removeClass('btn-primary');
    });

    $('.doing-hotple').html("진행중인 장소<br>" + $('.doing-reservation').length + "건");
    $('.done-hotple').html("완료된 장소<br>" + $('.done-reservation').length + "건");

    $('.modal-menu').click(function() {
        let riCode = $(this).next().val();
        let str = "";
        let total = 0;
        rs_map[riCode].forEach(i => {
            str += "<tr><td>" + i.meName + "</td><td>" + i.mePrice + "</td><td>" + i.rsMeNum + "</td></tr>";
            total += i.mePrice;
        });
        $('#menu-info').html(str);
        $('#total').text(total);
        let time = new Date(rs_map[riCode][0].riTime);
        $('#riTime').text(time.toLocaleDateString() + " " + time.toLocaleTimeString());
    });

    $('.submit-review').click(function() {
        $.ajax({
            type: "post",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({
                riCode : $(this).parent().prop('id').replaceAll('review', ''),
                rvRating : $(this).prev().children('select').val(),
                rvCont : $(this).prev().children('textarea').val()
            }),
            async : true, //동기, 비동기 여부
            cache : false, // 캐시 여부
            url: "/submit-review",
            success: function(data, status, xhr) {
                alert(data);
                self.location.reload();
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });

    $('.update-review').click(function() {
        $.ajax({
            type: "post",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({
                riCode : $(this).parent().prop('id').replaceAll('review', ''),
                rvRating : $(this).prev().prev().children('select').val(),
                rvCont : $(this).prev().prev().children('input').val()
            }),
            async : true, //동기, 비동기 여부
            cache : false, // 캐시 여부
            url: "/update-review",
            success: function(data, status, xhr) {
                alert(data);
                self.location.reload();
            },
            error: function (xhr, status, err) {
                alert(xhr.responseText);
            }
        });
    });

    $('.cancel-reservation').click(function() {
        requestServlet({
            riCode : $(this).prev().val()
        }, "/refund", function(data) {
            swal("환불 성공!", data, "success").then(() => { location.reload(); });
        }, basicErrorFunc);
    })
});