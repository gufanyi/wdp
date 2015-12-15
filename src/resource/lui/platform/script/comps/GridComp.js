/**
 * @gridComp 表格控件
 * @auther liy lxl
 * @version lui 1.0
 */
(function($) {
	$.grid = {};
	$.extend($.grid,{
		// 表头的默认单行高度
		HEADERROW_HEIGHT : 24,
		HEADERROW_WIDTH : 120,
		// grid数据行的默认高度
		ROW_HEIGHT : 24,
		// 固定选择列的宽度值(isMultiSelWithBox为true)
		SELECTCOLUM_WIDTH : 30,
		// 单元格底边高度
		CELL_BOTTOM_BORDER_WIDTH : 1,
		// grid分页条的高度
		PAGEBAR_HEIGHT : 32,
		// 列的左边界宽
		COLUMN_LEFT_BORDER_WIDTH : 0,
		// 合计单元格的padding
		SUMCELL_PADDING : 10,
		// 行状态列宽
		ROWSTATE_COLUMN_WIDTH : 13,
		// 多选列宽
		MULTISEL_COLUMN_WIDTH : 30,
		// "合计" 所在Div所占列宽
		SUMROW_DIV_WIDTH : 34,
		COlUMWIDTH_DEFAULT : 70,
		SCROLLBAE_HEIGHT : 17,
		// cell单元格左padding
		CELL_LEFT_PADDING : 10,
		CELL_RIGHT_PADDING : 10,
		// 自动扩展表头的每一份的最小宽度
		EXPANDHEADER_MINWIDTH : 100,
		tipDiv : null,
		initEditCompsForGrid : function(gridId) {
			var oThis = gridId;
			if (oThis == null)
				return;
			if (oThis.compsInited)
				return;
			// 标示控件已经被初始化过
			oThis.compsInited = true;
			// 标示各个控件是否已经初始化
			var stringInited = false,textAreaInited = false,integerInited = false,decimalInited = false,
				dateInited = false,multiDateInited = false,dateTimeInited = false,multiDateTimeInited = false,
				boolInited = false;
			var basicHeaders = oThis.basicHeaders;
			if (oThis.compsMap == null)
				oThis.compsMap = $.hashmap.getObj();
			for (var i = 0; i < basicHeaders.length; i++) {
				
				//判断editorType是否有多个（逗号隔开）
				var editorTypeArr = [];
				if(basicHeaders[i].editorType.indexOf(",") != -1){
					editorTypeArr =  basicHeaders[i].editorType.split(",");
				} else {
					editorTypeArr.push(basicHeaders[i].editorType);
				}
				for(var j = 0; j < editorTypeArr.length; j++){
					var comp = null;
					// textArea控件
					if (editorTypeArr[j] == $.editortype.TEXTAREA && !textAreaInited) {
						comp = $('<div id="textArea">').appendTo('body').textarea({
									position : 'absolute'
								}).textarea('instance');
						oThis.compsMap.put($.editortype.TEXTAREA, comp);
						textAreaInited = true;
					} else if (editorTypeArr[j] == $.editortype.INTEGERTEXT && !integerInited) {
						// 整型输入框
						comp = $('<div id="integerText">').appendTo('body').integertext({
									position : 'absolute'
								}).integertext('instance');
						oThis.compsMap.put($.editortype.INTEGERTEXT, comp);
						integerInited = true;
					} else if (editorTypeArr[j] == $.editortype.DECIMALTEXT && !decimalInited) {
						// 浮点数输入框
						comp = $('<div id="floatText">').appendTo('body').floattext({
									position : 'absolute'
								}).floattext('instance');
						oThis.compsMap.put($.editortype.DECIMALTEXT, comp);
						decimalInited = true;
					} else if (editorTypeArr[j] == $.editortype.CHECKBOX && !boolInited) {
						comp = $('<div id="checkbox">').appendTo('body').checkbox({
									position : 'absolute'
								}).checkbox('instance');
						oThis.compsMap.put($.editortype.CHECKBOX, comp);
						boolInited = true;
					} else if (editorTypeArr[j] == $.editortype.DATETEXT && !dateInited) {
						// 日期类型输入框
						comp = $('<div id="dateText">').appendTo('body').datetext({
									position : 'absolute'
								}).datetext('instance');
						oThis.compsMap.put($.editortype.DATETEXT, comp);
						dateInited = true;
					} else if (editorTypeArr[j] == $.editortype.MULTIDATETEXT && !multiDateInited) {
						// 日期类型输入框
						comp = $('<div id="dateText">').appendTo('body').datetext({
									position : 'absolute',
									multiple : true
								}).datetext('instance');
						oThis.compsMap.put($.editortype.MULTIDATETEXT, comp);
						multiDateInited = true;
					} else if (editorTypeArr[j] == $.editortype.DATETIMETEXT && !dateTimeInited) {
						// 日期时间类型输入框
						comp = $('<div id="dateText">').appendTo('body').datetext({
									position : 'absolute'
								}).datetext('instance');
						comp.setShowTimeBar(true);
						oThis.compsMap.put($.editortype.DATETIMETEXT, comp);
						dateTimeInited = true;
					} else if (editorTypeArr[j] == $.editortype.MULTIDATETIMETEXT && !multiDateTimeInited) {
						// 日期时间类型输入框
						comp = $('<div id="dateText">').appendTo('body').datetext({
									position : 'absolute',
									multiple : true
								}).datetext('instance');
						comp.setShowTimeBar(true);
						oThis.compsMap.put($.editortype.MULTIDATETIMETEXT, comp);
						multiDateTimeInited = true;
					} else if (editorTypeArr[j] == $.editortype.COMBOBOX) {
						// 下拉框类型(暂时设置下拉控件为仅选择的)
						comp = $('<div id="COMBOBOX' + i + '">').appendTo('body').combo({
									position : 'absolute'
								}).combo('instance');
						basicHeaders[i].comboComp = comp;
						// 设置输入控件的高度
						comp.element.css('height', $.grid.ROW_HEIGHT + "px");
						// 仅显示图片的combo
						if (basicHeaders[i].comboData != null) {
							comp.setComboData(basicHeaders[i].comboData);
						}
						if(basicHeaders[i].editorType.indexOf(",") != -1){
					       var key = $.editortype.COMBOBOX + i+j;
						   oThis.compsMap.put(key, comp);
				        } else {
					       var key = $.editortype.COMBOBOX + i;
						   oThis.compsMap.put(key, comp);
				        }
						
					} else if (editorTypeArr[j] == $.editortype.MULTICOMBOBOX) {
						// 下拉框类型(暂时设置下拉控件为仅选择的)
						comp = $('<div id="MULTICOMBOBOX' + i + '">').appendTo('body').combo({
									position : 'absolute',
									multiple : true
								}).combo('instance');
						basicHeaders[i].comboComp = comp;
						// 设置输入控件的高度
						comp.element.css('height', $.grid.ROW_HEIGHT + "px");
						// 仅显示图片的combo
						if (basicHeaders[i].comboData != null) {
							comp.setComboData(basicHeaders[i].comboData);
						}
				        var key = $.editortype.MULTICOMBOBOX + i;
					    oThis.compsMap.put(key, comp);
						
					}  else if (editorTypeArr[j] == $.editortype.REFERENCE) {
						comp = $('<div id="Reference' + i + '">').appendTo('body').reference({
									position : 'absolute',
									nodeInfo : basicHeaders[i].nodeInfo
								}).reference('instance');
						comp.setBindInfo(oThis.model.dataset.id, basicHeaders[i].keyName);
						comp.refGridId = oThis.id;
						comp.refGridHeaderId = basicHeaders[i].keyName;
						comp.viewpart = oThis.viewpart;
						
						if(basicHeaders[i].editorType.indexOf(",") != -1){
					       var key = $.editortype.REFERENCE + i+j;
						   oThis.compsMap.put(key, comp);
				        } else {
					       var key = $.editortype.REFERENCE + i;
						   oThis.compsMap.put(key, comp);
				        }
					} else if (!stringInited) {
						// 字符串输入框
						comp = $('<div id="stringtext">').appendTo('body').stringtext({
									position : 'absolute'
								}).stringtext('instance');
						oThis.compsMap.put($.editortype.STRINGTEXT, comp);
						stringInited = true;
					}
					$.grid.addCompListener(oThis, comp, i);
				}
			}
		},
		/**
		 * 对控件增加事件
		 * @param {} grid
		 * @param {} comp
		 * @param {} colIndex
		 */
		addCompListener : function(grid, comp, colIndex) {
			var oThis = grid;
			if (comp != null) {
				comp.ingrid = true;
				comp.colIndex = colIndex;
				comp.element.on(comp.widgetName + 'onkeydown', function(e) {
					// 当前激活的cell
					// end键 home键分别编辑当前行最后一个、第一个cell
					var keyCode = e.keyCode;
					if ((keyCode == 35 || keyCode == 36) && !e.shiftKey) {
						var activeCell = oThis.selectedCell;
						var nextActiveCell;
						if (oThis.editable) {
							if (keyCode == 35) {
								var cell = oThis.getEditableCellByDirection(activeCell, 1);
								while (cell != null && activeCell.data('rowIndex') == cell.data('rowIndex')) {
									nextActiveCell = cell;
									cell = oThis.getEditableCellByDirection(cell, 1);
								}
							} else if (keyCode == 36) {
								var cell = oThis.getEditableCellByDirection(activeCell, -1);
								while (cell != null && activeCell.data('rowIndex') == cell.data('rowIndex')) {
									nextActiveCell = cell;
									cell = oThis.getEditableCellByDirection(cell, -1);
								}
							}
						}
						if (oThis.showComp && oThis.showComp.input)
							oThis.showComp.input.triggerHandler('blur');
						if (nextActiveCell != null) {
							oThis.hiddenComp();
							oThis.setCellSelected(nextActiveCell);
						}
					}
					// "tab"键和"shift+tab"键和回车键处理
					else if ((keyCode == 9 && e.shiftKey) || keyCode == 9 || keyCode == 13) {
						var activeCell = oThis.selectedCell;
						var nextActiveCell;
						if (oThis.editable) {
							nextActiveCell = oThis.getEditableCellByDirection(activeCell, 1);
							if (keyCode == 9 && e.shiftKey)
								nextActiveCell = oThis.getEditableCellByDirection(activeCell, -1);
						}
						oThis.showComp.oldValue = null;
						oThis.showComp.input.triggerHandler('blur');
						// grid上最后一行的最后一个可编辑列按回车事件
						if ((keyCode == 13) && !e.shiftKey && (nextActiveCell == null)) {
							oThis.hiddenComp();
							oThis.onLastCellEnter(e);
						}
						if (nextActiveCell != null) {
							if (activeCell.rowIndex == nextActiveCell.rowIndex) {
								oThis.hiddenComp();
								oThis.setCellSelected(nextActiveCell);
							}
							// 回车换行
							else if (activeCell.data('rowIndex') != nextActiveCell.data('rowIndex')) {
								oThis.model.setRowSelected(nextActiveCell.data('rowIndex'));
								oThis.hiddenComp();
								oThis.setCellSelected(nextActiveCell);
							}
						}
						e.preventDefault();
						e.stopPropagation();
					}
					// 下移
					else if (keyCode == 40) {
						oThis.showComp.input.triggerHandler('blur');
						var activeCell = oThis.selectedCell;
						var nextActiveCell;
						var rowIndex = oThis.getCellRowIndex(activeCell);
						var colIndex = activeCell.data('colIndex');
						var rowCount = oThis.basicHeaders[colIndex].dataDiv.data('childElementCount');
						// 最后一条记录
						if (rowIndex == rowCount - 1) {
							// 最后一行时，触发onLastCellEnter事件
							oThis.onLastCellEnter(e);
						} else {
							nextActiveCell = oThis.basicHeaders[colIndex].dataDiv.data("cells")[rowIndex + 1];
							if (nextActiveCell) {
								oThis.model.setRowSelected(nextActiveCell.data('rowIndex'));
								oThis.hiddenComp();
								oThis.setCellSelected(nextActiveCell);
							}
						}
					}
					// 上移
					else if (keyCode == 38) {
						oThis.showComp.input.triggerHandler('blur');
						var activeCell = oThis.selectedCell;
						var nextActiveCell;
						var rowIndex = oThis.getCellRowIndex(activeCell);
						var colIndex = activeCell.data('colIndex');
						var rowCount = oThis.basicHeaders[colIndex].dataDiv.data('cells').length;
						nextActiveCell = oThis.basicHeaders[colIndex].dataDiv.data('cells')[rowIndex - 1];
						if (nextActiveCell) {
							oThis.model.setRowSelected(nextActiveCell.data('rowIndex'));
							oThis.hiddenComp();
							oThis.setCellSelected(nextActiveCell);
						}
					}
				});
				comp.hide();
				// 只有IE下支持粘贴操作
				if ($.browsersupport.IS_IE) {
					comp.element[0].onPaste = function(e) {
						if (typeof(grid.onPaste) == 'function') {
							var activeCell = grid.selectedCell;
							var colIndex = activeCell.data('colIndex');
							var filedName = grid.model.rows[0].getFiledNameByColIndex(colIndex);
							var rowIndex = activeCell.data('rowIndex');
							var data = null;
							if (window.clipboardData)
								data = window.clipboardData.getData("text");
							var result = grid.onPaste.call(grid, filedName, rowIndex, data);
							return result;
						} else {
							return true;
						}
					};
				}
				// 数据改变事件
				comp.element.on(comp.widgetName + 'valuechanged', function(e, obj) {
					var currCell = oThis.selectedCell;
					if (currCell) {
						// var comp = valueChangeEvent.obj;
						// 如果输入控件的旧值和改变后的值不一样才改变model的值
						var newValue = comp.getValue();
						if (comp.oldValue != newValue || comp.componentType == "COMBOBOX") {
							// 下拉框的valuechange事件有问题
							var colIndex = currCell.data('colIndex');
							var compColIndex = comp.currColIndex;
							if (compColIndex != null && compColIndex != colIndex)
								return;
							var rowIndex = oThis.getCellRowIndex(currCell);
							if (comp.componentType == "COMBOBOX" && comp.colIndex != null && comp.colIndex != currCell.data('colIndex'))
								colIndex = comp.colIndex;
							// cell编辑后事件
							oThis.onAfterEdit(rowIndex, colIndex, comp.oldValue, newValue);
							oThis.model.setValueAt(rowIndex, colIndex, newValue);
						}
					}
				});
				// 焦点移出事件
				comp.element.on(comp.widgetName + 'onblur', function() {
					var currCell = oThis.selectedCell;
					if (currCell) {
						// 激活将要激活的控件,因为控件隐藏触发失去焦点是个异步的过程,必须在上一个控件失去焦点后才能触发下一个控件激活
						if (oThis.nextNeedActiveCell) {
							oThis.setCellSelected(oThis.nextNeedActiveCell);
							oThis.nextNeedActiveCell = null;
						}
					}
					if (comp.componentType == "EDITOR") {// EditorComp子项，为dataset设值
						if (oThis.getFocusIndex() == -1)
							return;
						var index = oThis.dataset.nameToIndex(this.id);
						if (index == -1)
							return;
						// 当前值
						var newValue = this.getValue();
						// 旧值
						var oldValue = oThis.dataset.getValueAt(oThis.rowIndex,	index);
						if (oldValue != newValue) {// 值发生变化
							// 更新ds内容
							oThis.dataset.setValueAt(oThis.rowIndex, index,	newValue);
						}
					} else {// 非EditorComp子项，鼠标移出且内容为空，进行数据校验
						var selectRows = grid.getSelectedRows();
						if (!selectRows || selectRows.length == 0)
							return;
						var selectRowIndexs = grid.getSelectedRowIndice();
						for (var i = 0; i < selectRows.length; i++) {
							var row = selectRows[i];
							if (typeof(row) == 'undefined') {
								continue;
							}
							var colIndex = comp.currColIndex;
							if (colIndex == -1) {
								continue;
							}
							var value = row.getCellValue(colIndex);
							// 获取当前字段名称
							var keyName = grid.basicHeaders[colIndex].keyName;
							// 获取当前字段在dataset中的索引
							var datasetColIndex = row.pageData.dataset.nameToIndex(keyName);
							var resultStr = $.dataset.checkDatasetCell(row.pageData.dataset, value, datasetColIndex, row);
							var cell = grid.basicHeaders[colIndex].dataDiv.data('cells')[selectRowIndexs[i]];
							cell.data('errorMsg',resultStr);
							var warningIcon = cell.data('warningIcon');
							if (!warningIcon) {
								warningIcon = $("<div>").addClass('cellwarning');
								cell.data('warningIcon',warningIcon).css('position','relative');
							}
							cell.append(warningIcon);
							if (typeof(resultStr) == "string" && resultStr != "") {
								if (typeof(comp.setError) == 'function') {
									comp.setError(true);
								}
								if (typeof(comp.setErrorStyle) == 'function') {
									comp.setErrorStyle();
								}
								if (typeof(comp.setErrorPosition) == 'function') {
									var top = cell.offset().top;
									if (grid.headerDiv && grid.headerDiv.outerHeight()) {
										top += grid.headerDiv.outerHeight();
									}
									if (grid.outerDiv && grid.outerDiv.offset().top > 0) {
										top += grid.outerDiv.offset().top;
									}
									var left = cell.offset().left + $.grid.CELL_LEFT_PADDING;
									comp.setErrorPosition(grid.wholeDiv, left, top - 31);
								}
								warningIcon.show();
							} else {
								if (typeof(comp.setError) == 'function') {
									comp.setError(false);
								}
								if (typeof(comp.setErrorMessage) == 'function') {
									comp.setErrorMessage("");
								}
								if (typeof(comp.setNormalStyle) == 'function') {
									comp.setNormalStyle();
								}
								warningIcon.hide();
							}
						}
					}
				});
			}
		},
		/**
		 * 重新处理设置为自动扩展表头的宽度
		 * 
		 * @private
		 */
		processAutoExpandHeadersWidth : function(gridId) {
			var grid = gridId;
			if(!grid.isHHeader()) {
				var dynamicDivWidth = 0;
				if(grid.isVScroll()) {
					dynamicDivWidth = grid.outerDiv.outerWidth() - grid.constant.headerWidth - $.grid.SCROLLBAE_HEIGHT;
				} else {
					dynamicDivWidth = grid.outerDiv.outerWidth() - grid.constant.headerWidth;
				}
				grid.dynamicColumDataDiv.css('width',dynamicDivWidth + "px");
				return;
			}
			var autoHeaders = grid.getAutoExpandHeaders();
			// 此Header是自动扩展表头
			if (autoHeaders != null && autoHeaders.length > 0) {
				// if (!grid.isScroll()) {
				var expandTotalWidth = 0;
				// 减1是为了修正在portal中自动调整后仍有横向滚动条的问题
				if (grid.isVScroll())
					expandTotalWidth = grid.outerDiv.outerWidth() - grid.getNoAutoExpandHeadersWidth() - $.grid.COLUMN_LEFT_BORDER_WIDTH - 17 - 2;
				else
					expandTotalWidth = grid.outerDiv.outerWidth() - grid.getNoAutoExpandHeadersWidth() - $.grid.COLUMN_LEFT_BORDER_WIDTH - 2;
				// 60px为多选列的宽度
				if (grid.isMultiSelWithBox)
					expandTotalWidth = expandTotalWidth - $.grid.MULTISEL_COLUMN_WIDTH - $.grid.COLUMN_LEFT_BORDER_WIDTH;
				// 如果显示数字列,自动扩展宽度要减去数字列
				if (grid.isShowNumCol)
					expandTotalWidth = expandTotalWidth - grid.constant.rowNumHeaderDivWidth;
				// 每一份的宽度
				var oneWidth = Math.floor(expandTotalWidth / autoHeaders.length) - $.grid.COLUMN_LEFT_BORDER_WIDTH;
				// 如果每一份的宽度大于设定的最小宽度则进行处理，最小宽度目前在常量中指定，待扩展,当前是120
				if (oneWidth > $.grid.EXPANDHEADER_MINWIDTH) {
					for (var i = 0, count = autoHeaders.length; i < count; i++) {
						if (i == count - 1) {
							autoHeaders[i].width = expandTotalWidth - i * (oneWidth + $.grid.COLUMN_LEFT_BORDER_WIDTH) - $.grid.COLUMN_LEFT_BORDER_WIDTH;
							// 改变dynamicHeaderTableDiv的宽度
							var dynTableDivRealWidth = grid.getDynamicTableDivRealWidth(true);
							
							grid.dynamicHeaderTableDiv.css('width',dynTableDivRealWidth + "px");
							if (autoHeaders[i].dataTable && autoHeaders[i].dataTable.size() > 0) {
								autoHeaders[i].dataTable.css('width',autoHeaders[i].width + "px");
								autoHeaders[i].cell.css('width',autoHeaders[i].width);
								autoHeaders[i].contentDiv.css('width',(autoHeaders[i].width - 1) + "px");
								autoHeaders[i].dataDiv.css('width',(autoHeaders[i].width - 1) + "px");
							} else {
								// 多表头
								// 获取多表头列的子表头
							    var childrenHeaders = autoHeaders[i].parent.children;
								autoHeaders[i].contentDiv.css('width',(autoHeaders[i].width - 1) + "px");
								autoHeaders[i].dataDiv.css('width',(autoHeaders[i].width - 1) + "px");
								autoHeaders[i].parent.width = autoHeaders[i].width * childrenHeaders.length;//合并单元格的宽
								
							}
							grid.dynamicColumDataDiv.css('width',dynTableDivRealWidth + "px");
							if (grid.dynSumRowContentDiv) {
								grid.dynSumRowContentDiv.css('width',dynTableDivRealWidth + "px");
							}
							// 处理合计行
							if (autoHeaders[i].sumCell) {
								if (autoHeaders[i].keyName == grid.basicHeaders[0].keyName)
									autoHeaders[i].sumCell.css('width',autoHeaders[i].width + $.grid.ROWSTATE_COLUMN_WIDTH - $.grid.SUMROW_DIV_WIDTH - ($.grid.SUMCELL_PADDING * 2) - 1 + "px");
								else
									autoHeaders[i].sumCell.css('width',autoHeaders[i].width - ($.grid.SUMCELL_PADDING * 2) - 1 + "px");
							}
							if (grid.dynSumPageDataDiv)
								grid.dynSumPageDataDiv.css('width',dynTableDivRealWidth + "px");
						} else {
							if (autoHeaders[i].cell && autoHeaders[i].cell.size()>0) {
								autoHeaders[i].width = oneWidth;
								autoHeaders[i].cell.css('width',oneWidth);
								autoHeaders[i].contentDiv.css('width',(oneWidth - 1) + "px");
								autoHeaders[i].dataTable.css('width',(oneWidth-1) + "px");
								autoHeaders[i].dataDiv.css('width',(oneWidth - 1) + "px") ;
							} else {      //多表头
								// 获取多表头列的子表头
								var childrenHeaders = autoHeaders[i].parent.children;
	//							autoHeaders[i].parent.dataTable.style.width = oneWidth * childrenHeaders.length + "px";
								autoHeaders[i].topHeader.dataTable.css('width',(autoHeaders[i].width * (childrenHeaders.length-1) + oneWidth) + "px") ;
								autoHeaders[i].width = oneWidth ;  //多表头列的宽
								autoHeaders[i].contentDiv.css('width',(oneWidth - 1) + "px");
							    autoHeaders[i].dataDiv.css('width',(oneWidth - 1) + "px");
							    for (var j = 0; j < childrenHeaders.length; j++) {
							    	if(j < childrenHeaders.length-1) {
	//						    		childrenHeaders[j].topHeader.width = oneWidth * count;//合并单元格的宽
								    	childrenHeaders[j].width = autoHeaders[i].width;
								    	childrenHeaders[j].contentDiv.css('width',(autoHeaders[i].width - 1) + "px");
								    	childrenHeaders[j].dataDiv.css('width',(autoHeaders[i].width - 1)  + "px");
							    	}else{//多表头最后一个单元格
							    		childrenHeaders[j].width = oneWidth;
								    	childrenHeaders[j].contentDiv.css('width',(oneWidth - 1) + "px");
								    	childrenHeaders[j].dataDiv.css('width',(oneWidth - 1) + "px");
							    	}
								}
							}
	
							// 处理合计行
							if (autoHeaders[i].sumCell) {
								if (autoHeaders[i].keyName == grid.basicHeaders[0].keyName)
									autoHeaders[i].sumCell.css('width',autoHeaders[i].width + $.grid.ROWSTATE_COLUMN_WIDTH - $.grid.SUMROW_DIV_WIDTH - ($.grid.SUMCELL_PADDING * 2) + "px");
								else
									autoHeaders[i].sumCell.css('width',autoHeaders[i].width - ($.grid.SUMCELL_PADDING * 2) + "px");
							}
						}
						if (!autoHeaders[i].isHidden) {
							for (var j = 0, rowLength = autoHeaders[i].dataDiv.data('cells').length; j < rowLength; j++) {
								var tempCell = autoHeaders[i].dataDiv.data('cells')[j];
								if (tempCell) {
									// 修正当有列设置为autoExpand="true"时，该元素宽度的显示问题
									tempCell.css('width',autoHeaders[i].width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING + "px");
								}
							}
						}
					}
				}
			}
		},
		/**
		 * 显示空行信息
		 * 画动态列数据区div时被调用，为解决onbeforeshow()之前显示空行信息，增加根据进度条进行判断的逻辑
		 */
		showNoRowsDiv : function(gridId) {
			if (window.loadingBar && window.loadingBar.visible) {
				setTimeout(function() {
					$.grid.showNoRowsDiv(gridId);
				}, 500);
			} else {
				var oThis = gridId;
				// 如果onbeforeshow事件后，数据不为空，则会将needShowNoRowsDiv置为false
				if (oThis.needShowNoRowsDiv != null && oThis.needShowNoRowsDiv) {
					oThis.noRowsDiv.show();
					oThis.dynamicColumDataDiv.css('margin-bottom','0px');
					oThis.dataOuterDiv.css('overflow','hidden').hide();
				}
			}
		},
		doShowTipMessage : function(left, top, width, title) {
			var div = $.grid.tipDiv;
			if ($.grid.tipDiv == null) {
				div = $("<div>").addClass('tip_message').css('z-index',$.measures.getZIndex()).appendTo("body");
				$.grid.tipDiv = div;
				$.grid.popwindow(div);
			}
			div.show().css({
				'left' : left + "px",
				'top' : (top - 7) + "px"
			});
			div.data('centerDiv').html(decodeURIComponent(title));
		},
		/**
		 * grid自动调整大小逻辑
		 * @param gridId 当前调整大小grid的id
		 */
		gridResize : function(gridId) {
			var grid = gridId;
			if (grid == null)
				return;
			var outerDiv = grid.wholeDiv;
			var barHeight = $.grid.SCROLLBAE_HEIGHT;
			outerDiv.css('height','100%');
			grid.height = "100%";
			outerDiv.css('width',"100%");
			grid.width = '100%';
			try {
				if (!grid.flowmode) {
					var height = outerDiv.outerHeight();
					if (grid.descDiv)
						height = height - grid.descDiv.outerHeight();
					if (grid.headerBtnDiv && grid.headerBtnDiv.is(":visible"))
						height = height - grid.headerBtnDiv.outerHeight();
					if (!grid.paginationBar || !grid.paginationBar.is(":visible") || grid.pageSize == -1)
						height = height - (grid.isHHeader() ? grid.constant.headerHeight : 0);
					else
						height = height - (grid.isHHeader() ? grid.constant.headerHeight : 0) - $.grid.PAGEBAR_HEIGHT;
					if (height > 0) {
						grid.dataOuterDiv.css('height',height + "px");
					}
				}
				// 如果此时grid大小和上次不发生变化,则不会真正的重新调整grid大小
				var cond1 = (grid.constant.outerDivWidth != null && grid.constant.outerDivWidth == outerDiv.outerWidth());
				var cond2 = (grid.constant.outerDivHeight != null && grid.constant.outerDivHeight == outerDiv.outerHeight());
				if (cond1 && cond2)
					return;
				grid.constant.outerDivWidth = outerDiv.outerWidth();
				grid.constant.outerDivHeight = outerDiv.outerHeight();
				grid.constant.fixedHeaderDivWidth = grid.fixedHeaderDiv.outerWidth();
				if (grid.width.indexOf("%") != -1) {
					var fixedHeaderWidth = grid.constant.fixedHeaderDivWidth;
					var currWidth = grid.constant.outerDivWidth;
					var fixedColumDivWidth = grid.fixedColumDiv.outerWidth();
					grid.dataOuterDiv.css('width', (grid.isHHeader()? (currWidth - fixedColumDivWidth) : currWidth- grid.constant.headerWidth )+ "px");
					// 垂直滚动
					if (grid.isVScroll()) {
						var _headerDivWidth = grid.isHHeader() ? (currWidth - barHeight) : grid.constant.headerWidth;
						grid.headerDiv.css('width', _headerDivWidth + "px");
						grid.headerDiv.data('defaultWidth',_headerDivWidth);
						var dynHeaderWidth = grid.isHHeader() ? (currWidth - fixedHeaderWidth - barHeight) : grid.constant.headerWidth;
						if (dynHeaderWidth > 0)
							grid.dynamicHeaderDiv.css('width',dynHeaderWidth + "px");
						grid.dynamicHeaderDiv.data('defaultWidth',dynHeaderWidth);
					} else {
						var _headerDivWidth = grid.isHHeader() ? currWidth : grid.constant.headerWidth;
						grid.headerDiv.css('width',_headerDivWidth + "px");
						grid.headerDiv.data('defaultWidth',_headerDivWidth);
						var dynHeaderWidth = grid.isHHeader() ? (currWidth - fixedHeaderWidth) : grid.constant.headerWidth;
						if (dynHeaderWidth > 0)
							grid.dynamicHeaderDiv.css('width',dynHeaderWidth + "px");
						grid.dynamicHeaderDiv.data('defaultWidth',dynHeaderWidth);
					}
					if(!grid.isHHeader()) {
						if(grid.isScroll()) {
							grid.headerDiv.css('height',(grid.constant.outerDivHeight - barHeight) + "px");
						} else {
							grid.headerDiv.css('height',grid.constant.outerDivHeight + "px");
						}
					}
				}
				grid.setScrollLeft(0);
				// 调整合计行的top
				if (grid.isShowSumRow && grid.sumRowDiv) {
					// 调整放置合计行的div的宽度
					if (grid.dynamicColumDataDiv && grid.dynamicColumDataDiv.outerWidth() > 0) {
						grid.dynSumRowContentDiv.css('width',(grid.dynamicColumDataDiv.outerWidth()) + "px");
					} else {
						grid.dynSumRowContentDiv.css('width',(grid.dynamicHeaderDiv.outerWidth()) + "px");
					}
				}
				// 调整自动扩展表头的宽度,采用缓画,只有gridResize结束后才调整表头自动扩展列的宽度
				if (grid.stForAutoExpand != null)
					clearTimeout(grid.stForAutoExpand);
				grid.stForAutoExpand = setTimeout(function() {
					$.grid.processAutoExpandHeadersWidth(gridId);
				}, 100);
			} catch (e) {
			}
		},
		popwindow : function(parentDiv) {
			$("<div>").addClass('left_top_div').appendTo(parentDiv);
			$("<div>").addClass('center_top_div').appendTo(parentDiv);
			$("<div>").addClass('right_top_div').appendTo(parentDiv);
			$("<div>").addClass('left_center_div').appendTo(parentDiv);
			var centerDiv = $("<div>").addClass('center_div').appendTo(parentDiv);
			$("<div>").addClass('right_center_div').appendTo(parentDiv);
			$("<div>").addClass('left_bottom_div').appendTo(parentDiv);
			$("<div>").addClass('center_bottom_div').appendTo(parentDiv);
			$("<div>").addClass('right_bottom_div').appendTo(parentDiv);
			parentDiv.data('centerDiv',centerDiv);
		},
		pageNavgate : function(e, index, grid) {
			e.preventDefault();
			e.stopPropagation();
			grid = $(this).data('gridId');
			if ($(this).data('pageIndex')) {
				index = $(this).data('pageIndex');
			}
			if (index == grid.pageIndex)
				return;
			grid.processServerPagination(index);
		},
		grid_init : function(e) {
			var grid = $(this).data('refGrid');
			// 首先隐藏掉上一个显示出的控件
			if (grid.showComp != null) {
				grid.hiddenComp();
			}
			// 得到触发源(header.contentDiv)
			var src = $(e.target);
			if (src.is("input") || src.is("img")) {
				return;
			}
			// 触发源div相对body的left
			var offsetLeft = src.offset().left;
			var outerDivScrollLeft = grid.dataOuterDiv.scrollLeft();
			// 当前鼠标的位置
			var currX = e.pageX + $('body').scrollLeft();
			var dragSrc = null;
			if (src.is("td")) {
				dragSrc = src;
			} else if (src.is("table")) {
				dragSrc = src.find("td:first");
			} else {
				var tempSrc = src.parent();
				while (!tempSrc.is("td")) {
					tempSrc = tempSrc.parent();
				}
				if (tempSrc == null)
					dragSrc = src;
				else
					dragSrc = tempSrc;
			}
			// 获得触发源所在的header
			var curHeader = dragSrc.data('owner');
			if (curHeader == null) {
				return;
			}
			// 获得grid的引用
			window.gridOwner = curHeader.owner;
			// 鼠标在以下这两个区域内才能拖拽
			if (currX > offsetLeft + parseInt(dragSrc.attr('width')) - 5 && currX < offsetLeft + parseInt(dragSrc.attr('width'))) {
				// 保存真正处理拖动的header
				window.dragHeader = curHeader;
				window.src = src;
				window.dragStart = true;
				window.src.css('cursor','col-resize');
			}
			window.dragSrc = dragSrc;
			// 开始拖动时的X坐标
			window.dragSrcX = e.pageX + $('body').scrollLeft();
			// 拖动表头的原始宽度(被拖的td宽度)
			window.defaultHeaderWidth = parseInt(window.dragSrc.attr('width'));
			// 动态数据区的原始宽度(整个)
			window.dynamicColumDataDivWidth = window.gridOwner.dynamicColumDataDiv.outerWidth();
			// 动态表头的初始宽度
			window.defaultDynamicHeaderWidth = window.gridOwner.getDynamicTableDivRealWidth(true) + 2;
			window.defaultDynHeaderTableWidth = window.gridOwner.dynamicHeaderTableDiv.outerWidth();
			// 保存此表头的允许最小宽度
			if (window.src != null) {
				if (window.src.children().first().is('input'))
					window.minWidth = $.measures.getTextWidth(window.src.children('eq(1)').html(), window.src.attr('class')) + 25;
				else
					window.minWidth = $.measures.getTextWidth(window.src.html(), window.src[0].nodeName) + 10;
			}
		},
		grid_end : function(e) {
			window.dragStart = false;
			if(window.src != null)
			    window.src.css('cursor','default');
		},
		grid_drag : function(e) {
			// 得到触发源(header.contentDiv)
			var src = $(e.target);
			if (src.size() > 0 && (src.is("input") || src.is("img"))) {
				return;
			}
			// 触发源div相对body的left
			var offsetLeft = src.offset().left;
			// 当前鼠标的位置
			var currX = e.pageX + $('body').scrollLeft();
			if (window.dragStart == null || window.dragStart == false) {
				var dragSrc = null;
				if (src.is("td")) {
					dragSrc = src;
				} else if (src.is("table")) {
					dragSrc = src.find("td:first");
				} else {
					var tempSrc = src.parent();
					while (!tempSrc.is("td")) {
						tempSrc = tempSrc.parent();
					}
					if (tempSrc == null)
						dragSrc = src;
					else
						dragSrc = tempSrc;
				}
				var flag = 0;
				if (currX > offsetLeft + parseInt(dragSrc.attr('width')) - 5 && currX < offsetLeft + parseInt(dragSrc.attr('width'))) {
					// 多表头暂时不允许拖动
					if (this.refHeader && this.refHeader.isGroupHeader)
						return;
					flag = 1;
				}
				if (flag == 1 || flag == 2)
					src.css('cursor','e-resize');
				else
					src.css('cursor','default');
			}
			// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
			if ((e.button == 1 || e.button == 0) && window.dragStart) {
				window.src.css('cursor','col-resize');
				window.headerChangedWidth = currX - window.dragSrcX;
			}
			// 拖拽时处理
			if (window.dragStart != null && window.dragStart) {
				var header = window.dragHeader;
				// 改变拖动表头的宽度
				if (header.isFixedHeader == false) {/* 改变动态表头的宽度、改变数据区的宽度、改变相应列的宽度 */
					// 改变的宽度
					var changedWidth = window.headerChangedWidth;
					// 没有drag则changedWidth为null,直接返回不进行下面的处理
					if (changedWidth == null)
						return;
					// 当前表头的宽度(拖动的td)
					var currWidth = window.defaultHeaderWidth + changedWidth;
					if (currWidth > 0 && currWidth > window.minWidth) {/* 改变动态数据区、动态表头宽度 */
						// 动态列headerDiv
						var grid = window.gridOwner;
						// 改变动态表头区宽度(整个)
						grid.dynamicHeaderTableDiv.css('width',window.defaultDynHeaderTableWidth + changedWidth + "px");
						grid.dynamicColumDataDiv.css('width', window.dynamicColumDataDivWidth + changedWidth + "px");
						// 改变拖动表头列宽度
						header.dataDiv.css('width',(currWidth-1) + "px");
						
						if (grid.dynSumRowContentDiv) {
							grid.dynSumRowContentDiv.css('width',(window.dynamicColumDataDivWidth + changedWidth) + "px");
						}
						// 改变拖动表头div的宽度
						if (header == grid.getLastDynamicVisibleHeader())
							header.contentDiv.css('width',(currWidth - 1) + "px");
						else
							header.contentDiv.css('width',(currWidth - 1) + "px");
						// 改变拖动表头td的宽度
						window.dragSrc.attr('width',currWidth);
						// 改变拖动表头table的宽度
						header.dataTable.css('width',currWidth + "px");
						// 如果该列为合计列,设置合计列的宽度
						if (header.sumCell) {
							grid.dynSumPageDataDiv.css('width',(window.defaultDynHeaderTableWidth + changedWidth) + "px");
							if (header.keyName == grid.basicHeaders[0].keyName)
								header.sumCell.css('width',currWidth + $.grid.ROWSTATE_COLUMN_WIDTH - $.grid.SUMROW_DIV_WIDTH - ($.grid.SUMCELL_PADDING * 2) + "px");
							else
								header.sumCell.css('width',currWidth - ($.grid.SUMCELL_PADDING * 2) + "px");
						}
						// 记录拖动表头的width属性
						header.width = currWidth;
						grid.adjustFixedColumDivHeight();
					}
					// 增加此段代码是为了避免表头和数据区div在拖动之后不重合的问题,关键所在! gd 2008-02-21
					var grid = window.gridOwner;
					if ($.measures.isDivScroll(grid.dataOuterDiv)) {
						if (grid.dataOuterDiv.scrollLeft() > 0) {
							grid.setScrollLeft(grid.dataOuterDiv.scrollLeft() - 1);
						}
					}
					// 循环设置该列每个cell的宽度，以适应XHTML中的宽度计算问题
					for (var i = 0, n = header.dataDiv.children().length; i < n; i++) {
						// 获取cell元素
						if (header.dataDiv.children(':eq('+i+')').attr('class').indexOf("gridcell_") != -1) {
							header.dataDiv.children(':eq('+i+')').css('width',(header.dataDiv.outerWidth() - 21) + "px");
						}
					}
				}
			}
		},
		/**
		 * grid滚动处理函数.设置30毫秒延迟
		 */
		handleScrollEvent : function(e) {
			$.grid.handleScrollEvent.triggerObj = e.triggerObj;
			if ($.grid.handleScrollEvent.timer != null)
				clearTimeout($.grid.handleScrollEvent.timer);
			$.grid.handleScrollEvent.timer = setTimeout(function(){}, 30);
		}
	});
	var FormaterMap = $.hashmap.getObj();
	$.widget("lui.grid", $.lui.base, {
		options : {
			id : '',
			left : '0',
			top : '0',
			width : '100%',
			height : '100%',
			position : 'absolute',
			editable : true,
			isShowTip : true,
			isAllowMouseoverChange : true,
			isMultiSelWithBox : false,
			isShowNumCol : false,
			isShowSumRow : false,
			attr : null,
			groupHeaderIds : '',
			sortable : true,
			className : 'grid_div',
			isPagenationTop : true,
			showColInfo : true,
			oddType : '0',
			isGroupWithCheckbox : true,
			isShowHeader : true,
			extendCellEditor : CellEditor,
			rowRender : DefaultRowRender,
			currentLanguageCode : '',
			headerPosition : 'top',
			headerWidth : $.grid.HEADERROW_WIDTH,
			onclick : null,
			onmouseout : null,
			onmouseover : null,
			onmousedown : null,
			onmouseup : null
		},
		_initParam : function() {
			this.id = this.options.id;
			this._super();
			this.componentType = "GRIDCOMP";
			this.name = this.options.name;
			this.left = this.options.left;
			this.top = this.options.top;
			this.width = this.options.width;
			this.height = this.options.height;
			this.position = $.argumentutils.getString(this.options.position, "absolute");
			this.className = $.argumentutils.getString(this.options.className,	"grid_div");
			this.rowRender = this.options.rowRender == null ? DefaultRowRender : this.options.rowRender;
			this.currentLanguageCode = this.options.currentLanguageCode;
			this.extendCellEditor = this.options.extendCellEditor;
			// 初始化静态常量
			this.initStaticConstant();
			// 标示此grid是否可以编辑
			this.editable = this.options.editable;
			//是否显示提示
			this.isShowTip = this.options.isShowTip;
			// 当前选中行的索引(数组)
			this.selectedRowIndice = null;
			// 表头数组(最上层header的集合,多表头情况指最上层的header)
			// this.headers = null;
			// 表头基础数组(最下层header的集合,多表头情况指最下层包括真正有用信息的header),此数组是根据headers内部生成的
			this.basicHeaders = [];
			// grid行高度
			this.rowHeight = $.grid.ROW_HEIGHT;
			this.headerRowHeight = $.grid.HEADERROW_HEIGHT;
			this.headerRowWidth = $.grid.HEADERROW_WIDTH;
			// 数据区真正的宽度
			this.realWidth = 0;
			// 编辑控件.针对每种类型控件初始化一个控件,然后将控件动态展示到编辑单元格上,这样做主要是展现效率问题.
			this.compsMap = null;
			// 保存当前激活的cell
			this.currActivedCell = null;
			// 保存当前选中的cell
			this.selectedCell = null;
			// 调用addOneRow()第一次出现竖直滚动条的标志
			this.firstVScroll = false;
			// 保存所有的静态表头headers
			this.defaultFixedHeaders = null;
			// 保存所有的动态表头headers
			this.defaultDynamicHeaders = null;
			// 当前显示编辑控件
			this.showComp = null;
			// 标示控件是否被初始化过
			this.compsInited = false;
			// 是否多行选择(多行选择则显示第一列的固定选择列,否则不显示第一行的固定选择列)
			this.isMultiSelWithBox = this.options.isMultiSelWithBox;
			// 是否显示数字行号
			this.isShowNumCol = this.options.isShowNumCol;
			// 是否显示合计行
			this.isShowSumRow = this.options.isShowSumRow;
			// 分组的列
			this.groupHeaderIds = this.options.groupHeaderIds;
			// this.groupHeaderIds = "pk_branch_showName,degree,sex";
			if (this.groupHeaderIds == null)
				this.groupHeaderIds = "";
			if (this.groupHeaderIds != "")
				this.groupHeaderIds = this.groupHeaderIds.split(",");
			// 整体是否可以排序
			this.sortable = this.options.sortable;
			this.pageSize = -1;
			this.flowmode = false;
			this.autoRowHeight = false;
			// 行高自适应时 记录每行最小高度
			this.rowMinHeight = [];
			this.defaultRowMinHeight = [];
			// 是否运行态
			this.isRunMode = false;
			// 是否简单分页
			this.isSimplePagination = false;
			// 是否显示功能按钮
			this.isShowImageBtn = false;
			// 自定义功能按钮
			this.selfDefImageBtnRender = null;
			// 粘贴事件
			this.onPaste = null;
			// 描述信息
			this.descArray = null;
			this.canCopy = true;
			// 解析grid属性对象
			if (this.options.attr != null) {
				this.pageSize = $.argumentutils.getInteger(	this.options.attr.pageSize, this.pageSize);
				this.flowmode = this.options.attr.flowmode;
				this.isRunMode = $.argumentutils.getBoolean(this.options.attr.isRunMode, this.isRunMode);
				this.isSimplePagination = $.argumentutils.getBoolean(this.options.attr.isSimplePagination,	this.isSimplePagination);
				this.isShowImageBtn = $.argumentutils.getBoolean(this.options.attr.isShowImageBtn, this.isShowImageBtn);
				this.autoRowHeight = $.argumentutils.getBoolean(this.options.attr.autoRowHeight, this.autoRowHeight);
				this.canCopy = $.argumentutils.getBoolean(this.options.attr.canCopy, this.canCopy);
				this.selfDefImageBtnRender = this.options.attr.selfDefImageBtnRender;
				this.onPaste = this.options.attr.onPaste;
				if (this.options.attr.descArray instanceof Array) {
					this.descArray = this.options.attr.descArray;
				} else if (typeof(this.options.attr.descArray) != 'undefined') {
					this.descArray = [];
					this.descArray.push(this.options.attr.descArray);
				}
			}
			// 翻页条是否在上面
			this.isPagenationTop = this.options.isPagenationTop;
			// 是否可显示“显示列”和“锁定列”菜单
			this.showColInfo = this.options.showColInfo;
			// 判断单双行类型，“0”为单双行各一行交错排列；“1”为单行2行，双行1行交错排列
			this.oddType = 0;
			// this.options.oddType;
			// 分组显示时，多选框是否分组
			this.isGroupWithCheckbox = this.options.isGroupWithCheckbox;
			// 是否显示表头，默认为显示
			this.isShowHeader = this.options.isShowHeader;
			// 存储页面显示焦点行索引
			this.focusIndex = -1;
			this.headerPosition = this.options.headerPosition;
			this.headerWidth = this.options.headerWidth;
			// 注册外部回掉函数
			window.clickHolders.push(this);
			this.treeLevel = null;
		},
		_create : function() {
			this._initParam();
		},
		create : function() {
			// 创建最外层包容div
			if (this.wholeDiv == null) {
				this.initWholeDiv();
				// 描述信息
				this.initDescArrayDiv();
				// 功能按钮
				this.initImageBtn();
				// 用户自定义功能按钮
				if (this.isRunMode	&& typeof(this.selfDefImageBtnRender) == 'function') {
					this.selfDefImageBtnRender.call(this, this);
				}
				this.initOuterDiv();
				// 初始化错误提示对象
				this.initWholeErrorMsgDiv();
			}
			this.setGridDescContent();
			this.manageSelf();
		},
		/**
		 * 销毁控件
		 * 
		 * @private
		 */
		destroySelf : function() {
			this.basicHeaders = null;
			// 销毁模型
			if (this.model) {
				this.model.destroySelf();
				this.model = null;
			}
			// 销毁编辑控件
			if (this.compsMap) {
				var comps = this.compsMap.values();
				for (var i = 0; i < comps.length; i++) {
					var comp = comps[i];
					comp.destroySelf();
				}
				this.compsMap.clear();
				this.compsMap = null;
			}
			this._super();
		},
		/**
		 * 返回grid的显示对象
		 */
		getObjHtml : function() {
			return this.wholeDiv;
		},
		/**
		 * 创建Grid框架各个组成部分
		 * 
		 * @private
		 */
		manageSelf : function() {
			var oThis = this;
			// 在此之前必须完成model的初始化工作,否则下面的框架初始化没法完成
			if (this.model == null)
				return;
			this.paintData();
			// 如果控件可编辑,则创建编辑控件
			if (this.model.dataset.editable) {
				// 缓初始化编辑控件
				setTimeout(function() {
					$.grid.initEditCompsForGrid(oThis);
				}, 100);
			}
			// 重新设置自动表头的宽度
			setTimeout(function() {$.grid.processAutoExpandHeadersWidth(oThis);}, 350);
		},
		/**
		 * 判断是否为双数行
		 * 
		 * @private
		 */
		isOdd : function(index) {
			if (index == null)
				return false;
			if (this.oddType == "0") {
				return index % 2 == 1;
			} else if (this.oddType == "1") {
				return index % 3 != 0;
			}
		},
		initWholeDiv : function() {
			if (!this.wholeDiv) {
				this.wholeDiv = this.element;
			}
			this.wholeDiv.addClass('whole_grid_div');
		},
		initDescArrayDiv : function() {
			if (!this.descDiv) {
				this.descDiv = $("<div>").addClass('desc_div').appendTo(this.wholeDiv);
				this.descBKMiddleDiv = $("<div>").addClass('desc_bk_middle_div').appendTo(this.descDiv);
			}
		},
		setGridDescContent : function(descMsg) {
			this.descBKMiddleDiv.empty();
			if (descMsg instanceof Array) {
				this.descArray = descMsg;
			} else if (typeof(descMsg) != 'undefined') {
				this.descArray = [];
				this.descArray.push(descMsg);
			}
			if (this.descArray && this.descArray.length > 0) {
				for (var i = 0; i < this.descArray.length; i++) {
					var desc = $("<font>").addClass('desc_msg').html(this.descArray[i]).appendTo(this.descBKMiddleDiv);
					if (i == 0) {
						desc.css("padding-left", "5px");
					}
					if (i == this.descArray.length - 1) {
						desc.css("border-right", "none");
					}
				}
				this.descDiv.show();
				$.grid.gridResize(this);
			}
		},
		initImageBtn : function() {
			if (typeof(this.headerBtnDiv) == 'undefined') {
				this.headerBtnDiv = $("<div>").addClass('headerbtnbar_div').appendTo(this.wholeDiv);
			}
			if (this.isShowImageBtn) {
				this.headerBtnDiv.show();
			} else {
				this.headerBtnDiv.hide();
			}
//			if (!this.isRunMode) {// 如果是非运行态,隐藏功能按钮区.
//				this.headerBtnDiv.hide();
//			}
			this.menubarComp = $("<div>").appendTo($(this.headerBtnDiv)).menubar({
					left : 0,
					top : 8,
					className : 'white_menubar_div2'
				}).menubar("instance");
			this.menubarComp.centerDiv.css('width', 'auto');
			this.menubarComp.element.children().first().css('float', 'right');
		},
		setHeaderBtnVisible : function(visible) {
			this.isShowImageBtn = visible;
			if (visible)
				this.headerBtnDiv.show();
			else
				this.headerBtnDiv.hide();
			$.grid.gridResize(this);
		},
		addHeaderBtn : function(id, caption, imgSrc) {
			if (!this.isShowImageBtn) {
				return;
			}
			this.menubarComp.addMenu(id, caption, caption, imgSrc, null, false, null);
		},
		getHeaderBtn : function(id) {
			if (!this.isShowImageBtn) {
				return;
			}
			return this.menubarComp.getMenu(id);
		},
		removeHeaderBtn : function(id) {
			if (!this.isShowImageBtn) {
				return;
			}
			if (this.menubarComp.menuItems
					&& this.menubarComp.menuItems != null
					&& this.menuItems.values()
					&& this.menuItems.values().length > 0) {
				var items = this.menuItems.values();
				for (var i = 0; i < items.length; i++) {
					if (items[i].id && items[i].id == id) {
						items[i].destroySelf();
						break;
					}
				}
			}
		},
		removeAllHeaderBtn : function() {
			if (!this.isShowImageBtn) {
				return;
			}
			this.menubarComp.destroySelf();
		},
		/**
		 * 初始化全局错误提示对象
		 */
		initWholeErrorMsgDiv : function(errorBoldMsg, errorMsg) {
			var oThis = this;
			this.errorMsgDiv = $("<div>").addClass('error_whole_msg_div').hide().appendTo(this.wholeDiv);
			// 九宫格
			this.wholeMsgDiv = $("<div>").addClass('whole_msg_div').appendTo(this.errorMsgDiv);
			$("<div>").addClass('bg_left_top').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_top_middle').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_right_top').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_right_middle').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_right_bottom').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_bottom_middle').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_left_bottom').appendTo(this.wholeMsgDiv);
			$("<div>").addClass('bg_left_middle').appendTo(this.wholeMsgDiv);
			this.errorCenterDiv =  $("<div>").addClass('error_center_up_div').appendTo(this.wholeMsgDiv);
			this.errorMsg = $("<div>").addClass('errorMsg').appendTo(this.errorCenterDiv);
			this.warningIcon = $("<div>").addClass('warning').appendTo(this.wholeMsgDiv);
			this.closeIcon = $("<div>").addClass('close_normal').appendTo(this.wholeMsgDiv).on({
				'mouseover' : function(e) {
					$(this).removeClass().addClass("close_press");
				},
				'mouseout' : function(e) {
					$(this).removeClass().addClass("close_normal");
				},
				'mouseup' : function(e) {
					$(this).removeClass().addClass("close_normal");
					oThis.errorMsgDiv.hide();
				}
			});
		},
		/**
		 * 隐藏整体错误信息框
		 */
		hideErrorMsg : function(widgetId, componentId) {
			var widget = pageUI.getViewPart(widgetId);
			if (widget) {
				var component = widget.getComponent(componentId);
				if (component && this.componentType == component.componentType) {
					if (component.errorMsgDiv) {
						component.errorMsgDiv.hide();
					}
				}
			}
		},
		setWholeErrorPosition : function() {
		},
		onImageBtnClick : function(fun) {
		},
		/**
		 * 初始化静态常量 创建GridComp时调用
		 */
		initStaticConstant : function() {
			$.grid.HEADERROW_HEIGHT = $.measures.getCssHeight(this.className + "_HEADERROW_HEIGHT");
			$.grid.ROW_HEIGHT = $.measures.getCssHeight(this.className	+ "_ROW_HEIGHT");
			$.grid.SELECTCOLUM_WIDTH = $.measures.getCssHeight(this.className + "_SELECTCOLUM_WIDTH");
			$.grid.CELL_BOTTOM_BORDER_WIDTH = $.measures.getCssHeight(this.className + "_CELL_BOTTOM_BORDER_WIDTH");
		},
		/**
		 * 外部click事件发生时的回调函数.用来隐藏当前显示的控件
		 */
		outsideClick : function(e) {
			if (e && e.calendar)
				return;
			if (this.showComp) {
				if (window.clickHolders.trigger == this.showComp)// 显示参照DIV情况
					return;
				this.hiddenComp();
			}
			this.hideTipMessage(true);
			this.hiddenSettingMenu();
		},
		/**
		 * 外部鼠标滚轮事件发生时的回调函数.用来隐藏当前显示的控件
		 */
		outsideMouseWheelClick : function(e) {
			if (e && e.calendar)
				return;
			if (this.showComp) {
				if (window.clickHolders.trigger == this.showComp)// 显示参照DIV情况
					return;
				this.hiddenComp();
			}
			this.hiddenSettingMenu();
			this.hideTipMessage(true);
		},
		/**
		 * 数据区点击后执行方法
		 */
		click : function(e) {
			document.onclick(e);
			// 隐藏已经显示出来的设置按钮
			this.hiddenSettingMenu();
			// grid整体禁用直接返回
			if (this.isGridActive == false)
				return;
			// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
			var cell = this.getRealCell(e);
			var columDiv = cell.parent();
			var rowIndex = this.getCellRowIndex(cell);
			var colIndex = cell.data('colIndex');
			// 只处理点击动态数据区和静态数据区cell的情况,点击数据区的其他地方不处理
			if (columDiv == null || (columDiv.parent().size() > 0 && columDiv.parent().attr('id') != "dynamicDataDiv"))
				return;
			// 首先隐藏掉上一个显示出的控件
			if (this.showComp != null)
				this.hiddenComp();
			this.setFocusIndex(rowIndex);
			// 行选中之前调用用户的方法,返回false将不允许点击下一行
			var _row = this.getRow(rowIndex);
			if (this.onBeforeRowSelected(rowIndex, _row) == false)
				return;
			if (this.isMultiSelWithBox) {
				this.selectColumDiv.children(':eq('+rowIndex+')').children().first().triggerHandler('mousedown',e);
			} else
				this.processCtrlSel(false, rowIndex);
			// 不管整体能否编辑都要将点击的cell传给用户,征求用户处理意见(参数:cellItem, rowId,
			// columId)(比如用户想显示一些提示信息)
			if (this.onCellClick(cell, rowIndex, colIndex) == false) {
				e.preventDefault();
				return;
			}
			if(!_row.pageData.isEdit) {
				return;
			}
			// 如果当前列可编辑，单元格编辑前调用的方法
			if (this.model.dataset.editable && this.basicHeaders[colIndex].columEditable) {
				if (this.onBeforeEdit(rowIndex, colIndex) == false)
					return;
			}
			// 调用公有方法激活真正相应cell的控件
			this.setCellSelected(cell, e.ctrlKey);
			// 改变当前点击的cell的外观
			this.changeSelectedCellStyle(rowIndex);
			if (typeof(this.compsMap) != 'undefined' && this.compsMap != null) {
				var comp = null;
				var comps = this.compsMap.values();
				if (comps && comps.length > 0) {
					for (var i = 0; i < comps.length; i++) {
						if (typeof(cell.data('editorType')) == 'string' && comps[i].componentType == cell.data('editorType').toUpperCase()) {
							comp = comps[i];
							break;
						}
					}
				}
				if (comp != null) {
					var warningIcon = cell.data('warningIcon');
					if (typeof(warningIcon) == 'undefined') {
						warningIcon = $("<div>").addClass('cellwarning').appendTo(cell);
						cell.data('warningIcon',warningIcon);
						cell.css('position','relative');
					}
					if (typeof(cell.data('errorMsg')) == "string"	&& cell.data('errorMsg') != "") {
						if (typeof(comp.setError) == 'function') {
							comp.setError(true);
						}
						if (typeof(comp.setErrorMessage) == 'function') {
							comp.setErrorMessage(cell.data('errorMsg'));
						}
						if (typeof(comp.setErrorStyle) == 'function') {
							comp.setErrorStyle();
						}
						if (typeof(comp.setErrorPosition) == 'function') {
							var top = cell.offset().top;
							if (this.headerDiv && this.headerDiv.outerHeight()) {
								top += this.headerDiv.outerHeight();
							}
							if (this.outerDiv && this.outerDiv.offset().top > 0) {
								top += this.outerDiv.offset().top;
							}
							var left = cell.outerWidth() * (colIndex) + 10;
							comp.setErrorPosition(this.wholeDiv, left, top - 31);
						}
						warningIcon.show();
					} else {
						if (typeof(comp.setError) == 'function') {
							comp.setError(false);
						}
						if (typeof(comp.setErrorMessage) == 'function') {
							comp.setErrorMessage("");
						}
						if (typeof(comp.setNormalStyle) == 'function') {
							comp.setNormalStyle();
						}
						warningIcon.hide();
					}
				}
			}
		},
		/**
		 * 数据区的事件处理
		 */
		attachEvents : function() {
			var oThis = this;
			// 监测整体数据区的双击事件
			this.dataOuterDiv.on({
				'dblclick' : function(e) {
					// grid整体禁用直接返回
					if (oThis.isGridActive == false)
						return;
					// 整体能编辑不支持双击事件
					if (oThis.editable)
						return;
					// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
					var cell = oThis.getRealCell(e);
					if (cell == null || cell[0] == this)// 点击的是空白区域
						return;
					var rowIndex = oThis.getCellRowIndex(cell);
					oThis.onRowDblClick(rowIndex, oThis.getRow(rowIndex));
				},
				'rclick' : function(e) {
					// 先执行点击方法，选中该行
					oThis.click(e);
					var result = oThis.onDataOuterDivContextMenu(e);
					if (result == false) {
						e.preventDefault();
						e.stopPropagation();
					}
				},
				'click' : function(e) {
					oThis.click(e);
					oThis.hideTipMessage(true);
					e.stopPropagation();
				},
				'keydown' : function(e) {
					// grid整体禁用直接返回
					if (oThis.isGridActive == false) {
						return;
					}
					// 首先隐藏掉上一个显示出的控件
					if (oThis.showComp != null)
						oThis.hiddenComp();
					var cell = oThis.selectedCell;
					if (cell == null)
						cell = $(e.target);
					var ch = e.keyCode;
					// 当前没有选中的单元格，直接返回
					if (!cell.is("div")) {
						return;
					}
					var rowIndex = oThis.getCellRowIndex(cell);
					var colIndex = cell.data('colIndex');
					// 下移
					if (ch == 40) {
						if (!oThis.isMultiSelWithBox) {// 单选表格
							if (rowIndex == null) {// 当前没有选中单元格
								var selIndexs = oThis.getSelectedRowIndice();
								if (selIndexs == null || selIndexs.length == 0)
									rowIndex = -1;
								else
									rowIndex = selIndexs[0];
								// 选中下一行
								oThis.model.setRowSelected(rowIndex + 1);
							} else if (rowIndex + 1 <= oThis.getRowsNum() - 1) {
								cell = oThis.getCell(rowIndex + 1, colIndex);
								oThis.setCellSelected(cell);
								// 选中下一行
								oThis.model.setRowSelected(rowIndex + 1);
							}
						}
					}
					// 上移
					else if (ch == 38) {
						if (!oThis.isMultiSelWithBox) {// 单选表格
							if (rowIndex == null) {// 当前没有选中单元格
								var selIndexs = oThis.getSelectedRowIndice();
								if (selIndexs == null || selIndexs.length == 0) {
									return;
								} else
									rowIndex = selIndexs[0];
								// 选中下一行
								if (rowIndex > 0)
									oThis.model.setRowSelected(rowIndex - 1);
							} else if (rowIndex > 0) {
								// 选中上一行单元格
								cell = oThis.getCell(rowIndex - 1, colIndex);
								oThis.setCellSelected(cell);
								// 选中上一行
								oThis.model.setRowSelected(rowIndex - 1);
							}
						}
					}
					// 左移
					else if (ch == 37) {
						if (!oThis.isMultiSelWithBox && rowIndex != null) {// 单选表格
							var tmpCell = oThis.getCell(rowIndex, colIndex - 1);
							var tmpRowIndex = oThis.getCellRowIndex(tmpCell);
							if (tmpCell == null)
								tmpCell = oThis.getVisibleCellByDirection(cell, -1);
							if (tmpCell != null) {
								oThis.setCellSelected(tmpCell);
								if (cell.data('rowIndex') != tmpRowIndex)// 选中新行
									oThis.model.setRowSelected(tmpRowIndex);
								// 改变当前点击的cell的外观
								oThis.changeSelectedCellStyle(tmpRowIndex);
							}
						}
					}
					// 右移
					else if (ch == 39) {
						if (!oThis.isMultiSelWithBox && rowIndex != null) {// 单选表格
							var tmpCell = oThis.getCell(rowIndex, colIndex + 1);
							var tmpRowIndex = oThis.getCellRowIndex(tmpCell);
							if (tmpCell == null) {
								if (rowIndex + 1 <= oThis.getRowsNum() - 1) {
								}
								tmpCell = oThis.getVisibleCellByDirection(cell, 1);
							}
							if (tmpCell != null) {
								oThis.setCellSelected(tmpCell);
								if (cell.data('rowIndex') != tmpRowIndex)// 选中新行
									oThis.model.setRowSelected(tmpRowIndex);
								// 改变当前点击的cell的外观
								oThis.changeSelectedCellStyle(tmpRowIndex);
							}
						}
					}
					// 开放Ctrl C
					if (!e.ctrlKey) {
						e.preventDefault();
					}
				},
				'scroll' : function(e) {
					// 修改编辑控件位置
					if (oThis.currActivedCell && oThis.showComp) {
						if (oThis.currActivedCell.data('editorType') == $.editortype.TEXTAREA) {
							var cell = oThis.currActivedCell;
							var comp = oThis.showComp;
							var bodyWidth = $('body').outerWidth();
							var bodyHeight = $('body').outerHeight();
							if (bodyWidth < 100 || bodyHeight < 200) {
								comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING,
										cell.offset().top - 1, 
										cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
										cell.outerHeight());
							} else {
								var compLeft = cell.offset().left + $.grid.CELL_LEFT_PADDING;
								var compTop = cell.offset().top - 1;
								if (parseInt(compLeft) + 200 > bodyWidth)
									compLeft = bodyWidth - 200;
								if (parseInt(compTop) + 100 > bodyHeight)
									compTop = bodyHeight - 100;
								comp.setBounds(compLeft, compTop, "200", "100");
							}
						} else {
							oThis.showComp.setBounds(oThis.currActivedCell.offset().left + $.grid.CELL_LEFT_PADDING,
										oThis.currActivedCell.offset().top,
										oThis.currActivedCell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
										oThis.currActivedCell.outerHeight());
							oThis.showComp.setFocus();
						}
					}
					// 滚动时的缓加载数据
					e.triggerObj = oThis;
					$.grid.handleScrollEvent(e);
					// 滚动时隐藏掉当前显示的控件
					if (oThis.showComp != null) {
						oThis.autoScroll = false;
					}
					var src = $(e.target);
					if(oThis.isHHeader()) {
						var iScrollLeft = src.scrollLeft();
						if (oThis.dynamicHeaderDiv.data('oldLeft') == null) {
							oThis.dynamicHeaderDiv.data('oldLeft',parseInt(oThis.dynamicHeaderDiv.css('left')));
						}
						oThis.dynamicHeaderDiv.css('left',(oThis.dynamicHeaderDiv.data('oldLeft') - iScrollLeft) + "px");
						// 动态列headerDiv
//						if (oThis.dynamicHeaderDiv.data('defaultWidth') + iScrollLeft > 0)
//							oThis.dynamicHeaderDiv.css('width',(oThis.dynamicHeaderDiv.data('defaultWidth') + iScrollLeft) + "px");
					} else {
						var iScrollTop = src.scrollTop();
						if (oThis.dynamicHeaderDiv.data('oldTop') == null) {
							oThis.dynamicHeaderDiv.data('oldTop',parseInt(oThis.dynamicHeaderDiv.css('top')));
						}
						oThis.dynamicHeaderDiv.css('top',(oThis.dynamicHeaderDiv.data('oldTop') - iScrollTop) + "px");
					}
					// 合计行的div,"合计"
					if (oThis.sumRowDiv) {
						oThis.dynSumPageDataDiv.css('left',oThis.sumRowDiv.outerWidth() + "px");
					}
					if (oThis.isMultiSelWithBox) {
						var scrollTop = src.scrollTop();
						if (oThis.selectColumDiv) {
							oThis.selectColumDiv.css('top',(-1 * scrollTop) + "px");
						}
					}
					if (oThis.isShowNumCol) {
						var scrollTop = src.scrollTop();
						if (oThis.rowNumDiv) {
							oThis.rowNumDiv.css('top',(-1 * scrollTop) + "px");
						}
					}
					e.triggerObj = null;
				}
			});
			// 固定列的双击事件(改变整体的行的外观)
			this.fixedColumDiv.on({
				'dblclick' : function(e) {
					// grid整体禁用直接返回
					if (oThis.isGridActive == false)
						return;
					// 整体能编辑不支持双击事件
					if (oThis.editable)
						return;
					// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
					var cell = oThis.getRealCell(e);
					// 删除事件对象（用于清除依赖关系）
					if (cell == null || cell == this) {// 点击的是空白区域
						return;
					}
					var rowIndex = oThis.getCellRowIndex(cell);
					oThis.onRowDblClick(cell.rowIndex, oThis.getRow(rowIndex));
				},
				'click' : function(e) {
					// 隐藏已经显示出来的设置按钮
					oThis.hiddenSettingMenu();
					// grid整体禁用直接返回
					if (oThis.isGridActive == false)
						return;
					// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
					var cell = oThis.getRealCell(e);
					// 点击行标区直接返回
					if (cell.attr('id') == "rowNumDiv" || cell.attr('id') == "fixedColum") {
						return;
					}
					// 点击行标div,行状态div直接返回
					if (cell.parent().attr('id').startWith("numline")
							|| cell.attr('id').startWith("numline")
							|| cell.parent().attr('id').startWith("sumRowDiv")
							|| cell.attr('id').startWith("sumRowDiv")
							|| cell.parent().attr('id') == "lineStateColumDiv") {
						return;
					}
					// 改变当前选中行的外观
					var columDiv = cell.parent();
					var rowIndex = oThis.getCellRowIndex(cell);
					var colIndex = cell.data('colIndex');
					// 点击的是固定选择列
					if (columDiv.attr('id') == "fixedSelectColum") {
					} else {
						// 首先隐藏掉上一个显示出的控件
						if (oThis.showComp != null)
							oThis.hiddenComp();
						// 行选中之前调用用户的方法
						if (oThis.onBeforeRowSelected(rowIndex, oThis.getRow(rowIndex)) == false) {
							return;
						}
						// 不管整体能否编辑都要将点击的cell传给用户,征求用户处理意见(参数:cellItem, rowId,
						// columId)(比如用户想显示一些提示信息)
						if (oThis.onCellClick(cell, rowIndex, colIndex) == false) {
							// 多选模式下只改变选中行的外观
							if (oThis.isMultiSelWithBox) {
								// oThis.rowSelected(rowIndex);
							} else {
								oThis.processCtrlSel(false, rowIndex);
							}
							e.preventDefault();
							return;
						}
						// 单元格编辑前调用的方法
						if (oThis.onBeforeEdit(rowIndex, colIndex) == false) {
							oThis.rowSelected(rowIndex);
							return;
						}
						// 调用公有方法激活真正相应cell的控件
						oThis.setCellSelected(cell, e.ctrlKey);
						if (columDiv.parent().attr('id') == "fixedDataDiv") {
							// 多选模式下只改变选中行的外观
							if (oThis.isMultiSelWithBox)
								// oThis.rowSelected(rowIndex);
								// 多选状态,调用checkbox事件.
								if (oThis.model.treeLevel == null)
									oThis.selectColumDiv.children(':eq('+rowIndex+')').children().first().triggerHandler('mousedown',e);
								else {
									oThis.processCtrlSel(false, rowIndex);
								}
						}
						e.stopPropagation();
					}
				}
			});
			// 增加统一的resize事件处理必须给对象一个id
			this.outerDiv.attr('id',oThis.id + "_outerdiv");
			// 给outerDiv增加onresize方法
			$(window).on("resize", function() {$.grid.gridResize(oThis);});
			// 弹出窗口中包含grid，在编辑控件显示时，滚动滚动条（body第一个子元素的滚动条），隐藏编辑控件。
			if ($('body').children().size()>0) {
				if (!$('body').children().first().data('gridMap')) {
					$('body').children().first().data('gridMap',$.hashmap.getObj());
				}
				$('body').children().first().data('gridMap').put(this.id, this);
				$('body').children().first().on('scroll',function(e){
					// 滚动时隐藏掉当前显示的控件
					var grids = this.gridMap.values();
					for (var i = 0; i < grids.length; i++) {
						if (grids[i].showComp != null) {
							if (grids[i].autoScroll != true)
								grids[i].hiddenComp();
							else
								grids[i].autoScroll = false;
						}
					}
					e.stopPropagation();
				});
			}
		},
		/**
		 * 设置dataOuterDiv的横向滚动条位置
		 */
		setScrollLeft : function(scrollLeft) {
			// 系统自动调整标志（非手工拖动）
			this.autoScroll = true;
			this.dataOuterDiv.scrollLeft(scrollLeft);
		},
		/**
		 * 设置dataOuterDiv的纵向滚动条位置
		 */
		setScrollTop : function(scrollTop) {
			// 系统自动调整标志（非手工拖动）
			this.autoScroll = true;
			this.dataOuterDiv.scrollTop(scrollTop);
		},
		/**
		 * 得到真正的cell对象(render的时候用户可能在cell中加上自己的div，所以点击的不一定是cell)
		 */
		getRealCell : function(e) {
			var cell = $(e.target);
			if (cell.data('editorType') == null) {
				var pNode = cell.parent();
				while (pNode.size()>0) {
					if (pNode.data('editorType') != null) {
						cell = pNode;
						break;
					}
					pNode = pNode.parent();
				}
			}
			return cell;
		},
		/**
		 * 设置grid数据行单行高度
		 */
		setRowHeight : function(height) {
			height = parseInt(height);
			if (height < 10)
				height = 10;
			this.rowHeight = height;
		},
		/**
		 * 设置grid表头单行高度
		 */
		setHeaderRowHeight : function(height) {
			height = parseInt(height);
			if (height < 10)
				height = 10;
			this.headerRowHeight = height;
		},
		/**
		 * 根据行,列索引得到cell 注意:如果此列是隐藏列则继续向下获取不隐藏的最近的一个 cell,此cell可能位于下一行中
		 * @param rowIndex 行索引值
		 * @param colIndex 列索引值
		 */
		getCell : function(rowIndex, colIndex) {
			rowIndex = parseInt(rowIndex);
			colIndex = parseInt(colIndex);
			if (rowIndex < 0 || rowIndex > this.getRowsNum() - 1)
				return null;
			if (colIndex < 0 || colIndex > this.basicHeaders.length - 1)
				return null;
			if (this.basicHeaders[colIndex] != null && this.basicHeaders[colIndex].isHidden == false)
				return this.basicHeaders[colIndex].dataDiv.data('cells')[rowIndex];
			return null;
		},
		/**
		 * 获取该cell的前一个或者上一个可见cell
		 * @param direction -1,向左;1,向右
		 */
		getVisibleCellByDirection : function(cell, direction) {
			var rowIndex = this.getCellRowIndex(cell);
			var colIndex = cell.data('colIndex');
			// 尝试获取下一个可见cell
			if (direction == 1) {
				// 首先获取本行内的下一个可见cell
				for (var j = colIndex + 1, count = this.basicHeaders.length; j < count; j++) {
					if (this.basicHeaders[j].isHidden == false)
						return this.basicHeaders[j].dataDiv.data('cells')[rowIndex];
				}
				// 如果本行内没有了可见cell,则继续向下面的行搜寻可见cell
				for (var i = rowIndex + 1, rowNum = this.getRowsNum(); i < rowNum; i++) {
					for (var j = 0, count = this.basicHeaders.length; j < count; j++) {
						if (this.basicHeaders[j].isHidden == false)
							return this.basicHeaders[j].dataDiv.data('cells')[i];
					}
				}
			}
			// 尝试获取上一个可见cell
			else if (direction == -1) {
				for (var j = colIndex - 1; j >= 0; j--) {
					if (this.basicHeaders[j].isHidden == false)
						return this.basicHeaders[j].dataDiv.data('cells')[rowIndex];
				}
				for (var i = rowIndex - 1; i >= 0; i--) {
					for (var j = this.basicHeaders.length - 1; j >= 0; j--) {
						if (this.basicHeaders[j].isHidden == false)
							return this.basicHeaders[j].dataDiv.data('cells')[i];
					}
				}
			}
			return null;
		},
		/**
		 * 获取该cell的前一个或者上一个可编辑cell
		 * @param direction -1,向左;1,向右
		 */
		getEditableCellByDirection : function(cell, direction) {
			if (this.editable == false)
				return null;
			var rowIndex = this.getCellRowIndex(cell);
			var colIndex = cell.data('colIndex');
			// 尝试获取下一个可见cell
			if (direction == 1) {
				// 首先获取本行内的下一个可见cell
				for (var j = colIndex + 1, count = this.basicHeaders.length; j < count; j++) {
					if (this.basicHeaders[j].isHidden == false
							&& this.basicHeaders[j].columEditable == true
							&& this.onBeforeEdit(rowIndex, j) != false)
						return this.basicHeaders[j].dataDiv.data('cells')[rowIndex];
				}
				// 如果本行内没有了可见cell,则继续向下面的行搜寻可见cell
				for (var i = rowIndex + 1, rowNum = this.getRowsNum(); i < rowNum; i++) {
					for (var j = 0, count = this.basicHeaders.length; j < count; j++) {
						if (this.basicHeaders[j].isHidden == false
								&& this.basicHeaders[j].columEditable == true
								&& this.onBeforeEdit(i, j) != false)
							return this.basicHeaders[j].dataDiv.data('cells')[i];
					}
				}
			}
			// 尝试获取上一个可见cell
			else if (direction == -1) {
				for (var j = colIndex - 1; j >= 0; j--) {
					if (this.basicHeaders[j].isHidden == false
							&& this.basicHeaders[j].columEditable == true
							&& this.onBeforeEdit(rowIndex, j) != false)
						return this.basicHeaders[j].dataDiv.data('cells')[rowIndex];
				}
				for (var i = rowIndex - 1; i >= 0; i--) {
					for (var j = this.basicHeaders.length - 1; j >= 0; j--) {
						if (this.basicHeaders[j].isHidden == false
								&& this.basicHeaders[j].columEditable == true
								&& this.onBeforeEdit(i, j) != false)
							return this.basicHeaders[j].dataDiv.data('cells')[i];
					}
				}
			}
			return null;
		},
		/**
		 * 设置cell选中，同时让隐藏的cell显示出来，如果该列能够编辑则激活相应的编辑控件
		 * 若选择模式为单选模式则同时通知model行选中，多行选择模式下如果是checkbox多选仅改变行的选中外观，如果是ctrl多行则改变选中的多行
		 */
		setCellSelected : function(cell, ctrl, shift) {
			var oThis = this;
			if (cell == null)
				return;
			if (this.isMultiSelWithBox && !this.editable) {
				// 多选并且不是编辑态,不设置cell选中.
				return;
			}
			var cellClassName = cell.attr('class');
			// 第一次没有选中的cell
			if (this.oldCell == null) {
				if (this.basicHeaders[cell.data('colIndex')] != null && this.basicHeaders[cell.data('colIndex')].isFixedHeader)
					cell.addClass('fixedcell_select');
				else
					cell.addClass('cell_select');
				this.oldCell = cell;
				this.oldClassName = cellClassName;
			}
			// 有选中的cell了
			else {
				// 选中cell和当前cell不是同一个cell
				if (this.oldCell[0] != cell[0]) {
					this.oldCell.attr('class',this.oldClassName);
					var oldRowIndex = this.getCellRowIndex(this.oldCell);
					var isOdd = this.isOdd(oldRowIndex);
					var curHeader = this.basicHeaders[this.oldCell.data('colIndex')];
					if (!this.isMultiSelWithBox) {
						if (this.selectedRowIndice != null && this.selectedRowIndice.indexOf(oldRowIndex) != -1) {
							if (curHeader != null && curHeader.isFixedHeader)
								this.oldCell.attr('class',isOdd ? "fixed_gridcell_odd fixedcell_select" : "fixed_gridcell_even fixedcell_select");
							else
								this.oldCell.attr('class',isOdd ? "gridcell_odd cell_select" : "gridcell_even cell_select");
						} else {
							if (curHeader != null && curHeader.isFixedHeader)
								this.oldCell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even");
							else
								this.oldCell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
						}
					} else {
						if (curHeader != null && curHeader.isFixedHeader)
							this.oldCell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even");
						else
							this.oldCell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
					}
					this.oldCell = cell;
					this.oldClassName = cellClassName;
				}
			}
			// 记录当前选中的cell
			this.selectedCell = cell;
			// 让隐藏的cell显示出来
			this.letCellVisible(cell);
			// 如果该列可以编辑设置cell编辑控件激活
			if (this.editable && this.basicHeaders[cell.data('colIndex')].columEditable) {
				this.setCellActive(cell);
			}
			// 如果列不能编辑设置cell的焦点,注意该处必须设置焦点,避免cell失去焦点而导致不能接收键盘事件
			else
				cell.focus();
		},
		/**
		 * 激活某个cell的编辑控件
		 * @param cell 当前选中的表格单元格
		 * @param ctrl 当前是否按着ctrl键操作,按着ctrl键不激活相应控件
		 */
		setCellActive : function(cell, ctrl) {
			if (this.editable == false)
				return;
			if (ctrl)
				return;
			// 记录选中cell为当前cell
			this.selectedCell = cell;
			var rowIndex = this.getCellRowIndex(cell);
			var colIndex = cell.data('colIndex');
			// 用户自定义editor
			var extendComp = null;
			if (this.extendCellEditor !== null) {
				var row = this.model.rows[rowIndex];
				extendComp = this.extendCellEditor.call(this, $('body'), row, colIndex);
			}
			if (extendComp) {
				this.compsMap.put("extend$" + colIndex, extendComp);
				$.grid.addCompListener(this, extendComp, colIndex);
				extendComp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING,
						cell.offset().top, 
						cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
						$.grid.ROW_HEIGHT);
				extendComp.setValue(this.model.getCellValueByIndex(rowIndex,colIndex));
				extendComp.show();
				extendComp.setFocus();
				this.currActivedCell = cell;
				this.showComp = extendComp;
				this.showComp.extend = true;
				this.showComp.element.css('z-index',$.measures.getZIndex());
			} else {
				// 如果comp为null,得到stringtext
				if (cell.data('editorType') == null || cell.data('editorType') == "") {
					var comp = this.compsMap.get($.editortype.STRINGTEXT);
					comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING, 
							cell.offset().top, 
							cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
							cell.outerHeight());
					// 将真实值设置到编辑控件中
					comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
					comp.show();
					comp.setFocus();
					this.currActivedCell = cell;
					this.showComp = comp;
				} else if (cell.data('editorType') != $.editortype.CHECKBOX
						&& cell.data('editorType') != $.editortype.COMBOBOX
						&& cell.data('editorType') != $.editortype.MULTICOMBOBOX
						&& cell.data('editorType') != $.editortype.REFERENCE
						&& cell.data('editorType') != $.editortype.TEXTAREA
						&& cell.data('editorType') != $.editortype.LANGUAGECOMBOBOX) {
					var comp = this.compsMap.get(cell.data('editorType'));
					var header = cell.parent().data('header');
					if (cell.data('editorType') == $.editortype.STRINGTEXT) {
						comp.setMaxSize(header.maxLength);
					}
					// 若为数字类型根据header的precision属性设置精度,如果有最大最小值则进行设置
					if (cell.data('editorType') == $.editortype.DECIMALTEXT) {
						comp.setPrecision(header.precision);
						if (header.floatMinValue != null)
							comp.setMinValue(header.floatMinValue);
						if (header.floatMaxValue != null)
							comp.setMaxValue(header.floatMaxValue);
					}
					// 若为整数类型根据header的maxValue和minValue设置最大最小值
					if (cell.data('editorType') == $.editortype.INTEGERTEXT) {
						comp.setIntegerMinValue(header.integerMinValue);
						comp.setIntegerMaxValue(header.integerMaxValue);
					}
					if (cell.data('editorType') == $.editortype.DATETEXT
							|| cell.data('editorType') == $.editortype.MULTIDATETEXT
							|| cell.data('editorType') == $.editortype.DATETIMETEXT
							|| cell.data('editorType') == $.editortype.MULTIDATETIMETEXT) {
						comp.id = header.keyName;
					}
					comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING, 
							cell.offset().top, 
							cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
							cell.outerHeight());
					if(cell.data('editorType') == $.editortype.MULTIDATETEXT || cell.data('editorType') == $.editortype.MULTIDATETIMETEXT) {
						var _val = this.model.getCellValueByIndex(rowIndex,colIndex);
						if(_val) {
							var _valArray = _val.split(comp.options.multiSplitChar);
							if(_valArray.length==2) {
								comp.isNext = false;
								comp.setValue(_valArray[0]);
								comp.isNext = true;
								comp.setValue(_valArray[1]);
							} else {
								comp.isNext = false;
								comp.setValue(_valArray[0]);
							}
						}
					} else {
						comp.setValue(this.model.getCellValueByIndex(rowIndex,colIndex));
					}
					
					comp.show();
					comp.setFocus();
					this.currActivedCell = cell;
					this.showComp = comp;
				}
				// 处理下拉框类型(激活控件时)
				else if (cell.data('editorType') == $.editortype.COMBOBOX || cell.data('editorType') == $.editortype.MULTICOMBOBOX) {
					var comp = null;
					if(cell.data('combo')) {
						comp = cell.data('combo');
					} else {
						comp = this.compsMap.get(cell.data('editorType') + colIndex);
					}
					var header = this.basicHeaders[colIndex];
					comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING, 
							cell.offset().top, 
							cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
							cell.outerHeight());
					// 将记录的上次的旧值清空
					if (comp.oldValue)
						comp.oldValue = null;
					comp.show();
					comp.setFocus();
					comp.nowCell = cell;
					var selInd = -1;
					if (header.comboData != null) {
						var keyValues = header.comboData.getValueArray();
						if(cell.data('editorType') == $.editortype.MULTICOMBOBOX) {
							comp.setNullValue(false);
							selInd = [];
							var _val = this.model.getCellValueByIndex(rowIndex, colIndex);
							if(_val) {
								var _valArray= _val.split(comp.options.multiSplitChar);
								$.each(_valArray,function(index,_item) {
									selInd.push(keyValues.indexOf(_item));
								});
							}
						} else {
							selInd = keyValues.indexOf(this.model.getCellValueByIndex(rowIndex, colIndex));
						}
						
					} else if(cell.data('comboData')) {
						var keyValues = cell.data('comboData').getValueArray();
						selInd = keyValues.indexOf(this.model.getCellValueByIndex(rowIndex, colIndex));
					}
					if(cell.data('editorType') == $.editortype.MULTICOMBOBOX) {
						if(selInd!=-1) {
							if(selInd.length>0) {
								$.each(selInd,function(index,_item){
									comp.setSelectedItem(_item);
								});
							} else {
								comp.setSelectedItem(-1);
							}
						}
					} else {
						comp.setSelectedItem(selInd);
					}
					this.currActivedCell = cell;
					this.showComp = comp;
				}
				// 处理多语输入控件
				else if (cell.data('editorType') == $.editortype.LANGUAGECOMBOBOX) {
					var comp = this.compsMap.get(cell.data('editorType') + colIndex);
					var header = this.basicHeaders[colIndex];
					comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING, 
							cell.offset().top, 
							cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
							cell.outerHeight());
					// 将记录的上次的旧值清空
					if (comp.oldValue)
						comp.oldValue = null;
					// 初始化下拉数据
					var gridDs = this.model.dataset;
					var currentRow = gridDs.getSelectedRow();
					comp.setComboDatas4Grid(gridDs, currentRow);
					comp.show();
					comp.setFocus();
					comp.nowCell = cell;
					this.showComp = comp;
				}
				// 处理参照
				else if (cell.data('editorType') == $.editortype.REFERENCE) {
					var comp = null;
					if(cell.data('refnode')) {
						comp = cell.data('refnode');
					} else{
					    comp = this.compsMap.get(cell.data('editorType') + cell.data('colIndex'));
					}
					comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING, 
							cell.offset().top, 
							cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
							cell.outerHeight());
					comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
					comp.show();
					comp.setFocus();
					this.currActivedCell = cell;
					this.showComp = comp;
				}
				// 文本域类型
				else if (cell.data('editorType') == $.editortype.TEXTAREA) {
					var comp = this.compsMap.get(cell.data('editorType'));
					var bodyWidth = $('body').innerWidth();
					var bodyHeight = $('body').innerHeight();
					if (bodyWidth < 100 || bodyHeight < 200)
						comp.setBounds(cell.offset().left + $.grid.CELL_LEFT_PADDING, 
								cell.offset().top - 1, 
								cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
								cell.outerHeight());
					else {
						var compLeft = cell.offset().left + $.grid.CELL_LEFT_PADDING;
						var compTop = cell.offset().top - 1;
						if (parseInt(compLeft) + 200 > bodyWidth)
							compLeft = bodyWidth - 200;
						if (parseInt(compTop) + 100 > bodyHeight)
							compTop = bodyHeight - 100;
						comp.setBounds(compLeft, compTop, cell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1, cell.outerHeight());
					}
					comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
					comp.show();
					comp.setFocus();
					this.currActivedCell = cell;
					this.showComp = comp;
				}
				// 在弹出窗口中，激活某个cell的编辑控件，弹出窗口会遮住comp
				if (this.showComp) {
					this.showComp.currColIndex = colIndex;
					this.showComp.element.css('z-index', $.measures.getZIndex());
				}
			}
		},
		/**
		 * 若cell处于部分隐藏状态，则将此cell全部显示出来
		 * @param cell 要全部显示出来的cell
		 */
		letCellVisible : function(cell) {
			if (cell == null)
				return;
			// 处理左右隐藏的情况
			// 该cell之前所有显示的表头的宽度
			var preHeadersWidth = this.getPreHeadersWidth(cell);
			var columDiv = cell.parent();
			var rowIndex = this.getCellRowIndex(cell);
			var iScrollLeft = this.dataOuterDiv.scrollLeft();
			var flag = true;
			if (preHeadersWidth == 0) {
				this.setScrollLeft(0);
				// 1px为修正cell边框显示重合问题
				flag = false;
			} else if (iScrollLeft > preHeadersWidth) {
				var deltX = iScrollLeft - preHeadersWidth;
				this.setScrollLeft(iScrollLeft - deltX - 1);
				// 1px为修正cell边框显示重合问题
				flag = false;
			}
			var gridWidth = this.constant.outerDivWidth;
			var gridHeight = this.constant.outerDivHeight;
			if (this.pageSize > 0)// 有分页条，要将其高度减去
				gridHeight -= $.grid.PAGEBAR_HEIGHT;
			if (flag) {
				var realWidth = columDiv.offset().left + cell.outerWidth();
				var currWidth = gridWidth - this.constant.fixedColumDivWidth + iScrollLeft;
				if (realWidth > currWidth) {
					var deltX = realWidth - currWidth;
					if (this.isVScroll()) {
						this.setScrollLeft(iScrollLeft + deltX + 1	+ $.grid.SCROLLBAE_HEIGHT);
					} else {
						this.setScrollLeft(iScrollLeft + deltX + 1);
					}
				}
			}
			// 处理上下隐藏的情况
			var preRowsHeight = rowIndex * this.rowHeight;
			var iScrollTop = this.dataOuterDiv.scrollTop();
			if (iScrollTop > preRowsHeight) {
				var deltY = iScrollTop - preRowsHeight;
				this.setScrollTop(iScrollTop - deltY);
			}
			var realHeight = (rowIndex + 1) * this.rowHeight;
			if (this.isScroll())
				currHeight = gridHeight - this.constant.headerHeight + iScrollTop - $.grid.SCROLLBAE_HEIGHT;
			else
				currHeight = gridHeight - this.constant.headerHeight + iScrollTop;
			if (realHeight > currHeight) {
				var deltY = realHeight - currHeight;
				this.setScrollTop(iScrollTop + deltY);
			}
		},
		/**
		 * 处理按住ctrl并且选行时的逻辑
		 * @ctrl 是否按住ctrl键点击当前行
		 */
		processCtrlSel : function(ctrl, rowIndex) {
			// 如果此时有多行选中,则清除所有选中行,然后选中当前行
			if (this.selectedRowIndice != null && this.selectedRowIndice.length > 1) {
				// 选中当前行
				this.model.setRowSelected(rowIndex);
			} else {
				// 如果点击的cell处在选中行上,并且该行已经选中则不再通知model行选中 gd 2007-12-05
				if (this.selectedRowIndice == null || (this.selectedRowIndice.length > 0 && this.selectedRowIndice[0] != rowIndex))
					this.model.setRowSelected(rowIndex);
				else
					this.rowSelected(rowIndex);
			}
		},
		/**
		 * 设置当前焦点行失去焦点,Grid没有焦点行.
		 */
		loseFocusIndex : function() {
			var focusIndex = this.getFocusIndex();
			if (typeof(focusIndex) == 'number' && focusIndex >= 0) {
				var headers = this.basicHeaders;
				for (var i = 0; i < headers.length; i++) {
					if (headers[i].dataDiv) {
						var focusCell = headers[i].dataDiv.data('cells')[focusIndex];
						if (focusCell && focusCell.hasClass('cell_focus')) {
							focusCell.removeClass('cell_focus');
							focusCell.isFocusRow = false;
						}
					}
				}
				this.focusIndex = -1;
				this.model.setFocusIndex(-1);
			}
		},
		/**
		 * 设置当前聚焦行（点击行，包括选中或未选中）
		 */
		setFocusIndex : function(rowIndex) {
			// 如果行rowIndex < 0,焦点行不改变.
			if (typeof(rowIndex) == 'number' && rowIndex >= 0) {
				var oldFocusRowIndex = this.getFocusIndex();
				// 设置新焦点行在数据集中的真实索引,真实索引由GridCompModel负责转换.
				this.model.setFocusIndex(rowIndex);
				var headers = this.basicHeaders;
				// 设置旧焦点行恢复失去焦点时样式,设置新焦点行获取焦点时样式.
				for (var i = 0; i < headers.length; i++) {
					if (headers[i].dataDiv && headers[i].dataDiv.data('cells')) {
						var focusCell = null;
						if (typeof(oldFocusRowIndex) == 'number' && oldFocusRowIndex >= 0 && rowIndex != oldFocusRowIndex) {
							// 旧焦点行恢复失去焦点时样式
							focusCell = headers[i].dataDiv.data('cells')[oldFocusRowIndex];
							if (focusCell && focusCell.hasClass('cell_focus')) {
								focusCell.removeClass('cell_focus');
								focusCell.data('isFocusRow',false);
							}
						}
						focusCell = headers[i].dataDiv.data('cells')[rowIndex];
						if (focusCell && !focusCell.hasClass('cell_focus')) {// 新焦点行获取焦点时样式
							focusCell.addClass("cell_focus");
							focusCell.data('isFocusRow',true);
						}
					}
				}
				this.focusIndex = rowIndex;
			}
		},
		/**
		 * 获取当前聚焦行（点中行）
		 */
		getFocusIndex : function() {
			// 返回页面显示焦点行索引,不是pageData数据集中的真实行索引.
			return this.focusIndex;
		},
		/**
		 * 清除所有选中行外观
		 */
		clearAllUISelRows : function() {
			var selRowsIndice = this.selectedRowIndice;
			if (selRowsIndice != null && selRowsIndice.length > 0) {
				// 所有选中行
				var selIndice = [];
				for (var i = 0, count = selRowsIndice.length; i < count; i++)
					selIndice.push(this.selectedRowIndice[i]);
				// 清除所有选中行的外观
				for (var i = selIndice.length - 1; i >= 0; i--) {
					var index = selIndice[i];
					for (var j = 0, headerLength = this.basicHeaders.length; j < headerLength; j++) {
						var header = this.basicHeaders[j];
						if (header.isHidden == false) {
							var cell = header.dataDiv.data('cells')[index];
							if (cell != null) {
								var isOdd = this.isOdd(index);
								// 校验不通过的的字段颜色不清除掉
								if (cell.data('isErrorCell')) {
									if (header.isFixedHeader)
										cell.attr('class',isOdd	? "fixed_gridcell_odd cell_error" : "fixed_gridcell_even cell_error");
									else
										cell.attr('class',isOdd	? "gridcell_odd cell_error"	: "gridcell_even cell_error");
								} else {
									if (header.isFixedHeader)
										cell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even");
									else
										cell.attr('class',isOdd	? "gridcell_odd" : "gridcell_even");
								}
							}
						}
					}
					// 设置此行状态div的背景
					var node = this.lineStateColumDiv.data('cells')[index];
					if (node != null && node.attr('class') != "row_state_div row_update_state"
							&& node.attr('class') != "row_state_div row_add_state")
						node.attr('class',"row_state_div");
					// 从选择数组中将此选择行删除
					this.selectedRowIndice.splice(i, 1);
					// 没有选中行将选中数组置空
					if (this.selectedRowIndice.length == 0)
						this.selectedRowIndice = null;
				}
			}
		},
		/**
		 * 获取非自动扩展表头的宽度
		 */
		getNoAutoExpandHeadersWidth : function() {
			if (this.basicHeaders == null)
				return -1;
			var width = 0;
			for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
				if (!this.basicHeaders[i].isHidden	&& !this.basicHeaders[i].isAutoExpand)
					width += this.basicHeaders[i].width + $.grid.COLUMN_LEFT_BORDER_WIDTH;
			}
			return width;
		},
		/**
		 * 获取自动扩展宽度的表头
		 */
		getAutoExpandHeaders : function() {
			if (this.basicHeaders == null)
				return null;
			var autoHeaders = [];
			for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
				if (!this.basicHeaders[i].isHidden	&& this.basicHeaders[i].isAutoExpand)
					autoHeaders.push(this.basicHeaders[i]);
			}
			return autoHeaders;
		},
		/**
		 * 初始化所有常量
		 */
		initConstant : function() {
			if (this.constant == null)
				this.constant = {};
			if (this.wholeDiv.outerWidth() != 0) {
				this.constant.outerDivHeight = this.wholeDiv.outerHeight();
				this.constant.outerDivWidth = this.wholeDiv.outerWidth();
			}
		},
		/**
		 * 获得基本头信息。此grid支持多表头，只有最下层的表头包含有用信息。
		 * @param headers 最顶层的headers
		 */
		initBasicHeaders : function() {
			// 已经调用model.setDataset初始化了basicHeaders
			var basicHeaders = this.model.getBasicHeaders();
			// 对需要分组的header设上标示
			if (this.groupHeaderIds != "") {
				// 记录分组列的列索引
				this.groupHeaderColIndice = [];
				var j = 0;
				for (var i = 0, count = basicHeaders.length; i < count; i++) {
					if (j == this.groupHeaderIds.length)
						break;
					if (basicHeaders[i].keyName == this.groupHeaderIds[j]) {
						basicHeaders[i].isGroupBy = true;
						this.groupHeaderColIndice.push(i);
						j++;
					}
				}
			}
			if (basicHeaders != null && basicHeaders.length > 0) {
				// 保存header的总体高度
				if (this.isShowHeader) {
					if(this.isHHeader()) {
						this.constant.headerHeight = this.getHeaderDepth()	* (this.headerRowHeight);
					} else {
						this.constant.headerWidth = this.getHeaderDepth() * (this.headerRowWidth);
						this.constant.headerHeight = this.headerRowHeight;
					}
				} else {
					this.constant.headerHeight = 0;
					this.constant.headerWidth = 0;
				}
			} else {
				if (this.model.getHeaders() == null)
					throw new Error("grid must be initialized with headers!");
			}
		},
		/**
		 * 根据key获取header
		 */
		getHeader : function(keyName) {
			var basicHeaders = this.model.getBasicHeaders();
			if (basicHeaders == null)
				return null;
			else {
				for (var i = basicHeaders.length - 1; i >= 0; i--) {
					if (basicHeaders[i].keyName == keyName)
						return basicHeaders[i];
				}
			}
		},
		removeHeader : function(keyName) {
			var header = this.model.removeHeader(keyName);
			return header;
		},
		/**
		 * 判断初始界面是否会出现横向、纵向滚动条
		 */
		adjustScroll : function() {
			var gridWidth = this.constant.outerDivWidth;
			var gridHeight = this.constant.outerDivHeight;
			// 判断初始界面时是否会出现横向滚动条
			var dataRealWidth = this.getDynamicTableDivRealWidth(true)	+ this.getDynamicTableDivRealWidth(false);
			this.scroll = dataRealWidth > gridWidth;
			// 判断初始界面时是否会出现纵向滚动条(修正表头宽度)
			this.vScroll = false;
			var dataRealHeight = gridHeight - this.getRowsNum() * this.rowHeight - this.constant.headerHeight;
			if (this.pageSize > 0)
				dataRealHeight -= $.grid.PAGEBAR_HEIGHT;
			if (dataRealHeight < 0)
				this.vScroll = true;
		},
		/**
		 * 创建分页操作条
		 */
		initPaginationBar : function() {
			var oThis = this;
			this.paginationBar = $("<div>").appendTo(this.outerDiv).addClass('grid_paginationbar').css('width',this.constant.outerDivWidth + "px");
			this.paginationPanel = $("<div>").appendTo(this.paginationBar).addClass('paginationPanel');
			this.paginationContent = $("<div>").appendTo(this.paginationPanel).attr({
				'id' : 'grid_paginationcontent',
				'class' : 'pageinationbgcenter'
			});
			this.paginationMessage = $("<div>").addClass('paginationMessage');
			this.paginationText1 = $("<div>").addClass('paginationText').css('margin-right','5px').html(trans("ml_goto"));
			this.paginationText2 = $("<div>").addClass('paginationText').css({
				'margin-right' : '20px',
				'margin-left' : '5px'
			}).html(trans("ml_goto_page"));
			this.sumRowCountSpan = $("<span>").html(this.model.dataset.getAllRowCount());
			this.paginationText3 = $("<div>").addClass('paginationText').css('margin-right','20px').html(trans("ml_all")+this.sumRowCountSpan+trans("ml_line"));
			this.paginationText4 = $("<div>").addClass('paginationText').css('margin-right','20px').html(trans("ml_pageRowCount") +
				"<span style='color:#0086b2;'>" + this.pageSize + "</span>" + trans("ml_page_line"));
			this.paginationMessage.append(this.paginationText1);
		},
		/**
		 * 创建简单分页操作条
		 */
		initSimplePaginationBar : function() {
			var oThis = this;
			this.paginationBar = $("<div>").appendTo(this.outerDiv).addClass('grid_paginationbar').css('width',this.constant.outerDivWidth + "px");
			this.paginationPanel = $("<div>").appendTo(this.paginationPanel).addClass('paginationPanel');
			this.paginationContent = $("<div>").css({
				'id' : 'grid_paginationcontent',
				'class' : 'pageinationbgcenter'
			});
			this.paginationMessage = $("<div>").addClass('simple_paginationMessage');
			this.paginationText1 = $("<div>").addClass('paginationText').css({
				'margin-right' : '12px',
				'cursor' : 'pointer'
			}).html(trans("ml_pagepre")).on('click',this.pagePre);
			this.paginationText1.data('gridId',this);
			this.sumPageCountSpan =  $("<span>");
			this.paginationText2 = $("<div>").addClass('paginationText').css({
				'margin-left':'0px',
				'margin-right':'12px'
			}).html("/").append(this.sumPageCountSpan);
			this.paginationText4 = $("<div>").addClass('paginationText').css('cursor','pointer').html(trans("ml_pagenext")).on('click',this.pageNext);
			this.paginationText4.data('gridId',this);
			this.paginationMessage.append(this.paginationText1);
			this.paginationInput = $("<div id='paginationInput'></div>").appendTo(this.paginationMessage).integertext({
					left : 0,
					top : 4,
					width : 35,
					position : 'relative'
				}).integertext("instance");
			this.paginationInput.element.attr("class", 'paginationText');
			this.paginationMessage.append(this.paginationText2);
			this.paginationMessage.append(this.paginationText4);
			this.paginationPanel.append(this.paginationMessage);
		},
		/**
		 * 处理后台分页
		 * @param pageIndex 当前页码
		 */
		processServerPagination : function(pageIndex) {
			var pageCount = this.model.getPageCount();
			if (pageIndex < 0 || pageIndex > pageCount - 1)
				return;
			this.processPaginationInfo(pageIndex, this.model.dataset.getAllRowCount(), pageCount, this.pageSize);
			this.model.dataset.setCurrentPage(pageIndex);
		},
		/**
		 * 处理分页信息
		 */
		setPaginationInfo : function() {
			if (this.pageSize > 0) {
				var pageIndex = this.model.dataset.getPageIndex(null);
				// 显示分页信息(后台分页)
				this.processPaginationInfo(pageIndex, this.model.dataset.getAllRowCount(), this.model.getPageCount(),	this.pageSize);
			}
		},
		/**
		 * 根据传入的参数,控制分页按钮状态,显示分页信息
		 * @param pageIndex 页码
		 * @param rowCount 当前页行数,对于前台分页指当前分页的总行数
		 * @param pageCount 页数
		 * @param pageRowCount 每页行数
		 * @param isClient 是否前台分页
		 */
		processPaginationInfo : function(pageIndex, rowCount, pageCount, pageRowCount) {
			if (this.sumRowCountSpan) {
				this.sumRowCountSpan.html(rowCount);
			}
			if (this.sumPageCountSpan) {
				this.sumPageCountSpan.html(pageCount);
			}
			this.pageIndex = pageIndex;
			if (this.paginationBar) {
				if (pageCount <= 1) {
					this.paginationBar.hide();
					return;
				}
			} else {
				return;
			}
			this.paginationBar.show();
			if (this.isSimplePagination) {
				var pagepanelWidth = this.paginationText1.outerWidth()	+ 14 + this.paginationText2.outerWidth() + 14
						+ this.paginationText4.outerWidth()
						+ this.paginationInput.element.outerWidth() + "px"
				this.paginationPanel.css('width',pagepanelWidth);
				this.paginationInput.input.val(pageIndex + 1);
			} else {
				this.paginationContent.empty();
				var preDiv = $("<div class='pre'></div>").on('mouseover',this.preMouseOver).on('mouseout',this.preMouseOut).html(trans("ml_pagepre"));
				var preImgDiv = $("<div class='pre_img'></div>").appendTo(preDiv);
				var preA = $("<a href='#'></a>").appendTo(this.paginationContent).on('click',this.pagePre).data('gridId',this).data('pageIndex',pageIndex);
				preA.append(preDiv);
				var pageFirst = $("<a href='#'></a>").on('click',this.pageFirst).data('gridId',this).data('pageIndex',pageIndex);
				var selectedCenterDiv = $("<div class='pagefirst'></div>").html(trans("ml_pagefirst")).appendTo(pageFirst);
				this.paginationContent.append(pageFirst);
				var classNamePre = "un";
				if (pageCount <= 8) {
					for (var i = 0; i < pageCount; i++) {
						var a = $("<a href='#'></a>").on('click',$.grid.pageNavgate).data('pageIndex',i).data('gridId',this);
						if (i == pageIndex) {
							classNamePre = "";
						} else {
							classNamePre = "un";
						}
						$("<div>").addClass(classNamePre + "selected").html(i + 1).appendTo(a);
						this.paginationContent.append(a);
					}
				} else {
					var beginIndex = 0;
					var endIndex = 0;
					if (pageIndex <= 1) {
						beginIndex = 0;
					} else {
						beginIndex = pageIndex - 2;
					}
					endIndex = beginIndex + 4;
					if (pageIndex >= pageCount - 6) {
						beginIndex = pageCount - 8;
						endIndex = beginIndex + 7;
					}
					if (pageIndex >= pageCount - 2) {
						beginIndex = 0;
						endIndex = beginIndex + 4;
					}
					for (var i = 0; i < pageCount; i++) {
						if ((i >= beginIndex && i <= endIndex) || (i >= pageCount - 2)) {
							var a = $("<a href='#'></a>").on('click',$.grid.pageNavgate).data('pageIndex',i).data('gridId',this);
							if (i == pageIndex) {
								classNamePre = "";
							} else {
								classNamePre = "un";
							}
							$("<div>").addClass(classNamePre + "selected").html(i + 1).appendTo(a);
							this.paginationContent.append(a);
						} else if (i == endIndex + 1) {
							var a = $("<a>");
							$("<div>").addClass('unselected').html("...").appendTo(a);
							this.paginationContent.append(a);
						}
					}
				}
				var pageLast = $("<a href='#'></a>").on('click',this.pageLast).data('gridId',this).data('pageIndex',pageIndex);
				var selectedCenterDiv = $("<div class='pagelast'></div>").html(trans("ml_pagelast")).appendTo(pageLast);
				this.paginationContent.append(pageLast);
				var nextDiv = $("<div class='next'></div>").on('mouseover',this.nextMouseOver).on('mouseout',this.nextMouseOut).html(trans("ml_pagenext"));
				$("<div class='next_img'></div>").appendTo(nextDiv);
				var nextA = $("<a href='#'></a>").on('click',this.pageNext).data('gridId',this).data('pageIndex',pageIndex);
				nextA.append(nextDiv);
				this.paginationContent.append(nextA);
				this.paginationPanel.css('width',(this.paginationContent.outerWidth()+2) + "px");
			}
			this.adjustFixedColumDivHeight();
			return;
		},
		/**
		 * 创建表头区整体div
		 */
		initHeaderDiv : function() {
			var gridWidth = this.constant.outerDivWidth;
			var gridHeight = this.constant.outerDivHeight;
			this.headerDiv = $("<div class='headerbar_div'></div>").appendTo(this.outerDiv);
			// 根据是否显示分页条调整top
			if (this.pageSize > 0 && this.isPagenationTop) {
				this.headerDiv.css("top", $.grid.PAGEBAR_HEIGHT + "px");
			} else {
				this.headerDiv.css("top", "0px");
			}
			// 表头不显示菜单
			this.headerDiv.on('rclick',function(e) {
				e.preventDefault();
				e.stopPropagation();
			});
			if(this.isHHeader()) {
				if (this.vScroll) {
					this.headerDiv.css("width" , (gridWidth - $.grid.SCROLLBAE_HEIGHT) + "px");
					// 记录headerDiv的原始宽度
					this.headerDiv.data('defaultWidth', gridWidth - $.grid.SCROLLBAE_HEIGHT);
				} else {
					this.headerDiv.css("width" , gridWidth + "px");
					// 记录headerDiv的原始宽度
					this.headerDiv.data('defaultWidth' , gridWidth);
				}
				this.headerDiv.css("height", this.constant.headerHeight + "px");
			} else {
				this.headerDiv.css({
					'width' : this.constant.headerWidth + "px",
					'float' : this.headerPosition,
					"height" : gridHeight + "px"
				}).data('defaultWidth' , this.constant.headerWidth);
			}
			this.constant.headerDivWidth = this.headerDiv.data('defaultWidth');
			
			if (!this.isShowHeader)
				this.headerDiv.hide();
		},
		/**
		 * 创建固定表头区整体div
		 */
		initFixedHeaderDiv : function() {
			this.fixedHeaderDivWidth = 0;
			// 得到固定表头的宽度
			var headers = this.model.getHeaders();
			for (var i = headers.length - 1; i >= 0; i--) {
				if (headers[i].isFixedHeader)
					this.fixedHeaderDivWidth += this.getHeaderDefaultWidth(headers[i]);
			}
			// 加上固定选择列的宽度
			if (this.isMultiSelWithBox)
				this.fixedHeaderDivWidth += $.grid.SELECTCOLUM_WIDTH;
			this.fixedHeaderDiv = $("<div>").appendTo(this.headerDiv).css({
						'height' : this.constant.headerHeight + "px",
						'width' : this.fixedHeaderDivWidth + "px",
						'left' : '0px',
						'top' : '0px',
						'position' : 'absolute',
						'z-index' : $.measures.getZIndex()
					});
			// 将固定表头值放入常量对象
			this.constant.fixedHeaderDivWidth = this.fixedHeaderDiv.outerWidth();
		},
		/**
		 * 创建静态表头区行标区表头div
		 */
		initRowNumHeaderDiv : function() {
			this.rowNumHeaderDiv = $("<div>").appendTo(this.fixedHeaderDiv).addClass('row_num_header_div').css('height',this.constant.headerHeight+"px");
			var width = this.rowNumHeaderDiv.outerWidth();
			// 将行号表头区的宽度放入constant常量中,供后续使用
			this.constant.rowNumHeaderDivWidth = width;
			// 如果启用了fixedHeaderDiv则需要改变fixedHeaderDiv的宽度
			this.fixedHeaderDiv.css('width',(width + this.fixedHeaderDivWidth)	+ "px");
			this.constant.fixedHeaderDivWidth = width + this.fixedHeaderDivWidth;
		},
		/**
		 * 创建表格最左侧功能设置触发区
		 */
		initLineStateHeaderDiv : function() {
			this.constant.lineStateHeaderDivWidth = 0;
			return;
		},
		/**
		 * 创建固定表头区固定选择列header
		 */
		initSelectColumHeaderDiv : function() {
			if(!this.isHHeader())
				return;
			var oThis = this;
			this.selectColumHeaderDiv = $("<div>").attr({
					'id' : 'fixedSelectColumHeader',
					'class' : 'select_headerdiv'
				}).css({
					'height' : (this.constant.headerHeight - 1) + "px",
					'left' : (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth) + "px"
				}).appendTo(this.fixedHeaderDiv);
			// 如果多表头,表头高度会超过两倍表头高,这时要计算paddingTop让checkbox聚中显示
			// 多选模式下的全选checkbox
			this.selectAllBox = $("<input type='checkbox'>").appendTo(this.selectColumHeaderDiv).prop("checked",false).on('click',function(e){
				// 置空当前选择单元格
				oThis.selectedCell = null;
				oThis.oldCell = null;
				// 隐藏编辑框
				oThis.hiddenComp();
				// 取消全选
				var indices = [];
				if (!this.checked) {
					for (var i = 0, count = oThis.model.getRowsCount(); i < count; i++) {
						indices.push(i);
					}
					oThis.model.setRowUnSelected(indices);
				}
				// 全选
				else {
					var count = oThis.selectColumDiv.children().length;
					for (var i = count - 1; i >= 0; i--) {
						// 触发每一行checkbox事件
						if (oThis.selectColumDiv.children(':eq('+i+')').children(':first').prop('checked') == false) {
							oThis.selectColumDiv.children(':eq('+i+')').children(':first').trigger('mousedown',e);
						}
					}
					$(this).prop('checked',true);
					for (var i = 0, count = oThis.model.getRowsCount(); i < count; i++) {
						indices.push(i);
					}
					oThis.model.addRowSelected(indices);
				}
				// 取消焦点行
				oThis.loseFocusIndex();
				e.stopPropagation()
			});
			this.selectAllBox.prop('checked',false);
		},
		/**
		 * 创建固定表头区header数据区div
		 */
		initFixedHeaderTableDiv : function() {
			var currLeft = this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth;
			if (this.isMultiSelWithBox)
				currLeft = currLeft + this.selectColumHeaderDiv.outerWidth();
			this.fixedHeaderTableDiv = $("<div>").appendTo(this.fixedHeaderDiv).css({
				'overflow' : 'hidden',
				'top' : '0px',
				'left' : currLeft + "px",
				'height' : this.constant.headerHeight + "px"
			});
			if (this.constant.fixedHeaderDivWidth - currLeft > 0) {
				this.fixedHeaderTableDiv.css("width", (this.constant.fixedHeaderDivWidth - currLeft) + "px");
			}
			// 将固定表头区header数据区的宽度放入常量对象
			this.constant.fixedHeaderTableDivWidth = this.constant.fixedHeaderDivWidth	- currLeft;
		},
		/**
		 * 创建动态表头区整体div
		 */
		initDynamicHeaderDiv : function() {
			var gridWidth = this.constant.outerDivWidth;
			var gridHeight = this.constant.outerDivHeight;
			var fixedHeaderWidth = this.constant.fixedHeaderDivWidth;
			var fixedHeaderHeight = this.constant.fixedHeaderDivHeight || 0;
			this.dynamicHeaderDiv = $("<div>").css({
				'position' : 'absolute',
				'top' : fixedHeaderHeight + 'px',
				'left' : fixedHeaderWidth + "px",
				'height' : this.constant.headerHeight + "px"
			}).appendTo(this.headerDiv);
			if(this.isHHeader()) {
				if (this.vScroll) {
					var dynHeaderWidth = gridWidth - fixedHeaderWidth - $.grid.SCROLLBAE_HEIGHT;
					if (dynHeaderWidth > 0) {
						this.dynamicHeaderDiv.css("width", dynHeaderWidth + "px").data('defaultWidth',dynHeaderWidth);
					} else {
						this.dynamicHeaderDiv.css("width", "0px").data('defaultWidth',dynHeaderWidth);
					}
				} else {
					var dynHeaderWidth = gridWidth - fixedHeaderWidth;
					if (dynHeaderWidth > 0) {
						this.dynamicHeaderDiv.css("width", (gridWidth - fixedHeaderWidth) + "px").data('defaultWidth',gridWidth - fixedHeaderWidth);
					} else {
						this.dynamicHeaderDiv.css("width", "0px").data('defaultWidth',dynHeaderWidth);
					}
				}
			} else {
				this.dynamicHeaderDiv.css("width", this.constant.headerWidth + "px").data('defaultWidth',this.constant.headerWidth);
			}
		},
		/**
		 * 创建动态表头区header数据区div
		 */
		initDynamicHeaderTableDiv : function() {
			// 包装tables的包容div
			if(this.isHHeader()) {
				this.dynamicHeaderTableDiv = $("<div>").css({
					'left' : '0px',
					'height' : this.constant.headerHeight + "px",
					'width' : this.getDynamicTableDivRealWidth(true) + "px"
				}).appendTo(this.dynamicHeaderDiv);
			} else {
				this.dynamicHeaderTableDiv = $("<div>").css({
					'left' : '0px',
					'height' : this.getDynamicTableDivRealHeight(true) + "px",
					'width' : this.constant.headerWidth + "px"
				}).appendTo(this.dynamicHeaderDiv);
			}
			
		},
		/**
		 * 根据生成的headers model画固定和动态header内的table
		 */
		initHeaderTables : function() {
			// 表头的总深度
			this.headerDepth = this.getHeaderDepth();
			// 将动态表头和固定表头放在以下两个数组中
			this.defaultFixedHeaders = [];
			this.defaultDynamicHeaders = [];
			var headers = this.model.getHeaders();
			for (var i = 0, count = headers.length; i < count; i++) {
				if (headers[i].isFixedHeader)
					this.defaultFixedHeaders.push(headers[i]);
				else if (headers[i].isFixedHeader == false)
					this.defaultDynamicHeaders.push(headers[i]);
			}
			// 目前对于多表头先不允许拖动
			for (var i = 0, count = headers.length; i < count; i++)
				this.initHeaderTable(headers[i]);
		},
		getFirstVisibleHeader : function() {
			var headers = this.model.getHeaders();
			for (var i = 0; i < headers.length; i++) {
				if (headers[i].isHidden == false)
					return headers[i];
			}
			return null;
		},
		getLastVisibleHeader : function() {
			var headers = this.model.getHeaders();
			for (var i = headers.length - 1; i >= 0; i--) {
				if (headers[i].isHidden == false)
					return headers[i];
			}
			return null;
		},
		/**
		 * 得到动态表头的第一个不隐藏的header
		 */
		getFirstDynamicVisibleHeader : function() {
			if (this.defaultDynamicHeaders == null	|| this.defaultDynamicHeaders.length == 0)
				return null;
			for (var i = 0, count = this.defaultDynamicHeaders.length; i < count; i++) {
				if (this.defaultDynamicHeaders[i].isHidden == false)
					return this.defaultDynamicHeaders[i];
			}
		},
		/**
		 * 得到动态表头的最后一个不隐藏的header
		 */
		getLastDynamicVisibleHeader : function() {
			if (this.defaultDynamicHeaders == null || this.defaultDynamicHeaders.length == 0)
				return null;
			for (var i = this.defaultDynamicHeaders.length - 1; i >= 0; i--) {
				if (this.defaultDynamicHeaders[i].isHidden == false)
					return this.defaultDynamicHeaders[i];
			}
		},
		/**
		 * 得到固定表头的第一个不隐藏的header
		 */
		getFirstFixedVisibleHeader : function() {
			if (this.defaultFixedHeaders == null || this.defaultFixedHeaders.length == 0)
				return null;
			for (var i = 0, count = this.defaultFixedHeaders.length; i < count; i++) {
				if (this.defaultFixedHeaders[i].isHidden == false)
					return this.defaultFixedHeaders[i];
			}
		},
		/**
		 * 得到固定表头的最后一个不隐藏的header
		 */
		getLastFixedVisibleHeader : function() {
			if (this.defaultFixedHeaders == null || this.defaultFixedHeaders.length == 0)
				return null;
			for (var i = this.defaultFixedHeaders.length - 1; i >= 0; i--) {
				if (this.defaultFixedHeaders[i].isHidden == false)
					return this.defaultFixedHeaders[i];
			}
		},
		/**
		 * 得到指定header的下一个显示的basic header
		 */
		getNextVisibleBasicHeader : function(header) {
			if (this.basicHeaders == null)
				throw new Error("basicHeaders为null!");
			if (header == null)
				return null;
			for (var i = 0; i < this.basicHeaders.length; i++) {
				if (this.basicHeaders[i] == header	&& this.basicHeaders[i + 1] != null) {
					for (var j = i + 1; j < this.basicHeaders.length; j++) {
						if (this.basicHeaders[j].isHidden == false)
							return this.basicHeaders[j];
					}
				}
			}
			return null;
		},
		/**
		 * 得到指定header的上一个显示的basic header
		 */
		getLastVisibleBasicHeader : function(header) {
			if (this.basicHeaders == null)
				throw new Error("basicHeaders为null!");
			if (header == null)
				return null;
			for (var i = 0; i < this.basicHeaders.length; i++) {
				if (this.basicHeaders[i] == header) {
					for (var j = i - 1; j >= 0; j--) {
						if (this.basicHeaders[j].isHidden == false)
							return this.basicHeaders[j];
					}
				}
			}
			return null;
		},
		/**
		 * 为boolean列创建全选checkbox(提出此方法主要为了代码复用)
		 */
		createCheckBoxForSelAll : function(header) {
			var oThis = this;
			var selectBox = $("<input type='checkbox'>").css({
				'vertical-align' : 'middle',
				'margin-top' : '0px'
			}).prop('checked',false);
			// 设置checkbox的禁用状态
			if (this.editable == false || header.columEditable == false)
				selectBox.prop('disabled',true);
			selectBox.on({
				'mousedown' : function(e) {
					// 置空当前选择单元格
					oThis.selectedCell = null;
					oThis.oldCell = null;
					// 隐藏编辑框
					oThis.hiddenComp();
					var ds = oThis.model.dataset;
					var colIndex = ds.nameToIndex(header.keyName);
					var dsRows = ds.getRows(null);
					if (header.valuePair == null || header.valuePair[1] == null)
						return;
					// 取消全选
					if ($(this).prop('checked')) {
						if (dsRows != null) {
							var rowIndices = [],values = [];
							for (var i = 0; i < dsRows.length; i++) {
								rowIndices.push(i);
								values.push(header.valuePair[1]);
							}
							ds.setValuesAt(rowIndices, colIndex, values);
						}
						$(this).prop('checked',false);
					}
					// 全选
					else {
						if (dsRows != null) {
							var rowIndices = [],values = [];
							for (var i = 0; i < dsRows.length; i++) {
								rowIndices.push(i);
								values.push(header.valuePair[0]);
							}
							ds.setValuesAt(rowIndices, colIndex, values);
						}
						$(this).prop('checked',true);
					}
				},
				'click' : function(e) {
					e.preventDefault();
					e.stopPropagation();
				}
			});
			return selectBox;
		},
		isHHeader : function() {
			return this.headerPosition == 'top' || this.headerPosition == 'bottom';
		},
		/**
		 * 绘制每个header内的table
		 * @param header this.headers数组中的header
		 * @param isLastHeader 是否最后一个header
		 */
		initHeaderTable : function(header) {
			// 隐藏列不创建
			if (header.isHidden)
				return;
			var oThis = this;
			var tempDiv = null;
			// 得到header的总深度
			var totalDepth = header.getDepth();
			// 如果为多表头，且 header总深度小于2，说明 多表头的子全部隐藏了，不创建此多表头
			if (header.children != null && totalDepth < 2)
				return;
			// 得到此header的总宽度作为table的宽度
			var tableWidth = this.getHeaderDefaultWidth(header);
			var tableHeight = this.getHeaderDefaultHeight(header);
			// 创建table
			if(!this.isHHeader() && header.children != null) {
				header.dataTable = $("<div>").data('headerOwner',header);
			} else {
				header.dataTable = $("<table>").data('headerOwner',header);
			}
			var headerTable = header.dataTable;
			if (!header.isGroupHeader && this.isHHeader()) {
				// 处理表头拖动
				header.dataTable.data('refGrid',this).on({
					'mousedown' : $.grid.grid_init,
					'mouseup' : $.grid.grid_end,
					'mousemove' : $.grid.grid_drag
				});
				//this.headerDiv.onmouseup = $.grid.grid_end;
				// 存储这两个引用直接供grid_drag内部判断鼠标的位置区域时使用
				header.dataTable.data('refHeader',header);
				//this.headerDiv.onmousemove = $.grid.grid_drag;
			}
			// 根据header是固定还是动态,将header添加到不同的div中
			if (!header.isFixedHeader)
				this.dynamicHeaderTableDiv.append(header.dataTable);
			else
				this.fixedHeaderTableDiv.append(header.dataTable);
			if(!header.dataTable.is('div')) {
				header.dataTable.attr({
					'cellspacing' : '0px',
					'cellpadding' : '0px'
				});
			}
			header.dataTable.css({
				'height' : this.isHHeader() ? (this.constant.headerHeight + "px") : ((tableHeight-1) + "px"),
				'width' : this.isHHeader() ? (tableWidth + "px") : (this.constant.headerWidth)
			});
			// 创建tbody
			var oTBody = null;
			if(!header.dataTable.is('div')) {
				oTBody = $("<tbody>").appendTo(header.dataTable);
			}
			// 单表头情况
			if (header.children == null) {
				headerTable.attr('class','headerdiv');
				// 设置表头颜色
				if (header.required)
					headerTable.addClass('header_required');
				else {
					// 根据header中的textcolor属性设置文字颜色
					if (header.textColor != null && header.textColor != "")
						headerTable.css('color',header.textColor);
				}
				var row = this.addTableRow(oTBody, null);
				var cell = $('<td>').appendTo(row).attr('width',this.isHHeader() ? header.width : this.constant.headerWidth);
				// 保存cell的引用在
				header.cell = cell;
				var selectBox = null;
				// 如果是默认的boolean渲染类型,或者编辑类型为$.editortype.CHECKBOX,则创建全选checkbox
				if (header.renderType == BooleanRender
						|| header.editorType == $.editortype.CHECKBOX
						|| (header.dataType == $.datatype.FBOOLEAN && header.editorType != $.editortype.STRINGTEXT)) {
					if (header.isShowCheckBox)
						selectBox = this.createCheckBoxForSelAll(header);
				}
				// 将header的引用绑定在cell上
				cell.data('owner',header);
				tempDiv = $("<div>").addClass('tempDiv');
				if(this.isHHeader()) {
					if (header.width && header.width > 0) {
						tempDiv.css('width',header.width - 1 + "px");
					}	
				} else {
					tempDiv.css('width',this.constant.headerWidth - 1 + "px");
				}
				if(!this.isHHeader()) {
					cell.attr('height',this.headerRowHeight - 2);
				}
				cell.append(tempDiv);
				// 将放置内容的div引用绑定header上
				header.contentDiv = tempDiv;
				// 在header上保存grid的引用
				header.owner = this;
				header.textNode = $("<div>").attr({
					'id' : this.id + "_" + header.keyName,
					'title' : header.showName
				}).css({
					'white-space' : 'nowrap',
					'overflow' : 'hidden',
					'text-overflow' : 'ellipsis'
				});
				var widgetid;
				try {
					widgetid = ((header.owner.viewpart) ? header.owner.viewpart.id : "");
				} catch (e) {
					widgetid = "";
				}
				var params = {
					uiid : "",
					eleid : this.id,
					type : "grid_header",
					widgetid : widgetid,
					subeleid : header.id
				};
				if (window.editMode) {
					$.design.getObj({
						divObj : header.textNode,
						params : params,
						objType : $.design.Constant.COMPOMENT_TYPE
					});
				}
				tempDiv.append(header.textNode);
				if (selectBox != null) {
					// 只有header的高度为2倍表头高度以上才设置此时padding
					header.selectBox = selectBox;
					header.textNode.append(selectBox);
				}
				if (typeof(header.renderType) != "undefined" && header.renderType != null && typeof(header.renderType.headerRender) == "function") {
					header.renderType.headerRender.call(this, header.textNode, header.showName);
				} else {
					header.textNode.html(header.showName);
				}
				// 显示"排序箭头",并排序,按着ctrl点击表头支持多列排序
				if (this.sortable && header.sortable && this.model.treeLevel == null) {
					tempDiv.on({
						'click' : function(e){
							// tempdiv相对body的left
							var offsetLeft = $(this).offset().left;
							// 当前鼠标的位置
							var currX = e.pageX + $('body').scrollLeft();
							if (currX > offsetLeft + 15 && currX < offsetLeft + $(this).outerWidth() - 15) {
								// 未按ctrl键
								if (!e.ctrlKey) {
									if (oThis.sortHeaders != null) {
										var headerDiv = null;
										for (var i = 0, count = oThis.sortHeaders.length; i < count; i++) {
											headerDiv = oThis.sortHeaders[i].contentDiv;
											headerDiv.find('.sort_img').remove();
										}
										// 清除多列排序记录数组
										while (oThis.sortHeaders.length != 0) {
											oThis.sortHeaders.shift().contentDiv.find('.sort_img').remove();
										}
									}
									// 如果当前排序header不为空,且当前排序header不为现在点击的header,则先将当前排序header的图标隐藏
									if (oThis.sortHeader != null && oThis.sortHeader != header) {
										var lastHeaderDiv = oThis.sortHeader.contentDiv;
										lastHeaderDiv.find('.sort_img').remove();
									}
								}
								// 按着ctrl键
								else {
									// 如果有上次单击的排序列,首先清除上次的单击排序列图标
									if (oThis.sortHeader != null && oThis.sortHeader != header) {
										var lastHeaderDiv = oThis.sortHeader.contentDiv;
										lastHeaderDiv.find('.sort_img').remove();
										oThis.sortHeader = null;
									}
								}
								var sortImg = $(this).find('.sort_img');
								if (sortImg.size() ==0) {
									sortImg = $("<img>").appendTo(tempDiv).addClass('sort_img').attr('src',window.themePath + "/comps/grid/images/up_arrow.png");
									// 记录当前的排序方向
									header.ascending = -1;
								} else {
									if (header.ascending == -1) {
										sortImg.attr('src',window.themePath + "/comps/grid/images/down_arrow.png");
										header.ascending = 1;
									} else if (header.ascending == 1) {
										sortImg.attr('src',window.themePath + "/comps/grid/images/up_arrow.png");
										header.ascending = -1;
									}
								}
								// 未按ctrl键
								if (!e.ctrlKey) {
									oThis.model.sortable([header], null, null);
									// 记录当前排序列
									oThis.sortHeader = header;
								}
								// 按着ctrl键
								else {
									// 记录需要排序的多列表头,按点击顺序记录
									if (oThis.sortHeaders == null)
										oThis.sortHeaders = [];
									oThis.sortHeaders.push(header);
								}
							}
							e.preventDefault();
							e.stopPropagation();
						},
						'keyup' : function(e) {}
					});
				}
			}
			// 多表头情况
			else {
				headerTable.attr('class','multiheaderdiv');
				if(this.isHHeader()) {
					/* colspan,rowspan计算公式 */
					var tempHeaders = [];
					// totalDepth即为行数
					for (var i = 0; i < totalDepth; i++) {
						var row = $("<tr>").appendTo(oTBody);
						// 得到这一行内的所有列(不包括隐藏列)
						tempHeaders = header.getVisibleHeadersByLevel(i);
						for (var j = 0; j < tempHeaders.length; j++) {
							// 判断是否隐藏
							var tempHeader = tempHeaders[j];
							var cell = $("<td>").appendTo(row).addClass('multiheadercell').attr({
								'rowSpan' : tempHeader.getRowspan(totalDepth),
								'colSpan' : tempHeader.getColspan()
							});
							cell.data('owner',tempHeader);
							var selectBox = null;
							var tempDiv = $("<div>").addClass('tempDiv');
							if (tempHeader.children == null) {
								var headerLevel = tempHeader.getHeaderLevel();
								// 不是多表头中最底层的header
								if (headerLevel != totalDepth - 1)
									cell.attr('height',(this.headerDepth - tempHeader.getHeaderLevel()) * this.headerRowHeight);
								else
									cell.attr('height',this.headerRowHeight - 1);
								
								// 如果是默认的boolean渲染类型,则创建全选checkbox(注:对于用户自己写的boolean渲染器不会创建全选checkbox)
								if (tempHeader.renderType == BooleanRender
										|| tempHeader.editorType == $.editortype.CHECKBOX
										|| (tempHeader.dataType == $.datatype.FBOOLEAN && tempHeader.editorType != $.editortype.STRINGTEXT)) {
									if (tempHeader.isShowCheckBox)
										selectBox = this.createCheckBoxForSelAll(tempHeader);
								}
							} else {
								if (tempHeader == header)
									cell.attr('height',(this.headerDepth - header.getHeaderChildrenLevel()) * this.headerRowHeight);
								else
									cell.attr('height',this.headerRowHeight - 1);
							}
							if (j != 0)
								cell.attr('width',tempHeader.width - 1);
							else
								cell.attr('width',tempHeader.width);
							// 将放置内容的div引用绑定header上
							tempHeader.contentDiv = tempDiv;
							// 在header上保存grid的引用
							tempHeader.owner = this;
							tempHeader.textNode = $("<div>").attr({
								'id' : this.id + "_" + header.keyName,
								'title' : tempHeader.showName
							}).css({
								'white-space' : 'nowrap',
								'overflow' : 'hidden',
								'text-overflow' : 'ellipsis'
							}).appendTo(tempDiv);
							
							if (selectBox != null) {
								header.selectBox = selectBox;
								tempHeader.textNode.append(selectBox);
							}
							if (tempHeader.renderType && typeof(tempHeader.renderType.headerRender) == "function") {
								tempHeader.renderType.headerRender.call(this, tempHeader.textNode, tempHeader.showName);
							} else {
								tempHeader.textNode.html(tempHeader.showName);
							}
							cell.append(tempDiv);
						}
					}
				} else {
					// totalDepth即为行数
					header.dataTable.css('position','relative');
					for (var i = 0; i < totalDepth; i++) {
						// 得到这一行内的所有列(不包括隐藏列)
						var tmpTop = 0;
						tempHeaders = header.getVisibleHeadersByLevel(i);
						for (var j = 0; j < tempHeaders.length; j++) {
							// 判断是否隐藏
							var tempHeader = tempHeaders[j];
							
							var tmpLeft = 0;
							var tmpH = tempHeader.parent;
							while(tmpH) {
								tmpLeft += (tmpH.tmpCellWidth || 0);
								tmpH = tmpH.parent;
							}
							
							var cell = $("<div>").appendTo(header.dataTable).addClass('multiheadercell').data('owner',tempHeader).css({
								'height' : tempHeader.height * tempHeader.getColspan() + "px",
								'width' : (this.headerRowWidth - 1) + "px",
								'position' : 'absolute',
								'left' : tmpLeft + "px",
								'top' : tmpTop + "px"
							});
							
							if (tempHeader.children == null) {
								var headerLevel = tempHeader.getHeaderLevel();
								// 不是多表头中最底层的header
								if (headerLevel != totalDepth - 1)
									cell.css('width',((this.headerDepth - tempHeader.getHeaderLevel()) * this.headerRowWidth-1) + "px");
								else
									cell.css('width',(this.headerRowWidth - 1) + "px");
								
							} else {
								if (tempHeader == header)
									cell.css('width',((this.headerDepth - header.getHeaderChildrenLevel()) * this.headerRowWidth-1) + "px");
								else
									cell.css('width',(this.headerRowWidth - 1) + "px");
							}
							
							var tempDiv = $("<div>").addClass('tempDiv');
							// 将放置内容的div引用绑定header上
							tempHeader.contentDiv = tempDiv;
							// 在header上保存grid的引用
							tempHeader.owner = this;
							tempHeader.textNode = $("<div>").attr({
								'id' : this.id + "_" + header.keyName,
								'title' : tempHeader.showName
							}).css({
								'line-height' : tempHeader.height * tempHeader.getColspan() + "px",
								'white-space' : 'nowrap',
								'overflow' : 'hidden',
								'text-overflow' : 'ellipsis'
							}).appendTo(tempDiv);
							
							if(j==0) {
								cell.css('height',cell.outerHeight()-1+"px");
								tempHeader.textNode.css('line-height',cell.outerHeight()-1+"px");
							}
							
							if (tempHeader.renderType && typeof(tempHeader.renderType.headerRender) == "function") {
								tempHeader.renderType.headerRender.call(this, tempHeader.textNode, tempHeader.showName);
							} else {
								tempHeader.textNode.html(tempHeader.showName);
							}
							cell.append(tempDiv);
							
							tmpTop += cell.outerHeight();
							tempHeader.tmpCellWidth = cell.outerWidth();
						}
					}
				}
				tempHeaders = null;
			}
			// 固定列的设置top,right边界
			if (header.isFixedHeader) {
				headerTable.addClass('fixedheaderdiv');
			}
			// 列的第一个header不设置left边界(包括fixed和dynamic) 这里需要判断未隐藏的固定列是否>0
			if (header == this.getFirstFixedVisibleHeader() || header == this.getFirstDynamicVisibleHeader()) {
				headerTable.css('border-left-width','0px');
			}
			// headers最后一个去掉右边界
			if ((header == this.getLastFixedVisibleHeader() || header == this.getLastDynamicVisibleHeader()) && this.isHHeader()) {
				tempDiv.css('border-right-width','0px');
				headerTable.css('border-right-width','0px');
			}
		},
		/**
		 * 创建信息提示区
		 */
		initNoRowsDiv : function() {
			this.noRowsDiv = $("<div>").appendTo(this.outerDiv).css({
				'position' : 'relative',
				'margin-top' : '10px',
				'margin-bottom' : '10px',
				'text-align' : 'center'
			}).hide().html(trans("ml_grid_norow"));
		},
		/**
		 * 设置grid提示信息
		 */
		setGridTipContent : function(html) {
			this.noRowsDiv.html(html).show();
			this.dynamicColumDataDiv.css('margin-bottom','0px');
			this.dataOuterDiv.css('overflow','hidden').hide();
		},
		/**
		 * 创建数据区整体div
		 */
		initDataOuterDiv : function() {
			this.dataOuterDiv = $("<div>").appendTo(this.outerDiv).addClass('data_outer_div').css('z-index','101');
			var fixedColumDivWidth = 0;
			if (this.fixedColumDiv)
				fixedColumDivWidth = this.fixedColumDiv.outerWidth();
			if(this.isHHeader()) {
				this.dataOuterDiv.css({
					'width' : this.constant.outerDivWidth - fixedColumDivWidth + "px",
					'left' : this.constant.fixedColumDivWidth + "px"
				});
			} else {
				this.dataOuterDiv.css({
					'width' : this.constant.outerDivWidth - this.constant.headerWidth + "px",
					'left' : '0px'
				});
			}
			
			if (this.canCopy == false) {
//				document.body.onselectstart = function(e) {
//					return false;
//				};
//				document.body.ondragstart = function(e) {
//					return false;
//				};
//				this.dataOuterDiv.style.MozUserSelect = "none";
			}
		},
		/**
		 * 创建固定列整体div
		 */
		initFixedColumDiv : function() {
			var h = this.constant.headerHeight;
			this.fixedColumDiv = $("<div>").appendTo(this.outerDiv).attr({
				'id' : 'fixedColum',
				'class' : 'fixedcolum_div'
			}).css('width',this.constant.fixedHeaderDivWidth + "px");
			// 调整高度,paintRows的时候调用，初始化的时候不用调用
			// 将固定列的宽度放入常量对象
			this.constant.fixedColumDivWidth = this.constant.fixedHeaderDivWidth;
		},
		/**
		 * 调整fixedColumDiv的高度,当滚动条从有到无或者从无到有时均需要调整此高度
		 */
		adjustFixedColumDivHeight : function() {
			if(this.isHHeader()) {
				var oH = this.constant.outerDivHeight, h = this.constant.headerHeight;
				// 调整top值
				if (this.pageSize > 0 && this.isPagenationTop == true)
					this.fixedColumDiv.css('top',(h + $.grid.PAGEBAR_HEIGHT) + "px");
				else
					this.fixedColumDiv.css('top',h + "px");
				if (!this.flowmode) {
					var height = this.wholeDiv.outerHeight();
					if (this.descDiv)
						height = height - this.descDiv.outerHeight();
					if (this.headerBtnDiv && this.headerBtnDiv.is(":visible"))
						height = height - this.headerBtnDiv.outerHeight();
					if (!this.paginationBar || !this.paginationBar.is(":visible") || this.pageSize == -1) {
						height = height - this.constant.headerHeight;
					} else {
						height = height - this.constant.headerHeight - $.grid.PAGEBAR_HEIGHT;
					}
					if (height > 0) {
						this.fixedColumDiv.css('height',height + "px");
						this.dataOuterDiv.css('height',height + "px");
					} else {
						this.fixedColumDiv.css('height','100%');
						this.dataOuterDiv.css('height','100%');
					}
				}
			}
		},
		/**
		 * 画行数字行标div
		 */
		initRowNumDiv : function() {
			this.rowNumDiv = $("<div>").appendTo(this.fixedColumDiv).attr({
				'id' : 'rowNumDiv',
				'class' : 'num_div'
			}).css('width',this.constant.rowNumHeaderDivWidth + "px");
			// 保存此div所有子div的引用
			this.rowNumDiv.data('cells',[]);
		},
		/**
		 * 创建行状态显示列
		 */
		initLineStateColumDiv : function() {
			this.lineStateColumDiv = $("<div>").appendTo(this.fixedColumDiv).attr({
				'id' : 'lineStateColumDiv',
				'class' : 'state_div'
			}).css('top','0px');
			// 显示合计行,要减小行状态列的高度，使用offsetHeight，待验证
			if (this.isShowSumRow) {
				if (this.fixedColumDiv.outerHeight() > 0) {
					this.lineStateColumDiv.css("height", (this.fixedColumDiv.outerHeight() - $.grid.ROW_HEIGHT)+ "px");
					this.lineStateColumDiv.data('defaultHeight',this.fixedColumDiv.outerHeight() - $.grid.ROW_HEIGHT);
				}
			}
			// 设置left
			if (this.isShowNumCol) {
				this.lineStateColumDiv.css("left", this.constant.rowNumHeaderDivWidth + "px");
			} else {
				this.lineStateColumDiv.css("left", "0px");
			}
			this.lineStateColumDiv.css("width",	this.constant.lineStateHeaderDivWidth + "px");
			// 保存此div所有子div的引用
			this.lineStateColumDiv.data('cells',[]);
		},
		/**
		 * 画合计行在固定列区域的部分
		 */
		initSumRowDiv : function() {
			this.sumRowDiv = $("<div>").appendTo(this.dynSumRowContentDiv).attr({
				'id' : 'sumRowDiv',
				'class' : 'sum_div'
			}).css({
				'left' : '0px',
				'top' : '0px',
				'height' : $.grid.ROW_HEIGHT + "px",
				'line-height' : $.grid.ROW_HEIGHT + "px",
				'width' : (this.constant.rowNumHeaderDivWidth + 40) + "px"
			}).html("<center>" + trans("ml_total") + "</center>");
		},
		/**
		 * 画合计行div,此div内放置合计行的每个cell
		 */
		initSumPageDataDiv : function() {
			// 创建放置动态数据区"合计"行的div,该div的overflow设置为hidden
			this.dynSumRowContentDiv = $("<div class='dynsumcontainer_div'></div>").appendTo(this.dataOuterDiv);
			this.initSumRowDiv();
			if (this.dynamicHeaderDiv.data('defaultWidth') - 2 > 0) {
				this.dynSumRowContentDiv.css("width", (this.dynamicHeaderDiv.data('defaultWidth') - 2) + "px");
			} else {
				this.dynSumRowContentDiv.css("width", "0px");
			}
			this.dynSumRowContentDiv.css({
				'height' : $.grid.ROW_HEIGHT + "px",
				'left' : this.fixedColumDiv.outerWidth() + "px"
			});
			this.dynSumRowContentDiv.data('defaultLeft',this.fixedColumDiv.outerWidth() + "px");
			// 创建动态数据区"合计"行
			this.dynSumPageDataDiv = $("<div>").appendTo(this.dynSumRowContentDiv).attr({
				'id' : 'dynsumrow_div',
				'class' : 'dynsumrow_div'
			}).css({
				'top' : '0px',
				'left' : '40px',
				'height' : '100%',
				'width' : (this.dynamicHeaderTableDiv.outerWidth() - this.sumRowDiv.outerWidth()) + "px"
			});
		},
		/**
		 * 画合计行中的每个单元格
		 */
		initSumRowCells : function() {
			var firstVisibleHeader = this.getFirstVisibleHeader();
			for (var i = 0, count = this.defaultDynamicHeaders.length; i < count; i++) {
				if (!this.defaultDynamicHeaders[i].isHidden && !this.defaultDynamicHeaders[i].isGroupHeader) {
					if (firstVisibleHeader != null	&& firstVisibleHeader.keyName == this.defaultDynamicHeaders[i].keyName)
						this.createSumRowCell(this.defaultDynamicHeaders[i],true);
					else
						this.createSumRowCell(this.defaultDynamicHeaders[i],false);
				} else if (!this.defaultDynamicHeaders[i].isHidden && this.defaultDynamicHeaders[i].isGroupHeader) {
					for (var j = 0; j < this.defaultDynamicHeaders[i].basicHeaders.length; j++) {
						if (!this.defaultDynamicHeaders[i].basicHeaders[j].isHidden && !this.defaultDynamicHeaders[i].isGroupHeader) {
							if (firstVisibleHeader != null && firstVisibleHeader.keyName == this.defaultDynamicHeaders[i].basicHeaders[j].keyName)
								this.createSumRowCell(this.defaultDynamicHeaders[i].basicHeaders[j],true);
							else
								this.createSumRowCell(this.defaultDynamicHeaders[i].basicHeaders[j],false);
						}
					}
				}
			}
		},
		createSumRowCell : function(header, isFirstHeader) {
			var cell = $("<div>").appendTo(this.dynSumPageDataDiv).addClass('dynsumcell_div').css({
				'height' : $.grid.ROW_HEIGHT + "px",
				'line-height' : $.grid.ROW_HEIGHT + "px"
			});
			cell.data('headKey',header.keyName);
			// 表头保存sumcell的引用
			header.sumCell = cell;
			var extendWidth = 0;
			if (isFirstHeader) {
				extendWidth = this.constant.rowNumHeaderDivWidth + 40;
			}
			if (header.dataDivWidth != null && header.dataDivWidth > 0) {
				cell.css('width',(header.dataDivWidth - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - extendWidth) + "px");
			} else if (header.dataDiv && header.dataDiv.outerWidth() > 0) {
				cell.css('width',(header.dataDiv.outerWidth() - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - extendWidth) + "px");
			} else if (header.width && header.width > 0) {
				cell.css('width',(header.width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - extendWidth) + "px");
			}
			cell.css({
				'padding-left' : $.grid.CELL_LEFT_PADDING + "px",
				'padding-right' : "10px"
			});
			/*
			 * 标题中对齐 文本类、参照类、枚举类的左对齐 数字类的右对齐 日期/布尔类的居中
			 */
			if (typeof(header.dataType) == "string") {
				switch (header.dataType) {
					case "FDateTime" :
					case "FDate" :
					case "FTime" :
					case "Date" :
					case "ShortDate" :
					case "Boolean" :
					case "boolean" :
					case "FBoolean" :
						cell.css('text-align','center');
						break;
					case "Integer" :
						if (header.editorType && header.editorType == "ComboBox") {
							// 下拉框当做文本类处理
							cell.css('text-align','left');
							break;
						}
					case "int" :
					case "Double" :
					case "double" :
					case "FDouble" :
					case "Float" :
					case "float" :
					case "BigDecimal" :
					case "Decimal" :
					case "Long" :
					case "long" :
						cell.css('text-align','right');
						break;
					default :
						cell.css('text-align','left');
						break;
				}
			} else {
				// 根据header中textAlign属性设置文字在cell中的位置.
				cell.css('text-align',header.textAlign);
			}
		},
		/**
		 * 创建固定选择列
		 */
		initSelectColumDiv : function() {
			this.selectColumDiv = $("<div>").appendTo(this.fixedColumDiv).attr({
				'id' : 'fixedSelectColum',
				'class' : 'fixed_select_colum'
			}).css({
				'width' : $.grid.SELECTCOLUM_WIDTH + "px",
				'left' : (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth) + "px"
			});
			// 将固定表头引用保存在固定选择列的header属性中
			this.selectColumDiv.data('header',this.selectColumHeaderDiv);
			// 当前model中的行数
			var rowNum = this.getRowsNum();
			// 存放所有cells索引的数组
			this.selectColumDiv.data('cells',[]);
			if (!this.flowmode) {
				this.selectColumDiv.css("position", "absolute");
			}
		},
		/**
		 * 画动态列数据区div
		 */
		initDynamicColumDataDiv : function() {
			var iOffsetWidth = this.constant.fixedColumDivWidth;
			var rowsNum = this.getRowsNum(),oThis = this;
			if (this.noRowsDiv) {
				if (rowsNum <= 0) {
					this.needShowNoRowsDiv = true;
					if (this.model.dataset.lazyLoad) {// 懒加载无数据提示晚于loading动画
						setTimeout(function(){
							$.grid.showNoRowsDiv(oThis);
						}, 1500);
					} else {
						setTimeout(function(){
							$.grid.showNoRowsDiv(oThis);
						}, 500);
					}
				} else {
					this.needShowNoRowsDiv = false;
					this.noRowsDiv.hide();
				}
			}
			// 保存数据区真正的宽度值,动态表格区真正的宽度值
			var dynamicDataDivRealWidth = this.getDynamicTableDivRealWidth(true);
			this.dynamicColumDataDiv = $("<div>").attr({
				'id' : 'dynamicDataDiv',
				'class' : 'dynamic_data_div'
			}).css('width',(this.isHHeader() ? (dynamicDataDivRealWidth + 2) : this.dataOuterDiv.outerWidth()) + "px");
			if (this.dataOuterDiv.children(':eq(0)').size() > 0)
				this.dynamicColumDataDiv.prependTo(this.dataOuterDiv);
			else
				this.dataOuterDiv.append(this.dynamicColumDataDiv);
			this.realWidth = dynamicDataDivRealWidth + iOffsetWidth;
			if (this.dynSumRowContentDiv) {
				this.dynamicColumDataDiv.css("width", (dynamicDataDivRealWidth + 2) + "px");
			}
			if (rowsNum > 0) {
				this.dynamicColumDataDiv.css({
					"margin-bottom" : "17px",
					"overflow" : "auto",
					"display" : "block"
				});
			} else {
				this.dynamicColumDataDiv.css('margin-bottom','0px');
				this.dataOuterDiv.css('overflow','hidden').hide();
			}
			if (this.defaultDynamicHeaders != null) {
				var len = this.defaultDynamicHeaders.length;
				var rowNum = this.getRowsNum();
				for (var i = 0; i < len; i++) {
					var tempH = this.defaultDynamicHeaders[i];
					// 单表头情况
					if (tempH.basicHeaders == null && tempH.isHidden == false) {
						tempH.dataDiv = $("<div>").css({
							'width' : (tempH.width - 1) + "px",
							'position' : 'relative',
							'overflow' : 'hidden',
							'border-right' : '1px solid #cbccd0'
						});
						if(this.isHHeader()) {
							tempH.dataDiv.css('float','left');
						}
						var tempDiv = tempH.dataDiv;
						// 将header的引用保存在dataDiv上
						tempDiv.data('header',tempH);
						// 列的数据存放数组,为了快速检索数据
						tempDiv.data('cells',[]);
						this.dynamicColumDataDiv.append(tempDiv);
					}
					// 多表头情况
					else if (tempH.basicHeaders != null && tempH.isHidden == false) {
						var tempHeaders = tempH.basicHeaders;
						for (var j = 0; j < tempHeaders.length; j++) {
							if (tempHeaders[j].isHidden == false) {
								tempHeaders[j].dataDiv = $("<div>").css({
									'width' : (tempHeaders[j].width - 1) + "px",
									'position' : 'relative',
									'overflow' : 'hidden',
									'border-right' : '1px solid #cbccd0'
								});
								if(this.isHHeader()) {
									tempHeaders[j].dataDiv.css('float','left');
								}
								var tempDiv = tempHeaders[j].dataDiv;
								// 将header的引用保存在dataDiv上
								tempDiv.data('header',tempHeaders[j]);
								// 列的数据存放数组,为了快速检索数据
								tempDiv.data('cells',[]);
								this.dynamicColumDataDiv.append(tempDiv);
							}
						}
					}
				}
			}
			if ($.measures.isDivVScroll(this.dataOuterDiv)) {
				this.setScrollTop(0);
			}
		},
		/**
		 * 画静态列数据区div
		 */
		initFixedColumDataDiv : function() {
			var rowsNum = this.getRowsNum();
			if (this.defaultFixedHeaders != null) {
				var len = this.defaultFixedHeaders.length;
				var rowNum = this.getRowsNum();
				for (var i = 0; i < len; i++) {
					var tempH = this.defaultFixedHeaders[i];
					// 单表头情况
					if (tempH.basicHeaders == null && tempH.isHidden == false) {
						tempH.dataDiv = $("<div>").css({
							'width' : tempH.width + "px",
							'position' : 'relative',
							'float' : 'left',
							'overflow' : 'hidden'
						});
						var tempDiv = tempH.dataDiv;
						// 将header的引用保存在dataDiv上
						tempDiv.data('header',tempH);
						// 列的数据存放数组,为了快速检索数据
						tempDiv.data('cells',[]);
						this.fixedColumDiv.append(tempDiv);
					}
					// 多表头情况
					else if (tempH.basicHeaders != null && tempH.isHidden == false) {
						var tempHeaders = tempH.basicHeaders;
						for (var j = 0; j < tempHeaders.length; j++) {
							if (tempHeaders[j].isHidden == false) {
								tempHeaders[j].dataDiv = $("<div>").css({
									'width' : tempHeaders[j].width + "px",
									'position' : 'relative',
									'float' : 'left',
									'overflow' : 'hidden'
								});
								var tempDiv = tempHeaders[j].dataDiv;
								// 将header的引用保存在dataDiv上
								tempDiv.data('header',tempHeaders[j]);
								// 列的数据存放数组,为了快速检索数据
								tempDiv.data('cells',[]);
								this.fixedColumDiv.append(tempDiv);
							}
						}
					}
				}
			}
			if ($.measures.isDivVScroll(this.dataOuterDiv)) {
				this.setScrollTop(0);
			}
		},
		/**
		 * 返回model中的行数量
		 */
		getRowsNum : function() {
			if (this.model == null)
				return 0;
			else
				return this.model.getRowsCount();
		},
		/**
		 * 得到动态列中给定header之前的所有显示的动态列header的宽度和
e		 */
		getPreHeadersWidth : function(cell) {
			var totalWidth = 0;
			// 得到此cell的header
			var len = this.basicHeaders.length;
			for (var i = 0; i < len; i++) {
				var header = this.basicHeaders[i];
				if (!header.isFixedHeader && !header.isHidden) {
					if (header != this.basicHeaders[cell.colIndex])
						totalWidth += header.width;
					else
						break;
				}
			}
			return totalWidth;
		},
		/**
		 * 得到动态表头区或者固定表头区真正的宽度
		 * @param isDynamic true:动态表区 false:静态表区
		 */
		getDynamicTableDivRealWidth : function(isDynamic) {
			var headers = this.model.getHeaders();
			if (headers == null)
				return;
			var realWidth = 0;
			if (isDynamic) {
				// 得到动态表头的宽度
				for (var i = 0; i < headers.length; i++) {
					var header = headers[i];
					if (header.isFixedHeader == false && header.isHidden == false)
						realWidth += this.getHeaderDefaultWidth(header) + $.grid.COLUMN_LEFT_BORDER_WIDTH;
				}
			} else {
				// 得到固定表头的宽度
				for (var i = 0; i < headers.length; i++) {
					var header = headers[i];
					if (header.isFixedHeader && header.isHidden == false)
						realWidth += this.getHeaderDefaultWidth(header) + $.grid.COLUMN_LEFT_BORDER_WIDTH;
				}
			}
			return realWidth;
		},
		getDynamicTableDivRealHeight : function(isDynamic) {
			var headers = this.model.getHeaders();
			if (headers == null)
				return;
			var realHeight = 0;
			if (isDynamic) {
				// 得到动态表头的宽度
				for (var i = 0; i < headers.length; i++) {
					var header = headers[i];
					if (header.isFixedHeader == false && header.isHidden == false)
						realHeight += this.getHeaderDefaultHeight(header) + $.grid.COLUMN_LEFT_BORDER_WIDTH;
				}
			} else {
				// 得到固定表头的宽度
				for (var i = 0; i < headers.length; i++) {
					var header = headers[i];
					if (header.isFixedHeader && header.isHidden == false)
						realHeight += this.getHeaderDefaultHeight(header) + $.grid.COLUMN_LEFT_BORDER_WIDTH;
				}
			}
			return realHeight;
		},
		/**
		 * 设置grid的编辑属性,为true则点击cell时会激活相应的编辑控件
		 * @param isEditable 设置grid可否编辑
		 */
		setEditable : function(isEditable) {
			this.editable = $.argumentutils.getBoolean(isEditable, true);
			if (this.editable && this.compsInited == false)
				$.grid.initEditCompsForGrid(this);
			for (var i = 0; i < this.basicHeaders.length; i++) {
				var header = this.basicHeaders[i];
				// 特殊处理BooleanRender的列
				if (header.renderType == BooleanRender) {
					if (isEditable) {
						if (header.selectBox != null)
							header.selectBox.prop('disabled',false);
						if (header.dataDiv != null && header.dataDiv.data('cells') != null) {
							for (var j = 0; j < header.dataDiv.data('cells').length; j++) {
								if (header.dataDiv.data('cells')[j])
									header.dataDiv.data('cells')[j].children().first().prop('disabled',false);
							}
						}
					} else {
						if (header.selectBox != null)
							header.selectBox.prop('disabled',true);
						if (header.dataDiv != null	&& header.dataDiv.data('cells') != null) {
							for (var j = 0; j < header.dataDiv.data('cells').length; j++) {
								if (header.dataDiv.data('cells')[j])
									header.dataDiv.data('cells')[j].children().first().prop('disabled',true);
							}
						}
					}
				}
			}
		},
		/**
		 * 设置是否多选
		 * @param {} isMultiSelect
		 */
		setMultiSelect : function(isMultiSelWithBox) {
			if (isMultiSelWithBox == this.isMultiSelWithBox)
				return;
			this.isMultiSelWithBox = isMultiSelWithBox;
			// 由复选到单选时，要把选中行去掉
			if (this.isMultiSelWithBox == false) {
				var ds = this.model.dataset;
				ds.setAllRowUnSelected();
				ds.setRowSelected(0);
			}
			this.paintData();
		},
		/**
		 * 设置此grid是否激活
		 * @param isActive true表示处于激活,否则表示禁用状态
		 */
		setActive : function(isActive) {
			this.isGridActive = $.argumentutils.getBoolean(isActive, true);
		},
		/**
		 * 根据后台生成的js脚本设置合计值
		 * @param colIndex:列号
		 * @param keyName:列名
		 * @param sumValue:合计值
		 */
		setSumCellValue : function(colIndex, keyName, sumValue) {
			if (this.model) {
				this.model.setSumColValueByExecuteJs(colIndex, keyName, sumValue);
			}
		},
		/**
		 * 回车事件处理
		 */
		compEnterFun : function(oThis) {
			var activeCell = oThis.selectedCell;
			// 获取下一个将要激活的cell
			var nextActiveCell = oThis.getEditableCellByDirection(activeCell, 1);
			if (nextActiveCell != null) {
				if (activeCell.data('rowIndex') != nextActiveCell.data('rowIndex'))// 选中新行
					oThis.model.setRowSelected(nextActiveCell.data('rowIndex'));
				oThis.nextNeedActiveCell = nextActiveCell;
				oThis.hiddenComp();
			}
		},
		getCellRowIndex : function(cell) {
			if (cell && cell.parent().size()>0) {
				var nodes = cell.parent().children();
				for (var i = 0; i < nodes.length; i++) {
					if (nodes[i] == cell[0])
						return i;
				}
			}
			return 0;
		},
		/**
		 * 隐藏当前显示的控件
		 */
		hiddenComp : function() {
			if (this.showComp != null) {
				this.showComp.hide();
				if (this.showComp.extend)
					this.showComp.destroySelf();
				this.showComp = null;
				// 将激活控件的记录变量设为null(只有编辑控件真正的隐藏才会设置当前的激活cell为null)
				this.currActivedCell = null;
			}
		},
		/**
		 * 隐藏所有已经显示出来的设置menu
		 */
		hiddenSettingMenu : function() {
			if (this.contextMenu && this.contextMenu.visible)
				this.contextMenu.hide();
		},
		/**
		 * 得到最顶层header的默认初始宽度
		 * @param header 最顶层header
		 */
		getHeaderDefaultSize : function(header,sizeType) {
			if (header.parent != null)
				return null;
			var headerSize = 0;
			// 单表头
			if (header.children == null)
				headerSize = header[sizeType];
			// 多表头
			else {
				var basicHeaders = header.basicHeaders;
				for (var j = 0, count = basicHeaders.length; j < count; j++) {
					if (basicHeaders[j].isHidden == false)
						headerSize += basicHeaders[j][sizeType];
				}
			}
			return headerSize;
		},
		getHeaderDefaultWidth : function(header) {
			return this.getHeaderDefaultSize(header,'width');
		},
		getHeaderDefaultHeight : function(header) {
			return this.getHeaderDefaultSize(header,'height');
		},
		/**
		 * 创建表格行
		 * @param table 要增加的行的table的引用
		 * @param posi 要增加行的位置
		 */
		addTableRow : function(tbody, posi) {
			if (posi == null || isNaN(posi))
				posi = tbody.children().length;
			var row = $("<tr>");
			if(posi == 0) {
				row.appendTo(tbody);
			} else {
				row.insertAfter(tbody.children(':eq('+ (posi-1) +')'));
			}
			return row;
		},
		/**
		 * 得到dataOuterDiv是否出了纵向滚动条
		 */
		isDataDivVScroll : function() {
			return $.measures.isDivVScroll(this.dataOuterDiv);
		},
		/**
		 * 得到dataOuterDiv是否出了横向滚动条
		 */
		isDataDivScroll : function() {
			return $.measures.isDivScroll(this.dataOuterDiv);
		},
		/**
		 * 根据当前grid中的行数判断是否出纵向滚动条
		 * @return true|false
		 */
		isVScroll : function() {
			if (this.flowmode)
				return false;
			var num = this.isHHeader() ? this.getRowsNum() : this.basicHeaders.length;
			if (num > 0) {
				// 是否竖直滚动
				return num * this.rowHeight > this.constant.outerDivHeight - this.constant.headerHeight + 2;
			} else if (num == 0)
				return false;
		},
		/**
		 * 根据数据区(数字列+静态表头+动态表头)真正的宽度判断当前是否出横向滚动条
		 * @return boolean 是否出横向滚动条
		 */
		isScroll : function() {
			if(this.isHHeader()) {
				var gridRealWidth = this.getDynamicTableDivRealWidth(true) + this.getDynamicTableDivRealWidth(false)
						+ this.constant.rowNumHeaderDivWidth + 5;
				if (this.isMultiSelWithBox)
					gridRealWidth += $.grid.SELECTCOLUM_WIDTH;
				return gridRealWidth > this.constant.outerDivWidth;
			} else {
				var gridRealWidth = this.getRowsNum() * this.headerRowWidth;
				return gridRealWidth > this.dataOuterDiv.outerWidth();
			}
		},
		/**
		 * 插入一行 用户调用此方法插入一行数据
		 * @param row $.gridrow
		 * @param index 插入的位置
		 */
		insertRow : function(row, index) {
			if (this.model == null)
				this.model = $.gridcompmodel.getObj();
			if (row == null || $.gridrow.prototype.isPrototypeOf(row))
				return this.model.insertRow(row, index);
			else
				throw new Error("Row must be the instance of gridrow or null!");
		},
		/**
		 * 增加一行 用户调用此方法增加一行数据
		 * @param row $.gridrow
		 */
		addRow : function(row) {
			if (this.model == null)
				this.model = $.gridcompmodel.getObj();
			if (row == null || $.gridrow.prototype.isPrototypeOf(row))
				return this.model.addRow(row);
			else
				throw new Error("Row must be the instance of gridrow or null!");
		},
		/**
		 * 增加一组行
		 * @param rows $.gridrow数组
		 */
		addRows : function(rows) {
			if (rows != null) {
				for (var i = 0; i < rows.length; i++)
					this.addRow(rows[i]);
			}
		},
		/**
		 * Model中插入行时触发此方法.
		 * @param index 插入行的位置
		 */
		fireRowInserted : function(index, level, parentRowIndex) {
			var gridHeight = this.constant.outerDivHeight;
			// 可见区域高度
			var areaHeight = 0;
			if (this.scroll)
				areaHeight = gridHeight - this.constant.headerHeight - $.grid.SCROLLBAE_HEIGHT;
			else
				areaHeight = gridHeight - this.constant.headerHeight;
			if (areaHeight < 0)
				areaHeight = 0;
			// 每一个header.dataDiv的高度增加一个行高
			var basicHeaders = this.basicHeaders;
			var row = this.model.getRow(index);
			if (level != null)
				row.level = level + 1;
			this.setHeadersOffsetWidth();
			initLayoutMonitorState();
			this.addOneRow(row, index, this.dataOuterDiv.scrollLeft(), this.rowHeight, this.model.getRowsCount(), parentRowIndex);
			this.clearHeadersOffsetWidth();
			executeLayoutMonitor();
			//TODO:不知道为什么原来这里还要再绘制一遍,为了使树表的展开生效，这里暂时加判断处理
			if(parentRowIndex<0)
				this.paintRows(true);
		},
		/**
		 * 创建行状态和行标div
		 */
		andLineStateAndColNum : function(rowCount, index, rowHeight, row) {
			var isOdd = this.isOdd(index);
			// 增加数字行号
			if (this.isShowNumCol) {
				var $n = $("<div>").addClass('num_cell').attr('id',"numline" + index).css({
					'height' : (rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH) + "px",
					'line-height' : (rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH) + "px"
				}).html("<center>" + (index + 1) + "</center>");
				$n.data('rowIndex',index);
				if (typeof(this.rowNumDiv.data('cells')[index]) == "undefined") {
					this.rowNumDiv.append($n);
				} else {
					$n.insertBefore(this.rowNumDiv.data('cells')[index]);
				}
			}
			// 创建行状态列内的div
			var $l = $("<div>").addClass('row_state_div').css('height',rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH + "px");
			if (this.lineStateColumDiv.children().length == index)
				this.lineStateColumDiv.append($l);
			else
				$l.insertBefore(this.lineStateColumDiv.children(':eq('+index+')'));
			if (this.isShowNumCol) {
				this.rowNumDiv.data('cells').splice(index, 0, $n);
				var len = this.rowNumDiv.data('cells').length;
				for (var i = index + 1; i < len; i++) {
					if (typeof(this.rowNumDiv.data('cells')[i]) != "undefined") {
						this.rowNumDiv.data('cells')[i].data('rowIndex',i);
						this.rowNumDiv.data('cells')[i].html("<center>" + (i + 1) + "</center>");
					}
				}
			}
			this.lineStateColumDiv.data('cells').splice(index, 0, $l);
			if (row.pageData.state == $.datasetrow.CONST.STATE_NEW)
				this.lineStateColumDiv.data('cells')[index].attr('class',"row_state_div row_add_state");
			else
				this.lineStateColumDiv.data('cells')[index].attr('class','row_state_div');
		},
		/**
		 * 设置复选框选中状态
		 * @param checked:是否选中
		 * @param rowIndex:行号
		 * @private addOneRow()的时候调用
		 */
		setCheckBoxChecked : function(checked, rowIndex) {
			// 分组复选框
			if (this.groupHeaderIds.length > 0 && this.isMultiSelWithBox && this.isGroupWithCheckbox) {
				var groupRowIds = this.model.rows[rowIndex].groupRowIds;
				if (groupRowIds != null) {
					for (var i = groupRowIds.length - 1; i >= 0; i--) {
						var index = this.model.getRowIndexById(groupRowIds[i]);
						if (index != -1) {
							if (checked) {
								this.model.addRowSelected(index);
							} else {
								this.model.setRowUnSelected(index);
							}
						}
					}
				}
			} else {
				if (checked == true) {
					// 递归勾选所有行
					this.expandAndSeclectNodesByRowIndex(rowIndex);
					this.rowSelected(rowIndex);
					this.setFocusIndex(rowIndex);
				} else {
					this.unselectNodesByRowIndex(rowIndex);
					this.setFocusIndex(rowIndex);
				}
			}
		},
		/**
		 * 给表格增加一行
		 * @param row(model数据中的一行)
		 * @param len 即headers.length
		 * @param isInsertInMiddle 向中间插入行
		 */
		addOneRow : function(row, index, scrollLeft, rowHeight, rowCount, parentRowIndex) {
			// initLayoutMonitorState();
			if (this.noRowsDiv) {
				this.noRowsDiv.hide();
				this.dynamicColumDataDiv.css('margin-bottom','17px');
			}
			this.dataOuterDiv.css('overflow','auto').show();
			var isOdd = this.isOdd(index);
			var tempHeaders = [];
			var fixedheaderWidth = this.constant.fixedHeaderDivWidth;
			var scrollTop = 0;
			var oThis = this;
			// 判断是否出竖直滚动条,将headerDiv的宽度缩小17px
			if (this.firstVScroll == false) {
				if (this.isVScroll() && this.isHHeader()) {
					var barWidth = $.grid.SCROLLBAE_HEIGHT;
					var dynHeaderWidth = this.constant.outerDivWidth - fixedheaderWidth - barWidth - 1 + scrollLeft;
					if (dynHeaderWidth > 0)
						this.dynamicHeaderDiv.css('width',dynHeaderWidth + "px");
					this.dynamicHeaderDiv.data('defaultWidth',dynHeaderWidth);
					this.headerDiv.data('defaultWidth',this.constant.outerDivWidth - barWidth - 1);
					this.firstVScroll = true;
				}
			}
			this.andLineStateAndColNum(rowCount, index, rowHeight, row);
			var checkDiv = null;
			var checkBox = null;
			if (this.isMultiSelWithBox && this.isHHeader()) {
				// 向固定选择列增加选择框
				checkDiv = $("<div>").addClass(isOdd ? "fixed_selectcolum_checkbox_div_odd" : "fixed_selectcolum_checkbox_div_even").css({
					'left' : '0px',
					'width' : ($.grid.SELECTCOLUM_WIDTH - 1) + "px",
					'height' : (rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH) + "px",
					'line-height' : (rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH) + "px"					
				});
				checkDiv.data('editorType',"CheckBox");
				checkBox = $("<input type='checkbox'/>").appendTo(checkDiv).css('margin-top','5px');
				this.selectColumDiv.append(checkDiv);
				if (this.selectColumDiv.children().length == index)
					this.selectColumDiv.append(checkDiv);
				else
					checkDiv.insertBefore(this.selectColumDiv.children(':eq('+index+')'));
				if (this.selectColumDiv.data('divWidth') != null)
					this.fixedColumDiv.css('height',this.selectColumDiv.data('divWidth') + "px");
				else
					this.fixedColumDiv.css('height',this.selectColumDiv.outerHeight() + "px");
				// 设置选中状态
				checkBox.prop('checked',this.model.isRowSelected(index)).on({
					'mousedown' : function(e) {
						// 置空当前选择单元格
						oThis.selectedCell = null;
						oThis.oldCell = null;
						var rowIndex = oThis.getCellRowIndex($(this).parent());
						// 设置选中状态
						if (oThis.model.rows[rowIndex].loadImg	&& oThis.model.rows[rowIndex].loadImg.data('plus')) {
							this.tempChecked = this.checked;
							oThis.setCheckBoxChecked(!this.checked, rowIndex);
						} else {
							if (typeof(this.tempChecked) == "boolean") {// plus从true到false过程中的第一次
								this.tempChecked = null;
							} else {
								oThis.setCheckBoxChecked(!this.checked, rowIndex);
							}
						}
						// 隐藏编辑框
						oThis.hiddenComp();
					},
					'click' : function(e) {
						e.preventDefault();
						e.stopPropagation();
					}
				});
				this.selectColumDiv.data('cells').splice(index, 0, checkDiv);
			}
			// 动态表头长度
			var dynHeaderLen = this.defaultDynamicHeaders.length;
			var lastHeader = this.getLastDynamicVisibleHeader();
			var firstHeader = this.getFirstDynamicVisibleHeader();
			var rowsCount = this.model.getRowsCount();
			// this.setHeadersOffsetWidth();
			// 存储一行的所有cell,行渲染用
			var rowCells = null;
			if (this.rowCells == null)
				rowCells = [];
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true)
					continue;
				var cell = $("<div>");
				rowCells.push(cell);
				cell.data('rowIndex',index);
				if (row.level != null)
					cell.data('level',row.level);
				if (row.hasChildren && row.hasChildren != null)
					cell.data('hasChildren',row.hasChildren);
				// 记录cell对应ds中的第几列
				cell.data('colIndex',i);
				if(typeof(switchEditorType) == 'function') {
					switchEditorType(this,row,cell,header);
				} else {
					cell.data('editorType',header.editorType);
				}
				
				cell.addClass(isOdd ? "gridcell_odd" : "gridcell_even").on({
					'mouseover' : function() {
						if(oThis.isShowTip){
							oThis.showTipMessage($(this));
						}
						oThis.gridRowMouseOver($(this));
					},
					'mouseout' : function() {
						oThis.hideTipMessage();
						oThis.gridRowMouseOut($(this));
					}
				});
				if (this.autoRowHeight == false) {
					cell.css('height',rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH + "px")
				} else {
					var minHeight = rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH;
					cell.css('min-height',minHeight + "px");
					if (minHeight > this.rowMinHeight[index]) {
						this.rowMinHeight[index] = minHeight;
					}
					this.defaultRowMinHeight[index] = minHeight;
				}
				// 在div中只有一行的情况下设置行距等于div高度,可以使div中的文字距中,从而不需要设置paddingTop
				cell.css({
					'line-height' : rowHeight - $.grid.CELL_BOTTOM_BORDER_WIDTH + "px",
					'padding-left' : $.grid.CELL_LEFT_PADDING + "px",
					'padding-right' : $.grid.CELL_RIGHT_PADDING + "px"
				});
				/*
				 * 标题中对齐 文本类、参照类、枚举类的左对齐 数字类的右对齐 日期/布尔类的居中
				 * 如果align为空，则走默认，否则为传过来的align值
				 */
				if (typeof(header.dataType) == "string" && !header.textAlign) {
					switch (header.dataType) {
						case "FDateTime" :
						case "FDate" :
						case "FTime" :
						case "Date" :
						case "ShortDate" :
						case "Boolean" :
						case "boolean" :
						case "FBoolean" :
							cell.css('text-align','center');
							break;
						case "Integer" :
							if (header.editorType && header.editorType == "ComboBox") {
								// 下拉框当做文本类处理
								cell.css('text-align','left');
								break;
							}
						case "int" :
						case "Double" :
						case "double" :
						case "FDouble" :
						case "Float" :
						case "float" :
						case "BigDecimal" :
						case "Decimal" :
						case "Long" :
						case "long" :
							cell.css('text-align','right');
							break;
						default :
							cell.css('text-align','left');
							break;
					}
				} else {
					// 根据header中textAlign属性设置文字在cell中的位置.
					cell.css('text-align',header.textAlign);
				}
				if (parentRowIndex != null) {
					cell.data('parentCell',header.dataDiv.children(':eq('+ parentRowIndex +')'));
				}
				// 先将cell放入dataDiv,以便于子可以获取各种定位属性
				if (header.dataDiv.children().length == index) {
					header.dataDiv.append(cell);
				} else {
					cell.insertBefore(header.dataDiv.children(':eq('+ index +')'));
				}
				if(this.isHHeader()) {
					if (header.dataDivWidth != null && header.dataDivWidth > 0) {
						cell.css('width',(header.dataDivWidth - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
					} else if (header.dataDiv && header.dataDiv.outerWidth() > 0) {
						cell.css('width',(header.dataDiv.outerWidth() - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
					} else if (header.width && header.width > 0) {
						cell.css('width',(header.width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
					}
				} else {
					cell.css('width',(header.width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
				}
				
				// 如果分组显示,新分组开始的标示
				var newGroupBegin = false;
				// 如果分组显示,新分组结束的标示
				var newGroupEnd = false;
				if (header.isGroupBy) {
					// 第一个分组的header
					if (header.keyName == this.groupHeaderIds[0]) {
						if ((index > 0 && row.getCellValue(i) != this.model.rows[index - 1].getCellValue(i)) || index == 0) {
							newGroupBegin = true;
							// 为Model设置临时的新分组第一行
							this.model.newGroupRow = this.model.rows[index];
							// 第一行的相关分组行数组，用于级联选中和反选
							this.model.rows[index].groupRowIds = [];
							var rowId = this.model.rows[index].pageData.rowId;
							this.model.rows[index].groupRowIds.push(rowId);
						}
					}
					/*
					 * 后面分组的header开始新组的条件: 1.第一行肯定要分组 2.此cell数据和上一行该列的cell数据不一样
					 * 3.上一个分组header列的该行的cell数据和此列上一行的不一致则也要分组
					 */
					else {
						if (index == 0 || (row.getCellValue(i) != this.model.rows[index - 1].getCellValue(i)) || this.isCurrCellNeedNewGroup(row, index, i))
							newGroupBegin = true;
					}
					// 判断是否为新分组的最后一个cell
					if ((index <= (this.model.rows.length - 2) 
							&& row.getCellValue(i) != this.model.rows[index + 1].getCellValue(i)) 
							|| index == this.model.rows.length - 1) {
						newGroupEnd = true;
					}
				}
				if(this.isHHeader()) {
					// 动态列
					if (header.isFixedHeader == false) {
						// 单表头
						if (header.children == null) {
							if (dynHeaderLen > 0 && header != firstHeader && header != lastHeader) {
							} else {
								// 动态列最后一列为单表头的情况
								if (dynHeaderLen > 0 && header == lastHeader) {
									if (header.isGroupBy) {
									} else {
										if (header.dataDivWidth != null && header.dataDivWidth > 0)
											cell.css('width',(header.dataDivWidth - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - 1) + "px");
										// 最后一列border为1
										else if (header.dataDiv && header.dataDiv.outerWidth() > 0) {
											cell.css('width',(header.dataDiv.outerWidth() - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - 1) + "px");
											// 最后一列border为1
										} else if (header.width && header.width > 0) {
											cell.css('width',(header.width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - 1) + "px");
										}
										cell.css('border-right','1px solid #D1DFE4');
									}
								}
							}
						} else {
							if (dynHeaderLen > 0) {
								if (header != firstHeader && header != lastHeader) {
									cell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
								}
								// 动态列第一列为多表头的情况
								else if (header == firstHeader) {
									cell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
									if (j == 0) {
										// 多表头最后一列不显示右边线
										cell.css('border-left','none');
									}
								}
								// 动态列最后一列为多表头的情况
								else if (header == lastHeader) {
									cell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
								}
								cell.css('border-right','1px solid #D1DFE4');
							}
						}
					}
					// 固定列
					else {
						// 单表头
						if (header.children == null) {
							cell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even");
						} else {
							cell.attr('class','fixed_colum_grid_cell');
						}
					}
				} else {
					if(this.getRowsNum() != header.dataDiv.data('cells').length+1) {
						cell.css('width',(header.width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING - 1) + "px");
						cell.css('border-right','1px solid #D1DFE4');
					}
				}
				// 渲染cell
				var childNode = null;
				// 判断是否为分组显示的列
				if (!header.isGroupBy) {
					childNode = header.renderType.render.call(this, index, i, row.getCellValue(i), header, cell, parentRowIndex);
				} else if (header.isGroupBy == true) {
					// 新组开始才画该cell
					var realValue = null;
					if (newGroupBegin) {
						realValue = row.getCellValue(i);
					}
					childNode = header.renderType.render.call(this, index, i, realValue, header, cell, parentRowIndex);
					if (checkDiv != null && checkBox != null && this.isGroupWithCheckbox) {
						if (!newGroupBegin) {
							// 第一个分组的header
							if (header.keyName == this.groupHeaderIds[0]) {
								// 去掉除新组第一个cell以外的复选框
								checkBox.remove();
								// 向第一个分组行增加相关分组行数据，用于级联选中和反选
								var rowId = this.model.rows[index].pageData.rowId;
								this.model.newGroupRow.groupRowIds.push(rowId);
							}
						}
					}
					// 分组非第一个cell,改变前一个cell的背景色和下边框,当前cell的背景色.
					if (!newGroupBegin) {
						if (index != 0) {
							var preCell = this.basicHeaders[i].dataDiv.data('cells')[index - 1];
							preCell.css({
								'border-bottom-color' : '#ffffff',
								'background-color' : '#ffffff'
							});
						}
						cell.css('background-color','#ffffff');
					}
				}
				// 根据header中的bgcolor属性设置列背景色
				if (header.columBgColor != null && header.columBgColor != "" && cell.css('background-color') != "#ffffff") {
					cell.css('background-color',header.columBgColor);
				}
				if (header.textColor != null && header.textColor != "") {
					cell.css('color',header.textColor);
				}
				if(!this.isHHeader()) {
					var _cells = header.dataDiv.data('cells');
					header.dataDiv.css('width',((_cells && _cells.length ==0) ? 0 : header.dataDiv.outerWidth()-1) + cell.outerWidth());
					cell.css('float','left');
				}
				header.dataDiv.data('cells').splice(index, 0, cell);
				// 最后调整高度
				if (this.autoRowHeight == true) {
					this.adjustRowHeight(index, cell);
				}
			}
			// 行渲染
			this.rowRender.render.call(this, row, rowCells);
		},
		setTipSticky : function() {
			this.tipSticky = true;
		},
		hideTipMessage : function(force) {
			if (!this.tipSticky || force) {
				if ($.grid.tipDiv)
					$.grid.tipDiv.hide();
			}
			if (this.tipRt)
				clearTimeout(this.tipRt);
		},
		showTipMessage : function(cell) {
			if (this.tipRt != null)
				clearTimeout(this.tipRt);
			var title = cell.data('tip');
			if (title == null || title == "")
				return;
			if (cell.data('editorType') != null && cell.data('editorType') == "CheckBox")
				return;
			var left = cell.offset().left + 1;
			var top = cell.offset().top + $.grid.ROW_HEIGHT;
			var width = (cell.outerWidth() - 2) + "px";
			this.tipRt = setTimeout(function() {
				$.grid.doShowTipMessage(left, top, width, encodeURIComponent(title));
			}, 500);
		},
		/**
		 * 设置每个Header的OffsetWidth属性，避免增行时反复计算
		 */
		setHeadersOffsetWidth : function() {
			var headers = this.model.getHeaders();
			for (var i = 0, n = headers.length; i < n; i++) {
				var header = headers[i];
				if (header.dataDiv != null) {
					header.dataDivWidth = header.dataDiv.outerWidth();
					var basicHeaders = header.basicHeaders;
					if (basicHeaders != null) {
						for (var j = 0, m = basicHeaders.length; j < m; j++) {
							var basicHeader = basicHeaders[j];
							basicHeader.dataDivWidth = basicHeader.dataDiv.outerWidth();
						}
					}
				}
			}
			if (this.selectColumDiv)
				this.selectColumDiv.data('divWidth',this.selectColumDiv.outerWidth());
		},
		/**
		 * 清空每个Header的OffsetWidth属性
		 */
		clearHeadersOffsetWidth : function() {
			var headers = this.model.getHeaders();
			for (var i = 0, n = headers.length; i < n; i++) {
				var header = headers[i];
				header.dataDivWidth = null;
				var basicHeaders = header.basicHeaders;
				if (basicHeaders != null) {
					for (var j = 0, m = basicHeaders.length; j < m; j++) {
						basicHeaders[j].dataDivWidth = null;
					}
				}
			}
			if (this.selectColumDiv)
				this.selectColumDiv.data('divWidth',null);
		},
		/**
		 * 鼠标移入后改变行样式
		 */
		gridRowMouseOver : function(cell) {
			var rowIndex = this.getCellRowIndex(cell);
			if (rowIndex == this.getFocusIndex()) {
				// 焦点行不改变样式
				return;
			}
			// 改变当前行的外观
			for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
				var header = this.basicHeaders[i];
				if (header.isHidden == false) {
					var tempCell = header.dataDiv.data('cells')[rowIndex];
					if (tempCell != null) {
						if (!tempCell.data('isErrorCell')) {
							var isOdd = this.isOdd(rowIndex);
							if (!tempCell.hasClass('cell_select')) {
								var row = this.getRow(rowIndex);//获取当前行
								// 当前行不是选中行,不是焦点行,改变外观.
								tempCell.data('oldClassName',tempCell.attr('class'));
								//若grid的isAllowMouseoverChange==true时，grid的row属性isAllowMouseoverChange==true才管用
								if(this.options.isAllowMouseoverChange && row.pageData.isAllowMouseoverChange){
									tempCell.attr('class',isOdd ? "gridcell_odd gridcell_mouseover" : "gridcell_even gridcell_mouseover");
								}else{
									tempCell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
								}
							}
						}
					}
				}
			}
		},
		/**
		 * 鼠标移出后改变行样式
		 */
		gridRowMouseOut : function(cell) {
			var rowIndex = this.getCellRowIndex(cell);
			if (rowIndex == this.getFocusIndex()) {
				// 焦点行
				for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
					var header = this.basicHeaders[i];
					if (header.isHidden == false) {
						var tempCell = header.dataDiv.data('cells')[rowIndex];
						if (tempCell != null) {
							if (!tempCell.data('isErrorCell')) {
								tempCell.removeClass('gridcell_mouseover');
							}
						}
					}
				}
				return;
			}
			var selectedRowIndice = this.getSelectedRowIndice();
			if (selectedRowIndice && selectedRowIndice.indexOf(rowIndex) != -1) {
				this.rowSelected(rowIndex);
				return;
			}
			for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
				var header = this.basicHeaders[i];
				if (header.isHidden == false) {
					var tempCell = header.dataDiv.data('cells')[rowIndex];
					if (tempCell != null) {
						if (!tempCell.data('isErrorCell')) {
							tempCell.attr('class',tempCell.data('oldClassName'));
						}
					}
				}
			}
		},
		/**
		 * 判断该cell是否应该启动新的分组
		 */
		isCurrCellNeedNewGroup : function(row, rowIndex, curGroupColIndex) {
			// 分组列列号数组
			var indice = this.groupHeaderColIndice;
			if (indice != null && indice.length > 0) {
				// 获取上一个最近的分组
				var startIndex = indice.indexOf(curGroupColIndex) - 1;
				for (var i = startIndex; i >= 0; i--) {
					if ((row.getCellValue(indice[i]) != this.model.rows[rowIndex - 1].getCellValue(indice[i])))
						return true;
				}
				return false;
			} else
				return false;
		},
		/**
		 * 若选中行不在选中区域,滚动行到选中的行
		 * @param index 行位置
		 */
		scrollToSelectedRow : function(index) {
			// 滚动时将当前选中的cell置空,则在index行选中时会清除当前行的选中cell的外观
			this.selectedCell = null;
			// 滚动的时候隐藏当前显示的控件
			this.hiddenComp();
			// 得到显示区域的top,bottom值
			var displayCont = this.calcuDisplayRowNum();
			var displayContTop = displayCont[0] + 2;
			// 因为显示的特殊需要此displayContBottom的值要大于实际的显示区域的底部值,故减去2
			var displayContBottom = displayCont[1] - 2;
			var sRowH = index * this.rowHeight;
			if (sRowH < displayContTop * this.rowHeight)
				this.setScrollTop(parseFloat(this.dataOuterDiv.scrollTop()) - (displayContTop * this.rowHeight - sRowH));
			else if (sRowH > displayContBottom * this.rowHeight)
				this.setScrollTop(parseFloat(this.dataOuterDiv.scrollTop()) + (sRowH - displayContTop * this.rowHeight));
			displayCont = this.calcuDisplayRowNum();
			displayContTop = displayCont[0];
			displayContBottom = displayCont[1];
		},
		/**
		 * 得到key在header中的位置
		 * @return key在headers中的索引
		 */
		nameToIndex : function(key) {
			for (var i = this.basicHeaders.length - 1; i >= 0; i--) {
				if (this.basicHeaders[i].keyName == key)
					return i;
			}
			return -1;
		},
		/**
		 * 得到当前选中行的索引数组
		 * @return 当前选中行的索引数组
		 */
		getSelectedRowIndice : function() {
			if (this.isMultiSelWithBox == false)
				return this.selectedRowIndice;
			else {
				if (this.model.dataset != null)
					return this.model.getSelectedIndices();
			}
		},
		/**
		 * 得到选中的所有行
		 * @return $.gridrow的数组
		 */
		getSelectedRows : function() {
			if (this.isMultiSelWithBox == false) {
				if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0)
					return [this.getRow(this.selectedRowIndice[0])];
			} else
				return this.model.getSelectedRows();
		},
		/**
		 * 改变点击的cell所在一行的所有cell的外观
		 * @param rowIndex 行号
		 * @param isAddSel 是否追加显示选中行
		 */
		rowSelected : function(rowIndex, isAddSel) {
			// 此模式下只能有一行的外观为选中行样式
			if (isAddSel == null) {
				// 改变上次选中行外观效果
				if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0) {
					if (!this.isMultiSelWithBox && this.selectedRowIndice[0] != -1) {
						for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
							var header = this.basicHeaders[i];
							if (header.isHidden == false) {
								var selIndex = this.selectedRowIndice[0];
								var isOdd = this.isOdd(selIndex);
								var seleCell = header.dataDiv.data('cells')[selIndex];
								// 将上次选中行的背景色全部变为白色,需要校验的field不变色
								if (seleCell != null) {
									if (seleCell.data('isErrorCell')) {
										if (header.isFixedHeader)
											seleCell.attr('class',isOdd ? "fixed_gridcell_odd cell_error" : "fixed_gridcell_even cell_error")
										else
											seleCell.attr('class',isOdd ? "gridcell_odd cell_error" : "gridcell_even cell_error");
									} else {
										if (header.isFixedHeader)
											seleCell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even");
										else
											seleCell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
									}
								}
							}
						}
					}
				} else {
					// 没有记录的选中行,说明是第一次点击
					this.selectedRowIndice = [];
				}
				// 改变当前行的外观
				this.changeCurrSelectedRowStyle(rowIndex);
				// 设置行状态
				if (this.isMultiSelWithBox) {
					if (this.selectedRowIndice.length > 0) {
						// 恢复上次选中行的状态图标
						var node = this.lineStateColumDiv.data('cells')[this.selectedRowIndice[0]];
						if (node != null && node.attr('class') == "row_state_div row_normal_state")
							node.attr('class','row_state_div');
						var curNode = this.lineStateColumDiv.data('cells')[rowIndex];
						if (curNode != null && curNode.attr('class') != "row_state_div row_delete_state")
							curNode.attr('class',"row_state_div row_normal_state");
					}
				} else {
					var node = this.lineStateColumDiv.data('cells')[rowIndex];
					if (node != null
							&& node.attr('class') != "row_state_div row_update_state"
							&& node.attr('class') != "row_state_div row_add_state"
							&& node.attr('class') != "row_state_div row_delete_state")
						node.attr('class','row_state_div row_normal_state');
				}
				// 改变当前点击的cell的外观
				this.changeSelectedCellStyle(rowIndex);
				if (this.selectedCell != null && this.currActivedCell != null && (this.currActivedCell != this.selectedCell)) {
					if (this.selectedCell.data('rowIndex') == rowIndex)
						this.selectedCell.attr('class','cell_select');
					else {
						var isOdd = this.isOdd(rowIndex);
						var header = this.basicHeaders[this.selectedCell.data('colIndex')];
						if (header.isFixedHeader)
							this.selectedCell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even");
						else
							this.selectedCell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
					}
				}
				if (!this.isMultiSelWithBox) {
					// 保存当前选中行
					this.selectedRowIndice[0] = rowIndex;
				} else {
					// 保存当前选中行
					var isExist = false;
					for (var i = 0; i < this.selectedRowIndice.length; i++) {
						if (this.selectedRowIndice[i] == rowIndex) {
							isExist = true;
						}
					}
					if (!isExist) {
						this.selectedRowIndice.push(rowIndex);
					}
				}
			}
			// 追加行选中
			else if (isAddSel) {
				if (this.selectedRowIndice == null)
					this.selectedRowIndice = [];
				// 将当前点击行的外观改变
				this.changeCurrSelectedRowStyle(rowIndex);
				// 改变当前点击的cell的外观
				this.changeSelectedCellStyle(rowIndex);
				// 记录当前选中行
				this.selectedRowIndice.push(rowIndex);
			}
			// 调用用户的方法
			if (this.isMultiSelWithBox == false)
				this.onRowSelected(rowIndex);
		},
		changeCurrSelectedRowStyle : function(rowIndex) {
			for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
				var header = this.basicHeaders[i];
				if (header.isHidden == false) {
					var tempCell = header.dataDiv.data('cells')[rowIndex];
					if (tempCell != null) {
						if (!tempCell.data('isErrorCell')) {
							var isOdd = this.isOdd(rowIndex);
							if (header.isFixedHeader)
								tempCell.attr('class',isOdd ? "fixed_gridcell_odd cell_select" : "fixed_gridcell_even cell_select");
							else
								tempCell.attr('class',isOdd ? "gridcell_odd cell_select" : "gridcell_even cell_select");
						}
					}
				}
			}
		},
		changeSelectedCellStyle : function(rowIndex) {
			if (!this.isMultiSelWithBox && this.selectedCell != null && !this.selectedCell.data('isErrorCell')) {
				var isOdd = this.isOdd(rowIndex);
				var header = this.basicHeaders[this.selectedCell.data('colIndex')];
				if (header == null)
					return;
				if (header.isFixedHeader)
					this.selectedCell.attr('class',isOdd ? "fixed_gridcell_odd cell_focus" : "fixed_gridcell_even cell_focus");
				else
					this.selectedCell.attr('class',isOdd ? "gridcell_odd cell_focus" : "gridcell_even cell_focus");
			}
		},
		/**
		 * 得到给定索引数组的行
		 * @return $.gridrow数组
		 */
		getRows : function(indice) {
			if (indice == null || !(indice instanceof Array))
				return null;
			var rows = [];
			for (var i = 0; i < indice.length; i++)
				rows.push(this.model.getRow(indice[i]));
			return rows;
		},
		/**
		 * 得到给定索引数组的行
		 * @return $.gridrow数组
		 */
		getRow : function(index) {
			if (index == null)
				return null;
			var tempIndex = parseInt(index);
			return this.getRows([tempIndex])[0];
		},
		getDatasetRow : function(uiRowIndex) {
			var row = this.getRow(uiRowIndex);
			if (row != null)
				return row.pageData;
			return null;
		},
		/**
		 * model中行删除时的回调方法
		 */
		fireRowDeleted : function(indice) {
			// 如果outerDiv没有显示出来，不进行处理
			if (this.outerDiv.outerWidth() == 0) {
				this.needPaintRows = true;
				return;
			};
			var gridWidth = this.constant.outerDivWidth;
			for (var i = 0, count = indice.length; i < count; i++) {
				// 若显示控件在当前选中行则隐藏当前控件
				if (this.currActivedCell != null && this.currActivedCell.attr('id') == indice[i])
					this.hiddenComp();
				// 删除整个选中行
				this.deleteRows([indice[i]]);
			}
			this.oldCell = null;
		},
		/**
		 * 删除选中行
		 */
		deleteSeletedRow : function() {
			var selectedRowIndice = this.getSelectedRowIndice();
			if (selectedRowIndice != null && selectedRowIndice.length > 0) {
				// 删除model中的此行数据
				this.model.deleteRows(selectedRowIndice);
				this.selectedRowIndice = null;
			}
		},
		/**
		 * 删除指定行
		 * @param indice 指定的索引值数组
		 */
		deleteRows : function(indice) {
			initLayoutMonitorState();
			if (indice == null || indice.length <= 0)
				return;
			// 要删除的行数
			var deleCount = indice.length;
			// 将数组的值按升序排列
			indice.sort(ascendRule_int);
			var len = this.basicHeaders.length;
			// 如果显示数字行标,则从最后行删除deleCount个numdiv
			if (this.isShowNumCol) {
				for (var i = 0; i < deleCount; i++) {
					if (this.rowNumDiv.children().length - 1 >= 0) {
						var node = this.rowNumDiv.data('cells')[indice[i]];
						if (node != null) {
							// 从界面上删除
							node.remove();
						}
						// 从数组中移除,必须在最后移除,否则数据会错位
						this.rowNumDiv.data('cells').splice(indice[i], 1);
					}
				}
				var cellLen = this.rowNumDiv.data('cells').length;
				for (var i = 0; i < cellLen; i++) {
					if (typeof(this.rowNumDiv.data('cells')[i]) != "undefined") {
						this.rowNumDiv.data('cells')[i].data('rowIndex',i).html("<center>" + (i + 1) + "</center>");
					}
					if (this.rowNumDiv.children(':eq('+i+')').size() > 0) {
						this.rowNumDiv.children(':eq('+i+')').data('rowIndex',i).html("<center>" + (i + 1) + "</center>");
					}
				}
			}
			// 删除行状态列,deleCount个numdiv
			for (var i = 0; i < deleCount; i++) {
				if (this.lineStateColumDiv.children().length - 1 >= 0) {
					var node = this.lineStateColumDiv.data('cells')[indice[i]];
					if (node != null) {
						node.remove();
					}
					this.lineStateColumDiv.data('cells').splice(indice[i], 1);
				}
			}
			if (deleCount == 1) {
				var rowIndex = indice[0];
				var rowCount = this.model.getRowsCount();
				// 如果多选,删除多选列中的checkbox
				if (this.isMultiSelWithBox) {
					this.selectColumDiv.data('cells')[rowIndex].remove();
					this.selectColumDiv.data('cells').splice(rowIndex, 1);
				}
				// 删除选中行中cells
				for (var i = 0; i < len; i++) {
					if (this.basicHeaders[i].isHidden == false) {
						// 得到列div
						var dataDiv = this.basicHeaders[i].dataDiv;
						dataDiv.data('cells')[rowIndex].remove();
						dataDiv.data('cells').splice(rowIndex, 1);
					}
				}
				// 从该删除行的下一行开始向下扫描,改变每一行的cell的id,并将cell上移一个单位
				var signLen = this.model.getRowsCount();
				var cell = null;
				var seleCheck = null;
				for (var i = indice[0]; i < signLen; i++) {
					if (this.isMultiSelWithBox) {
						seleCheck = this.selectColumDiv.data('cells')[i];
						seleCheck.data('rowIndex',seleCheck.data('rowIndex') - 1);
					}
					for (var j = 0; j < len; j++) {
						if (this.basicHeaders[j].isHidden == false) {
							cell = this.basicHeaders[j].dataDiv.data('cells')[i];
							// 改变行号
							cell.data('rowIndex',cell.data('rowIndex') - 1);
							if ($.editortype.CHECKBOX == cell.data('editorType') && cell.data('checkBox')) {
								cell.data('checkBox').data('rowIndex',cell.data('rowIndex'));
							}
						}
					}
				}
				if (this.selectedRowIndice != null) {
					// 调整选中行的索引值
					for (var i = 0, count = this.selectedRowIndice.length; i < count; i++) {
						if (this.selectedRowIndice[i] > rowIndex)
							this.selectedRowIndice[i]--;
					}
				}
			}
			if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0) {
				if (this.selectedRowIndice[0] == rowIndex)
					this.selectedRowIndice = null;
			}
			executeLayoutMonitor();
		},
		/**
		 * 获得Header宽度
		 */
		getHeaderWidth : function() {
			var headers = this.model.getHeaders();
			if (headers == null)
				return 0;
			var width = 0;
			for (var i = 0; i < headers.length; i++) {
				if (!headers[i].isHidden)
					width += headers[i].width;
			}
			return width;
		},
		/**
		 * 获得Header最大深度
		 */
		getHeaderDepth : function() {
			var headers = this.model.getHeaders();
			var maxDepth = 1;
			for (var i = 0; i < headers.length; i++) {
				var depth = headers[i].getDepth();
				if (maxDepth <= depth)
					maxDepth = depth;
			}
			return maxDepth;
		},
		/**
		 * model改变后会调用此方法通知grid
		 * @param rowIndex cell所在行号
		 * @param colIndex cell所在列号
		 * @param newValue 新值
		 * @param oldValue 旧值
		 */
		cellValueChangedFunc : function(rowIndex, colIndex, newValue, oldValue) {
			// 此列不隐藏
			if (this.basicHeaders[colIndex].isHidden == false) {
				var cell = this.basicHeaders[colIndex].dataDiv.data('cells')[rowIndex];
				var header = this.basicHeaders[colIndex];
				cell.children().first().remove();
				this.basicHeaders[colIndex].renderType.render.call(this, rowIndex, colIndex, newValue, header, cell);
				if (this.autoRowHeight)
					this.adjustRowHeight(rowIndex, cell);
				// 如果该cell原先处于选中行要将此cell的背景重新设置为选中行样式
				var isOdd = this.isOdd(rowIndex);
				if (header.isFixedHeader) {
					if (this.selectedRowIndice != null && this.selectedRowIndice.indexOf(rowIndex) != -1)
						cell.attr('class',isOdd ? "fixed_gridcell_odd cell_select" : "fixed_gridcell_even cell_select");
					else
						cell.attr('class',isOdd ? "fixed_gridcell_odd" : "fixed_gridcell_even") ;
				} else {
					if (this.selectedRowIndice != null && this.selectedRowIndice.indexOf(rowIndex) != -1)
						cell.attr('class',isOdd ? "gridcell_odd cell_select" : "gridcell_even cell_select");
					else
						cell.attr('class',isOdd ? "gridcell_odd" : "gridcell_even");
				}
				cell.data('isErrorCell',false);
				// 处理列分组
				if (this.basicHeaders[colIndex].isGroupBy == true) {
					this.changeGroupCellStyle(rowIndex, colIndex);
				}
				if (header.editorType == $.editortype.REFERENCE) {
					if (this.compsMap != null) {
						var comp = this.compsMap.get($.editortype.REFERENCE + colIndex);
						if (comp != null && comp.visible) {
							if (this.currActivedCell != null && this.currActivedCell[0] == cell[0])
								comp.setValue(newValue);
						}
					}
				}
				// 设置行状态(行状态为更新行才设置行状态图标为更新状态,新增行cell数据的改变不变化行状态图标)
				if (this.model != null && this.model.getRow(rowIndex).pageData.state == $.datasetrow.STATE_UPD)
					this.lineStateColumDiv.data('cells')[rowIndex].attr('class',"row_state_div row_update_state"); 
			}
			// cell值改变后通知用户
			this.onCellValueChanged(rowIndex, colIndex, oldValue, newValue);
		},
		/**
		 * 数据改变后，校正分组显示
		 * @param {} rowIndex
		 * @param {} colIndex
		 */
		changeGroupCellStyle : function(rowIndex, colIndex) {
			if (this.model.rows.length < rowIndex)
				return;
			var preRow = null;
			var currentRow = null;
			var nextRow = null;
			if (rowIndex != 0)
				preRow = this.model.rows[rowIndex - 1];
			if (rowIndex != (this.model.rows.length - 1))
				nextRow = this.model.rows[rowIndex + 1];
			currentRow = this.model.rows[rowIndex];
			var currentCell = this.basicHeaders[colIndex].dataDiv.data('cells')[rowIndex];
			if (preRow != null) {
				var preCell = this.basicHeaders[colIndex].dataDiv.data('cells')[rowIndex - 1];
				// 与上一行相同
				if (preRow.getCellValue(colIndex) == currentRow.getCellValue(colIndex)) {
					preCell.css({
						'border-bottom-color' : '#ffffff',
						'background-color' : '#ffffff'
					});
					currentCell.css('background-color','#ffffff');
					// 如果currentCell有子节点，先去掉再渲染
					if (currentCell.children().length > 0)
						currentCell.children().first().remove();
					this.basicHeaders[colIndex].renderType.render.call(this, rowIndex, colIndex, null, this.basicHeaders[colIndex], currentCell, null);
					if (this.autoRowHeight)
						this.adjustRowHeight(rowIndex, currentCell);
				}
				// 与上一行不同
				else {
					preCell.css('border-bottom-color',"");
					if (rowIndex - 2 >= 0) {
						var pre2Row = this.model.rows[rowIndex - 2];
						if (preRow.getCellValue(colIndex) == pre2Row.getCellValue(colIndex)) {
							preCell.css('background-color','#ffffff');
						} else {
							preCell.css('background-color','');
						}
					} else {
						preCell.css('background-color','');
					}
					currentCell.css('background-color','');
				}
			}
			if (nextRow != null) {
				var nextCell = this.basicHeaders[colIndex].dataDiv.data('cells')[rowIndex + 1];
				// 如果nextCell有子节点，先去掉再渲染
				if (nextCell.children().length > 0)
					nextCell.children().first().remove();
				// 与下一行相同
				if (nextRow.getCellValue(colIndex) == currentRow.getCellValue(colIndex)) {
					currentCell.css({
						'border-bottom-color' : '#ffffff',
						'background-color' : '#ffffff'
					});
					nextCell.css('background-color','#ffffff');
					this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, null, this.basicHeaders[colIndex], nextCell, null);
					if (this.autoRowHeight)
						this.adjustRowHeight(rowIndex + 1, nextCell);
				}
				// 与下一行不同
				else {
					currentCell.css('border-bottom-color','');
					if (rowIndex - 1 >= 0) {
						var preRow = this.model.rows[rowIndex - 1];
						if (preRow.getCellValue(colIndex) == currentRow.getCellValue(colIndex)) {
							currentCell.css('background-color','#ffffff');
						} else {
							currentCell.css('background-color','');
						}
					} else {
						currentCell.css('background-color','');
					}
					var next2Row = this.model.rows[rowIndex + 2];
					if (next2Row != null) {
						if (nextRow.getCellValue(colIndex) == next2Row.getCellValue(colIndex)) {
							nextCell.css('background-color','#ffffff');
						} else {
							nextCell.css('background-color','');
						}
					} else {
						nextCell.css('background-color','');
					}
					var value = nextRow.getCellValue(colIndex);
					this.basicHeaders[colIndex].renderType.render.call(this, rowIndex + 1, colIndex, value, this.basicHeaders[colIndex], nextCell, null);
					if (this.autoRowHeight)
						this.adjustRowHeight(rowIndex + 1, nextCell);
				}
			}
		},
		/**
		 * 获取grid当前的显示列
		 * @return 显示列id的数组
		 */
		getVisibleColumnIds : function() {
			if (!this.model)
				return null;
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return null;
			var visibleColumns = [];
			for (var i = 0, count = headers.length; i < count; i++) {
				if (headers[i].isHidden == false)
					visibleColumns.push(headers[i].keyName);
			}
			return visibleColumns;
		},
		/**
		 * 获取grid当前的隐藏列
		 * @return 隐藏列id的数组
		 */
		getHiddenColumnIds : function() {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return null;
			var hiddenColumns = [];
			for (var i = 0, count = headers.length; i < count; i++) {
				if (headers[i].isHidden)
					hiddenColumns.push(headers[i].keyName);
				return hiddenColumns;
			}
			return hiddenColumns;
		},
		/**
		 * 设置要显示的列
		 * @param columnIds 要显示的列的id数组
		 */
		setShowColumns : function(columnIds) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			// 全部隐藏
			if (columnIds == null || columnIds.length == 0) {
				for (var i = 0, count = headers.length; i < count; i++)
					headers[i].isHidden = true;
			}
			// 按指定列隐藏
			else {
				for (var i = 0, count = headers.length; i < count; i++) {
					headers[i].isHidden = columnIds.indexOf(headers[i].keyName) == -1;
				}
			}
			this.setModel(this.model);
			// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
			this.paintData();
		},
		/**
		 * 根据ID取列
		 * @param columnId 要取的列ID
		 */
		getBasicHeaderById : function(columnId) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0 || columnId == null)
				return;
			for (var i = 0, count = headers.length; i < count; i++) {
				if (headers[i].keyName == columnId)
					return headers[i];
			}
			return;
		},
		getGroupHeaderById : function(columnId) {
			var headers = this.model.headers;
			if (headers == null || headers.length == 0 || columnId == null)
				return;
			for(var i = 0, count = headers.length; i < count; i++) {
				if(headers[i].children && headers[i].children.length>0 && headers[i].keyName == columnId) {
					return headers[i];
				}
			}
		},
		/**
		 * 设置要锁定的列
		 * @param columnIds 要锁定的列的id数组
		 */
		setFixedColumns : function(columnIds) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			if (columnIds == null || columnIds.length == 0) {
				for (var i = 0, count = headers.length; i < count; i++)
					headers[i].isFixedHeader = false;
			} else {
				for (var i = 0, count = headers.length; i < count; i++) {
					headers[i].isFixedHeader = columnIds.indexOf(headers[i].keyName) != -1;
				}
			}
			this.setModel(this.model);
			// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的绘制了界面显示的区域.必须设置重新画一下
			this.paintZone();
		},
		/**
		 * 设置参数指定列的显示隐藏属性
		 * @param{Array} columns 列显示隐藏数组
		 */
		setColumnVisible : function(columns) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			var hasChanged = false;
			for (var i = 0; i < headers.length; i++) {
				var header = headers[i];
				// columns: [columName:visible]
				if (columns.indexOf(header.keyName + ":" + String(header.isHidden)) != -1) {
					header.isHidden = (!header.isHidden);
					// 修改group的显示属性(如果group中的所有子都隐藏，group也设为隐藏)
					if (header.isGroupHeader) {
						if (header.isHidden) {
							var groupHeader = header.topHeader;
							if (groupHeader != null && groupHeader.isHidden == false) {
								var childrenHeaders = groupHeader.allChildrenHeader;
								var allHidden = true;
								for (var j = 0; j < childrenHeaders.length; j++) {
									if (childrenHeaders[j].isHidden == false) {
										allHidden = false;
										break;
									}
								}
								if (allHidden == true) {
									groupHeader.isHidden = true;
								}
							}
						} else {
							var groupHeader = header.topHeader;
							if (groupHeader != null && groupHeader.isHidden == true) {
								groupHeader.isHidden = false;
							}
						}
					}
					hasChanged = true;
				}
			}
			if (hasChanged) {
				this.model.rows = null;
				this.model.initUIRows();
				this.setModel(this.model);
				// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
			}
		},
		/**
		 * 设置参数指定列的显示背景颜色属性
		 * @param {} columns
		 */
		setColumnBgcolor : function(columns) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			var hasChanged = false;
			for (var i = 0; i < headers.length; i++) {
				var header = headers[i];
				for (var j = 0; j < columns.length; j++) {
					var column = columns[j];
					var attrValue = column.split(":");
					var name = attrValue[0];
					var value = attrValue[1];
					if (header.keyName == name) {
						if (header.isGroupHeader == true) {
							var childrenHeaders = groupHeader.allChildrenHeader;
							for (var k = 0; k < childrenHeaders.length; k++) {
								childerHeaders[k].columBgColor = value;
							}
						} else {
							header.columBgColor = value;
						}
						hasChanged = true;
						break;
					}
				}
			}
			if (hasChanged) {
				this.paintData();
			}
		},
		/**
		 * 设置columnTextColor
		 * @param {} columns
		 */
		setColumnTextcolor : function(columns) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			var hasChanged = false;
			for (var i = 0; i < headers.length; i++) {
				var header = headers[i];
				for (var j = 0; j < columns.length; j++) {
					var column = columns[j];
					var attrValue = column.split(":");
					var name = attrValue[0];
					var value = attrValue[1];
					if (header.keyName == name) {
						if (header.isGroupHeader == true) {
							var childrenHeaders = groupHeader.allChildrenHeader;
							for (var k = 0; k < childrenHeaders.length; k++) {
								childerHeaders[k].textColor = value;
							}
						} else {
							header.textColor = value;
						}
						hasChanged = true;
						break;
					}
				}
			}
			if (hasChanged) {
				this.paintData();
			}
		},
		/**
		 * 设置参数指定列的editable属性
		 * @param{Array} columns 列editable数组，格式: [columName:editable]
		 */
		setColumnEditable : function(columns) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			for (var i = 0; i < headers.length; i++) {
				var header = headers[i];
				if (columns.indexOf(header.keyName + ":" + String(!header.columEditable)) != -1) {
					header.columEditable = (!header.columEditable);
					if (header.columEditable == true && this.editable == false) {
						// 根据model编辑属性设置grid的编辑属性
						if (this.model.dataset != null && this.model.dataset.editable)
							this.setEditable(true);
					}
				}
				// 因为grid的setEditable()方法在调用的时候激活了所有的BooleanRender，所以在这里要重新设置
				if (header.renderType == BooleanRender) {
					if (header.columEditable) {
						if (header.selectBox != null)
							header.selectBox.prop('disabled',false);
						if (header.dataDiv != null && header.dataDiv.data('cells') != null) {
							for (var j = 0; j < header.dataDiv.data('cells').length; j++) {
								if (header.dataDiv.data('cells')[j])
									header.dataDiv.data('cells')[j].children(':first').prop('disabled',false);
							}
						}
					} else {
						if (header.selectBox != null)
							header.selectBox.prop('disabled',true);
						if (header.dataDiv != null && header.dataDiv.data('cells') != null) {
							for (var j = 0; j < header.dataDiv.data('cells').length; j++) {
								if (header.dataDiv.data('cells')[j])
									header.dataDiv.data('cells')[j].children(':first').prop('disabled',true);
							}
						}
					}
				}
			}
		},
		/**
		 * 设置列头全选框隐藏/显示
		 * @param keyName 列值名称
		 * @param visible 显示隐藏属性 true/false
		 */
		setHeaderCheckBoxVisible : function(keyName, visible) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			for (var i = 0; i < headers.length; i++) {
				if (headers[i].keyName == keyName) {
					// 判断是否存在全选框
					if (headers[i].selectBox) {
						visible = $.argumentutils.getBoolean(visible, true);
						if (visible)
							headers[i].selectBox.show();
						else
							headers[i].selectBox.hide();
					}
					break;
				}
			}
		},
		/**
		 * 根据header中设置的显示状态和当前状态判断是否应该显示
		 */
		showByState : function(state) {
			var headers = this.model.basicHeaders;
			if (headers == null || headers.length == 0)
				return;
			var hasChanged = false;
			for (var i = 0, count = headers.length; i < count; i++) {
				if (headers[i].showState == null)
					continue;
				if (headers[i].showState != state) {
					if (headers[i].isHidden == false) {
						headers[i].isHidden = true;
						hasChanged = true;
					}
				} else {
					if (headers[i].isHidden == true) {
						headers[i].isHidden = false;
						hasChanged = true;
					}
				}
			}
			if (!hasChanged)
				return;
			this.setModel(this.model);
			this.paintZone();
		},
		/**
		 * 设置此Grid的model，重画表头和所有行数据
		 * @param model grid数据集
		 */
		setModel : function(model) {
			// 初始化设置model
			if (this.model == null) {
				this.model = model;
				// 将grid对象绑定在model的owner属性上
				this.model.owner = this;
				// 将model的basicHeaders保存到grid的basicHeaders中
				this.basicHeaders = this.model.basicHeaders;
				this.create();
			} else {
				if (this.model != model) {
					this.model.dataset.unbindComponent(this.model);
					delete this.model;
					this.model = model;
					this.model.owner = this;
				}
				this.basicHeaders = this.model.basicHeaders;
				this.paintData();
			}
			if (this.pageSize > 0)
				this.setPaginationInfo();
			// 计算所有合计列的值
			if (this.isShowSumRow) {
				this.model.setSumColValue(null, null);
			}
			// 根据model编辑属性设置grid的编辑属性
			if (this.model.dataset != null && this.model.dataset.isEditable() == false)
				this.setEditable(false);
			else if (this.model.dataset != null && this.model.dataset.editable && this.model.dataset.editableChanged)
				this.setEditable(true);
		},
		/**
		 * 删除参数节点下所有子节点
		 */
		removeAllChildren : function(p) {
			if (p) {
				while (p.children().length > 0)
					p.children(':eq(0)').remove();
			}
		},
		/**
		 * 根据header的数据类型解析data
		 */
		parseData : function(header, data, isComputeSum) {
			if (header.dataType == $.datatype.BOOLEAN) {
				var formater;
				if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
					formater = $.booleanformater.getObj();
					FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
				}
				return formater.format(data);
			} else if (header.dataType == $.datatype.CHOOSE) {
				if (header.comboData == null)
					return data;
				var keyValues = header.comboData.getValueArray();
				var captionValues = header.comboData.getNameArray();
				var index = keyValues.indexOf(data);
				if (index != -1)
					return captionValues[index];
				return "";
			} else if (header.dataType == $.datatype.DATE) {
				var formater;
				if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
					formater = $.dateformater.getObj();
					FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
				}
				return formater.format(data);
			} else if (header.dataType == $.datatype.INTEGER) {
				var formater;
				if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
					formater = $.integerformater.getObj(header.integerMinValue, header.integerMaxValue);
					FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
				}
				return formater.format(data);
			} else if (header.dataType == $.datatype.Decimal
					|| header.dataType == $.datatype.FLOAT
					|| header.dataType == $.datatype.fLOAT
					|| header.dataType == $.datatype.FDOUBLE
					|| header.dataType == $.datatype.dOUBLE) {
				var formater;
				if (isComputeSum) {
					formater = $.dicimalformater.getObj(header.precision, null, null);
				} else {
					if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
						formater = $.dicimalformater.getObj(header.precision, header.floatMinValue, header.floatMaxValue);
						FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
					}
				}
				// 如果decimal精度变化则重新设置formatter精度
				if (formater.precision != header.precision)
					formater.precision = header.precision;
				return formater.format(data);
			} else {
				var formater;
				if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
					formater = $.formater.getObj();
					FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
				}
				return formater.format(data);
			}
		},
		/**
		 * 获取对象信息
		 */
		getContext : function() {
			var context = new Object;
			context.c = "GridContext";
			context.id = this.id;
			context.enabled = this.isGridActive == null || this.isGridActive == true;
			context.editable = this.editable;
			context.multiSelect = this.isMultiSelWithBox;
			if (this.model && this.model.owner && this.model.owner.selectedCell && this.model.owner.selectedCell != null) {
				var currColIndex = this.model.owner.selectedCell.colIndex;
				if (this.model.rows && this.model.rows[0] != null) {
					var filedName = this.model.rows[0].getFiledNameByColIndex(currColIndex);
					context.currentColID = filedName;
				}
			}
			var showColumns = this.getVisibleColumnIds();
			if (showColumns)
				context.showColumns = showColumns.join(",");
			return context;
		},
		/**
		 * 设置对象信息
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.editable != null && (this.editable == null || (this.editable != null && this.editable != context.editable)))
				this.setEditable(context.editable);
			if (context.multiSelect != null)
				this.setMultiSelect(context.multiSelect);
			if (context.showColumns != null && context.showColumns != "")// 显示列（用“,”分割的字符串）
				this.setShowColumns(context.showColumns.split(","));
			// 重新setContext后需要重新设置分页
			if (this.pageSize > 0)
				this.setPaginationInfo();
		},
		nextMouseOver : function(e) {
			e.preventDefault();
			e.stopPropagation();
			$(this).attr('class','nextover').children(':first').attr('class','next_over_img');
		},
		nextMouseOut : function(e) {
			e.preventDefault();
			e.stopPropagation();
			$(this).attr('class','next').children(':first').attr('class','next_img');
		},
		preMouseOver : function(e) {
			e.preventDefault();
			e.stopPropagation();
			$(this).attr('class','preover').children(':first').attr('class','pre_over_img');
		},
		preMouseOut : function(e) {
			e.preventDefault();
			e.stopPropagation();
			$(this).attr('class','pre').children(':first').attr('class','pre_img');
		},
		pagePre : function(e) {
			e.preventDefault();
			e.stopPropagation();
			var grid = $(this).data('gridId');
			if (grid.pageIndex == 0)
				return;
			grid.processServerPagination(grid.pageIndex - 1);
		},
		pageFirst : function(e) {
			e.preventDefault();
			e.stopPropagation();
			var grid = $(this).data('gridId');
			if (grid.pageIndex == 0)
				return;
			grid.processServerPagination(0);
		},
		pageNext : function(e) {
			e.preventDefault();
			e.stopPropagation();
			var grid = $(this).data('gridId');
			if (grid.pageIndex == grid.model.getPageCount() - 1)
				return;
			grid.processServerPagination(grid.pageIndex + 1);
		},
		pageLast : function(e) {
			e.preventDefault();
			e.stopPropagation();
			var grid = $(this).data('gridId');
			if (grid.pageIndex == grid.model.getPageCount() - 1)
				return;
			grid.processServerPagination(grid.model.getPageCount() - 1);
		},
		/**
		 * paint所有重新设定的model数据
		 */
		paintData : function() {
			// 清空原来数据显示页
			this.clearDivs();
			if (this.model == null)
				return;
			// 初始化构建框架所需的各个常量
			this.initConstant();
			// 根据model初始化header
			this.initBasicHeaders();
			// 判断初始化界面时是否会出现横向、纵向滚动条
			this.adjustScroll();
			// 初始化基础框架
			this.initDivs();
			// 根据model初始化数据
			this.initDatas();
			// 真正的画界面
			this.paintZone();
			// 各区域的事件处理
			this.attachEvents();
			// 当前选中行索引
			this.selectedRowIndice = null;
			this.currActivedCell = null;
			if (this.showComp)
				this.hiddenComp();
		},
		/**
		 * 创建最外层包容div
		 */
		initOuterDiv : function() {
			if (this.outerDiv) {
				return;
			}
			var oThis = this;
			this.outerDiv = $("<div>").addClass(this.className).css({
				'left' : this.left,
				'top' : this.top,
				'width' : this.width
			});
			if (!this.isRunMode) {
				this.outerDiv.css("overflow-x", "auto");
			}
			if (!this.flowmode) {
				this.outerDiv.css("overflow", "hidden");
			}
			this.outerDiv.on('scroll',function(e){
				// 修改编辑控件位置
				if (oThis.currActivedCell && oThis.showComp) {
					oThis.showComp.setBounds(
							oThis.currActivedCell.offset().left + $.grid.CELL_LEFT_PADDING,
							oThis.currActivedCell.offset().top,
							oThis.currActivedCell.outerWidth() - $.grid.CELL_RIGHT_PADDING - 1,
							oThis.currActivedCell.outerHeight());
					oThis.showComp.setFocus();
				}
			});
			this.wholeDiv.append(this.outerDiv);
		},
		/**
		 * 画区域
		 */
		paintZone : function(key, rowIndex, hasParent, level) {
			if (this.model == null)
				return;
			if (this.needPaintRows != null && this.needPaintRows == true) {
				this.needPaintRows = false;
				this.paintRows();
			} else {
				if (rowIndex == null)
					rowIndex = 0;
				// 对每行进行处理
				var rows = this.model.getRows(key);
				if (rows == null || rows.length == 0)
					return;
				var modelLen = rows.length;
				var scrollLeft = this.dataOuterDiv.scrollLeft();
				var rowHeihgt = this.rowHeight;
				var rowCount = this.model.getRowsCount();
				this.setHeadersOffsetWidth();
				initLayoutMonitorState();
				for (var i = 0; i < modelLen; i++) {
					if (hasParent) {
						if (level != null) {
							rows[i].level = level + 1;
							this.addOneRow(rows[i], rowIndex + i, scrollLeft, rowHeihgt, rowCount, rowIndex - 1);
						}
					} else
						this.addOneRow(rows[i], rowIndex + i, scrollLeft, rowHeihgt, rowCount, null);
					// GridRowsForLigerui.push(rows[i].pageData.contentJson);
				}
				this.clearHeadersOffsetWidth();
				setTimeout("executeLayoutMonitor()", 500);
			}
			// 调整固定列高度
			this.adjustFixedColumDivHeight();
		},
		/**
		 * 清除grid所有框架div
		 */
		clearDivs : function() {
			this.removeAllChildren(this.outerDiv);
			if (this.dataOuterDiv != null) {
				this.dataOuterDiv.css('width','0px');
			}
		},
		/**
		 * 初始化grid中的各个框架结构
		 */
		initDivs : function() {
			// 创建表头区整体div
			this.initHeaderDiv();
			// 创建固定表头区整体div
			this.initFixedHeaderDiv();
			if (this.isShowNumCol && this.isHHeader()) {
				// 创建行号列表头div
				this.initRowNumHeaderDiv();
			} else
				this.constant.rowNumHeaderDivWidth = 0;
			// 创建表格行状态显示列header
			this.initLineStateHeaderDiv();
			// 创建固定表头区固定选择列
			if (this.isMultiSelWithBox)
				this.initSelectColumHeaderDiv();
			// 创建固定表头区header数据区div
			this.initFixedHeaderTableDiv();
			// 创建动态表头区整体div
			this.initDynamicHeaderDiv();
			// 创建动态表头区header数据区div
			this.initDynamicHeaderTableDiv();
			// 创建提示信息区
			if (this.isRunMode) {
				this.initNoRowsDiv();
			}
			// 创建固定列整体div
			this.initFixedColumDiv();
			// 创建数据区整体div
			this.initDataOuterDiv();
			if (this.isShowNumCol && this.isHHeader()) {
				// 画行数字行标列div
				this.initRowNumDiv();
			}
			// 创建固定选择列
			if (this.isMultiSelWithBox && this.isHHeader())
				this.initSelectColumDiv();
			// 创建行状态显示列
			this.initLineStateColumDiv();
			// 创建浮动在最下面的合计行
			if (this.isShowSumRow) {
				this.initSumPageDataDiv();
			}
			if (this.pageSize > 0) {
				if (this.isSimplePagination) {
					this.initSimplePaginationBar();
				} else {
					this.initPaginationBar();
				}
			}
		},
		/**
		 * 根据model中的数据初始化界面数据
		 */
		initDatas : function() {
			// 根据headers model画headers内的table(数据)
			this.initHeaderTables();
			this.initFixedColumDataDiv();
			// 画动态列数据区的数据外层包装div及每列数据div
			this.initDynamicColumDataDiv();
			// 初始化合计行中的每个cell单元
			if (this.isShowSumRow)
				this.initSumRowCells();
		},
		/**
		 * model改变后调用此方法重画所有行数据
		 * @param sort 表示是排序时重画，不需要重新initUIRows
		 */
		paintRows : function(sort, startIndex, count) {
			if (this.isMultiSelWithBox) {
				if (this.selectColumDiv != null)
					this.selectColumDiv.remove();
			}
			if (this.dynamicColumDataDiv != null)
				this.dynamicColumDataDiv.remove();
			if (this.fixedColumDiv != null)
				this.fixedColumDiv.empty();
			// 移除行标列
			if (this.isShowSumRow && this.rowNumDiv != null) {
				this.rowNumDiv.remove();
				this.rowNumDiv.data('cells',null);
			}
			// 重新paintRows时，可能原来有滚动条，现在没有了，要重算 this.constant.outerDivWidth
			this.constant.outerDivWidth = this.wholeDiv.outerWidth();
			var fixedColumDivWidth = 0;
			if (this.fixedColumDiv)
				fixedColumDivWidth = this.fixedColumDiv.outerWidth();
			if(this.isHHeader()) {
				this.dataOuterDiv.css('width',this.constant.outerDivWidth - fixedColumDivWidth + "px");
			} else {
				this.dataOuterDiv.css('width',this.constant.outerDivWidth - this.constant.headerWidth + "px");
			}
			// 将model的rows置为null,需要重新取新数据
			if (!sort) {
				this.model.rows = null;
				if (startIndex != null && count != null)
					this.model.initUIRows(startIndex, count);
				else
					this.model.initUIRows(startIndex);
			}
			var rowCount = this.getRowsNum();
			// 当前选中行索引
			this.selectedRowIndice = null;
			this.currActivedCell = null;
			// 重画行时隐藏掉当前显示的控件
			if (this.showComp)
				this.hiddenComp();
			var gridWidth = this.constant.outerDivWidth;
			var fixedHeaderDivWidth = this.constant.fixedHeaderDivWidth;
			// 调整表头的宽度
			if (this.isVScroll()) {
				var _headrDivWidth = this.isHHeader() ? (gridWidth - $.grid.SCROLLBAE_HEIGHT) : this.constant.headerWidth;
				this.headerDiv.css({
					'width' : _headrDivWidth + "px",
					'left' : "0px"
				});
				this.headerDiv.data('defaultWidth',_headrDivWidth);
				this.fixedHeaderDiv.css('left','0px');
				var dynHeaderWidth = this.isHHeader() ? (gridWidth - fixedHeaderDivWidth - $.grid.SCROLLBAE_HEIGHT) : this.constant.headerWidth;
				if (dynHeaderWidth > 0)
					this.dynamicHeaderDiv.css('width',dynHeaderWidth + "px");
				this.dynamicHeaderDiv.data('defaultWidth',dynHeaderWidth);
			} else {
				var _width = this.isHHeader() ? gridWidth : this.constant.headerWidth;
				this.headerDiv.css({
					'width' : _width + "px",
					'left' : "0px"
				});
				this.headerDiv.data('defaultWidth',_width);
				this.fixedHeaderDiv.css('left',"0px");
				var _dynWidth = this.isHHeader() ? (gridWidth - fixedHeaderDivWidth) : this.constant.headerWidth;
				this.dynamicHeaderDiv.css('width',_dynWidth + "px");
				this.dynamicHeaderDiv.data('defaultWidth',_dynWidth);
			}
			if (this.isScroll())
				this.setScrollLeft(0);
			// 画动态列数据区的数据外层包装div及每列数据div
			this.initDynamicColumDataDiv();
			// 画数字行标区
			if (this.isShowNumCol)
				this.initRowNumDiv();
			// 画行状态区
			this.initLineStateColumDiv();
			// 画固定选择列
			if (this.isMultiSelWithBox)
				this.initSelectColumDiv();
			this.initFixedColumDataDiv();
			this.paintZone();
			if (this.stForAutoExpand != null)
				clearTimeout(this.stForAutoExpand);
			var oThis = this;
			this.stForAutoExpand = setTimeout(function() {
				$.grid.processAutoExpandHeadersWidth(oThis);
			}, 100);
		},
		/**
		 * 激活编辑框，进入编辑状态
		 */
		setGridInEdit : function() {
			if (this.editable == false)
				return;
			var ds = this.model.dataset;
			if (ds == null)
				return;
			if (ds.editable == false)
				return;
			var rowIndex = ds.getSelectedIndex();
			if (rowIndex == -1)
				return;
			var colIndex = -1;
			var headers = this.model.getHeaders();
			for (var i = 0; i < headers.length; i++) {
				if (headers[i].isHidden == false && headers[i].columEditable == true) {
					colIndex = i;
					break;
				}
			}
			if (colIndex == -1)
				return;
			var cell = this.getCell(rowIndex, colIndex);
			if (cell == null)
				return;
			this.hiddenComp();
			this.setCellSelected(cell);
		},
		hideChildRows : function(rowIndex) {
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true)
					continue;
				var dataDiv = header.dataDiv;
				var _columns = dataDiv.children();
				var cell = $(_columns[rowIndex]);
				var parentCell = [];
				for (var j = rowIndex + 1; j < _columns.length; j++) {
					var cCell = $(_columns[j]);
					if (cCell.data('parentCell') && cCell.data('parentCell')[0] == cell[0]) {
						// 递归的先收起子，再收起本行
						this.hideChildRows(j);
						// 隐藏左侧checkbox
						if (this.selectColumDiv != null)
							this.selectColumDiv.data('cells')[j].hide();
						cCell.hide();
						parentCell.push(cCell);
					} else {
						if (parentCell.length > 0) {
							for (var k = 0; k < parentCell.length; k++) {
								if (cCell.data('parentCell') && cCell.data('parentCell')[0] == parentCell[k][0]) {
									cCell.hide();
									parentCell.push(cCell);
									break;
								}
							}
						}
					}
				}
			}
		},
		showChildRows : function(rowIndex) {
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true)
					continue;
				var dataDiv = header.dataDiv;
				var cell = dataDiv.children(':eq('+rowIndex+')');
				for (var j = rowIndex + 1; j < dataDiv.children().length; j++) {
					var cCell = dataDiv.children(':eq('+j+')');
					if (cCell.is(':visible'))
						break;
					var gridRow = this.model.getRow(j);
					if (gridRow.loadImg.data('plus') == false) {
						gridRow.loadImg.data('plus',true);
						gridRow.loadImg.attr('src',DefaultRender.plusImgSrc);
					}
					if (cCell.data('parentCell') && cCell.data('parentCell')[0] == cell[0]) {
						// 显示左侧checkbox
						if (this.selectColumDiv != null)
							this.selectColumDiv.data('cells')[j].show();
						cCell.show();
					}
				}
			}
		},
		loadChild : function(fk, rowIndex, level) {
			this.paintChild(fk, rowIndex, true, level);
			var pRow = this.model.getRow(rowIndex - 1);
			pRow.loadedChild = true;
			this.refreshRowIndexs();
		},
		refreshRowIndexs : function() {
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true)
					continue;
				header.dataDiv.children().each(function(i){//所有列
					$(this).data('rowIndex', i);//重新给rowIndex赋值
				});
			}
		},
		paintChild : function(key, rowIndex, hasParent, level) {
			this.model.initUIRows(rowIndex, null, key, level);
			this.paintZone(key, rowIndex, hasParent, level);
		},
		/**
		 * 展开所有树节点
		 */
		expandAllNodes : function() {
			if (this.model.treeLevel == null)
				return;
			for (var i = 0; i < this.model.rows.length; i++) {
				var gridRow = this.model.rows[i];
				if (gridRow.loadImg.data('plus')) {
					gridRow.loadImg.triggerHandler('click');
				}
			}
		},
		/**
		 * 勾选本行和所有子行的checkbox,先触发展开节点方法,再递归勾选
		 * @param index:父行的行号
		 */
		expandAndSeclectNodesByRowIndex : function(index) {
			this.model.addRowSelected(index);
			var gridRow = this.model.rows[index];
			if (gridRow == null || gridRow.loadImg == null
					|| gridRow.loadImg.data('plus') == null
					|| typeof(gridRow.loadImg) == "undefined"
					|| typeof(gridRow.loadImg.data('plus')) == "undefined") {
				return;
			}
			if (gridRow.loadImg) {
				if (gridRow.loadImg.data('plus')) {
					// 触发展开事件
					gridRow.loadImg.data('byCheckBox',true);
					gridRow.loadImg.triggerHandler('click');
				}
			}
			if (this.model.treeLevel == null)
				return;
			var childrenRows = [];
			// 得到子级节点的索引
			childrenRows = this.getChildrenRowsByRowIndex(index);
			if (childrenRows != null && childrenRows.length > 0) {
				for (var i = 0; i < childrenRows.length; i++) {
					var index = childrenRows[i];
					// 递归调用展开子级
					this.expandAndSeclectNodesByRowIndex(index);
				}
			}
		},
		expandNodesByRowIndex : function(index) {
			var gridRow = this.model.rows[index];
			if (gridRow.loadImg.data('plus') == true) {
				gridRow.loadImg.triggerHandler('click');
			}
			if (this.model.treeLevel == null)
				return;
			var childrenRows = [];
			childrenRows = this.getChildrenRowsByRowIndex(index);
			if (childrenRows != null && childrenRows.length > 0) {
				for (var i = 0; i < childrenRows.length; i++) {
					var index = childrenRows[i];
					this.expandNodesByRowIndex(index);
				}
			}
		},
		/**
		 * 勾掉本行和本行的所有子行的checkbox,递归调用
		 * @param index:父行的行号
		 */
		unselectNodesByRowIndex : function(index) {
			this.model.setRowUnSelected(index);
			if (this.model.treeLevel == null)
				return;
			var childrenRowIndexs = this.getChildrenRowsByRowIndex(index);
			if (childrenRowIndexs != null && childrenRowIndexs.length > 0) {
				for (var k = 0; k < childrenRowIndexs.length; k++) {
					this.unselectNodesByRowIndex(childrenRowIndexs[k]);
				}
			}
		},
		/**
		 * 获取本行的下一级子节点的行号集合
		 * @param rowIndex：父行行号
		 */
		getChildrenRowsByRowIndex : function(rowIndex) {
			var childrenRows = [];
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true) {
					continue;
				} else {
					var dataDiv = header.dataDiv;
					var cell = dataDiv.children(':eq('+rowIndex+')');
					for (var j = rowIndex + 1; j < dataDiv.children().length; j++) {
						var cCell = dataDiv.children('eq('+j+')');
						if (cCell.data('parentCell')[0] == cell[0]) {
							childrenRows.push(j);
						}
					}
					break;
				}
			}
			return childrenRows;
		},
		/**
		 * 调整行高
		 */
		adjustRowHeight : function(rowIndex, cell) {
			cell.css({
				'text-overflow' : '',
				'white-space' : 'normal',
				'word-wrap' : 'break-word',
				'line-height' : '',
				'overflow' : 'auto',
				'min-height' : ''
			});
			var height = cell.outerHeight();
			cell.data('realHeight',height);
			var defaultHeight = this.defaultRowMinHeight[rowIndex] == null ? $.grid.ROW_HEIGHT : this.defaultRowMinHeight[rowIndex];
			// 行高变小
			if (height < this.rowMinHeight[rowIndex]) {
				var maxHeight = defaultHeight;
				for (var i = 0; i < this.model.basicHeaders.length; i++) {
					var header = this.model.basicHeaders[i];
					if (header.isHidden == true)
						continue;
					if (header.dataDiv.data('cells')[rowIndex]) {
						if (header.dataDiv.data('cells')[rowIndex].data('realHeight') > maxHeight)
							maxHeight = header.dataDiv.data('cells')[rowIndex].data('realHeight');
					}
				}
				this.setRowMinHeight(rowIndex, maxHeight);
			} else if (height == this.rowMinHeight[rowIndex]) {
				cell.css('min-height',this.rowMinHeight[rowIndex] + "px");
			}
			// 行高变大
			else
				this.setRowMinHeight(rowIndex, height);
		},
		/**
		 * 设置一行的最小高度
		 * @param {} rowIndex
		 * @param {} height
		 */
		setRowMinHeight : function(rowIndex, height) {
			this.rowMinHeight[rowIndex] = height;
			if (this.lineStateColumDiv) {
				this.lineStateColumDiv.data('cells')[rowIndex].css('min-height',height + "px");
			}
			if (this.selectColumDiv) {
				this.selectColumDiv.children(':eq('+ rowIndex +')').css('min-height',height + "px");
			}
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true)
					continue;
				if (header.dataDiv.data('cells')[rowIndex])
					header.dataDiv.data('cells')[rowIndex].css('min-height',height + "px");
			}
		},
		/**
		 * cell单击时用户重载方法
		 * @param cell
		 * @param rowIndex 行id
		 * @param colIndex 列id
		 */
		onCellClick : function(cell, rowIndex, colIndex) {
			var cellEvent = {
				"obj" : this,
				"cell" : cell,
				"rowIndex" : rowIndex,
				"colIndex" : colIndex
			};
			this._trigger('oncellclick',null,cellEvent);
		},
		/**
		 * cell编辑时调用该方法,用户可以重载该方法返回自定义的cell编辑器
		 */
		getCellEditor : function(cell, rowIndex, colIndex) {
			var cellEvent = {
				"obj" : this,
				"cell" : cell,
				"rowIndex" : rowIndex,
				"colIndex" : colIndex
			};
			this._trigger('celledit',null,cellEvent);
		},
		/**
		 * cell编辑完成后调用此方法
		 * @param rowIndex 行号
		 * @param colIndex 列号
		 * @param oldValue 旧值
		 * @param newValue 新值
		 */
		onAfterEdit : function(rowIndex, colIndex, oldValue, newValue) {
			var afterCellEditEvent = {
				"obj" : this,
				"rowIndex" : rowIndex,
				"colIndex" : colIndex,
				"oldValue" : oldValue,
				"newValue" : newValue
			};
			this._trigger('afteredit',null,afterCellEditEvent);
		},
		/**
		 * cell编辑前调用的方法,返回false可以阻止编辑cell
		 * @param rowIndex 行索引
		 * @param colIndex 列索引
		 */
		onBeforeEdit : function(rowIndex, colIndex) {
			var beforeCellEditEvent = {
				"obj" : this,
				"rowIndex" : rowIndex,
				"colIndex" : colIndex
			};
			this._trigger('beforeedit',null,beforeCellEditEvent);
		},
		/**
		 * cell值改变后调用此方法
		 * @param rowIndex 行号
		 * @param colIndex 列号
		 * @param oldValue 旧值
		 * @param newValue 新值
		 */
		onCellValueChanged : function(rowIndex, colIndex, oldValue, newValue) {
			var cellValueChangedEvent = {
				"obj" : this,
				"rowIndex" : rowIndex,
				"colIndex" : colIndex,
				"oldValue" : oldValue,
				"newValue" : newValue
			};
			this._trigger('cellvaluechanged',null,cellValueChangedEvent);
		},
		/**
		 * 行选中之前调用的方法
		 * @param rowIndex 选中行的索引
		 * @param{$.gridrow} row
		 */
		onBeforeRowSelected : function(rowIndex, row) {
			var rowEvent = {
				"obj" : this,
				"rowIndex" : rowIndex,
				"row" : row
			};
			this._trigger('beforerowselected',null,rowEvent);
		},
		/**
		 * 行双击时调用的方法(只有在grid整体不能编辑时该方法才有用)
		 * @param rowIndex 选中行的索引
		 * @param{$.gridrow} row
		 */
		onRowDblClick : function(rowIndex, row) {
			var rowEvent = {
				"obj" : this,
				"rowIndex" : rowIndex,
				"row" : row
			};
			this._trigger('onrowdbclick',null,rowEvent);
		},
		/**
		 * 行选中时调用的方法
		 * @param rowIndex 选中行的索引
		 */
		onRowSelected : function(rowIndex) {
			var rowSelectedEvent = {
				"obj" : this,
				"rowIndex" : rowIndex
			};
			this._trigger('onrowselected',null,rowSelectedEvent);
		},
		/**
		 * 数据区上点击鼠标右键时调用这个方法
		 */
		onDataOuterDivContextMenu : function(e) {
			var mouseEvent = {
				"obj" : this,
				"event" : e
			};
			this._trigger('ondataouterdivcontextmenu',null,mouseEvent);
		},
		/**
		 * grid上最后一个可编辑列按回车事件
		 */
		onLastCellEnter : function(e) {
			var gridEvent = {
				"obj" : this,
				"event" : e
			};
			this._trigger('onlastcellenter',null,gridEvent);
		},
		processPageCount : function(pageInfo) {
			var processPageCountEvent = {
				"obj" : this,
				"pageInfo" : pageInfo
			};
			this._trigger('processpagecount',null,processPageCountEvent);
			return pageInfo;
		},
		/**
		 * 粘贴之前的事件
		 * @param {} pasteEvent
		 */
		onBeforePaste : function(e) {
			var gridEvent = {
				"obj" : this,
				"event" : e
			};
			this._trigger('onbeforepaste',null,gridEvent);
		}
	});

	$.widget("lui.gridheader", $.lui.base, {
		options : {
			keyName : '',
			showName : '',
			width : $.grid.COlUMWIDTH_DEFAULT,
			height : $.grid.HEADERROW_HEIGHT,
			dataType : $.datatype.STRING,
			sortable : true,
			isHidden : false,
			columEditable : true,
			defaultValue : '',
			columBgColor : '',
			textAlign : '',
			textColor : '',
			isFixedHeader : false,
			renderType : null,
			editorType : $.editortype.STRINGTEXT,
			topHeader : null,
			groupHeader : null,
			isGroupHeader : false,
			isSumCol : false,
			isAutoExpand : false,
			isShowCheckBox : true,
			sumColRenderFunc : null,
			onclick : null,
			onmouseout : null,
			onmouseover : null,
			onmousedown : null,
			onmouseup : null
		},
		_initParam : function() {
			this._super();
			var heardwidth = $.argumentutils.getInteger(this.options.width, this.options.width);
			if (heardwidth < 35) {
				heardwidth = 35;
			}
			// 第三方grid数据
			this.width = heardwidth;
			this.height = $.argumentutils.getInteger(this.options.height, $.grid.HEADERROW_HEIGHT);
			this.children = null;
			this.keyName = this.options.keyName;
			this.showName = this.options.showName;
			// 此列是否默认隐藏
			this.isHidden = this.options.isHidden;
			// 初始是否是固定表头(默认为动态表头)
			this.isFixedHeader = this.options.isFixedHeader;
			// 此列的数据类型
			this.dataType = this.options.dataType;
			// 此列是否可排序
			this.sortable = this.options.sortable;
			// cell中元素的对齐方式
			this.textAlign = this.options.textAlign;
			this.columBgColor = this.options.columBgColor;
			this.textColor = this.options.textColor;
			// 此列的默认值
			this.defaultValue = this.options.defaultValue;
			this.columEditable = this.options.columEditable;
			// 值解析器.如果没有设置,则将根据数值类型调用默认解析器.
			this.parser = null;
			// 渲染器对象的引用
			this.renderType = this.options.renderType;
			// 此列的编辑类型(即是什么控件)
			this.editorType = this.options.editorType;
			// 表示该列是否是合计列
			this.isSumCol = this.options.isSumCol;
			// 合计行的渲染方式
			this.sumColRenderFunc = this.options.sumColRenderFunc;
			// 该表头是否自动扩展
			this.isAutoExpand = this.options.isAutoExpand;
			// 是否分组显示,如果分组显示,相同的一组值将只会显示第一个值
			this.isGroupBy = false;
			// 是否显示表头checkbox
			this.isShowCheckBox = this.options.isShowCheckBox;
			// 标示此header是否是多表头中的header
			this.isGroupHeader = this.options.isGroupHeader;
			if (this.isGroupHeader) {
				// 多表头最顶层表头
				this.topHeader = this.options.topHeader;
				if (this.topHeader == null || this.topHeader == "")
					this.allChildrenHeader = [];
				else
					this.topHeader.allChildrenHeader.push(this);
				if (this.options.groupHeader != null && this.options.groupHeader != "") {
					// 如果 groupHeader隐藏，子改设置为隐藏
					if (this.options.groupHeader.isHidden == true)
						this.isHidden == true;
					this.options.groupHeader.addChildHeader(this);
				}
			}
		},
		_create : function() {
			this._initParam();
		},
		/**
		 * 修改表头显示文字
		 */
		replaceText : function(text) {
			if (this.textNode && text != null) {
				this.textNode.empty().html(text);
			}
		},
		/**
		 * 设置显示状态
		 */
		setShowState : function(state) {
			this.showState = state;
		},
		/**
		 * 增加子header,多表头情况的处理
		 */
		addChildHeader : function(header) {
			if (this.children == null)
				this.children = [];
			this.children.push(header);
			// 保存此header的父header
			header.parent = this;
		},
		/**
		 * 根据传入的最父级header,返回指定层数的所有header
		 * @param level 层数从0开始
		 * @return header数组(包括隐藏列) 注:调用此方法的只能是最顶层header
		 */
		getHeadersByLevel : function(level) {
			var headers = this.getAllChildrenHeaderByLevel(level);
			return headers;
		},
		/**
		 * 得到多表头列的每一层的最左边的header(用于改变边界style)
		 * @return headers数组
		 */
		getAllLeftHeaders : function() {
			if (this.children == null)
				return;
			var depth = this.getDepth();
			var headers = [];
			var temp = null;
			for (var i = 0; i < depth; i++) {
				temp = this.getVisibleHeadersByLevel(i);
				if (temp != null && temp.length > 0)
					headers.push(temp[0]);
			}
			return headers;
		},
		/**
		 * 得到指定层的可见header,不包括隐藏列
		 * @param level 层数从0开始
		 * @return header数组 注:调用此方法的只能是最顶层header
		 */
		getVisibleHeadersByLevel : function(level) {
			// 隐藏列直接返回
			if (this.isHidden)
				return null;
			var headers = this.getAllChildrenHeaderByLevel(level);
			var temp = [];
			for (var i = 0; i < headers.length; i++) {
				if (headers[i].isHidden == false)
					temp.push(headers[i]);
			}
			headers = null;
			return temp;
		},
		/**
		 * 得到指定层的所有子header,不考虑子header是隐藏列的情况 注:任何header均可调用此方法
		 */
		getAllChildrenHeaderByLevel : function(level) {
			var temp = [];
			// 顶层header调用情况
			if (this.parent == null && this.children != null) {
				// 取第0级直接返回this
				if (level == 0) {
					temp.push(this);
					return temp;
				}
				// 取其他级
				else {
					if (this.allChildrenHeader != null && this.allChildrenHeader.length > 0) {
						for (var i = 0; i < this.allChildrenHeader.length; i++) {
							if (this.allChildrenHeader[i].getHeaderLevel() == level)
								temp.push(this.allChildrenHeader[i]);
						}
					}
				}
			}
			// basicHeader调用情况
			else if (this.parent != null && this.children == null) {
				temp.push(this);
				return temp;
			} else {
				// 得到此header的所在层数
				var currHeaderLevel = this.getHeaderLevel();
				var header = this;
				// 得到最顶层header
				while (header.parent != null)
					header = header.parent;
				for (var i = 0; i < header.allChildrenHeader.length; i++) {
					if (header.allChildrenHeader[i].getHeaderLevel() == currHeaderLevel + 1 + level)
						return header.allChildrenHeader[i].parent.children;
				}
			}
			// 返回空数组
			return temp;
		},
		/**
		 * 得到header深度
		 */
		getDepth : function() {
			return 1 + this.getMaxDepth(this);
		},
		/**
		 * 递归得到表头最大深度
		 */
		getMaxDepth : function() {
			var maxDepth = 0;
			if (this.children != null) {
				var childs = this.children;
				for (var i = 0; i < childs.length; i++) {
					if (!childs[i].isHidden) {
						var depth = 1 + childs[i].getMaxDepth();
						if (depth > maxDepth)
							maxDepth = depth;
					}
				}
			}
			return maxDepth;
		},
		/**
		 * 得到header所在的层值 注:层数从0开始
		 */
		getHeaderLevel : function() {
			var level = 0;
			if (this.parent != null)
				level = 1 + this.parent.getHeaderLevel();
			return level == 0 ? 0 : level;
		},
		/**
		 * 递归得到给定header的colspan值
		 */
		getColspan : function() {
			var w = 0;
			if (this.children != null) {
				for (var i = 0; i < this.children.length; i++) {
					if (!this.children[i].isHidden) {
						var ret = this.children[i].getColspan();
						w += ret;
					}
				}
			}
			return w == 0 ? 1 : w;
		},
		/**
		 * 得到header列最底层的全部basicHeaders(顺序为从左到右)
		 * @return basicHeaders数组 注:调用此方法的只能是顶层header
		 */
		getBasicHeaders : function() {
			var basicHeaders = [];
			if (this.children == null) {
				basicHeaders.push(this);
			} else {
				this.getBasicHeader(this, basicHeaders);
			}
			return basicHeaders;
		},
		getBasicHeader : function(header, basicHeaders) {
			for (var i = 0; i < header.children.length; i++) {
				var childrenHeader = header.children[i];
				if (childrenHeader.children == null)
					basicHeaders.push(childrenHeader);
				else
					this.getBasicHeader(childrenHeader, basicHeaders);
			}
		},
		/**
		 * 得到任意给定header的所有下挂的basicHeaders
		 */
		getBasicHeadersBySpecify : function() {
			var headers = [];
			if (this.children) {
				if (this.parent) {
					// 得到此header的孩子层数
					var childLevel = this.getHeaderChildrenLevel();
					for (var i = 0; i <= childLevel; i++) {
						var currLevelHeaders = this.getAllChildrenHeaderByLevel(i);
						for (var j = 0; j < currLevelHeaders.length; j++) {
							if (currLevelHeaders[j].children == null) {
								headers.push(currLevelHeaders[j]);
							}
						}
					}
					return headers;
				} else {
					return this.getBasicHeaders();
				}
			} else {
				headers.push(this);
				return headers;
			}
		},
		/**
		 * 此header列最底层有多少个子header 根据传入的最父级header
		 */
		getDepthestHeadersNum : function() {
			if (this.parent != null)
				return null;
			// 最顶层header的colspan的值就是最底层的header数
			return this.getColspan();
		},
		/**
		 * 得到header的rowspan值
		 * @totalDepth 此header所在table的总深度
		 */
		getRowspan : function(totalDepth) {
			var childLevel = 0;
			if (this.children)
				childLevel = this.getHeaderChildrenLevel();
			var rowspan = totalDepth - this.getHeaderLevel() - childLevel;
			return rowspan;
		},
		/**
		 * 对于多表头列求得给定一个header的孩子层数
		 */
		getHeaderChildrenLevel : function() {
			return this.getMaxDepth();
		},
		/**
		 * 如果header是符点数，则调用此方法设置最小值
		 */
		setFloatMinValue : function(minValue) {
			if (!isNaN(parseFloat(minValue))) {
				this.floatMinValue = parseFloat(minValue);
			} else
				this.floatMinValue = null;
		},
		/**
		 * 如果header是浮点数，则调用此方法设置最大值
		 */
		setFloatMaxValue : function(maxValue) {
			if (!isNaN(parseFloat(maxValue))) {
				this.floatMaxValue = parseFloat(maxValue);
			} else
				this.floatMaxValue = null;
		},
		/**
		 * 如果header类型是inttext，则调用此方法设置最小值
		 * @param minValue 最小值，不能小于javascript允许的整数最小值，小于则采用默认最小值
		 */
		setIntegerMinValue : function(minValue) {
			if (minValue) {
				if ($.argumentutils.isNumber(minValue)) {
					if ((parseInt(minValue) >= -9007199254740992) && (parseInt(minValue) <= 9007199254740992)) {
						this.integerMinValue = minValue;
					} else {
						this.integerMinValue = "";
					}
				} else {
					this.integerMinValue = -9007199254740992;
				}
			}
		},
		/**
		 * 如果header类型是inttext，则调用此方法设置最大值
		 * @param maxValue 最大值，不能大于javascript允许的整数最大值，大于则采用默认最大值
		 */
		setIntegerMaxValue : function(maxValue) {
			if (maxValue != null) {
				// 判断maxValue是否是数字
				if ($.argumentutils.isNumber(maxValue)) {
					if (parseInt(maxValue) >= -9007199254740992 && parseInt(maxValue) <= 9007199254740992)
						this.integerMaxValue = maxValue;
					else
						this.integerMaxValue = "";
				} else
					this.integerMaxValue = 9007199254740992;
			}
		},
		/**
		 * 如果header类型是numtext，则调用此方法设置精度
		 * @param precision int型，小数点后几位
		 */
		setPrecision : function(precision, fromDs) {
			fromDs = (fromDs == null) ? false : fromDs;
			if (fromDs == true) {
				this.precisionFromDs = true;
			}
			// 以ds设置的精度为准
			if (this.precisionFromDs != null && this.precisionFromDs && fromDs == false)
				return;
			if (this.precision == null || this.precision != precision) {
				this.precision = precision;
				this.reRender();
			}
		},
		/**
		 * 重新render列中的cell
		 */
		reRender : function() {
			if (this.dataDiv && this.dataDiv.data('cells')) {
				var grid = this.owner;
				for (var i = 0; i < this.dataDiv.data('cells').length; i++) {
					var cell = this.dataDiv.data('cells')[i];
					if (cell == null)
						continue;
					var value = grid.model.getCellValueByIndex(i, cell.colIndex);
					cell.children().first().remove();
					if (this.renderType && this.renderType.render) {
						this.renderType.render.call(grid, i, this.dataDiv.data('cells')[i].data('colIndex'), value, this, cell);
					}
					if (this.autoRowHeight)
						this.adjustRowHeight(i, cell);
				}
			}
		},
		/**
		 * 如果header类型是stringtext，则调用此方法设置最大长度
		 * @param{int} maxLength 最大字符允许长度
		 */
		setMaxLength : function(maxLength) {
			this.maxLength = parseInt(maxLength);
		},
		/**
		 * 设置此列是否必须填写，设置为true会改变表头的颜色
		 * @param isRequired 此列是否为必输项
		 */
		setRequired : function(isRequired) {
			this.required = $.argumentutils.getBoolean(isRequired, false);
		},
		/**
		 * 参照类型设置nodeinfo
		 */
		setNodeInfo : function(nodeInfo) {
			this.nodeInfo = nodeInfo;
		},
		/**
		 * 设置checkbox的真实值和显示值
		 */
		setValuePair : function(valuePair) {
			this.valuePair = valuePair;
		},
		/**
		 * 如果header类型是combox，则调用此方法把showImgOnly的值传入，来标明此列 的combox是什么类型的combox
		 */
		setShowImgOnly : function(showImgOnly) {
			this.showImgOnly = showImgOnly;
		},
		/**
		 * 如果header类型是combox，则调用此方法设置combox的ComboData
		 */
		setHeaderComboBoxComboData : function(comboData) {
			this.comboData = comboData;
		},
		/**
		 * 如果header类型是languageCcombox，则调用此方法设置下拉项
		 */
		setHeaderLanguageComboBoxs : function(langugeComboDatas) {
			this.langugeComboDatas = langugeComboDatas;
		},
		destroySelf : function() {
			this.comboData = null;
			if (this.langugeComboDatas != null)
				this.langugeComboDatas = null;
			this.textNode = null;
			this.renderType = null;
			this.editorType = null;
			this.parent = null;
			this.topHeader = null;
			this.sumColRenderFunc = null;
			this.dataDiv = null;
			this.cell = null;
			if (this.dataTable) {
				this.dataTable.removeData('headerOwner');
				this.dataTable = null;
			}
			this.owner = null;
		}
	});
	/**
	 * Grid行定义
	 */
	$.gridrow = function(){};
	$.extend($.gridrow.prototype,Array.prototype,{
		/**
		 * 设置此行实际的数据
		 * @param row 一维数组或者dsRow
		 */
		setPageData : function(row) {
			this.pageData = row;
		},
		/**
		 * 得到此行实际的数据
		 * @return 一维数组或者dsRow
		 */
		getPageData : function() {
			return this.pageData;
		},
		/**
		 * 设定GridCompRow和DsRow列的对应关系
		 */
		setBindRelation : function(relation) {
			this.relation = relation;
		},
		/**
		 * 根据字段名返回value
		 * @param {} name
		 */
		getCellValueByFieldName : function(name) {
			var dataset = this.pageData.dataset;
			var dsIndex = dataset.nameToIndex(name);
			return this.pageData.getCellValue(dsIndex);
		},
		/**
		 * 根据字段名返回row中对应的列index
		 * @param {} name
		 */
		getColIndexByFieldName : function(name) {
			var dataset = this.pageData.dataset;
			var dsIndex = dataset.nameToIndex(name);
			for (var i = 0; i < this.relation.length; i++) {
				if (this.relation[i] == dsIndex)
					return i;
			}
			return -1;
		},
		/**
		 * 根据列index取出字段名
		 * @param {} name
		 */
		getFiledNameByColIndex : function(colIndex) {
			var dsColIndex = this.relation[colIndex];
			if (typeof(dsColIndex) == "undefined" || dsColIndex == -1)
				return null;
			var dataset = this.pageData.dataset;
			if (dataset.fieldList[dsColIndex])
				return dataset.fieldList[dsColIndex].key;
			else
				return null;
		},
		/**
		 * 根据指定的grid中的index返回dataset中实际列位置的值
		 */
		getCellValue : function(index) {
			// dataset数据(根据帮定关系返回真实数据)
			if (this.relation != null) {
				if (index >= this.pageData.length || index < 0)
					$.pageutils.showErrorDialog("Index out of bounds exception!");
				else {
					// 得到dataset中实际的列号
					var relPosi = this.relation[index];
					return this.pageData.getCellValue(relPosi);
				}
			}
		},
		/**
		 * 根据索引设置row中相应列的值
		 * @param index 列号
		 */
		setCellValue : function(index, value) {
			// dataset数据(根据帮定关系返回真实数据)
			if (this.relation != null) {
				if (index >= this.pageData.length || index < 0)
					$.pageutils.showErrorDialog("Index out of bounds exception!");
				else {
					// 得到实际的dataSet列号
					var relPosi = this.relation[index];
					this.pageData.setCellValue(relPosi, value);
				}
			}
		}
	});
	$.gridrow.getObj = function() {
		return new $.gridrow();
	};
	/** 
	 * treeLevel 树表相关方法
	 **/
	$.gridtreelevel = function(id, recursivePkField, recursivePPkField, labelFields, loadField, leafField) {
		this.id = id;
		// 此level所挂的contextMenu菜单
		this.contextMenu = null;
		this.recursiveKeyField = recursivePkField;
		this.recursivePKeyField = recursivePPkField;
		this.labelFields = labelFields;
		this.loadField = loadField;
		this.leafField = leafField;
	}
	$.gridtreelevel.getObj = function(id, recursivePkField, recursivePPkField, labelFields, loadField, leafField) {
		return new $.gridtreelevel(id, recursivePkField, recursivePPkField, labelFields, loadField, leafField);
	}
})(jQuery);
/*
 * 测试Render
 */
function selfDefHeaderBtnRender(grid) {
	grid.removeAllHeaderBtn();
	grid.addHeaderBtn("gridHeaderBtn_Test", "测试", window.themePath + "/comps/grid/images/headerbtn/add_normal.png");
}
