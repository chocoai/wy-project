$(function() {
	var $window = $(window);

	// 展开和收起支付方式操作
	$('.ac_togglePayment').click(function() {
		$(this).closest('.togglePayment').toggleClass('current');
	});

	// 给元素赋予全高
	function fullHeight(item) {
		item.height($window.height());
	}
	fullHeight($('.fullHeight'));

	// 展开和收起选择银行操作
	$('.ac_check_bank dt').click(function() {
		$(this).closest('li').toggleClass('current');
	});
	// 银行列表选择
	$('.ac_check_bank').find('dd').click(function() {
		var $this = $(this);
		if (!$this.hasClass('addOthers')) {
			var thisSpan = $this.find('span')
			var $thisDt = $this.closest('dl').find('dt');
			var curSpan = $thisDt.find('span')
			curSpan.find('input[type="radio"]').removeAttr('checked');
			thisSpan.find('input[type="radio"]').attr('checked', 'checked');
			var thisHtml = thisSpan.html();
			var curHtml = curSpan.html();
			curSpan.html(thisHtml);
			thisSpan.html(curHtml);
			$this.closest('li').removeClass('current');
		}
	});

	// 所有控件失去焦点
	function loseFocus() {
		$select.removeClass('current');
		$txtNormal.removeClass('current');
	}

	// 下拉菜单select控件
	// 初始化
	var $select = $('.select');
	$select.each(function(i, item) {
		var $item = $(item);
		$item.find('>p').html($item.find('option:selected').html());
	});
	$select.find('select').change(
	        function() {
		        var $this = $(this);
		        $this.closest('.select').find('>p').html(
		                $this.find('option:selected').html());
	        });
	$select.find('select').focus(function() {
		loseFocus();
		$(this).closest('.select').addClass('current');
	});

	// txt-normal
	var $txtNormal = $('.txt-normal');
	$txtNormal.find('input').focus(function() {
		loseFocus();
		$(this).closest('.txt-normal').addClass('current');
	});

	// txt_normal 100%
	var $txt_100 = $('.txt_100');
	function setText100() {
		$txt_100.each(function(i, item) {
			var $item = $(item);
			$item.width($item.width() - ($item.innerWidth() - $item.width()));
		});
	}
	// setText100();
	window.setText100 = setText100;

	// msgList toggle
	var $msgList = $('.msgList');
	$msgList.find('.body').click(function() {
		$(this).toggleClass('current');
	});

	// tab切换
	var $tab = $('.tab');
	$tab.find('li').click(
	        function() {
		        var $this = $(this);
		        var clickIndex = $this.index();
		        $this.addClass('current').siblings().removeClass('current');
		        $this.closest('.tabStream').find('.tabItem').eq(clickIndex)
		                .show().siblings('.tabItem').hide();
	        });

	// 确定弹出层
	function abConfirm(options) {
		options = options || {};
		var defaultOptions = {
		    msg : abTips.msg_waiting,
		    okTxt : abTips.ok,
		    cancTxt : abTips.cancel,
		    textAlign : 'center',
		    onAccept : function() {
		    },
		    acceptLink : 'javascript:void(0)',
		    onCancel : function() {
		    }
		};
		options = $.extend({}, defaultOptions, options);
		var popHtml = '<div class="mask"></div><div class="pop pop_confirm"><div class="body" style="text-align:'
		        + options.textAlign
		        + '">'
		        + options.msg
		        + '</div><div class="op"><a href="'
		        + options.acceptLink
		        + '" class="ac_popAccept">'
		        + options.okTxt
		        + '</a><a href="javascript:void(0);" class="ac_popCancel">'
		        + options.cancTxt + '</a></div></div>';
		$('body').append(popHtml);
		$('.pop .ac_popCancel').click(function() {
			$('.pop, .mask').remove();
			options.onCancel();
		});
		$('.pop .ac_popAccept').click(function() {
			$('.pop, .mask').remove();
			options.onAccept();
		});
	}
	window.abConfirm = abConfirm;

	// 确定弹出层
	function abConfirmColor(options) {
		options = options || {};
		var defaultOptions = {
		    msg : abTips.msg_waiting,
		    okTxt : abTips.ok,
		    cancTxt : abTips.cancel,
		    textAlign : 'center',
		    onAccept : function() {
		    },
		    acceptLink : 'javascript:void(0)',
		    onCancel : function() {
		    }
		};
		options = $.extend({}, defaultOptions, options);
		var popHtml = '<div class="mask"></div><div class="pop pop_confirm"><div class="body" style="text-align:'
		        + options.textAlign
		        + '">'
		        + options.msg
		        + '</div><div class="op"><a href="'
		        + options.acceptLink
		        + '" class="ac_popAccept">'
		        + options.okTxt
		        + '</a><a href="javascript:void(0);" style="background-color: #2cbae7;color:white" class="ac_popCancel">'
		        + options.cancTxt + '</a></div></div>';
		$('body').append(popHtml);
		$('.pop .ac_popCancel').click(function() {
			$('.pop, .mask').remove();
			options.onCancel();
		});
		$('.pop .ac_popAccept').click(function() {
			$('.pop, .mask').remove();
			options.onAccept();
		});
	}
	window.abConfirmColor = abConfirmColor;

	// alert弹出 一个按钮
	function abAlert(options) {
		options = options || {};
		var defaultOptions = {
		    title : abTips.msg_title,
		    msg : abTips.msg_op_success,
		    okTxt : abTips.msg_ok_know,
		    acceptLink : 'javascript:void(0);',
		    onAccept : function() {
		    }
		}
		options = $.extend({}, defaultOptions, options);
		var popHtml = $('<div class="mask" style="z-index:9998;"></div><div class="pop pop_alert" style="z-index:9999;"><p class="title">'
		        + options.title
		        + '</p><div class="body">'
		        + options.msg
		        + '</div><a href="'
		        + options.acceptLink
		        + '" class="btnOk ac_popAccept">'
		        + options.okTxt
		        + '</a></div>');
		$('body').append(popHtml);
		popHtml.find('.ac_popAccept').click(function() {
			popHtml.remove();
			options.onAccept();
		});
	}
	window.abAlert = abAlert;

	// 没有button的情况下，为form添加提交事件
	$('.ac_submit').click(function() {
		$(this).closest('form').submit();
	});

	// 再次获取短信验证码
	function clickGetSms(element, time) {
		var $this = element;
		var timeGetAgain = null;
		$this.addClass('btn_gray').unbind('click');
		$this.html(time);
		function getAgain() {
			if (parseInt($this.html()) > 0) {
				$this.html($this.html() - 1);
				timeGetAgain = setTimeout(getAgain, 1000);
			} else {
				clearTimeout(timeGetAgain);
				$this.removeClass('btn_gray').html(abTips.sms_obtain);
				$('.ac_getsms').bind('click', function() {
					clickGetSms($(this), time);
				});
				$('.ac_submit').click(function() {
					$(this).closest('form').submit();
				});
			}
		}
		timeGetAgain = setTimeout(getAgain, 1000);
	}
	window.clickGetSms = clickGetSms;

	// 按钮定时计数器
	function btnClickTime(element, time) {
		var $this = element;
		var timeGetAgain = null;
		$this.addClass('btn_gray').unbind('click');
		$this.html(time);
		function getAgain() {
			if (parseInt($this.html()) > 0) {
				$this.html($this.html() - 1);
				timeGetAgain = setTimeout(getAgain, 1000);
			} else {
				clearTimeout(timeGetAgain);
				$this.removeClass('btn_gray').html(abTips.sms_obtain);
				$('.ac_submit').click(function() {
					$(this).closest('form').submit();
				});
			}
		}
		timeGetAgain = setTimeout(getAgain, 1000);
	}
	window.btnClickTime = btnClickTime;

	// $('.ac_getsms').bind('click',function(){
	// if($('input[name=PhoneNum]').length &&
	// $('input[name=PhoneNum]').val()!=''){
	// clickGetSms($(this));
	// }else{
	// abConfirm({
	// msg:'请输入手机号',//提示的文案
	// okTxt:'确定',//确定按钮的文案
	// cancTxt:'取消'//取消按钮的文案
	// });
	// }
	// });

	// 返回上一页
	$('.ac_back').click(function() {
		history.go(-1);
	});

	// 置顶错误提示调用
	var $errTop = $('.errTop');
	function getTopErr(msg) {
		$errTop.find('.errBody').html(msg).end().show();
		$('body').css({
			'padding-top' : $errTop.height() + 44
		});
	}
	// 关闭顶部错误提示
	$errTop.find('.close').click(function() {
		$(this).closest('.errTop').hide().find('.errBody').html('');
		$('body').css({
			'padding-top' : 44
		});
	});
	window.getTopErr = getTopErr;

	function closeTopErr() {
		$errTop.hide().find('.errBody').html('');
		$('body').css({
			'padding-top' : 44
		});
	}
	window.closeTopErr = closeTopErr;
	// pop_login
	var $pop_login = $('.pop_login');
	var $pop_login_close = $pop_login.find('.close');
	$('.ac_login').click(function() {
		$('<div class="mask"></div>').insertBefore($pop_login);
		$pop_login.show();
		return false;
	});
	$pop_login_close.click(function() {
		$('.mask').remove();
		$pop_login.hide();
	});

	// 调取银行卡支持弹出层
	var $pop_bank = $('.pop_bank');
	function showPopBank() {
		$('<div class="mask"></div>').insertBefore($pop_bank);
		$pop_bank.show();
		$pop_bank.find('.close').click(function() {
			hidePopBank();
		});
		return false;
	}
	function hidePopBank() {
		$pop_bank.hide();
		$('.mask').remove();
	}
	window.showPopBank = showPopBank;

	// alert扩展弹框 一个按钮，一个其他方式
	function abExAlert(options) {
		options = options || {};
		var defaultOptions = {
		    msg : abTips.msg_waiting,
		    okTxt : abTips.ok,
		    cancTxt : abTips.cancel,
		    onAccept : function() {
		    },
		    acceptLink : 'javascript:void(0)',
		    onCancel : function() {
		    }
		};
		options = $.extend({}, defaultOptions, options);
		var popHtml = '<div class="mask" style="z-index:9998;"></div><div style="z-index:9999;" class="pop pop_exalert"><p class="title">'
		        + options.msg
		        + '</p><a href="javascript:void(0);" class="main btn_block marginT20 card_submit ac_popAccept">'
		        + options.okTxt
		        + '</a><a class="btnOk ac_popCancel" href="javascript:void(0);">'
		        + options.cancTxt + '</a></div>';
		$('body').append(popHtml);
		$('.pop .ac_popCancel').click(function() {
			$('.pop, .mask').remove();
			options.onCancel();
		});
		$('.pop .ac_popAccept').click(function() {
			$('.pop, .mask').remove();
			options.onAccept();
		});
	}
	window.abExAlert = abExAlert;

});
if (jQuery.validator) {
	jQuery.validator.addMethod("isChinese", function(value, element) {
		var str = /^[\u4E00-\u9FA5]+$/;
		return this.optional(element) || (str.test(value));
	}, abTips.valid_zh);

	jQuery.validator.addMethod("isMobile", function(value, element) {
		var length = value.length;
		var mobile = /^1\d{10}$/;
		return this.optional(element) || (length == 11 && mobile.test(value));
	}, abTips.valid_phone);

	jQuery.validator.addMethod("isCardNo", function(value, element) {
		var str = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		return this.optional(element) || (str.test(value));
	}, abTips.valid_id_card);

}

// 倒计时跳转
function timeJump(time, url, item) {
	var time_n = time;
	function timeDs() {
		if (time_n > 0) {
			time_n -= 1;
			item.html(time_n);
			setTimeout(timeDs, 1000);
		} else {
			window.location.href = url;
		}
	}
	setTimeout(timeDs, 1000);
}

// 弹出层输入手机校验码
function abMobileVerifyCode(options) {
	options = options || {};
	var defaultOptions = {
	    msg : abTips.msg_waiting,
	    okTxt : abTips.ok,
	    cancTxt : abTips.cancel,
	    textAlign : 'center',
	    onAccept : function() {
	    },
	    acceptLink : 'javascript:void(0)',
	    onCancel : function() {
	    }
	};
	options = $.extend({}, defaultOptions, options);
	var pophtml = '<div class="mask"></div><div class="pop pop_confirm"><div class="aligncenter size13 margin10">'
	        + options.smschecktip
	        + '</div>'
	        + '<div style="color: #e72356;font-size: 12px;line-height: 30px;padding: 5px 10px;"><span id="v_errmsg"></span></div>'
	        + '<div class="main enterList marginT20"><dl class="clearfix"><dt>'
	        + abTips.verify_code
	        + '</dt><dd class="phone_vc clearfix">'
	        + '<div class="txt-normal"><input id="v_verifyCode" type="text" placeholder="'
	        + abTips.verify_sms_code
	        + '" /></div>'
	        + '</dd></dl></div><div class="main margin20"><a href="#" class="btn_block" id="v_comfirm">'
	        + abTips.verify_comfirm
	        + '</a>'
	        + '<p class="alignright"><a href="#" class="blue" id="v_cancel">'
	        + abTips.verify_didnt_code + '</a></p></div></div>';
	$('body').append(pophtml);
	$('#v_comfirm').click(function() {
		var flag = options.onAccept();
		if (flag == true) {
			$('.pop, .mask').remove();
		}
	});
	$('#v_cancel').click(function() {
		options.onCancel();
		$('.pop, .mask').remove();
	});
}
window.abMobileVerifyCode = abMobileVerifyCode;

function alertMsg(options) {
	options = options || {};
	var defaultOptions = {
	    msg : '',
	    butTxt : '',
	    footerTxt : '',
	    textAlign : 'center',
	    onButton : function() {
	    },
	    acceptLink : 'javascript:void(0)',
	    onFooter : function() {
	    },
	    onClose : function() {

	    }
	};
	options = $.extend({}, defaultOptions, options);
	var popHtml = $('<div class="mask" style="z-index:9998;"></div><div class="pop pop_alertMsg" style="z-index:9999;"><a href="'
	        + options.acceptLink
	        + '" class="close ac_Close">'
	        + '</a>'
	        + '<div class="bodyMsg">'
	        + options.msg
	        + '</div>'
	        + '<a href="javascript:void(0);" class="ac_popBut">'
	        + options.butTxt
	        + '</a><p class="ac_popFooter"> '
	        + options.footerTxt + '</p></div>');
	$('body').append(popHtml);
	var $pop_alert = $('.pop_alertMsg');
	$pop_alert.css({
		'margin-top' : -$pop_alert.height() / 2
	});
	$('.pop .ac_popBut').click(function() {
		$('.pop, .mask').remove();
		options.onButton();
	});
	$('.pop .ac_Close').click(function() {
		$('.pop, .mask').remove();
		options.onClose();
	});
	$('.pop .callWX').click(function() {
		options.onFooter();
	});
}
window.alertMsg = alertMsg;