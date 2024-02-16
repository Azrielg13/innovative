com.digitald4.iis.NoteAddCtrl = function($window, noteService) {
  this.style = {top: ($window.visualViewport.pageTop + 20) + 'px'};
  this.noteService = noteService;
  this.types = ['General', 'Important', 'Concerning'];
  this.type = this.type || 'General';
}

com.digitald4.iis.NoteAddCtrl.prototype.createNote = function() {
  if (this.onStateChange) {
    this.onStateChange();
  }
  this.noteService.create(
      {entityType: this.entityType, entityId: this.entityId, note: this.note, type: this.type},
      note => {
        if (this.notes) {
          this.notes.unshift(note);
        }
        if (this.onCreate) {
          this.onCreate(note);
        }
        if (this.onStateChange) {
          this.onStateChange();
        }
        this.note = "";
        this.closeDialog();
      });
}

com.digitald4.iis.NoteAddCtrl.prototype.closeDialog = function() {
  this.isVisible = false;
}
