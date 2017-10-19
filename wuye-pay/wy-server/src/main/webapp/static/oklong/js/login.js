$(function() {
	var hasPopLogin = $("#popLoginWindow").length==1;
	// 验证表单
	$('#ajaxLogin').validate({
		rules : {
			LoginName : {
				required : true,
				minlength : 4,
				maxlength : 24
			},
			PassWord : {
				required : true,
				minlength : 6,
				maxlength : 24
			}
		},
		messages : {
			LoginName : {
				required : abTips.valid_loginname_len(4, 24),
				minlength : abTips.valid_loginname_len(4, 24),
				maxlength : abTips.valid_loginname_len(4, 24)
			},
			PassWord : {
				required : abTips.valid_loginpwd(6, 24),
				minlength : abTips.valid_loginpwd(6, 24),
				maxlength : abTips.valid_loginpwd(6, 24)
			}
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.next('.formErrBox'));
		}

	});
	$('input[name="PassWord"]').focus(function(){
		$("#authent").remove();
		});
	
	var $LoginName = $('input[name="LoginName"]');
	var log = /[^\w\-]/g;
	$LoginName.keyup(function(){
		var tel = $LoginName.val();
		tel=tel.replace(log,'');
		if(tel!=$LoginName.val()){
			$LoginName.val(tel);
		}
	});
	$LoginName.blur(function(){
		var tel = $LoginName.val();
		tel=tel.replace(log,'');
		if(tel!=$LoginName.val()){
			$LoginName.val(tel);
		}
	});
	// ajax提交 成功后刷新当前页面
	$('#ajaxLogin').ajaxForm({
		dataType : 'JSON',
		beforeSerialize : function() {
			encValue('#ajaxLogin');
		},
		success : function(ret) {
			if (ret.RetCode == 0) {
				pushCookieStorage(ret.flushCookies);
				var redirectUrl = ret.Body && ret.Body.redirectUrl;
				if (redirectUrl && !hasPopLogin) {
					window.location.href = redirectUrl;
				} else {
					location.reload();
				}
			} else {
				var code = ret.RetCode;
				$('input[name="PassWord"]').next('.formErrBox').append("<label id='authent' class='error'>"+ret.ErrMsg+"</label>");
				/*abAlert({
					title : '登录异常',
					msg : ret.ErrMsg,
					okTxt : abTips.input_again
				});*/
				resetEncValue();
			}
		},
		error : function(e) {
			abAlert({
				title : '登录异常',
				msg : abTips.system_error,
				okTxt : abTips.input_again
			});
			resetEncValue();
		}
	});
});