/**
 *	@fileoverview 
 *	模态对话框控件。提供基本的状态对话框容器。同时由此派生一些
 *	常用对话框。比如MessageDialog,WarningDialog等
 *	@author lxl
 *  @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 模态对话框构造方法
	 * @class 模态对话框类
	 * @constructor
	 * @param name 控件名称
	 * @param title 控件标题
	 * @param left 控件左部x坐标
	 * @param top 控件顶部y坐标
	 * @param width 控件宽度
	 * @param height 控件高度
	 * @param className css文件的名字
	 */
	$.widget("lui.modaldialog", $.lui.base, {
		options : {
			name : '',
			title : trans("ml_dialog"),
			className : 'dialog_div',
			isShowLine : true,
			//event
			onbeforeshow : null,
			onbeforeclose : null,
			onafterclose : null,
			onclosing : null
		},
		_initParam : function() {
			this.componentType = "MODALDIALOG";
			
			this.id = this.options.name?this.options.name:this.element.attr("id");
			// 模态对话框的高度常量默认值
			this.HEIGHT = 200;
			// 模态对话框的宽度常量默认值
			this.WIDTH = 300;
			// 页面Dialog最大的z-index;
			this.ZINDEX = 0;
			// 如果是子类扩展声明，则不需初始化

			this._super();
		},
		_create : function() {
			this._initParam();
			var _width = '';
//			if($.browsersupport.IS_IE7){
//				var clientWidth = document.body.clientWidth;
//				if(clientWidth > 1220)
//					clientWidth = clientWidth - 20;
//				_width = clientWidth + "px";
//			}else{
				_width = "100%";
//			}

			this.element.css({
				'position' : 'absolute',
				'left' : '0px',
				'top' : '0px',
				'width' : _width,
				'min-height' : '100%',
				'z-index' : $.measures.getZIndex(),
				'visibility' : 'hidden',
				'overflow' : 'hidden',
				'background' : "url(" + window.themePath + "/global/images/transparent.gif)"
			});

			this._managerSelf();
		},
		/**
		 * 此控件的回掉函数
		 * 
		 * @private
		 */
		_managerSelf : function () {
		    var oThis = this,ele = this.element,opts = this.options;
			if((opts.height+"").indexOf("%") != -1){
				opts.height = (document.body.clientHeight * $.measures.translatePercentToFloat(opts.height)) + "px";
			}
		    this.divdialog = $("<div></div>").addClass(opts.className).css({
				'width' : opts.width,
				'height' : opts.height
			});

		    //遮挡层大小改变重新设置Dialog大小
		    addResizeEvent(ele[0], function(e) {
//		    	if($.browsersupport.IS_IE){
//					var topHeight = window.document.body.scrollHeight;
//					ele.css('height',topHeight + "px");
//		    	}
//		        clearEventSimply(e);
			});
			
		    //var frmstr = '<iframe src="" style="position:absolute; visibility:inherit; top:0px; left:0px; width:100%;height:100%; z-index:-1; border:none;" frameborder="0"></iframe>';
		    //this.divdialog.html(frmstr);

		    // 将对话框添加到透明背景上
		    ele.append(this.divdialog);
		    

			//title
		    this.titleC = $("<div></div>").addClass('centerheaddiv').appendTo(this.divdialog);

			//content
			var _ccClass = '';
			if(opts.isShowLine){
				_ccClass = "centerbodydiv";
			}else{
				_ccClass = "centerbodynolinediv";
			}
		    this.cC = $("<div></div>").addClass(_ccClass).css({
				'height' : this.divdialog.innerHeight() - 40 + "px",
				'background-color' : '#FFFFFF'
			}).appendTo(this.divdialog);

           //console.log(opts.title)
		    // 对话框标题
		    this.titleDiv = $("<div></div>").addClass('dialog_title').css({
				'width' : this.divdialog.outerWidth() - 110 + "px"
			}).appendTo(this.titleC).html(opts.title.replace("\<", "&lt;").replace("\>", "&gt"));

		    this.closeBt = $("<div></div>").addClass('closebt_off').on('mouseover',function(e){
				$(this).attr('class','closebt_on');
			}).on('mouseout',function(e){
				$(this).attr('class','closebt_off');
			}).on('mousedown',function(e){
				$(this).attr('class','closebt_click');
			}).on('click',function(e){
				try{
					if (oThis.getContentPane() != null){
						var frame = oThis.getContentPane().children().first();
						if (frame && frame.contentWindow && frame.contentWindow.document)
							if(frame.contentWindow.document.onclick)
								frame.contentWindow.document.onclick();
					}
				}catch(e){}
				oThis.close();
			}).appendTo(this.titleC);

		    this.contentDiv = $("<div></div>").addClass('dialog_content').appendTo(this.cC);
		    
		    this.divdialog.draggable({
				cancel: ".closebt_off , .closebt_on , .closebt_click",
				handle: ".centerheaddiv"
			});
		},
		showLine : function(isShowLine){
			if (isShowLine == this.options.isShowLine) return;
			this.options.isShowLine = isShowLine;
			this.cC.attr('class',isShowLine ? 'centerbodydiv' : 'centerbodynolinediv');
		},
		/**
		 * 显示模式对话框
		 */
		show : function(refDiv) {
			this.element.css({
				'visibility' : 'visible',
				'z-index' : $.measures.getZIndex()
			}).show();
			this.visible = true;
			var sctop = 0;
			var clinetHeight = 0;
			sctop = $.measures.compFirstScrollTop(this.element[0]);
			clinetHeight = $.measures.compFirstScrollClientHeight(this.element[0]);
			this.element.css('top',sctop);
			var _dialogWidth = this.divdialog.outerWidth();
			var _dialogHeight = this.divdialog.outerHeight();
			if(_dialogWidth > 0 && _dialogHeight > 0){
				var iframe = this.divdialog.children().first().get(0);
				if(iframe && iframe.tagName == 'IFRAME'){
					$(iframe).css({
						'width' : _dialogWidth - 2*2 + "px",
						'height' : _dialogHeight - 12 - 2 + "px",
						'left' : '2px',
						'top' : '12px'
					});
				}
			}
//			if(_dialogWidth > document.body.clientHeight)
//				document.body.style.height = (_dialogHeight + 10) + "px";
			// 将控件显示在父控件的中间
			$.measures.positionElementToScreenCenter(this.divdialog.get(0), sctop, clinetHeight);
			setTimeout("document.body.style.height = '100%'", 1000);
//			window.onresize();
			if(window.pageUI)
				pageUI.hasChanged = false;
			$.pageutils.adjustContainerFramesHeight();
		},
		/**
		 * 重载父类的方法
		 * 
		 * @param left 新的左部x坐标
		 * @param top 新的顶部y坐标
		 * @param width 新宽度
		 * @param height 新高度
		 */
		setBounds : function(left, top, width, height) {
			this._super(left, top, width, height);
		},
		/**
		 * 隐藏模式对话框
		 */
		hide : function() {
			this.element.hide();
			this.visible = false;
			var html = this.element.html();
//			if ($.browsersupport.IS_IE8 && html.indexOf("cke_contents_htmlcontent_content") != -1){
//				var td = $("#cke_contents_htmlcontent_content");
//				try{
//					td.get(0).childNodes[0].contentWindow.document.body.innerHTML = "";
//				}catch(e){}
//			}
		},
		/**
		 * 设置对话框大小
		 * 
		 * @param width 新宽度
		 * @param height 新高度
		 */
		setSize : function(width, height) {
			this.options.width = parseInt(width, 10) + "px";
			this.options.height = parseInt(height, 10) + "px";
			this.divdialog.css({
				'width' : this.options.width,
				'height' : this.options.height
			});
			var _dialogHeight = this.divdialog.outerHeight();
			var _dialogWidth = this.divdialog.outerWidth();
			this.cC.css('height',_dialogHeight - 42 + "px");
			if(_dialogWidth > 0 && _dialogHeight > 0){
				var iframe = this.divdialog.children().first().get(0);
				if(iframe && iframe.tagName == 'IFRAME'){
					$(iframe).css({
						'width' : _dialogWidth - 2*2 + "px",
						'height' : _dialogHeight - 12 - 2 + "px",
						'left' : '2px',
						'top' : '12px'
					});
				}
			}
			this.titleDiv.css('width',_dialogWidth - 110 + "px");
		},
		/**
		 * 得到内容面版
		 */
		getContentPane : function() {
			return this.contentDiv;
		},
		/**
		 * 给对话框添加一个组件,覆盖base的add方法
		 * 
		 * @param objHtml 显示对象
		 */
		add : function(objHtml) {
			this.getContentPane().get(0).appendChild(objHtml);
		},
		/**
		 * 设置对话框的标题
		 * 
		 * @param title 标题文字
		 */
		setTitle : function(title) {
			this.options.title = title;
			this.titleDiv.html(this.title);
		},
		/**
		 * 关闭对话框内部处理函数,子类如果需要特殊处理需要覆盖此方法
		 * 
		 * @private
		 */
		close : function() {
			// 关闭之前调用用户的处理
			if (this._trigger("onbeforeclose",null)==false)
				return;
			this._trigger("onclosing",null);
			this.hide();
			this._trigger("onafterclose",null);
			return true;
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			return {
				c : "ModalDialogComp"
			};
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
		}
	});
})(jQuery);
