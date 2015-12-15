DefaultRender.HEADERROW_HEIGHT = 29;

DefaultRender.plusImgSrc = window.themePath + "/comps/grid/images/folder_off.png";
DefaultRender.minusImgSrc = window.themePath + "/comps/grid/images/folder_on.png";

function DefaultRender() {};

DefaultRender.render = function(rowIndex, colIndex, value, header, cell, parentRowIndex) {
    if (typeof(value) == "string" && value.indexOf("&") != -1) {
        value = value.replace(/[&]/g, "&amp;");
    }
    var grid = this;
    cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    var cellHeight = 0;
    if ($.argumentutils.isPercent(cell.css('height'))) 
    	cellHeight = cell.outerHeight();
    else 
    	cellHeight = parseInt(cell.css('height'));
    if (cellHeight > DefaultRender.HEADERROW_HEIGHT) {
    	cell.css({
	    	'white-space' : 'normal',
	    	'word-wrap' : 'break-word',
	    	'line-height' : '',
	    	'overflow' : 'auto'
	    });
    }
    if (value == null) 
    	value = "";
    cell.data('tip',value).empty();
    if ((parentRowIndex != null || cell.data('level') != null) && colIndex == 0) {
        if (cell.data('level') != null) {
            for (var i = 0; i < cell.data('level'); i++) {
                $('<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>').appendTo(cell);
            }
        } else {
            cell.html('<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>');
        }
    } else {
        cell.empty();
    }

    if (grid.model.treeLevel != null && colIndex == 0) {
        var gridRow = grid.model.getRow(rowIndex);
        var loadImg = $("<img>").css('float','left');
        if (cell.data('hasChildren') && cell.data('hasChildren') == true)
        	loadImg.attr('src',DefaultRender.plusImgSrc);
        else 
        	loadImg.attr('src',DefaultRender.minusImgSrc);
        loadImg.data('plus',true);
        loadImg.data('initialized',false);
        if (gridRow.loadImg) {
            loadImg.data('plus',gridRow.loadImg.data('plus'));
            loadImg.data('initialized',gridRow.loadImg.data('initialized'));
        }
        loadImg.css({
        	'margin' : '6px',
        	'margin-top' : '9px',
        	'width' : '8px',
        	'height' : '8px'
        });
        loadImg.data('owner',grid).data('row',gridRow.pageData).data('cell',cell);
        if (cell.data('level') == null) 
        	cell.data('level',0);
        loadImg.on('click',expandGridChild);
        cell.append(loadImg);
        gridRow.loadImg = loadImg;
    }
    var userDiv = null;
    if (typeof fillCellContent != "undefined") {
        userDiv = fillCellContent.call(this, grid, rowIndex, colIndex, value, header, parentRowIndex);
    }
    if (userDiv == null) {
        var textSpan = $("<span>");
        if (typeof(value) == "string") 
        	value = value.replaceAll("\n", "<br/>");
        textSpan.html(value);
        cell.append(textSpan);
    } else 
    	cell.append(userDiv);
};

//定义LanguageComboRender
function LanguageComboRender() {};

/**
 * 多语类型的render,显示当前登录语种的内容，如果为空显示主语种内容
 * 参照DefaultRender实现，只是内容部分由改变，实现数据集中取出多语内容
 * 
 * @param {} rowIndex 行号
 * @param {} colIndex 列号
 * @param {} value 值
 * @param {} header 列头
 * @param {} cell 单元格
 */
LanguageComboRender.render = function(rowIndex, colIndex, value, header, cell, parentRowIndex) {
    var grid = this;
    var ds = grid.model.dataset;
    var grid = this;
    var cellStyle = cell.style;
    cellStyle.overflow = "hidden";
    cellStyle.cursor = "default";
    cellStyle.textOverflow = "ellipsis";
    if (cell.offsetHeight > DefaultRender.HEADERROW_HEIGHT) {
        cellStyle.whiteSpace = "normal";
        cellStyle.wordWrap = "break-word";
        cellStyle.lineHeight = "";
        cellStyle.overflow = "auto";
    }
    if (value == null) value = "";

    cell.innerHTML = "";
    if ((parentRowIndex != null || cell.level != null) && colIndex == 0) {
        if (cell.level != null) {
            for (var i = 0; i < cell.level; i++) {
                cell.innerHTML += '<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
            }
        } else {
            cell.innerHTML = '<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
        }
    } else {
        cell.innerHTML = "";
    }

    if (grid.model.treeLevel != null && colIndex == 0) {
        var gridRow = grid.model.getRow(rowIndex);
        var loadImg = $("<IMG>").get(0);
        //loadImg.marginTop = "5px";
        loadImg.style[$.CONST.ATTRFLOAT] = "left";
        if (cell.hasChildren && cell.hasChildren != null && cell.hasChildren == true) loadImg.src = DefaultRender.plusImgSrc;
        else loadImg.src = DefaultRender.minusImgSrc;
        loadImg.plus = true;
        loadImg.initialized = false;
        if (gridRow.loadImg) {
            loadImg.plus = gridRow.loadImg.plus;
            loadImg.initialized = gridRow.loadImg.initialized;
        }
        loadImg.style.margin = "6px";
        loadImg.style.marginTop = "9px";
        loadImg.style.width = "8px";
        loadImg.style.height = "8px";
        loadImg.owner = grid;
        loadImg.row = gridRow.pageData;
        loadImg.cell = cell;
        if (cell.level == null) cell.level = 0;
        loadImg.onclick = expandGridChild;
        cell.appendChild(loadImg);
        gridRow.loadImg = loadImg;
    }

    //--------------------------多语内容------------------------------------
    var currentLanguageCode = grid.currentLanguageCode;
    if (typeof(currentLanguageCode) == "undefined" || currentLanguageCode == null) {
        currentLanguageCode = "1";
    }
    var fieldName = header.keyName;
    //当前语种编号，即字段后续号。例name、name2、name3等
    var realShowContent = "";
    if (currentLanguageCode != "1") {
        var langFieldName = fieldName + currentLanguageCode;
        var gridRow = grid.model.getRow(rowIndex);
        realShowContent = gridRow.getCellValueByFieldName(langFieldName);
        if (realShowContent == null || typeof(realShowContent) == "undefined" || "" == realShowContent || "null" == realShowContent) {
            realShowContent = value;
        }
    } else {
        realShowContent = value;
    }
    //------------------------------------------------------------------------
    //创建节点和提示信息
    cell.tip = realShowContent;
    var userDiv = null;
    if (typeof fillCellContent != "undefined") {
        userDiv = fillCellContent.call(this, grid, rowIndex, colIndex, realShowContent, header, parentRowIndex);
    }
    if (userDiv == null) {
        //		cell.appendChild(document.createTextNode(value));
        var textSpan = $("<SPAN>").get(0);
        if (typeof(value) == "string") value = value.replaceAll("\n", "<br/>");
        textSpan.innerHTML = realShowContent;
        cell.appendChild(textSpan);
    } else cell.appendChild(userDiv);
};

function TextAreaRender() {};

function expandGridChild(e) {
    var grid = $(this).data('owner');
    if ($(this).data('plus')) {
        if (!$(this).data('initialized')) {
            var ds = grid.model.dataset;
            var rowIndex = grid.getCellRowIndex($(this).parent());
            var index = ds.nameToIndex(grid.model.treeLevel.recursiveKeyField);
            var row = $(this).data('row');
            var key = row.getCellValue(index);
            grid.loadChild(key, rowIndex + 1, $(this).data('cell').data('level'));
            $(this).data('initialized',true);
        } else {
            var rowIndex = grid.getCellRowIndex($(this).parent());
            grid.showChildRows(rowIndex);
        }
        $(this).data('plus',false).attr('src',DefaultRender.minusImgSrc);
    } else {
        var rowIndex = grid.getCellRowIndex($(this).parent());
        grid.hideChildRows(rowIndex);
        $(this).data('plus',true).attr('src',DefaultRender.plusImgSrc);
    }
    if (e) {
    	e.preventDefault();
    	e.stopPropagation();
    }
};

function ImageRender() {};

ImageRender.render = function(rowIndex, colIndex, value, header, cell) {};

function BooleanRender() {};

BooleanRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.css({
		'overflow' : 'hidden',
		'text-overflow' : 'ellipsis',
		'cursor' : 'default',
		'padding-top' : '0px'
	});
    value = (value == null) ? "": value.toString();
    var grid = header.owner;
    var checkBox = $("<input type='checkbox'>").css('margin-top',"5px");
    checkBox.data('rowIndex',rowIndex);
    checkBox.data('colIndex',colIndex);
    cell.empty();
    cell.append(checkBox);
    cell.data('checkBox',checkBox);
    if (header.valuePair != null) {
        if (value == header.valuePair[0]) {
            cell.data('trueValue',value);
            checkBox.prop('checked',true);
        } else if (value == header.valuePair[1]) {
            cell.data('trueValue',value);
            checkBox.prop('checked',false);
        }
        if (header.columEditable == false || grid.editable == false) 
        	checkBox.prop('disabled',true);

        checkBox.on({
        	'mousedown' : function(e) {
        		rowIndex = cell.data('rowIndex'); //行索引从单元格中获取
	            if (this.checked == true) 
	            	grid.model.setValueAt(rowIndex, colIndex, header.valuePair[1]);
	            else 
	            	grid.model.setValueAt(rowIndex, colIndex, header.valuePair[0]);
        	},
        	'click' : function(e) {
        		e.preventDefault();
    			e.stopPropagation();
        	}
        });
    }
};

function BooleanImageRender() {};

BooleanImageRender.render = function(rowIndex, colIndex, value, header, cell) {
    cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    value = (value == null) ? "": value.toString();
    var grid = header.owner;

    if (header.valuePair != null) {
        if (value == header.valuePair[0]) 
        	cell.css('background',"#ffffff url('" + window.globalPath + "/html/image/checked.gif" + "') no-repeat center center");
        else if (value == header.valuePair[1]) 
        	cell.css('background',"#ffffff url('" + window.globalPath + "/html/image/unchecked.gif" + "') no-repeat center center");
    }
};

function IntegerRender() {};

IntegerRender.render = function(rowIndex, colIndex, value, header, cell) {
    cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    }).empty();
    var cellValue = this.parseData(header, value);
    var result = $.pageutils.getMasker("INTEGERTEXT").format(cellValue);
    var cellDisplayVal = $.maskerutil.toColorfulString(result);
    cell.data('tip',cellDisplayVal).html(cellDisplayVal);
};

function DecimalRender() {};

//DecimalRender.render = IntegerRender.render;
DecimalRender.render = function(rowIndex, colIndex, value, header, cell) {
     cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    }).empty();
    var cellValue = "";
    cellValue = this.parseData(header, value);
    var result = $.pageutils.getMasker("FLOATTEXT").format(cellValue);
    var cellDisplayVal = $.maskerutil.toColorfulString(result);
    cell.data('tip',cellValue);
    //此语句在调用render之前是调用未生效，在这里调用
    cell.children(":first").remove();
    cell.html(cellDisplayVal);
};

function ComboRender() {};

ComboRender.render = function(rowIndex, colIndex, value, header, cell) {
    cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    var parsedValue = "";
    if (header.comboData != null && header.comboData.getValueArray() != null) {
        var varr = header.comboData.getValueArray();
        var vs = (value == null) ? [] : value.split(",");
        var indices = [];
        for (var i = 0; i < vs.length; i++) {
            var index = varr.indexOf(vs[i].trim());
            indices.push(index);
        }
        var parsedValueArr = [];
        if (indices.length > 0) {
            var narr = header.comboData.getNameArray();
            for (var i = 0; i < indices.length; i++) {
                if (indices[i] != null && indices[i] != -1) 
                	parsedValueArr.push(narr[indices[i]]);
                else 
                	parsedValueArr.push("");
            }
        }
        parsedValue = parsedValueArr.join(",");
    }

    if (header.showImgOnly == true) {
        cell.css({
        	'background' : "url(" + parsedValue + ") no-repeat center",
        	'background-color' : ''
        });
    } else {
        cell.data('tip',parsedValue);
        cell.html(parsedValue);
    }
};

ComboRender.changeRender = function(grid, colIndex, comboData) {
    var header = grid.basicHeaders[colIndex];
    header.setHeaderComboBoxComboData(comboData);

    var comp = header.comboComp;
    if (comp) {
        comp.clearOptions();
        if (comboData != null) {
            var keyValues = comboData.getValueArray();
            var captionValues = comboData.getNameArray();
            if (header.showImgOnly) {
                for (var j = 0, count = keyValues.length; j < count; j++) 
                	comp.createOption(captionValues[j], keyValues[j], null, false, -1, true);
            } else {
                for (var j = 0, count = keyValues.length; j < count; j++) 
                	comp.createOption(captionValues[j], keyValues[j], null, false, -1, false);
            }
        }
    }
};

function EmailRender() {};

EmailRender.render = function(rowIndex, colIndex, value, header, cell) {
    if (value != null && value.trim() != "") {
    	cell.css({
	    	'overflow' : 'hidden',
	    	'cursor' : 'default',
	    	'text-overflow' : 'ellipsis'
	    });
        var mailContent = "<a href='mailto:" + value + "?subject='' enctype='text/plain' target='_blank'>" + value + "</a>";
        cell.data('tip',value);
        cell.html(mailContent);
    }
};

function UrlRender() {};

UrlRender.render = function(rowIndex, colIndex, value, header, cell) {
    if (value != null && value.trim() != "") {
    	cell.css({
	    	'overflow' : 'hidden',
	    	'cursor' : 'default',
	    	'text-overflow' : 'ellipsis'
	    });
        var urlContent = "<a href='#' onclick=\"window.open('http://" + value + "');return false;\">" + value + "</a>";
        cell.data('tip',value);
        cell.html(urlContent);
    }
};

function CheckBoxRender() {};

CheckBoxRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    if (value == 1) 
    	cell.css('background',"#ffffff url('" + window.globalPath + "/html/image/checked.gif" + "') no-repeat center center")
    else if (value == 0) 
    	cell.css('background',"#ffffff url('" + window.globalPath + "/html/image/unchecked.gif" + "') no-repeat center center")
};

function SexRender() {};

SexRender.render = function(rowIndex, colIndex, value, header, cell) {
    cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    if (value == 1) 
    	cell.css('background',"#ffffff url('" + window.globalPath + "/html/image/woman.gif" + "') no-repeat center center")
    else if (value == 0) 
    	cell.css('background',"#ffffff url('" + window.globalPath + "/html/image/man.gif" + "') no-repeat center center")
};

function DateRender() {};

DateRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    if (value != null && value.length > 10) 
    	value = value.substring(0, 10);
    else if (value == null) 
    	value = "";
    else if (value == 0) 
    	value = "";
    if (value == null) 
    	value = "";
    var showValue = value;
    var maskerType = "DATETEXT";
    var masker = $.pageutils.getMasker(maskerType);
    if (masker != null) showValue = $.maskerutil.toColorfulString(masker.format(value));
    cell.data('tip',showValue);
    cell.html(showValue);
};

function DateTimeRender() {};

DateTimeRender.render = function(rowIndex, colIndex, value, header, cell) {
    cell.css({
    	'overflow' : 'hidden',
    	'cursor' : 'default',
    	'text-overflow' : 'ellipsis'
    });
    if (value == null) 
    	value = "";
    var showValue = value;
    var maskerType = "DateTimeText";
    var masker = $.pageutils.getMasker(maskerType);
    if (masker != null) 
    	showValue = $.maskerutil.toColorfulString(masker.format(value));
    cell.data('tip',showValue);
    cell.html(showValue);
};

function ascendRule_int(a, b) {
    if (parseInt(a) < parseInt(b)) 
    	return - 1;
    else if (parseInt(a) > parseInt(b)) 
    	return 1;
    else 
    	return 0;
};

/**
 * 默认行渲染
 * 
 */
function DefaultRowRender() {};

/**
 * 默认行渲染
 * 
 * @param {} row 数据行
 * @param {} rowCells 当前渲染行中的所有cell，通过cell.colIndex 可以取到该cell对应ds中的第几列
 * 
 */
DefaultRowRender.render = function(row, rowCells) {
};

/***
 * 创建单元格控件
 */
function CellEditor(parent, row, colIndex) {
};