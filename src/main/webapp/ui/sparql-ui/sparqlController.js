
function sparqlController($log) {

    var vm = this;

    vm.aceModel = "Enter your SPARQL query here...";
    vm.modes = ['SQL', 'XML'];
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
        {name: 'Snorql / Wikipedia', email: 'adam@email.com'},
        {name: 'Sparql / Wikidata', email: 'amalie@email.com'}
    ];
    vm.selectedApi = vm.apis[0];

    vm.send = function () {

    };

    vm.clear = function () {
        vm.aceModel = "";
    };
}

angular.module('protowiki').controller('sparqlController', sparqlController);
