/**
 * 
 * 只读布局管理器
 * 
 */

ReadGridLayout.prototype.className = "ReadGridLayout";
ReadGridLayout.prototype = new PanelComp;

// 额外的空白高度
ReadGridLayout.SPACE_HEIGHT = 10;

/**
 * 表单固定布局构造函数
 * @class 表单固定布局
 */
function ReadGridLayout(parent, name, left, top, width, height, position, scroll, compDefaultWidth, marginTop, rowHeight, columnCount, renderType) {
    this.base = PanelComp;
    this.base(parent, name, left, top, width, height, position, scroll);
    this.parentOwner = parent;
    this.defaultwidth = $.argumentutils.getString(compDefaultWidth, "120");
    this.basewidth = this.defaultwidth;
    this.gridWidth = 0; // 每一列的应有宽度
    this.colmNum = 0; // 应有的列数
    this.lastX = 0; // 上一组件的起始X坐标,以此来计算下一组件的开始坐标
    this.wordsdefault = 5; // 缺省的label中的字数
    // 保存每次传入的组件
    this.comps = new Array();
    // this.labels = new Array();
    this.marginTop = $.argumentutils.getInteger(marginTop, 6);
    this.currentRow = null;
    this.rowHeight = $.argumentutils.getInteger(rowHeight, 22);
    this.columnCount = $.argumentutils.getInteger(columnCount, 2);
    //记录每列最大宽度（只计算固定宽度的） 固定居中布局使用 
    this.maxCellsWidth = new Array(this.columnCount);
    //记录每列中控件最大宽度（只计算固定宽度的） 固定居中布局使用
    this.maxCompsWidth = new Array(this.columnCount);
    //记录每列中控件最大Label  
    this.maxLabelWidth = new Array(this.columnCount);

    // 强制刷新,如果强制刷新,每次paint前要设置该值为true
    this.forceRepaint = false;
    if (ReadGridLayout.syncArray == null) {
        ReadGridLayout.syncArray = new Array;
    }
    ReadGridLayout.syncArray.push(this);
    if (ReadGridLayout.wordArray == null) ReadGridLayout.wordArray = new Array;

    ReadGridLayout.COMPHEIGHT = $.measures.getCssHeight("text_form_div_COMPHEIGHT");

    this.labelPosition = "left";
    this.renderType = renderType == null ? DefaultFormRender: renderType;
};

/**
 * 创建组件div
 * @private
 */
ReadGridLayout.prototype.create = function() {
    var oThis = this;
    var jq_Div_gen = $("<div style='left:"+this.left+"px; top:"+this.top+"px; width:100%; height:100%; position:"+this.position+";overflow-y:auto; overflow-x:hidden;'></div>");
    this.Div_gen = jq_Div_gen.get(0);     
    //创建table
    this.table = this.createTable();
    this.Div_gen.appendChild(this.table);

    this.rt = null;
    if (this.parentOwner) this.placeIn(this.parentOwner);
};

/**
 * @private
 */
function resizeLayout(compId) {
    alert("resizeLayout");
};

/**
 * 调用paint函数进行绘制
 * @private
 */
ReadGridLayout.prototype.paint = function() {
    var compsCount = this.comps.length;
    if (compsCount == 0) return;

    var offsetWidth = this.Div_gen.offsetWidth;
    // 处于隐藏状态
    if (offsetWidth == 0) return;

    // 要隐藏的控件,要显示的控件
    var visibleComps = [],
    hiddenComps = [],
    allComps = this.comps;
    for (var i = 0; i < compsCount; i++) {
        if (allComps[i].visible == true) {
            visibleComps.push(allComps[i]);
            var label = allComps[i].attachedLabel;
            if (label && label != null) label.style.display = "block";
            if (typeof(allComps[i].show) == "function") {
                allComps[i].show();
            }
        } else {
            hiddenComps.push(allComps[i]);
            var label = allComps[i].attachedLabel;
            if (label && label != null) label.style.display = "none";
            if (typeof(allComps[i].hide) == "function") {
                allComps[i].hide();
            }
        }
    }

    // 将comps设置为显示控件,下面只渲染显示控件
    this.comps = visibleComps;
    //重绘table
    this.Div_gen.removeChild(this.table);
    this.table = this.createTable();
    //	this.table.setAttribute("width","100%");
    this.table.style.margin = "0px auto";
    //	this.table.style.tableLayout = "fixed";
    this.Div_gen.appendChild(this.table);

    //记录当前行索引
    var nowRow = 0;
    //记录当前行的控件个数
    var count = 0;
    //记录当前第几行
    var rowIndex = 0;
    //记录所有行的占用span数
    //	var rowInfoArray = new Array();
    var labels = new Array(this.columnCount);
    //当前行
    var currentRow = null;
    //记录布局中被占用的cell
    var gridGraph = new Array();
    for (var i = 0,
    compCount = this.comps.length; i < compCount; i++) {
        var colSpan = this.comps[i].colSpan;
        var rowSpan = this.comps[i].rowSpan;

        // 新行开始，加上行标
        if (count == 0) {
            currentRow = this.createRow();
        }
        // 加上跨行元素宽度
        if (gridGraph[nowRow] == null) gridGraph[nowRow] = new Array();
        while (gridGraph[nowRow][count] != null) {
            count++;
        }
        // 检查越界
        if (count > this.columnCount) count = this.columnCount;
        // 检查是否一个元素就超出了界限
        if (colSpan > this.columnCount) colSpan = this.columnCount;
        // 如果加上当前控件越界,则补齐当前行td数，转到下一行
        if (count + parseInt(colSpan) > this.columnCount) {
            for (; count < this.columnCount; count++) this.createNullCell(currentRow);
            i--;
            count = 0;
            nowRow++;
            continue;
        }
        //如果是强制下一行，并且当前行并不是新起的行,则根据是否已经是新行进行判断
        else if (count != 0 && this.comps[i].nextLine) {
            for (; count < this.columnCount; count++) this.createNullCell(currentRow);
            i--;
            count = 0;
            nowRow++;
            continue;
        } else {
            for (var row = 0; row < rowSpan; row++) {
                if (gridGraph[row + nowRow] == null) gridGraph[row + nowRow] = new Array();
                for (var col = 0; col < colSpan; col++) {
                    gridGraph[row + nowRow][col + count] = 1;
                }
            }
            var labelCell = this.createCell(currentRow, rowSpan, '1');
            var valueCell = this.createCell(currentRow, rowSpan, colSpan);

            //创建控件元素
            this.renderOneElement(labelCell, valueCell, this.comps[i], count, labels, this.table, currentRow);
            count += parseInt(colSpan, 10);
        }
        if (count == this.columnCount) {
            count = 0;
            nowRow++;
        }
    }
    this.comps = allComps;
    this.afterRepaint();
};

ReadGridLayout.prototype.renderOneElement = function(labelCell, valueCell, comp, count, labels, table, row) {
    comp.labelCell = labelCell;
    comp.valueCell = valueCell;
    comp.row = row;
    comp.table = table;
    comp.colIndex = count;

    this.renderType.call(this, comp, labelCell, valueCell, table, row);
};

/**
 * 创建固定布局的Tabel
 * @private
 */
ReadGridLayout.prototype.createTable = function() {
    var table = $("<table>").get(0);
    return table;
};

/**
 * 创建固定布局的TR
 * @private
 */
ReadGridLayout.prototype.createRow = function() {
    if (this.table == null) return;
    var row = this.table.insertRow( - 1);
    row.setAttribute("height", this.rowHeight + "px");
    return row;
};

/**
 * 创建空TD
 * @private
 */
ReadGridLayout.prototype.createNullCell = function(row) {
    if (row == null) return;
    var cell = row.insertCell( - 1);
    return cell;
};

/**
 * 创建TD
 * @private
 */
ReadGridLayout.prototype.createCell = function(row, rowSpan, colSpan) {
    if (row == null) return;
    var cell = row.insertCell( - 1);
    cell.setAttribute("valign", "top");
    cell.setAttribute("rowSpan", rowSpan);
    cell.setAttribute("colSpan", colSpan);

    // rSpan解决ie8的bug用
    var rSpan = row.getAttribute("rSpan");
    if (rSpan == null || rowSpan > rSpan) row.setAttribute("rSpan", rowSpan);
    //	cell.style.width = "0px";
    return cell;
};

/**XX
 * 增加td内容
 */
ReadGridLayout.prototype.addComp = function(cell, comp) {
    //标题
	var jq_labelDiv = $("<div style='text-align:right; float:left; padding-top:2px; padding-right:5px; width:44px;'></div>");
    var labelDiv = jq_labelDiv.get(0);   
    labelDiv.value = comp.labelName;
    cell.appendChild(labelDiv);
    //comp
    var objHtml = comp.getObjHtml();
    cell.appendChild(objHtml);
    comp.parentOwner = cell;
};

/**
 * @private
 */
ReadGridLayout.prototype.afterRepaint = function() {
    //	this.Div_gen.style.height = this.getHeight() + ReadGridLayout.SPACE_HEIGHT + "px"; 	
    this.Div_gen.style.height = "100%";
};

/**
 * 获取高度
 * @private
 */

ReadGridLayout.prototype.getHeight = function() {
    if (this.table == null) return 0;
    var height = (this.rowHeight + 3) * this.table.rows.length;
    return height;
};

/**
 * 添加要排版的组件
 * @private
 */
ReadGridLayout.prototype.add = function(comp) {
    //	PanelComp.prototype.add.call(this, comp);
    this.comps.push(comp);
};

/**
 * 设置是否强制重新paint
 * @private
 */
ReadGridLayout.prototype.setForceRepaint = function(isForceRepaint) {
    this.forceRepaint = $.argumentutils.getBoolean(isForceRepaint, false);
};

/**
 * 得到传入的所有panel组件
 * @private
 */
ReadGridLayout.prototype.getComps = function() {
    return this.comps;
};

/**
 * 根据id获取子项对象
 */
ReadGridLayout.prototype.getCompById = function(id) {
    if (this.comps != null) {
        for (var i = 0,
        count = this.comps.length; i < count; i++) {
            if (this.comps[i].id == id) return this.comps[i];
        }
    }
};

/**
 * 根据id删除子项对象
 */
ReadGridLayout.prototype.removeCompById = function(id) {
    var comp = null;
    if (this.comps != null) {
        var newComps = new Array();
        for (var i = 0,
        count = this.comps.length; i < count; i++) {
            if (this.comps[i].id == id) {
                comp = this.comps[i];
            } else {
                newComps.push(this.comps[i]);
            }
        }
        this.comps = newComps;
    }
    return comp;
};

/**
 * 固定布局居中显示的时候，对设置有百分比的控件，重算高度
 */
ReadGridLayout.prototype.reSetBounds = function(comps, lables) {
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
//        if (comps[i].componentType == "TEXTAREA" && $.browsersupport.IS_IE8) comps[i].setBounds(0, 0, compWidth, null);
 //       else 
              comps[i].setBounds(0, 0, compWidth, comps[i].height);
    }

};

ReadElement.prototype = new ListenerUtil;

function ReadElement(eleId, layout) {
    this.id = eleId;
    this.layout = layout;
    this.layout.add(this);
    ListenerUtil.call(this, true);
};

ReadElement.prototype.setValue = function(value) {
    this.value = value;
    if (this.labelCell && this.valueCell) {
        this.labelCell.innerHTML = "";
        this.valueCell.innerHTML = "";
        this.layout.renderType.call(this, this, this.labelCell, this.valueCell, this.table, this.row);
    }
};

ReadElement.prototype.getValue = function() {
    return this.value;
};

/**
 * 兼容其它布局
 */
ReadElement.prototype.setActive = function() {};

ReadElement.prototype.getContext = function() {
    var context = new Object;
    context.visible = this.visible;
    return context;
};
ReadElement.prototype.setContext = function(context) {
    if (context.visible != null) this.visible = context.visible;
};

function DefaultFormRender(ele, labelCell, valueCell, table, row) {
    if (table && table.getAttribute("rendered") == null) {
        table.setAttribute("border", 1);
        table.setAttribute("cellpadding", 0);
        table.setAttribute("cellspacing", 0);
        table.setAttribute("cellspacing", 0);
        table.setAttribute("bordercolor", "#D1DFE4");
        table.style.borderCollapse = "collapse";
        table.style.wordWrap = "break-word";
        table.style.tableLayout = "fixed";
        table.setAttribute("rendered", 1);
    }
    row.setAttribute("height", "29px");
    labelCell.innerHTML = ele.labelName;
 //   if ($.browsersupport.IS_IE7) labelCell.style.width = "83px";
//    else 
         labelCell.style.width = "153px"; //ele.layout.labelMinWidth + "px";
    labelCell.style.backgroundColor = "#F7FCFF";
    labelCell.style.textAlign = "right";
    labelCell.style.paddingLeft = "10px";
    labelCell.style.paddingRight = "10px";
    labelCell.style.verticalAlign = "middle";
    if (ele.value != null) {
        var showValue = ele.value;
        if ($.editortype.DATETEXT == ele.editorType) {
            var masker = $.pageutils.getMasker(DateTextComp.prototype.componentType);
            if (masker != null) {
                showValue = $.maskerutil.toColorfulString(masker.format(showValue));
            }
        } else if ($.editortype.DATETIMETEXT == ele.editorType) {
            var masker = $.pageutils.getMasker("DateTimeText");
            if (masker != null) {
                showValue = $.maskerutil.toColorfulString(masker.format(showValue));
            }
        }
        valueCell.innerHTML = showValue;
    }
 //   if ($.browsersupport.IS_IE7) valueCell.style.width = "110px";
//    else 
         valueCell.style.width = "211px"; //ele.layout.eleWidth + "px";
    valueCell.style.paddingLeft = "10px";
    valueCell.style.paddingRight = "10px";
    valueCell.style.verticalAlign = "middle";
};