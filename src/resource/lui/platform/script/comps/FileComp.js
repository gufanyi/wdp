/**
 * 文件上传控件
 */
(function($) {
	$.widget("lui.file" , $.lui.textfield , {
		options : {
			dataType : 'A',
			type : 'forminput',    //包括"link"和"forminput"两种模式
			value : null,
			text : '附件'
		},
		_initParam : function() {
			this.componentType = "FILECOMP";
			//按钮图片宽高
			this.imageWidth = 19;
			this.imageHeight = 22;
			this._super();
		},
		_create : function() {
			var ele = this.element,opts = this.options,oThis = this;
			ele.fileListPanel({
				files : opts.value ? opts.value.files : [],
				forderCode : opts.value ? opts.value.forderCode : null,
				ok : function(e, files) {
					var oldValue = opts.value;
					opts.value = files;
					oThis._setValue(files);
					oThis.valuechanged(e,oldValue,files)
					this.ctxChanged = true;
				}
			});
			if(opts.type === 'link') {
				ele.css({
					'position' : opts.position,
					'left' : opts.left + "px",
					'top' : opts.top + "px",
					'width' : opts.width,
					'height' : opts.height
				});
				$("<a href='javascript:void(0);'>").appendTo(ele).addClass("file-link").html(opts.text).on('click',function() {
					oThis.click(e);
				});
			} else {
				this._super();
			}
		},
		_managerSelf : function() {
			this._super();
			
			var oThis = this,opts = this.options,ele = this.element;
			var width = this.Div_text.outerWidth();
			if (width == 0 && !$.argumentutils.isPercent(opts.width))
				width =  parseInt(opts.width);
			var inputWidth = width - this.imageWidth;
			this.input.css({
				'width' : inputWidth - 2 + "px", //2 按钮图片
				'position' : 'relative',
				'float' : 'left'
			});
			
			this.input.attr('readOnly',true);
			this.input.off('blur').on('blur',function(e){
				if(oThis.Div_text.children().length == 3){
					var children = oThis.Div_text.children();
					$.each(children,function(index,item){
						$(item).attr('class',$(item).attr('class').replaceStr('input_highlight','input_normal'));
					});
					oThis.input.removeClass('input_highlight_center_bg').addClass('input_normal_center_bg');
				}
				oThis.blur(e);
				
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
			
			this.divButton = $("<div></div>").attr({
				'id' : ele.attr("id") + '__file_sel_button'
			}).addClass("file-arrow-nm").css({
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
				this.input.css('width',inputWidth - 2 + 'px');//2 按钮图片右间距
			}
			
			var divButtonClickHandler = function(e) {
				oThis.click(e);
			};
			
			var divButtonMouseoverHandler = function(e) {
				$(e.target).removeClass().addClass('file-arrow-on');
			};
			
			var divButtonMouseoutHandler = function(e) {
				$(e.target).removeClass().addClass('file-arrow-nm');
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
		},
		/**
		 * 聚焦后执行方法
		 */
		focus : function(e) {
			this.oldValue = this.getValue();
			// 为避免tab键进入密码框时不能输入字符的bug
			if (this.visible) {
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
			if (readOnly) {
				this.Div_text.attr('class',this.options.className + " " + this.options.className + "_readonly").children().css('background-color','#E1E1E1');
				this.input.attr('class',this.inputClassName_init + " text_input_readonly");
				this.divButton.attr('class','file-arrow-nm');
			} else {
				this.Div_text.attr('class',this.options.className).children().css('background-color','#FFFFFF');
				this.input.attr('class',this.inputClassName_init);
				this.divButton.attr('class','file-arrow-nm');
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
		 */
		blur : function(e) {
			// 调用用户的处理方法
			this._trigger("onblur",e);
		},
		getValue : function() {
			return this.options.value;
		},
		click : function() {
			this.element.fileListPanel("show");
		},
		setValue : function(value) {
			if(value != this.options.value ) {
				var oldValue = this.options.value;
				this.options.value = value;
				this.element.fileListPanel('setValues',value);
				this._setValue(value);
				oThis.valuechanged(e,oldValue,value)
				this.ctxChanged = true;
			}
			
		},
		_setValue : function(value) {
			var fileNameArray = [];
			if(value && value.files) {
				for(var i=0;i<value.files.length;i++) {
					fileNameArray.push(value.files[i].name);
				}
				this.input.val(fileNameArray.join(','));
			}
		},
		/**
		 * 获取对象信息
		 */
		getContext : function() {
			return {
				c : "FileContext",
				id : this.element.attr("id"),
				fileValue : this.getValue()
			};
		},
		setContext : function(context) {
			if(context.fileValue) {
				this.setValue(context.fileValue)
			}
			this.ctxChanged = false;
		}
	});
	
	$.widget("lui.fileListPanel" , $.lui.base , {
		options : {
			title : '文件管理',
			files : [],
			forderCode : null,
			//event
			ok : null
		},
		_initParam : function() {
			this.width = 300;
			this.height = 400;
			if(this.options.files && this.options.files.length == 0) {
				this.options.forderCode = new Date().getTime();
			}
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options, ele = this.element;
			this.modaldialog = $("<div></div>").appendTo("body").modaldialog({
				title : opts.title,
				left : opts.left,
				top : opts.top,
				width : this.width,
				height : this.height,
				onclosing : function(e) {
					oThis._trigger('ok',e,{forderCode : opts.forderCode , files : opts.files});
				}
			}).modaldialog("instance");
				
			this.modaldialog.contentDiv.css('overflow','hidden');
			this.contentDiv = $("<div>").addClass('file-list').appendTo(this.modaldialog.getContentPane()).on({
				// 拖拽进入目标对象时触发
				 "dragenter" : function(event) { 
					 event.stopPropagation(); 
					 event.preventDefault(); 
				 },
				 // 拖拽在目标对象上时触发
				 "dragover" : function(event) { 
					 event.stopPropagation(); 
					 event.preventDefault(); 
				 },
				 // 拖拽结束时触发
				 "drop" : function(event) {
					 var files = event.originalEvent.dataTransfer.files; 
					 event.stopPropagation(); 
					 event.preventDefault();
					 oThis._operFileList(files);
				 } 
			});
			
			var fileInput = $("<input type='file' multiple='multiple'>").appendTo(this.contentDiv).hide().on('change',function() {
				var files = $(this).get(0).files;
				oThis._operFileList(files)
			});
			var hintDiv = $("<div>").addClass('file-hint').appendTo(this.contentDiv).html('点击浏览或拖拽文件');
			
			var bottomDiv = $("<div></div>").addClass('file-bottomdiv').appendTo(this.modaldialog.getContentPane());
			var cancelBtDiv = $("<div></div>").addClass('file-cancelbtdiv').appendTo(bottomDiv);
			var browseBtDiv = $("<div></div>").addClass('file-browsebtdiv').appendTo(bottomDiv);
			// 生成确定按钮
			this.browseBt = $("<div>").button({
				top : 7,
				width : 74,
				text : "浏览",
				position : 'relative',
				className : 'button_div',
				onclick : function(e) {
					fileInput.get(0).click();
				}
			}).appendTo(browseBtDiv);
			
			// 生成确定按钮
			this.cancelBt = $("<div>").button({
				top : 7,
				width : 74,
				text : "确定",
				position : 'relative',
				className : 'button_div',
				onclick : function(e) {
					oThis.hide();
					oThis._trigger('ok',e,{forderCode : opts.forderCode , files : opts.files});
					return true;
				}
			}).appendTo(cancelBtDiv);
			this.contentDiv.css('height',this.modaldialog.getContentPane().outerHeight() - bottomDiv.outerHeight() + "px");
			
			if(opts.files && opts.files.length > 0) {
				this.setValues(opts);
			}
		},
		_operFileList : function(files) {
			this.contentDiv.find('.file-hint').hide();
			for(var i=0,count=files.length ; i<count ; i++) {
				var _file = {
					name : files[i].name,
					size : files[i].size
				};
				if(this._fileIndex(_file) >= 0 ) {
					return;
				}
				this._uploadFile(files[i] , this._createFileList(_file));
			}
		},
		_createFileList : function(file,state) {
			var that = this;
			var fileListItem = $("<div>").addClass('file-list-item').appendTo(this.contentDiv);
			var fileProgress = $("<div>").addClass('upload-progress').appendTo(fileListItem);
			var fileContent =  $("<div>").addClass('file-content').appendTo(fileListItem);
			var fileImg = $("<div>").addClass('file-img').appendTo(fileContent);
			var fileInfo = $("<div>").addClass('file-info').appendTo(fileContent);
			var fileNameInfo = $("<div>").addClass('file-name-info').appendTo(fileInfo);
			$("<span>").addClass('file-name').appendTo(fileNameInfo).text(file.name).attr('title',file.name);
			$("<span>").addClass('file-del').appendTo(fileNameInfo).html("删除").on('click',function() {
				fileListItem.fadeOut('fast',function() {
					fileListItem.data('xhr').abort();
					that.options.files.splice(that._fileIndex(file),1);
					fileListItem.remove();
					if(that.contentDiv.children('.file-list-item').length == 0) {
						that.contentDiv.find('.file-hint').show();
					}
					var url = window.globalPath + $.file.DELETE_URL + "?filename=" + encodeURIComponent(encodeURIComponent(file.name)) + "&fordercode=" + that.options.forderCode;
					$.post(url);
				});
			});
			var fileSizeInfo = $("<div>").addClass('file-size-info').appendTo(fileInfo);
			var fileSize = $("<span>").addClass('file-size').appendTo(fileSizeInfo).text("0KB/" + $.file._transFileSize(file.size));
			$("<span>").addClass('file-state').appendTo(fileSizeInfo);
			this._resizeFileList();
			if(state === 'init') {
				fileProgress.addClass('upload-progress-complete').css('width','100%')
				fileImg.addClass('file-success');
				fileSize.text($.file._transFileSize(file.size));
			} else {
				this.options.files.push(file);
			}
			return fileListItem;
		},
		_uploadFile : function(file, fileItemDiv) {
			var url = window.globalPath + $.file.UPLOAD_URL + "?filename=" + encodeURIComponent(encodeURIComponent(file.name)) + "&fordercode=" + this.options.forderCode;
			var fd = new FormData();
            fd.append("fileToUpload", file);
            var xhr = new XMLHttpRequest();
            fileItemDiv.data('xhr',xhr);
            xhr.jqObj = fileItemDiv;
            xhr.fileName = file.name;
            xhr.forderCode = this.options.forderCode;
            var upload = xhr.upload;
            upload.jqObj = fileItemDiv;
            upload.addEventListener("progress", $.file._uploadProgress, false);
            xhr.addEventListener("load", $.file._uploadComplete, false);
            xhr.addEventListener("error", $.file._uploadFailed, false);
            xhr.addEventListener("abort", $.file._uploadCanceled, false);
            xhr.open("POST", url, true);
            xhr.send(fd);
		},
		_resizeFileList : function() {
			this.contentDiv.children("div:not(.file-hint)").each(function() {
				var _fileContent = $(this).find('.file-content');
				var _fileInfoDiv = $(this).find('.file-info');
				_fileInfoDiv.css('width',(_fileContent.innerWidth() - 12 -34) + "px");
				
				var _fileNameInfo = $(this).find('.file-name-info');
				var _fileNameSpan = $(this).find('.file-name');
				var _fileDelSpan = $(this).find('.file-del')
				_fileNameSpan.css('width', (_fileNameInfo.innerWidth() - _fileDelSpan.outerWidth() - 6) + "px");
			});
		},
		_fileIndex : function(file) {
			for(var i=0;i<this.options.files.length;i++) {
				if(file.name == this.options.files[i].name && file.size == this.options.files[i].size) {
					return i;
				}
			}
			return -1;
		},
		setValues : function(opts) {
			this.options.files = [];
			this.contentDiv.find('.file-hint').hide();
			this.contentDiv.children("div:not(.file-hint)").remove();
			for(var i=0;i<opts.files.length;i++) {
				this._createFileList(opts.files[i],'init');
			}
			this.options.forderCode = opts.forderCode;
		},
		show : function() {
			this.modaldialog.show();
		},
		hide : function() {
			this.modaldialog.hide();
		}
	});
	
	$.file = {
		UPLOAD_URL : "/pt/file/upload",
		DOWNLOAD_URL : "/pt/file/down",
		DELETE_URL : "/pt/file/delete",
		
		showFileListPanel : function(opts) {
			if(!$.file.fileListPanel) {
				$.file.fileListPanel = $("<div>").fileListPanel({
					ok : function(e,param) {
					    var proxy =  $.serverproxy.getObj({async:false});
					    proxy.addParam({
					    	"clc" : opts.classname,
					    	"m_n" : opts.method,
					    	'fileInfo' : param
					    });
					    var sbr = $.submitrule.getObj();
					    proxy.setSubmitRule(sbr);
					    proxy.execute();
					}
				}).fileListPanel("instance");
			}
			$.file.fileListPanel.show();
		},
		_uploadProgress : function(evt) {
			if (evt.lengthComputable) {
				var percentComplete = Math.round(evt.loaded * 100 / evt.total);
				this.jqObj.find('.upload-progress').css('width',percentComplete.toString() + '%');
				this.jqObj.find('.file-size').text($.file._transFileSize(evt.loaded)+"/"+$.file._transFileSize(evt.total));
				this.jqObj.find('.file-img').removeClass('file-uploading file-error file-success').addClass('file-uploading');
            }
		},
		_uploadComplete : function(evt) {
			var that = this;
			this.jqObj.find('.file-state').text("上传完成");
			this.jqObj.find('.upload-progress').addClass('upload-progress-complete').css('width','100%');
			this.jqObj.find('.file-img').removeClass('file-uploading file-error file-success').addClass('file-success');
			var fileNameJq = this.jqObj.find('.file-name');
			var fileNameText = fileNameJq.text();
			fileNameJq.empty();
			$("<a href='javascript:void(0)'>").appendTo(fileNameJq).text(fileNameText).on('click',function() {
				if($('#fileDownLoad_iframe_forFileComp').size() == 0) {
					$("<iframe>").appendTo('body').attr('id','fileDownLoad_iframe_forFileComp').hide();
				}
				var url = window.globalPath + $.file.DOWNLOAD_URL + "?filename=" + encodeURIComponent(encodeURIComponent(that.fileName)) + "&fordercode=" + that.forderCode;
				$('#fileDownLoad_iframe_forFileComp').attr('src',url);
			});
		},
		_uploadFailed : function(evt) {
			this.jqObj.find('.file-img').removeClass('file-uploading file-error file-success').addClass('file-error');
		},
		_uploadCanceled : function(evt) {
			
		},
		_transFileSize : function(size) {
			if(size) {
				var fileSize = 0;
	            if (size > 1024 * 1024)
					fileSize = (Math.round(size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
				else
					fileSize = (Math.round(size * 100 / 1024) / 100).toString() + 'KB';
				return fileSize;
			}
			return "0KB";
			
		}
	}
})(jQuery);
