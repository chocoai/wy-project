function IframePay() {
	var baseRandom = (Math.random() + "").replace(".", "");
	var baseZIndex = 0;

	function createIfrPop() {
		var _ifrPop = document.createElement('div');
		_ifrPop.id = '_ifrPop' + baseRandom;
		_ifrPop.style.width = '100%';
		_ifrPop.style.height = '100%';
		_ifrPop.style.position = 'fixed';
		_ifrPop.style.zIndex = baseZIndex + 9999;
		_ifrPop.style.overflow = 'hidden';
		_ifrPop.style.left = '0';
		_ifrPop.style.top = '0';
		try {
			_ifrPop.style["-webkit-overflow-scrolling"] = "touch";
			_ifrPop.style["overflow-y"] = "scroll";
		} catch (e) {
		}
		document.getElementsByTagName('body').item(0).appendChild(_ifrPop);
	}

	function createIframePay(ifrmSrc) {
		var oScript = document.createElement("iframe");
		oScript.id = '_aibeiFrame' + baseRandom;
		oScript.style.zIndex = "9999";
		oScript.style.backgroundColor = '#fff';
		oScript.frameBorder = '0';
		oScript.width = "100%";
		oScript.height = '100%';
		oScript.src = ifrmSrc;
		E("_ifrPop" + baseRandom).appendChild(oScript);

	}

	function createifrClose(closeTxt) {
		var tmcloseTxt;
		if (closeTxt == undefined) {
			tmcloseTxt = "返回收银台";
		} else {
			tmcloseTxt = closeTxt;
		}
		var _ifrClose = document.createElement('div');
		_ifrClose.id = '_ifrCloseTip';
		_ifrClose.style.width = '70px';
		_ifrClose.style.height = '30px';
		_ifrClose.style.lineHeight = '30px';
		_ifrClose.style.textAlign = 'center';
		_ifrClose.style.fontSize = '12px';
		_ifrClose.style.color = '#fff';
		_ifrClose.style.backgroundColor = 'rgba(0,0,0,0.4)';
		_ifrClose.style.position = 'absolute';
		_ifrClose.style.borderRadius = '2px';
		_ifrClose.style.right = '20px';
		_ifrClose.style.bottom = '40px';
		_ifrClose.onclick = function() {
			closeCallback();
		}
		_ifrClose.style.fontFamily = 'Microsoft YaHei';
		_ifrClose.appendChild(document.createTextNode(tmcloseTxt));
		E("_ifrPop" + baseRandom).appendChild(_ifrClose);
	}

	// 创建遮盖原始页面层
	function createIfrCover() {
		var _ifrCover = document.createElement('div');
		// _ifrCover.style.display = 'none';
		_ifrCover.id = '_ifrCover' + baseRandom;
		_ifrCover.style.position = 'fixed';
		_ifrCover.style.top = 0;
		_ifrCover.style.left = 0;
		_ifrCover.style.width = '100%';
		_ifrCover.style.height = '100%';
		_ifrCover.style.backgroundColor = '#ffffff';
		_ifrCover.style.zIndex = baseZIndex + 8888;
		var first = document.body.firstChild; // 得到第一个元素
		document.body.insertBefore(_ifrCover, first); // 在第原来的第一个元素之前插入
	}

	// 跟id获取元素
	function E(id) {
		return document.getElementById(id);
	}

	var maskOptions = {
		maskId : "ifr_Mask" + baseRandom,
		textColor : "#eee",
		textFontSize : 16,
		tip : ''
	};

	// ifr_mask({tip : 'loading'});
	function ifr_mask(options) {
		var settings = extendOptions(options);

		var maskHtml = '<div class="mask"></div><div style="text-align:center; height:30px; line-height:30px; position:absolute; padding-right:20px; left:0; right:0; top:50%; margin-top:-15px; z-index:9;"><span class="loading">&nbsp;</span><span style="padding-left:10px; color:'
				+ settings.textColor
				+ '; font-size:'
				+ settings.textFontSize
				+ 'px;">' + settings.tip + '</span></div>';
		var _ifrMask = document.createElement('div');
		_ifrMask.id = settings.maskId;
		_ifrMask.style.position = 'fixed';
		_ifrMask.style.left = '0';
		_ifrMask.style.top = '0';
		_ifrMask.style.right = '0';
		_ifrMask.style.bottom = '0';
		_ifrMask.style.zIndex = baseZIndex + 99999;
		_ifrMask.innerHTML = maskHtml;
		document.getElementsByTagName('body').item(0).appendChild(_ifrMask);

	}

	// ifr_unmask({});
	function ifr_unmask(options) {
		var settings = extendOptions(options);
		removeElement(E("ifr_Mask" + baseRandom));
	}

	function isExist(id) {
		var tmDoc = E(id);
		if (tmDoc == undefined) {
			return false;
		} else {
			return true;
		}
	}

	function extendOptions(options) {
		if (options == undefined)
			return maskOptions;
		var settings = maskOptions;
		if (options.maskId != undefined) {
			settings.maskId = options.maskId;
		}
		if (options.textColor != undefined) {
			settings.textColor = options.textColor;
		}
		if (options.textFontSize != undefined) {
			settings.textFontSize = options.textFontSize;
		}
		if (options.tip != undefined) {
			settings.tip = options.tip;
		}
		return settings;
	}

	function closeCallback() {
		try {
			ifr_mask({
				tip : 'loading'
			});
			removeElement(E("_ifrCover" + baseRandom));
			removeElement(E("_ifrPop" + baseRandom));
			ifr_unmask({});
		} catch (e) {
			removeElement(E("_ifrCover" + baseRandom));
			removeElement(E("_ifrPop" + baseRandom));
			ifr_unmask({});
		}
		if ($CpUrl != undefined && $CpUrl != '') {
			window.location.href = $CpUrl;
		}
	}

	function jsonp(options) {
		options = options || {};
		if (!options.url || !options.callback) {
			throw new Error("参数不合法");
		}

		// 创建 script 标签并加入到页面中
		var callbackName = ('jsonp_' + Math.random()).replace(".", "");
		var oHead = document.getElementsByTagName('head')[0];
		options.data[options.callback] = callbackName;
		var params = formatParams(options.data);
		var oS = document.createElement('script');
		oHead.appendChild(oS);

		// 创建jsonp回调函数
		window[callbackName] = function(json) {
			oHead.removeChild(oS);
			clearTimeout(oS.timer);
			window[callbackName] = null;
			options.success && options.success(json);
		};

		// 发送请求
		if (options.url.indexOf("\?") != -1) {
			oS.src = options.url + '&' + params;
		} else {
			oS.src = options.url + '?' + params;
		}

		// 超时处理
		if (options.time) {
			oS.timer = setTimeout(function() {
				window[callbackName] = null;
				oHead.removeChild(oS);
				options.fail && options.fail({
					message : "超时"
				});
			}, time);
		}
	}

	// 格式化参数
	function formatParams(data) {
		var arr = [];
		for ( var name in data) {
			arr.push(encodeURIComponent(name) + '='
					+ encodeURIComponent(data[name]));
		}
		return arr.join('&');
	}

	function removeElement(_element) {
		if (_element == null || _element == undefined
				|| _element == "undefined")
			return;
		var _parentElement = _element.parentNode;
		if (_parentElement) {
			_parentElement.removeChild(_element);
		}
	}

	// {transId："transId",closeTxt:"返回游戏",retFunc:"callbackGame",baseZIndex:100,"redirecturl":"www.baidu.com","cpurl":"http://www.sina.com.cn","sign":"","signtype":"RSA"}
	this.invIfr = function(data) {
		try {
			ifr_mask({
				tip : 'loading'
			});
			var zIndex = data.baseZIndex;
			var ifrmSrc = data.ifrSrc;
			var closeTxt = data.closeTxt;
			if (zIndex == "undefined" || isNaN(zIndex)) {
				zIndex = 0;
			} else {
				zIndex = parseInt(zIndex);
			}
			baseZIndex = zIndex;
			createIfrCover();
			createIfrPop();
			createIframePay(ifrmSrc);
			createifrClose(closeTxt);
			ifr_unmask({});
		} catch (e) {
			ifr_unmask({});
		}
	}
}
function closeIframe(link) {
	document.getElementById("_ifrCloseTip").click();
	if (link != undefined && link != null &&link != ''){
		window.location.href = link;
		return;
	}else{
		if("undefined" != typeof $CpUrl && $CpUrl != null && $CpUrl != undefined && $CpUrl != ''){
			window.location.href = $CpUrl;
			return;
		}
	}	
}