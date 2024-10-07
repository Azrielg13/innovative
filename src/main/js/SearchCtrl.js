com.digitald4.iis.SearchCtrl = function($filter, searchService) {
  this.filter = $filter;
	this.searchService = searchService;
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
  searchResult.urlType = searchResult.type.toLowerCase();
  if (searchResult.type == 'Patient' && searchResult.items) {
    searchResult.items.forEach(
        patient => patient.info = "DOB " + this.filter('date')(patient.dateOfBirth, 'MM/dd/yyyy'));
  }
}

com.digitald4.iis.SearchCtrl.prototype.close = function() {
  this.showResults = false;
}