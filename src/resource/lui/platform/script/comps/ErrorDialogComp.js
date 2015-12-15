/**
 * @fileoverview ErrorDialogComp组件。 通过显示输入字符串和"错误"图片提供此对话框.
 * @auther lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 错误对话框构造方法
	 * @class 错误对话框类，通过输入指定字符串和显示图片提供此对话框。<br>
	 *        <b>组件皮肤设置说明：</b><br>
	 *        modaldialog.css对对话框中显示字体的外观控制，如字体、大小等<br>
	 *        <br>
	 * 
	 * <b>使用js，直接通过new的用法生成ErrorDialog实例：</b><br>
	 * errordialog = ErrorDialogComp.showDialog(msg); // 用需要显示的字符串代替msg
	 * @constructor ErrorDialogComp构造函数
	 * @version 1.2
	 * 
	 * WarningDialogComp构造函数
	 * @param name 控件的名称(也就是id)
	 * @param title 对话框标题
	 * @param left 控件左部x坐标
	 * @param top 控件顶部y坐标
	 * @param msg 显示的出错信息,用户输入
	 * @param refImg 错误对话框左侧的显示图片的url
	 */
	$.widget("lui.errordialog", {
		options : {
			name : '',
			title : trans("ml_errordialog"),
			left : 0,
			top : 0,
			msg : '',
			refImg : '',
			okText : trans("ml_ok")
		},
		_initParam : function() {
			this.componentType = "ERRORDIALOG";
			this.HEIGHT = 196;
			this.WIDTH = 348;
			this.refImgPath = "/comps/dialog/images/tip1.png";
			
			this.width = this.WIDTH;
			this.height = this.HEIGHT;

			this.options.refImg = $.argumentutils.getString(this.options.refImg, window.themePath + this.refImgPath);
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options,ele = this.element;
			this.modaldialog = $("<div></div>").appendTo("body").modaldialog({
				name : opts.name,
				title : opts.title,
				left : opts.left,
				top : opts.top,
				width : this.width,
				height : this.height
			}).modaldialog("instance");
				
			this.modaldialog.contentDiv.css('overflow','hidden');

			this.contentDiv = $("<div></div>").addClass('message_contentdiv').appendTo(this.modaldialog.getContentPane());
			this.leftDiv = $("<div></div>").addClass('message_leftdiv').appendTo(this.contentDiv);
			this.rightDiv = $("<div></div>").html("<div class='rightInnerDiv'></div>").addClass('message_rightdiv').appendTo(this.contentDiv);
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

			this.bottomDiv = $("<div></div>").addClass('message_bottomdiv').appendTo(this.modaldialog.getContentPane());

			this.okBtDiv = $("<div></div>").addClass('message_okbtdiv').appendTo(this.bottomDiv);
//			if (!$.browsersupport.IS_IE) {
//				this.okBtDiv.css('text-align','-moz-center');
//			}
			// 生成确定按钮
			this.okBt = $("<div id=\"okBt\"></div>").button({
				top : 7,
				width : 74,
				text : opts.okText,
				position : 'relative',
				className : 'blue_button_div',
				onclick : function(e) {
					oThis.hide();
				}
			}).appendTo(this.okBtDiv);
				
//			if ($.browsersupport.IS_IE8) {
//				this.okBt.button("instance").element.css('margin','0 auto');
//			}
			this.contentDiv.css('height',this.modaldialog.getContentPane().outerHeight() - this.bottomDiv.outerHeight() + "px");
		},
		/**
		 * 改变ErrorDialog的显示信息
		 * 
		 * @param msg 要显示的信息
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
		adjustContentdivWidth : function(){
			//this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth - 2 + "px";	
		},
		/**
		 * 隐藏对话框
		 * 
		 * @private
		 */
		hide : function() {
			this.modaldialog.hide();
		}
	});
	
	$.errorDialogComp = {
		showDialog : function(message, title, okText, func) {
			if (!window.globalObject.$c_ErrorDialog)
				window.globalObject.$c_ErrorDialog = $("<div></div>").errordialog({
					name : "ErrorDialog",
					title : title,
					msg : message,
					okText : okText
				}).errordialog("instance");
			var dialog = window.globalObject.$c_ErrorDialog;
			dialog.changeMsg(message);
			dialog.show();
			if(func!=null){
				dialog.okBt.on("buttononclick",function(e){
					func.call(this);
					$.errorDialogComp.hideDialog();
				});
				dialog.modaldialog.close = function(){
					func.call(this);
					$.errorDialogComp.hideDialog();
				};
			}
			else{
				dialog.okBt.on("buttononclick",function(e){
					$.errorDialogComp.hideDialog();
				});
				dialog.modaldialog.close = function(){
					$.errorDialogComp.hideDialog();
				};
			}
			return dialog;
		},
		/**
		 * 隐藏对话框
		 */
		hideDialog : function() {
			try {
				var dialog = window.globalObject.$c_ErrorDialog;
				dialog.hide();
			} catch (error) {
			}
		}
	};
})(jQuery);
