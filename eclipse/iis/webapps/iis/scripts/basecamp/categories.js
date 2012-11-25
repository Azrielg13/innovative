categories = {
  toggleEdit: function() {
    if($('edit_categories_link')['bc:active']) {
      Element.activate('edit_categories_link', false)
      $('CategoryList').removeClassName("editing")
    } else {
      Element.activate('edit_categories_link', true)
      $('CategoryList').addClassName("editing")
    }
    $('add_new_category').toggle();
  },

  toggleOperationSpinner: function(id) {
    Element.toggle('operation_wait_' + id,
      'operation_rename_' + id, 'operation_delete_' + id)
  },

  cancelEdit: function(id) {
    $('edit_category_' + id).remove()
    $('category_menu_item_' + id).show()
  },

  edit: function(id, name, url) {
    name = prompt("Rename this category", name);
    if(name) {
      this.toggleOperationSpinner(id);
      new Ajax.Request(url, {asynchronous:true, evalScripts:true,
        parameters:'name=' + encodeURIComponent(name),
        onFailure: function(req) { categorySelect.addFailed(req) }
      });
    }
  },

  addNew: function() {
    toggle = function() { Element.toggle('add_new_category_link', 'add_new_category_spinner'); };
    categories.add({ addFailed: toggle, validName: toggle });
  },

  add: function(options) {
    options = options || {};
    var name = prompt("Enter the new category name:", "");
    if(name) name = name.strip()
    if(!name || name.length < 1) {
      if(options.invalidName) options.invalidName();
    } else {
      if(options.validName) options.validName(name);
      new Ajax.Request(this.url, {asynchronous:true, evalScripts:true,
        parameters:'name=' + encodeURIComponent(name),
        onFailure: function(req) { categories.addFailed(req, options); }
      });
    }
  },

  addFailed: function(request, options) {
    alert("An error prevented the category from being added. Please try again.")
    if(options.addFailed) options.addFailed(req);
  }
}

categorySelect = {
  controls: [],
  allowNone: false,

  register: function(id) {
    var control = $(id)
    this.controls.push(control)
    Event.observe(control, 'change', this.detectChange.bind(this))
  },

  detectChange: function(event) {
    var control = Event.element(event)
    if(control.selectedIndex == control.options.length - 1) {
      categories.add({
        invalidName: function() {
          control.selectedIndex = 0;
        },

        validName: function(name) {
          categorySelect.control = control
          categorySelect.enableControls(false)
        },

        addFailed: function(req) {
          categorySelect.enableControls(true)
          categorySelect.control.selectedIndex = 0
       }
      });
    }
  },

  enableControls: function(setting) {
    this.controls.each(function(item) {
      item.disabled = !setting;
    })
  },

  addCategory: function(id, name, position) {
    if(this.allowNone) position++
    this.controls.each(function(item){
      var options = $A(item.options)
      options.splice(position, 0, new Option(name, id))
      for(var idx = 0; idx < options.length; idx++) {
        item.options[idx] = options[idx]
      }
    })
    this.reset(position);
  },

  reset: function(selected) {
    if(this.control) this.control.selectedIndex = (selected || 0);
    this.enableControls(true)
  },

  renameCategory: function(id, name) {
    this.controls.each(function(item){
      for(var idx = 0; idx < item.options.length; idx++) {
        if(parseInt(item.options[idx].value) == id) {
          item.options[idx].text = name;
          break;
        }
      }
    })
  },

  deleteCategory: function(id) {
    this.controls.each(function(item){
      for(var idx = 0; idx < item.options.length; idx++)
        if(parseInt(item.options[idx].value) == id) {
          item.options[idx] = null;
          break;
        }
    })
  }
}
