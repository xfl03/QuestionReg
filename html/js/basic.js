/**
 *
 * Created by Epix on 2015/7/1.
 */

var BASEURL = 'http://127.0.0.1:5000/';
$(document).ready(function () {

    //init validator
    $('.form-horizontal').validator({
        delay: 300,
        disable: false
    });
    $('#loginModal').modal({backdrop: 'static'});
    $('#registerUsername').tooltip({trigger: 'manual'}).blur(function () {
        $.ajax({
            context: this,
            type: 'GET',
            url: BASEURL + 'api/userexist',
            dataType: 'text',
            data: {username: $(this).val()},
            success: function (data) {
                switch (data) {
                    case '0':
                        $(this).tooltip('hide');
                        break;
                    case '1':
                        $(this).attr('title', 'Username Exist').tooltip('fixTitle').tooltip('show');
                        break;
                    case '2':
                        $(this).attr('title', 'On White List').tooltip('fixTitle').tooltip('show');
                        break;
                    case '3':
                        $(this).attr('title', 'On Black List').tooltip('fixTitle').tooltip('show');
                        break;
                }
            }
        })
    }).on('show.bs.tooltip', function () {
        $('#registerButton').prop('disabled', true);
    }).on('hide.bs.tooltip', function () {
        $('#registerButton').prop('disabled', false);
    });
    $('#loginUsername').tooltip({trigger: 'manual'}).blur(function () {
        $.ajax({
            context: this,
            type: 'GET',
            url: BASEURL + 'api/userexist',
            dataType: 'text',
            data: {username: $(this).val()},
            success: function (data) {
                switch (data) {
                    case '0':
                        $(this).attr('title', 'Username Not Exist').tooltip('fixTitle').tooltip('show');
                        break;
                    case '1':
                        $(this).tooltip('hide');
                        break;
                    case '2':
                        $(this).attr('title', 'On White List').tooltip('fixTitle').tooltip('show');
                        break;
                    case '3':
                        $(this).attr('title', 'On Black List').tooltip('fixTitle').tooltip('show');
                        break;
                }
            }
        })
    }).on('show.bs.tooltip', function () {
        $('#loginButton').prop('disabled', true);
    }).on('hide.bs.tooltip', function () {
        $('#loginButton').prop('disabled', false);
    });
    //return MD5(SHA1(password)+MD5(password))
    function passwordHash(password) {
        return CryptoJS.MD5(CryptoJS.SHA1(password).toString(CryptoJS.enc.Hex) + CryptoJS.MD5(password).toString(CryptoJS.enc.Hex)).toString(CryptoJS.enc.Hex)
    }

    function getIn() {
        console.log('get in');
        $.ajax({
            type: 'GET',
            url: BASEURL + "api/userinfo",
            dataType: 'text',
            success: function (data) {
                var infoArray = data.split(',');
                var isAdmin = infoArray[5];
                var passed = infoArray[9];
                if (isAdmin == '1') {
                    $('.adminOnly').removeClass('hidden');
                }
                if (passed == '1') {
                    $('#startButton').addClass('hidden');
                }
                showInfo(data);
                $('#loginModal').modal('hide');
            }
        });
        $.ajax({
            type: 'GET',
            url: BASEURL + "api/userpast",
            dataType: 'text',
            success: function (data) {
                var infoArray = data.split(',');
                var leftTime = infoArray[1];
                showQuestionInfo(data);
                if (leftTime == 0) {
                    $('#startButton').addClass('hidden');
                }
            }
        })
    }

    function showInfo(data) {
        var infoArray = data.split(',');
        var username = infoArray[0];
        var qq = infoArray[1];
        var age = infoArray[2];
        var registerDate = infoArray[3];
        var valid = infoArray[4];
        var lastLoginDate = infoArray[6];
        var lastLoginIp = infoArray[7];
        var code = infoArray[8];
        var passed = infoArray[9]
        $('#infoUsername').text(username);
        $('#infoQQ').text(qq);
        $('#infoAge').text(age);
        $('#infoRegisterDate').text(registerDate);
        $('#infoValid').text(valid);
        $('#infoLastLoginDate').text(lastLoginDate);
        $('#infoLastLoginIp').text(lastLoginIp);
        $('#infoCode').val(code);
        if (passed == '1') {
            $('#infoPassed').text('Passed');
        }else{
            $('#infoPassed').text('Not Passed')
        }

    }

    function showQuestionInfo(data) {
        var infoArray = data.split(',');
        var doneTime = infoArray[0];
        var leftTime = infoArray[1];
        var score = infoArray.slice(2).join(', ');
        $('#doneTime').text(doneTime);
        $('#leftTime').text(leftTime);
        $('#score').text(score);
    }


    $('#getQuestionInfoForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/userpasta",
                dataType: 'text',
                data: {
                    username: $('#getQuestionInfo').val()
                },
                success: function (data) {
                    showQuestionInfo(data)
                }
            });
        }
        return false;
    });

    $('#getUserInfoForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/userinfoa",
                dataType: 'text',
                data: {
                    input: $('#getInfoUsername').val()
                },
                success: function (data) {
                    showInfo(data)
                }
            });
        }
        return false;
    });
    //switch tabs
    $('#myTabs').find('a').click(function (e) {
        e.preventDefault();
        $(this).tab('show')
    });
    $('#loginTabs').find('a').click(function (e) {
        e.preventDefault();
        $(this).tab('show')
    });

    //login
    $('#loginForm').submit(function (e) {
            if (e.isDefaultPrevented()) {
                // handle the invalid form...
            } else {
                $.ajax({
                    type: 'GET',
                    url: BASEURL + "api/login",
                    dataType: 'text',
                    data: {
                        username: $('#loginUsername').val(),
                        password: passwordHash($('#loginPassword').val())
                    },
                    success: function (data) {
                        if (data == 'Success.') {
                            getIn();
                        }
                    }
                });
            }
            return false;
        }
    );
    //register
    $('#registerForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + 'api/reg',
                dataType: 'text',
                data: {
                    username: $('#registerUsername').val(),
                    password: passwordHash($('#registerPassword').val()),
                    email: $('#registerQQ').val(),
                    age: $('#registerAge').val()
                },
                success: function (data) {
                    if (data == 'Success.') {
                        getIn();
                    }
                }
            });
        }
        return false;
    });
    $('#getQuestionForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + 'api/userques',
                dataType: 'text',
                success: function (data) {
                    var questionArray = data.split(',');
                    var questionNumber = questionArray[0];
                    for (var i = 1; i <= questionNumber; i++) {
                        var q = questionArray[i];
                        var question = $('<div class="panel panel-default question-panel" id="questionPanel' + q + '">\
                        <div class="panel-heading"></div>\
                        <div class="panel-body"><div class="btn-group" data-toggle="buttons">\
                            <label class="btn btn-default">\
                            <input type="radio" name="question' + q + '" value="a" required></label>\
                        <label class="btn btn-default">\
                            <input type="radio" name="question' + q + '" value="b" required></label>\
                        <label class="btn btn-default">\
                            <input type="radio" name="question' + q + '" value="c" required></label>\
                        <label class="btn btn-default">\
                            <input type="radio" name="question' + q + '" value="d" required></label></div></div></div>');
                        $('#questionSubmit').before(question);
                        $.ajax({
                            type: 'GET',
                            url: BASEURL + 'api/question',
                            dataType: 'text',
                            data: {q: q},
                            q: q,
                            success: function (questionContent) {
                                fillQuestion(this.q, questionContent)
                            }
                        })
                    }
                }
            });
            $(this).addClass('hidden');
            $('#questionFormWrapper').toggleClass('hidden');
        }
        return false;
    });

    function fillQuestion(questionNumber, questionContent) {
        var questionArray = questionContent.split('{SPLITLINE}');
        var questionPanel = $('#questionPanel' + questionNumber);
        questionPanel.find('.panel-heading').text(questionArray[0]);
        var questionRadios = questionPanel.find('input');
        for (var i = 0; i < 4; i++) {
            $(questionRadios[i]).after(document.createTextNode(questionArray[i + 1]))
        }
    }

    $('#questionForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/answer",
                dataType: 'text',
                data: {
                    a: $.map($('input:checked', '#questionForm'), function (c) {
                        return $(c).val()
                    }).join(',')
                },
                success: function (data) {
                    var result = data.split(',');
                    alert('Score: ' + result[0] + ', ' + (result[1] == '1' ? 'Pass' : 'No Good'));
                    $.ajax({
                        type: 'GET',
                        url: BASEURL + "api/userpast",
                        dataType: 'text',
                        success: function (data) {
                            var infoArray = data.split(',');
                            var leftTime = infoArray[1];
                            showQuestionInfo(data);
                            if (leftTime == 0) {
                                $('#startButton').addClass('hidden');
                            }
                        }
                    });
                    $('#questionFormWrapper').toggleClass('hidden');
                    $('#getQuestionForm').removeClass('hidden');
                    $('.question-panel').remove();
                }
            });
        }
        return false;
    });

    $('#changeForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/changepw",
                dataType: 'text',
                data: {
                    old: passwordHash($('#oldPassword').val()),
                    new: passwordHash($('#changePassword').val())
                },
                success: function (data) {
                    if (data == 'Success.') {
                        alert('Changed!');
                    }
                }
            });
        }
        return false;
    });
    $('#verifyForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/verify",
                dataType: 'text',
                data: {
                    input: $('#verify').val()
                },
                success: function (data) {
                    if (data == '1') {
                        alert('Done!');
                    }
                }
            });
        }
        return false;
    });
    $('#addWhitelistForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/whitelist",
                dataType: 'text',
                data: {
                    username: $('#addWhitelist').val()
                },
                success: function (data) {
                    if (data == '1') {
                        alert('Done!');
                    }
                }
            });
        }
        return false;
    });
    $('#addBlacklistForm').submit(function (e) {
        if (e.isDefaultPrevented()) {
            // handle the invalid form...
        } else {
            $.ajax({
                type: 'GET',
                url: BASEURL + "api/blacklist",
                dataType: 'text',
                data: {
                    username: $('#addBlacklist').val()
                },
                success: function (data) {
                    if (data == '1') {
                        alert('Done!');
                    }
                }
            });
        }
        return false;
    });

});