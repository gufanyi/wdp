/**
 * 时间输入框控件
 * @author wlh
 */
 
(function($) {
	$.widget("lui.timetext", $.lui.base, {
		options : {
			height : 24,
			widht : 60,
			value : '',
			//event
			valuechanged : null
		},
		
		_create : function() {
			var ele = this.element.css({
				position : 'relative'
			}),
			  inputH = $("<input type='text' />").addClass('inputtime'),
			  inputM = $("<input type='text' />").addClass('inputtime'),
			  inputS = $("<input type='text' />").addClass('inputtime'),
			  inputHDiv = $("<div></div>").addClass('inputtime_div').append(inputH),
			  inputMDiv = $("<div></div>").addClass('inputtime_div').append(inputM),
			  inputSDiv = $("<div></div>").addClass('inputtime_div').append(inputS),
			  strDiv1 = $("<div>:</div>").addClass('str_div'),
			  strDiv2 = $("<div>:</div>").addClass('str_div'),
			  outerInputDiv = $("<div></div>").addClass('outinput_div');
			  inputHDiv.appendTo(outerInputDiv);
			  strDiv1.appendTo(outerInputDiv);
			  inputMDiv.appendTo(outerInputDiv);
			  strDiv2.appendTo(outerInputDiv);
			  inputSDiv.appendTo(outerInputDiv);
		}
	});
})(jQuery); 
