/**
 * @fileoverview 自定义jQuery事件
 * @author lxl
 * @version 1.0
 */
(function ($) {
	
	$.event.special = {
		/**
		 * 右键单击事件
		 */
		rclick : {
			setup : function(data, namespaces, eventHandle) {
				var oThis = this;
				$(this).on('contextmenu',function(e){ 
					return false; 
				}).on('mouseup',function(e){ 
					if(3 == e.which){ 
						$.event.trigger('rclick', null, this);  
					}
				});
			},
			teardown : function(namespaces) {
				$(this).off('contextmenu mouseup');
			}
		}
	};
	
	//快捷方式
	$.fn.rclick = function(callback) {
		return this.on('rclick',callback);
	};
})(jQuery);
