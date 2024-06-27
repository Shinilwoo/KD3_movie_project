const slides = document.querySelectorAll('.slide_item');
const prevButton = document.querySelector('.slide_prev_button');
const nextButton = document.querySelector('.slide_next_button');
let currentSlide = 0;

function showSlide(index) {
	slides.forEach((slide, i) => {
		slide.style.display = i === index ? 'block' : 'none';
	});
}

function adjustElements(slideIndex) {
	const titleElement = document.getElementById(`movieTitle${slideIndex + 1}`);
	const yearElement = document.getElementById(`movieYear${slideIndex + 1}`);
	const buttonContainer = document.getElementById(`buttonContainer${slideIndex + 1}`);
	const titleHeight = titleElement.offsetHeight;
	const padding = 20; // 기본 패딩 값

	if (titleHeight > 100) {
		const extraPadding = titleHeight - 100;
		yearElement.style.paddingTop = `${extraPadding}px`;
		buttonContainer.style.paddingTop = `${extraPadding}px`;
	} else {
		yearElement.style.paddingTop = '0';
		buttonContainer.style.paddingTop = '0';
	}
}

prevButton.addEventListener('click', () => {
	currentSlide = (currentSlide === 0) ? slides.length - 1 : currentSlide - 1;
	showSlide(currentSlide);
	adjustElements(currentSlide);
});

nextButton.addEventListener('click', () => {
	currentSlide = (currentSlide === slides.length - 1) ? 0 : currentSlide + 1;
	showSlide(currentSlide);
	adjustElements(currentSlide);
});

showSlide(currentSlide);
adjustElements(currentSlide);

(function() {
	var inner = document.getElementById("image-section");
	var img = inner.querySelector("img");

	var mouse = {
		_x: 0,
		_y: 0,
		x: 0,
		y: 0,
		updatePosition: function(event) {
			var e = event || window.event;
			this.x = e.clientX - this._x;
			this.y = (e.clientY - this._y);
		},
		setOrigin: function(e) {
			this._x = e.offsetLeft + Math.floor(e.offsetWidth / 2);
			this._y = e.offsetTop + Math.floor(e.offsetHeight / 2);
		},
		show: function() {
			return "(" + this.x + "," + this.y + ")";
		}
	};

	mouse.setOrigin(inner);

	var counter = 0;
	var refreshRate = 10;
	var isTimeToUpdate = function() {
		return counter++ % refreshRate === 0;
	};

	var onMouseEnterHandler = function(event) {
		update(event);
	};

	var onMouseLeaveHandler = function() {
		img.style.transform = "rotateX(0deg) rotateY(0deg)";
		img.style.webkitTransform = "rotateX(0deg) rotateY(0deg)";
		img.style.mozTransform = "rotateX(0deg) rotateY(0deg)";
		img.style.msTransform = "rotateX(0deg) rotateY(0deg)";
		img.style.oTransform = "rotateX(0deg) rotateY(0deg)";
	};

	var onMouseMoveHandler = function(event) {
		if (isTimeToUpdate()) {
			update(event);
		}
	};

	var update = function(event) {
		mouse.updatePosition(event);
		updateTransformStyle(
			(mouse.y / inner.offsetHeight).toFixed(2),
			(mouse.x / inner.offsetWidth).toFixed(2)
		);
	};

	var updateTransformStyle = function(x, y) {
		var style = "rotateX(" + -x + "deg) rotateY(" + y + "deg)";
		img.style.transform = style;
		img.style.webkitTransform = style;
		img.style.mozTransform = style;
		img.style.msTransform = style;
		img.style.oTransform = style;
	};

	inner.onmousemove = onMouseMoveHandler;
	inner.onmouseleave = onMouseLeaveHandler;
	inner.onmouseenter = onMouseEnterHandler;
})();