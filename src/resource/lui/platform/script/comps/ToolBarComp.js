/**
 *
 * @fileoverview ToolBar,可加标题作为Panel的Header，也可加各种按钮等作为工具条
 * 
 * @author lxl
 * @version lui 1.0
 */
(function($) {
	/**
	 * 工具条构造函数
	 * @class 工具条控件
	 * @constructor
	 * 
	 * @param transparent: 是否透明，默认为否
	 */
	$.widget("lui.toolbar", $.lui.base, {
		options : {
			position : 'relative',
			className : '',
			transparent : false
		},
		_initParam : function() {
			this.componentType = "TOOLBAR";
			this.parentOwner = this.element.parent()[0];
			this._super();
			// 保存ToolBar中添加的所有按钮项
			this.buttonItems = $.hashmap.getObj();
			// 保存ToolBar中添加的所有自定义项
			this.selfDefItems = $.hashmap.getObj();
			// 保存子项对应的分隔条
			this.itemSepMap = $.hashmap.getObj();
			this.leftButtonItems = [];
			this.rightButtonItems = [];
			this.leftSelfDefItems = [];
			this.rightSelfDefItems = [];
			
			// 所有子项的外层DIV数组，用于在长度超出时进行调整
			this.allItemsDivArray = new Array();
			
			this.userClassName = this.options.className;
			var baseClassName = $.argumentutils.getString(this.userClassName, "toolbar_div");
			this.className = baseClassName;
			this.baseClassName = baseClassName;
			if (this.options.transparent) {
				this.options.transparent = true;
				this.className += "_transparent";
			}
			this.toolHeight = this.options.height || "40";
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element;
			ele.addClass(this.className).css({
				'left' : opts.left + "px",
				'top' : opts.top  + "px",
				'position' : opts.position,
				'width' : opts.width,
				'height' : this.convertSize(this.toolHeight)
			});
			
			this.centerDiv = $("<div></div>").addClass('center_div').appendTo(ele);
			this.toRightDiv = $("<div></div>").addClass(this.baseClassName + "_toright_div_hidden").appendTo(this.centerDiv);
			this.toLeftDiv = $("<div></div>").addClass(this.baseClassName + "_toleft_div_hidden").appendTo(this.centerDiv);
		},
		/** 
		 * 销毁控件
		 * @private
		 */
		destroySelf : function() {
//			// 销毁子按钮
//			var buttonItems = this.buttonItems.values();
//			for (var i = 0, n = buttonItems.length; i < n; i++) {
//				var button = buttonItems[i];
//				button.destroySelf();
//			}
//			// 销毁自定义子项
//			var selfDefItems = this.selfDefItems.values();
//			for (var i = 0, n = selfDefItems.length; i < n; i++) {
//				var item = selfDefItems[i];
//				if (item.destroySelf)
//					item.destroySelf();
//			}
//			this.destroy();
			this.element.remove();
		},
		/**
		 * 增加标题
		 * @param text 标题文字
		 * @param color 标题文字颜色
		 * @param isBold 标题文字是否为粗体
		 * @param srcImg 标题图片链接
		 * @param menu 标题图片点击后触发的Menu
		 */
		setTitle : function(text, color, isBold, refImg_1, refImg_2, menu) {
			var opts = this.options,ele = this.element,oThis = this;
			this.className = $.argumentutils.getString(this.userClassName, this.baseClassName + "_title");
			if (opts.transparent == true) {
				this.className += "_transparent";
			}
			ele.attr("class",this.className);
			
			if (!this.title) {
				this.title = $("<div></div>").addClass("toolbar_title").css({
					'overflow' : 'hidden',
					'float' : 'left'
				}).appendTo(this.centerDiv);
				if (refImg_1) {
					var top = "2";
//					if (!$.browsersupport.IS_IE)
						top = "3";
					
					this.title_img = $("<div id=\""+ele.attr("id")+"_title_image\"></div>").image({
						refImg1 : refImg_1,
						left : 0,
						top : top,
						width : 18,
						height : 18,
						refImg2 : (refImg_2 == "" ? null : refImg_2),
						position : 'relative',
						boolFloatLeft : true
					}).appendTo(this.title);
					if (menu == null) {
						this.title_img.css("cursor","default").on("mouseover",function(e){
							oThis.title_img.css("cursor","default");
						}).on("mouseover",function(e){
							oThis.title_img.css("cursor","default");
						}).on("mouseout ",function(e){
							oThis.title_img.css("cursor","default");
						});
					} else {  // ToolBar的右键菜单在题目的图标上显示
						this.title.css("cursor","pointer").on("click",function(e){
							if (menu != null)
								menu.show(e);
							e.stopPropagation();
						}).on("mouseover",function(){
							//oThis.title.img.img1.src = refImg_2;
							oThis.title_img.image("changeImage",refImg_2,'');
						}).on("mouseout",function(){
							//oThis.title.img.img1.src = refImg_1;
							oThis.title_img.image("changeImage",refImg_1,'');
						});
					}
				}
				var width = $.measures.getTextWidth(text, this.className + "_text");
				if (isBold)
					width += text.length * 2;
				this.titleTextDiv = $("<div></div>").addClass(this.className + "_text").css({
					'float' : 'left',
					'width' : width + "px",
					'whiteSpace' : 'nowrap',
					'color' : (color ? color : "black"),
					'font-weight' : (isBold ? "bold" : "normal")
				}).html(text).appendTo(this.title);
				
				this.title.css("width",width + 25 + "px");
				
			} else {
				if (refImg_1) {
					//this.title.img.refImg1 = refImg_1;
					//this.title.img.img1.src = refImg_1;
					this.title_img.image("changeImage",refImg_1,'');
				}
				if (refImg_2 && refImg_2 != "") {
					//this.title.img.refImg2 = refImg_2;
					//this.title.img.img2.src = refImg_2;
					this.title_img.image("changeImage",refImg_1,refImg_2);
				}
				this.titleTextDiv.css({
					'color' : (color ? color : "black"),
					'font-weight' : (isBold ? "bold" : "normal")
				});
			}
		},
		/**
		 * 增加分隔条
		 * @param align 对齐方式
		 */
		addSep : function(align, itemId) {
			var floatAttr = (align && align == "right") ? "right" : "left";
			var height = this.toolHeight == 30 ? 20:30;
			var sep = $("<div></div>").addClass("toolitem_seperator").css({
				'float' : floatAttr, 
				'height' : height
			}).appendTo(this.centerDiv);
			if(this.toolHeight == 30)
				sep.css({
					'margin-top' : 5
				});
			
			if (itemId != null) {
				sep.attr("id",itemId + "_sep");
				this.itemSepMap.put(itemId, sep);
			}
			this.addItemDiv(sep);
		},
		onBeforeShow : function() {
           //TODO   临时增加  赵维武
        },
		/**
		 * 增加按钮
		 * @param id 按钮ID
		 * @param text 文字
		 * @param tip 鼠标悬停提示信息
		 * @param refImg 图片链接
		 * @param align 对齐方式
		 * @param withSep 是否有分隔条
		 * @param width 按钮宽度
		 */
		addButton : function(options) {
			var oThis = this,opts = this.options,ele = this.element;
			var floatAttr = (options.align && options.align == "right") ? "right" : "left";
			
			if (!options.id || "" == options.id)
				options.id = this.element.attr("id") + "_button_" + this.buttonItems.length;
			
			var className = this.title ? this.baseClassName + "_button_title" : this.baseClassName + "_button";
			if (opts.transparent)
				className = this.baseClassName + "_button_transparent";
					
			var realWidth = options.width;
			if (!realWidth) {
				if(this.toolHeight == 30)
					realWidth = 30;
				else
					realWidth = 40;
				if (options.text && options.text != ""){
					if(this.toolHeight == 30)
						realWidth = $.measures.getTextWidth(options.text, "toolbar_div_button") + 31;
					else
						realWidth = $.measures.getTextWidth(options.text, "toolbar_div_button") + 41;
				}
			}
			var toolbarItemDiv = $("<div id='"+options.id+"_toolbarItemDiv'>").appendTo(this.centerDiv);
			var button = $("<div id=\""+options.id+"\"></div>").appendTo(toolbarItemDiv).button({
				top : 0,
				width : realWidth,
				height : this.toolHeight,
				text : options.text,
				tip : options.tip,
				refImg : options.refImg,
				position : 'relative',
				align : 'right',
				className : className,
				onmouseover : function(e){
					var btn = $(e.target).button('instance');
					if(options.refImg) {
						btn.imgNode.attr('src',$.argumentutils.addImgSuffix(options.refImg,"_on"));
					}
					if(oThis.toolHeight == 30){//若高为30，则去掉底下的蓝条
						btn.butt.attr('class', 'btn_on_30');
					}
					if(options.droplist) {//三角形区一起变亮
						if(oThis.toolHeight == 30)
							$(e.target).next().addClass('triangle_on_30');
						else
							$(e.target).next().addClass('triangle_on');//oThis.triangle----$(e.target).next()
					}
				},
				onmouseout : function(e){
					var btn = $(e.target).button('instance');
					if(options.refImg) {
						btn.imgNode.attr('src',options.refImg);
					}
					if(options.droplist) {//三角形区
						if(oThis.toolHeight == 30)
							$(e.target).next().removeClass('triangle_on_30');
						else
							$(e.target).next().removeClass('triangle_on');
						$(e.target).next().removeClass("triangle_down");
						$(e.target).next().removeClass('triangle_up');
					}
				},
				onmousedown : function(e){
					if(options.refImg) {
						var btn = $(e.target).button('instance');
						btn.imgNode.attr('src',$.argumentutils.addImgSuffix(options.refImg,"_down"));
					}
					if(options.droplist) {//三角形区
						$(e.target).next().removeClass("triangle_on_30");
						$(e.target).next().removeClass("triangle_on");
					}
				},
				onmouseup : function(e){
					if(options.droplist) {//三角形区
						if(oThis.toolHeight == 30)
							$(e.target).next().addClass('triangle_on_30');
						else
							$(e.target).next().addClass('triangle_on');
						$(e.target).next().addClass('triangle_up');
					}
				}
			}).button('instance');
//			if($.browsersupport.IS_IE7){
//				this.centerDiv.css("width",70);
//			}
			toolbarItemDiv.css("float",floatAttr);
			
			this.buttonItems.put(options.id, button);
			
			if (options.withSep != false) {
				if (floatAttr == "left" && this.leftButtonItems.length != 0)
					this.addSep("left", options.id); 
				else if (floatAttr == "right" && this.rightButtonItems.length != 0)
					this.addSep("right", options.id);
			}

			if (floatAttr == "left" && this.leftButtonItems.length == 0)
				button.element.css("margin-left","0px");
			else if (floatAttr == "right" && this.rightButtonItems.length == 0)
				button.element.css("margin-right","0px");
			
			if (floatAttr == "left")
				this.leftButtonItems.push(button);
			else if (floatAttr == "right")
				this.rightButtonItems.push(button);
			
			if(options.droplist) {
				//加三角形区
				var newWidth = realWidth * 1.45//realWidth + 14;
				toolbarItemDiv.css("width", newWidth);
				button.element.css({
					'float' : 'left',
					'width' : realWidth - 6
				});
				this.triangle = $("<div></div").appendTo(toolbarItemDiv).addClass("toolitem_triangle").css({
					'width' : realWidth * 0.45,//14,
					'height' : this.toolHeight-1,
					'float' : 'left',
					'left' : '-5px'
				}).on("click", function(e){
					$(document).triggerHandler("click");
			        var dorplist = pageUI.getViewPart(options.viewPartId).getMenu(options.droplist);
			        dorplist.setPosFix(true);
					dorplist.setPosLeft($(e.target).prev().offset().left);
					dorplist.setPosTop($(e.target).prev().offset().top + $(e.target).outerHeight());
					dorplist.show(e);
					e.stopPropagation();
				}).on("mouseover", function(e){
					var btn = $(e.target).prev().button('instance');
					if(options.refImg) {
						btn.imgNode.attr('src',$.argumentutils.addImgSuffix(options.refImg,"_on"));
					}
					if(oThis.toolHeight == 30){//若高为30，则去掉底下的蓝条
						btn.butt.attr('class', 'btn_on_30');
					}
					if(oThis.toolHeight == 30)
						$(e.target).addClass('triangle_on_30');
					else
						$(e.target).addClass('triangle_on');
				}).on("mouseout", function(e){
					var btn = $(e.target).prev().button('instance');
					btn.element.triggerHandler("mouseout");
				}).on("mousedown", function(e){
					$(e.target).addClass('triangle_down');
				}).on("mouseup", function(e){
					$(e.target).removeClass('triangle_down');
				});
				
				 
				//下拉菜单，button单击事件触发
//				button.element.on('buttononclick',function(e){
//					$(document).triggerHandler("click");
//			        var dorplist = pageUI.getViewPart(options.viewPartId).getMenu(options.droplist);
//			        dorplist.setPosFix(true);
//					dorplist.setPosLeft($(e.target).offset().left);
//					dorplist.setPosTop($(e.target).offset().top + $(e.target).outerHeight());
//					dorplist.show(e);
//			    });
			}
				
			//TODO : 右键菜单暂时屏蔽
//			// 为按钮增加右键菜单显示事件
//			var menuListener = new ContextMenuListener();
//			menuListener.beforeShow = function(e) {
//				var btn = e.obj;
//				// 如果按钮没有右键菜单，则将工具条的右键菜单赋给按钮
//				if (btn.getContextMenu() == null) {
//					if (oThis.getContextMenu() != null)
//						btn.setContextMenu(oThis.getContextMenu());
//				}
//			};
//			button.addListener(menuListener);

			this.addItemDiv(button.element);
			
			if (options.disabled)
				button.setActive(false);
			
			button.setVisible(options.visible);
			
		    if (window.editMode) {
	            var params = {
	                uiid: "",
	                eleid: this.element.attr("id"),
	                widgetid: options.viewPartId || "",
	                type: "toolbar_element",
	                subeleid: options.id
	            };
	            if (!window.windowEditorMode) {
	                $.design.getObj({divObj:toolbarItemDiv[0], params:params, objType:$.design.Constant.COMPOMENT_TYPE});
	            }
		    }
			
			return button;
		},
		/**
		 * 增加自定义子项
		 * @param id 子项ID
		 * @param align 对齐方式
		 * @param width 宽度
		 * @param innerHTML 内容代码
		 */
		addSelfDefItem : function(id, align, width, innerHTML) {

			var floatAttr = (align && align == "right") ? "right" : "left";
			
			if (!id || "" == id)
				id = this.element.attr("id") + "_selfdef_" + this.buttonItems.length;
					
			var selfDef = $("<div id=\""+id+"\"></div>").css({
				'width' : (width.indexOf("%") == -1 ? $.argumentutils.getInteger(width) + "px" : width),
				'height' : '100%',
				'float' : floatAttr
			}).appendTo(this.centerDiv).html(innerHTML);
				
			this.selfDefItems.put(id, selfDef);

			if (floatAttr == "left" && this.leftSelfDefItems.length == 0)
				selfDef.css("margin-left","5px");
			else if (floatAttr == "right" && this.rightSelfDefItems.length == 0)
				selfDef.css("margin-right","5px");
			
			if (floatAttr == "left")
				this.leftSelfDefItems.push(selfDef);
			else if (floatAttr == "right")
				this.rightSelfDefItems.push(selfDef);

			this.addItemDiv(selfDef);
			
			return selfDef;
		},
		/**
		 * 删除按钮
		 */
		removeButton : function(id) {
			var btn = this.getButton(id);
			if (btn != null) {
				// 删除分割线
				this.removeSep(btn.element);
				// 删除按钮
				this.removeItemDiv(btn.element);
				//btn.destroySelf();
				btn.element.remove();
				// 从工具条按钮集合中删除
				this.buttonItems.remove(id);
				if (btn.option.align == "left")
					this.leftButtonItems.removeEle(btn);
				else if (btn.option.align == "right")
					this.rightButtonItems.removeEle(btn);
			}
		},
		/**
		 * 删除子项对应的分割线
		 */
		removeSep : function(button) {
			if (this.itemSepMap.containsKey(button.attr("id"))) {
				var sep = this.itemSepMap.get(button.attr("id"));
				this.removeItemDiv(sep);
				sep.remove();
				this.itemSepMap.remove(button.attr("id"));
			}
		},
		/**
		 * 删除自定义子项
		 */
		removeSelfDefItem : function(id) {
			var selfDef = this.getSelfDefItem(id);
			if (selfDef != null) {
				if (selfDef.css("float") == "left")
					this.leftSelfDefItems.remove(id);
				else if (selfDef.css("float") == "right")
					this.rightSelfDefItems.remove(id);
				// 删除自定义子项
				this.removeItemDiv(selfDef);
				selfDef.remove();
				//selfDef.destroySelf();
				// 从工具条按钮集合中删除
				this.selfDefItems.remove(id);
			}
		},
		/**
		 * 获取所有按钮子项（Map类型）
		 */
		getButtonItems : function() {
			return this.buttonItems;
		},
		/**
		 * 根据ID获取按钮
		 */
		getButton : function(id) {
			if (this.buttonItems.containsKey(id))
				return this.buttonItems.get(id);
			return null;
		},
		/**
		 * 获取所有自定义子项（Map类型）
		 */
		getSelfDefItems : function() {
			return this.selfDefItems;
		},
		/**
		 * 根据ID获取自定义子项
		 */
		getSelfDefItem : function(id) {
			if (this.selfDefItems.containsKey(id))
				return this.selfDefItems.get(id);
			return null;
		},
		/**
		 * 设置自定义子项内容
		 */
		setSelfDefItemInnerHTML : function(id, html) {
			if (this.selfDefItems.containsKey(id)) {
				var selfDef = this.selfDefItems.get(id);
				selfDef.html(html);
			}
		},
		/**
		 * 响应快捷键（只响应第一个匹配子项的点击事件）
		 * @private
		 */
		handleHotKey : function(key) {
			// 匹配Button子项快捷键
			var buttonItems = this.buttonItems.values();
			if (buttonItems.length > 0) {
				for (var i = 0, n = buttonItems.length; i < n; i++) {
					var obj = buttonItems[i].button("handleHotKey",key);
					if (obj != null)
						return obj;
				}
			}
			return null;
		},
		/**
		 * 检查工具条中内容总长度
		 * @param withArrowDiv 是否把箭头算在内
		 * @return true为总长度小于容器长度，false为总长度大于容器长度
		 * @private
		 */
		checkWidth : function(withArrowDiv) {
			var totalWidth = 0;
			var centerDivWidth = this.centerDiv.outerWidth();
			for (var i = 0, n = this.allItemsDivArray.length; i < n; i++) {
				if (this.allItemsDivArray[i].data("shown")) {
					totalWidth += this.allItemsDivArray[i].outerWidth();
					if (withArrowDiv) {
						if (totalWidth >= centerDivWidth - 50)  // 50为预留宽度加两个箭头宽度
							return false;
					} else {
						if (totalWidth >= centerDivWidth - 20)  // 20为预留宽度
							return false;
					}
				}
			}
			return true;
			
		},
		/**
		 * 向子项DIV数组中加入新项
		 * @private
		 */
		addItemDiv : function(div) {
			this.allItemsDivArray.push(div);
			div.data("shown",true);
		},
		/**
		 * 向子项DIV数组中删除
		 * @private
		 */
		removeItemDiv : function(div) {
			this.allItemsDivArray.removeEle(div);
			if (div.outerWidth() >= 5 && this.allItemsDivArray.length > 0) {  // 不是分隔条
				// 显示下一个
				while (this.checkWidth(this.checkArrow()) 
					&& this.allItemsDivArray[this.allItemsDivArray.length - 1].data("shown") != true)
					this.showNextItemDiv();
				if (!this.checkWidth(this.checkArrow())) {  // 隐藏多显示的
					this.hideNextItemDiv();
				} else {  // 显示上一个
					while (this.checkWidth(this.checkArrow()) 
						&& this.allItemsDivArray[0].data("shown") != true)
						this.showPreviousItemDiv();
					if (this.checkWidth(this.checkArrow()) == false)  // 隐藏多显示的
						this.hidePreviousItemDiv();
				}
			}
			if (this.allItemsDivArray[0] 
				&& this.allItemsDivArray[0].data("shown") == true 
				&& this.allItemsDivArray[this.allItemsDivArray.length - 1].data("shown"))  // 闅愯棌绠ご
				this.hideArrowDiv();
		},
		/**
		 * 显示前一个隐藏子项DIV
		 * @private
		 */
		showPreviousItemDiv : function() {
			for (var i = 0, n = this.allItemsDivArray.length; i < n; i++) {
				if (this.allItemsDivArray[i].data("shown")) {
					if (i == 0)
						return;
					else {
						this.allItemsDivArray[i-1].data("shown",true).show();
						if (this.allItemsDivArray[i-1].outerWidth() < 5)  // 分隔条
							this.showPreviousItemDiv();
						this.activeArrow();
						return;
					}
				}
			}
		},
		/**
		 * 显示后一个隐藏子项DIV
		 * @private
		 */
		showNextItemDiv : function() {
			for (var i = this.allItemsDivArray.length - 1; i >= 0; i--) {
				if (this.allItemsDivArray[i].data("shown")) {
					if (i == this.allItemsDivArray.length - 1)
						return;
					else {
						this.allItemsDivArray[i+1].data("shown",true).show();
						if (this.allItemsDivArray[i+1].outerWidth() < 5)  // 分隔条
							this.showPreviousItemDiv();
						this.activeArrow();
						return;
					}
				}
			}
		},
		/**
		 * 隐藏前一个隐藏子项DIV
		 * @private
		 */
		hidePreviousItemDiv : function() {
			for (var i = 0, n = this.allItemsDivArray.length; i < n; i++) {
				if (this.allItemsDivArray[i].data("shown")) {
					var width = this.allItemsDivArray[i].outerWidth();
					this.allItemsDivArray[i].data("shown",false).hide();
					if (width < 5)  // 分隔条
						this.hidePreviousItemDiv();
					this.activeArrow();
					return;
				}
			}
		},
		/**
		 * 隐藏后一个隐藏子项DIV
		 * @private
		 */
		hideNextItemDiv : function() {
			for (var i = this.allItemsDivArray.length - 1; i >= 0; i--) {
				if (this.allItemsDivArray[i].data("shown") == true) {
					var width = this.allItemsDivArray[i].outerWidth();
					this.allItemsDivArray[i].data("shown",false).hide();
					if (width < 5)  // 分隔条
						this.hidePreviousItemDiv();
					this.activeArrow();
					return;
				}
			}
		},
		/**
		 * 显示到第一个子项
		 * @private
		 */
		showFirstItemDiv : function() {
			while (this.allItemsDivArray[0].data("shown") == false) {
				this.leftMove();
			}
		},
		/**
		 * 显示到最后一个子项
		 * @private
		 */
		showLastItemDiv : function() {
			var length = this.allItemsDivArray.length;
			while (this.allItemsDivArray[length - 1].data("shown") == false) {
				this.rightMove();
			}
		},
		/**
		 * 校验箭头是否已经显示
		 * @private
		 */
		checkArrow : function() {
			if (this.toLeftDiv && this.toLeftDiv.attr("class") == this.baseClassName + "_toleft_div")
				return true;
			return false;
		},
		/**
		 * 显示左右箭头
		 * @private
		 */
		showArrowDiv : function() {
			if (this.checkArrow() == false) {
				var oThis = this;
				this.toLeftDiv.attr("class",this.baseClassName + "_toleft_div");
				this.toRightDiv.attr("class",this.baseClassName + "_toright_div");
				if (!this.toLeftImg || !this.toRightImg) {
					// 左箭头
					this.toLeftImg = $("<img/>").addClass(this.baseClassName + "_arrow_div").attr({
						"src" : window.themePath + "/images/toolbar/" + "toleft.gif"
					}).on("click",function(){
						oThis.leftMove();
					}).appendTo(this.toLeftDiv);
					// 右箭头
					this.toRightImg = $("<img/>").addClass(this.baseClassName + "_arrow_div").attr({
						"src" : window.themePath + "/images/toolbar/" + "toright.gif"
					}).on("click",function(){
						oThis.rightMove();
					}).appendTo(this.toRightDiv);
				}
				this.activeArrow();
			}
		},
		/**
		 * 子项向左移动一格
		 * @private
		 */
		leftMove : function() {
			if (this.allItemsDivArray[0].data("shown"))
				return;
			// 隐藏下一个
			this.hideNextItemDiv();
			// 显示上一个的数量
			var addCount = 0;
			// 显示上一个
			while (this.checkWidth(this.checkArrow()) && this.allItemsDivArray[0].data("shown") != true) {
				this.showPreviousItemDiv();
				addCount++;
			}
			if (addCount > 1)  // 隐藏多显示的
				this.hidePreviousItemDiv();
			// 显示不下则隐藏下一个
			while (this.checkWidth(this.checkArrow()) == false)
				this.hideNextItemDiv();
		},
		/**
		 * 子项向右移动一格
		 * @private
		 */
		rightMove : function() {
			if (this.allItemsDivArray[this.allItemsDivArray.length - 1].data("shown"))
				return;
			// 隐藏上一个
			this.hidePreviousItemDiv();
			// 显示下一个的数量
			var addCount = 0;
			// 显示下一个
			while (this.checkWidth(this.checkArrow()) && this.allItemsDivArray[this.allItemsDivArray.length - 1].data("shown") != true) {
				this.showNextItemDiv();
				addCount++;
			}
			if (addCount > 1)  // 隐藏多显示的
				this.hideNextItemDiv();
			// 显示不下则隐藏上一个
			while (this.checkWidth(this.checkArrow()) == false)
				this.hidePreviousItemDiv();
		},
		/**
		 * 计算并设置左右箭头的可用性
		 * @private
		 */
		activeArrow : function() {
			if (this.checkArrow() == true && this.allItemsDivArray.length > 1) {
				var length = this.allItemsDivArray.length;
				var firstItemShown = this.allItemsDivArray[0].data("shown");
				var lastItemShown = this.allItemsDivArray[length - 1].data("shown");
				this.toLeftImg.show();
				this.toRightImg.show();
				this.toRightImg.attr("src",window.themePath + "/images/toolbar/" + "toright.gif");
				this.toLeftImg.attr("src",window.themePath + "/images/toolbar/" + "toleft.gif");
				if (!firstItemShown && lastItemShown) {
//					this.toLeftImg.style.display = "block";
					this.toRightImg.hide();
					
//					this.toRightImg.src =  window.themePath + "/images/toolbar/" + "toright1.gif";
				} else if (firstItemShown && !lastItemShown) {
					this.toLeftImg.hide();
//					this.toRightImg.style.display = "block";
					
//					this.toLeftImg.src =  window.themePath + "/images/toolbar/" + "toleft1.gif";
				} else if (!firstItemShown && !lastItemShown) {
//					this.toLeftImg.style.display = "block";
//					this.toRightImg.style.display = "block";
				} else {
					this.toLeftImg.hide();
					this.toRightImg.hide();
				}
			}
		},
		/**
		 * 隐藏左右箭头
		 * @private
		 */
		hideArrowDiv : function() {
			this.activeArrow();
			this.toLeftDiv.attr("class",this.baseClassName + "_toleft_div_hidden");
			this.toRightDiv.attr("class",this.baseClassName + "_toright_div_hidden");
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			var context = new Object;
			context.c = "ToolbarContext";
			context.id = this.element.attr("id");
			// 获取子项Context
			// Button子项
			var buttonItems = this.buttonItems.values();
			if (buttonItems.length > 0) {
				context.buttonItemContexts = new Array();
				for (var i = 0, n = buttonItems.length; i < n; i++) {
					context.buttonItemContexts.push(buttonItems[i].getContext());
				}
			}

			return context;
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			// 为子项设置Context
			if (context.buttonItemContexts) {
				// Button子项
				for (var i = 0, n = context.buttonItemContexts.length; i < n; i++) {
					var btnContext = context.buttonItemContexts[i];
					var btn = this.buttonItems.get(context.buttonItemContexts[i].id);
					if (btn)
						btn.setContext(btnContext);
				}
			}
			if (context.delIds != null) {
				for (var i = 0; i < context.delIds.length; i ++) {
					var delId = context.delIds[i];
//					var btn = this.buttonItems.get(delId);
//					var withSep = btn.withSep;
//					if (withSep == true) {
//						this.removeSep(btn);
//					}
//					btn.destroySelf();
					this.removeButton(delId);
				}
			}
		}
	});
})(jQuery);
