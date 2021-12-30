$(function() {
	var $banner = $('.banner');
	var $banner_ul = $('.banner-img');
	var $btn = $('.banner-btn');
	var $btn_a = $btn.find('a')
	var v_width = $banner.width();
	var page = 1;
	var timer = null;
	var time = 3000;
	var btnClass = null;
	var page_img = $banner_ul.find('li img');
	var page_count = $banner_ul.find('li').length;
	var load = $banner.find('.banner-load');
	$banner_ul.width(page_count * v_width);

	function move(obj, classname) {
		if (!$banner_ul.is(':animated')) {
			loading_start();
			if (classname == 'prevBtn') {
				if (page == 1) {
					$banner_ul.animate({
						left: -v_width * (page_count - 1)
					});
					page = page_count;
					cirMove();
				} else {
					$banner_ul.animate({
						left: '+=' + v_width
					}, "slow");
					page--;
					cirMove();
				}
			} else {
				if (page == page_count) {
					$banner_ul.animate({
						left: 0
					});
					page = 1;
					cirMove();
				} else {
					$banner_ul.animate({
						left: '-=' + v_width
					}, "slow");
					page++;
					cirMove();
				}
			}
		}
	}(function loading_reset() {
		load.css({
			"height": "3px",
			"position": "absolute",
			"left": "0",
			"bottom": "0",
			"background": "#FF4939",
			"z-index": "10"
		});
		loading_start();
	})();

	function loading_start() {
		load.animate({
			"width": "100%"
		}, time).animate({
			"width": 0
		}, 0);
	}

	function loading_stop() {
		load.stop(true, true).animate({
			"width": 0
		}, 0)
	}

	function cirMove() {
		$('.banner-nav li').eq(page - 1).addClass('selected').siblings().removeClass('selected');
	}
	$banner.mouseover(function() {
		$btn.css({
			'display': 'block'
		});
		clearInterval(timer);
		loading_stop();
	}).mouseout(function() {
		$btn.css({
			'display': 'none'
		});
		loading_start();
		clearInterval(timer);
		timer = setInterval(move, time);
	}).trigger("mouseout");
	$btn_a.mouseover(function() {
		$(this).animate({
			opacity: 1
		}, 'fast');
		$btn.css({
			'display': 'block'
		});
		return false;
	}).mouseleave(function() {
		$(this).animate({
			opacity: 0.3
		}, 'fast');
		$btn.css({
			'display': 'none'
		});
		return false;
	}).click(function() {
		btnClass = this.className;
		loading_stop();
		clearInterval(timer);
		timer = setInterval(move, time);
		move($(this), this.className);
	});
	$('.banner-nav li').live('click', function() {
		var index = $('.banner-nav li').index(this);
		$banner_ul.animate({
			left: -v_width * index
		}, 'slow');
		page = index + 1;
		cirMove();
	});
});