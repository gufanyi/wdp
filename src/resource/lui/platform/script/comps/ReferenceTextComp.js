/**
 * @fileoverview 参照控件,此参照控件走WebFrame参照方式
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 参照输入框构造函数
	 * @class 参照输入框
	 */
	$.widget("lui.reference" , $.lui.textfield , {
		options : {
			dataType : 'R',
			isShowLine : true,
			refFormId : null,
			refFormeleID : null,
			refType : null,
			nodeInfo : null,
			//event
			onclick : null,
			beforeopenrefpage : null
		},
		_initParam : function() {
			this.componentType = "REFERENCETEXT";
			//参照对话框标识
			this.DIALOG_INDEX = 0;

//			this.inputClassName_init = $.browsersupport.IS_IE7? "text_input" : "input_normal_center_bg text_input";
//			this.inputClassName_inactive = $.browsersupport.IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
			this.inputClassName_init = "input_normal_center_bg text_input";
			this.inputClassName_inactive = "input_normal_center_bg text_input_inactive";
			//按钮图片宽高
			this.imageWidth = 19;
			this.imageHeight = 22;

			this.DEFAULT = 0;
			this.TREE = 1;
			this.GRID = 2;
			this.GRIDTREE = 3;
			this.TREETREE = 4;

			//TODO 暂时写在这里
			this.PAGEWIDTH = 1500;
			this.PAGEHEIGHT = 1500;
			
			this.refCodeName = null;
			if(this.options.refType){
				this.options.refType = $.argumentutils.getInteger(this.options.refType, this.DEFAULT);
			}
			else{
				if(this.options.nodeInfo)
					this.options.refType = $.argumentutils.getInteger(this.options.nodeInfo.refType, this.DEFAULT);
			}
			this.showValue = null;
			this.trueValue = null;
			if(this.options.nodeInfo != null)
				this.options.nodeInfo.bindReference(this);
			this._super();
			//设置input框是否只读
			if(this.options.nodeInfo && this.options.nodeInfo.isRead){
				this.options.readOnly = true;
			}else{
				this.options.readOnly = false;
			}
			//默认以弹出框打开，设为为false的话，已下拉框打开
			this.isDialog = true;
			
			this.refIndex = "ref_" + (this.DIALOG_INDEX ++);
			this.refresh = false;

			this.dialogWidth = 800;
			this.divWidth = 600;
			if(this.options.refType == this.TREE){
				this.dialogWidth = 36 + 310;//36左右图片宽度和
				this.divWidth = 36 + 310;
			}else if(this.options.refType == this.GRID){
				this.dialogWidth = 650;
				this.divWidth = 36 + 618;
			}else if(this.options.refType == this.GRIDTREE){
				this.dialogWidth = 36 + 240 + 525;
				this.divWidth = 36 + 240 + 525;
			}else if(this.options.refType == this.TREETREE){
				this.dialogWidth = 36 + 240 + 240 + 80 + 4;
				this.divWidth = 36 + 240 + 240 + 80 + 4;
			}
			if(this.dialogWidth >= this.PAGEWIDTH)
				this.dialogWidth = this.PAGEWIDTH - 100;

			this.dialogHeight = 460;
			this.divHeight = 460;
			if(this.options.refType == this.TREE){
				this.dialogHeight = 48 + 412 + 45;//36左右图片宽度和
				this.divHeight = 48 + 412 + 45;
			}else if(this.options.refType == this.GRID){
				this.dialogHeight = 48 + 300 + 45;
				this.divHeight = 48 + 422 + 45;
			}else if(this.options.refType == this.GRIDTREE){
				this.dialogHeight = 48 + 422 + 45;
				this.divHeight = 48 + 422 + 45;
			}else if(this.options.refType == this.TREETREE){
				this.dialogHeight = 48 + 400 + 20 + 45;
				this.divHeight = 48 + 400 + 20 + 45;
			}
			if(this.dialogWidth >= this.PAGEHEIGHT)
				this.dialogHeight = this.PAGEHEIGHT - 100;
			//自定义参照取一下高度、宽度
			/*if(attrArr)	{
				if(attrArr.refHeight)
					this.dialogHeight = attrArr.refHeight;
				if(attrArr.refWidth)
					this.dialogWidth = attrArr.refWidth;
			}	
			*/
			/**
			 * 设计态默认是没有nodeinfo的.做空指针判断
			 */
			if(this.options.nodeInfo != null){
				if(this.options.nodeInfo.dialogHeight)
					this.dialogHeight = this.options.nodeInfo.dialogHeight;
				if(this.options.nodeInfo.dialogWidth)
					this.dialogWidth = this.options.nodeInfo.dialogWidth;
			}
			

			// 是否每次打开参照刷新,默认不刷新
			this.refreshRefPage = false;
			if (this.options.nodeInfo != null && this.options.nodeInfo.refreshRefPage) {
				this.refreshRefPage = $.argumentutils.getBoolean(this.options.nodeInfo.refreshRefPage, false);
			}
			
			//利用此属性表示当前是用户手工输入还是通过参照框选择后输入的。
			//1:手工输入；2:通过参照框传入
			this.inputType = null;
			
			this.datasetId = null;
			this.field = null;
			
			// 注册外部回掉函数
			window.clickHolders.push(this);
		},
		_create : function() {
			this._super();
		},
		_managerSelf : function() {
			this._super();
			var oThis = this,opts = this.options,ele = this.element;
			//如果父容器目前不可见，此width取传入值。暂时不考虑百分比情况
			var width = this.Div_text.outerWidth();
			//ipad需要再减10
//			if ($.browsersupport.IS_IPAD){
//				width = width - 10;
//			}
			if (width == 0 && !$.argumentutils.isPercent(opts.width))
				width =  parseInt(opts.width);
			
			var inputWidth = width - this.imageWidth;
//			if($.browsersupport.IS_IE)
//				inputWidth -= 3;
			this.input.css({
				'width' : inputWidth + "px",
				'position' : 'relative',
				'float' : 'left'
			});
			
//			var divButtonTop = (this.Div_text.outerHeight() - this.imageHeight)/2;
//			if(divButtonTop < 0){
//				divButtonTop = 0;
//			}
			
			this.divButton = $("<div></div>").addClass('reference_arrow_nm').attr({
				'id' : ele.attr('id') + "_ref_sel_button"		
			}).css({
				'position' : 'absolute',
				'cursor' : 'pointer',
				'width' : this.imageWidth + "px",
				'height' : this.imageHeight + "px",
				'right' : '-2px',
				'top' : "1px"
			});
			
			if(this.Div_text.children().length == 3){
				var centerWidth = width - 3*2;//3*2左右边框图片宽度
				this.Div_text.children().eq(1).css('width',centerWidth + "px");
				var imgWidth = (this.Div_text.children().eq(1).children().length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
				var inputWidth = centerWidth - imgWidth;
//				if($.browsersupport.IS_IE)
//					inputWidth -= 3;
				this.input.css('width',inputWidth + "px");//2*2 input输入框距离左右间距
			}
			
			if(this.Div_text.children().length == 3){
				this.Div_text.children().eq(1).append(this.divButton);
			}else{
				this.Div_text.append(this.divButton);
			}
			
			var divButtClickHandler = function(e) {
				e.triggerObj = oThis;
				oThis.isFromDiv = true;
				if (oThis._trigger('onclick') != false) {
					oThis.needRef = true;
					var pageWidth = $("body").innerWidth();
					var pageHeight = $("body").innerHeight();
					if (oThis.isDialog != false || oThis.divWidth > pageWidth || oThis.divHeight > pageHeight) {
						oThis.focus(e);
						oThis.input.blur();
//						oThis.input.focus(e);
						oThis.openRefDialog(e, true);
					} else {
						if (oThis.divIsShown)
							oThis.hideRefDiv();
						else
							oThis.openRefDiv(e);
					}
				}
				
				if (opts.stopHideDiv != true) {
					window.clickHolders.trigger = oThis;
					document.onclick();
				}
				e.stopPropagation();
				e.preventDefault();
				e.triggerObj = null;
			};
			
			var divButtMouseoverHandler = function(e) {
				if (!this.disabled)	
					$(e.target).attr('class','reference_arrow_on');
			};
			
			var divButtMouseoutHandler = function(e) {
				if(!this.disabled)	
					$(e.target).attr('class','reference_arrow_nm');
			}
			
			this.divButton.data('clickEvent',divButtClickHandler)
				.data('mouseoverEvent',divButtMouseoverHandler)
				.data('mouseoutEvent',divButtMouseoutHandler);
			
			this.divButton.off('click mouseover mouseout').on('click',function(e){
				divButtClickHandler(e);
			}).on('mouseover',function(e){
				divButtMouseoverHandler(e);
			}).on('mouseout',function(e){
				divButtMouseoutHandler(e);
			});
			
			this.input.off('focus').on('focus',function(e){
				oThis.warnIcon.hide();
				if(oThis.isError && typeof(oThis.errorMessage) == 'string' && oThis.errorMessage != ''){
					oThis.errorCenterDiv.html(oThis.errorMessage);
					oThis.errorMsgDiv.show();
				}
				if(this.readOnly)
					return;
				if(oThis.isFromDiv != true){
					oThis.oldValue = oThis.getValue();
				}
				else{
					oThis.oldValue = null;
				}
				oThis.isFromDiv = false;
				if(oThis.trueValue != null){
					oThis.setValue(oThis.trueValue);
				}
				
				var cnodes = oThis.Div_text.children();
				if(cnodes.length == 3){
					$.each(cnodes,function(index,item){
						$(item).attr('class',$(item).attr('class').replaceStr('input_normal','input_highlight'));
					});
					oThis.input.removeClass('input_normal_center_bg').addClass('input_highlight_center_bg');
				}
				
				oThis.focus(e);
				oThis.needRef = true;
				oThis.hideTip();
				
			}).off('blur').on('blur',function(e){
				if(this.readOnly)
					return;
				if(oThis.Div_text.children().length == 3){
					var children = oThis.Div_text.children();
					$.each(children,function(index,item){
						if(typeof(item) != "undefined"){
							$(item).attr('class',$(item).attr('class').replaceStr('input_highlight','input_normal'));
						}
					});
					oThis.input.removeClass('input_highlight_center_bg').addClass('input_normal_center_bg');
				}
				oThis.blur(e);
				oThis.needRef = false;
				oThis.showTip();
				
				
			}).off('click').on('click',function(e){
				e.stopPropagation();
			});
			
			//初始控件为禁用状态
		   	if (opts.disabled) {
		   		this.divButton.attr('class','reference_arrow_disable');
				//将与下拉按钮的各种动作事件保存
				this.divButton.off('click mouseout mouseover').css('cursor','default');
				this.setActive(false);
			}
		   	//TODO:先不处理了
		   	// 增加事件，进行输入条件后过滤
//		   	var keyDownListener = new KeyListener();
//		   	keyDownListener.onKeyUp = function(e) {
//		   		e = e.event;
//		   		if ((e.keyCode == 9 && e.shiftKey) || e.keyCode == 9) {  // "tab"键和"shift+tab"键处理
//		   			if (oThis.divIsShown) {
//						// 隐藏参照DIV
//		   				oThis.hideRefDiv();
//		   			}
//		   		}
//		   		else if (e.keyCode == 40){
//		   			oThis.focusSearchResult(1);
//		   		}
//		   		else if (e.keyCode == 38){
//		   			oThis.focusSearchResult(-1);
//		   		}
//		   		else if (e.keyCode != 13) {  // 不是上下箭头或回车
//			   		var value = oThis.input.val();
//		   			// 自动打开选框
//					if (!(e.keyCode == 32 && e.ctrlKey) && e.keyCode != 20 && e.keyCode != 16 && e.keyCode != 17 && e.keyCode != 18 && e.keyCode != 33 && e.keyCode != 34 && e.keyCode != 91) {
//						if (oThis.showRefDivTimeOut != null)
//							clearTimeout(oThis.showRefDivTimeOut);
//						oThis.needRef = true;
//						oThis.showRefDivTimeOut = setTimeout("ReferenceTextComp.showRefDivAndDoFilter('" + oThis.id + "','" + value + "');", 1000);
//					} 
//		   		}
//		   		else if (e.keyCode == 113) {  // F2键
//		   			// 打开对话框
//		   			oThis.openRefDialog(e, oThis.refreshRefPage || oThis.refresh);
//		   		}
//		   		
//		   		if (oThis.divIsShown) {
//		   			e.stopEvent = true;
//		   		}
//		   	};
//
//		   	keyDownListener.onEnter = function(e) {
//				if (oThis.divIsShown) {
//					var selIndex = oThis.comboCenterDiv.selIndex;
//					var node = oThis.comboCenterDiv.childNodes[selIndex];
//					if(node != null){
//						ReferenceTextComp.searchClick.call(node);
//					}
//		   			oThis.hideRefDiv();
//		   			oThis.input.val(value);
//					e.stopEvent = true;
//				}
//		   	};
//		   	this.addListener(keyDownListener);
//		   	
//		   	var focusListener = new FocusListener();
//		   	focusListener.onBlur = function(e) {
//				if(this.isFromDiv)
//					return;
//				var value = oThis.input.val();
//				oThis.doBlurSearch(value);
//		   	};
//		   	this.addListener(focusListener);
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			this.input.attr({
				'readOnly' : readOnly,
				'class' : this.inputClassName_init
			});
			this.options.readOnly=readOnly;
			this.Div_text.attr('class',this.options.className + (readOnly ? (" " + "text_div_readonly") : ''));
			this.divButton.css('visibility','');
		},
		getFormater : function() {
			return this._super();
		},
		setMessage : function(msg) {
			this._super(msg);
		},
		verify : function(val) {
			this._super(val);
		},
		blur : function(e) {
			if (this.visible) {
				var value = this.input.val();
				if (this.options.dataType!='P') {
					if(this.showTipMessage && this.showTipMessage != null)
						this.setMessage(this.showTipMessage);
					else
						this.setMessage(value);
				}	
				this.newValue = this.getFormater().format(value);
				var verifyR = this.verify(value);
				if(verifyR == null || verifyR){
					if (this.newValue != this.oldValue){
						this._trigger('valuechanged',e,{oldValue: this.oldValue,newValue : this.newValue});
						this._trigger('onblur',e);
					}
				}
			}
		},
		/**
		 * 若没打开DIV，焦点移出后的执行方法
		 * @private
		 */
		doBlurSearch : function(value) {
			//if(value != ""){
				if (this.doBlurSearchFromCache(value) != true) {  // 从前台缓存中获取不到，则到后台执行
					var rts = this.doMatchRefPkService(value);
				}
			//}
		},
		/**
		 * 调用后台服务，根据过滤条件查询符合条件的结果
		 * @private
		 */
		doMatchRefPkService : function(value) {
			var proxy = $.serverproxy.getObj({async:false});
			if(this.options.nodeInfo.dataListener && this.options.nodeInfo.dataListener != null)
				proxy.addParam("clc", this.options.nodeInfo.dataListener);
			else
				proxy.addParam("clc", "xap.lui.core.refrence.AppRefDftCtrl");
			proxy.addParam("m_n", "matchRefPk");
			proxy.addParam("matchValue", value);
			proxy.addParam("widgetId", this.viewpart.id);
			proxy.addParam("refNodeId", this.options.nodeInfo.id);
			proxy.addParam("widget_id", this.viewpart.id);
			proxy.addParam("referenceTextId", this.element.attr('id'));
			proxy.addParam('el', '2');
			//传递datasetId
			proxy.addParam("datasetId", this.datasetId);
			proxy.execute();
		},
		/**
		 * 父字段值改变后，将本字段值置空
		 */
		clearValue : function() {
			var ds = this.viewpart.getDataset(this.datasetId);
			var index = ds.nameToIndex(this.field);
			var rowIndex = ds.getSelectedIndex();
			ds.setValueAt(rowIndex, index - 1, "");
			ds.setValueAt(rowIndex, index, "");
			this.setValue("");
			this.setRefresh(true);
		},
		/**
		 * 从前台缓存中对输入内容进行校验和匹配
		 * @private
		 */
		doBlurSearchFromCache : function(value) {
			return false;
		},
		/**
		 * 显示控件(显示属性是visibility)
		 */
		showV : function() {
			this.element.css('visibility','');
			this.visible = true;
		},
		/**
		 * 设置大小和位置
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options,ele = this.element;
			opts.left = left;
			opts.top = top;
			opts.width = $.argumentutils.getString($.measures.convertWidth(width), ele.outerWidth() + "px");
			opts.height = $.argumentutils.getString($.measures.convertHeight(height), ele.outerHeight() + "px");
			
			// 设置最外层的大小
			ele.css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'width' : opts.width,
				'height' : opts.height
			});

			var tempWidth = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempWidth = ele.outerWidth();
			else
				tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);
			// 设置输入区域的大小
			this.Div_text.css('width' , tempWidth - 2 + "px");
			if (this.hasLabel)
				this.Div_text.css('width' , tempWidth - opts.labelWidth - 2 + "px");
			
			var pixelHeight = this.Div_text.outerHeight();
			var pixelWidth = this.Div_text.outerWidth();
//			if ($.browsersupport.IS_IE8 && (pixelHeight == 0 || pixelWidth == 0))
//				return;
			//ipad需要再减10	
//			if ($.browsersupport.IS_IPAD){
//				pixelWidth = pixelWidth - 10;
//			}
			this.input.css('width',(pixelWidth - this.imageWidth) + "px");//2*2 input距离左右间距 
			
			if(this.Div_text.children().length == 3){
				var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
				this.Div_text.children().eq(1).css('width',centerWidth + "px");
				var imgWidth = (this.Div_text.children().eq(1).children().length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
				var inputWidth = centerWidth - imgWidth;
//				if($.browsersupport.IS_IE){
//					inputWidth -= 3;
//				}
				this.input.css('width',inputWidth + "px");//2*2 input输入框距离左右间距
			}
		},
		/**
		 * 设置对话框宽度
		 */
		setDialogWidth : function(width) {
			this.dialogWidth = width;	
		},
		/**
		 * 设置对话框高度
		 */
		setDialogHeight : function(height) {
			this.dialogHeight = height;
		},
		/**
		 * 设置DIV宽度
		 */
		setDivWidth : function(width) {
			this.divWidth = width;	
		},
		/**
		 * 设置DIV高度
		 */
		setDivHeight : function(height) {
			this.divHeight = height;
		},
		/**
		 * 设置打开参照后是否刷新
		 */
		setRefresh : function(refresh) {
			this.refresh = refresh;
			this.refreshRefPage = refresh;
		},
		/**
		 * 设置过滤SQL语句
		 */
		setFilterSql : function(filterSql) {
			this.options.nodeInfo.filterSql = filterSql;
			this.refresh = true;
		},
		/**
		 * 打开参照对话框
		 * @private
		 */
		openRefDialog : function(e, isRefreshDialog) {
			var opts = this.options;
			if(opts.nodeInfo instanceof $.selfrefnodeinfo){
				var param = this._trigger('beforeopenrefpage',e);
				if (!param)
					return;
				if (typeof globalBeforeOpenRefDialog != "undefined")
					param = globalBeforeOpenRefDialog(this);
				if (typeof(param) == "boolean" && !param)
					return;
			
				var appUniqueId = $.pageutils.getAppUniqueId();
				var url = opts.nodeInfo.url;
				if(url.indexOf("?") == -1)
					url += "?";
				else
					url += "&";
				url = url +  "widgetId=" + this.viewpart.id + "&otherPageId=" + $.pageutils.getPageId() + "&otherPageUniqueId=" + $.pageutils.getPageUniqueId() + "&nodeId=" + opts.nodeInfo.id + "&owner=" + this.element.attr('id') + "&appUniqueId=" +appUniqueId + "&isReference=true";
				if (param != null && param != true && param.trim().length != 0)
			  		url += "&param=" + param;
				if(opts.nodeInfo.dialogWidth &&opts.nodeInfo.dialogWidth!= null && opts.nodeInfo.dialogWidth != "")
				    this.dialogWidth = opts.nodeInfo.dialogWidth;
				if(opts.nodeInfo.dialogHeight &&opts.nodeInfo.dialogHeight!= null && opts.nodeInfo.dialogHeight != "")
					this.dialogHeight = opts.nodeInfo.dialogHeight;
				$.pageutils.showDialog(url, opts.nodeInfo.name, this.dialogWidth, this.dialogHeight, this.refIndex, null, {"isShowLine":opts.isShowLine});
			}
			else{
//				this.getExtendsParam();
				var param = this.beforeopenParamValue;
				if(param == 'undefined' || param == null){
					var param = this._trigger('beforeopenrefpage',e);
					if (!param)
						return;
				}
				if (typeof globalBeforeOpenRefDialog != "undefined")
					param = globalBeforeOpenRefDialog(this);
				if (param != "" && !param)
					return;
				//记录当前参照refnode的id
					
				if (opts.nodeInfo&&opts.nodeInfo.id){
					window.$nowRefNodeId = opts.nodeInfo.id;
				}
				var oThis = this;
				var url = null;
				if (window.appType != null && window.appType == "true"){
					var appUniqueId = $.pageutils.getAppUniqueId();
					url = window.globalPath + "/app/" + window.appId + "/" + this.viewpart.id + "_" + opts.nodeInfo.id + "?pageId=" +  encodeURIComponent(opts.nodeInfo.pageMeta) + "&widgetId=" + this.viewpart.id + "&otherPageId=" + $.pageutils.getPageId() + "&otherPageUniqueId=" + $.pageutils.getPageUniqueId() + "&nodeId=" + opts.nodeInfo.id + "&owner=" + this.element.attr('id') + "&appUniqueId=" +appUniqueId + "&isReference=true&model=xap.lui.core.refrence.AppRefDftWindow";
				}
//				else{
//					if(window.clientMode)
//						url =  this.nodeInfo.pageMeta+this.viewpart.id+this.id+this.nodeInfo.id + ".html";
//					else if(this.nodeInfo.pageMeta == null || this.nodeInfo.pageMeta == "null")
//						url = window.corePath + "/" + this.nodeInfo.path + "&widgetId=" + this.viewpart.id + "&otherPageId=" + $.pageutils.getPageId() + "&otherPageUniqueId=" + $.pageutils.getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id;
//					else	
//						url = window.corePath + "/" + this.nodeInfo.path + "?pageId=" +  encodeURIComponent(this.nodeInfo.pageMeta) + "&widgetId=" + this.viewpart.id + "&otherPageId=" + $.pageutils.getPageId() + "&otherPageUniqueId=" + $.pageutils.getPageUniqueId() + "&nodeId=" + this.nodeInfo.id + "&owner=" + this.id;
//				}
						
			 	if (param != null && param != true && param.trim().length != 0)
			  		url += "&param=" + param;
			
				//this.refIndex = "ref_" + (ReferenceTextComp.DIALOG_INDEX ++);
				this.refIndex = opts.nodeInfo.id;
				if (this.refresh) {
					
					this.refresh = false;
				}
				if (isRefreshDialog)
					$.pageutils.showDialog(url, opts.nodeInfo.name, this.dialogWidth, this.dialogHeight, this.refIndex, null, {"isShowLine":opts.isShowLine});
				else	
					$.pageutils.showDialog(url, opts.nodeInfo.name, this.dialogWidth, this.dialogHeight, this.refIndex, null, {"isShowLine":opts.isShowLine});
			
				// Dialog中的Iframe的ID（该格式在PageUtil.js中的showDialog()方法里定义）
				var iframeId = "$modalDialogFrame" + this.refIndex;
				// 调用方法加载数据
				//ReferenceTextComp.doFilter("", iframeId, true);
			}
		},
		/**
		 * 打开参照选择Div
		 * @private
		 */
		openRefDiv : function(value) {
			var opts = this.options;
			//var service = $.pageutils.getFormularService();
			var rt = [];
			var proxy = $.serverproxy.getObj({async:true});
			
			if(opts.nodeInfo.dataListener && opts.nodeInfo.dataListener != null)
				proxy.addParam("clc", opts.nodeInfo.dataListener);
			else
				proxy.addParam("clc", "xap.lui.core.refrence.AppRefDftCtrl");
			proxy.addParam("m_n", "matchSearch");
			proxy.addParam("matchValue", value);
			proxy.addParam("widgetId", this.viewpart.id);
			proxy.addParam("widget_id", this.viewpart.id);
			proxy.addParam("datasetId", this.datasetId);
			proxy.addParam('el', '2');
			proxy.addParam("refNodeId", opts.nodeInfo.id);
			
			proxy.addParam("referenceTextId", this.element.attr('id'));
			
			proxy.addParam('refFormId', opts.refFormId);
			proxy.addParam("refFormeleID", opts.refFormeleID);
			
			proxy.addParam('refGridId', this.refGridId);
			proxy.addParam("refGridHeaderId", this.refGridHeaderId);
				
			//传递datasetId
			proxy.addParam("datasetId", this.datasetId);
			proxy.execute();
		
			/*
			var rt = null;
			matchValues = $.pageutils.getSessionAttribute('matchsValues');
			if(searchValues != null)
				rt = searchValues.split(";");
			if(searchValues=="")
				rt = new Array();
			
			//var rt = service.execute('matchSearch', "$S_" + value, $.pageutils.getPageUniqueId(), this.viewpart.id, this.nodeInfo.id);
			this.fillSearchDiv(rt);	
			*/
				
		},
		fillSearchDiv : function(rt){
			this.showDiv(rt);
		},
		/**
		 * 设置此参照输入框的激活状态
		 */
		setActive : function(isActive) {
			var isActive = $.argumentutils.getBoolean(isActive, false);
			
			// 控件处于激活状态变为非激活状态
			if (!isActive) {
				this._super(false);
				this.setReadOnly(true);
				//将与下拉按钮的各种动作事件保存
				this.divButton.off('click mouseout mouseover').css('cursor','default').attr('class','reference_arrow_disable');
			}
			// 控件处于禁用状态变为激活状态
			else if (isActive) {
				this._super(true);
				this.setReadOnly(false);
				this.divButton.off('click mouseout mouseover').on('click',function(e){$(this).data('clickEvent')(e);})
					.on('mouseout',function(e){$(this).data('mouseoutEvent')(e)})
					.on('mouseover',function(e){$(this).data('mouseoverEvent')(e)})
					.css('cursor','pointer').attr('class','reference_arrow_nm');
			}
			if(this.options.nodeInfo && this.options.nodeInfo.isRead){
				//this.setReadOnly(true);
			}else{
				//this.setReadOnly(false);
			}
		},
		/**
		 * 得到参照输入框的激活状态
		 */
		isActive : function() {
			return !this.options.disabled;
		},
		/**
		 * 获取参照对话框的Window对象
		 * @private
		 */
		getRefDlgWindow : function() {
			return window["$modalDialogFrame" + this.refIndex].contentWindow;
		},
		/**
		 * 设置该参照所在的Dataset与Field属性 
		 */
		setBindInfo : function(dataset, field) {
			this.datasetId = dataset;
			this.field = field;
		},
		/**
		 * 设值，即数据PK等隐藏值
		 */
		showTip : function() {
			this._super();
		},
		setValue : function(value) {
			value = $.argumentutils.getString(value, "");
			if (null != this.datasetId) {  // 绑定了dataset
				this.input.val(value);
				if (this.checkTip()) {
					if (this.input.val() == "")
						this.showTip();
					else
						this.input.css('color','black');
				}
			}
			this.options.value = value;
			this.newValue = value;

			this.inputType = 2;
			
			if (this.newValue != this.oldValue){
				this._trigger('valuechanged',null,{oldValue : this.oldValue,newValue : this.newValue});
				// 记录旧值
				this.oldValue = value;
			}
		},
		checkTip : function() {
			this._super();
		},
		/**
		 * 设置显示值，即输入框内显示值
		 */
		setShowValue : function(showValue) {
			showValue = $.argumentutils.getString(showValue, "");
			this.setMessage(showValue);
			this.showValue = showValue;
			this.input.val(showValue);
			if (this.checkTip()) {
				if (this.input.val() == "")
					this.showTip();
				else
					this.input.css('color','black');
			}
		},
		setMatchValues : function(matchValues) {
			if(this.options.nodeInfo && this.options.nodeInfo.isRead){
				return;
			}
			this.matchValues = matchValues;
			var rt = null;
			if(matchValues != null)
				rt = matchValues.split(";");
			if(matchValues=="")
				rt = new Array();
			this.fillSearchDiv(rt);	
		},
		beforeOpenParam : function(beforeopenParam) {
			this.beforeopenParamValue = beforeopenParam;
		},
//		/**
//		 * 打开参照前用户传递参照
//		 */
//		getExtendsParam : function() {
//			var opts = this.options;
//			var proxy = $.serverproxy.getObj({async:false});
//			if(opts.nodeInfo&&opts.nodeInfo.dataListener){
//				proxy.addParam("clc", opts.nodeInfo.dataListener);
//			}else{
//			   proxy.addParam("clc", "xap.lui.core.refrence.AppRefDftCtrl");
//			}
//			proxy.addParam("m_n", "getExtendsParam");
//			proxy.addParam("widgetId", this.viewpart.id);
//			proxy.addParam("widget_id", this.viewpart.id);
//			proxy.addParam("datasetId", this.datasetId);
//			proxy.addParam('el', '2');
//			if(opts.nodeInfo){
//				proxy.addParam("refNodeId", opts.nodeInfo.id);
//			}
//			proxy.addParam("referenceTextId", this.element.attr('id'));
//			proxy.addParam('refFormId', opts.refFormId);
//			proxy.addParam("refFormeleID", opts.refFormeleID);
//			proxy.addParam('refGridId', this.refGridId);
//			proxy.addParam("refGridHeaderId", this.refGridHeaderId);
//			//传递datasetId
//			proxy.addParam("datasetId", this.datasetId);
//			proxy.execute();
//		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			return {
				c : "ReferenceTextContext",
				id : this.element.attr('id'),
				value : this.options.value,
				showValue : this.input.value,
				readOnly : this.options.readOnly,
				enabled : !this.options.disabled,
				visible : this.visible
			};
		},
		hideV : function() {
			this._super();
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.focus)
				this.setFocus();
//			if (context.showValue != this.input.value) {
//				this.showValue = context.showValue;
//				if (context.value != this.value)
//					this.value = context.value;
//				this.setShowValue(context.showValue);
//			} 
//			else if (context.value != this.value)
//				this.setValue(context.value);
				
			if (context.readOnly != null && this.options.readOnly != context.readOnly)
				this.setReadOnly(context.readOnly);
//			if (context.visible != this.visible) {
				if (context.visible)
					this.showV();
				else
					this.hideV();
//			}
		},
		/**
		 * 显示参照Div
		 * @private
		 */
		showDiv : function(rt) {
			var oThis = this;
			if (!this.dataDiv) {
				this.comboDiv = $("<div></div>").addClass('combo_div');
			
				$("<div></div>").addClass('combo_center_top_div').appendTo(this.comboDiv);
				$("<div></div>").addClass('combo_left_center_div').appendTo(this.comboDiv);
				
				this.comboCenterDiv = $("<div></div>").addClass('combo_center_div').appendTo(this.comboDiv);
				
				$("<div></div>").addClass('combo_right_center_div').appendTo(this.comboDiv);
				$("<div></div>").addClass('combo_center_bottom_div').appendTo(this.comboDiv);
			
				// 创建显示数据的下拉区
				this.dataDiv = $("<div></div>").attr({
					'id' : this.element.attr('id') + 'comb_data_div'
				}).addClass('combobox_data_div').css({
					'z-index' : $.measures.getZIndex(),
					'width' : (this.options.width == null ? '120px' : this.options.width),
					'overflow' : 'auto',
					'position' : 'absolute'
				}).hide().appendTo('body').append(this.comboDiv);
			
				this.comboCenterDiv.get(0).owner = this;
			}
			
			var optionLength = 1;
			if(rt != null){
				optionLength = rt.length;
				if(optionLength < 3)
					optionLength = 3;
			}
			
			// 数据区宽度和输入框宽度要相等
			var _width = '';
			_width = this.Div_text.outerWidth() + "px";
			if (this.Div_text.outerWidth() == 0) {
				_width = this.options.width == null ? '120px' : this.options.width;			
			}
			
			var _height = '';
			if (this.dataDivHeight != null)
				_height = this.dataDivHeight + "px";
			else {
				if (optionLength <= ComboComp.defaultVisibleOptionsNum)
					_height = (optionLength * LanguageOptionComp.ITEM_HEIGHT + 11*2) + "px";
				else{
					_height = (ComboComp.defaultVisibleOptionsNum * LanguageOptionComp.ITEM_HEIGHT + 11*2) + "px";
				}
			}
			
			this.dataDiv.css({
				'left' : this.Div_text.offset().left + "px",
				'top' : (this.Div_text.offset().top + this.Div_text.outerHeight()) + "px",
				'z-index' : $.measures.getZIndex(),
				'width' : _width,
				'height' : _height
			});
			
			
			this.comboDiv.css('height',(optionLength * LanguageOptionComp.ITEM_HEIGHT) + 11*2 + "px");//11*2背景上下边宽度和
			this.comboCenterDiv.empty();
			this.comboCenterDiv.get(0).selIndex = null;
			if(rt != null){
				for(var i = 0; i < rt.length; i ++){
					var div = $("<div></div>");
					div.on('mousedown',function() {
						var textComp = oThis;
						textComp.isFromDiv = true;
					}).on('click',function() {
						var div = this;
						var value = div.value;
						var name = div.name;
						if(div.allowExtendValue){
							name = div.name;
						}
						var textComp = oThis;
						textComp.isFromDiv = true;
						textComp.setFocus();
						var oldShowValue = textComp.input.val();
						if(oldShowValue != null && oldShowValue.indexOf(",") != -1){
							var rv = oldShowValue.substr(0, oldShowValue.lastIndexOf(",")) + "," + value;
							if(textComp.refFormId != null){
								textComp.setValue(rv);
							}
							else{
								textComp.setShowValue(rv);
							}
						}
						else{
							if(textComp.refFormId != null){
								textComp.setValue(value);
								textComp.setShowValue(name);
							}
							else
								textComp.setShowValue(name);
						}
						textComp.oldValue = null;
						textComp.dataDiv.hide();
						e.stopPropagation();
						e.preventDefault();
					});
					var pair = rt[i].split(",");
					div.value = pair[0];
					if(pair[1] == null || pair[1] == ''){
						div.html(pair[0]).attr('name','');
					}
					else{
						div.html(pair[0] + " " + pair[1]).attr('name',pair[1]);
					}
					div.allowExtendValue = this.options.nodeInfo.allowExtendValue;
					this.comboCenterDiv.append(div);
				}
			}
			
			this.dataDiv.show();
			// 将控件放在视线内
			$.measures.positionElementInView(this.dataDiv.get(0));
			
			this.divIsShown = true;
			// 重新设置所有选项的选中样式
//			this.resetSelStyle();
			this.focusSearchResult(0);
			return this.dataDiv.get(0);
		},
		focusSearchResult : function(dir) {
			if(!this.divIsShown)
				return;
			var nodes = this.comboCenterDiv.children();
			if(nodes.length == 0)
				return;
			var selIndex = this.comboCenterDiv.get(0).selIndex;
			var oldSelIndex = selIndex;
			var size = nodes.length;
			if(selIndex == null)
				selIndex = 0;
			else{
				if(dir > 0){
					selIndex ++;
				}
				else if(dir < 0){
					selIndex --;
				}
			}
			if(selIndex > size - 1)
				return;
			this.comboCenterDiv.get(0).selIndex = selIndex;
			nodes.eq(selIndex).css('background','yellow');
			if(oldSelIndex != null && oldSelIndex <= size - 1)
				nodes.eq(oldSelIndex).css('background','');
		},
		/**
		 * 隐藏参照Div
		 * @private
		 */
		hideRefDiv : function() {
			if (this.dataDiv && this.divIsShown) {
				this.dataDiv.hide();
				this.comboCenterDiv.get(0).selIndex = null;
				this.divIsShown = false;
			}
		},
		/**
		 * 外部点击事件发生时的回调函数。用来隐藏下拉框数据部分
		 * @private
		 */
		outsideClick : function(e) {
			if (window.clickHolders.trigger == this)
				return;
			this.hideRefDiv();
		}
	});
	
	$.referenceTextComp = {
		waitFilterRt : null,
		/**
		 * 显示选项DIV并且进行过滤(setTimeOut调用)
		 * @private
		 */
		showRefDivAndDoFilter : function(id, value) {
			var refComp = window.objects[id];
			if (refComp != null){
				//not supported
				if(refComp.option.nodeInfo.multiSel)
					return;
				if(!refComp.needRef)
					return;
				$.referenceTextComp.doOpenRefDiv(refComp, value);
			}
		},
		/**
		 * 执行打开Div方法
		 * @private
		 */
		doOpenRefDiv : function (refComp, value) {
			refComp.openRefDiv(value);
			if (refComp.stopHideDiv != true) {
				window.clickHolders.trigger = refComp;
				document.onclick();
			}
		},
		/**
		 * 过滤
		 * @private
		 */
		doFilter : function(value, frameId, doIt) {
			if (doIt){
				if ($.referenceTextComp.waitFilterRt != null)
					clearTimeout($.referenceTextComp.waitFilterRt);
				// 判断Div是否完全打开
				if (window[frameId] == null ||window[frameId].contentWindow == null || window[frameId].contentWindow.renderDone == null) {
					$.referenceTextComp.waitFilterRt = setTimeout("$.referenceTextComp.doFilter('" + value + "','" + frameId + "'," + doIt + ");", 50);
					return;
				}
				window[frameId].contentWindow.doFilter(value);
			}
		}
	};
})(jQuery);

function doSearchFun(result, args, success, rt) {
		//没有异常
	var searchValues = $.pageutils.getSessionAttribute('matchsValues');
	if(searchValues != null)
		rt = searchValues.split(";");
	if(searchValues=="")
		rt = [];
}
