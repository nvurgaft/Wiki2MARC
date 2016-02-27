
function sparqlController($log, sparqlService) {

    var vm = this;

    vm.aceModel = "Enter your SPARQL query here...";
    vm.modes = ['SQL', 'XML', 'HTML'];
    vm.mode = vm.modes[0];
    vm.themes = ['Chrome', 'Monokai'];
    vm.theme = vm.themes[0];
    
    vm.aceOptions = {
        mode: vm.mode.toLowerCase(),
        theme: vm.theme.toLowerCase(),
        onLoad: function (_editor) {
            $log.debug('aceLoaded');
            vm.modeChanged = function (mode) {
                $log.debug('mode ' + mode + ' selected');
                _editor.getSession().setMode("ace/mode/" + vm.mode.toLowerCase());
            };
            vm.themeChanged = function (theme) {
                $log.debug('theme ' + theme + ' selected');
                _editor.setTheme("ace/theme/" + vm.theme.toLowerCase());
            };
        },
        onChange: function (e) {
            $log.debug('aceChanged');
        }
    };

    vm.apis = [
        {name: 'DBPedia (SNORQL)', url: 'http://dbpedia.org/sparql'},
        {name: 'Wikidata (SPARQL)', url: 'https://query.wikidata.org/sparql'}
    ];
    vm.selectedApi = vm.apis[0];
    
    vm.runningQuery = false;
    vm.send = function () {
        vm.runningQuery = true;
        sparqlService.postQuery(vm.aceModel, vm.selectedApi.url).then(function(response) {
            vm.aceModel = response;
        }, function(response) {
            vm.aceModel = response;
        })['finally'](function() {
            vm.runningQuery = false;
        });
    };

    vm.clear = function () {
        vm.aceModel = "";
    };
}

angular.module('app').controller('sparqlController', sparqlController);
