/**
 * @fileoverview 此文件定义了GridComp对外暴露事件与事件模型
 * 
 * @author dingrf
 * @version NC6.1
 */

/**
 * cell单击时用户重载方法
 * 
 * @param cell
 * @param rowIndex
 *            行id
 * @param colIndex
 *            列id
 * @private
 */
GridComp.prototype.onCellClick = function(cell, rowIndex, colIndex) {
    var cellEvent = {
        "obj": this,
        "cell": cell,
        "rowIndex": rowIndex,
        "colIndex": colIndex
    };
    this.doEventFunc("onCellClick", GridCellListener.listenerType, cellEvent);
};

/**
 * cell编辑时调用该方法,用户可以重载该方法返回自定义的cell编辑器
 * 
 * @private
 */
GridComp.prototype.getCellEditor = function(cell, rowIndex, colIndex) {
    var cellEvent = {
        "obj": this,
        "cell": cell,
        "rowIndex": rowIndex,
        "colIndex": colIndex
    };
    this.doEventFunc("cellEdit", GridCellListener.listenerType, cellEvent);
};

/**
 * cell编辑完成后调用此方法
 * 
 * @param rowIndex
 *            行号
 * @param colIndex
 *            列号
 * @param oldValue
 *            旧值
 * @param newValue
 *            新值
 * @private
 */
GridComp.prototype.onAfterEdit = function(rowIndex, colIndex, oldValue, newValue) {
    var afterCellEditEvent = {
        "obj": this,
        "rowIndex": rowIndex,
        "colIndex": colIndex,
        "oldValue": oldValue,
        "newValue": newValue
    };
    this.doEventFunc("afterEdit", GridCellListener.listenerType, afterCellEditEvent);
};

/**
 * cell编辑前调用的方法,返回false可以阻止编辑cell
 * 
 * @param rowIndex
 *            行索引
 * @param colIndex
 *            列索引
 * @private
 */
GridComp.prototype.onBeforeEdit = function(rowIndex, colIndex) {
    var beforeCellEditEvent = {
        "obj": this,
        "rowIndex": rowIndex,
        "colIndex": colIndex
    };
    var result = this.doEventFunc("beforeEdit", GridCellListener.listenerType, beforeCellEditEvent);
    if (result != null) return result;
};

/**
 * cell值改变后调用此方法
 * 
 * @param rowIndex
 *            行号
 * @param colIndex
 *            列号
 * @param oldValue
 *            旧值
 * @param newValue
 *            新值
 * @private
 */
GridComp.prototype.onCellValueChanged = function(rowIndex, colIndex, oldValue, newValue) {
    var cellValueChangedEvent = {
        "obj": this,
        "rowIndex": rowIndex,
        "colIndex": colIndex,
        "oldValue": oldValue,
        "newValue": newValue
    };
    this.doEventFunc("cellValueChanged", GridCellListener.listenerType, cellValueChangedEvent);
};

/**
 * 行选中之前调用的方法
 * 
 * @param rowIndex
 *            选中行的索引
 * @param{GridCompRow} row
 * @private
 */
GridComp.prototype.onBeforeRowSelected = function(rowIndex, row) {
    var rowEvent = {
        "obj": this,
        "rowIndex": rowIndex,
        "row": row
    };
    return this.doEventFunc("beforeRowSelected", GridRowListener.listenerType, rowEvent);
};

/**
 * 行双击时调用的方法(只有在grid整体不能编辑时该方法才有用)
 * 
 * @param rowIndex
 *            选中行的索引
 * @param{GridCompRow} row
 * @private
 */
GridComp.prototype.onRowDblClick = function(rowIndex, row) {
    var rowEvent = {
        "obj": this,
        "rowIndex": rowIndex,
        "row": row
    };
    this.doEventFunc("onRowDbClick", GridRowListener.listenerType, rowEvent);
};

/**
 * 行选中时调用的方法
 * 
 * @param rowIndex
 *            选中行的索引
 * @private
 */
GridComp.prototype.onRowSelected = function(rowIndex) {
    var rowSelectedEvent = {
        "obj": this,
        "rowIndex": rowIndex
    };
    this.doEventFunc("onRowSelected", GridRowListener.listenerType, rowSelectedEvent);
};

/**
 * 数据区上点击鼠标右键时调用这个方法
 * 
 * @private
 */
GridComp.prototype.onDataOuterDivContextMenu = function(e) {
    var mouseEvent = {
        "obj": this,
        "event": e
    };
    return this.doEventFunc("onDataOuterDivContextMenu", GridListener.listenerType, mouseEvent);
};

/**
 * grid上最后一个可编辑列按回车事件
 * 
 * 
 */
GridComp.prototype.onLastCellEnter = function(e) {
    var gridEvent = {
        "obj": this,
        "event": e
    };
    return this.doEventFunc("onLastCellEnter", GridListener.listenerType, gridEvent);
};

/**
 * @private
 */
GridComp.prototype.processPageCount = function(pageInfo) {
    var processPageCountEvent = {
        "obj": this,
        "pageInfo": pageInfo
    };
    this.doEventFunc("processPageCount", GridListener.listenerType, processPageCountEvent);
    return pageInfo;
};

/**
 * 粘贴之前的事件
 * @param {} pasteEvent
 */
GridComp.prototype.onBeforePaste = function(e) {
    var gridEvent = {
        "obj": this,
        "event": e
    };
    this.doEventFunc("onBeforePaste", GridListener.listenerType, gridEvent);
};