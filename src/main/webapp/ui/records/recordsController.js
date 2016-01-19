
function recordsController($log, recordsService) {
    
    var vm = this;
    vm.data = [];
    recordsService.getFiles().then(function(response) {
        vm.data = response;
    }, function(response) {
        $log.error(response);
    });
    
    vm.parseXMLFile = function(fileName) {
        recordsService.store(fileName).then(function(response) {
            
        }, function(response) {
            $log.error(response);
        });
    };
};

angular.module('protowiki').controller('recordsController', recordsController);