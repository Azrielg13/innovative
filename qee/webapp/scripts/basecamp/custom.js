Element.empty = function(id) {
  return $(id).innerHTML.match(/^\s*$/);
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

Element.getY = function(element) {
  if(element.y) return element.y
  if(element.offsetTop || element.offsetParent) {
    var y = element.offsetTop
    while(element = element.offsetParent) y += element.offsetTop
    return y
  }
  return 0
}

Element.resizeTo = function(container, extra) {
  container = $(container)
  var width = container.clientWidth || container.scrollWidth
  width += extra // a hack to account for margins, padding, etc.
  if(width > 0) {
    for(var i = 2; i < arguments.length; i++)
      $(arguments[i]).style.width = width + "px"
  }
}

window.installResizerFor = function() {
  var container_and_elements = arguments
  
  var current_onload = window.onload
  window.onload = function() {
    if(current_onload) current_onload()
    Element.resizeTo.apply(Element, container_and_elements)
  }
  
  var current_onresize = window.onresize
  window.onresize = function() {
    var size = {}
    size.width = window.innerWidth || 
      document.documentElement && document.documentElement.clientWidth ||
      document.body.clientWidth
    size.height = window.innerHeight ||
      document.documentElement && document.documentElement.clientHeight ||
      document.body.clientHeight

    /* keep track of the size from the last resize, because IE likes to call
     * resize over and over even if the window hasn't really resized. */

    if(window.last_size && window.last_size.width == size.width && window.last_size.height == size.height)
      return
    window.last_size = size
    
    if(current_onresize) current_onresize()
    Element.resizeTo.apply(Element, container_and_elements)
  }
}

Element.activate = function(element, activate) {
  element = $(element)

  var inactive_text = element['bc:inactive_text']
  if(!inactive_text) {
    element['bc:inactive_text'] = element.innerHTML
    inactive_text = element.innerHTML
  }

  var active_text = element.getAttribute('bc:active_text') || element.innerHTML
  var active_class = element.getAttribute('bc:active_class') || element.className

  if(!activate) {
    element.innerHTML = inactive_text
    if(active_class) Element.removeClassName(element, active_class)
    element['bc:active'] = false
  } else {
    element.innerHTML = active_text
    if(active_class) Element.addClassName(element, active_class)
    element['bc:active'] = true
  }
}

Effect.Zoom = Class.create()
Effect.Zoom.prototype = {
  initialize: function(from, to, duration, onFinish) {
    this.target = $(to)
    this.initializeZoomer()

    var from = $(from)

    this.onFinish = onFinish

    this.target.style.opacity = 0
    this.target.style.visibility = "hidden"
    Element.show(this.target)

    this.startTop = this.getY(from)
    this.startLeft = this.getX(from)
    var endTop = this.getY(this.target)
    var endLeft = this.getX(this.target)
    this.steps = 30.0 * duration
    this.dx = (endLeft - this.startLeft) / this.steps
    this.dy = (endTop - this.startTop) / this.steps
    this.currentStep = 0
    this.zoomer.style.display = "block"
    this.zoom()
  },

  initializeZoomer: function() {
    this.zoomer = document.createElement("DIV")
    this.zoomer.id = "zoomer"
    this.target.parentNode.appendChild(this.zoomer)
  },

  getX: function(element) {
    if(element.x) return element.x
    if(element.offsetLeft || element.offsetParent) {
      var x = element.offsetLeft
      while(element = element.offsetParent) x += element.offsetLeft
      return x
    }
    return 0
  },

  getY: function(element) {
    return Element.getY(element)
  },

  zoom: function() {
    opacity = 100 - (100.0/this.steps) * this.currentStep
    this.zoomer.style.filter = "alpha(opacity=" + opacity + ")"
    this.zoomer.style.opacity = opacity/100

    var pct = this.currentStep / this.steps
    this.zoomer.style.top = this.startTop + this.dy * this.currentStep + "px"
    this.zoomer.style.left = this.startLeft + this.dx * this.currentStep + "px"
    this.zoomer.style.width = this.target.offsetWidth * pct + "px"
    this.zoomer.style.height = this.target.offsetHeight * pct + "px"

    this.currentStep++
    if(this.currentStep < this.steps)
      setTimeout(this.zoom.bind(this), 20)
    else {
      Element.remove(this.zoomer)
      this.target.style.opacity = 1
      this.target.style.visibility = "visible"
      if(this.onFinish) this.onFinish()
    }
  }
}

Effect.ZoomOut = Class.create()
Effect.ZoomOut.prototype = {
  initialize: function(from, to, time) {
    this.target = $(to)
    this.initializeZoomer()
    var from = $(from)

    this.startTop = this.getY(from)
    this.startLeft = this.getX(from)
    var endTop = this.getY(this.target)
    var endLeft = this.getX(this.target)
    this.startWidth = from.offsetWidth
    this.startHeight = from.offsetHeight
    var endWidth = this.target.offsetHeight
    var endHeight = this.target.offsetHeight
    this.steps = 30.0 * time
    this.dx = (endLeft - this.startLeft) / this.steps
    this.dy = (endTop - this.startTop) / this.steps
    this.dh = (endHeight - this.startHeight) / this.steps
    this.dw = (endWidth - this.startWidth) / this.steps
    this.currentStep = 0
    this.zoomer.style.display = "block"

    new Element.hide(from)
    this.zoom()
  },

  initializeZoomer: function() {
    this.zoomer = document.createElement("DIV")
    this.zoomer.id = "zoomer"
    this.target.parentNode.appendChild(this.zoomer)
  },

  getX: function(element) {
    if(element.x) return element.x
    if(element.offsetLeft || element.offsetParent) {
      var x = element.offsetLeft
      while(element = element.offsetParent) x += element.offsetLeft
      return x
    }
    return 0
  },

  getY: function(element) {
    return Element.getY(element)
  },

  zoom: function() {
    opacity = 100 - (100.0/this.steps) * this.currentStep
    this.zoomer.style.filter = "alpha(opacity=" + opacity + ")"
    this.zoomer.style.opacity = opacity/100

    var pct = this.currentStep / this.steps
    this.zoomer.style.top = this.startTop + this.dy * this.currentStep + "px"
    this.zoomer.style.left = this.startLeft + this.dx * this.currentStep + "px"
    this.zoomer.style.width = this.startWidth + this.dw * this.currentStep + "px"
    this.zoomer.style.height = this.startHeight + this.dh * this.currentStep + "px"

    this.currentStep++
    if(this.currentStep < this.steps)
      setTimeout(this.zoom.bind(this), 20)
    else {
      Element.remove(this.zoomer)
    }
  }
}
