
/* global _ */

function recordsController($log, confirm, recordsService) {

    var vm = this;
    vm.data = [];
    vm.logs = [];

    vm.currentPage = 1;
    vm.itemsPerPage = 10;

    vm.aceModel = "";
    vm.mode = 'XML';
    vm.theme = 'Chrome';

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

    vm.onPageSelected = function (pageNum) {
        $log.debug("Selected page " + pageNum);
    };

    vm.doneLoadingFiles = false;
    recordsService.getFiles().then(function (response) {
        vm.data = response;
        vm.totalFiles = vm.data.length;
    }, function (response) {
        $log.error(response);
    })['finally'](function () {
        vm.doneLoadingFiles = true;
    });

    vm.doneLoadingLogFiles = false;
    recordsService.getLogs().then(function (response) {
        vm.logs = response;
        vm.totalLogFiles = vm.logs.length;
    }, function (response) {
        $log.error(response);
    })['finally'](function () {
        vm.doneLoadingLogFiles = true;
    });

    vm.parseXMLFile = function (fileName) {
        vm.processComplete = false;
        recordsService.postXMLFileDetails(fileName).then(function (response) {
            vm.respStatus = response.status;
        }, function (response) {
            vm.respStatus = response.status;
            $log.error(response);
        })['finally'](function () {
            vm.processComplete = true;
        });
    };

    vm.downloadFile = function (fileName) {

    };

    vm.deleteFile = function (fileName) {
        vm.processComplete = false;

        confirm.show("Are you sure you want to delete this file ?").then(function () {
            return recordsService.deleteFile(fileName);
        }).then(function (response) {
            $log.debug("success: " + response);
            var idx = _.findIndex(vm.data, function (file) {
                return (file.name === fileName);
            });
            vm.data.splice(idx, 1);
            vm.selectedFile = false;
        }, function (response) {
            $log.warn(response);
        })['finally'](function () {
            vm.processComplete = true;
        });
    };
    
    vm.saveFileChanges = function(file) {
        
    };
    
    vm.revertFileChanges = function(file) {
        
    };
}

angular.module('app').controller('recordsController', recordsController);
