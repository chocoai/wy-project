/**
 * 生成url地址
 * 
 * @param url
 * @param params
 * @returns
 */
function genUrl(url, params) {

	if (params) {
		if (url.lastIndexOf("?") < 0) {
			url += "?";
		} else {
			url += "&";
		}

		for ( var p in params) {
			url += (p + "=" + encodeURIComponent(params[p]) + "&");
		}

		return url.substring(0, url.length - 1);
	}
	return url;
}

function postMessageToCp(data) {
	if (window.opener) {
		window.opener.postMessage(JSON.stringify(data), '*');
	}
	if (window.opener !== window.parent) {
		if (window.parent) {
			window.parent.postMessage(JSON.stringify(data), '*');
		}
	}
}

function timeClose(item, callbackUrl, messageData) {
	if (!item) {
		return;
	}
	var time_n = $(item).attr("time");
	if (!time_n) {
		time_n = 5;
	}
	function timeC() {
		time_n -= 1;
		if (time_n > 0) {
			item.html(time_n);
		} else if (time_n == 0) {
			window.open('', '_self').close();
		} else if (callbackUrl) {
			window.location.href = genUrl(callbackUrl, messageData);
		}
		setTimeout(timeC, 1000);
	}

	setTimeout(timeC, 1000);
}

function pushCookieStorage(flushCookies) {
	if (flushCookies) {
		for ( var key in flushCookies) {
			localStorage.setItem(key, flushCookies[key]);
		}
	}
}
//$.ajaxSettings.beforeSend = function() {
//	$.mask({
//		tip : abTips.loading
//	});
//}
//$.ajaxSettings.complete = function() {
//	$.unmask();
//}

$.ajaxSettings.error = function(response, textStatus, errorThrown) {
	var status = response.status;
	if (status == 200) {
		alert('服务暂不可用!');
		return;
	}
	if (500 <= status) {
		alert("服务器异常！");
	} else {
		if (status >= 400 && status < 500) {
			alert("服务器拒绝服务！(" + status + ")");
		} else {
			alert("网络或服务异常(" + status + ")！请稍后再试！");
		}
	}
};

$(function() {
	window.onerror = function(msg, url, line) {
		// 猎豹浏览器IOS版本会出现闪屏
		// $.ajax({
		// url : '/err/jshandler',
		// data : {
		// msg : msg,
		// url : url,
		// line : line
		// }
		// });
	}
});

function isWeixinBrowse() {
	var ua = navigator.userAgent;
	var r = /MicroMessenger/;
	return r.test(ua);
}

function isHttp(str) {
	return startsWith(str, "http://") || startsWith(str, "https://");
}

function startsWith(str, prefix) {
	return str.indexOf(prefix) === 0;
}

function isHrefOpenWx() {
	var p_mac = /Mac OS/i;
	var p_version = /Version|Mobile/i;
	var p_safari = /Safari/i;
	var p_chrome = /Chrome|CriOS/i;
	var p_qh360 = /QHBrowser/i;
	var p_samsung = /SamsungBrowser/i;
	var ua = navigator.userAgent;
	var uax = [];
	// $('#ua').html(ua);
	var uaxs = ua.replace(/.*\(KHTML.*Gecko\)(.*)/, '$1').trim();
	var p_no_chrome = /^Version/i;
	var mayChrome = true;
	if (p_no_chrome.test(uaxs)) {
		mayChrome = false;
	}
	uaxs = uaxs.split(' ');
	for (i in uaxs) {
		if (!p_version.test(uaxs[i])) {
			uax.push(uaxs[i]);
		}
	}
	uaxs = uax.join(' ');
	if (p_mac.test(ua)) {// iOS系统
		if (uax.length == 1) {
			if (p_safari.test(uax[0])) {
				return 'safari';
			}
		}
	}
	if (mayChrome && uax.length == 2) {
		if (p_chrome.test(uaxs) && p_safari.test(uaxs)) {
			return 'chrome';
		}
	}
	if (p_samsung.test(uaxs)) {
		return 'samsung';
	}
	if (p_qh360.test(uaxs)) {
		return 'qh360';
	}
}

function isIOS() {
	var ua = navigator.userAgent;
	var r = /Mac OS/i;
	return r.test(ua);
}

function isIfr() {
	return self != top;
}

/**
 * 获取地址栏url
 * @param name
 * @returns
 */
function getRequestUrl(){     
     return window.location.href;
}
