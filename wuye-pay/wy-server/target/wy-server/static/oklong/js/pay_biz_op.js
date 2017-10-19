$(function() {

	/**
	 * <pre>
	 * options:
	 * queryUrl:支付查询链接&lt;br/&gt;
	 * continuePayUrl:继续支付的链接&lt;br/&gt;
	 * begSessUrl:begSession链接
	 * success: 成功callback
	 * </pre>
	 */
	$
			.extend(
					$.fn,
					{
						payBizOp : function(options, ret) {
							var target = this;
							var nodeName = target[0].nodeName;
							var errMsg = ret.ErrMsg;
							var payOrderId = ret.Body && ret.Body.PayOrderInfo ? ret.Body.PayOrderInfo.OrderID
									: '';
							if (ret.RetCode == 0) {
								$("#IgnoreLast").val("0")
								if (typeof (options.success) == 'function') {
									if (options.typeId == 403
											&& !isWeixinBrowse()) {
										options.success(ret);
										return;
									} else {
										$.mask({
											tip : abTips.loading
										});
										options.success(ret);
									}
								} else {
									redirect(genUrl(options.queryUrl, {
										OrderID : payOrderId
									}));
								}
								return;
							} else if (ret.RetCode == AUTH_TOKEN_EXPIRE) {
								abAlert({
									title : abTips.pay_error,
									msg : errMsg,
									okTxt : abTips.ok
								});
								$.unmask();
								return;
							}
							$.unmask();
							if (!ret.Body || !ret.Body.bizErrorCode) {
								abAlert({
									title : abTips.pay_error,
									msg : errMsg,
									okTxt : abTips.ok
								});
								return;
							}
							var bizCode = ret.Body.bizErrorCode;
							switch (bizCode) {
							case 'paypwd_error':
								abAlert({
									title : abTips.pay_error,
									msg : errMsg,
									okTxt : abTips.ok
								});
								break;
							case 'order_paying':
								abConfirmColor({
									msg : abTips.order_process_request,
									okTxt : abTips.pay_continue,
									cancTxt : abTips.pay_query_result,
									onAccept : function() {
										var target = $(this);
										$("#IgnoreLast").val('1');
										if (options.typeId == 403) {
											wxContinuePay(target,
													options.continuePayUrl,
													options.typeId,
													options.success);
										} else {
											if (nodeName == 'FORM') {
												$("#IgnoreLast").val('1');
												$('#cardInfo').submit();
											} else {
												wxContinuePay(target,
														options.continuePayUrl,
														options.typeId,
														options.success);
											}
											/*
											 * if (nodeName == 'FORM') {
											 * $("#IgnoreLast").val('1');
											 * target.submit(); } else {
											 * redirect(options.continuePayUrl); }
											 */
										}
									},
									onCancel : function() {
										redirect(genUrl(options.queryUrl, {
											OrderID : payOrderId
										}));
									}
								});
								break;
							case 'order_payed':
								abAlert({
									title : abTips.pay_error,
									msg : errMsg,
									okTxt : abTips.pay_query_result,
									onAccept : function() {
										redirect(genUrl(options.queryUrl, {
											OrderID : payOrderId
										}));
									}
								});
								break;
							case 'balance_not_enough':
								abAlert({
									msg : errMsg,
									okTxt : abTips.ok,
									onAccept : function() {
										redirect(options.begSessUrl);
									}
								});
								break;
							case 'bindcard_error':
							case 'card_error':
								abConfirm({
									msg : errMsg,
									okTxt : abTips.edit_cardinfo,
									cancTxt : abTips.choice_other_paytype,
									onCancel : function() {
										redirect(options.begSessUrl);
									}
								});
								break;
							}
						}
					});
});

function redirect(url) {
	window.location.replace(url);
}

var queryCallback = function(ret) {
	var delayTime = 500, continue_query = true, that = this;
	var fixDelayTime = 2000;
	var startTime = 0, endTime = 0;
	var orderId = (ret.Body && ret.Body.PayOrderInfo && ret.Body.PayOrderInfo.OrderID)
			|| '';
	var queryLink = genUrl(this.queryUrl, {
		OrderID : orderId
	});
	var query = function() {
		if (continue_query) {
			$.ajax({
				url : $AJX_QueryResultLink,
				type : 'POST',
				data : {
					OrderID : orderId,
					act : 'P'
				},
				beforeSend : function() {
					startTime = new Date().getTime();
				},
				dataType : 'json',
				success : function(data) {
					endTime = new Date().getTime();
					if (data.RetCode != 0) {
						var bizCode = data.Body && data.Body.bizErrorCode;
						if (bizCode === 'order_paying') {
							var intervalTime = endTime - startTime;
							setTimeout(query, intervalTime > fixDelayTime ? 0
									: (fixDelayTime - intervalTime));
						} else {
							abConfirm({
								msg : data.ErrMsg,
								okTxt : abTips.edit_cardinfo,
								cancTxt : abTips.choice_other_paytype,
								onCancel : function() {
									redirect($BegLink);
								}
							});
							/*
							 * abAlert({ msg : data.ErrMsg, okTxt : abTips.ok,
							 * });
							 */
							$.unmask();
							continue_query = false;
							return;
						}
					} else {
						$.unmask();
						continue_query = false;
						redirect(queryLink);
					}
				},
				complete : function() {
				}
			});
		}
	};
	setTimeout(query, delayTime);
}

function wxContinuePay(target, payLink, id, queryCallback1) {
	var ignoreLast = $("#IgnoreLast").val();
	if (ignoreLast == "1" && payLink.indexOf("&IgnoreLast") == -1) {
		payLink = payLink + "&IgnoreLast=1"
	}
	if (payLink.indexOf("json") == -1) {
		window.location.replace(payLink);
		return;
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
			options.success = queryCallback1;
			options.continuePayUrl = payLink;
			options.typeId = id;
			target.payBizOp(options, ret);
		}
	});

}