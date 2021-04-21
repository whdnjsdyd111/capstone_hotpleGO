$(function(){
    let before_category;

    $('#submit-menu').click( function() {
        let formData = new FormData();
        formData.append("meCate", $('#me-category').val());
        formData.append("meName", $('#me-name').val());
        formData.append("mePrice", $('#me-price').val());
        formData.append("meIntr", $("#me-intro").val());
        formData.append("meHashTag", $('#me-hashtag').val());
        formData.append("upload", $('#me-picture')[0].files[0]);
        $.ajax({
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            url: "/manager/rest/menu-add",
            success: function (data, status, xhr) {
                let cate = $("div[name='" + data.meCate + "']");
                if(cate.length === 0) {
                    add_category(data.meCate);
                    cate = $("div[name='" + data.meCate + "']");
                }
                menu_append(cate.children().children('.area4')[0], data);
            },
            error: function (xhr, status, err) {
                alert("다시 시도해주십시오.");
            }
        });
        init_add_modal();
    });

    $('#update-menu').click(function() {
        let formData = new FormData();
        formData.append("meCode", $('#me-update-code').val());
        formData.append("meCate", $('#me-category-editing').val());
        formData.append("meName", $('#me-title-editing').val());
        formData.append("mePrice", $('#me-price-editing').val());
        formData.append("meIntr", $("#me-intro-editing").val());
        formData.append("meHashTag", $('#me-hashtag-editing').val());
        formData.append("upload", $('#me-picture-editing')[0].files[0]);
        formData.append("uuid", $('#me-update-uuid').val());
        formData.append("uploadPath", $('#me-update-uploadPath').val());
        formData.append("fileName", $('#me-update-fileName').val());
        $.ajax({
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            url: "/manager/rest/menu-update",
            success: function (data, status, xhr) {
                if (before_category === data.meCate) {
                    menu_update(data);
                } else {
                    $(document.getElementById(data.meCode)).parents('table').remove();
                    let cate = $("div[name='" + data.meCate + "']");
                    if(cate.length === 0) {
                        add_category(data.meCate);
                        cate = $("div[name='" + data.meCate + "']");
                    }
                    let before_cate = $("div[name='" + before_category + "']");
                    if(before_cate.find('.area4').children().length === 0) {
                        before_cate.remove();
                    }
                    menu_append(cate.children().children('.area4')[0], data);
                }
            },
            error: function (xhr, status, err) {
                alert("다시 시도해주십시오.");
            }
        });
    })

    /*****오른쪽 동그란화살표 토글*****/
    $(document).on('click', '.icon_arr', function(){
        if ($(this).hasClass('fa-arrow-circle-down'))
        {
            $(this).removeClass('fa-arrow-circle-down')
            $(this).addClass('fa-arrow-circle-up');
        } else {
            $(this).addClass('fa-arrow-circle-down')
            $(this).removeClass('fa-arrow-circle-up');
        }
    });

    $(document).on('click', '.update-modal-btn', function() {
        let menus = $(this).parent();
        before_category = menus.parents('.menu-div').attr('name');
        $('#me-category-editing').val(before_category);
        $('#me-title-editing').val(menus.find('.menu-title').text());
        $('#me-picture-editing').val("");
        $('#me-intro-editing').val(menus.find('.menu-expl').text());
        $('#me-price-editing').val(menus.find('.menu-price').text());
        $('#me-hashtag-editing').val(menus.find('.menu-hashtag').text());
        $('#me-update-code').val(menus.find('.menu-code').val());
        $('#me-update-uuid').val(menus.find('.menu-uuid').val());
        $('#me-update-uploadPath').val(menus.find('.menu-uploadPath').val());
        $('#me-update-fileName').val(menus.find('.menu-fileName').val());
    });

    $('#update-category').click(function() {
        let category = $('#category-editing').val();
        if (before_category === category) return;
        if (confirm("카테고리를 일괄 변경 하시겠습니까?")) {
            $.ajax({
                type: "post",
                data: {
                    before: before_category,
                    category: category
                },
                async: false, //동기, 비동기 여부
                cache : false, // 캐시 여부
                url: "/manager/rest/cate-update",
                success: function(data, status, xhr) {
                    let before = $('div[name="' + before_category + '"]');
                    before.find('.category-title').text(category);
                    before.attr('name', category);
                    alert(data);
                    $('#category-update-modal').modal('hide');
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $(document).on('click', '.update-category', function() {
        let cate = $(this).parents('.menu-div').attr('name');
        before_category = cate;
        $('#category-editing').val(cate);
    });

    $(document).on('click', '.delete-category', function() {
        if(confirm("해당 메뉴들과 카테고리가 일괄삭제 됩니다. 정말 삭제하시겠습니까?")) {
            let parent_div = $(this).parent();
            $.ajax({
                type: "post",
                data: {
                    category: parent_div.attr('name')
                },
                async: false, //동기, 비동기 여부
                cache : false, // 캐시 여부
                url: "/manager/rest/delete-cate",
                success: function(data, status, xhr) {
                    alert(data);
                    parent_div.remove();
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $(document).on('click', '.delete-menu', function() {
        if (confirm("해당 메뉴를 삭제하시겠습니까?")) {
            let parent_div = $(this).parent();
            $.ajax({
                type: "post",
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    meCode : parent_div.children('.menu-code').val(),
                    uuid : parent_div.children('.menu-uuid').val(),
                    uploadPath : parent_div.children('.menu-uploadPath').val(),
                    fileName : parent_div.children('.menu-fileName').val()
                }),
                async: false, //동기, 비동기 여부
                cache : false, // 캐시 여부
                url: "/manager/rest/delete-menu",
                success: function(data, status, xhr) {
                    alert(data);
                    let area_div = parent_div.parents('.area4');
                    parent_div.parents('table').remove();
                    if (area_div.children('table').length === 0) {
                        area_div.parents('.menu-div').remove();
                    }
                },
                error: function (xhr, status, err) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $("input[type='file']").change(function() {
        $(this).next().html($(this)[0].files[0].name);
    });
});

// 여기 까지 메뉴 추가 및 카테고리 추가

function init_add_modal() {
    $('#me-category').val("");
    $('#me-name').val("");
    $('#me-price').val("");
    $("#me-intro").val("");
    $('#me-hashtag').val("");
    $('#me-picture').val("");
}

function menu_append(obj, data) {
    let div =
        '<table style="width:100%">' +
        '<tbody><tr class="row">' +
        '<td class="photo-area col-12 col-sm-6">' +
        '<img src="' + dataToImg(data) + '" alt="메뉴 이미지를 추가해주세요." class="img-fluid">' +
        '</td>' +
        '<td class="menu-text col-12 col-sm-6">' +
        '<input type="button" class="btn btn-info update-modal-btn ml-1" value="메뉴 수정" data-toggle="modal" data-target="#menu-update-modal">' +
        '<input type="button" class="btn btn-dark delete-menu ml-3" value="메뉴 삭제">' +
        '<div><h3 class="menu-title">' + data.meName + '</h3></div>' +
        '<div><h6 class="menu-hashtag">' + data.meHashTag +'</h6></div>' +
        '<div><h6 class="menu-expl">' + data.meIntr +'</h6></div>' +
        '<div class="d-flex"><h6 class="menu-price">' + data.mePrice +'</h6><h6>원</h6></div>' +
        '<input type="hidden" class="menu-code" id="' + data.meCode + '" value="' + data.meCode + '">' +
        '<input type="hidden" class="menu-uuid" value="' + data.uuid + '">' +
        '<input type="hidden" class="menu-uploadPath" value="' + data.uploadPath +'">' +
        '<input type="hidden" class="menu-fileName" value="' + data.fileName + '">' +
        '</td></tr></tbody></table>';
    $(obj).append(div);
}

function add_category(category) {
    let div = '<div name="' + category +'" class="menu-div p-2 border-dark border rounded mb-5">' +
        '<input type="button" class="btn btn-danger delete-category" value="카테고리 삭제">' +
        '<section class="area2">' +
        '<ul>' +
        '<li><input type="button" class="btn btn-warning update-category" value="카테고리 수정" data-toggle="modal" data-target="category-update-modal"></li>' +
        '<li><h2 class="category-title">' + category + '</h2></li>' +
        '<li><i class="fa fa-arrow-circle-down icon_arr fa-5x mr-2" aria-hidden="true" data-toggle="collapse" role="button" data-target="#collapse' + category + '"></i></li>' +
        '</ul>' +
        '<section class="area4 collapse" id="collapse' + category +'">' +
        '</section>' +
        '</section>' +
        '</div>';
    $('#field').append(div);
}

function dataToImg(data) {
    if (data.uuid === null || data.uuid === "") {
        return "/images/clip.jpg"; // 디폴트 사진
    } else {
        let url = "http://localhost:8000/hotpleImage/";
        url += data.uploadPath.replaceAll('\\', '/') + '/' + data.uuid + "_" + data.fileName;
        return url;
    }
}

function menu_update(data) {
    let obj = $(document.getElementById(data.meCode)).parent();
    obj.find('.menu-title').text(data.meName);
    obj.find('.menu-hashtag').text(data.meHashTag);
    obj.find('.menu-expl').text(data.meIntr);
    obj.find('.menu-price').text(data.mePrice);
    obj.find('.menu-uuid').val(data.uuid);
    obj.find('.menu-uploadPath').val(data.uploadPath);
    obj.find('.menu-fileName').val(data.fileName);
    obj.prev().find('img').attr('src', dataToImg(data));
}