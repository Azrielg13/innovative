
MultiCheck = function(div, options, selected) {
	this.options = options;
	this.selected = selected;
	var out = '';
	for (var idx = 0; idx < options.length; idx++) {
		out += '<input type="checkbox" name="' + options[idx] + '"';
		if (selected.indexOf(options[idx]) > -1) {
			out += ' checked';
		}
		out += '>';
	}
	div.innerHTML = out;
};

MultiCheck.prototype.options = [];

MultiCheck.prototype.selected = [];

MultiCheck.prototype.getSelected = function() {
	return this.selected;
};