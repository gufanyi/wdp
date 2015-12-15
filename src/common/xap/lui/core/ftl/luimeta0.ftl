<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Pragma" content="no-cache"/>
<script>
window.globalPath = "${ROOT_PATH}";
window.corePath = "${ROOT_PATH}/core";
window.themeId = "default";
window.baseGlobalPath = "${ROOT_PATH}";
window.frameGlobalPath = "${FRAME_GLOBAL_PATH}";
window.themeGlobalPath = "${ROOT_PATH}";
window.themePath ='${THEME_PATH}';
window.nodePath = "${NODE_PATH}";
window.nodeThemePath = "${NODE_THEME_PATH}";
window.nodeStylePath = "${NODE_STYLE_PATH}";
window.nodeImagePath = "${NODE_IMAGE_PATH}";
window.appId = '${NODE_ID}';
window.appType = 'true';
window.tempPath = '';
window.debugMode = 'debug';
<#if ("${ModePhase}" == "eclipse" && "${NODE_ID}" !="pa")>  
window.editMode = true;
<#else>
window.editMode = false;
</#if> 
window.windowEditorMode = false;
window.datasourceName = 'design';
window.clientMode = false;
</script>
