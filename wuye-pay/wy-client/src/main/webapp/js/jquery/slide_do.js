function doSlide() {
	var _now = 0;
	var oul = $(".myslidetwo");
	var numl = $(".daohang ol li");
	var ali = $(".myslide").eq(0).width();

	numl.click(function() {
		var index = $(this).index();
		$(this).addClass("current").siblings().removeClass();
		oul.animate({
			'left' : -ali * index
		}, 500);
	});

	setInterval(function() {
		if (_now == numl.size() - 1) {
			_now = 0;
		} else {
			_now++;
		}
		numl.eq(_now).addClass("current").siblings().removeClass();
		oul.animate({
			'left' : -ali * _now
		}, 500)
	}, 4000);
}