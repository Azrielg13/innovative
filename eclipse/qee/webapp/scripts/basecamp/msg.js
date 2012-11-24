var msgs = {
  beforeRemoveAttachment: function(id) {
    Element.show('spin_' + id)
  },

  afterRemoveAttachment: function(id) {
    new Effect.Fade('attachment_' + id,
      {duration: 0.5,
       afterFinish: function() { Element.remove('attachment_' + id) }})
  },

  submitter: {
    prepare: function(form) {
      if(!validateField('post_title', 'Every message needs at least a title')) {
        return false; 
      }
      Element.toggle('buttons', 'please_wait');
      msgs.submitter.fixAction(form);
      return true;
    },

    prepareComment: function(form) {
      if(!validateField('commentBody', 'Every comment must have a body')) {
        return false; 
      }
      Element.toggle('buttons', 'please_wait');
      msgs.submitter.fixAction(form);
      return true;
    },

    fixAction: function(form) {
      var needs_upload = false;
      $$('#attachment_fields input').each(function(field) {
        if(field.value.match(/^\s*$/)) {
          var id = field.id.match(/_(\d+)$/)[1];
          msgs.attacher.removeField(id);
        } else {
          needs_upload = true;
        }
      })

      if(!needs_upload) {
        msgs.attacher.hide();
        form.action = form.action.replace(/\/upload\//, "/");
      } else {
        msgs.attacher.working();
      }
    }
  },

  attacher: {
    toggle: function() {
      this.visible() ? this.hide(true) : this.show(true)
    },

    show: function(animate) {
      visible = this.visible()

      animate ?
        new Effect.BlindDown('Attachments', {duration: 0.2}) :
        Element.show('Attachments')

      Element.hide('Attachments_link')

      return visible
    },

    hide: function(animate) {
      if(!$('Attachments')) return;

      visible = this.visible();
      
      animate ?
        new Effect.BlindUp('Attachments', {duration: 0.2}) :
        $('Attachments').hide();

      $('Attachments_link').show();

      return visible;
    },

    visible: function() {
      return $('Attachments').style.display != "none";
    },

    working: function() {
      if(!$('Attachments')) return;
      $('add_another_attachment_link').hide();
      $('attaching_files').show();
      $('add_attachment_file_spinner').show();
    },

    removeField: function(id) {
      // remove the form field, so it won't be submitted
      Element.remove("attachment_field_" + id)

      // hide the p tag, so it still counts as a row when determing the next
      // available row number.
      Element.hide("new_attachment_" + id)
    }
  },

  milestoner: {
    toggle: function() {
      this.visible() ? this.hide(true) : this.show(true)
    },

    show: function(animate) {
      visible = this.visible()

      animate ?
        new Effect.BlindDown('RelatedMilestone', {duration: 0.2}) :
        Element.show('RelatedMilestone')

      Element.hide('RelatedMilestone_link')

      return visible
    },

    hide: function(animate) {
      visible = this.visible()
      
      animate ?
        new Effect.BlindUp('RelatedMilestone', {duration: 0.2}) :
        Element.hide('RelatedMilestone')

      Element.show('RelatedMilestone_link')

      return visible
    },

    visible: function() {
      return $('RelatedMilestone').style.display != "none"
    },
    
    associate: function() {
      var list = $('post_milestone_id')
      if(list.selectedIndex == 0) {
        // "none"
        Element.hide('with_milestone')
        Element.show('without_milestone')
      } else {
        // a valid milestone
        Element.hide('without_milestone')
        $('milestone_name').innerHTML = list.options[list.selectedIndex].innerHTML
        Element.show('with_milestone')
        if($('completes_milestone').checked)
          Element.show('completes_milestone_check')
        else
          Element.hide('completes_milestone_check')
      }
      this.hide(true)
    }
  },
  
  notifier: {
    toggle: function() {
      this.visible() ? this.hide() : this.show()
    },

    visible: function() {
      return Element.visible('NotifyBlock')
    },

    show: function() {
      visible = this.visible()

      new Effect.BlindDown('NotifyBlock', {duration: 0.2})
      Element.hide('Notify_link')
      $('notify_everyone').checked = false

      return visible
    },

    hide: function() {
      visible = this.visible()
      
      new Effect.BlindUp('NotifyBlock', {duration: 0.2})
      Element.show('Notify_link')

      return visible
    },
    
    all: function() {
      if($('notify_everyone').checked) this.hide()
    }
  },
  
  formatGuide: {
    toggle: function() {
      this.visible() ? this.hide() : this.show()
    },

    visible: function() {
      return Element.visible('formatting_guide')
    },

    show: function() {
      visible = this.visible()

      new Effect.BlindDown('formatting_guide', {duration: 0.2})
      Element.activate('formatting_guide_link', true)

      return visible
    },

    hide: function() {
      visible = this.visible()
      
      new Effect.BlindUp('formatting_guide', {duration: 0.2})
      Element.activate('formatting_guide_link', false)

      return visible
    }
  }
}
