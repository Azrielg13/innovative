com.digitald4.iis.SearchCtrl = function($filter, searchService, userService) {
  this.filter = $filter;
	this.searchService = searchService;
	this.userService = userService;
}

com.digitald4.iis.SearchCtrl.prototype.onKey = function(keyEvent) {
  if (keyEvent.which == 13) {
    this.search();
  }
}

com.digitald4.iis.SearchCtrl.prototype.search = function() {
  this.searching = true;

  var request = {searchText: this.searchText};

  this.searchService.search(request, response => {
    this.searchResults = response.items;
    this.searchResults.forEach(sr => this.setDisplayText(sr));
    this.searching = false;
    this.showResults = true;
  });
}

com.digitald4.iis.SearchCtrl.prototype.setDisplayText = function(searchResult) {
  searchResult.items = searchResult.items || [];
  searchResult.urlType = searchResult.type.toLowerCase();
  if (searchResult.type == 'Patient') {
    searchResult.items.forEach(patient => {
      patient.info = "DOB " + this.filter('date')(patient.dateOfBirth, 'MM/dd/yyyy');
      patient.url = "#" + searchResult.urlType + "/" + patient.id
    });
  } else if (searchResult.type == 'Invoice') {
    searchResult.items.forEach(
        invoice => invoice.url = this.userService.getFileUrl("invoice-" + invoice.id + ".pdf"));
  } else {
    searchResult.items.forEach(
        result => result.url = "#" + searchResult.urlType + "/" + result.id);
  }
}

com.digitald4.iis.SearchCtrl.prototype.close = function() {
  this.showResults = false;
}