<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">	
	<head>
		<#include "luimeta1.ftl">
		<#include "luitheme.ftl">
		<#include "luiscript.ftl">
	</head>
	<body>
		<script>
			window.isPopView = true;
			function handleClose(){
				window.pageUI.getViewPart(window.widgetId).onclose();
				window.pageUI.removeViewPart(window.widgetId);
				window.pageUI.removeDialog(window.widgetId);
			}
			//function initScript() {
			   // debugger;
				if (!$.pageutils.getTrueParent){
				  setTimeout("initScript()", 500);	
				}
				window.pageUI = $.pageutils.getTrueParent().pageUI;
				window.$maskerMeta = $.pageutils.getTrueParent().$maskerMeta;
				window.$paramsMap = $.pageutils.getTrueParent().$paramsMap;
				window.$cs_clientStickKeys = $.pageutils.getTrueParent().$cs_clientStickKeys;
				window.$cs_clientSession = $.pageutils.getTrueParent().$cs_clientSession;
			    ${hahahaha}
				$(window).off("unload");
				$(window).triggerHandler("resize");
				window.renderDone = true;
			//}
		</script>
	</body>
</html>
