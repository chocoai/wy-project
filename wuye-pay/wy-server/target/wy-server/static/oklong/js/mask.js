$(function() {
	var maskOptions = {
		maskId : "winMask",
		textColor : "#eee",
		textFontSize : 16,
		tip : ''
	};

	$.mask = function(options) {

		var settings = $.extend({}, maskOptions, options);
		$.unmask();
		var maskHtml = '<div style="position:absolute; left:0; top:0; right:0; bottom:0; z-index:99999;" id="'
				+ settings.maskId
				+ '"><div class="mask"></div><div style="text-align:center; height:30px; line-height:30px; position:absolute; padding-right:20px; left:0; right:0; top:50%; margin-top:-15px; z-index:9;"><span class="loading">&nbsp;</span><span style="padding-left:10px; color:'
				+ settings.textColor
				+ '; font-size:'
				+ settings.textFontSize
				+ 'px;">' + settings.tip + '</span></div></div>';
		$('body').append(maskHtml);
	}

	$.unmask = function(options) {
		var settings = $.extend({}, maskOptions, options);
		$("#" + settings.maskId).remove();
	}

});