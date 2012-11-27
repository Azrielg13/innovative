var Color= new Array();
Color[1] = "ff";
Color[2] = "ee";
Color[3] = "dd";
Color[4] = "cc";
Color[5] = "bb";
Color[6] = "aa";
Color[7] = "99";

function waittofade() {
	if (document.getElementById('fade')) {
    setTimeout("fadeIn(7)", 1000);
	 }
}

function fadeIn(where) {
    if (where >= 1) {
        document.getElementById('fade').style.backgroundColor = "#ffff" + Color[where];
		  if (where > 1) {
			  where -= 1;
			  setTimeout("fadeIn("+where+")", 200);
			} else {
			  where -= 1;
			  setTimeout("fadeIn("+where+")", 200);
			  document.getElementById('fade').style.backgroundColor = "transparent";
			}
    }
}

function validateField(fieldId, alertMessage) {
    if (document.getElementById(fieldId).value == "") {
        alert(alertMessage);
        document.getElementById(fieldId).focus();
        return false;
    } else {
        return true;
    }
}

function popup(url,width,height) {
        window.open(url,'popup','width='+width+',height='+height+',scrollbars=auto,resizable=yes,toolbar=no,directories=no,menubar=no,status=no,left=100,top=100');
        return false;
}

function milestoneResponsibleParty(control) {
  var item = control.options[control.selectedIndex]
  var match = control.name.match(/\[(\d+)\]/)
  var suffix = match ? ("_" + match[1]) : ""
  if(item.value.match(/^c/)) {
    Element.hide('reminder' + suffix)
  } else {
    Element.show('reminder' + suffix)
  }
}

var ShowHide = Class.create();
ShowHide.prototype = {
  initialize: function(element, callbacks) {
    this.element   = element = $(element);
    this.effect    = element.getAttribute('effect') || 'slide';
    this.duration  = parseFloat(element.getAttribute('duration')) || 0.25;
    this.activeClassName = element.getAttribute('activeclassname') || 'active';
    this.callbacks = callbacks;
    this.active    = Element.visible(element);
    this.element.showHide = this;
  },
  
  togglers: function() {
    return document.getElementsByClassName('show_hide_toggler_' + this.element.id);
  },
  
  toggle: function() {
    if (this.callbacks.beforeToggle) this.callbacks.beforeToggle(this);
    Effect.toggle(this.element, this.effect, {duration: this.duration, 
      afterFinish: (this.callbacks.afterToggle || Prototype.K).bind(null, this)});
    this.active = !this.active;
    this.togglers().concat(this.element).each(this.adjustClassName.bind(this));
  },
  
  show: function() {
    if (this.active) return;
    this.toggle();
  },
  
  hide: function() {
    if (!this.active) return;
    this.toggle();
  },
  
  adjustClassName: function(element) {
    Element[this.active ? 'addClassName' : 'removeClassName'](element, this.activeClassName);
  }
}
