function toggleExtended() {
  var extended = $('extended')
  if (extended.style.display == 'none') {
    extended.style.display = 'block'
  } else {
    extended.style.display = 'none'
  }
}

function showProgress() {
  $("fileUploadName").appendChild(document.createTextNode('your file'))
  if($('AttachmentList')) $('AttachmentList').style.display = 'block'
  $('UploadProgress').style.display = 'block'
  if($('Explanation')) $('Explanation').style.display = 'none'
  return true
}

function addFile() {
  if (validateField('file', 'You have to select a file')) {
    showProgress()
    $('post_form').action = 'add_attachment';
    $('post_form').submit()
  }

  return false;
}

function relabelAttachment(name) {
  $("controls_for_" + name).style.display = 'none';
  $("label_for_" + name).style.display = 'inline';
  return false;
}

function saveNewLabel(input_id, old_label, progress_action) {
  var new_label = $(input_id).value;
  location.href =
    'relabel_attachment?old_label=' + escape(old_label) +
    '&new_label=' + escape(new_label) +
    '&progress_action=' + progress_action;
  return false;
}

function togglePeople(master, className) {
  var people = document.getElementsByClassName(className);
  if (master.checked) {
    for (i = 0; i < people.length; i++) { people[i].checked = true; }
  } else {
    for (i = 0; i < people.length; i++) { people[i].checked = false; }
  }
}

function toggleMaster(checkbox, master) {
  master = $(master)
  if(!master) return
  if (master.checked && !checkbox.checked) {
    master.checked = false;
  }
}

function toggleDimClientPeople(hide_as_private) {
  hide_as_private.checked ? dimClientPeople() : litClientPeople();
}

function dimClientPeople() {
  var people = document.getElementsByClassName("clientPeople");
  for (i = 0; i < people.length; i++) {
    people[i].checked = false;
    people[i].disabled = true;
  }
}

function litClientPeople() {
  var people = document.getElementsByClassName("clientPeople");
  for (i = 0; i < people.length; i++) { people[i].disabled = false; }
}

function addFileFromFileUpload() {
  if (validateField('file', 'You have to select a file')) {
    showProgress()
    $('file_form').submit()
  }
}

function cancelFileDialog() {
  var rows = $$("tbody#upload_rows tr").slice(1)
  for(var i = 0; i < rows.length; i++) {
    Element.remove(rows[i])
  }
  Element.update('file_form', $('file_form').innerHTML)
}