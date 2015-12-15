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
	 * confirmdialog = ThreeButtonsDialog.showDialog(msg);// 用需要显示的字符串代替msg<br>
	 * confirmdialog.onOk = confirm; //把点击确认按钮后需要执行的函数名赋给onOk属性<br>
	 * confirmdialog.onCancel = noconfirm; //把点击取消需要执行的函数名赋给onCancel属性<br>
	 * 
	 * @constructor ThreeButtonsDialog的构造函数
	 * @param name 对话框名字
	 * @param msg 需要显示的信息
	 * @param title 对话框标题
	 * @param refImg 对话框左部要显示的图片(有默认图片)
	 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
	 */
	$.widget("lui.threebuttonsdialog", {
		options : {
			name : '',
			title : trans("ml_confirmdialog"),
			left : 0,
			top : 0,
			msg : '',
			refImg : null,
			another : null,
			//event
			onok : null,
			onmiddle : null,
			oncancel : null
		},
		_initParam : function() {
			this.componentType = "THREEBUTTONSDIALOG";
			this.HEIGHT = 196;
			this.WIDTH = 348;

			this.refImgPath = "/ui/ctrl/dialog/images/tip2.png";
			
			this.width = this.WIDTH;
			this.height = this.HEIGHT;

			this.options.refImg = $.argumentutils.getString(this.options.refImg, window.themePath + this.refImgPath);
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options, ele = this.element;
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
			this.rightDiv = $("<div></div>").addClass('confirm_rightdiv').html("<div class='rightInnerDiv'></div>").appendTo(this.contentDiv);

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

			this.okBtDiv = $("<div></div>").addClass('input_1btdiv').appendTo(this.bottomDiv);
			this.middleBtDiv = $("<div></div>").addClass('input_2btdiv').appendTo(this.bottomDiv);
			this.cancelBtDiv = $("<div></div>").addClass('input_3btdiv').appendTo(this.bottomDiv);

			// 生成确定按钮
			this.okBt = $("<div id=\"okBt\"></div>").button({
				top : 7,
				width : 74,
				text : trans("ml_ok"),
				position : 'relative',
				className : 'blue_button_div',
				onclick : function(e) {
					oThis._trigger("onok",e);
					oThis.hide();
					return true;
				}
			}).appendTo(this.okBtDiv);
				
			// 生成中间按钮
			this.middleBt = $("<div id=\"middleBt\"></div>").button({
				top : 7,
				width : 74,
				text : "执行",
				position : 'relative',
				className : 'button_div',
				onclick : function(e) {
					oThis._trigger("onmiddle",e);
					oThis.hide();
					return true;
				}
			}).appendTo(this.middleBtDiv);
				
			
			// 生成取消按钮
			this.cancelBt = $("<div id=\"cancelBt\"></div>").button({
				top : 7,
				width : 74,
				text : trans("ml_cancel"),
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
			
			this.contentDiv.css('height', this.modaldialog.getContentPane().outerHeight() - this.bottomDiv.outerHeight() + "px");
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
		 * 改变ThreeButtonsDialog的显示信息
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
	
	$.threeButtonsDialog = {
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
		showDialog : function(message, okFunc, cancelFunc, middleFunc, btntexts, obj1, obj2, obj3, zIndex, another, title) {
			var dialog = null;
			if (!another) {
				if (!window.globalObject.$c_3ButtonsDialog)
					window.globalObject.$c_3ButtonsDialog = $("<div></div>").threebuttonsdialog({
						name : "threeButtonsDialog",
						title : title,
						msg : message
					}).threebuttonsdialog('instance');
				dialog = window.globalObject.$c_3ButtonsDialog;
			} 
			else {
				dialog = $("<div></div>").threebuttonsdialog({
					name : "threeButtonsDialog",
					title : title,
					msg : message
				}).threebuttonsdialog('instance');
			}
			dialog.changeMsg(message);
			if (zIndex)
				dialog.modaldialog.element.css('z-index',zIndex);
	
			if (okFunc) {
				dialog.option.onok = function() {
					if (obj1)
						okFunc.call(this,obj1);
					else
						okFunc.call(this);
				};
			}
	
			if (cancelFunc) {
				dialog.option.oncancel = function() {
					if (obj2)
						cancelFunc.call(this,obj2);
					else
						cancelFunc.call(this);
				};
			}
			if (middleFunc) {
				dialog.option.onmiddle = function() {
					if (obj3)
						middleFunc.call(this,obj3);
					else if (obj1)
						middleFunc.call(this,obj1);			
					else
						middleFunc.call(this);
				};
			}
			
			if(btntexts != null){
				if(btntexts[0] != null){
					dialog.okBt.button("changeText",btntexts[0]);
				}
				if(btntexts[1] != null){
					dialog.middleBt.button("changeText",btntexts[1]);
				}
				if(btntexts[2] != null){
					dialog.cancelBt.button("changeText",btntexts[2]);
				}
			}
			dialog.show();
			return dialog;
		},
		/**
		 * 静态方法,隐藏对话框
		 */
		hideDialog : function() {
			var dialog = window.globalObject.$c_3ButtonsDialog;
			dialog.hide();
		}
	};
})(jQuery);
