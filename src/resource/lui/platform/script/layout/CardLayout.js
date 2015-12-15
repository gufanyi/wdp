/**
 * @fileoverview 卡片型布局管理器
 * 
 * @author lxl
 * @version lui 1.0
 */
(function($) {
	/**
	 * 卡片型布局管理器构造函数
	 * @class 卡片型布局管理器
	 */
	$.widget("lui.cardLayout", {
		options : {
			id : '',
			cardDiv : null,
			index : null,
			//event
			beforepageinit : null,
			afterpageinit : null,
			onbeforepagechange : null
		},
		_initParam : function() {
			this.componentType = "CARD";
			this.id = this.options.id;
			this.cardDivId = $(this.options.cardDiv).attr('id');

//		    if ($.browsersupport.IS_IE && !$.browsersupport.IS_STANDARD) this.pages = $(cardDiv).children();
//		    else {
		        this.pages = $(this.options.cardDiv).children(':not(:text)');
//		    }
		    this.currentIndex = 0;
		    if (this.options.index != null) this.currentIndex = this.options.index;
		},
		_create : function() {
			this._initParam();
			this.setPage();
		},
		refreshPages : function(index) {
		    this.pages = [];
		    var cardDiv = $("#"+this.cardDivId);
		    this.pages = $(cardDiv).children(':not(:text)');
		    this.currentIndex = 0;
		    if (index != null) this.currentIndex = index;
		    this.setPage();
		},
		/**
		 * 设置显示页面
		 */
		setPage : function(index) {
		    if (index != null) {
		        if (this.currentIndex == index) return;
		        this.currentIndex = index;
		    }
		    var _currentPage = this.pages.eq(this.currentIndex);
		    for (var i = 0; i < this.pages.length; i++) {
		        if (i != this.currentIndex) {
		            this.pages.eq(i).hide();
//		            if ($.browsersupport.IS_IE7) {
//		                if (this.pages.eq(i).css('width')) {
//		                    this.pages.eq(i).oriWidth = this.pages.eq(i).css('width');
//		                }
//		                if (this.pages.eq(i).css('height')) {
//		                    this.pages.eq(i).oriHeight = this.pages.eq(i).css('height');
//		                }
//
//		                this.pages.eq(i).css({
//		                    'width' : _currentPage.outerWidth() + "px",
//		                    'height' : _currentPage.outerHeight() + "px"
//		                });
//		            }
		            this.pages.eq(i).css({
		                'z-index' : -999,
		                'position' : 'absolute',
		                'left' : '0px',
		                'top' : '0px',
		                'overflow' : 'auto'
		            });
		        }
		    }
		    _currentPage.show();
//		    if ($.browsersupport.IS_IE7) {
//		        if (_currentPage.oriWidth) {
//		            _currentPage.css('width',_currentPage.oriWidth);
//		        }
//		        if (_currentPage.oriHeight) {
//		            _currentPage.css('height',_currentPage.oriHeight);
//		        }
//		    }

		    _currentPage.css({
		        'z-index' : '',
		        'position' : 'relative'
		    });
		    $(window).triggerHandler("resize");
		    var func;
		    if ((func = window["$" + this.id + "_item" + this.currentIndex]) != null) {
		        this._trigger("beforepageinit",null,this.currentIndex);
		        func();
		        window["$" + this.id + "_item" + this.currentIndex] = null;
		        this._trigger("afterpageinit",null,this.currentIndex);
		    }
		    // 调用布局初始化方法，以调整Card中包含的其他布局
		    //layoutInitFunc();
		   
		},
		/**
		 * 获取当前显示页面索引
		 */
		getCurrentPageIndex : function() {
		    return this.currentIndex;
		},
		/**
		 * 显示下个页面
		 */
		nextPage : function() {
		    if (this.currentIndex >= this.pages.length - 1) this.currentIndex = 0;
		    else this.currentIndex++;
		    this.setPage();
		},
		/**
		 * 显示最后一页
		 */
		lastPage : function() {
		    this.setPage(this.pages.length - 1);
		},
		/**
		 * 显示上个页面
		 */
		prePage : function() {
		    if (this.currentIndex == 0) this.currentIndex = this.pages.length - 1;
		    else this.currentIndex--;
		    this.setPage();
		},
		/**
		 * 显示第一页
		 */
		firstPage : function() {
		    this.setPage(0);
		},
		/**
		 * @private
		 */
		getContext : function() {
		    var context = {};
		    context.c = "CardContext";
		    context.currentIndex = this.currentIndex;
		    context.id = this.id;
		    var ids = [];
		    for (var i = 0; i < this.pages.length; i++) {
		        var pageid = this.pages.eq(i).attr('id');
		        ids[i] = pageid.replace(this.cardDivId + "_", "");
		    }
		    context.pageIds = ids;
		    return context;
		},
		/**
		 * @private
		 */
		setContext : function(context) {
		    var index = context.currentIndex;
		    if (index != this.currentIndex) {
		        this.setPage(index);
		    }
		}
	});
})(jQuery);
