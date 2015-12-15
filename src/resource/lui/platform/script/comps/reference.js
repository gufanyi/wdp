function getLoadDsParams() {
	var nodeInfo = parent.getRefNodeInfo($.pageutils.getParameter('nodeId'));
	if(nodeInfo != null && nodeInfo.filterSql != null && nodeInfo.filterSql != "")
		return "filterSql=" + nodeInfo.filterSql;
	return null;
}