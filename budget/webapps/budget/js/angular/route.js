var com = {
  digitald4: {
	  budget: {}
  }
};

com.digitald4.budget.router = function($routeProvider) {
	$routeProvider
		.when('/',
		    {
				controller: 'DefaultViewCtrl',
				templateUrl: 'html/defview.html'
		    })
		.when('/accounts',
				{
					controller: 'AccountsCtrl',
					templateUrl: 'html/accounts.html'
				})
		.when('/list',
		    {
				controller: 'ListCtrl',
				templateUrl: 'html/listview.html'
		    })
		.when('/cal',
			{
				controller: 'CalCtrl',
				templateUrl: 'html/calview.html'
			})
		.when('/accounting',
			{
				controller: 'AccountingCtrl',
				templateUrl: 'html/accview.html'
			})
		.when('/summary',
			{
				controller: 'SumView',
				templateUrl: 'html/sumview.html'
			})
		.otherwise({ redirectTo: '/'});
}
