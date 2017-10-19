/*
 * Publish Message
 * $.publish("message", data1, data2, data3, ...);
 *
 * Subscribe Message
 * $.subscribe("message", function(data1, data2, data3, ..., message){ }, fetchCache);
 *
 * When you subscribe a message, you can also get "message" with last parameter.
 * And, you can use "fetchCache" to determine whether to get messages which are
 * fired before subscribing or not. By defaut, subscribe with fetch cached messages always.
 * 
 * You can also use linked style like below to pub/sub multiple messages.
 * $.publish().subscribe.publish()
 *
 * You can do multiple messages subscribing, if you don't care about "data" parameters.
 * $.subscribe("message1 message2", function(){ });
	<a class="ac_aaaa"></a>
	$.sub('mousero_aaaa', function(){
		
		});
 *
 */

;(function($) {

// cache objec for publish-subscribe sequence.
var cache = {};
var regex = /^(click|mouseover|mouseout|mouseenter|mouseleave|change|)_\w+/i;
$.pub = function() {
	var message = arguments[0], args = [], i = 1;

	// construct the args array, and append message as the last element.
	while( i < arguments.length ) {
		args.push(arguments[i++]);
	}
	args.push(message);
	
	$('body').triggerHandler(message,args);
	return $;
};


$.sub= function() {
	var messages = $.trim(arguments[0]).replace(/\s+/," "),
		arrMsg = messages.split(" "),
		callback = arguments[1];
	
	for (var i = 0; i < arrMsg.length; i++) {
			var message = arrMsg[i];
			var actions = regex.exec(message);
			if(actions && actions.length == 2 ){
				$('body').on(actions[1],actions[0].replace(actions[1],'.ac'),callback);
			}else{
				$('body').on(message,callback);
			}
	}	
	return $;
};
})(jQuery);
