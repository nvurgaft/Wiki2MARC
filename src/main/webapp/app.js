
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
            });
}

function run() { }

angular.module('protowiki', ['ui.bootstrap', 'ui.router', 'smart-table', 'ui.ace', 'ui.select', 'ngSanitize'])
        .config(config)
        .run(run);