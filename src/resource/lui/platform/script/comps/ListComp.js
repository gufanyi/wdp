/**
 * @fileoverview 列表控件
 * 
 * @auther lxl
 * @version lui 1.0
 *  
 */
(function($) {
	$.widget("lui.list", $.lui.base, {
		options: {
			position : 'absolute',
			isShowIcon : false,
			className : 'list_div',
			//event
			onclick : null,
			onmouseout : null,
			onmouseover : null
		},
		_initParam : function() {
			this.componentType = "LIST";
			this._super();
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element;
			ele.addClass(opts.className);
		},
		createListOption : function(options) {
			var opts = this.options,ele = this.element,oThis = this;
			var optionWrapper = $('<div>').addClass('optionWrapper').appendTo(ele);
			this.option = $('<ul>').appendTo(optionWrapper).data('val',options.value);
			var optionIcon = $('<li>').addClass('optionIcon').appendTo(this.option);
			var optionText = $('<li>').addClass('optionText').appendTo(this.option).html(options.text);
			
			if(options.icon) {
				opts.isShowIcon = true;
				ele.find('ul li:first-child').show();
				$('<div>').addClass('optionIconDiv').css('background','url('+options.icon+')');
			}
			
			if(!opts.isShowIcon) {
				ele.find('ul li:first-child').hide();
			}
			
			this.option.on({
				click : function(e) {
					ele.find('ul').removeClass('optionSelected');
					$(this).addClass('optionSelected');
					oThis._trigger('onclick',e,options.value);
				},
				mouseover : function(e) {
					$(this).addClass('optionOver');
					oThis._trigger('onmouseover',e);
				},
				mouseout : function(e) {
					$(this).removeClass('optionOver');
					oThis._trigger('onmouseout',e);
				}
			});
		},
		setValue : function(val) {
			this.element.find('ul').each(function(){
				if($(this).data('val') === val) {
					this.option.triggerHandler('click');
					return false;
				}
			})
		},
		getValue : function() {
			return this.element.find('ul.optionSelected').data('val');
		},
		clearListOptions : function() {
			this.element.empty();
		},
		setDataList : function(dataList) {
			this.clearListOptions();
			if (!dataList)
				return;
			var nameArr = dataList.getNameArray();
			var valueArr = dataList.getValueArray();
			var imageArr = dataList.getImageArray();
			if (nameArr != null) {
				for ( var i = 0; i < nameArr.length; i++) {
					this.createListOption({
						text : nameArr[i],
						value : valueArr[i],
						icon : imageArr[i]
					});
				}
			}
		},
		getContext : function() {
			return {
				'c' : 'ListContext',
				'id' : this.element.attr('id'),
				'value' : this.getValue()
			};
		},
		setContext : function(context) {
			if(context.value) {
				this.setValue(context.value);
			}
		}
	});
})(jQuery);
