(function($) {
	$.widget("lui.canvas", $.lui.base, {
		options : {
			parent : null,
			position : 'absolute',
			title : trans("ml_CanvasTitle"),
			className : 'fullcanvas'
		},
		_initParam : function() {
			this.componentType = "CANVAS";
			this.parentOwner = this.options.parent;
			this.id = this.element.attr("id");
			this._super();
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element;
		    ele.css({
		        'left' : opts.left + "px",
		        'top' : opts.top + "px",
		        'width' : '100%',
		        'min-height' : '30px'
		    });
		    this.generateDiv(opts.className);
		    if(this.parentOwner) {
		    	var fc = $(this.parentOwner).children("div").first();
		        if (fc.size()>0) {
		            this.contentDiv.append($(fc));
		        } else {
		            if (opts.title != null && opts.title != "") {
		                this.contentDiv.addClass('canvas_title title_font').html(opts.title);
		            }
		        }
		        ele.appendTo($(this.parentOwner));
	        }
		},
		generateDiv : function(className) {
			var ele = this.element;
		    //var type = $.measures.getCssHeight(className + "_TYPE");
		    var contentTd = null;
		    if (className == "nonecanvas") {
		        var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='centercontent'></td></tr></table>";
		        ele.html(html);
		        contentTd = ele.find("td").first();
		    }
		    //leftcanvas
		    else if (className == "leftcanvas") {
		        var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='leftborder'></td><td class='centercontent'></td></tr></table>";
		        ele.html(html);
		        contentTd = ele.find("td:eq(1)");
		    }

		    else if (className == "rightcanvas") {
		        var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='centercontent'></td><td class='rightborder'></td></tr></table>";
		        ele.html(html);
		        contentTd = ele.find("td").first();
		    }

		    else if (className == "fullcanvas") {
		        var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='leftborder'></td><td class='centercontent'></td><td class='rightborder'></td></tr></table>";
		        ele.html(html);
		        contentTd = ele.find("td:eq(1)");
		    }

		    else if (className == "centercanvas") {
		        var html = "<table cellspacing='0' cellpadding='0' class='" + className + "'><tr><td class='centercontent'></td></tr></table>";
		        ele.html(html);
		        contentTd = ele.find("td").first();
		    }

		    this.contentDiv = $("<div></div>");
		    contentTd.append(this.contentDiv);
		},
		getContentDiv : function() {
		    return this.contentDiv.get(0);

		},
		setContent : function(obj) {
		    this.contentDiv.append($(obj));
		},
		removeContent : function(obj) {
			$(obj).remove();
		},
		changeClass : function(className) {
		    //var child = this.contentDiv.children().first();
		    this.generateDiv(className);
		},
		getContext : function() {
		    return {
		    	c : 'CanvasContext',
		    	id : this.element.attr("id")
		    };
		},
		setContext : function(context) {
		    if (context.display != null && context.display != this.display) this.setDisplay(context.display);
		    if (context.visible != null && context.visible != this.visible) this.setVisible(context.visible);
		}
	});
})(jQuery);
