/**
 * <pre>
 * 支付方式链接data定义: enName英文名typeId方式idcheckVCoin是否检查虚拟币余额discount溢价信息discountNotify是否有溢价提示rechargeRate充值手续费rechargeNotify是否有充值手续费提示payUrl支付链接rechargeUrl充值链接
 * </pre>
 */

$(function() {
	$(".topay").click(function() {
		var target = $(this);
		var id = getData(target, 'typeId');
		var discount = getData(target, 'discount');
		var checkVCoin = getData(target, 'checkVCoin');
		if (checkVCoin == "true" && !hasVCoin(discount)) { // 如果虚拟币不足
			return false;
		}
		var discountNotify = getData(target, 'discountNotify');
		var payLink = genPayUrl(getData(target, 'payUrl'), id);
		if (discountNotify == "true" && !callDiscountTip(target, discount, payLink, id)) {
			return false;
		}
		payTypeProcess(target, payLink, id);
		return false;
	});

	function payTypeProcess(target, payLink, id) {
		if (payLink.indexOf('json') > 0) {
			var ignoreLast = $("#IgnoreLast").val();
			if (ignoreLast == "1") {
				payLink = payLink + "&IgnoreLast=1"
			}
			$.ajax({
				url : payLink,
				type : 'get',
				cache : false,
				dataType : 'json',
				success : function(ret) {
					var options = {};
					options.queryUrl = $QueryPayLink;
					options.begSessUrl = $BegLink;
					if (id == 403 && !isWeixinBrowse()) {
						options.success = invkWx;
					} else if (id == 403 && isIfr()) {
						options.success = invkWxQrcode;
					} else if (id == 401 && isWeixinBrowse()) {
						options.success = invkWxAlipay;
					} else {
						options.success = queryCallback1;
					}
					options.continuePayUrl = payLink;
					options.typeId = id;
					target.payBizOp(options, ret);
				}
			});
		} else {
			window.location.href = payLink;
		}
	}

	var queryCallback1 = function(ret) {
		var payparam = JSON.parse(ret.Body.PayOrderInfo.PayParam);
		invPayment(payparam);
	}

	/**
	 * @param discount
	 *            溢价信息
	 * @globalParam $VCoin
	 */
	function hasVCoin(discount) {
		var finalFee = getFinalFee(discount);
		if (finalFee > $VCoin) {
			abConfirm({
				msg : abTips.vcoin_not_enough,
				okTxt : abTips.goto_recharge,
				acceptLink : $RechrLink
			});

			return false;
		}
		return true;
	}

	function callDiscountTip(target, discount, payLink, id) {
		var finalFee = getFinalFee(discount);
		if (finalFee > $PayFee) {
			var tip = abTips.discountTip(getYuan(finalFee / 100), getYuan((finalFee - $PayFee) / 100));
			abConfirm({
				msg : tip,
				okTxt : abTips.change_paytype,
				cancTxt : abTips.pay_continue,
				onCancel : function() {
					payTypeProcess(target, payLink, id);
				}
			});
			return false;
		}
		return true;
	}

	/**
	 * 生成支付方式链接
	 * 
	 * @param payTypeId
	 */
	function genPayUrl(url, typeId) {
		return genUrl(url, {
			PayType : typeId,
			FeeID : $FeeId
		});
	}

	/**
	 * @globalParam $PayFee
	 */
	function getFinalFee(discount) {
		return Math.floor($PayFee * discount / 100);
	}
});

/**
 * 获取节点属性
 */
function getData(_jq, attrName) {
	return _jq.attr("data-" + attrName);
}

/**
 * 获取以元为单位的金额
 * 
 * @param NumberValue
 */
function getYuan(value) {
	return parseFloat(value).toFixed(2);
}

function replacePayUrltoJson(payUrl) {
	return payUrl.replace(/pay/, "pay.json");
}

function doGet(payParam) {
	window.location.replace(payParam.url);
}

function submitPost(args) {
	var payParam = new Object();
	if (!(args instanceof Object)) {
		payParam.url = arguments[0];
		payParam.params = arguments[1];
	} else {
		payParam = args;
	}
	var form = $('<form></form>');
	// 设置表单属性
	form.attr('action', payParam.url);
	form.attr('method', 'post');
	form.attr('target', '_self');
	// 创建原始
	var params = payParam.params;
	for (p in params) {
		var t_hidden = $('<input type="hidden"/>');
		t_hidden.attr('name', p);
		t_hidden.attr('value', params[p]);
		form.append(t_hidden);
	}
	form.appendTo("body")
	form.css('display', 'none')
	form.submit();
	return false;
}

function invPayment(payParam) {
	var type = payParam.type;
	if (type == 'POST') {
		submitPost(payParam);
	} else {
		doGet(payParam);
	}
}

function invkWx(ret) {
	var payparam = ret.Body.PayOrderInfo.PayParam;
	var wxurl = JSON.parse(payparam).url;
	var payOrderId = ret.Body.PayOrderInfo.OrderID;
	var r = /^weixin:/i;
	var httpR = /^http/i;
	var wx154R = /wx.tenpay.com/i;
	if (r.test(wxurl)) {
		wxAbConfirm(wxurl, payOrderId)
		if ('qh360' == isHrefOpenWx() && isIOS()) {
			$('#wxifr').attr('src', wxurl);
		} else {
			top.location.href = wxurl;
		}
	} else if (httpR.test(wxurl)) {
		if (!isIfr() && wx154R.test(wxurl)) {
			var url = "/h5/wxh5;" + $BegLink.split(";")[1] + "&OrderID=" + ret.Body.PayOrderInfo.OrderID + "&openSafari=" + (isHrefOpenWx() == "safari") + "&r=" + encodeURIComponent(payparam);
			window.location.href = url;
		} else {
			setTimeout(function() {
				$('#wxifr').attr('src', "");
			}, 4500);
			$('#wxifr').attr('src', wxurl);
		}
	} else {
		wxAbConfirm(wxurl, payOrderId)
		$('#wxifr').attr('src', wxurl);
	}
};

function wxAbConfirm(wxurl, payOrderId) {
	var wxConfirm = function() {
		$.unmask();
		alertMsg({
			msg : abTips.wx_get_result,
			butTxt : abTips.wx_get_right_now,
			footerTxt : '无法打开微信支付？<span class="callWX" style="color: #0c6cc5;text-decoration:underline;">点此尝试</span>',
			onButton : function() {
				// 已支付并查询
				redirect(genUrl($QueryPayLink, {
					OrderID : payOrderId
				}));
			},
			onFooter : function() {
				// 手动打开微信支付方式
				$('#wxifr').attr('src', "");
				window.open(wxurl);
			},
			onClose : function() {
				// 避免加载缓慢，因此延迟5s执行
				setTimeout(function() {
					$('#wxifr').attr('src', "");
				}, 5000);
			}
		});
	}
	$.mask({
		tip : abTips.loading
	});
	setTimeout(wxConfirm, 1000);
}

function invkWxQrcode(ret) {
	window.location.href = ret.Body.WxQrCodeUrl;
}
function invkWxAlipay(ret) {
	var payparam = ret.Body.PayOrderInfo.PayParam;
	var aliurl = JSON.parse(payparam).url;
	var url = "/h5/wxalipay;" + $BegLink.split(";")[1] + "&OrderID=" + ret.Body.PayOrderInfo.OrderID + "&r=" + encodeURIComponent(encodeURIComponent(payparam));
	window.location.href = url;
}