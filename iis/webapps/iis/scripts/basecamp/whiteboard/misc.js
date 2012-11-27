var toggle_edit_link_possible = true;

function toggle_edit_link(on, show_container, edit_container) {
  if (toggle_edit_link_possible == false) return
  
  if (on) {
    if($(show_container)) $(show_container).style.background='url(/images/hover-gradient.gif) left repeat-y'
    Element.showIf(edit_container, edit_container + "2")
  } else {
    if($(show_container)) $(show_container).style.background='none'
    Element.hideIf(edit_container, edit_container + "2")
  }
}

Element.empty = function(id) {
  return $(id).innerHTML.match(/^\s*$/);
}

Element.visible = function(element) {
  return ($(element).style.display != "none")
}

Element.showAll = function(array) {
  Element.show.apply(Element.show, array)
}

Element.hideAll = function(array) {
  Element.hide.apply(Element.hide, array)
}

Element.showIf = function() {
  for(var i = 0; i < arguments.length; i++ ) {
    var element = $(arguments[i])
    if(element) Element.show(element)
  }
}

Element.hideIf = function() {
  for(var i = 0; i < arguments.length; i++ ) {
    var element = $(arguments[i])
    if(element) Element.hide(element)
  }
}

function scrollWindowTo(id) {
  element = $(id)
  x = ( element.x ? element.x : element.offsetLeft )
  y = ( element.y ? element.y : element.offsetTop )
  window.scrollTo(x,y)
}


var slideDuration = 0.2

function toggleElement(element, options) {
  Element.visible(element) ? hideElement(element, options) : showElement(element, options)
}

function showElement(element, options) {
  if($(element) && !Element.visible(element)) {
    options = options || {}
    new Effect.BlindDown(element, {duration: slideDuration,
      afterFinish: function() {
        Element.undoClipping(element)
        $(element).style.width = "auto"
        $(element).style.height = "auto"
        if(options.afterFinish) options.afterFinish()
      }})
  }
}

function hideElement(element, options) {
  if($(element) && Element.visible(element)) {
    options = options || {}
    new Effect.BlindUp(element, {duration: slideDuration,
      afterFinish: function() {
        Element.undoClipping(element)
        Element.hide(element)
        if(options.afterFinish) options.afterFinish()
      }})
  }
}

function toggleLink(link) {
  link = $(link)

  var inactive_text = link['wb:inactive_text'] ||
                      link.getAttribute('wb:inactive_text')
  if(!inactive_text) {
    link['wb:inactive_text'] = link.innerHTML
    inactive_text = link.innerHTML
  }

  var active_text = link.getAttribute('wb:active_text')
  var active_class = link.getAttribute('wb:active_class')

  if(link['wb:active']) {
    link.innerHTML = inactive_text
    if(active_class) Element.removeClassName(link, active_class)
    link['wb:active'] = false
  } else {
    link.innerHTML = active_text
    if(active_class) Element.addClassName(link, active_class)
    link['wb:active'] = true
  }
}
