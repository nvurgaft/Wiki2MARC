
function config($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/main");
    $stateProvider
            .state('main', {
                url: "/main",
                controller: "mainController as vm",
                templateUrl: "ui/main.html"
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
            });
}

function run() { }

angular.module('protowiki', ['ui.bootstrap', 'ui.router', 'smart-table', 'ui.ace', 
    'angularFileUpload' ,'ui.select', 'ngSanitize'])
        .config(config)
        .run(run);