/**
 * @fileoverview
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 确认对话框构造函数
	 * @class Confirm对话框类。<br>
	 *        Confirm对话框是对ModalDialog的封装，在对话框底部添加了OK，
	 *        Cancel按钮。使用者只需要给出ContentPane部分内容信息即可 <br>
	 *        <b>组件皮肤设置说明：</b><br>
	 *        confirmdialog.css是对ConfirmDialog中显示字体的外观控制，如字体、大小等<br>
	 *        modaldialog.css是对ConfirmDialog外观整体的控制，包括标题背景，内容区背景等<br>
	 *        <br>
	 * 
	 * <b>使用js，直接通过new的用法生成ConfirmDialog实例：</b><br>
	 * confirmdialog = ConfirmDialogComp.showDialog(msg);// 用需要显示的字符串代替msg<br>
	 * confirmdialog.onOk = confirm; //把点击确认按钮后需要执行的函数名赋给onOk属性<br>
	 * confirmdialog.onCancel = noconfirm; //把点击取消需要执行的函数名赋给onCancel属性<br>
	 * 
	 * @constructor ConfirmDialogComp的构造函数
	 * @param name 对话框名字
	 * @param msg 需要显示的信息
	 * @param title 对话框标题
	 * @param refImg 对话框左部要显示的图片(有默认图片)
	 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
	 */
	$.widget("lui.confirmdialog", {
		options : {
			name : '',
			title : trans("ml_confirmdialog"),
			left : 0,
			top : 0,
			msg : '',
			refImg : '',
			another : null,
			okText : trans("ml_ok"),
			cancelText : trans("ml_cancel"),
			//event
			onok : null,
			oncancel : null
			
		},
		_initParam : function() {
			this.componentType = "CONFIRMDIALOG";
			this.HEIGHT = 196;
			this.WIDTH = 348;
			this.refImgPath = "/comps/dialog/images/tip3.png";
			this.width = this.WIDTH;
			this.height = this.HEIGHT;
			this.options.refImg = $.argumentutils.getString(this.options.refImg, window.themePath + this.refImgPath);
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options,ele = this.element;
			this.modaldialog = $("<div></div>").modaldialog({
				name : opts.name,
				title : opts.title,
				left : opts.left,
				top : opts.top,
				width : this.width,
				height : this.height
			}).appendTo("body").modaldialog("instance");
				
			this.modaldialog.contentDiv.css('overflow','hidden');

			this.contentDiv = $("<div></div>").addClass('confirm_contentdiv').appendTo(this.modaldialog.getContentPane());
			this.leftDiv = $("<div></div>").addClass('confirm_leftdiv').appendTo(this.contentDiv);
			this.rightDiv = $("<div></div>").html("<div class='rightInnerDiv'></div>").addClass('confirm_rightdiv').appendTo(this.contentDiv);
			this.imgNode = $("<img/>").attr({
				'src' : opts.refImg
			}).appendTo(this.leftDiv);
//			if($.browsersupport.IS_IE6){
//				this.imgNode.css({
//					'width' : '32px',
//					'height' : '32px'
//				});
//			}
			this.msgTd = this.rightDiv.children().first();

			this.bottomDiv = $("<div></div>").addClass('confirm_bottomdiv').appendTo(this.modaldialog.getContentPane());

			this.cancelBtDiv = $("<div></div>").addClass('confirm_cancelbtdiv').appendTo(this.bottomDiv);
			this.okBtDiv = $("<div></div>").addClass('confirm_okbtdiv').appendTo(this.bottomDiv);
//			if (!$.browsersupport.IS_IE) {
				this.okBtDiv.css('text-align','-moz-right');
				this.cancelBtDiv.css('text-align','-moz-left');
//			}
//			if ($.browsersupport.IS_IE6) {
//				this.okBtDiv.css('padding-top','3px');
//				this.cancelBtDiv.css('padding-top','3px');
//			}

			// 生成确定按钮
			this.okBt = $("<div id=\"okBt\"></div>").button({
				top : 7,
				width : 74,
				text : opts.okText,
				position : 'relative',
				className : 'blue_button_div',
				onclick : function(e) {
					oThis._trigger("onok",e);
					oThis.hide();
					return true;
				}
			}).appendTo(this.okBtDiv);
				
			// 生成取消按钮
			this.cancelBt = $("<div id=\"cancelBt\"></div>").button({
				top : 7,
				width : 74,
				text : opts.cancelText,
				position : 'relative',
				className : 'button_div',
				onclick : function(e) {
					oThis._trigger("oncancel",e);
					oThis.hide();
					return true;
				}
			}).appendTo(this.cancelBtDiv);
				
				
			this.modaldialog.close = function(){
				oThis.close();
			};
//			if ($.browsersupport.IS_IE8) {
//				this.okBt.css('float','right');
//			}
			this.contentDiv.css('height',this.modaldialog.getContentPane().outerHeight() - this.bottomDiv.outerHeight() + "px");
		},
		/**
		 * 覆盖父类的close方法实现自己的逻辑处理
		 * 
		 * @private
		 */
		close : function() {
			this._trigger("oncancel",null);
			this.hide();
			return true;
		},
		/**
		 * 得到要添加内容的面板
		 */
		getContentPane : function() {
			return this.contentDiv;
		},
		/**
		 * 改变ConfirmDialogComp的显示信息
		 * 
		 * @param msg 要显示的确认文字
		 */
		changeMsg : function(msg) {
			this.options.msg = msg;
			this.msgTd.html(this.options.msg);
		},
		/**
		 * 显示对话框
		 * 
		 * @private
		 */
		show : function() {
			this.modaldialog.show();
			this.adjustContentdivWidth();	
			
			var height = this.msgTd.outerHeight();
			if(height <= 0){
				height = $.measures.getTextHeight(this.options.msg);
			}
			var marginTop = 59 - height/2;
			marginTop = marginTop > 0 ? marginTop : 0; 
			this.msgTd.css('margin-top',marginTop + "px");
		},
		/**
		 * 隐藏对话框
		 * 
		 * @private
		 */
		hide : function() {
			this.modaldialog.hide();
		},
		adjustContentdivWidth : function(){
			//this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth - 2 + "px";	
		}
	});
	
	$.confirmDialogComp = {
		/**
		 * 创建一个对话框的类方法
		 * 
		 * @param message 要显示的信息
		 * @param okFunc 点击确定按钮时调用的函数名
		 * @param cancelFunc 点击取消按钮时调用的函数名
		 * @param obj1 调用okFunc函数的对象
		 * @param obj2 调用cancelFunc函数的对象
		 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
		 */
		showDialog : function(message, okFunc, cancelFunc, obj1,
				obj2, zIndex, another, okText, cancelText, title) {
			title = $.argumentutils.getString(title, trans("ml_confirmdialog"));
			var dialog = null;
			if (!another) {
				if (!window.globalObject.$c_ConfirmDialog){
					window.globalObject.$c_ConfirmDialog = $("<div></div>").confirmdialog({
						name : "confirmDialog",
						title : title,
						msg : message,
						okText : okText,
						cancelText : cancelText
					}).confirmdialog("instance");
				}
				else{
					if(window.globalObject.$c_ConfirmDialog.okText != okText)
						window.globalObject.$c_ConfirmDialog = $("<div></div>").confirmdialog({
							name : "confirmDialog",
							title : title,
							msg : message,
							okText : okText,
							cancelText : cancelText
						}).confirmdialog("instance");
					window.globalObject.$c_ConfirmDialog.modaldialog.setTitle(title);
				}
				dialog = window.globalObject.$c_ConfirmDialog;
			} else {
				dialog = $("<div></div>").confirmdialog({
					name : "confirmDialog",
					title : title,
					msg : message
				}).confirmdialog("instance");
			}
			dialog.changeMsg(message);
			dialog.modaldialog.element.css('z-index',$.measures.getZIndex());

			if (okFunc) {
				dialog.options.onok = function(){
					if (obj1)
						okFunc.call(this,obj1);
					else
						okFunc.call(this);
				};
			}

			if (cancelFunc) {
				dialog.options.oncancel = function(){
					if (obj2)
						cancelFunc.call(this,obj2);
					else
						cancelFunc.call(this);
				};
			}
			else{
				dialog.options.oncancel = function(){					
				};
			}

			dialog.show();
			return dialog;
		},
		/**
		 * 静态方法,隐藏对话框
		 */
		hideDialog : function() {
			var dialog = window.globalObject.$c_ConfirmDialog;
			dialog.hide();
		}
	};
})(jQuery);
