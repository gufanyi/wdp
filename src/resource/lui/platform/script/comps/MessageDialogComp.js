/**
 * @fileoverview MessageDialogComp组件。
 * @auther lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 信息对话框构造函数
	 * @class 信息对话框类，通过输入指定字符串和显示图片提供此对话框。<br>
	 *        <br>
	 * 
	 * <b>组件皮肤设置说明：</b><br>
	 * modaldialog.css对对话框中显示字体的外观控制，如字体，大小等。<br>
	 * <br>
	 * 
	 * <b>使用js，直接通过new的用法生成MessageDialog实例：</b><br>
	 * messagedialog = MessageDialogComp.showDialog(msg); // 用户需要显示的字符串代替msg
	 * @constructor MessageDialogComp构造函数
	 * @version 1.0
	 * 
	 * @param name 控件的名称(也就是id)
	 * @param title 对话框标题
	 * @param left 控件左部x坐标
	 * @param top 控件顶部y坐标
	 * @param msg 显示的信息,用户输入
	 * @param refImg 信息对话框左侧的显示图片的url
	 */
	$.widget("lui.messagedialog", {
		options : {
			name : "",
			title : "",
			msg : "",
			refImg : "",
			left : 0,
			top : 0,
			//event
			onclick : null
		},
		_initParam : function() {
			this.componentType = "MESSAGEDIALOG";
			this.HEIGHT = 196;
			this.WIDTH = 348;
			this.refImgPath = "/comps/dialog/images/tip4.png";
			
			this.width = this.WIDTH;
			this.height = this.HEIGHT;

			this.tmpDiv = $("<div></div>").html($.argumentutils.getString(this.options.msg, ""));

			this.options.refImg = $.argumentutils.getString(this.options.refImg, window.themePath + this.refImgPath);
			// 是否显示倒计时
			this.isShowSec = false;
		},
		_create : function() {
			this.element.hide();
			this._initParam();
			var oThis = this,opts = this.options;
			this.modaldialog = $("<div></div>").modaldialog({
				name : opts.name,
				title : opts.title,
				left : opts.left,
				top : opts.top,
				width : this.width,
				height : this.height
			}).appendTo("body").modaldialog("instance");
			
			this.modaldialog.contentDiv.css('overflow','hidden');

			this.contentDiv = $("<div></div>").addClass('message_contentdiv').appendTo(this.modaldialog.getContentPane());
			this.leftDiv = $("<div></div>").addClass('message_leftdiv').appendTo(this.contentDiv);
			this.rightDiv = $("<div></div>").addClass('message_rightdiv').html("<div class='rightInnerDiv' style='font-size:14px'></div>").appendTo(this.contentDiv);
			this.msgTd = this.rightDiv.children().first();
			this.imgNode = $("<img/>").attr({
				'src' : opts.refImg
			}).appendTo(this.leftDiv);

			this.bottomDiv = $("<div></div>").addClass('message_bottomdiv').appendTo(this.modaldialog.getContentPane());

			//左下方提示信息
			this.messageDiv = $("<div></div>").addClass('message_msgdiv').html(trans("ml_dialogclose_msg1") + "<span id='secMsgDiv'>4</span>" + trans("ml_dialogclose_msg2"));
			var a = $("<a href=\"javascript:void(0)\"></a>").html(trans("ml_cancel")).on('click',function(e){
				oThis.cancelSec = true;
				oThis.messageDiv.css('visibility','hidden');
				e.stopPropagation();
			}).appendTo(this.messageDiv);
			this.bottomDiv.append(this.messageDiv);

			this.okBtDiv = $("<div></div>").addClass('message_okbtdiv').appendTo(this.bottomDiv);
			this.cancelBtDiv = $("<div></div>").addClass('message_cancelbtdiv');
//			if (!$.browsersupport.IS_IE) {
				this.okBtDiv.css('text-align','-moz-center');
//			}
			// 生成确定按钮
			this.okBt = $("<div id=\"okBt\"></div>").button({
				top : 7,
				width : 74,
				text : trans("ml_understand"),
				position : 'relative',
				className : 'blue_button_div',
				onclick : function(e) {
					oThis._trigger("onclick",e);
					oThis.hide();
				}
			}).appendTo(this.okBtDiv);
			this.modaldialog.close = function(){
				oThis.close();
			};
			this.okBt.button("instance").element.css('margin','0 auto');
			this.contentDiv.css('height',this.modaldialog.getContentPane().outerHeight() - this.bottomDiv.outerHeight() + "px");
		},
		/**
		 * 改变MessageDialog的显示信息
		 * 
		 * @param msg 要显示的提示信息文字
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
			if(!this.isShowSec){
				this.messageDiv.css('visibility','hidden');
			}else{
				this.messageDiv.css('visibility','');
			}
			this.modaldialog.show();
			this.adjustContentdivWidth();
			
			var height = this.msgTd.outerHeight();
			if(height <= 0){
				height = $.measures.getTextHeight(this.options.msg);
			}
			var marginTop = 59 - height/2;
			marginTop = marginTop > 0 ? marginTop : 0; 
			this.msgTd.css('margin-top',marginTop + "px");
			
			$("#secMsgDiv").html(4);
			if(this.isShowSec){
				this.secChange();
			}
		},
		secChange : function(){
			var oThis = this;
			if(this.secTimeout){
				window.clearTimeout(this.secTimeout);
			}
			if (this.cancelSec && this.cancelSec == true){
				return;
			}
			var sec = parseInt($("#secMsgDiv").html(), 10);
			if(sec > 0){
				sec = sec - 1;
				$("#secMsgDiv").html(sec);
				this.secTimeout = window.setTimeout(function(){oThis.secChange();}, 1000);
			}else{
				this.hide();
			}
		},
		adjustContentdivWidth : function(){
			//this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth + "px";	
		},
		/**
		 * 隐藏对话框
		 * 
		 * @private
		 */
		hide : function() {
			this.modaldialog.hide();
		},
		close : function() {
			this.hide();
		}
	});
	
	$.messageDialogComp = {
		showDialog : function(message, title, btnText ,func, isShowSec) {
			var oThis = this;
			if (!window.globalObject.$c_MessageDialog){
				window.globalObject.$c_MessageDialog = $("<div>").messagedialog({
					name : "MessageDialog",
					title : trans("ml_messagedialog"),
					msg : message
				}).messagedialog("instance");
			}
			var dialog = window.globalObject.$c_MessageDialog;
			dialog.changeMsg(message);
			if(title != null)
				dialog.modaldialog.setTitle(title);
			if(btnText != null)
				dialog.okBt.button("changeText",btnText);
			if(typeof(isShowSec) != 'undefined'){
				dialog.isShowSec = isShowSec;
			}
			dialog.show();
			if(func!=null){
				dialog.okBt.on("buttononclick",function(e){
					//执行后台传来的function
					func.call(this);
					$.messageDialogComp.hideDialog();
					dialog.onclick.call(this,e);
				});
				dialog.close = function(){
					func.call(this);
					$.messageDialogComp.hideDialog();
				};
			}
			return dialog;
		},
		/**
		 * 静态方法,隐藏对话框
		 */
		hideDialog : function() {
			try {
				var dialog = window.globalObject.$c_MessageDialog;
				dialog.hide();
			} catch (error) {
			}
		}
	};
	
})(jQuery);
