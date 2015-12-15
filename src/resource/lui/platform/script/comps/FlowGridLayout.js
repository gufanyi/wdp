/**
 * 
 * 传入组件的宽度必须为FlowGridLayout.BASEWIDTH的整数倍 模拟的一个布局管理器,基类为panelcomp
 * 
 * @auther lxl
 * @version lui 1.0
 * 
 */
(function($) {
	
	$.flowGridLayout = {
		syncArray : [],
		resize : function(id) {
		    var layout = window.objects[id];
		    try {
		        layout.paint(true);
		    } catch(e) {}
		},
		/**
		 * 检查元素所需占用的位置是否已被其他元素占用
		 * 
		 * @private
		 */
		checkPosition : function(colSpan, colm, colNum, rowNum) {
		    for (var i = 0; i < colSpan; i++) {
		        try {
		            if (colm[colNum + i][rowNum] != null) return false;
		        } catch(error) {
		            return false;
		        }
		    }
		    return true;
		},
		/**
		 * @private
		 */
		syncAllLayouts : function() {
		    var syncArray = $.flowGridLayout.syncArray;
		    for (var i = 0; i < syncArray.length; i++) {
		        syncArray[i].sync();
		    }
		},
		/**
		 * 获取标签的DIV宽度
		 * 
		 * @private
		 */
		getLabelDivWidth : function(width) {
		    return parseInt(width, 10) - 0;
		}
	};
	/**
	 * 表单流式布局构造函数
	 * 
	 * @class 表单流式布局
	 */
	$.widget("lui.flowgridlayout", $.lui.panel, {
		options : {
			scroll : '',
			compDefaultWidth : null,
			marginTop : 6
		},
		_initParam : function() {
			this.className = "FlowGridLayout";
			// 常量设置
			this.BASEWIDTH = 120;
			this.COLSPACE = 21;
			this.ROWSPACE = 6;
			this.COMPHEIGHT = 24;
			
			this._super();
		    this.defaultwidth = $.argumentutils.getString(this.options.compDefaultWidth, this.BASEWIDTH);
		    this.basewidth = this.defaultwidth;
		    this.gridWidth = 0; // 每一列的应有宽度
		    this.colmNum = 0; // 应有的列数
		    this.lastX = 0; // 上一组件的起始X坐标,以此来计算下一组件的开始坐标
		    this.wordsdefault = 5; // 缺省的label中的字数
		    // 保存每次传入的组件
		    this.comps = [];
		    // this.labels = new Array();
		    // 记录当前绘制时画布大小
		    this.nowPaintWidth = 0;
		    this.nowPaintHeight = 0;
		    // 强制刷新,如果强制刷新,每次paint前要设置该值为true
		    this.forceRepaint = false;
		    if ($.flowGridLayout.syncArray == null) {
		    	$.flowGridLayout.syncArray = [];
		    }
		    $.flowGridLayout.syncArray.push(this);
		    if (this.wordArray == null) this.wordArray = [];

		    this.COMPHEIGHT = $.measures.getCssHeight("text_form_div_COMPHEIGHT");
		    this.labelPosition = "left";
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options, ele = this.element;
		    
			var layoutWidth = opts.width + "";
			if(layoutWidth.indexOf("%") == -1){
			  layoutWidth = opts.width + "px";
			}
			var layoutHeight = opts.height + "";
			if(layoutHeight.indexOf("%") == -1){
			  layoutHeight = opts.width + "px";
			}
			ele.css({
				'left'     : opts.left + "px",
			    'top'      : opts.top + "px",
			    'height'   : layoutHeight,
			    'width'    : layoutWidth,
			    'position' : opts.position,
			    'overflowY': this.overflow
			});
		    
		    //this.Div_gen = document.createElement("DIV");
		    //this.Div_gen.style.left = this.left + "px";
		    //this.Div_gen.style.top = this.top + "px";
		    //if (this.width.toString().indexOf("%") == -1) this.Div_gen.style.width = this.width + "px";
		    //else this.Div_gen.style.width = this.width;
		    //if (this.height.toString().indexOf("%") == -1) this.Div_gen.style.height = this.height + "px";
		    //else this.Div_gen.style.height = this.height;
		    //this.Div_gen.style.position = this.position;
		    //if (this.overflow != null) this.Div_gen.style.overflowY = this.overflow;
//		    if (!$.browsersupport.IS_CHROME && !$.browsersupport.IS_SAFARI) { //快捷栏form在chrome/safari下不出现滚动条
//		    	ele.css("overflow-x",'auto');
//		    }

		    this.rt = null;
		    $(window).on('resize',function(){
		    	$.flowGridLayout.resize(oThis.id);
		    });
//		    addResizeEvent(this.element[0],
//		    function() {
//		        $.flowGridLayout.resize(oThis.id);
//		    });
		    this.element.appendTo($('#'+this.options.parent));
		},
		/**
		 * 调用paint函数进行绘制
		 * 
		 * @private
		 */
		paint : function(isForce) {
		    //FlowGridLayout.COMPHEIGHT=24;
		    var compsCount = this.comps.length;
		    if (compsCount == 0) return;

		    var offsetWidth = this.element.outerWidth();
		    // 处于隐藏状态
		    if (offsetWidth == 0) return;
		    // 控制只有width变化时进行绘制,减少浏览器压力
		    if (this.forceRepaint == false && (isForce == null || isForce == false)) {
		        if (offsetWidth == this.nowPaintWidth) return;
		    }
		    this.nowPaintWidth = offsetWidth;

		    // 要隐藏的控件,要显示的控件
		    var visibleComps = [],
		    hiddenComps = [],
		    allComps = this.comps;

		    for (var i = 0; i < compsCount; i++) {
		        if (allComps[i].visible == true) {
		            visibleComps.push(allComps[i]);
		            var label = $(allComps[i].attachedLabel);
		            if (label && label != null) label.show();
		            if (typeof(allComps[i].show) == "function") {
		                allComps[i].show();
		            }
		        } else {
		            hiddenComps.push(allComps[i]);
		            var label = $(allComps[i].attachedLabel);
		            if (label && label != null) label.hide();
		            if (typeof(allComps[i].hide) == "function") {
		                allComps[i].hide();
		            }
		        }
		    }

		    // 将comps设置为显示控件,下面只渲染显示控件
		    this.comps = visibleComps;

		    // 得到组件的高度
		    this.compHeight = this.getCompsHeight();

		    this.gridPanel();
		    this.lastCompTotalWidth = 0;

		    var rowNumTemp = 0;
		    for (var i = 0,compCount = this.comps.length; i < compCount; i++) {
		        var colNum = this.comps[i].colNum; // 列号
		        var rowNum = this.comps[i].rowNum; // 行号
		        if ("CHECKBOX" == this.comps[i].componentType) {
		            rowNumTemp += 1;
		        }
		        // 得到组件所占用的列数
		        var compColNum = this.getColmNum(this.comps[i]);
		        // 文字的宽度,组件的宽度
		        var labelWidth = this.wordArray[colNum];
		        var compWidth = 0;
		        for (var k = colNum,count1 = colNum + compColNum; k < count1; k++) {
		        	compWidth = compWidth + this.realColmWidth[k];
		        }
		        if (this.labelPosition != 'top') {
		            compWidth = compWidth - labelWidth;
		        }
		        // 新行开始直接往面版上加组件,无需判断.但要获得组件所占的列数
		        var usedWidth = 0;
		        if (colNum != 0) {
		            // 能放下,计算已经被占用的宽度
		            for (var j = 0; j < colNum; j++) {
		            	usedWidth += this.realColmWidth[j];	
		            }
		            if (this.labelPosition == 'top') {
		                usedWidth -= 50 * colNum;
		            }
		        }
		        var top = 0;
		        if (this.labelPosition == 'top') {
		            top = (rowNum * 2 - rowNumTemp) * (this.COMPHEIGHT) + (rowNum * 10); //10文字和输入框间距
		        } else {
		            top = this.options.marginTop + rowNum * (this.COMPHEIGHT + this.ROWSPACE);
		        }
		        var left = 0;
		        if (this.labelPosition == 'top') {
		            left = usedWidth;
		        } else {
		            left = usedWidth + this.COLSPACE - 6;
		        }
		        if (this.comps[i].attachedLabel != null) {
		            $(this.comps[i].attachedLabel).css({
		            	left : left + "px",
		            	top : top + "px"
		            }).show();
		            // 调整Label宽度
		            var attachedLabelWidth = $.flowGridLayout.getLabelDivWidth(labelWidth);
		            //if (this.ellipsis == true && this.comps[i].Div_text && attachedLabelWidth > this.comps[i].Div_text.outerWidth()) {
		            //    attachedLabelWidth = this.comps[i].Div_text.outerWidth();
		            //}
		            $(this.comps[i].attachedLabel).css('width',attachedLabelWidth + "px");
		            var htmlStr = null;
		            if (this.comps[i].isRequired) 
		            	htmlStr = "<div " + (this.comps[i].componentType == "CHECKBOX" ? "": "title='" + this.comps[i].labelName + "' ") + (this.ellipsis == true ? "class='div_label_ellipsis' ": "") + "style='position:relative;float:left;width:" + (attachedLabelWidth - 12) + "px;'>" + (this.comps[i].componentType == "CHECKBOX" ? "": this.comps[i].labelName) + "</div>" + "<span style='color:red;margin-left:3px;margin-right:3px;'>*</span>";
		            else {
		                if (this.comps[i].warnIcon) {
		                    this.comps[i].warnIcon.hide();
		                }
		                htmlStr = "<div " + (this.comps[i].componentType == "CHECKBOX" ? "": "title='" + this.comps[i].labelName + "' ") + (this.ellipsis == true ? "class='div_label_ellipsis' ": "") + "style='position:relative;margin-right:10px;'>" + (this.comps[i].componentType == "CHECKBOX" ? "": this.comps[i].labelName) + "</div>";
		            }
		            $(this.comps[i].attachedLabel).html(htmlStr);
		        } else {
		            var attachedLabel = this.createLabel(this.comps[i], usedWidth, top, labelWidth);
		            this.comps[i].attachedLabel = attachedLabel;
		            this.element.append($(attachedLabel));
		        }
		        if (this.labelPosition == 'top') {
		            $(this.comps[i].attachedLabel).css('text-align','left');
		        }
		        // 编辑态下的测试
		        if (window.editMode) {
		            if (this.comps[i].attachedLabel != null) {
		                $.design.getObj({divObj:this.comps[i].attachedLabel, params:this.comps[i].params, objType:$.design.Constant.COMPOMENT_TYPE});
		            }
		        }
		        var tmpCompHeight = this.COMPHEIGHT * this.comps[i].rowSpan + this.ROWSPACE * (this.comps[i].rowSpan - 1);
		        // 设置组件的位置
		        //如果是textarea，控件占满布局，否则控件大小不变
		        var compLeft = 0;
		        if (this.labelPosition == 'top') {
		            compLeft = usedWidth;
		            top = (rowNum * 2 - rowNumTemp) * (this.COMPHEIGHT) + (rowNum * 10) + this.COMPHEIGHT;
		            compWidth = this.nowPaintWidth;
		        } else {
		            compLeft = usedWidth + labelWidth + this.COLSPACE - 6;
		            top = this.options.marginTop + rowNum * (this.COMPHEIGHT + this.ROWSPACE);
	                if(this.comps[i].colSpan>1||this.comps[i].rowSpan>1){
	                	
	                }else{
	                	if (this.comps[i].basewidth.indexOf("%") != -1) compWidth = compWidth * parseFloat(this.comps[i].basewidth) / 100;
	                    else compWidth = this.comps[i].basewidth > compWidth ? compWidth: this.comps[i].basewidth;
	                }
		            //滚动条的情况需要减去滚动	
		            
		        }
		        this.comps[i].setBounds(compLeft, top, compWidth, tmpCompHeight);
		    }
		    // wt3.stop();
		    // 记录上次创建的labels
		    this.oldLabels = this.labels;
		    // 将这次显示的labels清空
		    this.labels = [];
		    this.comps = allComps;
		    this.forceRepaint = false;
		    this.afterRepaint();
		},
		/**
		 * @private
		 */
		afterRepaint : function() {},
		/**
		 * 获取高度
		 * 
		 * @private
		 */
		getHeight : function() {
		    if (this.comps == null || this.comps.length == 0) return 0;
		    var comp;
		    var maxHeight = 0;
		    for (var i = this.comps.length - 1; i >= 0; i--) {
		        if (this.comps[i].visible != false) {
		            comp = this.comps[i];
		            var height = comp.element.outerHeight() + comp.element[0].offsetTop + 5;
		            if (height > maxHeight) maxHeight = height;
		        }
		    }
		    return maxHeight;
		},
		/**
		 * 添加要排版的组件
		 * 
		 * @private
		 */
		add : function(comp) {
			this._super(comp);
		    //PanelComp.prototype.add.call(this, comp);
		    this.comps.push(comp.owner);
		},
		/**
		 * 设置是否强制重新paint
		 * 
		 * @private
		 */
		setForceRepaint : function(isForceRepaint) {
		    this.forceRepaint = $.argumentutils.getBoolean(isForceRepaint, false);
		},
		/**
		 * 得到传入的所有panel组件
		 * 
		 * @private
		 */
		getComps : function() {
		    return this.comps;
		},
		/**
		 * 根据id获取子项对象
		 */
		getCompById : function(id) {
		    if (this.comps != null) {
		        for (var i = 0, count = this.comps.length; i < count; i++) {
		            if (this.comps[i].element.attr("id") == id) return this.comps[i];
		        }
		    }
		},
		/**
		 * 根据id删除子项对象
		 */
		removeCompById : function(id) {
		    var comp = null;
		    if (this.comps != null) {
		        var newComps = [];
		        for (var i = 0, count = this.comps.length; i < count; i++) {
		            if (this.comps[i].element.attr("id") == id) {
		                comp = this.comps[i];
		            } else {
		                newComps.push(this.comps[i]);
		            }
		        }
		        this.comps = newComps;
		    }
		    return comp;
		},
		/**
		 * 得到组件中最长名字的长度
		 * 
		 * @private
		 */
		getMaxNameWidth : function() {
		    var comps = this.comps;
		    var maxLength = 0;
		    for (var i = 0; i < comps.length; i++) {
		        if (window.editMode) {
		            var nowLength = comps[i].labelName.lengthb() * 6;
		        } else {
		            var nowLength = $.measures.getTextWidth(comps[i].labelName, this.className);
		        }
		        if (nowLength > maxLength) maxLength = nowLength;
		    }
		    return maxLength;
		},
		/**
		 * 得到指定组件的名字的长度
		 * 
		 * @private
		 */
		getCompNameWidth : function(comp) {
		    if (comp.componentType == "CHECKBOX") {
		        return 0;
		    } else {
		        if (window.editMode) {
		            return comp.labelName.lengthb() * 6;
		        } else {
		            return $.measures.getTextWidth(comp.labelName, this.className);
		        }
		    }
		},
		/**
		 * 得到传入组件的高度
		 * 
		 * @private
		 */
		getCompsHeight : function() {
		    return 24;
		},
		/**
		 * 得到所有组件的宽度 return allWidth数组
		 * 
		 * @private
		 */
		getAllCompsWidth : function(comps) {
		    var allWidth = new Array(comps.length);
		    for (var i = 0; i < comps.length; i++) allWidth[i] = comps[i].getCompWidth();
		    return allWidth;
		},
		/**
		 * 得到组件所占的列数
		 * 
		 * @private
		 */
		getColmNum : function(comp) {
		    if (comp.colSpan == -1) return this.colmNum;
		    var cols = parseInt(comp.colSpan);
		    return cols > this.colmNum ? this.colmNum: cols;
		},
		/**
		 * 划分面版为网格,得到列数,计算每一条数线的横坐标,组件只能放置在以这些横坐标开始的位置
		 * @private
		 */
		gridPanel : function(sNum) {
		    // 应划分的列数(但每一列的宽度并不是相等的,需要根据各列中的文字最长宽度加上BASEWIDTH)
		    // 这个列数实际上是预估的
		    this.basewidth = this.defaultwidth;
		    if (sNum) this.colmNum = sNum;
		    else {
		        // 如果有过此布局，则统一使用同一分组列，使外观看起来整齐
		        this.colmNum = Math.floor(this.element.outerWidth() / (this.basewidth + this.wordsdefault * 12 + this.COLSPACE));
		        if (this.colmNum < 1) this.colmNum = 1;
		    }
		    //FlowGridLayout.colmNum = this.colmNum;
		    // 实际每一列的宽度
		    this.realColmWidth = new Array(this.colmNum);
		    // 保存每一列文字的最大宽度
		    if (!this.colmWordMaxWidth) this.colmWordMaxWidth = new Array(this.colmNum);
		    // 声明数组：存放对每个列长度有贡献的组件,以次计算各列的实际宽度
		    var colm = new Array(this.colmNum);
		    for (var i = 0,count = this.colmNum; i < count; i++) colm[i] = new Array();
		    // 记录列号的
		    var temp = 0;
		    // 当前计算到的行号
		    var rowNum = 0;
		    // 通过初次扫描,获得每个组件中的文字对哪个列做贡献,进行记录,然后由此计算各列的实际宽度,取得各列的开始x坐标
		    for (var i = 0,count = this.comps.length; i < count; i++) {
		        var comp = this.comps[i];
		        if (comp.nextLine && temp != 0) {
		            temp = 0;
		            rowNum++;
		        }
		        var colSpan = this.getColmNum(comp);
		        var rowSpan = comp.rowSpan;

		        // 计算摆放位置
		        if (colSpan == 1) { // 只占一列
		            while (colm[temp] && colm[temp][rowNum] != null) { // 当前位置已被占用
		                if (comp.nextLine) { //换行显示时，只要位置被占用，就换一行
		                    temp = 0;
		                    rowNum++;
		                } else {
		                    if (temp < this.colmNum - 1) {
		                        temp++;
		                    } else {
		                        temp = 0;
		                        rowNum++;
		                    }
		                }
		            }
		        } else if (colSpan > 1) { // 占多列
		            while ($.flowGridLayout.checkPosition(colSpan, colm, temp, rowNum) == false) { // 所需位置已被占用
		                if (temp + colSpan <= this.colmNum) temp++;
		                else {
		                    temp = 0;
		                    rowNum++;
		                }
		            }
		        }

		        // 计算此组件的文字宽度,存储到相应列的数组中
		        var labelWidth = this.getCompNameWidth(comp);
		        // 添加间距
		        if (colm[temp] != null) {
		            colm[temp][rowNum] = labelWidth + this.COLSPACE;
		            if (colSpan > 1) { // 所占列数大于1
		                for (var j = 1; j < colSpan; j++) {
		                    colm[temp + j][rowNum] = 0;
		                }
		            }
		            if (rowSpan > 1) { // 所占行数大于1
		                for (var j = 1; j < rowSpan; j++) {
		                    colm[temp][rowNum + j] = labelWidth + this.COLSPACE;
		                    if (colSpan > 1) { // 所占列数大于1
		                        for (var k = 1; k < colSpan; k++) {
		                            colm[temp + k][rowNum + j] = 0;
		                        }
		                    }
		                }
		            }
		            // 设置元素的位置信息
		            comp.rowNum = rowNum;
		            comp.colNum = temp;
		        }
		        temp = temp + parseInt(colSpan);
		        if (temp > this.colmNum - 1) {
		            temp = 0;
		            rowNum++;
		        }
		    }
		    // 计算每一列文字的实际最大宽度
		    for (var i = 0,count = this.colmNum; i < count; i++) {
		        this.colmWordMaxWidth[i] = 0;
		        for (var j = 0,
		        count1 = colm[i].length; j < count1; j++) {
		            if (colm[i][j] > this.colmWordMaxWidth[i]) this.colmWordMaxWidth[i] = colm[i][j];
		        }
		        if (this.wordArray[i] == null || this.wordArray[i] < this.colmWordMaxWidth[i]) {
		            this.wordArray[i] = this.colmWordMaxWidth[i];
		        }
		        this.colmWordMaxWidth[i] = this.wordArray[i];
		    }
		    // 去掉多余的列宽度
		    if (this.wordArray.length > this.colmNum) {
		        for (var i = this.wordArray.length - 1; i >= this.colmNum; i--) {
		            this.wordArray.remove(i);
		        }
		    }
		    // 进一步调整，预估可能不正确，需要减一列来调整
		    var totalCalc = 0;
		    for (var i = 0,count = this.colmNum; i < count; i++) {
		        totalCalc += this.colmWordMaxWidth[i];
		    }
		    var offsetWidth = this.element.outerWidth();
		    // 出现了滚动条
		    if (this.element[0].scrollHeight > this.element.outerHeight()) {
		        offsetWidth -= 17;
		    }
		    var remainSpace = offsetWidth - (this.basewidth + this.COLSPACE) * this.colmNum + this.COLSPACE - totalCalc;
		    if (remainSpace < 0 && this.colmNum > 1) {
		        this.gridPanel(this.colmNum - 1);
		        return;
		    }
		    this.basewidth = this.defaultwidth + Math.floor(remainSpace / this.colmNum);
		    // 每一列的实际宽度
		    for (var i = 0,
		    count = this.colmNum; i < count; i++) {
		        this.realColmWidth[i] = this.colmWordMaxWidth[i] + this.basewidth;
		    }
		},
		/**
		 * @private
		 */
		sync : function() {
		    var wordArray = this.wordArray;
		    var needSync = false;
		    if (this.colmWordMaxWidth == null) return;
		    for (var i = 0; i < this.colmNum; i++) {
		        if (wordArray[i] > this.colmWordMaxWidth[i]) {
		            needSync = true;
		            break;
		        }
		    }
		    if (needSync) {
		        this.paint(true);
		    }
		},
		/**
		 * 根据给定参数生成label,用div + textNode实现
		 * 
		 * @param comp 组件,从此组件获得文字描述创建label
		 * @param left label的左坐标值
		 */
		createLabel : function(comp, left, top, width) {
			if (width <= 0) width = 20;
		    var divLabelWidth = $.flowGridLayout.getLabelDivWidth(width);
		    if (this.ellipsis == true && comp.Div_text && divLabelWidth > comp.Div_text.outerWidth()) {
		        divLabelWidth = comp.Div_text.outerWidth();
		    }
			var divLabel = $("<div style='text-align:right;position:absolute;width:"+ divLabelWidth +"px;height:"+this.compHeight+"px;left:"+left+"px;top:"+top+"px;line-height:"+this.compHeight+"px;text-overflow:ellipsis;white-space:nowrap'></div>");
		   
		    var htmlStr = null;
		    if (comp.isRequired) 
		    	htmlStr = "<div " + (comp.componentType == "CHECKBOX" ? "": "title='" + comp.labelName + "' ") + (this.ellipsis == true ? "class='div_label_ellipsis' ": "") + "style='position:relative;float:left;width:" + (divLabelWidth - 12) + "px;'>" + (comp.componentType == "CHECKBOX" ? "": comp.labelName) + "</div>" + "<span style='color:red;margin-left:3px;margin-right:3px;'>*</span>";
		    else {
		        if (comp.warnIcon) {
		            comp.warnIcon.hide();//.style.display = "none";
		        }
		        htmlStr = "<div " + (comp.componentType == "CHECKBOX" ? "": "title='" + comp.labelName + "' ") + (this.ellipsis == true ? "class='div_label_ellipsis' ": "") + "style='position:relative;margin-right:10px;'>" + (comp.componentType == "CHECKBOX" ? "": comp.labelName) + "</div>";
		    }
		    if (comp.labelColor) divLabel.css('color',comp.labelColor);
		    // 将文字(label名字)放到label中
		    divLabel.html(htmlStr);
		    return divLabel[0];
		}
	});
	
	/**
	 * @private
	 */
	function resizeLayout(compId) {
	    var layout = objects[compId];
	    layout.paint();
	};
})(jQuery);
