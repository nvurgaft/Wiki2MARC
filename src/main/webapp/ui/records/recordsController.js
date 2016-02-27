
/* global _ */

function recordsController($log, confirm, recordsService) {

    var vm = this;
    vm.data = [];

    vm.currentPage = 1;
    vm.itemsPerPage = 10;

    vm.onPageSelected = function (pageNum) {
        $log.debug("Selected page " + pageNum);
    };

    recordsService.getFiles().then(function (response) {
        vm.data = response;
        vm.totalFiles = vm.data.length;
    }, function (response) {
        $log.error(response);
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

    vm.clickme = function () {
        confirm.show("Hello").then(function (response) {
            console.log(response);
        }, function (response) {
            console.error(response);
        });
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
}
;

angular.module('protowiki').controller('recordsController', recordsController);
