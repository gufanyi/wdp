/**
 * @fileoverview OutLookBarComp控件,提供类似百叶窗式的树行控件
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * OutLookBarComp构造函数
	 * @class OutLookBarComp控件，提供类似百叶窗式的树行控件。
	 * @constructor OutLookBarComp构造函数
	 */
	$.widget("lui.outlookbar", $.lui.base, {
		options : {
			position : 'absolute',
			className : 'outlookbar_div'
		},
		_initParam : function() {
			this.componentType = "OUTLOOKBAR";
			this._super();
			this.id = this.element.attr("id");
		    // 存放所有item项的数组
		    this.items = [];
		    // 存放当前打开的是哪个content
		    this.showContent = null;
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options,ele = this.element;
		    ele.addClass(opts.className).css({
		        'position' : opts.position,
		        'overflow' : 'hidden',
		        'width' : opts.width,
		        'height' : opts.height,
		        'top' : opts.top + "px",
		        'left' : opts.left + "px"
		    });
		    $(window).on("resize",function(){
		         oThis.adjustSelf();
		    });
		},
		 /**
	     * 创建每个子项
	     */
	    addItem : function(itemName, itemCaption, itemSrcImg, itemTitle) {
	        var item = $("<div></div>").appendTo(this.element).outlookbaritem({
	        	name : itemName,
	        	caption : itemCaption,
	        	srcImg : itemSrcImg,
	        	title : itemTitle
	        }).outlookbaritem('instance');
	        return item;
	    },
	    /**
	     * 移除某个子项
	     */
	    removeItem : function(itemId) {
	        //得到item对象
	        var targetItem = null;
	        for (var i = 0; i < this.items.length; i++) {
	            var realId = this.items[i].itemBarDiv.children().first().attr('id');
	            if (realId == itemId) {
	                targetItem = this.items[i];
	                targetItem.itemBarDiv.remove();
	                targetItem.content.remove();
	                this.items.splice(i, 1);
	            }
	        }
	        //删除掉item的itemBarDiv和content
	        this.showContent = null;
	        if (this.items.length != 0) {
	            this.items[0].itemBarDiv.children().first().triggerHandler('click');
	        }
	    },
	    /**
	     * 激活某一subitem
	     * @param index 要激活项的索引值(注:索引值从0开始)
	     */
	    activeItem : function(index) {
	        if (!this.items) return;
	        if (index < 0 || index > this.items.length - 1) return;

	        // 获取要激活的子项
	        var subItem = this.items[index];
	        // 没有任何一个子项激活
	        if (!this.showContent) {
	            subItem.content.show();
	            var newHeight = this.element.outerHeight() - this.items.length * (subItem.itemDiv.outerHeight());
	            subItem.content.css('height',newHeight + "px");
	            // 记录当前显示的内容区是哪个
	            this.showContent = subItem.content;
	            subItem.setActiveClass(true);
	            this.afterActivedItemChangeForInternal(subItem);
	        } else {
	            if (this.showContent[0] == subItem.content[0]) {
	                return;
	            } else if (this.showContent != null && this.showContent[0] != subItem.content[0]) {
	                // 首先隐藏掉上次打开的项
	                this.showContent.hide();
	                // 保存当前激活的itemContent
	                this.showContent = subItem.content;
	                for (var i = 0; i < this.items.length; i++) {
	                    this.items[i].setActiveClass(false);
	                }
	                subItem.content.show();
	                subItem.setActiveClass(true);
	                var newHeight = this.element.outerHeight() - this.items.length * subItem.itemDiv.outerHeight();
	                subItem.content.css('height',newHeight + "px");
	                this.afterActivedItemChangeForInternal(subItem);
	            }
	        }
	    },
	    /**
	     * 设置outlookbar高度
	     */
	    setHeight : function(newHeight) {
	        newHeight = parseInt(newHeight);
	        if (this.showContent != null) {
	            var contentHeight = newHeight - this.items.length * this.items[0].itemDiv.outerHeight();
	            if (contentHeight < 20) return;
	            this.showContent.css('height',(newHeight - this.items.length * this.items[0].itemDiv.outerHeight()) + "px");
	        }
	        this.height = $.measures.convertHeight(newHeight);
	        this.element.css('height',this.options.height);
	    },
	    /**
	     * 得到所有的子项
	     */
	    getAllItems : function() {
	        return this.items;
	    },
	    /**
	     * 自动调整大小
	     * @private
	     */
	    adjustSelf : function() {
	        if (!this.showContent) return;
	        var itemOffsetHeight = this.items[0].itemDiv.outerHeight();
	        //有1px的border，IE8的offsetHeight没有计算这一px，导致高度不准确
	//        if ($.browsersupport.IS_IE8) itemOffsetHeight = itemOffsetHeight + 1;
	        var newHeight = this.element.outerHeight() - this.items.length * itemOffsetHeight;
	        this.showContent.css('height',newHeight + "px");
	    },
	    /**
	     * 供内部使用的方法
	     * @private
	     */
	    afterActivedItemChangeForInternal : function(currItem) {
			var tmpFunc = window['$' + currItem.parentOwner.id + '_' + currItem.options.name + '_init'];
			if(tmpFunc){
				this._trigger("beforeItemInit",null);
				tmpFunc();
				window['$' + currItem.parentOwner.id + '_' + currItem.options.name + '_init'] = null;
				this._trigger("afterItemInit",null);
			}
	    },
	    /**
	     * 获取对象信息
	     * @private
	     */
	    getContext : function() {
	        return {
	        	c : "OutlookbarContext",
	        	id : this.element.attr("id"),
	        	currentIndex : this.currentIndex
	        };
	    },
	    /**
	     * 设置对象信息
	     * @private
	     */
	    setContext : function(context) {
	        var index = context.currentIndex;
	        if (index != null) this.activeItem(index);
	    }
	});
	/**-------------------------------------------------------------------------------------------------------------------------
	   -------------------------------------------------------OutLookBarItem----------------------------------------------------
	   ------------------------------------------------------------------------------------------------------------------------**/
	/**
	 * item子项构造函数
	 * @class item子项
	 */
	$.widget("lui.outlookbaritem", {
		options : {
			name : '',
			caption : '',
			srcImg : '',
			title : '',
			index : null
		},
		_initParam : function() {
			this.parentOwner = this.element.parent().outlookbar('instance');
			this.parentOwner.items.push(this);
		},
		_create : function() {
			this._initParam();
			this.element.hide();

			var opts = this.options,ele = this.element, oThis = this;
		 	this.itemBarDiv = $("<div></div>").addClass('itembardiv');
		    if (opts.index != null) {
		    	this.parentOwner.element.prepend(this.itemBarDiv);
		    } else {
		    	this.parentOwner.element.append(this.itemBarDiv);
		    }

		    this.leftBorderDiv = $("<div></div>");
		    this.rightBorderDiv = $("<div></div>");
		    // item子项的主体div
		    this.itemDiv = $("<div></div>").attr({
		        'title' : opts.title,
		        'id' : name
		    }).appendTo(this.itemBarDiv);

		    // 显示箭头图片
		    this.divArrowImg = $("<div></div>").addClass('item_arrow_div').appendTo(this.itemDiv);
		    this.arrowImg = $("<img/>").addClass('item_arrow_img').appendTo(this.divArrowImg);
		    this.setActiveClass(false);
		    if (opts.srcImg) {
		        // item图片
		        this.divImg = $("<div></div>").addClass('itemimg').appendTo(this.itemDiv);
		        this.img = $("<img/>");
		        if (opts.srcImg == "default") // 为"default"时显示默认图片
		            this.img.attr('src',window.themePath + "/layout/shutter/images/subitem.gif");
		        else
		            this.img.attr('src',opts.srcImg);
		        this.img.css({
		            'width' : '12px',
		            'height' : '13px'
		        }).appendTo(this.divImg);
		    }
		    // item文字div
		    this.divCaption = $("<div></div>").addClass('itemtitle').appendTo(this.itemDiv).html(opts.caption);

		    // 撑出空白区域
		    this.divBlank = $("<div></div>").addClass('itemblank').appendTo(this.itemDiv);

		    // item的内容区,每个item绑定一个自己的内容区
		    this.content = $("<div></div>").addClass('outlooktreeitem_content_div').css({
		        'overflow' : 'auto',
		        'width' : '100%'
		    });
		    if (opts.index != null) {
		    	this.parentOwner.element.children().eq(1).before(this.content);
		    } else {
		    	this.parentOwner.element.append(this.content);
		    }
		    this.content.hide();
		    // 保存和此content相关的item
		    this.content.data("relateItem",this);
		    this.itemDiv.data("owner",this);
		    var newHeight = this.parentOwner.element.outerHeight() - this.parentOwner.items.length * (oThis.itemDiv.outerHeight());
		    oThis.content.css('height',newHeight + "px");
		    //编辑态标识
		    this.itemDiv.data("objType",'outLookItem');
		    this.itemDiv.on('mouseout',function(e){
		        oThis.itemDiv.removeClass('outlooktreeitem_div_over');
		    }).on('mouseover',function(e){
		        oThis.itemDiv.addClass('outlooktreeitem_div_over');
		    }).on('click',function(e){
		        // 没有项处于打开状态
		        if (!oThis.parentOwner.showContent) {
		            var newHeight = oThis.parentOwner.element.outerHeight() - oThis.parentOwner.items.length * ($(this).outerHeight());
		            oThis.content.css('height',newHeight + "px").show();
		            oThis.setActiveClass(true);
		            // 记录当前显示的内容区是哪个
		            oThis.parentOwner.showContent = oThis.content;
		            oThis.parentOwner.afterActivedItemChangeForInternal(oThis);
		        } else {
		            if (oThis.parentOwner.showContent[0] == oThis.content[0]) {
		                // 如果点击的是最后一个item,则不隐藏
		                if (oThis.parentOwner.items[oThis.parentOwner.items.length - 1] == oThis) {
		                    return;
		                }
		                oThis.content.hide();
		                oThis.setActiveClass(false);
		                oThis.parentOwner.showContent = null;
		            } else if (oThis.parentOwner.showContent != null && oThis.parentOwner.showContent[0] != oThis.content[0]) {
		                // 首先隐藏掉上次打开的项
		                oThis.parentOwner.showContent.hide();
		                var relateItem = oThis.parentOwner.showContent.data("relateItem");
		                relateItem.setActiveClass(false);
		                // 保存当前激活的itemContent
		                oThis.parentOwner.showContent = oThis.content;

		                var newHeight = oThis.parentOwner.element.outerHeight() - oThis.parentOwner.items.length * (oThis.itemDiv.outerHeight());
		                oThis.content.css('height',newHeight + "px");
		                oThis.content.show();
		                oThis.setActiveClass(true);
		                oThis.parentOwner.afterActivedItemChangeForInternal(oThis);
		            }
		        }
		        //if (window.editMode) {
		            $(window).triggerHandler('resize');
		        //}
		    });
		},
		/**
		 * 设置外观显示
		 * @param isActive 是否是激活项
		 */
		setActiveClass : function(isActive) {
		    if (isActive) {
		        this.itemDiv.attr('class','outlooktreeitem_div_showcontent');
		        this.leftBorderDiv.attr('class','leftborderdiv_showcontent');
		        this.rightBorderDiv.attr('class','rightborderdiv_showcontent');
		        this.arrowImg.attr('src',window.themePath + "/layout/shutter/images/outlook_arrow_on.png");
		    } else {
		        this.itemDiv.attr('class','outlooktreeitem_div');
		        this.leftBorderDiv.attr('class','leftborderdiv');
		        this.rightBorderDiv.attr('class','rightborderdiv');
		        this.arrowImg.attr('src',window.themePath + "/layout/shutter/images/outlook_arrow.png");
		    }
		},
		/**
		 * 增加子项，返回子项的显示对象
		 */
		add : function(obj) {
		    this.content.append($(obj));
		}
	});
	
})(jQuery);
