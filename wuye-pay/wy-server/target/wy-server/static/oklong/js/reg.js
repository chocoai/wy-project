$(function(){
	var hasPopLogin = $("#popLoginWindow").length==1;
	// 验证表单
	$('#ajaxReg').validate({
		rules:{
			LoginName:{
				required:true,
				isMobile:true
			},
			PassWord:{
				required:true,
				minlength:6,
				maxlength:6
			},
			confirm_password:{
				required:true,
				equalTo: "#PassWord",
				minlength:6,
				maxlength:6
			},
			SmsCode:{
				required:true,
				minlength:6,
				maxlength:6
			}
		},
		messages:{
			LoginName:{
				required:abTips.valid_phone,
				isMobile :abTips.valid_isMobile
			},
			SmsCode:{
				required:abTips.sms_input,
				minlength:abTips.sms_len(6),
				maxlength:abTips.sms_len(6)
			},
			PassWord:{
				required:abTips.valid_pwd,
				minlength:abTips.valid_pwd_len(6),
				maxlength:abTips.valid_pwd_len(6)
			},
			confirm_password:{
				required:abTips.valid_pwd,
				minlength:abTips.valid_pwd_len(6),
				maxlength:abTips.valid_pwd_len(6),
				equalTo:abTips.valid_pwd_not_match
			}
		},
		errorPlacement: function(error, element) {  
		    error.appendTo(element.closest('dl').next('.formErrBox'));  
		}

	});
	var VALIDATED_CODE_ERR = 2300;
	var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
	var $regName = $("#RegName");
	$regName.keyup(function(){
		if(reg.test($('#RegName').val())){
			$('.ac_regGetsms').removeClass('btn_gray');
		}else{
			$('.ac_regGetsms').addClass('btn_gray');
		}
	});
	
	$('input[name="LoginName"]').focus(function(){
		$('label[name="echo"]').remove();
		});
	$('input[name="SmsCode"]').focus(function(){
		$('label[name="vcodeerr"]').remove();
		});
	$("input").keyup(function(){
			$('.errBody').empty();
			$('.errTop').hide();
		});
	$('#ajaxReg').ajaxForm({
		dataType:'JSON',
		beforeSerialize : function() {
			encValue('#ajaxReg');
		},
		success:function(ret){
			if (ret.RetCode == 0) {
				pushCookieStorage(ret.flushCookies);
				var redirectUrl = ret.Body && ret.Body.redirectUrl;
				if (redirectUrl && !hasPopLogin) {
					window.location.href = redirectUrl;
				} else {
					location.reload();
				}
			}else{
				if(ret.RetCode==VALIDATED_CODE_ERR){
					$('input[name="SmsCode"]').closest('dl').next('.formErrBox').append("<label name='vcodeerr' class='error'>"+ret.ErrMsg+"</label>");
				}else{
					$('.errBody').append(ret.ErrMsg);
					$('.errTop').show();
				}
				resetEncValue();
			}
		},error:function(){
			$('.errBody').append(ret.ErrMsg);
			$('.errTop').show();
			resetEncValue();
		}
	});
	
	// 再次获取短信验证码
	function clickGetSms(element,times){
		$('label[name="echo"]').remove();
		var $this = element;
		var timeGetAgain = null;
		$this.addClass('btn_gray').unbind('click');
		$this.html(times);
		function getAgain(){
			if(parseInt($this.html()) > 0){
				$this.html($this.html() - 1);
				timeGetAgain = setTimeout(getAgain,1000);
			}else{
				clearTimeout(timeGetAgain);
				$this.removeClass('btn_gray').html(abTips.sms_obtain);
				$('.ac_regGetsms').bind('click',regGetsmsAction);
			}
		}
		timeGetAgain = setTimeout(getAgain,1000);
	}
	
	var REG_LOGIN_NAME_EXIST = 2250;
	var REG_SMS_CODE_REQUEST_FREQUENTLY = 2302;
	var regCodeLink = $RegCodeLink;
	function regGetsmsAction(){
		$('label[name="echo"]').remove();
		if(reg.test($('#RegName').val() ) ){
			var target = $(this);
			target.html(abTips.msg_waiting);
			target.addClass('btn_gray').unbind('click');
			$.ajax({
				url:regCodeLink,
				data:{
					Phone:$('#RegName').val()
				},
				success:function(ret){
					if( ret.RetCode == 0 ){
						clickGetSms(target,ret.Body.Wait);
					}else {
						if(REG_SMS_CODE_REQUEST_FREQUENTLY == ret.RetCode){
							clickGetSms(target,ret.ErrAttrs.time);
						}else{
							target.removeClass('btn_gray').html(abTips.sms_obtain);
							$('.ac_regGetsms').bind('click',regGetsmsAction);
						}
						if(REG_LOGIN_NAME_EXIST == ret.RetCode){
							$('input[name="LoginName"]').closest('dl').next('.formErrBox').append("<label name='echo' class='error'>"+ret.ErrMsg+"</label>");
							/*$('.errBody').text(ret.ErrMsg);
							$('.errTop').show();*/
						}else{
							$('.errBody').text(ret.ErrMsg);
							$('.errTop').show();
						}
					}
				},
				error:function(ret){
					$('.errBody').append(ret.ErrMsg);
					$('.errTop').show();
				}
			});
		}
 	}
	
	$('.ac_regGetsms').bind('click',regGetsmsAction);
});

function swapUserPopWindow(popWindow){
	var showWindow = "#ajaxLogin";
	var hideWindow = "#ajaxReg";
	if( popWindow == 'reg' ){
		var swap = hideWindow; 
		hideWindow = showWindow;
		showWindow = swap; 
	} 
	$(showWindow).hide();
	
	$(hideWindow).show();
	
//	$(showWindow).fadeToggle(function(){
//		$(hideWindow).fadeToggle(function(){});
//	});	
}
/*var flag = true;
function disPwd(){
	      if(flag){
	        $("#DisPwd").val($("#PassWord").val());
	        $("#PassWord").hide();
	        $("#DisPwd").show();
	        flag = false;
	      }else{
	        $("#PassWord").val($("#DisPwd").val());
	        $("#DisPwd").hide();
	        $("#PassWord").show();
	        flag = true;
	 }
};*/