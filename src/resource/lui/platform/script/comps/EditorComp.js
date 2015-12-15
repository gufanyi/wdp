/**
 * @fileoverview WebFrame Editor控件定义文件
 * @author lxl
 * @version lui 1.0
 * 
 * 注：一个页面只能有一个EditorComp控件！（因为设置了window.currentEditor）
 */
(function($) {
	/**
	 * 高级文本编辑器构造函数
	 * 
	 * @class 高级文本编辑器
	 * @constructor
	 * @param{Array} hideBarIndices 一维数组,指定要隐藏的工具条
	 * @param{Array} hideImageIndices 二维数组,指定要隐藏的每行的按钮,形式如[[0,1],[],[2]]
	 * @param hideBarIndices 需要隐藏的行
	 */
	$.widget("lui.editor" , $.lui.base , {
		options : {
			position : 'absolute',
			className : 'cms_editor',
			toolbarType : 'Custom',
			//event
			onblur : null
		},
		_initParam : function() {
			this.componentType = "EDITOR";
			this.parentOwner = this.element.parent()[0];
			this._super();
			this.readOnly = false;
			window.clickHolders.push(this);	
		},

		/**
		 * 扩展CKEDITOR,为CKEDITOR的原型增加设置是否只读的方法
		 */
		_extendCKEditor : function() {
			var editor = this.editor;
	
		    var cancelEvent = function(evt) {
		        evt.cancel();
		    };	
		    
			editor.setReadOnly = function(isReadOnly){
		        this[isReadOnly ? 'on': 'removeListener']('key', cancelEvent, null, null, 0);
		        this[isReadOnly ? 'on': 'removeListener']('selectionChange', cancelEvent, null, null, 0);
		
		        // 置为失效所有wysiwyg模式下的命令
		        var command, commands = this._.commands,
		        mode = this.mode;
		        for (var name in commands) {
		            command = commands[name];
		            if (isReadOnly) {
		                command.disable();
		            } else {
		                command[command.modes[mode] ? 'enable': 'disable']();
		            }
		            this[isReadOnly ? 'on': 'removeListener']('state', cancelEvent, null, null, 0);
		        }
		        var i, j, k;
		        var toolbars = this.toolbox.toolbars;
		        for (i = 0; i < toolbars.length; i++) {
		            var toolbarItems = toolbars[i].items;
		            for (j = 0; j < toolbarItems.length; j++) {
		                var combo = toolbarItems[j].combo;
		                if (combo) {
		                    combo.setState(isReadOnly ? CKEDITOR.TRISTATE_DISABLED: CKEDITOR.TRISTATE_OFF);
		                }
		                var button = toolbarItems[j].button;
		                if (button && button.createPanel) {
		                    button.setState(isReadOnly ? CKEDITOR.TRISTATE_DISABLED: CKEDITOR.TRISTATE_OFF);
		                }
		            }
		        }
			};
		},
		_create : function() {
			this._initParam();
			this.element.addClass(this.className).css({
				'left' : this.options.left + "px",
				'top' : this.options.top + "px",
				'width' : this.options.width,
				'height' : this.options.height,
				'position' : this.options.position,
				'overflow' : 'auto'
			});

			this.contentId = this.element.attr("id") + "_content";
			this.element.html("<textarea name=\"" + this.contentId + "\" style=\"width:100%;height:30px;\"></textarea>");
			this._createFrame();
		},
		/**
		 * 创建编辑器
		 * 
		 * @private create()的时候调用
		 */
		_createFrame : function() {
			var oThis = this;
			CKEDITOR.basePath = window.frameGlobalPath + "/ui/editor/";
			var spanHeight = 0;
			if (this.options.toolbarType == "Full")
				spanHeight = this.element.outerHeight() * 0.6;
			else if (this.options.toolbarType == "Custom")	
				spanHeight = this.element.outerHeight() * 0.78;
			var langCookie = getCookie('LA_K1');
			var lang = 'zh-cn';
			if (langCookie == 'english') // 英文
				lang = 'en';
			else if (langCookie == 'tradchn') //中文繁体
				lang = 'zh';
					
			if (spanHeight > 0){
				CKEDITOR.replace(
					this.contentId,
					{
						height:spanHeight,toolbar:this.options.toolbarType,language:lang,
						filebrowserUploadUrl : '/wfw/ckeditor/uploader?Type=File',
						filebrowserImageUploadUrl : '/wfw/ckeditor/uploader?Type=Image',
						filebrowserFlashUploadUrl : '/wfw/ckeditor/uploader?Type=Flash'
					}
				);
			} else {
				CKEDITOR.replace(
					this.contentId,
					{
						language:lang,
						filebrowserUploadUrl : '/wfw/ckeditor/uploader?Type=File',
						filebrowserImageUploadUrl : '/wfw/ckeditor/uploader?Type=Image',
						filebrowserFlashUploadUrl : '/wfw/ckeditor/uploader?Type=Flash'
					}
				);
			}
			var editor1 = CKEDITOR.instances[this.contentId];
			oThis.editor = editor1;
			
			//扩展CKEditor
			oThis._extendCKEditor();
			
			//判断是否是只读模式，如果不是再注册onblur事件
			if (oThis.readOnly) {
				//等editor初始化后设置只读
				window.setTimeout(function(){
					oThis.editor.setReadOnly(true);
				},500);
			} else {
				editor1.on("blur", function(e) {
					oThis._trigger('onblur',e);
				});
			}
		},
		/**
		 * 清除内容
		 * 
		 * @private
		 */
		cleanHtml : function() {
			var win = this.editorWindow;
//			if ($.browsersupport.IS_IE) {
//				var fonts = win.document.body.all.tags("FONT");
//			} else {
				win = this.frame.contentWindow;
				var fonts = win.document.getElementsByTagName("FONT");
//			}
			var curr;
			for ( var i = fonts.length - 1; i >= 0; i--) {
				curr = fonts[i];
				if (curr.style.backgroundColor == "#ffffff")
					curr.outerHTML = curr.innerHTML;
			}

		},
		/**
		 * 插入内容
		 * 
		 * @private
		 */
		oblog_InsertSymbol : function(str1) {
			this.editorWindow.focus();
			var oblog_selection;
			if(this.oblog_selection){
				oblog_selection = this.oblog_selection;
			}else{
				oblog_selection = this.oblog_selectRange();
			}
			
//			if ($.browsersupport.IS_IE){
//				var oRange = oblog_selection.createRange();
//				oRange.pasteHTML(str1);
//			}
				
//			else {
		        var spanElement = document.createElement("span");
		        spanElement.innerHTML = str1;
				EditorComp.insertElement(this.frame.contentWindow, spanElement);
//			}
		},

		/**
		 * 设值
		 */
		setValue : function(value) {
			if (!this.editor) return;
			if (value != null){
				value = decodeURIComponent(value);
				this.editor.setData(value);
			} else {
				this.editor.setData("");
			}
		},
		/**
		 * 追加值
		 */
		appendValue : function(value) {
			if(value != null){
				if(this.editor){
					var text = this.getText();
					text = text + value;
					this.editor.setData(text);
				}
			}
		},
		/**
		 * 插入值
		 */
		insertValue : function(value) {
			if(value != null){
				if(this.editor)
					this.editor.insertHtml(value);
			}
		},
		/**
		 * 获取editor的内容
		 */
		getValue : function() {
			if(this.editor){
				try{
					return encodeURIComponent(this.editor.getData());
				}catch(e){}
			}
			return null;
		},
		/**
		 * 获取editor中的文本
		 */
		getText : function() {
			if(this.editor){
				//if(this.editor.html)
				return this.editor.document.getBody().getText();
			}
			return null;
		},
		/**
		 * 设置可编辑状态
		 */
		setActive : function(active) {
			var oThis = this;
			window.setTimeout(function(){
				oThis.readOnly = !active;
				oThis.editor.setReadOnly(oThis.readOnly);
			},1000);
		},
		/**
		 * editor之外点击事件
		 */
		outsideClick : function(e) {
			this._trigger("onblur",e);
		},
		/**
		 * 获取对象信息
		 * 
		 * @private
		 */
		getContext : function() {
			var context = {};
			context.c = "EditorContext";
			context.id = this.element.attr("id");
			context.enabled = !this.disabled;
			context.value = this.getValue();
			// 处理连接符(+,&)丢失问题
			context.value = context.value.replace(/\+/g, "%2B");
			context.value = context.value.replace(/\&/g, "%26");
			return context;
		},
		/**
		 * 设置对象信息
		 * 
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.readOnly != null)
				this.setActive(!context.readOnly);
			if (context.value != null && context.value != this.getValue())
				this.setValue(context.value);
		}
	});
})(jQuery);
