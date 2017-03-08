$(function () {
    $('#addChoice').on('click', function () {
        $("#addQue").append(
            "<div class='input-field col s12'> <input name='choices' id='select' type='text' class='validate sel''/> <label for='select'>选项：</label> </div>");
    });
    $("#submitQue").on('click', function () {
        var arr = [];

        var submitForm = $("#submitQuestion");

        $('.sel').each(function (i) {
            var choiceVal = $(this).val();
            arr.push(choiceVal);
        });

        var formData = new FormData();
        var file = $("#image")[0].files[0];
        formData.append('file', file);
        var content = $("#content").val();
        if (content == null || content.length == 0) {
            alert("题目内容不能为空!")
            return false;
        }
        formData.append('question', content);
        formData.append('department', $("select option:selected").val());
        formData.append('choices', arr);
        var result = $("#result").val();
        if (result == null || result.length == 0) {
            alert("答案不能为空!")
            return false;
        }
        formData.append('result', result);
        formData.append('tip', $("#tip").val());
        $.ajax({
            url: submitForm.attr('action'),
            data: formData,
            type: "POST",
            //dataType: "application/json",
            headers: {
                'X-CSRF-TOKEN': submitForm.find(">input[name=_csrf]").val()
            },
            contentType: false,
            processData: false,
            error: function () {
                alert('添加失败');
            },
            success: function (data) {
                alert('添加成功');
            }
        });
        return false;
    });
});

//{"question": "Hello World", "choices": ["Apple", "Banana","Grape"], "department": "计算机部"}
