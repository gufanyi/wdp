function execFormula(widgetId, dsId, fieldId,opts) {
	var proxy = $.serverproxy.getObj({async:true});
	proxy.addParam('clc', 'xap.lui.core.control.DftAppCtrl');
	proxy.addParam('m_n', 'onFormula');
	proxy.addParam('widgetId', widgetId);
	proxy.addParam("dsId", dsId);
	if(opts) {
		$.each(opts,function(key,val) {
			proxy.addParam(key,val);
		});
	}
	
	var sbr = $.submitrule.getObj();
	var wdRule = $.viewpartrule.getObj(widgetId);
	if(dsId){
		var dsRule = $.datasetrule.getObj(dsId, "ds_all_line");
		wdRule.addDsRule(dsId, dsRule);
	}
	sbr.addViewPartRule(widgetId, wdRule);
	proxy.setSubmitRule(sbr);
	proxy.addParam("fieldId", fieldId);
	$.serverproxy.wrapProxy(proxy);
	//proxy.execute();
}
