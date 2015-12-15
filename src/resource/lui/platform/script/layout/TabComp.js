/**
 * @iframe Tab控件
 * 
 * @auther liy lxl
 * @version lui 1.0
 *  
 */
var tabCompArray = [];
(function($) {
	$.widget("lui.tab", $.lui.base, {
	    options: {
			name : '',
			tabItemWidth : 80,			
			showCloseIcon : true,
			tabType : 'top',
			flowmode : true,
			hideTabBar : false,
			position : 'absolute',
	        tabHeight : '',
	        tabWidth : '',
            itemWidth : '',
            itemHeight : '',
            bgColor : '',
            activeItemColor : '',
            normalItemColor : '',
            activeLineColor : '',
            normalLineColor : '',
            fontSize : '',
            activeFontColor : '',
            normalFontColor : '',
			isOuterTab : false,
			// 设置是否一个tab 隐藏
			oneTabHide : false,
			className : 'tab_div'
			
		},
		_initParam : function() {
			this.componentType = "TAB",	
			this.id = this.options.name;
			this._super();
			// 当前页
			this.currActiveTab = -1;
			this.currShowMenu = null;
			this.divImgWidth = 30;
			this.SEP_HEIGHT = 3;
			//页签显示在内容上面
			this.TYPE_TOP = "top";
			//页签显示在内容下面
			this.TYPE_BOTTOM = "bottom";
			this.className = $.argumentutils.getString(this.options.className, "tab_div");
			this.height = $.argumentutils.getString(this.options.height, "100%");
			this.tabHeight = $.argumentutils.getString(this.options.tabHeight, this.options.isOuterTab?"46px":"30px");//tab总高度
			this.tabWidth = $.argumentutils.getString(this.options.tabWidth, "100%");				
			this.itemWidth = $.argumentutils.getString(this.options.itemWidth, this.options.isOuterTab?"130px":"80px");
			this.itemHeight = $.argumentutils.getString(this.options.itemHeight, this.options.isOuterTab?"36px":"26px");//tab子项高度
			this.bgColor = $.argumentutils.getString(this.options.bgColor, this.options.isOuterTab?"#2f2f2f":"#CFEDF7");//tab背景颜色
			this.activeItemColor = $.argumentutils.getString(this.options.activeItemColor, this.options.isOuterTab?"#009ae5":"#01ABF1");//tab激活项颜色
			this.normalItemColor = $.argumentutils.getString(this.options.normalItemColor, this.options.isOuterTab?"#2f2f2f":"#F1F1F1");//tab项普通状态颜色
			this.activeLineColor = $.argumentutils.getString(this.options.activeLineColor, this.options.isOuterTab?"#009ae5":"#27B6F3");//tab激活项边框颜色
			this.normalLineColor = $.argumentutils.getString(this.options.normalLineColor, this.options.isOuterTab?"#a5a5a6":"#C2C2C2");//tab项普通状态边框颜色
			this.fontSize  = $.argumentutils.getString(this.options.fontSize, this.options.isOuterTab?"18px":"14px");//tab字体大小
			this.activeFontColor = $.argumentutils.getString(this.options.activeFontColor, this.options.isOuterTab?"#fff":"#fff");//tab字体颜色
			this.normalFontColor = $.argumentutils.getString(this.options.normalFontColor, this.options.isOuterTab?"#a5a5a6":"#8C8B8B");//tab字体颜色
			this.closeImgTop = parseInt(this.itemHeight.replace('px',''))/2 -2;//关闭图标高度
			
			this.isHideTabHead = this.options.hideTabBar;
			
			// 存放全部tabItem的数组
		    this.tabItems = [];
		    // 存放可见部分的item数组
		    this.visibleItems = {};
		    // 可见部分的开始位置
		    this.visibleItems.begin = 0;
		    // 可见部分的结束位置
		    this.visibleItems.end = 0;
		    // 可见部分的长度
		    this.visibleItems.length = 0;
		    // 用于判断是否显示左右按钮的标志
		    this.showRLArrow = false;
		    // 保存当前激活的item
		    this.activedItem = null;
		},
		_create : function() {
			this._initParam();
			// 快捷键
			var oThis = this,ele = this.element,opts = this.options;
		    ele.addClass(this.className).css({
		    	'position' : opts.position,
		    	'width' : opts.width,
		    	'height' : this.height
		    });
		    
		    this.bgCenterDiv = $("<div>").addClass('bg_center_div').appendTo(ele).css('background',this.bgColor);
		    
		    if (this.isHideTabHead) {
		        this.bgCenterDiv.hide();
		    }
		    
		    //创建itemsBar工具条div
		    this.divItemsBarWrapper = $("<div>").appendTo(this.bgCenterDiv).attr({
		    	'id' : "div_items_bar_" + ele.attr("id"),
		    	'class' : 'tab_items_bar' + (opts.tabType == this.TYPE_TOP ? "" : "_bottom")
		    });
		    
            var divItemsBarDiv = $("<div>").appendTo(this.divItemsBarWrapper).css({
            	'position' : 'relative',
            	'height' : this.tabHeight
            });
            $("<div>").addClass("tab_item_bar_bottom"+(opts.isOuterTab?"_outer":"")).appendTo(this.divItemsBarWrapper);
            
		    this.divItemsBar = $("<div>").appendTo(divItemsBarDiv).css({
		    	'position' : 'absolute',
		    	'left' : '5px',
		    	'right' : '5px',
		    	'bottom' : '0px'
		    }).on('rclick',function(e){e.stopPropagation();e.preventDefault()});
		    
		    // 创建内容部分div
		    this.divContent = $("<div>").addClass('tab_content_div').attr({
		        'id' : "div_content_" + this.element.attr("id")
		    });
		    if (opts.tabType == this.TYPE_TOP) {
		        ele.append(this.divContent);
		    } else {
		        ele.prepend(this.divContent);
		    }
		
		    //tabRight容器
		    this.rightBarSpace = $("<div></div>").addClass('right_space').appendTo(this.divItemsBar);
		
		    // 创建右侧放置左右按钮的div
		    this.divImg = $("<div></div>").addClass('tab_img_div').appendTo(this.divItemsBar);
		
		    var barHeight = this.divItemsBarWrapper.outerHeight();
		    if (this.isHideTabHead) barHeight = 0;
		    var contentHeight = ele.outerHeight() - parseInt(barHeight);
		    if (contentHeight > 0 && (opts.tabType == this.TYPE_BOTTOM || !this.flowmode)) {
		        this.divContent.css('height',contentHeight + "px");
		    }
		    
		    tabCompArray.push(this);
		    
			$(window).on('resize',function(){
		    	oThis.tabResize();
		    });
		},
		tabCompResize : function(oTabComp) {
		    oTabComp.adjustSelf();
		},
		allTabCompResize : function() {
		    for (var i = 0, n = tabCompArray.length; i < n; i++) {
		        this.tabCompResize(tabCompArray[i]);
		    }
		},
		/**
		 * 初始化右键菜单
		 * @private
		 */
		initContextMenu : function() {
			if (!this.contextMenu) {
		        var menuName = this.element.attr("id") + "_menu";
		        this.contextMenu = new ContextMenuComp(menuName, 0, 0, false);
		        // 关闭当前页签
		        var closeCurrentCaption = "关闭当前页签";
		        var menuCloseCurrent = this.contextMenu.addMenu(menuName + "_close_current", closeCurrentCaption, null, null, false);
		        // 增加点击事件
		        var closeCurrentMouseListener = new MouseListener();
		        closeCurrentMouseListener.onclick = function(e) {
		            var menuItem = e.obj;
		            var currentItem = menuItem.parentOwner.triggerObj;
		            currentItem.parentTab.removeItemTab(currentItem.parentTab.getItemIndex(currentItem));
		        };
		        menuCloseCurrent.addListener(closeCurrentMouseListener);
		        // 关闭其它页签
		        var closeOthersCaption = "关闭其它页签";
		        var menuCloseOthers = this.contextMenu.addMenu(menuName + "_close_others", closeOthersCaption, null, null, false);
		        // 增加点击事件
		        var closeOthersMouseListener = new MouseListener();
		        closeOthersMouseListener.onclick = function(e) {
		            var menuItem = e.obj;
		            var currentItem = menuItem.parentOwner.triggerObj;
		            var tab = currentItem.parentTab;
		            for (var i = tab.tabItems.length - 1; i > 0; i--) {
		                var tabItem = tab.tabItems[i];
		                if (tabItem.showCloseIcon && tabItem != currentItem) {
		                    tab.removeItemTab(tab.getItemIndex(tabItem));
		                }
		            }
		        };
		        menuCloseOthers.addListener(closeOthersMouseListener);
		        // 关闭所有页签
		        var closeAllCaption = "关闭所有页签";
		        var menuCloseAll = this.contextMenu.addMenu(menuName + "_close_all", closeAllCaption, null, null, false);
		        // 增加点击事件
		        var closeAllMouseListener = new MouseListener();
		        closeAllMouseListener.onclick = function(e) {
		            var menuItem = e.obj;
		            var currentItem = menuItem.parentOwner.triggerObj;
		            var tab = currentItem.parentTab;
		            for (var i = tab.tabItems.length - 1; i > 0; i--) {
		                var tabItem = tab.tabItems[i];
		                if (tabItem.showCloseIcon) {
		                    tab.removeItemTab(tab.getItemIndex(tabItem));
		                }
		            }
		        };
		        menuCloseAll.addListener(closeAllMouseListener);
		    }
		},/**
		 * 改变大小
		 * @private
		 */
		tabResize : function(tabId) {
		    var tab = this;
		    var outerDiv = tab.element;
		
		    try {
		        // 如果此时tab大小和上次不发生变化,则不会真正的重新调整grid大小
		        var cond1 = (tab.outerDivWidth != null && tab.outerDivWidth == outerDiv.outerWidth());
		        var cond2 = (tab.outerDivHeight != null && tab.outerDivHeight == outerDiv.outerHeight());
		        var cond3 = (tab.lastTabSelectedIndex != null && tab.lastTabSelectedIndex == tab.getSelectedIndex());
		        var cond4 = (tab.lastItemCount != null && tab.lastItemCount == tab.tabItems.length);
		        if(!cond4){
		        	tab.letBeginToEndVisible();
	        		tab.lastItemCount = tab.tabItems.length;
	        	}
		        if (cond1 && cond2 && cond3) return;
		        tab.outerDivWidth = outerDiv.outerWidth();
		        tab.outerDivHeight = outerDiv.outerHeight();
		        tab.lastTabSelectedIndex = tab.getSelectedIndex();
		        
		        tab.adjustSelf();
		
		    } catch(error) {
		        log("tab resize error");
		    }
		},
		createTabRightPanel : function(panelId,panelWidth) {
			this.rightBarSpace.empty();
			$("<div id='"+panelId+"'>").appendTo(this.rightBarSpace).css({
				"min-width":"50px",
				"height":"28px",
				"overflow":"hidden",
				"width":panelWidth+"px"
			});
		},
		removeTabRightPanel : function() {
			this.rightBarSpace.empty();
		},
		changeTabRightPanelWidth : function(width) {
			this.rightBarSpace.children().css('width',width+'px');
		},
		/**
		  * 重置各子项的样式
	      * @private
	   	  */
		resetItemStyle : function() {
			var currentIndex = this.getSelectedIndex();
			if (currentIndex == null) currentIndex = 0;
			for (var i = 0, n = this.tabItems.length; i < n; i++) {
			    var item = this.tabItems[i];
			    // 设置中间位置样式
			    if (currentIndex == i) {
			        item.isActive = true;
			        item.divTitle.attr('class',this.options.tabType == this.TYPE_TOP ? "tab_item_centerborder_on": "tab_item_centerborder_on_bottom").css({
			        	'border' : '1px solid '+this.activeLineColor,
			        	'border-bottom' : 'none',
			        	'background' : this.activeItemColor,
			        	'color': this.activeFontColor
			        });
			    } else {
			        item.isActive = false;
			        item.divTitle.attr('class',this.options.tabType == this.TYPE_TOP ? "tab_item_centerborder_off": "tab_item_centerborder_off_bottom").css({
			        	'border' : '1px solid '+this.normalLineColor,
			        	'border-bottom' : 'none',
			        	'background': this.normalItemColor,
			        	'color': this.normalFontColor
			        });
			    }
			    
			    // 设置删除按钮样式
			    if (item.closeImgDiv) {
			        if (currentIndex == i) {
			            item.closeImgDiv.attr('class',this.options.tabType == this.TYPE_TOP ? "tab_item_closeicon_on": "tab_item_closeicon_on_bottom");
			        } else {
			            item.closeImgDiv.attr('class',this.options.tabType == this.TYPE_TOP ? "tab_item_closeicon": "tab_item_closeicon_bottom");
			        }
			        item.closeImgDiv.css({
			        	top:this.closeImgTop+'px',
			        	right : '10px'
			        });
				}
			}
		},
		
		/**
		 * 绘制隐藏标签下拉列表
		 * (不包含被用户主动隐藏的页签)
		 */
		ShowInvisibleTabList : function (){
			if(this.invisibleTabList)
				this.invisibleTabList.remove();
			var event = window.event;
			var tabListDiv=[];
			tabListDiv.push('<div class="invisible-tab-list-container" >');
			this.tabItems.forEach(function(tab){
				if(tab.trueVisible&&!tab.visible)
					tabListDiv.push('<div class="invisible-tab-list-item" name="'+tab.name+'">'+tab.options.title+'</div>');
			});
			tabListDiv.push('</div>');
			var $tabListDiv=$(tabListDiv.join(''));
			var $tabBar=$(event.target).parents(".tab_items_bar");
			var oThis=this;
			$tabBar.append($tabListDiv);
			$tabListDiv.css("top",$tabBar.height()+'px');
			$tabListDiv.css("right","0px");
			$tabListDiv.find(".invisible-tab-list-item").click(function(){
				oThis.SwitchTab();
			});
			this.invisibleTabList=$tabListDiv;
		},
		
		/**
		 * 切换可见页签
		 */
		SwitchTab : function(e) {
			var event = e || window.event;
			var $targetItem=$(event.target);
			var targetTabName=$targetItem.attr("name");
			var lastVisibleTab=null;
			var selectVisibleTabIndex=null;
			this.tabItems.forEach(function(tab,index){
				if(tab.visible && tab.trueVisible)
					lastVisibleTab=tab;
				if(tab.name===targetTabName)
					selectVisibleTabIndex=index;
			});
//			lastVisibleTab.hide();
//				lastVisibleTab.sep.hide();
			this.activeTab(selectVisibleTabIndex);
			this.invisibleTabList.remove();
			this.invisibleTabList=null;
		},

		
		/**
		 * 处理点击左右移动按扭后的函数
		 * @param direction
		 * 用数字表示移动的方向(1:向右移动,-1:向左移动)
		 * @private
		 */
		 MoveTab : function(direction) {
		    if (this.tabItems == null || this.tabItems.length == 0) return;
		    // 点击向右的按钮
		    if (direction > 0) {
		        if (this.tabItems[this.visibleItems.end + direction]) {
		            // 存放下一个要显示出来的item(右边的)
		            this.nextDisplayItem = this.tabItems[this.visibleItems.end + direction];
		            // 存放当前显示的最左边的item
		            this.borderItem = this.tabItems[this.visibleItems.begin];
		            // 存放当前所有显示item的开始位置
		            var mayActivePos = this.visibleItems.begin;
		
		            if (!this.tempLength) this.tempLength = this.visibleItems.begin + direction;
		
		            // 使两个指示指针向指定方向移动,卡住新范围内的可见items
		            this.visibleItems.begin += direction;
		            this.visibleItems.end += direction;
		
		            // 处理下一个将要显示的item
		            if (this.nextDisplayItem.trueVisible == true) this.nextDisplayItem.showTitle();
		            // 如果要隐藏的item是ActiveTab,则改变ActiveTab的位置
		            if (this.currActiveTab == mayActivePos) {
		                this.activeTab(mayActivePos + direction, this.tabItems[this.currActiveTab]);
		            }
		            this.manageImages();
		            this.changeVisibleItems();
		        } else {
		            if (this.tempLength < this.tabItems.length - 1) {
		                this.tempLength = this.tempLength + direction;
		                this.activeTab(this.tempLength, this.tabItems[this.currActiveTab]);
		            }
		        }
		    }
		    // 点击向左的的按钮
		    else if (direction < 0) {
		        if (this.tabItems[this.visibleItems.begin + direction]) {
		            // 存放下一个要显示出来的item(左边的)
		            this.nextDisplayItem = this.tabItems[this.visibleItems.begin + direction];
		            // 存放当前显示的最右边的item
		            this.borderItem = this.tabItems[this.visibleItems.end];
		            // 存放当前所有显示item的开始位置
		            var mayActivePos = this.visibleItems.end;
		            if (!this.tempLength) this.tempLength = this.visibleItems.end + direction;
		
		            // 使两个指示指针向指定方向移动,卡住新范围内的可见items
		            this.visibleItems.begin += direction;
		            this.visibleItems.end += direction;
		
		            // 处理下一个将要显示的item
		            if (this.nextDisplayItem.trueVisible != false) this.nextDisplayItem.showTitle();
		
		            // 如果要隐藏的item是ActiveTab,则改变ActiveTab的位置
		            if (this.currActiveTab == mayActivePos) {
		                this.activeTab(mayActivePos + direction, this.tabItems[this.currActiveTab]);
		            }
		            this.manageImages();
		            this.changeVisibleItems();
		        } else {
		            if (this.tempLength > 0) {
		                this.tempLength = this.tempLength + direction;
		                this.activeTab(this.tempLength, this.tabItems[this.currActiveTab]);
		            } else {
		                this.activeTab(0, this.tabItems[0]);
		            }
		        }
		    }
		},
		/**
		 * 根据情况设置图片的显示状态(现改为始终显示左右箭头)
		 * @private
		 */
		manageImages : function() {
		    this.divImg.css('width',this.divImgWidth + "px");
		},
		/**
		 * 改变显示区的items
		 * @private
		 */
		changeVisibleItems : function(millTime) {
		    if (!millTime) defaultMillTime = 100;
		    else defaultMillTime = millTime / 10;
		    if (parseInt(this.borderItem.element.outerWidth()) > 20) {
		        this.borderItem.changeWidth( - 20);
		        this.nextDisplayItem.changeWidth( + 20);
		        this.time = window.setTimeout("window.objects['" + this.element.attr("id") + "'].changeVisibleItems(" + defaultMillTime + ");", defaultMillTime);
		    } else this.borderItem.hide();
		},
		/**
		 * 改变显示区的items
		 * @private
		 */
		/**
		 * 跟据id激活组件
		 * @param {} id
		 */
		activeTabByName : function(name) {
		    var item = this.getItemByName(name);
		    if (item == null) return;
		    var index = this.getItemIndex(item);
		    if (index == -1) return;
		    this.activeTab(index);
		},
		/**
		 * 使指定位置的组件激活
		 * @param index
		 * 当前要激活的item的位置
		 * @param activedItem
		 * 当前激活的item
		 */
		activeTab : function(index, activedItem, ignevent) {
		    index = parseInt(index);
		    if (this.currActiveTab == index) return;
		    if (index > this.tabItems.length - 1 || index < 0) return;
		    else {
		        if (this.tabItems[index].trueVisible == false) this.showItem(this.tabItems[index].name);
		
		        if (activedItem != null) {
		            if (this.beforeActivedTabItemChange(activedItem, this.tabItems[index]) == false) return;
		        }
		        // 保存当前激活的tab索引
		        this.currActiveTab = index;
		        // 使当前tab可见
		        this.letBeginToEndVisible();
		        var tempActivedItem = this.tabItems[index];
		        if (this.activedItem != tempActivedItem) {
		            if (this.activedItem != null) {
		                var oldActiveItem = this.activedItem;
		                oldActiveItem.hideContent();
		            }
		            this.activedItem = tempActivedItem;
		            tempActivedItem.showContent();
		            //this.adjustSelf();
		        }
		        // 激活的还是自己只需设置一下display属性
		        else tempActivedItem.divContent.css('visibility','');
		        this.tempLength = index;
		
		        this.afterActivedTabItemChangeForInternal(this.tabItems[index]);
		        if (ignevent) {} else this.afterActivedTabItemChange(this.tabItems[index]);
		
		        // 重置各子项的样式
		        this.resetItemStyle();
		        $(window).triggerHandler('resize');
		    }
		},
		/**
		 * 由item项的名字得到item项的索引
		 * @return -1表示没有找到
		 */
		getItemIndexByName : function(name) {
		    for (var i = 0, count = this.tabItems.length; i < count; i++) {
		        if (this.tabItems[i].name == name) return i;
		    }
		    return - 1;
		},
		/**
		 * 得到item的索引值(索引值从0开始)
		 */
		getItemIndex : function(item) {
		    return this.tabItems.indexOf(item);
		},
		/**
		 * 获取第一个非隐藏页签
		 */
		getFirstVisibleItemIndex : function() {
		    for (var i = 0,count = this.tabItems.length; i < count; i++) {
		        if (this.tabItems[i].trueVisible == true) return i;
		    }
		    return - 1;
		},
		/**
		 * 根据name获取item
		 */
		getItemByName : function(name) {
		    for (var i = 0,
		    count = this.tabItems.length; i < count; i++) {
		        if (this.tabItems[i].name == name) return this.tabItems[i];
		    }
		    return null;
		},
		/**
		 * 显示item
		 */
		showItem : function(name) {
		    if (name == null) return;
		    var item = this.getItemByName(name);
		    if (item != null) {
		        item.trueVisible = true;
		        // 如果显示的tabItem不是激活的item,那么只是显示出element，不显示内容
		        if (this.getSelectedItem() != null && this.getSelectedItem().name == name) item.show();
		        else item.element.show();
		    }
		},
		/**
		 * 隐藏item
		 */
		hideItem : function(name) {
		    if (name == null) return;
		    var item = this.getItemByName(name);
		    if (item != null) {
		        item.trueVisible = false;
		        item.hide();
		        //找到第一个不隐藏的item,显示出来
		        if (this.getItemIndex(item) == this.currActiveTab) {
		            for (var i = 0; i < this.tabItems.length; i++) {
		                if (this.tabItems[i].trueVisible == true) {
		                    this.activeTab(i);
		                    break;
		                }
		            }
		        }
		
		    }
		},
		/**
		 * 得到当前的激活项
		 */
		getSelectedItem : function() {
		    if (this.currActiveTab == -1) return null;
		    return this.tabItems[this.currActiveTab];
		},
		/**
		 * 得到当前激活项的索引值
		 */
		getSelectedIndex : function() {
		    return this.currActiveTab;
		},
		getItemSize : function() {
			return this.tabItems.length;
		},
		/**
		 * 创建指定item子项
		 */
		createItem : function(name, title, showCloseIcon, isFirstItem, disabled, pageState,visiable) {
		    var oThis = this;
		    var sep = null;
		    if (this.tabItems == null || this.tabItems.length == 0) isFirstItem = true;
		    if (!isFirstItem) {
		        sep = $("<div></div>").addClass('tab_seperator_ver').appendTo(this.divItemsBar);
		    }
		
		    // tabItem是否采用自动宽度
		    var autoWidth = true;
		    if (this.itemWidth > 0) autoWidth = false;
		
		    // 创建item
		    var item = $("<div>").tabitem({
		    	name:name,
		    	title:title,
		    	showCloseIcon:showCloseIcon,
		    	isFirstItem:isFirstItem,
		    	disabled:disabled,
		    	pageState:pageState,
		    	itemHeight:this.itemHeight,
		    	itemWidth:this.itemWidth,
		    	fontColor:this.normalFontColor,
		    	fontSize:this.fontSize
		    }).tabitem("instance");
		    
		    // 将sep绑定到每个item数据对象上
		    if (!isFirstItem) item.sep = sep;
		    item.parentTab = this;
		    item.createtabitem();
		
		    var end = this.tabItems.push(item);
		    this.divContent.append(item.getObjHtml());
		    this.divItemsBar.append(item.element);
		    item.tabItemOffsetWidth = item.element.outerWidth();
		    
		    /**
		     * 从方法末尾移动到此处
		     * @start
		     */
		    // item的click事件
		    item.click = function(e) {
		        oThis.tempLength = oThis.tabItems.indexOf(item);			       
		        if (item.onclick(e) == false) return;
		        oThis.activeTab(oThis.tempLength, oThis.tabItems[oThis.currActiveTab]);
		        oThis.onclick(e);
		    };
		
		    item.show();
		    /**移动部分@end*/
		    	
		    // 重置各子项的样式
		    this.resetItemStyle();
		    if (this.oneTabHide == true && this.isHideTabHead == true && this.tabItems.length > 1) this.showTabHead();
		    
		    this.letBeginToEndVisible();
		    // 当item过多时显示左右移动按钮于右侧
		    /**
		     * 改为"如果计算得到的显示区域tab数量小于所有 可见 tab数量，则显示下拉按钮
		     */
		    var visibleItemCount=0;
		    this.tabItems.forEach(function(tab){
		    	if(tab.trueVisible)
		    		visibleItemCount++;
		    });
//		    if (this.visibleItems.length < this.tabItems.length) {
		    if (this.visibleItems.length < visibleItemCount) {
		      this.showRLArrow = true;
		    }
		    if (this.showRLArrow) {
		        if (this.next == null && this.previous == null) {
		            // 创建"箭头向右"的图标
		            var attrObj = {
		                position: 'relative',
		                boolFloatRight: true,
		                pRefInactive: window.themePath + "/layout/tab/images/toright.gif"
		            };
		            this.next = 
		            	$('<div id="ToRight" class="tab-invisible-menu-button">')
	        				.appendTo(this.divImg)
	        				.click(function(){
	        					oThis.ShowInvisibleTabList();
	    					})
	    					.mouseover(function(){
	    						$(this).css("background-image","url("+window.themePath+"/layout/tab/images/tab_list_hover.png)");
	    					})
	    					.mouseleave(function(){
	    						$(this).css("background-image","url("+window.themePath+"/layout/tab/images/tab_list_normal.png)");
	    					})
	        				.css("background-image","url("+window.themePath+"/layout/tab/images/tab_list_normal.png)");
//		            this.next = $('<div class="tab-invisible-menu-button">').appendTo(this.divImg).image($.extend({},{
//                        refImg1 : window.themePath + "/layout/tab/images/toright.gif",
//                        left : -8,
//                        top : 7,
//                        width : 16,
//                        height : 16,
//                        alt : trans("ml_toright"),
//                        refImg2 : window.themePath + "/layout/tab/images/toright.gif",
//                        onclick : function() {
//                            oThis.ShowInvisibleTabList();
//                        }
//                    },attrObj));
		            var barHeight = parseInt(this.divItemsBar.outerHeight()),
		            imageHeight = parseInt(this.next.outerHeight());
		
		            /* 创建"箭头向左"的图标
		            attrObj.pRefInactive = window.themePath + "/layout/tab/images/toleft.gif";
		            this.previous = $('<div>').appendTo(this.divImg).image($.extend({},{
                        refImg1 : window.themePath + "/layout/tab/images/toleft.gif",
                        left : -17,
                        top : 7,
                        width : 16,
                        height : 16,
                        alt : trans("ml_toleft"),
                        refImg2 : window.themePath + "/layout/tab/images/toleft.gif",
                        onclick : function() {
                            oThis.MoveTab(-1);
                        }
                    },attrObj));
		            this.divImg.show();*/
		        }
		    }
		    
		    return item;
		},
		/**
		 * 使处于begin和end间的items可见
		 * @private
		 */
		letBeginToEndVisible : function() {
		    this.manageVisibleItems();
		    //隐藏掉tab选择列表
		    if(this.invisibleTabList)
		    	this.invisibleTabList.remove();
		    // 先隐藏掉所有的items
//		    for (var i = 0, count = this.tabItems.length; i < count; i++) 
//		    	this.tabItems[i].hide();
//		    // 使处于begin和end间的item可见,重新设定begin to end的items的弹出菜单的left值
//		    for (var i = this.visibleItems.begin; i <= this.visibleItems.end; i++) {
//		        if (i >= this.tabItems.length) break;
//		        var item = this.tabItems[i];
//		        if (item.trueVisible == true) item.showTitle();
//		    }
		    this.manageImages();
		},
		/**
		 * 调整可见区域的begin和end,使得当前激活的tab处于可见区
		 * @private
		 */
		manageVisibleItems : function() {
		    var tabwidth = this.element.outerWidth();
		    
		    var itemsRealLength = tabwidth-20;
		    var sumWidth = 0;
		    for (var i = 0; i < this.tabItems.length; i++) {
		    	if(!this.tabItems[i].trueVisible)
		    		continue;
		        sumWidth += this.tabItems[i].tabItemOffsetWidth;
		        if (i > 0) {
		            sumWidth += this.tabItems[i].sep.outerWidth();
		        }
		    }
		    if (sumWidth > itemsRealLength) {
		        this.showRLArrow = true;
		    } else {
		        this.showRLArrow = false;
		        if(this.divImg)
		        	this.divImg.hide();
		        this.visibleItems.begin = 0;
		    }
		    // 创建了左右箭头按钮时的items可见区长度
		    if (this.showRLArrow) {
		        itemsRealLength = itemsRealLength - this.divImgWidth;
		    }
		
		    /**
		     * 调整可视区域计算方法
		     */
		     //隐藏所有的items
		     for (var i = 0, count = this.tabItems.length; i < count; i++) 
		    	this.tabItems[i].hide();
		     var _totalWidth=0;
		     this.visibleItems.length=0;
		     //先将当前激活页签加入总宽度计算
		     if(this.currActiveTab>=0){
			     _totalWidth+=this.tabItems[this.currActiveTab].tabItemOffsetWidth;
			     this.tabItems[this.currActiveTab].show();
			     if(this.currActiveTab >0 ){
			    	 _totalWidth += this.tabItems[this.currActiveTab].sep.outerWidth();
		    	 }
			     this.visibleItems.length++;
		     }
		     
		     //遍历所有节点，
		     for(var i=0;i<this.tabItems.length;i++){
		    	 if(i === this.currActiveTab||!this.tabItems[i].trueVisible)
		    		 continue;
		    	 _totalWidth += this.tabItems[i].tabItemOffsetWidth;
		    	 if(i > 0 ){
		    		 _totalWidth += this.tabItems[i].sep.outerWidth();
	    		 }
		    	 if(_totalWidth > itemsRealLength){
		    		 this.tabItems[i].element.hide();
	    		 }else{
	    			 this.tabItems[i].showTitle();
    				 this.visibleItems.length++;
    			 }
	    	 }
//		     
//
//		    // 调整可见区的begin和end,使currActiveTab处于可见区
//		    var needCal = true;
//		    while (needCal) {
//		        var visibleLength = 0;
//		        var lastBorderWidth = 0;
//		        for (var i = this.visibleItems.begin; i < this.tabItems.length; i++) {
//		            var itemWidth = this.tabItems[i].element.get(0).scrollWidth;
//		            // 隐藏值根据前一个推算
//		            if (itemWidth == 0) {
//		                if (lastBorderWidth == 0) {
//		                    if (this.tabItems[i - 1]) {
//		                        lastBorderWidth = this.tabItems[i - 1].element.outerWidth() - parseInt(this.tabItems[i - 1].divTitle.css('width'));
//		                        itemWidth = this.tabItems[i].tabItemOffsetWidth; //parseInt(this.tabItems[i].divTitle.style.width)	+ lastBorderWidth;
//		                    }
//		                }
//		            }
//		            if (itemWidth == 0) {
//		                this.tabItems[i].showTitle();
//		                itemWidth = this.tabItems[i].element.get(0).scrollWidth;
//		                this.tabItems[i].hide();
//		            }
//		            itemsRealLength -= itemWidth;
//		            if (this.tabItems[i].sep) {
//		                itemsRealLength -= this.tabItems[i].sep.outerWidth();
//		            }
//		            if (itemsRealLength >= 0) {
//		                visibleLength++;
//		            } else {
//		                break;
//		            }
//		        }
//		
//		        if (this.currActiveTab >= this.visibleItems.begin + visibleLength) {
//		            this.visibleItems.begin = this.currActiveTab - visibleLength + 1;
//		            needCal = (itemsRealLength > 0);
//		        } else if (this.currActiveTab >= 0 && this.currActiveTab < this.visibleItems.begin) {
//		            this.visibleItems.begin = this.currActiveTab;
//		            needCal = false;
//		        } else {
//		            needCal = false;
//		        }
//		    }
//		    if (itemsRealLength > 0) {
//		        for (var i = this.visibleItems.begin - 1; i > 0; i--) {
//		            var itemWidth = this.tabItems[i].element.get(0).scrollWidth;
//		            // 隐藏值根据前一个推算
//		            if (itemWidth == 0) {
//		                if (lastBorderWidth == 0) {
//		                    if (this.tabItems[i - 1]) {
//		                        lastBorderWidth = this.tabItems[i - 1].element.outerWidth() - parseInt(this.tabItems[i - 1].divTitle.css('width'));
//		                        itemWidth = this.tabItems[i].tabItemOffsetWidth; //parseInt(this.tabItems[i].divTitle.style.width) + lastBorderWidth;
//		                    }
//		                }
//		            }
//		            if (itemWidth <= 0) {
//		                this.tabItems[i].showTitle();
//		                itemWidth = this.tabItems[i].element.get(0).scrollWidth;
//		                this.tabItems[i].hide();
//		            }
//		            itemsRealLength -= itemWidth;
//		            if (this.tabItems[i].sep) {
//		                itemsRealLength -= this.tabItems[i].sep.outerWidth();
//		            }
//		            if (itemsRealLength >= 0) {
//		                visibleLength++;
//		                this.visibleItems.begin = i;
//		            } else {
//		                break;
//		            }
//		        }
//		    }
//		    
//		    this.visibleItems.length = visibleLength;
//		    this.visibleItems.end = (this.visibleItems.begin + this.visibleItems.length - 1 < this.tabItems.length) ? this.visibleItems.begin + this.visibleItems.length - 1 : this.visibleItems.length - 1;
//		    this.showRLArrow = this.visibleItems.length < this.tabItems.length;
		
		},
		/**
		 * 从tab中移除掉指定的item
		 */
		removeItemTab : function(index) {
		    if (this.tabItems.length == 0) return;
		
		    var item = this.tabItems[index];
		    // 在处理之前留给用户执行自己的代码段的机会,用户可以重载tab的closeItem方法来实现自己的功能
		    if (item.close() == false) return;
		
		    if (this.tabItems.length >= 2) {
		        // 删除tab处于可见区尾,且是激活tab
		        var targetItem = null;
		        if (index == this.visibleItems.end && index == this.currActiveTab) {
		            targetItem = this.tabItems[index - 1];
		            this.visibleItems.end--;
		        }
		        // 删除的tab在可见区中间或开头,且是激活tab
		        else if (index != this.visibleItems.end && index == this.currActiveTab) {
		            targetItem = this.tabItems[index + 1];
		        }
		
		        // 删除此tab边上的竖条
		        if (item.sep) item.sep.remove();//item.sep.parentNode.removeChild(item.sep);
		
		        // 删除掉此tab
		        item.element.remove();//this.divItemsBar.removeChild(item.element);
		        item.divContent.remove();//.innerHTML = "";
		        //this.divContent.removeChild(item.divContent);
		        this.tabItems.splice(index, 1);
		        this.letBeginToEndVisible();
		
		        if (this.tabItems[index]) this.activeTab(index, targetItem);
		        else this.activeTab(index - 1, targetItem);
		
		    }
		    // 只剩一个tab,且是激活tab
		    else if (this.tabItems.length == 1 && index == this.currActiveTab) {
		        this.beforeActivedTabItemChange(item, null);
		        item.element.remove();//this.divItemsBar.removeChild(item.element);
		        item.divContent.remove();//.innerHTML = "";
		        //this.divContent.removeChild(item.divContent);
		        this.tabItems.splice(index, 1);
		    } else if (this.tabItems.length < 1) return;
		
		},
		/**
		 * 调整大小
		 * @private
		 */
		adjustSelf : function() {
		    var selectedItem = this.getSelectedItem();
		    if (!selectedItem) {
		        var barHeight = this.divItemsBar.css('height');
		        if (this.isHideTabHead) barHeight = 0;
		        var height = this.element.outerHeight() - parseInt(barHeight);
		        return;
		    }
		    var oThis = this;
		    this.letBeginToEndVisible();
		    if (selectedItem.trueVisible != false) this.getSelectedItem().show();
		    // 如果显示区长度小于所有items长度,则显示左右按钮
		    /**
		     * 改为"如果计算得到的显示区域tab数量小于所有 可见 tab数量，则显示下拉按钮
		     */
		    var visibleItemCount=0;
		    this.tabItems.forEach(function(tab){
		    	if(tab.trueVisible)
		    		visibleItemCount++;
		    });
		    if (this.visibleItems.length < visibleItemCount) {
		        // 创建"箭头向右"的图标
		        if (this.next == null && this.previous == null) {
		            attrObj = {
		                position: 'relative',
		                boolFloatRight: true,
		                pRefInactive: window.themePath + "/layout/tab/images/toright.gif"
		            };
		            this.next = 
		            	$('<div id="ToRight" class="tab-invisible-menu-button">')
	        				.appendTo(this.divImg)
	        				.click(function(){
	        					oThis.ShowInvisibleTabList();
	    					})
	    					.mouseover(function(){
	    						$(this).css("background-image","url("+window.themePath+"/layout/tab/images/tab_list_hover.png)");
	    					})
	    					.mouseleave(function(){
	    						$(this).css("background-image","url("+window.themePath+"/layout/tab/images/tab_list_normal.png)");
	    					})
	        				.css("background-image","url("+window.themePath+"/layout/tab/images/tab_list_normal.png)");
		            
//		            this.next = $('<div id="ToRight"></div>').appendTo(this.divImg).image($.extend({},{
//                        refImg1 : window.themePath + "/layout/tab/images/toright.gif",
//                        left : -8,
//                        top : 7,
//                        width : 16,
//                        height : 16,
//                        alt : trans("ml_toright"),
//                        refImg2 : window.themePath + "/layout/tab/images/toright.gif",
//                        onclick : function() {
//                            oThis.ShowInvisibleTabList();
//                        }
//                    },attrObj));

		            /* 创建"箭头向左"的图标
		            attrObj = {
		                position: 'relative',
		                boolFloatRight: true,
		                pRefInactive: window.themePath + "/ui/container/tab/images/toleft.gif"
		            };
		            this.previous = $('<div id="ToLeft"></div>').appendTo(this.divImg).image($.extend({},{
                        refImg1 : window.themePath + "/layout/tab/images/toleft.gif",
                        left : -17,
                        top : 7,
                        width : 16,
                        height : 16,
                        alt : trans("ml_toleft"),
                        refImg2 : window.themePath + "/layout/tab/images/toleft.gif",
                        onclick : function() {
                            oThis.MoveTab(-1);
                        }
                    },attrObj));*/
		        }
		        this.divImg.show();
		    } else {
		        this.divImg.hide();
		    }
		
		    var barHeight = this.divItemsBarWrapper.outerHeight();
		    if (this.isHideTabHead) barHeight = 0;
		    var contentHeight = this.element.outerHeight() - parseInt(barHeight);
		    if (contentHeight > 20 && (this.options.tabType == this.TYPE_BOTTOM || !this.flowmode)) {
		        this.divContent.css('height',contentHeight + "px");
		    }
		    // 此处是为单表体显示时grid能够正常显示的处理
		    this.divContent.outerHeight();
		    if (window.editMode) $(window).triggerHandler('resize');
		},
		/**
		 * 隐藏tab头
		 */
		hideTabHead : function() {
		    this.isHideTabHead = true;
		    this.bgCenterDiv.hide();
		    this.adjustSelf();
		},
		/**
		 * 显示tab头
		 */
		showTabHead : function() {
		    this.isHideTabHead = false;
		    this.bgCenterDiv.show();
		    this.adjustSelf();
		},
		getTabItems : function() {
		    return this.tabItems;
		},
		/**
		 * 供内部使用的方法
		 * @private
		 */
		afterActivedTabItemChangeForInternal : function(targetItem) {
		    var tmpFunc = window['$' + targetItem.parentTab.id + '_' + targetItem.id + '_init'];
		    if (tmpFunc) {
		        this.beforeItemInit(targetItem);
		        tmpFunc();
		        window['$' + targetItem.parentTab.id + '_' + targetItem.id + '_init'] = null;
		        this.afterItemInit(targetItem);
		    }
		},
		/**
		 * 点击事件
		 * 
		 * @private
		 */
		onclick : function(e) {
		    var mouseEvent = {
		        "obj": this,
		        "event": e
		    };
		    //this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
		},
		/**
		 * 子项初始化前事件
		 * @private
		 */
		beforeItemInit : function(targetItem) {
		    var tabItemEvent = {
		        "obj": this,
		        "item": targetItem
		    };
		    //this.doEventFunc("beforeItemInit", TabListener.listenerType, tabItemEvent);
		},
		/**
		 * 子项初始化后事件
		 * 
		 * @private
		 */
		afterItemInit : function(targetItem) {
		    var tabItemEvent = {
		        "obj": this,
		        "item": targetItem
		    };
		    this._trigger('afterItemInit',null,tabItemEvent);
		    //this.doEventFunc("afterItemInit", TabListener.listenerType, tabItemEvent);
		},
		/**
		 * 在关闭item之前会调用该方法,返回false将阻止关闭页签
		 * 
		 * @private
		 */
		closeItem : function(item) {
		    var tabItemEvent = {
		        "obj": this,
		        "item": item
		    };
		    //return this.doEventFunc("closeItem", TabListener.listenerType, tabItemEvent);
		},
		/**
		 * 当激活item位置变化时会掉用这个函数,用户可以重载此函数处理自己事情
		 * @param activedItem
		 * 当前激活的item
		 * @param targetItem
		 * 要被激活的item
		 * @private
		 */
		beforeActivedTabItemChange : function(activedItem, targetItem) {
		    var tabItemChangeEvent = {
		        "obj": this,
		        "activedItem": activedItem,
		        "targetItem": targetItem
		    };
		    //this.doEventFunc("beforeActivedTabItemChange", TabListener.listenerType, tabItemChangeEvent);
		},
		/**
		 * 当激活item位置变化后会掉用这个函数,用户可以重载此函数处理自己事情
		 * @private
		 */
		afterActivedTabItemChange : function(activedItem) {
		    var tabItemEvent = {
		        "obj": this,
		        "item": activedItem
		    };
		    this._trigger('afterActivedTabItemChange',null,tabItemEvent);
		    //this.doEventFunc("afterActivedTabItemChange", TabListener.listenerType, tabItemEvent);
		},
		setOnTabHide : function(hide) {
		    if (hide == null) return;
		    this.oneTabHide = hide;
		    // 显示的tabitem数
		    var showItemCount = 0;
		
		    for (var i = 0; i < this.tabItems.length; i++) {
		        if (this.tabItems[i].trueVisible == true) showItemCount++;
		    }
		
		    if (showItemCount == 1 && this.oneTabHide && (this.isHideTabHead == null || this.isHideTabHead == false)) this.hideTabHead();
		    else if (showItemCount > 1 && this.isHideTabHead) this.showTabHead();
		
		},
		/**
		 * 获取对象信息
		 * 
		 * @private
		 */
		getContext : function() {
		    var context = {};
		    context.c = "TabContext";
		    var items = this.tabItems;
		    var itemIds = [],
		    itemTexts = [];
		    var showIcons = [];
		    var enables = [];
		    var visibles = [];
		    for (var i = 0; i < items.length; i++) {
		        itemIds.push(items[i].name);
		        itemTexts.push(items[i].options.title);
		        showIcons.push(items[i].options.showCloseIcon);
		        enables.push(!items[i].options.disabled);
		        visibles.push(items[i].trueVisible);
		    }
		    context.tabItemTexts = itemTexts;
		    context.tabItemIds = itemIds;
		    context.tabItemEnables = enables;
		    context.showCloseIcons = showIcons;
		    context.tabItemVisibles = visibles;
		    context.id = this.id;
		    context.currentIndex = this.getSelectedIndex();
		    context.oneTabHide = this.oneTabHide;
		    return context;
		},
		/**
		 * 设置对象信息
		 * 
		 * @private
		 */
		setContext : function(context) {
		    // 设置是否一个tab时隐藏
		    this.oneTabHide = context.oneTabHide;
		    // 显示的tabitem数
		    var showItemCount = 0;
		    for (var i = 0; i < context.tabItemVisibles.length; i++) {
		        // 处理TabItem显示隐藏
		        if (this.tabItems[i].trueVisible != context.tabItemVisibles[i]) {
		            if (context.tabItemVisibles[i] == true) this.showItem(this.tabItems[i].name);
		            else this.hideItem(this.tabItems[i].name);
		        }
		        // 统计显示的tabItem数
		        if (context.tabItemVisibles[i]) showItemCount++;
		    }
		    if (showItemCount == 1 && this.oneTabHide && (this.isHideTabHead == null || this.isHideTabHead == false)) this.hideTabHead();
		    else if (showItemCount > 1 && this.isHideTabHead) this.showTabHead();
		
		    if (context.currentIndex != null && context.currentIndex != this.getSelectedIndex()) this.activeTab(context.currentIndex, this.tabItems[this.getSelectedIndex()]);
		}
	});
	
	
	$.widget("lui.tabitem", $.lui.base, {
	     options: {
			name : '',		
			showCloseIcon : false,
			tabType : 'top',
			autoWidth : false,
			title : '',			
			disabled : false,
            isActive : false,
            pageState : '',
	        itemHeight: '',
	        itemWidth : '',
	        fontSize  : '',
			fontColor : ''
		},
		_initParam : function() {
			this.componentType = "TABITEM";	
			this._super();
			this.parentTab = null,
			this.trueVisible = true,
			this.TYPE_TOP = "top";			
			this.TYPE_BOTTOM = "bottom";
			this.itemHeight =  $.argumentutils.getString(this.options.itemHeight, "36px");
			this.itemWidth =  $.argumentutils.getString(this.options.itemWidth, "80px");
			this.fontSize  = $.argumentutils.getString(this.options.fontSize, "18px");
			this.fontColor = $.argumentutils.getString(this.options.fontColor, "#fff");
		},
		_create : function(){
			this.id = this.options.name;
			this.name = this.options.name;
			this._initParam();
		},
		/**
		 * 真正创建tabitem
		 * @private
		 */
		createtabitem : function() {
		    var oThis = this,opts = this.options,ele = this.element;
		
		    var className = opts.tabType == this.TYPE_TOP ? "tab_item_div": "tab_item_div_bottom";
		    // 创建每个tab的总div
		    ele.addClass(className).attr({
		        'title' : opts.title,
		        'id' : "div_tabitem_" + opts.name,
		        'objType' : 'tabItem'
		    });
		    
		    var eleWidth = this.itemWidth;
		    if (this.options.showCloseIcon) {
		    	var _itemWidth = 0;
		    	if(this.itemWidth.indexOf('px')>=0) {
		    		_itemWidth = parseInt(this.itemWidth.substring(0,this.itemWidth.indexOf('px')));
		    	} else {
		    		_itemWidth = parseInt(this.itemWidth);
		    	}
		    	//item宽度 + 关闭图片宽度 + 居右宽度
		    	eleWidth = (_itemWidth + 10 + 10) + 'px';
		    }
		    
		    // 创建中部显示标题名字的div
		    this.divTitle = $("<div>").addClass(opts.tabType == this.TYPE_TOP ? "tab_item_centerborder_off": "tab_item_centerborder_off_bottom").css({
		    	'font-size':this.fontSize,   
		        'color':this.fontColor,   
	            'line-height':this.itemHeight,		 
	            'width':eleWidth,
	            'height':this.itemHeight
		    }).appendTo(ele);
		    
		    $("<div>").appendTo(this.divTitle).html(opts.title).css({
		    	'width' : this.itemWidth
		    })
		
		    // 创建内容面板
		    this.divContent = $("<div></div>").css({
		        'height' : '100%'
		    });
		    
		    this.divContent.owner = this;
		
		    // 创建item关闭按钮
		    if (opts.showCloseIcon && this.closeImgDiv == null) {
		        this.closeImgDiv = $("<div>").appendTo(this.divTitle).on('click',function(e){		        	
		            oThis.parentTab.hideItem(oThis.options.name);
		            e.preventDefault();
		            e.stopPropagation();
		        }).on("mouseover",function(e) {
		        }).on("mouseout",function(e) {
		        });
		    }
		
		    ele.on('click',function(e){
		        e.triggerItem = oThis;
		        oThis.click(e);
		        e.triggerItem = null;
		    });
				
		    // 可关闭页签增加右键菜单
		    if (opts.showCloseIcon) {
		    	ele.on('rclick',function(e){
		            // 获取右键菜单对象
		            if (!oThis.parentTab.contextMenu) {
		                // 初始化右键菜单
		                oThis.parentTab.initContextMenu();
		            }
		            oThis.parentTab.contextMenu.triggerObj = oThis;
		            oThis.parentTab.contextMenu.show(e);
		    	})
		    }
		
		    if (opts.disabled) {
		        ele.off('click mouseout mouseover');
		    }
		
		    this.hideContent();
		
		},
		/**
		 * 隐藏包含内容
		 */
		hideContent : function() {
			this.divContent.hide();
		},
		/**
		 * 隐藏包含内容
		 */
		showContent : function() {
			this.divContent.show();
		},
		/**
		 * 改变item的title
		 * 
		 * @param title
		 */
		changeTitle : function(title) {
			this.options.title = title;
		    this.element.attr('title',this.options.title);
		    // 得到文字的宽度
		    var textWidth = $.measures.getTextWidth(this.options.title, this.parentTab.className + "_text_width") + 2;
		
		    this.divTitle.css('width',textWidth);
		    // 改变关闭按钮的位置
		    if (this.divTitle.closeImgDiv) this.divTitle.closeImgDiv.css('left',textWidth - 15);
		    this.divTitle.html(this.options.title);//this.divTitle.replaceChild(newTitle, this.divTitle.firstChild);
		    this.tabItemOffsetWidth = this.element.outerWidth();
		},
		/**
		 * 添加分行符
		 */
		addSeparator : function() {
		    this.menuDiv.append($("<hr/>"));
		},
		/**
		 * 使指定item项不可见,包括此item的title和content
		 */
		hide : function() {
		    this.element.hide();
		    this.divContent.hide();
		    if (this.sep) this.sep.hide();
		    this.visible = false;
		},
		/**
		 * 显示指定的item项
		 */
		show : function() {
		    this.element.show();
		    this.divContent.show();
		    if (this.sep)
		        this.sep.show();
		    this.visible = true;
		},
		/**
		 * 使指定的tabItem显示标题名字
		 */
		showTitle : function() {
		    this.element.show();
		    if (this.sep) this.sep.show();
		    this.visible = true;
		},
		/**
		 * 改变itemDiv的宽度
		 * 
		 * @private
		 */
		changeWidth : function(delta) {
		    if (delta == 0)
		        this.element.css('width','0px');
		    else
		        this.element.css('width',Math.max(0, parseInt(this.element.outerWidth()) + delta) + "px");
		},
		/**
		 * itemtab设置宽度
		 */
		setWidth : function(width) {
		    // 总tabdiv设置宽度
		    this.element.css('width',Math.max(0, width) + "px");
		},
		/**
		 * 在内容面板上添加需要的其它组件
		 */
		add : function(obj) {
		    this.divContent.append($(obj));
		},
		/**
		 * 得到内容面板的显示对象
		 */
		getObjHtml : function() {
		    return this.divContent.get(0);
		},
		/**
		 * 会调用到tab的closeItem方法,方便在tab控件统一监听item的关闭事件
		 */
		close : function() {
		    return this.parentTab.closeItem(this);
		},
		/**
		 * 响应具体的弹出菜单item点击事件
		 * 
		 * @private
		 */
		onclick : function(e) {
		    var mouseEvent = {
		        "obj": this,
		        "event": e
		    };
		    //this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
		},
		/**
		 * 若用户返回false,则不删除此itemtab.否则删除
		 * 
		 * @private
		 */
		onclose : function() {
		    var simpleEvent = {
		        "obj": this
		    };
		    //this.doEventFunc("onclose", TabItemListener.listenerType, simpleEvent);
		}
	});

})(jQuery); 
