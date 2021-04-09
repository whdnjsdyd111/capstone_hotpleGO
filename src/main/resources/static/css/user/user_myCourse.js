$(function() {
    $('#custom_submit').click(function() {
        requestBody({
            csWith : $('#with').val(),
            csNum : $('#person').val()
        }, '/custom-course', function(data) {
            swal(data);
        }, basicErrorFunc);
    })
})