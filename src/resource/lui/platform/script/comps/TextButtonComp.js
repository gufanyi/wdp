/**
 * 文本输入框控件构造函
 * 
 */

(function($) {
	$.widget("lui.textbutton", $.lui.base, {
		options : {
			height : 24,
			width : 60,
			value : '',
			// event
			onclick : null
		},
		_create : function() {
			var oThis = this;//整个对象
			// this.element就是最外層div
			var ele = this.element.css({
				'position' : 'relative'
			});
			// 创建<input>和<button>的div,并添加到ele
			var input = $("<input type='text' />").css({
			                'width' : '100px' 
		            	}), 
			     button = $("<input type='button' value='显示文本'/>").css({
			                'width' : '78px'
		            	}), 
			     inputDiv = $("<div></div>").appendTo(ele).append(input).css({
			     				'position' : 'relative',
								'float' : 'left',
								'width' : '100px'
							}),
				buttonDiv = $("<div></div>").appendTo(ele).append(button).css({
						'position' : 'relative',
						'float' : 'left',
						'width' : '80px',
						'left' : '10px'
					});
				button.on("click", function() {
						alert(input.val());
						oThis._trigger("onclick");//外部定义的onclick
					});

		},
		getContext : function() {//必须要，和后台context对应
			return {
				c : 'TextButtonContext'
			};
		}
	});
})(jQuery);
