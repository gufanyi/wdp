/**
 * @fileoverview Portal导航
 * data : [
 * 		{
 *			id:'id',
 *			text:'text',
 *			param:{},
 *			children:[{
 *				id:'id',
 *				text:'text',
 *				param:{}
 *			}] 
 * 		}
 * ]
 * @auther lxl
 * @version lui 1.0
 *  
 */
(function($) {
	$.widget("lui.nav", $.lui.base, {
		options: {
			className : 'portal_nav',
			top : 0,
			left : 0,
			data : []
		},
		_initParam : function() {
			this.componentType = "NAV";
			this.lastShowItem = null;
			this._super();
		},
		_create : function() {
			this._initParam();
			// 快捷键
			var oThis = this,ele = this.element,opts = this.options;
			ele.addClass(opts.className).css({
				'top' : opts.top + 'px',
				'left' : opts.left + 'px'
			});
			this.navContent = $("<div>").appendTo(ele).addClass("nav_content");
			this.switchBtn = $("<div>").appendTo(ele).addClass('nav_switch').hide();
			this.backTopLevelBar = $("<div>").appendTo(ele).addClass('back_toplevel_bar');
			this.createNavContent(opts.data)
			$(window).off('.nav').on('resize.nav',function() {
				oThis._adjustNav();
			});
			ele.hide();
		},
		createNavContent : function(data) {
			var oThis = this,ele = this.element,opts = this.options;
			data = data || [];
			if(data) {
				$.each(data,function(index,item){
					var navLevelWrapper = $("<div>").appendTo(oThis.navContent).addClass('nav_level')
						.on({
							'mouseover' : function(e) {
								$(this).children(':first').addClass('nav_toplevel_on');
							},
							'mouseout' : function(e) {
								$(this).children(':first').removeClass('nav_toplevel_on');
							}
						});
						
					var topLevel = $("<div>").attr('id',item.id).appendTo(navLevelWrapper)
						.addClass('nav_toplevel')
						.html(item.text);
						
					if(item.children) {
						$.each(item.children,function(cindex,citem) {
							var nav_sublevel_wrapper = $("<div>").attr('id',citem.id).appendTo(navLevelWrapper).addClass('nav_sublevel');
							$("<div>").addClass('sublevel_div').appendTo(nav_sublevel_wrapper)
								.html(citem.text)
								.on({
									'click' : function(e) {
										oThis._trigger('onclick',e,citem,item);
										ele.hide();
									}
								});
						})
					}
				});
				this._adjustNav();
			}
		},
		_adjustNav : function() {
			var oThis = this,ele = this.element;
			oThis.lastShowItem = null;
			//调整高度
			var _outerHeight = 0;
			this.navContent.children().each(function(){
				var _height = $(this).outerHeight();
				if(_height>_outerHeight) {
					_outerHeight = _height;
				}
			});
			this.navContent.css('height',_outerHeight).children().css('height',_outerHeight);
			
			//调整宽度
			if(ele.outerWidth() < oThis._getTopLevelSumWidth()) {
				var width = 0;
				this.navContent.children().each(function(){
					width += $(this).outerWidth();
					if(width > ele.outerWidth()) {
						oThis.lastShowItem = $(this).prev();
						oThis.backTopLevelBar.css('width',ele.outerWidth() - (width - $(this).outerWidth()));
						$(this).hide().nextAll().hide();
						return false;
					} else {
						oThis.lastShowItem = $(this);
						$(this).show();
					}
				});
				this.switchBtn.show().off('.nav_switch').on({
					'click.nav_switch' : function() {
						//显示位置计算
						var _top = $(this).offset().top + $(this).outerHeight();
						var _right = $('body').innerWidth() - $(this).offset().left - $(this).outerWidth();
						
						var dropdownDiv = $("<div>").addClass('nav_dropdown').appendTo('body').css({
							top : _top,
							right : _right
						});
						oThis.navContent.children(":hidden").each(function(){
							var hideItem = $(this);
							$("<div>").addClass('dropdown_item').appendTo(dropdownDiv)
								.html(hideItem.children(':first').html())
								.on({
									'click' : function() {
										oThis.lastShowItem.hide();
										oThis.lastShowItem = hideItem.show();
										dropdownDiv.remove();
									},
									'mouseover' : function() {
										$(this).addClass('dropdown_item_on');
									},
									'mouseout' : function() {
										$(this).removeClass('dropdown_item_on');
									}
								});
						});
					},
					'mouseover.nav_switch' : function() {
						$(this).addClass('nav_switch_on');
					},
					'mouseout.nav_switch' : function() {
						$(this).removeClass('nav_switch_on');
					}
				});
			} else {
				this.backTopLevelBar.css('width',ele.outerWidth() - oThis._getTopLevelSumWidth());
				this.navContent.children().show();
				this.switchBtn.hide();
			}
		},
		_getTopLevelSumWidth : function() {
			var width = 0;
			this.navContent.children().each(function(){
				width += $(this).outerWidth();
			});
			return width;
		},
		show : function() {
			this.element.slideDown(200);
		},
		hide : function() {
			this.element.hide();
		}
	});
})(jQuery);
