/**
 * 
 * 固定布局管理器
 * 
 * @auther lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 表单固定布局构造函数
	 * @class 表单固定布局
	 */
	$.widget("lui.fixedgridlayout", $.lui.panel, {
		options : {
			scroll : '',
			compDefaultWidth : 120,
			marginTop : 6,
			rowHeight : 24,
			columnCount : 2,
			renderType : null
		},
		_initParam : function() {
			this.className = "FixedGridLayout";
			// 额外的空白高度
			this.SPACE_HEIGHT = 10;
			
			this._super();
			
		    this.defaultwidth = this.options.compDefaultWidth;
		    this.basewidth = this.defaultwidth;
		    this.gridWidth = 0; // 每一列的应有宽度
		    this.colmNum = 0; // 应有的列数
		    this.lastX = 0; // 上一组件的起始X坐标,以此来计算下一组件的开始坐标
		    this.wordsdefault = 5; // 缺省的label中的字数
		    // 保存每次传入的组件
		    this.comps = [];
		    // this.labels = new Array();
		    this.currentRow = null;
		    //记录每列最大宽度（只计算固定宽度的） 固定居中布局使用 
		    this.maxCellsWidth = new Array(this.options.columnCount);
		    //记录每列中控件最大宽度（只计算固定宽度的） 固定居中布局使用
		    this.maxCompsWidth = new Array(this.options.columnCount);
		    //记录每列中控件最大Label  
		    this.maxLabelWidth = new Array(this.options.columnCount);
		    // 强制刷新,如果强制刷新,每次paint前要设置该值为true
		    this.forceRepaint = false;
		    if (this.syncArray == null) {
		        this.syncArray = [];
		    }
		    this.syncArray.push(this);
		    if (this.wordArray == null) this.wordArray = [];
		    this.COMPHEIGHT = $.measures.getCssHeight("text_form_div_COMPHEIGHT");
		    this.labelPosition = "left";
		},
		_create : function() {
			this._initParam();
			this.element.css({
				left : this.options.left + "px",
				top : this.options.top + "px",
				width : "100%",
				height : "100%",
				position : this.options.position,
				overflowY : "auto",
				overflowX : "hidden"
			});
		    //创建table
		    this.table = this.createTable();
		    this.element.append(this.table).appendTo($('#'+this.options.parent));
		    this.rt = null;
		},
		/**
		 * 调用paint函数进行绘制
		 * @private
		 */
		paint : function() {
			var opts = this.options,ele = this.element;
		    var compsCount = this.comps.length;
		    if (compsCount == 0) return;
		    var offsetWidth = ele.outerWidth();
		    // 处于隐藏状态
		    if (offsetWidth == 0) return;
		    //固定布局时，label宽度统一为列宽减去form上定义的控件宽度
		    if (opts.renderType == 5) {
		        var lableWidth = (parseInt(offsetWidth) / parseInt(opts.columnCount)) - parseInt(this.basewidth);
		        if (lableWidth > 0) this.fixedLabelWidth = lableWidth; //(parseInt(offsetWidth) / parseInt(this.columnCount)) - parseInt(this.basewidth);   
		    }
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
		    //重绘table
		    this.table.remove();
		    this.table = this.createTable();
		    this.table.css("margin","0px auto");
		    //解决ie8下总有一个滚动条在的问题
//		    if ($.browsersupport.IS_IE8) this.table.css("overflow-y","hidden");
		    ele.append(this.table);
		    //记录当前行索引
		    var nowRow = 0;
		    //记录当前行的控件个数
		    var count = 0;
		    //记录当前第几行
		    var rowIndex = 0;
		    //记录所有行的占用span数
		    var labels = new Array(opts.columnCount);
		    //当前行
		    var currentRow = null;
		    //记录布局中被占用的cell
		    var gridGraph = [];
		    for (var i = 0,
		    compCount = this.comps.length; i < compCount; i++) {
		        var colSpan = this.comps[i].colSpan;
		        var rowSpan = this.comps[i].rowSpan;
		        // 新行开始，加上行标
		        if (count == 0) {
		            currentRow = this.createRow();
		        }
		        // 加上跨行元素宽度
		        if (gridGraph[nowRow] == null) gridGraph[nowRow] = [];
		        while (gridGraph[nowRow][count] != null) {
		            count++;
		        }
		        // 检查越界
		        if (count > opts.columnCount) count = opts.columnCount;
		        // 检查是否一个元素就超出了界限
		        if (colSpan > opts.columnCount) colSpan = opts.columnCount;
		        // 如果加上当前控件越界,则补齐当前行td数，转到下一行
		        if (count + parseInt(colSpan) > opts.columnCount) {
		            for (; count < opts.columnCount; count++) this.createNullCell(currentRow);
		            i--;
		            count = 0;
		            nowRow++;
		            continue;
		        }
		        //如果是强制下一行，并且当前行并不是新起的行,则根据是否已经是新行进行判断
		        else if (count != 0 && this.comps[i].nextLine) {
		            for (; count < opts.columnCount; count++) this.createNullCell(currentRow);
		            i--;
		            count = 0;
		            nowRow++;
		            continue;
		        } else {
		            for (var row = 0; row < rowSpan; row++) {
		                if (gridGraph[row + nowRow] == null) gridGraph[row + nowRow] = [];
		                for (var col = 0; col < colSpan; col++) {
		                    gridGraph[row + nowRow][col + count] = 1;
		                }
		            }
		            var cell = this.createCell(currentRow, rowSpan, colSpan);
		            //创建控件元素
		            this.renderOneElement(cell, this.comps[i], count, labels);
		            //是否绑定下一个
		            if (this.comps[i].isAttachNext == true && this.comps[i + 1] != null) this.renderOneElement(cell, this.comps[++i], count, labels);
		            count += parseInt(colSpan, 10);
		            //处理跨行
		        }
		        if (count == opts.columnCount) {
		            this.createNullCell(currentRow);
		            count = 0;
		            nowRow++;
		        }
		    }
		    if (opts.renderType == 1) this.reSetBounds(this.comps, labels);
		    //ie8下table的最后一行的rowspan不好用
//		    if ($.browsersupport.IS_IE8 && currentRow != null) {
//		        var rSpan = parseInt(currentRow.attr("rSpan"));
//		        for (var i = 1; i < rSpan; i++) {
//		            this.createRow();
//		        }
//		    }
		    this.comps = allComps;
		    this.afterRepaint();
		},
		renderOneElement : function(cell, comp, count, labels) {
		    comp.colIndex = count;
		    var table = this.createTable();
		    table.attr({
		    	"width" : "100%",
		    	"height" : "100%"
		    });
		    cell.append(table);
		    if (this.labelPosition == 'top') {
		        var labelRow = $("<tr></tr>").appendTo(table);
		    }
		    var row = $("<tr></tr>").appendTo(table);
		    if ($.browsersupport.IS_IE8 && comp.rowSpan != null && comp.rowSpan > 1) {
		        table.attr("height", comp.rowSpan * this.options.rowHeight);
		    }
		    //	row.setAttribute("height","100%");
		    //创建label
		    var label = comp.labelName;
		    //	if(label != null && label != ""){
		    // checkbox不设置label
		    if (comp.componentType == "CHECKBOX") label = "";
		    var labelCell;
		    if (this.labelPosition == 'top') {
		        labelCell = $("<td></td>").appendTo(labelRow);
		        labelCell.css("text-align","left");
		    } else {
		        labelCell = $("<td></td>").appendTo(row);
		        labelCell.css("text-align","right");
		        labelCell.attr("valign", "top");
		    }
		    if (comp.labelColor) {
		        if (comp.labelColor == "null") comp.labelColor = "";
		        labelCell.css("color",comp.labelColor);
		    }
		    var innerHTML;
		    //计算lable宽度时用到
		    var lableExWidth = 0;
		    //必填项，加红色 *
		    if (comp.isRequired && label != null && this.editable) {
		        innerHTML = label + '<span style="color: red; padding-left: 3px; padding-right: 3px;">*</span>';
		        lableExWidth = 12;
		    } else {
		        if (comp.warnIcon) {
		            comp.warnIcon.hide();//.style.display = "none";
		        }
		        innerHTML = "<div style='margin-right:10px;'>" + $.argumentutils.getString(label, "") + "</div>";
		        lableExWidth = 10;
		    }
		    labelCell.html(innerHTML);
		    if (this.fixedLabelWidth) {
		        labelCell.css('width',this.fixedLabelWidth + "px");
		        //labelCell.style.minWidth = this.fixedLabelWidth + "px";
		    } else this.dealLabelWidth(labelCell, $.argumentutils.getString(label, ""), lableExWidth, count, labels);
		    //	}
		    var compCell = $("<td></td>").appendTo(row).attr("valign", "top").append(comp.element);
//		    if ($.browsersupport.IS_IE8 && comp.option.height == "100%" && comp.rowSpan != null && comp.rowSpan > 1) 
//		    	comp.element.css('height',comp.rowSpan * this.options.rowHeight + "px");
		    if (this.options.renderType == 1 && comp.basewidth.indexOf('%') == -1) {
		        if (this.maxCompsWidth[count] == null) this.maxCompsWidth[count] = comp.basewidth;
		        else if (this.maxCompsWidth[count] < comp.basewidth) this.maxCompsWidth[count] = comp.basewidth;
		    } else if (this.options.renderType == 1 && comp.basewidth.indexOf('%') != -1 && this.maxCompsWidth[count] == null) 
		    	this.maxCompsWidth[count] = 0;

		    //输入框外提示
		    if (comp.inputAssistant != null && comp.inputAssistant != "") {
		        var assCell = $("<td></td>").appendTo(row).attr("valign","top").css({
		        	'textAlign' : "right",
		        	'padding-top' : "3px",
		        	'paddingRight' : '5px'
		        }).html(comp.inputAssistant);
		    }
		    //描述
		    if (comp.description != null && comp.description != "") {
		    	 var descCell = $("<td></td>").appendTo(row).attr("valign","top").css({
		        	'textAlign' : "right",
		        	'padding-top' : "3px",
		        	'paddingRight' : '5px',
		        	'color' : 'gray'
		        }).html(comp.description);
		    }
		},
		/**
		 * 创建固定布局的Tabel
		 * @private
		 */
		createTable : function() {
		    return $("<table></table>");
		},
		/**
		 * 创建固定布局的TR
		 * @private
		 */
		createRow : function() {
		    if (this.table == null) return;
		    var row = $("<tr></tr>").appendTo(this.table);
		    row.attr("height", this.options.rowHeight + "px");
		    return row;
		},
		/**
		 * 创建空TD
		 * @private
		 */
		createNullCell : function(row) {
		    if (row == null) return;
		    var cell = $("<td></td>").appendTo(row);
		    return cell;
		},
		/**
		 * 创建TD
		 * @private
		 */
		createCell : function(row, rowSpan, colSpan) {
		    if (row == null) return;
		    var cell = $("<td></td>").appendTo(row).attr({
		    	'valign' : 'top',
		    	'rowSpan' : rowSpan,
		    	'colSpan' : colSpan
		    });

		    // rSpan解决ie8的bug用
		    var rSpan = row.attr("rSpan");
		    if (rSpan == null || rowSpan > rSpan) row.attr("rSpan", rowSpan);
		    return cell;
		},
		/**XX
		 * 增加td内容
		 */
		addComp : function(cell, comp) {
		    //标题
			var jq_labelDiv = $("<DIV value='"+comp.labelName+"' style='text-align:right; float:left; padding-top:2px; padding-right:5px;width:44px'></DIV>");
		    cell.append(jq_labelDiv);
		    var objHtml = comp.element;
		    cell.append(objHtml);
		    comp.parentOwner = cell;
		},
		/**
		 * @private
		 */
		afterRepaint : function() {
		    this.element.css('height','100%');
		},
		/**
		 * 获取高度
		 * @private
		 */

		getHeight : function() {
		    if (this.table == null) return 0;
		    var height = (this.options.rowHeight + 3) * this.table.find("tr").length;
		    return height;
		},
		/**
		 * 添加要排版的组件
		 * @private
		 */
		add : function(comp) {
			this._super(comp);
		    //PanelComp.prototype.add.call(this, comp);
		    this.comps.push(comp.owner);
		},
		/**
		 * 设置是否强制重新paint
		 * @private
		 */
		setForceRepaint : function(isForceRepaint) {
		    this.forceRepaint = $.argumentutils.getBoolean(isForceRepaint, false);
		},
		/**
		 * 得到传入的所有panel组件
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
		        for (var i = 0,
		        count = this.comps.length; i < count; i++) {
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
		        for (var i = 0,
		        count = this.comps.length; i < count; i++) {
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
		 * 处理label宽度
		 * @param {} label
		 */
		dealLabelWidth : function(labelDiv, label, lableExWidth, count, labels) {
		    var currentWidth = $.measures.getTextWidth(label, null);
		    currentWidth += parseInt(lableExWidth);
		    if (labels[count] == null) {
		        labels[count] = [];
		    }
		    if (this.maxLabelWidth[count] == null) this.maxLabelWidth[count] = currentWidth;
		    if (this.maxLabelWidth[count] < currentWidth) {
		        for (var i = 0; i < labels[count].length; i++) {
		            labels[count][i].css('width', currentWidth + "px");
		        }
		        this.maxLabelWidth[count] = currentWidth;
		    }
		    labelDiv.css('width',this.maxLabelWidth[count] + "px");
		    labels[count].push(labelDiv);
		},
		/**
		 * 固定布局居中显示的时候，对设置有百分比的控件，重算高度
		 */
		reSetBounds : function(comps, lables) {
		    for (var i = 0; i < comps.length; i++) {
		        if (comps[i].basewidth.indexOf('%') == -1) continue;
		        var colIndex = comps[i].colIndex;
		        var colSpan = comps[i].colSpan;
		        var compWidth = (this.maxCompsWidth[colIndex] == null || this.maxCompsWidth[colIndex] == 0) ? this.basewidth: this.maxCompsWidth[colIndex];
		        for (var j = 1; j < colSpan; j++) {
		            var nextCompWidth = this.maxCompsWidth[colIndex + j] == null ? 0 : this.maxCompsWidth[colIndex + j];
		            var nextLabelWidth = this.maxLabelWidth[colIndex + j] == null ? 0 : this.maxLabelWidth[colIndex + j];
		            var colWidth = parseInt(nextCompWidth) + parseInt(nextLabelWidth);
		            if (colWidth > 0) colWidth = parseInt(colWidth) + 6;
		            compWidth = parseInt(compWidth) + parseInt(colWidth) + 8; //
		        }
//		        if (comps[i].componentType == "TEXTAREA" && $.browsersupport.IS_IE8) comps[i].setBounds(0, 0, compWidth, null);
//		        else 
		           comps[i].setBounds(0, 0, compWidth, comps[i].height);
		    }
		}
	});
})(jQuery);
