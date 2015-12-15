function require_css(files) {
	if(files == null || files.length == 0)
		return;
	var html_doc = document.getElementsByTagName('head')[0];
	for(var i = 0; i < files.length; i ++){
		if(window.cssLoadRecord[files[i]] != null)
			continue;
		window.cssLoadRecord[files[i]] = 1;
		var css = document.createElement('link');
		css.setAttribute('rel', 'stylesheet');
		css.setAttribute('type', 'text/css');
		
		var path = window.themePath + "/" + files[i] + ".css";
		css.setAttribute('href', path);
		html_doc.appendChild(css);
	}
}
function require_framejs(files,func) {
	if(files == null || files.length == 0){
		return;
	}
	var html_doc = document.getElementsByTagName('head')[0];
	var js = document.createElement('script');
	js.setAttribute('type', 'text/javascript');
	var path = window.frameGlobalPath  + "/" + files[0];
	js.setAttribute('src', path);
	html_doc.appendChild(js);
	
	js.onload = js.onreadystatechange = function () {
		if(files.length == 1){
			func();
		}else{
			files.splice(0, 1);
			require_framejs(files,func);
		}
		if ( !js.readyState || /loaded|complete/.test( js.readyState ) ) {
			// Handle memory leak in IE
			js.onload = js.onreadystatechange = null;

			// Remove the script
			if ( js.parentNode ) {
				js.parentNode.removeChild( js );
			}
			// Dereference the script
			script = null;
		}
		
	};
} 
function require_extjs(files,func) {
	if(files == null || files.length == 0){
		return;
	}
	var html_doc = document.getElementsByTagName('head')[0];
	var js = document.createElement('script');
	js.setAttribute('type', 'text/javascript');
	var path =window.globalPath+window.nodePath + "/" + files[0];
	js.setAttribute('src', path);
	html_doc.appendChild(js);
	
	js.onload = js.onreadystatechange = function () {
		if(files.length == 1){
			func();
		}else{
			files.splice(0, 1);
			require_extjs(files,func);
		}
		if ( !js.readyState || /loaded|complete/.test( js.readyState ) ) {
			// Handle memory leak in IE
			js.onload = js.onreadystatechange = null;

			// Remove the script
			if ( js.parentNode ) {
				js.parentNode.removeChild( js );
			}
			// Dereference the script
			script = null;
		}
		
	};
}
function require_design(files,func) {
	if(files == null || files.length == 0){
		return;
	}
	var html_doc = document.getElementsByTagName('head')[0];
	var js = document.createElement('script');
	js.setAttribute('type', 'text/javascript');
	var path =window.globalPath+window.nodePath + "/" + files[0];
	js.setAttribute('src', path);
	html_doc.appendChild(js);
	
	js.onload = js.onreadystatechange = function () {
		if(files.length == 1){
			func();
		}else{
			files.splice(0, 1);
			require_design(files,func);
		}
		if ( !js.readyState || /loaded|complete/.test( js.readyState ) ) {
			// Handle memory leak in IE
			js.onload = js.onreadystatechange = null;

			// Remove the script
			if ( js.parentNode ) {
				js.parentNode.removeChild( js );
			}
			// Dereference the script
			script = null;
		}
		
	};
} 

var jsfiles=['comps/LinkComp.js',
             'comps/ModalDialogComp.js',
             'comps/ErrorDialogComp.js','comps/ConfirmDialogComp.js','comps/MessageDialogComp.js',
             'parts/Formula.js','parts/PagePart.js','parts/ViewPart.js','layout/FloatingPanelComp.js',
             'layout/OutLookBarComp.js','layout/PanelComp.js','layout/SpliterComp.js','layout/TabComp.js',
             'layout/CanvasComp.js','layout/CardLayout.js','layout/DividLayoutComp.js',
             'comps/LoadingBarComp.js','comps/ContextMenuComp.js','comps/ButtonComp.js','comps/TreeViewComp.js',
             'comps/TreeLevel.js','comps/MenuBarComp.js','comps/LabelComp.js','comps/TextComp.js',
             'comps/StringTextComp.js','comps/FloatTextComp.js','comps/IntegerTextComp.js','comps/CalendarComp.js',
             'comps/DateTextComp.js','comps/ReferenceTextComp.js','comps/ComboComp.js','comps/PswTextComp.js',
             'comps/RadioComp.js','comps/FileComp.js','comps/CheckboxComp.js','comps/CheckboxGroupComp.js',
             'comps/TextAreaComp.js','comps/RadioGroupComp.js','comps/ImageComp.js','comps/ToolBarComp.js','comps/EditorComp.js',
             'comps/SelfDefComp.js', 'comps/GridCompModel.js','comps/GridAssistant.js','comps/GridComp.js','comps/FormComp.js',
             'comps/IFrameComp.js','comps/FixedGridLayout.js','comps/InputDialogComp.js',
             'comps/FlowGridLayout.js','model/ViewModel.js','model/Dataset.js','comps/refscript.js','comps/HtmlContentComp.js',
             'comps/TextButtonComp.js'];           

