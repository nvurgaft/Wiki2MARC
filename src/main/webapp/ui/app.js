"strict mode";

var APP_NAME = "app";

angular.module(APP_NAME, ['ui.bootstrap', 'ui.router', 'smart-table', 'ui.ace',
    'angularFileUpload', 'ui.select', 'ngSanitize', 'confirm']);

config.$inject = ['$stateProvider', '$urlRouterProvider', 'confirmProvider'];
function config($stateProvider, $urlRouterProvider, confirmProvider) {
    $urlRouterProvider.otherwise("/main");
    $stateProvider
            .state('main', {
                url: "/main",
                controller: "mainController as vm",
                templateUrl: "ui/main/main.html"
            })
            .state('main.sparql', {
                url: "/sparql",
                controller: "sparqlController as vm",
                templateUrl: "ui/sparql-ui/sparql-client.html"
            })
            .state('main.records', {
                url: "/records",
                controller: "recordsController as vm",
                templateUrl: "ui/records/records.html"
            })
            .state('main.import', {
                url: "/import",
                controller: "importController as vm",
                templateUrl: "ui/import/import.html"
            })
            .state('main.manager', {
                url: "/manager",
                controller: "managerController as vm",
                templateUrl: "ui/manager/manager.html"
            });

    confirmProvider.setSize('sm');
}
angular.module(APP_NAME).config(config);
angular.module(APP_NAME).factory('mainService', mainService);

angular.module(APP_NAME).controller('mainController', mainController);
angular.module(APP_NAME).controller('importController', importController);
angular.module(APP_NAME).controller('managerController', managerController);
angular.module(APP_NAME).controller('recordsController', recordsController);
angular.module(APP_NAME).controller('sparqlController', sparqlController);

angular.module(APP_NAME).factory('importService', importService);
angular.module(APP_NAME).factory('managerService', managerService);
angular.module(APP_NAME).factory('recordsService', recordsService);
angular.module(APP_NAME).factory('sparqlService', sparqlService);

angular.module(APP_NAME).filter('startAt', startAt);
angular.module(APP_NAME).filter('fileFilter', fileFilter);
angular.module(APP_NAME).filter('decimalFilter', decimalFilter);