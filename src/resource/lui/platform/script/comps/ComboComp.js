/**
 * @fileoverview 下拉框支持文字子项和图片子项两种形式
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	var ITEM_HEIGHT = 20;
	
	/**
	 * 下拉框控件构造函数
	 * @class 下拉框类，此下拉框支持文字子项和图片子项的形式
	 * 
	 * @constructor combocomp的构造函数
	 * @param parent 下拉框控件的父控件
	 * @param name 此控件的名字,是对此控件的引用
	 * @param left 控件左部x坐标
	 * @param top 控件顶部y坐标
	 * @param width 控件宽度
	 * @param position 控件的定位属性
	 * @param dataDivHeight 下拉框的高度
	 * @param readonly 输入框是否为只读项
	 * @param selectOnly 在readOnly为false的情况下,selectOnly为true表示此下拉框是仅选择项只能从下拉框中选择项
	 *                   为false表示允许在输入框输入,如果输入值在options中会自动补全用户输入,如果不在会保留该项
	 * @param className css文件的名称
	 */
	$.widget("lui.combo" , $.lui.textfield , {
		options : {
			dataType : 'A',
			selectOnly : true,
			dataDivHeight : null,
			allowExtendValue : null,
			visibleOptionsNum : null,
			multiple : false,
			multiSplitChar : ',',
			//event
			valuechanged : null
		},
		_initParam : function() {
			this.componentType = "COMBOBOX";
			this.parentOwner = this.element.parent()[0];
			this.dataDivDefaultHeight = 80;
			// 如果没有指定数据区的高度,此属性表示数据区默认显示的option数目,超过这个数目数据区将出现滚动�?
			this.defaultVisibleOptionsNum = 10;

	//		this.inputClassName_init = $.browsersupport.IS_IE7? "text_input" : "input_normal_center_bg text_input";
	//		this.inputClassName_inactive = $.browsersupport.IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
			this.inputClassName_init = "input_normal_center_bg text_input";
			this.inputClassName_inactive = "input_normal_center_bg text_input_inactive";
			//按钮图片宽高
			this.imageWidth = 19;
			this.imageHeight = 22;

			
			// 保存所有的option子项
			this.element.data("options",[]).data("type","combo");
			// 保存当前选中的值索引
			this.selectedIndex = -1;
			//保存多选的索引值
			this.multiSelectedIndex = [];
			// 保存上次选中的旧值索引
			this.lastSelectedIndex = -1;
			// 将此combo控件放入clickHolder,注册相应的动作事件
			window.clickHolders.push(this);
			// 仅显示图片时标志
			this.showImgOnly = false;
			// 已经显示的option（已调整宽度）
			this.shownOptions = new Array();
			//记录最近未找到下拉数据的
			this.notFindComboDataValue = "";
			// 可显示下拉项个数
			this.visibleOptionsNum = this.defaultVisibleOptionsNum;
			this._super();
		},
		_create : function() {
			this._super();
		},
		_managerSelf : function() {
			this._super();
			
			// 保存数据对象
			var oThis = this,opts = this.options,ele = this.element;
			var width = this.Div_text.outerWidth();
			//ipad需要再计算
		//	if ($.browsersupport.IS_IPAD){
		//		width = width - 10;
		//	}
			if (width == 0 && !$.argumentutils.isPercent(opts.width))
				width =  parseInt(opts.width);
			var inputWidth = width - this.imageWidth;
		//	if($.browsersupport.IS_IE)
		//		inputWidth -= 3;
			this.input.css({
				'width' : inputWidth - 2 + "px", //2 按钮图片
				'position' : 'relative',
				'float' : 'left'
			});
			
			if (opts.readOnly) {
				this.input.attr('readOnly',true);
			}

			if (opts.selectOnly){
				this.input.attr('readOnly',true);
			}

			this.input.off('blur').on('blur',function(e){
				// IE 下阻止失去焦点
//				if ($.browsersupport.IS_IE) {
//					if (document.activeElement && (document.activeElement.id == (ele.attr("id") + "comb_sel_button")
//							|| document.activeElement.parentNode.id == (ele.attr("id") + "comb_data_div"))) {
//						e.stopPropagation();
//						return false;
//					}
//				}
				if(oThis.Div_text.children().length == 3){
					var children = oThis.Div_text.children();
					$.each(children,function(index,item){
						$(item).attr('class',$(item).attr('class').replaceStr('input_highlight','input_normal'));
					});
					oThis.input.removeClass('input_highlight_center_bg').addClass('input_normal_center_bg');
				}
				oThis.blur(e);
				
			}).off('keydown').on('keydown',function(e){
				var ch = e.keyCode;
				// 调用用户的方向
				if (!oThis._trigger('onkeydown',e)) {
					e.stopPropagation();
					return;
				}
				// 处理键盘向上移","下移"事件
				var currIndex = oThis.getSelectedIndex();
				var nextIndex = -1;
				// 下移
				if (ch == 40) {
					if (!oThis.isShown) {
						oThis.showData();
						return;
					}
					if (currIndex < oThis.getOptions().length - 1) {
						nextIndex = currIndex + 1;
						while (nextIndex < oThis.getOptions().length && oThis.getOptions()[nextIndex].css('display') == "none")
							nextIndex++;
						if (nextIndex < oThis.getOptions().length)
							oThis.setSelectedItem(nextIndex);
						else if (nextIndex == oThis.getOptions().length)
							oThis.setSelectedItem(currIndex);
					} else if (currIndex == oThis.getOptions().length - 1)
						oThis.setSelectedItem(currIndex);	
					this.valuechanged(e,(nextIndex == -1 ? null : oThis.getOptions()[nextIndex].option('option').value),(currIndex == -1 ? null : oThis.getOptions()[currIndex].option('option').value));
				}
				// 上移
				else if (ch == 38) {
					if (currIndex > 0) {
						nextIndex = currIndex - 1;
						while (nextIndex >= 0 && oThis.getOptions()[nextIndex].css('display') == "none")
							nextIndex--;
						if (nextIndex >= 0)
							oThis.setSelectedItem(nextIndex);
					}
					// 调用用户重载的方向
					this.valuechanged(e,(nextIndex == -1 ? null : oThis.getOptions()[nextIndex].option('option').value),(currIndex == -1 ? null : oThis.getOptions()[currIndex].option('option').value));
				} else if ((ch == 9 && e.shiftKey) || ch == 9 || ch == 13) {  // Tab或Shift+Tab或Enter，隐藏选项
					oThis.hideData();
					return true;
				}
				
				e.stopPropagation();
				return false;
				
			}).off('keyup').on('keyup',function(e){
				var ch = e.keyCode;
				if (!opts.selectOnly && !oThis.showImgOnly && !opts.readOnly) {
					// 过滤
					if (ch != 38 && ch != 40 && ch != 37 && ch != 39 && ch != 9 && ch != 13) {
						oThis.isKeyPressed = true;
						// 自动打开选框
						if (!oThis.isShown && !(ch == 32 && e.ctrlKey) && ch != 20 && ch != 16 && ch != 17 && ch != 18 && ch != 33 && ch != 34 && ch != 91) {
							if (this.showDataDivTimeOut != null)
								clearTimeout(this.showDataDivTimeOut);
							this.showDataDivTimeOut = setTimeout("this.showDataDiv('" + oThis.id + "');", 300);
						}
						// 过滤
						oThis.doFilter();
					}
				}
				
			}).off('focus').on('focus',function(e){
				if(oThis.Div_text.children().length == 3){
					if(!oThis.input.attr('readOnly')){
						var children = oThis.Div_text.children();
						$.each(children,function(index,item){
							$(item).attr('class',$(item).attr('class').replaceStr('input_normal','input_highlight'));
						});
						oThis.input.removeClass('input_normal_center_bg').addClass('input_highlight_center_bg');
					}
				}
				oThis.focus(e);
				oThis.hideTip();
				
			});
			
//			// 创建显示数据区的点击按钮
//			var divButtonTop = (this.Div_text.outerHeight() - this.imageHeight)/2;
//			if(divButtonTop < 0){
//				divButtonTop = 0;
//			}
			this.divButton = $("<div></div>").attr({
				'id' : ele.attr("id") + '__comb_sel_button'
			}).addClass("combo_arrow_nm").css({
				'position' : 'absolute',
				'cursor' : 'pointer',
				'width' : this.imageWidth + 'px',
				'height' : this.imageHeight + 'px',
				'right' : '-2px',
				'top' : '1px'
			});
			
			if(this.Div_text.children().length == 3){
				this.Div_text.children().eq(1).append(this.divButton);
			}else{
				this.Div_text.append(this.divButton);
			}

			
			if(this.Div_text.children().length == 3){
				var centerWidth = width - 3*2;//3*2左右边框图片宽度
				this.Div_text.children().eq(1).css('width',centerWidth + 'px');
				var imgWidth = (this.Div_text.children().eq(1).children().length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
				var inputWidth = centerWidth - imgWidth;
//				if($.browsersupport.IS_IE){
//					inputWidth -= 3;
//				}
				this.input.css('width',inputWidth - 2 + 'px');//2 按钮图片右间距
			}
			
			var divButtonClickHandler = function(e) {
				oThis.click(e);
			};
			
			var divButtonMouseoverHandler = function(e) {
				$(e.target).removeClass().addClass('combo_arrow_on');
			};
			
			var divButtonMouseoutHandler = function(e) {
				$(e.target).removeClass().addClass('combo_arrow_nm');
			};
			this.divButton.data('clickEvent',divButtonClickHandler)
				.data('mouseoverEvent',divButtonMouseoverHandler)
				.data('mouseoutEvent',divButtonMouseoutHandler);
			
			if (!opts.readOnly) {
				// 点击下拉按扭后的动作
				this.divButton.off("click").on('click',function(e){
					divButtonClickHandler(e);
				}).off("mouseover").on('mouseover',function(e){
					divButtonMouseoverHandler(e);
				}).off("mouseout").on('mouseout',function(e){
					divButtonMouseoutHandler(e);
				});

			} else {
				this.Div_text.attr('class',opts.className + " " + opts.className + "_readonly");
				this.divButton.css('visibility','hidden');
			}
			
			function activeComboStyle() {
				var textDivChildren = oThis.Div_text.children();
				textDivChildren.eq(0).attr('class','combo_active_left_bg');
				textDivChildren.eq(1).attr('class','combo_active_center_div_bg');
				textDivChildren.eq(2).attr('class','combo_active_right_bg');
				oThis.input.removeClass('input_normal_center_bg').addClass('combo_active_center_div_bg');
			}

			this.comboDiv = $("<div></div>").addClass('combo_div');
			
			$("<div></div>").addClass('combo_center_top_div').appendTo(this.comboDiv);
			$("<div></div>").addClass('combo_left_center_div').appendTo(this.comboDiv);
			
			this.comboCenterDiv = $("<div></div>").addClass('combo_center_div').appendTo(this.comboDiv);
			
			$("<div></div>").addClass('combo_right_center_div').appendTo(this.comboDiv);
			$("<div></div>").addClass('combo_center_bottom_div').appendTo(this.comboDiv);
			
			// 创建显示数据的下拉区
			this.dataDiv = $("<div></div>").attr({
				'id' : ele.attr("id") + 'comb_data_div'
			}).addClass('combobox_data_div').css({
				'z-index' : $.measures.getZIndex(),
				'width' : (opts.width == null ? '120px' : opts.width),
				'overflow' : 'auto',
				'position' : 'absolute'
			}).hide().appendTo('body').append(this.comboDiv);
			
//			this.divButtonClickFunc = divButtonClickHandler;
//			this.divButtonMouseOutFuc = divButtonMouseoverHandler;
//			this.divButtonMouseOverFuc = divButtonMouseoutHandler;
			
			// 阻止IE中滚轮的事件传播 
			this.dataDiv.get(0).onmousewheel = function(e) {
				e.stopPropagation();
			};
		},
		/**
		 * 聚焦后执行方法
		 * @private
		 */
		focus : function(e) {
			this.oldValue = this.getValue();
			// 为避免tab键进入密码框时不能输入字符的bug
			if (this.visible) {
				this.isKeyPressed = false;
				this._trigger("onfocus",e);
			}
		},
		valuechanged : function(e,oldvalue,newvalue) {
			this._trigger("valueChanged", e, {oldValue: oldvalue, newValue: newvalue});
        	if(this.formular) {
        		execFormula(this.viewpart.id,null,this.element.attr('id'),this.extendOpts);
        	}
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			this.options.readOnly=readOnly;
			this.input.attr('readOnly',readOnly);
			if (readOnly) {
				this.Div_text.attr('class',this.options.className + " " + this.options.className + "_readonly").children().css('background-color','#E1E1E1');
				this.input.attr('class',this.inputClassName_init + " text_input_readonly");
				this.divButton.attr('class','combo_arrow_nm');
			} else {
				this.Div_text.attr('class',this.options.className).children().css('background-color','#FFFFFF');
				this.input.attr('class',this.inputClassName_init);
				this.divButton.attr('class','combo_arrow_nm');
			}
			// 控件处于激活状态变为非激活状态
			if (readOnly) {
				// 将与下拉按钮的各种动作事件解除绑定
				this.divButton.off('click mouseout mouseover').css('cursor','default');
			}
			else  {	// 控件处于禁用状态变为激活状态
				this.divButton.off('click').on('click',function(e){$(this).data('clickEvent')(e);})
					.off('mouseout').on('mouseout',function(e){$(this).data('mouseoutEvent')(e);})
					.off('mouseover').on('mouseover',function(e){$(this).data('mouseoverEvent')(e);})
					.css('cursor','pointer');
			}
			this.ctxChanged = true;
		},
		/**
		 * 根据用户输入在输入框失去焦点时自动帮助用户补全剩余部分如果用户输入的值在options中没 有则不设置该项
		 * 
		 * @param private 私有方法,程序内部方法.
		 * @private
		 */
		blur : function(e) {
			// 整体可以编辑
			if (!this.options.readOnly) {
				// 仅对文字的下拉框有用
				if (!this.showImgOnly && !this.options.selectOnly) {
					var inputCaption = this.input.val();
					var options = this.getOptions();
					// 用户输入值为空则置空
					if (inputCaption == null || inputCaption.trim() == "")
						this.setNullValue(true);
					else {
						// 完全匹配则设置值或者按照最先匹配原则帮助用户设置
						var matched = false;
						for ( var i = 0; i < options.length; i++) {
							if (options[i].option("option").caption == inputCaption){
								this.setSelectedItem(i);
								matched = true;
								break;
							}
						}
						if (!matched){
							for ( var i = 0; i < options.length; i++) {
								if (options[i].option("option").caption == inputCaption
										|| (options[i].option("option").caption).startWith(inputCaption)) {
									this.setSelectedItem(i);
									break;
								} else {
									if (this.options.allowExtendValue)  // 允许存在范围外
										this.selectedIndex = -1;
									else  // 不允许存在范围外
										this.setNullValue(true);
								}
							}
						}
					}
					this.isKeyPressed = false;
				}
			}
			if (this.visible) {
				if(this.options.showTipMessage && this.options.showTipMessage != null)
					this.setMessage(this.options.showTipMessage);
				else
					this.setMessage(this.input.val());
			}
			// 调用用户的处理方法
			this._trigger("onblur",e);
		},
		setMessage : function(val) {
			this._super(val);
		},
		/**
		 * 由option组件调用这个函数
		 * 
		 * @param item 被点击的option的数据对象
		 * @private
		 */
		itemclick : function(index) {
			// 只有选则项和选中的项不相同时才调用valueChanged()
			if (this.options.multiple) {
				this.setSelectedItem(index);
			} else {
				if (index != this.selectedIndex) {
					this.setSelectedItem(index);
				}
				this.hideData();
				this.setFocus();
			}
			
			
		},
		/**
		 * 创建option子项
		 * 
		 * @param caption 子项的显示值
		 * @param value 子项的真实值
		 * @param refImg showImgOnly为false表示要显示在文字前面的图片的绝对路径
		 * @param selected 设置是否为初始选中项
		 * @param index 索引值一般初始时不用指定
		 * @param showImgOnly 是否为只显示图片的combox类型
		 */
		createOption : function(caption, value, refImg, selected,
				index, showImgOnly) {
			if (index == null || index == -1) {
				var option = $("<div id='"+value+"'></div>").option({
					caption : caption,
					value : value,
					refImg : refImg,
					selected : selected,
					showImgOnly : showImgOnly
				}).option("setParentOwner",this.element);
				var index = this.getOptions().push(option);
				option.option("setIndex",index - 1);
				this.comboCenterDiv.append(option);
				if (selected)
					this.setSelectedItem(index - 1);
//				else
//					this.setSelectedItem(0);
			}
		},
		/**
		 * 重写父类的setBounds方法
		 * 
		 * @param dataDivHeight 下拉数据区的高度
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options,ele = this.element;
			opts.left = left;
			opts.top = top;
			opts.width = $.argumentutils.getString($.measures.convertWidth(width), ele.outerWidth() + "px");
			opts.height = $.argumentutils.getString($.measures.convertHeight(height), ele.outerHeight() + "px");
			
			// 设置最外层
			ele.css({
				'left' : left + "px",
				'top' : top + "px",
				'width' : opts.width,
				'height' : opts.height
			});
			
			var tempWidth = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempWidth = ele.outerWidth();
			else
				tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);
			
			// 设置输入区域的大小
			this.Div_text.css('width',tempWidth - 4 + "px");
			if (this.hasLabel)
				this.Div_text.css('width',tempWidth - opts.labelWidth - 10 + "px");

			var pixelHeight = this.Div_text.outerHeight();
			var pixelWidth = this.Div_text.outerWidth();
			
//			if ($.browsersupport.IS_IPAD){
//				pixelWidth = pixelWidth - 10;
//			}
			this.input.css('width',pixelWidth - this.imageWidth + "px");
			
			if(this.Div_text.children().length == 3){
				var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
				this.Div_text.children().eq(1).css('width',centerWidth + "px");
				var imgWidth = (this.Div_text.children().eq(1).children().length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
				var inputWidth = centerWidth - imgWidth;
//				if($.browsersupport.IS_IE){
//					inputWidth -= 3;
//				}
				this.input.css('width',inputWidth - 2 + "px");//2 按钮图片右间距
			}
		},
		/**
		 * 显示控件(显示属性是visibility)
		 */
		showV : function() {
			this.element.css('visibility','');
			this.visible = true;
			this.ctxChanged = true;
		},
		/**
		 * 隐藏控件(显示属性是visibility)
		 */
		hideV : function() {
			this._super();
			this.hideData();
			this.ctxChanged = true;
		},
		/**
		 * combobox控件点击事件
		 * 
		 * @private
		 */
		click: function(e) {
			if (!this.isShown) {
				this.showData();
				this.isShown = true;
			} else {
				this.hideData();
				this.isShown = false;
			}
			
			if (this.stopHideDiv != true) {
				window.clickHolders.trigger = this;
				document.onclick();
			}
			e.stopPropagation();
		},
		/**
		 * 显示选项DIV(setTimeOut)
		 */
		showDataDiv : function(id) {
			var combo = window.objects[id];
			if (combo != null)
				combo.showData();
		},
		/**
		 * 显示选项DIV
		 */
		showData : function() {
			
			if (!this.isShown) {
				this.dataDiv.css({
					'left' : this.Div_text.offset().left + "px",
					'top' : (this.Div_text.offset().top + this.Div_text.outerHeight()) + "px",
					'z-index' : $.measures.getZIndex(),
					'width' : this.Div_text.outerWidth() + "px"
				}).show();
				// 数据区宽度和输入框宽度要相等
				if (this.Div_text.outerWidth() == 0) {
					this.dataDiv.css('width',this.options.width == null ? '120px' : this.options.width);			
				}
				if (this.dataDivHeight != null)
					this.dataDiv.css('height', this.dataDivHeight + "px");
				else {
					if (this.getOptions().length <= this.visibleOptionsNum)
						this.dataDiv.css('height',(this.getOptions().length * ITEM_HEIGHT + 11*2 + 1) + "px");
					else{
						this.dataDiv.css('height',(this.visibleOptionsNum * ITEM_HEIGHT + 11*2) + "px");
					}
				}
				this.comboDiv.css('height',(this.getOptions().length * ITEM_HEIGHT) + "px");//11*2背景上下边宽度和
			
				// 将控件放在视线内
				$.measures.positionElementInView(this.dataDiv.get(0));
				
				//TODO IE6和IE7在option的div中出现竖向滚动条时，其option项div宽度计算有问题，会相应出现横向滚动条
//				if ($.browsersupport.IS_IE6 || $.browsersupport.IS_IE7) {
//					for (var i = 0, n = this.getOptions().length; i < n; i++) {
//						if (this.shownOptions.indexOf(this.getOptions()[i]) == -1) {  // 该选项没有被显示过
//							this.getOptions()[i].css('width',this.getOptions()[i].outerWidth() - this.SCROLLWIDTH + "px");
//							this.shownOptions.push(this.getOptions()[i]);
//						}
//					}
//				}
				this.isShown = true;
				if (!this.options.selectOnly && !this.showImgOnly && !this.options.readOnly) {
					if (this.isKeyPressed) {  // 如果打开之前输入了内容，则进行过滤
						this.doFilter();
					} else {  // 打开之前没有输入内容，显示全部选项
						for (var i = 0, n = this.getOptions().length; i < n; i++) {
							this.getOptions()[i].show();
						}
					}
				}

				// 重新设置所有选项的选中样式
				this.resetSelStyle();
			}
		},
		/**
		 * 得到所有的option项
		 */
		getOptions : function() {
			if (this.element.data("options") != null)
				return this.element.data("options");
			else
				return null;
		},
		/**
		 * 清除所有的option
		 */
		clearOptions : function() {
			if (this.getOptions() == null)
				return;
			//this.dataDiv.innerHTML = "";
			this.comboCenterDiv.empty();

			this.element.data("options",[]);
			if(this.options.multiple) {
				this.multiSelectedIndex = [];
			} else {
				this.selectedIndex = -1;
			}
			
			this.input.val('');
			this.shownOptions.clear();
		},
		/**
		 * 清除参数指定的option
		 * 
		 * @param value option的真实的值
		 */
		clearOption : function(value) {
			if (value == null || value == "")
				return;

			var options = this.getOptions();
			if (options != null && options.length > 0) {
				for ( var i = 0; i < options.length; i++) {
					if (options[i].option("option").value == value) {
						//this.dataDiv.removeChild(options[i].Div_gen);
						options[i].remove();//this.comboCenterDiv.removeChild(options[i].Div_gen);
						this.getOptions().splice(i, 0, 1);
						this.shownOptions.removeEle(options[i]);
						return;
					}
				}
			}
		},
		getComboData : function() {
			return this.comboData;
		},
		/**
		 * 设值绑定数值
		 */
		setComboData : function(comboData,updateDs) {
			var oldValue = (this.getValue()==null && this.notFindComboDataValue != "")?this.notFindComboDataValue:this.getValue();
			this.clearOptions();
			if (!comboData)
				return;
			this.comboData = comboData;
			var nameArr = comboData.getNameArray();
			var valueArr = comboData.getValueArray();
			var imageArr = comboData.getImageArray();
			if (nameArr != null) {
				for ( var i = 0; i < nameArr.length; i++) {
					var selected = false;
					this.createOption(nameArr[i], valueArr[i], imageArr[i], selected, -1,
							this.showImgOnly);
				}
			}
			//如果oldValue本来就是空值，不需要再设置一遍空值
			if (oldValue != null)
				this.setValue(oldValue, false);
		},
		/**
		 * 设值提示信息
		 */
		setTitle : function(title) {
			if (!this.isError) {
				if (title != null && title != "") {
					var titleName;
					if (this.comboData)
						titleName = this.comboData.getNameByValue(title);
					if (titleName != null && titleName != "") {
						this.element.attr('title',titleName);
						if (this.input!=null)
							this.input.attr('title',titleName);
					} else {
						this.element.attr('title',title);
						if (this.input!=null)
							this.input.attr('title',title);
					}
				}
			} 
			else {
				if (title != null && title != "") {
					if (this.input!=null)
						this.input.attr('title',title);
					this.element.attr('title',title);
				} else if (title == "") {
					if (this.input!=null)
						this.input.attr('title','');
					this.element.attr('title','');
				}
			}
		},
		/**
		 * 根据输入内容过滤选择条件
		 */
		doFilter : function() {
			if (this.isShown) {
				var inputCaption = this.input.val();
				if (inputCaption == "") {
					for (var i = 0, n = this.getOptions().length; i < n; i++) {
						this.options[i].show();
					}
				} else {
					for (var i = 0, n = this.getOptions().length; i < n; i++) {
						if (this.options[i].option("option").caption == inputCaption 
								|| (this.options[i].option("option").caption).startWith(inputCaption)) {
							this.options[i].show();
						} else {
							this.options[i].hide();
						}
					}
				}
			}
		},
		/**
		 * 设置选中项
		 * 
		 * @param index 要设置选中项的索引项index小于0表示不设置任何option为选中状态
		 */
		setSelectedItem : function(index) {
			index = parseInt(index);
			var options = this.getOptions();
			if (isNaN(index) || index > options.length)
				return;

			// index小于0表示不设置任何option为选中状态
			if (index < 0) {
				this.setNullValue(true);
				return;
			}

			var oldSelectedIndex = null;
			var option = options[index];
			if(this.options.multiple) {
				oldSelectedIndex = this.multiSelectedIndex;
				var optIndex = option.option("getIndex");
				if(this.multiSelectedIndex.indexOf(optIndex)>=0) {
					this.multiSelectedIndex.removeEle(optIndex);
				} else {
					this.multiSelectedIndex.push(optIndex);
				}
			} else {
				oldSelectedIndex = this.selectedIndex;
				if(this.selectedIndex == option.option("getIndex") && this.input.val() == option.option('option').caption)
					return;
				this.selectedIndex = option.option("getIndex");
			}
			

			// 仅显示图片的combo类型
			if (option.option("option").showImgOnly) {
				this.input.css({
					'background-image' : "url(" + option.option("option").caption + ")",
					'background-repeat' : 'no-repeat',
					'background-position' : 'center'
				});
				this.input.attr('class',this.inputClassName_init);//"text_input";
				if (this.options.disabled)
					this.input.attr('class',this.inputClassName_init + " text_inactive_bgcolor");//"text_input text_inactive_bgcolor";
			}
			// 仅显示文字的combo类型
			else {
				if(this.options.multiple) {
					this.input.val(this.getMulti(this.multiSelectedIndex,'caption'));
					this.oldValue = this.getMulti(this.multiSelectedIndex,'value');
				} else {
					this.input.val(option.option("option").caption);
					this.oldValue = option.option("option").value;
				}
			}
			
			// 重新设置所有选项的选中样式
			if (this.isShown)
				this.resetSelStyle();

			// 选中项改变调用用户重载的方法
			if(this.options.multiple) {
				this.ctxChanged = true;
				this.valuechanged(null,this.getMulti(oldSelectedIndex,'value'),this.oldValue);
			} else {
				if (index != oldSelectedIndex){
					this.ctxChanged = true;
					this.valuechanged(null,
						(options[oldSelectedIndex] == null ? null : options[oldSelectedIndex].option("option").value),
						(option == null ? null : option.option("option").value));
				}
			}
		},
		getMulti : function(indexArray,prop) {
			if(indexArray) {
				var multiValue = [];
				var options = this.getOptions();
				$.each(indexArray, function(index,item){
					multiValue.push(options[item].option("option")[prop]);
				});
				return multiValue.join(this.options.multiSplitChar);
			}
			return "";
		},
		/**
		 * 重新设置所有选项的选中样式
		 * @private
		 */
		resetSelStyle : function() {
			if(this.options.multiple) {
				var options = this.getOptions();
				for (var i = 0, n = options.length; i < n; i++) {
					if (this.multiSelectedIndex.indexOf(i)>=0)
						options[i].option("setSelStyle");
					else
						options[i].option("setUnSelStyle");
				}
			} else {
				var options = this.getOptions();
				for (var i = 0, n = options.length; i < n; i++) {
					if (this.selectedIndex == i)
						options[i].option("setSelStyle");
					else
						options[i].option("setUnSelStyle");
				}
			}
			
		},
		/**
		 * 设置value,如果value不是任何一个options的value,则设置为空
		 * 
		 * @param value 要设置的值
		 * @param updateDs 当没匹配上comboData时，是否清空ds
		 */
		setValue : function(value,updateDs) {
			var options = this.getOptions();
			var sendValueChanged = updateDs == null ? true : updateDs; 
			if (value == null) {
				this.setNullValue(true);
				return;
			}
			if (this.options.readOnly) {
				if (this.setValueByOption(value) != true){
					this.notFindComboDataValue = value;
					//如果下拉项为空，不触发valueChange
					if (options.length > 0 && sendValueChanged)
						this.setNullValue(true);
					else	
						this.setNullValue(false);
				}
			} 
			else {
				if (this.options.selectOnly) {
					if (this.setValueByOption(value) != true){
						this.notFindComboDataValue = value;
						//如果下拉项为空，不触发valueChange
						if (options.length > 0  && sendValueChanged)
							this.setNullValue(true);
						else	
							this.setNullValue(false);
					}
				} 
				else{
//					if (value == null) {
//						this.setNullValue();
//						return;
//					}
					if (this.setValueByOption(value) != true){
						this.input.val(value);
						this.notFindComboDataValue = "";
					}
				}
			}
			this.ctxChanged = true;
		},
		/**
		 * 设置空值即不设置任何option选中
		 * 
		 * param {} isValueChanged 是否触发 valueChange事件
		 * 
		 * @private
		 */
		setNullValue : function(isValueChanged) {
			var oldValue = this.getValue();
			this.selectedIndex = -1;
			this.multiSelectedIndex = [];
			if (this.input.val() != ""){
				this.input.val('');
			}
			if (isValueChanged)
				this.valuechanged(null,null,oldValue);
		},
		/**
		 * @private
		 */
		setValueByOption : function(value) {
			var options = this.getOptions();
			var multiFlag = false;
			for ( var i = 0; i < options.length; i++) {
				if(this.options.multiple) {
					var multiValueArray = value.split(this.options.multiSplitChar);
					if(multiValueArray.indexOf(options[i].option("option").value)>=0) {
						this.setSelectedItem(i);
						multiFlag = true;
					}
				} else {
					if (value == options[i].option("option").value) {
						this.setSelectedItem(i);
						this.notFindComboDataValue = "";
						return true;
					}
				}
				
			}
			if(multiFlag) {
				this.notFindComboDataValue = "";
				return true;
			}
		},
		/**
		 * 得到选择项的value值
		 */
		getValue : function() {
			return this.getSelectedValue();
		},
		/**
		 * 设置下拉框为只显示图片的下拉框
		 * 
		 * @param show true为仅显示图片
		 */
		setShowImgOnly : function(show) {
			this.showImgOnly = show;
		},
		/**
		 * 得到指定value的索引值
		 * 
		 * @param value 指定的value
		 * @private
		 */
		getValueIndex : function(value) {
			var options = this.getOptions();
			if (value != null) {
				if(this.options.multiple) {
					var multiValueArray = value.split(this.options.multiSplitChar);
					var multiValueIndexArray = [];
					for ( var i = 0; i < options.length; i++) {
						if(multiValueArray.indexOf(options[i].option("option").value)>=0) {
							multiValueIndexArray.push(i);
						}
					}
					return multiValueIndexArray;
				} else {
					for ( var i = 0; i < options.length; i++) {
						if (options[i].option("option").value == value)
							return i;
					}
				}
				
			}
			return -1;
		},
		/**
		 * 得到选中的option的index值
		 */
		getSelectedIndex : function() {
			return this.selectedIndex;
		},
		/**
		 * 得到选中的option的真实值
		 * 该方法得到的是真实值而非显示值输入框中的项。但对于readOnly为false，并且selectOnly也为false的情况返回用户在输入框中输入的值
		 */
		getSelectedValue : function() {
			var options = this.getOptions();
			if (this.options.readOnly) {
				if ((!this.options.multiple && this.selectedIndex == -1) || (this.options.multiple && this.multiSelectedIndex == []))
					return null;
				if(this.options.multiple) {
					return this.getMulti(this.multiSelectedIndex,"value");
				} else {
					return options[this.selectedIndex].option("option").value;
				}
			} 
			else {
				if (this.options.selectOnly) {
					if ((!this.options.multiple && this.selectedIndex == -1) || (this.options.multiple && this.multiSelectedIndex == []))
						return null;
					if(this.options.multiple) {
						return this.getMulti(this.multiSelectedIndex,"value");
					} else {
						return options[this.selectedIndex].option("option").value;
					}
				} 
				else {
					if ((!this.options.multiple && this.selectedIndex == -1) || (this.options.multiple && this.multiSelectedIndex == [])){
						if (this.input.val() == "")
							return null;
						else
							return this.input.val();
					}
					if(this.options.multiple) {
						return this.getMulti(this.multiSelectedIndex,"value");
					} else {
						return options[this.selectedIndex].option("option").value;
					}
				}
			}
		},
		isSelectOption : function(option) {
			if(this.options.multiple) {
				return this.multiSelectedIndex.indexOf(option.option("getIndex"))>=0;
			} else {
				return this.selectedIndex == option.option("getIndex");
			}
		},
		/**
		 * 得到选择的option的caption
		 */
		getSelectedCaption : function() {
			if (this.selectedIndex == -1)
				return null;
			if(this.options.multiple) {
				return this.getMulti(this.selectedIndex,"caption");
			} else {
				return options[this.selectedIndex].option("option").caption;
			}
		},
		/**
		 * 外部鼠标滚轮滑动时的回掉函数.当滑动鼠标滚轮时隐藏掉datadiv
		 * @private
		 */
		outsideMouseWheelClick : function() {
			this.hideData();
		},
		/**
		 * 外部右键点击事件发生时的回调函数.用来隐藏下拉框数据部分
		 * @private
		 */
		outsideContextMenuClick : function() {
			this.hideData();
		},
		/**
		 * 外部点击事件发生时的回调函数。用来隐藏下拉框数据部分
		 * @private
		 */
		outsideClick : function() {
			if (window.clickHolders.trigger == this)
				return;
			this.hideData();
		},
		/**
		 * 设置此输入框控件的激活状态
		 * 
		 * @param isActive true表示处于激活状态否则表示禁用状态
		 */
		setActive : function(isActive) {
			var isActive = $.argumentutils.getBoolean(isActive, false);
			this.setReadOnly(!isActive);
			this.ctxChanged = true;
		},
		/**
		 * 隐藏data list
		 */
		hideData : function() {
			if (this.dataDiv.size()>0) {
				this.dataDiv.hide();
				this.isShown = false;
			}
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			if(!this.ctxChanged)
				return null;
			return {
				c : "ComboBoxContext",
				id : this.element.attr("id"),
				enabled : !this.options.disabled,
				value : this.getValue(),
				visible : this.visible,
				readOnly : this.options.readOnly
			};
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.readOnly != null && this.options.readOnly != context.readOnly)
				this.setReadOnly(context.readOnly);
			if (context.visible != this.visible) {
				if (context.visible)
					this.showV();
				else
					this.hideV();
			}
			if(!context.isform)
				this.setValue(context.value);
			this.ctxChanged = false;
		}
	});
	
	/**
	 * 选项控件构造函数
	 * @class 选项控件
	 * @param caption 显示值(showImgOnly为true时,capion为要显示图片的绝对路径)
	 * @param value 真实值
	 * @param refImg 显示图片的绝对路径
	 * @param selected 初始是否选中
	 * @param showImgOnly 此项若为true表示此option仅显示图片,此时caption值代表图片路径
	 */
	$.widget("lui.option", {
		options : {
			caption : '',
			value : null,
			refImg : '',
			selected : false,
			showImgOnly : false
		},
		_initParam : function() {
			ITEM_HEIGHT = 20;
		},
		_create : function() {
			this._initParam();
			var oThis = this, opts = this.options, ele = this.element;
			
			// 每个option项的总体div
			ele.attr({
				'title' : opts.caption
			}).css({
				'width' : '100%',
				'height' : ITEM_HEIGHT + "px",
				'overflow' : 'hidden',
				'position' : 'relative'
			}).addClass('option_unsel');
			this.index = -1;
			
			// 处理仅需显示图片的情况
			if (opts.showImgOnly) {
				var img = "<img src='" + opts.caption + "' width='16px' height='16px'/>";
				var table = "<table width='100%' height='" + ITEM_HEIGHT + "px'><tr><td align='center' valign='middle'>" + img +"</td></tr></table>";
				ele.html(table);
				
				// 鼠标移动到子项上的事件处理
				ele.on('mouseover',function(){
					if (oThis.parentOwner.data("type")=="combo") {
						var options = oThis.parentOwner.data("options");
						for (var i = 0, n = options.length; i < n; i++) {
							if (options[i][0] == ele[0])
								options[i].option("setSelStyle");
							else {
								if(!oThis.parentOwner.combo('isSelectOption',options[i])) {
									options[i].option("setUnSelStyle");
								}
							}
						}
					} else
						oThis.setSelStyle();
					
				}).on('mouseout',function(){
					if (oThis.parentOwner.data("type")=="combo") {
						if(!oThis.parentOwner.combo('isSelectOption',ele)) {
							oThis.setUnSelStyle();
						}
						
					}
				}).on('click',function(e){
							
					if (!oThis.parentOwner)
						alert("parent container is null, may be \r\nthis item is not create through method\r\n\"ComboComp.prototype.createOption\"");
						
					// combox的item子项被点击
					if (oThis.parentOwner.data("type")=="combo") {
						if(oThis.parentOwner.combo('isSelectOption',ele)) {
							oThis.setUnSelStyle();
						} else {
							oThis.setSelStyle();
						}
						oThis.parentOwner.combo("itemclick",oThis.index);
					}
					e.stopPropagation();
				});
				
			}
			// 显示文字的情况
			else {
				//创建显示在文字前的图片
				if ($.argumentutils.isNotNull(opts.refImg, false))  {
					this.divImg = $("<div></div>").css({
						'position' : 'absolute',
						'left' : '0px',
						'width' : this.IMAGE_WIDTH - 4 + "px",
						'height' : this.IMAGE_WIDTH - 4 + "px"
					}).appendTo(ele);
					
					this.img = $("<img/>").attr({
						'src' : window.themePath + opts.refImg
					}).css({
						'width' : this.IMAGE_WIDTH - 4 + "px",
						'height' : this.IMAGE_WIDTH - 4 + "px"
					}).appendTo(this.divImg);
				}
				
				var captionLeft = '';
				if ($.argumentutils.isNotNull(opts.refImg, false))
					captionLeft = ITEM_HEIGHT + "px";
				else
					captionLeft = "5px";
				
				
				// 创建显示文字的div
				this.divCaption = $("<div></div>").css({
					'position' : 'absolute',
					'white-space' : 'nowrap',
					'text-overflow' : 'ellipsis',
					'overflow' : 'hidden',
					'left' : captionLeft,
					'line-height' : ITEM_HEIGHT + "px"
				}).html(opts.caption).appendTo(ele);
				
				// 鼠标移动到子项上的事件处理
				ele.on('mouseover',function(){
					if (oThis.parentOwner.data("type")=="combo") {
						var options = oThis.parentOwner.data("options");
						for (var i = 0, n = options.length; i < n; i++) {
							if (options[i][0] == ele[0])
								options[i].option("setSelStyle");
							else {
								if(!oThis.parentOwner.combo('isSelectOption',options[i])) {
									options[i].option("setUnSelStyle");
								}
							}
						}
					} else
						oThis.setSelStyle();
						
				}).on('mouseout',function(){
					// list和listToList控件外观的变换
					if (oThis.parentOwner.data("type")=="list") {
						if (oThis.parentOwner.data("selectedItems")!= null && (oThis.parentOwner.data("selectedItems")).indexOf(oThis) != -1)
							opts.className = "option_click";
						else
							oThis.setUnSelStyle();
					} else if (oThis.parentOwner.data("type")=="combo") {
						if(!oThis.parentOwner.combo('isSelectOption',ele)) {
							oThis.setUnSelStyle();
						}
					}
				}).on('click',function(e){
						
					if (!oThis.parentOwner)
						alert("parent container is null, may be \r\nthis item is not create through method\r\n\"ComboComp.prototype.createOption\"");
					
					// combox的item子项被点击
					if (oThis.parentOwner.data("type")=="combo") {
						if(oThis.parentOwner.combo('isSelectOption',ele)) {
							oThis.setUnSelStyle();
						} else {
							oThis.setSelStyle();
						}
						oThis.parentOwner.combo("itemclick",oThis.index);
					}
					// list的item子项被点击
					else {
						// 如果是ctrl按钮按下,则处理多选
						if (oThis.parentOwner.data("multiSel")) {	
							if (e.ctrlKey)
								oThis.parentOwner.list('addItemSelected',oThis.index);
							else
								oThis.parentOwner.list('setItemSelected',oThis.index);
						} else {
							var ds = oThis.parentOwner.data("dataset");
							// 绑定ds
							if (ds != null) {
								ds.setRowSelected(ds.getRowIndex(oThis.optionData));	
							}
							else
								oThis.parentOwner.list('setItemSelected',oThis.index);
						}	
					}
					e.stopPropagation();
				});
				
			}
		},
		/**
		 * 设置选中时样式
		 * @private
		 */
		setSelStyle : function() {
			this.element.attr('class','option_sel');
		},
		/**
		 * 设置非选中时样式
		 * @private
		 */
		setUnSelStyle : function() {
			this.element.attr('class','option_unsel');
		},
		/**
		 * 设置optioncomp的index值
		 * @param private 在ComboComp.createOption()方法中内部调用了此方法
		 * @private
		 */
		setIndex : function(index) {
			this.index = index;
		},
		getIndex : function() {
			return this.index;
		},
		/**
		 * 设置所属父控件
		 * @private
		 */
		setParentOwner : function(parentOwner) {
			this.parentOwner = parentOwner;
		},
		/**
		 * 得到option的显示对象
		 */
		getObjHtml : function() {
			return this.element;
		}
		
	});	
	
})(jQuery);
