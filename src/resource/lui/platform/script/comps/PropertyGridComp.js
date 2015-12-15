/**
 * @propertygridComp 表格控件
 * @auther lxl
 * @version lui 1.0
 */
(function($) {
	$.widget("lui.propertygrid", $.lui.grid, {
		addOneRow : function(row, index, scrollLeft, rowHeight, rowCount, parentRowIndex) {
			if (this.noRowsDiv) {
				this.noRowsDiv.hide();
				this.dynamicColumDataDiv.css('margin-bottom','17px');
			}
			this.dataOuterDiv.css('overflow','auto').show();
			var isOdd = this.isOdd(index);
			var tempHeaders = [];
			var fixedheaderWidth = this.constant.fixedHeaderDivWidth;
			var scrollTop = 0;
			var oThis = this, _editorType = row.getCellValueByFieldName('editorType'),_dataType = row.getCellValueByFieldName('dataType');
			// 判断是否出竖直滚动条,将headerDiv的宽度缩小17px
			if (this.firstVScroll == false) {
				if (this.isVScroll()) {
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
			// 动态表头长度
			var dynHeaderLen = this.defaultDynamicHeaders.length;
			var lastHeader = this.getLastDynamicVisibleHeader();
			var firstHeader = this.getFirstDynamicVisibleHeader();
			var rowsCount = this.model.getRowsCount();
			// 存储一行的所有cell,行渲染用
			var rowCells = null;
			if (this.rowCells == null)
				rowCells = [];
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				var cell = $("<div>");
				rowCells.push(cell);
				cell.data('rowIndex',index);
				if (row.level != null)
					cell.data('level',row.level);
				if (row.hasChildren && row.hasChildren != null)
					cell.data('hasChildren',row.hasChildren);
				
				var _render = null;
				if(header.id == 'propName') {
					_render = DefaultRender;
				} else {
					var _renderTmp = row.getCellValueByFieldName('renderType');
					_render = _renderTmp ? eval(_renderTmp) : DefaultRender;
				}
					
				cell.data({
					'colIndex' : i,
					'renderType' : _render,
					'editorType' : _editorType,
					'minValue' : row.getCellValueByFieldName('minValue'),
					'maxValue' : row.getCellValueByFieldName('maxValue'),
					'precision' : row.getCellValueByFieldName('precision'),
					'comboData' : this.viewpart.getComboData(row.getCellValueByFieldName('refComboData')),
					'nodeInfo' : this.viewpart.getRefNode(row.getCellValueByFieldName('refNode')),
					'maxLength' : row.getCellValueByFieldName('maxLength'),
					'keyName' : row.getCellValueByFieldName('id')
				}).addClass(isOdd ? "gridcell_odd" : "gridcell_even").on({
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
				if (typeof(_dataType) == "string" && !header.textAlign) {
					switch (_dataType) {
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
							if (_editorType && _editorType == "ComboBox") {
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
				
				if (header.dataDivWidth != null && header.dataDivWidth > 0) {
					cell.css('width',(header.dataDivWidth - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
				} else if (header.dataDiv && header.dataDiv.outerWidth() > 0) {
					cell.css('width',(header.dataDiv.outerWidth() - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
				} else if (header.width && header.width > 0) {
					cell.css('width',(header.width - $.grid.CELL_LEFT_PADDING - $.grid.CELL_RIGHT_PADDING) + "px");
				}
				
				if (!(dynHeaderLen > 0 && header != firstHeader && header != lastHeader)) {
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
				
				cell.data('renderType').render.call(this, index, i, row.getCellValue(i), header, cell, parentRowIndex);
				
				// 根据header中的bgcolor属性设置列背景色
				if (header.columBgColor != null && header.columBgColor != "" && cell.css('background-color') != "#ffffff") {
					cell.css('background-color',header.columBgColor);
				}
				if (header.textColor != null && header.textColor != "") {
					cell.css('color',header.textColor);
				}
				header.dataDiv.data('cells').splice(index, 0, cell);
				// 最后调整高度
				if (this.autoRowHeight == true) {
					this.adjustRowHeight(index, cell);
				}
			}
			// 行渲染
			this.rowRender.render.call(this, row, rowCells);
			this.refreshRowClass();
		},
		refreshRowClass : function() {
			var that = this;
			for (var i = 0; i < this.model.basicHeaders.length; i++) {
				var header = this.model.basicHeaders[i];
				if (header.isHidden == true)
					continue;
				header.dataDiv.children().each(function(i){//所有列
					var _class = "";
					if(that.oddType == '0') {
						_class = i %2 == 1 ? 'gridcell_odd' : 'gridcell_even';
					} else {
						_class = i %3 == 1 ? 'gridcell_odd' : 'gridcell_even';
					}
					$(this).attr('class' , _class);//重新给rowIndex赋值
				});
			}
		}
		
	});
})(jQuery);
