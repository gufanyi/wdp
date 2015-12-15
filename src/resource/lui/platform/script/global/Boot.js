window.clickHolders = new Array();
if (window.globalObject == null) window.globalObject = new Object;
SHIFT_MASK = 1 << 0;
CTRL_MASK = 1 << 1;
ALT_MASK = 1 << 3;

document.oncontextmenu = documentContextMenu;
/**
 * 右键点击事件
 * @private
 */
function documentContextMenu() {
  for (var i = 0; i < window.clickHolders.length; i++) {
    if (window.clickHolders[i].outsideContextMenuClick) window.clickHolders[i].outsideContextMenuClick();
  }
  return true;
};

document.onclick = documentClick;
function documentClick(e) {
  e = EventUtil.getEvent();
  for (var i = 0; i < window.clickHolders.length; i++) {
    if (window.clickHolders[i].outsideClick) window.clickHolders[i].outsideClick(e);
  }
  window.clickHolders.trigger = null;
  // 删除事件对象（用于清除依赖关系）
//  clearEventSimply(e);
};

document.onmouseover = documentMouseover;
function documentMouseover() {
  for (var i = 0; i < window.clickHolders.length; i++) {
    if (window.clickHolders[i].outsideOver) window.clickHolders[i].outsideOver();
  }
};

document.onmousewheel = documentMouseWheelFunc;
/**
 * 滚轮事件处理方法
 * @param e
 * @return
 * @private
 */
function documentMouseWheelFunc(e) {
  for (var i = 0; i < window.clickHolders.length; i++) {
    if (window.clickHolders[i].outsideMouseWheelClick) window.clickHolders[i].outsideMouseWheelClick(e);
  }
};

document.onkeydown = documentKeydown;
/**
 * 屏蔽浏览器默认的一些快捷键操作,避免和自定义的快捷键操作冲突
 * @private
 */
function documentKeydown(e) {
  if (!e) {
    e = window.event;
  }
  var keyCode = e.keyCode ? e.keyCode: e.charCode ? e.charCode: e.which ? e.which: void 0;
  /**
	 * 屏蔽浏览器默认快捷方式
	 */
  // 屏蔽 Ctrl+n
  if ((e.ctrlKey) && (keyCode == 78)) {
    stopDefault(e);
    return false;
  }
  // 屏蔽 shift+F10
  else if ((e.shiftKey) && (keyCode == 121)) {
    stopDefault(e);
    return false;
  }
  // 屏蔽Ctrl+B
  else if ((e.ctrlKey) && (keyCode == 66)) {
    stopDefault(e);
    return false;
  }
  // 屏蔽Ctrl+E
  else if ((e.ctrlKey) && (keyCode == 69)) {
    stopDefault(e);
  }
  // 屏蔽Ctrl+R(重新载入)
  else if ((e.ctrlKey) && (keyCode == 82)) {
    stopDefault(e);
    return false;
  }
  // 屏蔽Ctrl+A
  else if ((e.ctrlKey) && (keyCode == 65)) {
    stopDefault(e);
    return false;
  }
  // 屏蔽Ctrl+P
  else if (keyCode == 80 && e.ctrlKey) {
    e.keyCode = 0;
    stopDefault(e);
  }
  // 屏蔽Ctrl+L
  else if (keyCode == 76 && e.ctrlKey) {
    stopDefault(e);
    return false;
  }
  // 屏蔽Ctrl+I
  else if (keyCode == 73 && e.ctrlKey) {
    stopDefault(e);
  }
  // 屏蔽Ctrl+C
  else if (e.ctrlKey && keyCode == 67) {
    // stopDefault(e);
    // return false;
  }
  // 屏蔽Ctrl+V
  else if (e.ctrlKey && keyCode == 86) {
    // stopDefault(e);
    // return false;
  }
  // 屏蔽Ctrl+D
  else if (e.ctrlKey && keyCode == 68) {
    stopDefault(e);
    return false;
  }
  // 屏蔽Alt+Z
  else if (e.altKey && keyCode == 90) {
    stopDefault(e);
    return false;
  } else if (keyCode == 114) {
    e.keyCode = 505;
    stopDefault(e);
    return false;
  } else if (keyCode == 8) {
    var ev = e.srcElement || e.target;
    if (ev && ev.tagName != "INPUT" && ev.tagName != "TEXTAREA") {
      stopAll(e);
      return false;
    }
  }
  return true;
};

document.onkeyup = documentKeyup;
/**
 * 按键抬起事件
 * @private
 */
function documentKeyup(e) {
  return $pageKeyUpFunc(e);
};

/**
 * 键盘点击事件执行方法
 * @private
 */
function $pageKeyUpFunc(e) {
  if (!window.pageUI) {
    return true;
  }
  if (!e) e = window.event;
  var keyCode = e.keyCode ? e.keyCode: e.charCode ? e.charCode: e.which ? e.which: void 0;

  // 处理自定义快捷键操作
  if (keyCode == 68 && e.ctrlKey && e.shiftKey) {
    // 调试模式才启动调试监测器
    if (window.debugMode != "production") showDebugDialog();
  }
  // 调用父页面键盘点击事件方法
  if (window.top != window) {
    var parentPage = parent;
    while (parentPage && !parentPage.$pageKeyUpFunc) {
      if (parentPage == parentPage.parent) break;
      parentPage = parentPage.parent;
    }
    if (parentPage && parentPage.$pageKeyUpFunc) return parentPage.$pageKeyUpFunc(e);
  }
  return true;
};

