
function recordsController($log, recordsService) {
    
    var vm = this;
    vm.data = [];
    recordsService.getFiles().then(function(response) {
        vm.data = response;
    }, function(response) {
        $log.error(response);
    });
    
    vm.parseXMLFile = function(fileName) {
        vm.processComplete = false;
        recordsService.postXMLFileDetails(fileName).then(function(response) {
            vm.respStatus = response.status;
        }, function(response) {
            vm.respStatus = response.status;
            $log.error(response);
        })['finally'](function() {
            vm.processComplete = true;
        });
    };
};

angular.module('protowiki').controller('recordsController', recordsController);