var absettings = {
	currency_unit : '\u5143',
	vcoin_name : '\u7231\u8d1d\u5e01',
	vcoin_unit : '\u5e01'
}
var errMsg = {

}
var abTips = {

	// msg
	ok : '\u786e\u5b9a',
	cancel : '\u53d6\u6d88',
	yes : '\u662f',
	no : '\u5426',
	msg_waiting : '\u8bf7\u7a0d\u5019...',
	msg_title : '\u63d0\u793a\u4fe1\u606f',
	msg_op_success : '\u64cd\u4f5c\u6210\u529f',
	msg_ok_know : '\u6211\u77e5\u9053\u4e86',
	loading : '\u6b63\u5728\u52a0\u8f7d...',
	request_error : '\u8bf7\u6c42\u53d1\u751f\u9519\u8bef!',
	input_again : '\u91cd\u65b0\u8f93\u5165',
	system_error : '\u7cfb\u7edf\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5',

	// sms
	sms_obtain : '\u83b7\u53d6\u9a8c\u8bc1\u7801',
	sms_required : '\u8bf7\u5148\u83b7\u53d6\u77ed\u4fe1\u9a8c\u8bc1\u7801',
	sms_input : '\u8bf7\u8f93\u5165\u77ed\u4fe1\u9a8c\u8bc1\u7801',
	sms_len : function(len) {
		return '\u8bf7\u8f93\u5165' + len + '\u4f4d\u9a8c\u8bc1\u7801';
	},

	// abExAlert
	exalert_msg : '\u652f\u4ed8\u6210\u529f\u540e\uff0c\u8bf7\u70b9\u51fb\u5b8c\u6210\u6309\u94ae\u9886\u53d6\u5546\u54c1',
	exalert_oktxt : '\u652f\u4ed8\u5b8c\u6210',
	exalert_canctxt : '\u652f\u4ed8\u9047\u5230\u95ee\u9898',
	// valid
	valid_zh : '\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u4e2d\u6587\u683c\u5f0f',
	valid_phone : '\u8bf7\u8f93\u5165\u624b\u673a\u53f7',
	valid_cardno : '\u8bf7\u8f93\u5165\u5361\u53f7',
	valid_card_username : '\u8bf7\u8f93\u5165\u6301\u5361\u4eba\u59d3\u540d',
	valid_right_cardno : '\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u5361\u53f7',
	valid_cardno_maxlen : function(max) {
		return '\u5361\u53f7\u6700\u5927\u957f\u5ea6:' + max;
	},
	valid_cardno_minlen : function(min) {
		return '\u5361\u53f7\u6700\u5c0f\u957f\u5ea6:' + min;
	},
	valid_cardamount : '\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u9762\u989d',
	valid_cardamount_min : function(num) {
		return '\u6700\u5c0f\u652f\u6301\u9762\u989d\u4e3a' + num
				+ absettings.currency_unit + '.';
	},
	valid_cardamount_max : function(num) {
		return '\u6700\u5927\u652f\u6301\u9762\u989d\u4e3a' + num
				+ absettings.currency_unit + '.';
	},
	valid_cardpwd : '\u8bf7\u8f93\u5165\u5361\u5bc6\u7801',
	valid_numorlatter : '\u8bf7\u8f93\u5165\u6570\u5b57\u5b57\u6bcd\u7ec4\u5408',
	valid_cardpwd_maxlen : function(max) {
		return '\u5361\u5bc6\u7801\u6700\u5927\u957f\u5ea6:' + max;
	},
	valid_cardpwd_minlen : function(min) {
		return '\u5361\u5bc6\u7801\u6700\u5c0f\u957f\u5ea6:' + min;
	},
	valid_loginname : '\u8bf7\u8f93\u5165\u7528\u6237\u540d',
	valid_loginname_len : function(min, max) {
		return '\u8bf7\u8f93\u516511\u4f4d\u624b\u673a\u53f7\u6216 ' + min
				+ '-' + max + ' \u4f4d\u7528\u6237\u540d';
	},
	valid_loginpwd : function(min, max) {
		return '\u8bf7\u8f93\u5165' + min + '-' + max + '\u4f4d\u5bc6\u7801';
	},
	valid_pwd : '\u8bf7\u8f93\u5165\u5bc6\u7801',
	valid_old_pwd : '\u8bf7\u8f93\u5165\u539f\u652f\u4ed8\u5bc6\u7801',
	valid_confirm_pwd : '\u8bf7\u8f93\u5165\u786e\u8ba4\u5bc6\u7801',
	valid_pwd_len : function(len) {
		return '\u8bf7\u8f93\u5165' + len + '\u4f4d\u6570\u5b57\u5bc6\u7801';
	},
	valid_pwd_not_match : '\u4e24\u6b21\u8f93\u5165\u7684\u5bc6\u7801\u4e0d\u4e00\u81f4',
	valid_id_card : '\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u8eab\u4efd\u8bc1\u53f7\u7801',
	valid_credit_date : '\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u6709\u6548\u671f(MM/YY)',
	valid_security_num : function(num) {
		return '\u8bf7\u8f93\u5165' + num
				+ '\u4f4d\u6570\u5b57\u5b89\u5168\u7801';
	},
	valid_available_time : '\u8bf7\u8f93\u5165\u6709\u6548\u671f',
	valid_user_agree : '\u8bf7\u540c\u610f\u7528\u6237\u534f\u8bae\uff01',
	valid_isMobile : '\u8bf7\u8f93\u5165\u6b63\u786e\u624b\u673a\u53f7',
	valid_mobileLength : '\u8bf7\u8f93\u516511\u4f4d\u624b\u673a\u53f7',
	// pay
	pay_continue : '\u7ee7\u7eed\u652f\u4ed8',
	pay_query_result : '\u67e5\u8be2\u652f\u4ed8\u7ed3\u679c',
	pay_error : '\u652f\u4ed8\u5f02\u5e38',
	change_paytype : '\u66f4\u6362\u652f\u4ed8\u65b9\u5f0f',
	choice_other_paytype : '\u9009\u62e9\u5176\u4ed6\u652f\u4ed8\u65b9\u5f0f',
	ret_pay_or_rechar : '\u8fd4\u56de\u652f\u4ed8\u6216\u5145\u503c\u5217\u8868',
	pay_query_result_continue : '\u652f\u4ed8\u7ed3\u679c\u5904\u7406\u4e2d\uff0c\u7ee7\u7eed\u67e5\u8be2\u652f\u4ed8\u7ed3\u679c?',

	// recharge
	goto_recharge : '\u53bb\u5145\u503c',
	recharge_continue : '\u7ee7\u7eed\u5145\u503c',
	other_recharge_type : '\u5176\u4ed6\u5145\u503c\u65b9\u5f0f',

	// order
	order_processing : '\u8ba2\u5355\u5904\u7406\u4e2d!',
	order_process_request : '\u8be5\u5546\u54c1\u5df2\u7ecf\u4e0b\u5355\uff0c\u5982\u679c\u60a8\u5df2\u7ecf\u652f\u4ed8\u6210\u529f\uff0c\u8bf7\u67e5\u8be2\u652f\u4ed8\u7ed3\u679c\uff0c\u907f\u514d\u7531\u4e8e\u91cd\u590d\u652f\u4ed8\u9020\u6210\u60a8\u7684\u7ecf\u6d4e\u635f\u5931\u3002',
	order_process_request_type : '\u8bf7\u6839\u636e\u60a8\u652f\u4ed8\u7684\u60c5\u51b5\uff0c\u9009\u62e9\u4e0b\u9762\u7684\u6309\u94ae',

	//wx
	wx_get_result:'\u8bf7\u5728\u5fae\u4fe1\u5185\u5b8c\u6210\u652f\u4ed8',
	wx_get_right_now:'\u4ed8\u6b3e\u5df2\u5b8c\u6210',
	
	// card
	change_card : '\u66f4\u6362\u5361',
	edit_cardinfo : '\u4fee\u6539\u5361\u4fe1\u606f',
	// mbileVerifyCode
	verify_title : '\u672c\u6b21\u4ea4\u6613\u9700\u8981\u77ed\u4fe1\u786e\u8ba4\uff0c\u9a8c\u8bc1\u7801\u5df2\u53d1\u9001\u81f3\u624b\u673a',
	verify_oper_msg : '\u8bf7\u6309\u63d0\u793a\u64cd\u4f5c',
	verify_code : '\u9a8c\u8bc1\u7801',
	verify_sms_code : '6\u4f4d\u6570\u5b57\u77ed\u4fe1\u9a8c\u8bc1\u7801',
	verify_comfirm : '\u786e\u8ba4\u652f\u4ed8',
	verify_didnt_code : '\u6536\u4e0d\u5230\u9a8c\u8bc1\u7801\uff1f',
	sms_verify_tip : '\u8bf7\u8f93\u51656\u4f4d\u6570\u7684\u77ed\u4fe1\u9a8c\u8bc1\u7801\uff01',

	// vcoin
	vcoin_not_enough : absettings.vcoin_name
			+ '\u4f59\u989d\u4e0d\u8db3\uff0c\u8bf7\u5145\u503c\u540e\u652f\u4ed8\u3002',

	// wx tip
	wx_load : '\u82e5\u672a\u80fd\u81ea\u52a8\u6253\u5f00\u5fae\u4fe1\u652f\u4ed8\u65b9\u5f0f\u8bf7\u70b9\u51fb',
	wx_loading : '\u6b63\u5728\u8c03\u8d77\u5fae\u4fe1\u652f\u4ed8\u002e\u002e\u002e',
	wx_open_manual : '\u624b\u52a8\u6253\u5f00\u5fae\u4fe1\u652f\u4ed8\u65b9\u5f0f',
	wx_done_query_result : '\u5982\u5df2\u5b8c\u6210\u652f\u4ed8\u8bf7\u67e5\u8be2\u652f\u4ed8\u7ed3\u679c',
	wx_change_paytype : '\u66f4\u591a\u652f\u4ed8\u65b9\u5f0f\u0020\u0026\u0067\u0074\u003b',

	// tips
	discountTip : function(total, discount) {
		return ('\u60a8\u5171\u9700\u652f\u4ed8#total'
				+ absettings.currency_unit + ',\u5176\u4e2d#discount'
				+ absettings.currency_unit + '\u4e3a\u5546\u5bb6\u624b\u7eed\u8d39\u3002')
				.replace('#total', total).replace('#discount', discount);
	},
	rechargeTip : function(total, discount, recharge) {
		return ('\u60a8\u5171\u5145\u503c#total'
				+ absettings.currency_unit
				+ ',\u5176\u4e2d#discount'
				+ absettings.currency_unit
				+ '\u4e3a\u5546\u5bb6\u624b\u7eed\u8d39\uff0c\u5176\u4f59#recharge'
				+ absettings.currency_unit + '\u5b58\u5165'
				+ absettings.vcoin_name + '\u4f59\u989d').replace('#total',
				total).replace('#discount', discount).replace('#recharge',
				recharge);
	},
	noRecharTip : function(total, price, ab) {
		return ('\u60a8\u652f\u4ed8#total'
				+ absettings.currency_unit
				+ '\uff0c\u5176\u4e2d\u8d2d\u4e70\u5546\u54c1\u4ef7\u683c#price'
				+ absettings.currency_unit + '\uff0c\u5b58\u5165'
				+ absettings.vcoin_name + '#ab' + absettings.currency_unit
				+ '(#coin' + absettings.vcoin_unit + ')').replace('#total',
				total).replace('#price', price).replace('#ab', ab).replace(
				'#coin', (ab * 10).toFixed(1));
	},
	noRecharTipError : function() {
		return '\u60a8\u9009\u62e9\u7684\u5361\u9762\u989d\u5c0f\u4e8e\u8ba2\u5355\u91d1\u989d\uff0c\u60a8\u53ef\u4ee5\u5148\u5145\u503c\u540e\u652f\u4ed8\uff0c\u6216\u9009\u62e9\u5176\u4ed6\u9762\u989d\u7684\u5361\u652f\u4ed8';
	},
	recharTip : function(total, price, pay_rechar_fee, ab) {
		return ('\u60a8\u652f\u4ed8#total' + absettings.currency_unit
				+ '\uff0c\u5176\u4e2d\u5546\u54c1\u4ef7\u683c#price'
				+ absettings.currency_unit
				+ '\uff0c\u624b\u7eed\u8d39\u4e3a#pay_rechar_fee'
				+ absettings.currency_unit + '\uff0c\u5b58\u5165'
				+ absettings.vcoin_name + '#ab' + absettings.currency_unit
				+ '(#coin' + absettings.vcoin_unit + ')').replace('#total',
				total).replace('#price', price).replace('#pay_rechar_fee',
				pay_rechar_fee).replace('#ab', ab).replace('#coin',
				(ab * 10).toFixed(1));
	},
	recharTipError : function(recharfee) {
		return ('\u60a8\u9009\u62e9\u7684\u5361\u9762\u989d\u5c0f\u4e8e\u8ba2\u5355\u91d1\u989d\uff0c\u60a8\u53ef\u4ee5\u5148\u5145\u503c\u540e\u652f\u4ed8(\u5145\u503c\u624b\u7eed\u8d39\u4e3a#rechar_fee'
				+ absettings.currency_unit + ')\uff0c\u6216\u9009\u62e9\u5176\u4ed6\u9762\u989d\u7684\u5361\u652f\u4ed8')
				.replace('#rechar_fee', recharfee);
	}
};