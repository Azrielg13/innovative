com.digitald4.iis.NoteTableCtrl = function($window, noteService) {
  this.style = {top: ($window.visualViewport.pageTop + 20) + 'px'};
  this.noteService = noteService;
  this.types = ['General', 'Important', 'Concerning'];
  this.type = this.type || 'General';
  if (this.allowAdd == undefined) {
  	this.allowAdd = this.entityId != undefined;
  }
}

com.digitald4.iis.NoteTableCtrl.prototype.createNote = function() {
  this.noteService.create(
      {entityType: this.entityType, entityId: this.entityId, note: this.note, type: this.type},
      note => {
        if (this.notes) {
          this.notes.unshift(note);
        }
        this.metadata.refresh();
        if (this.onCreate) {
          this.onCreate(note);
        }
        this.note = "";
        this.closeDialog();
      });
}

com.digitald4.iis.NoteTableCtrl.prototype.showAddNoteDialog = function() {
	this.addDialogShown = true;
}

com.digitald4.iis.NoteTableCtrl.prototype.closeDialog = function() {
  this.addDialogShown = false;
}
