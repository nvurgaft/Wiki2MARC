
function recordsController($log, recordsService) {
    
    var vm = this;
    vm.data = [];
    recordsService.getFiles().then(function(response) {
        vm.data = response;
    }, function(response) {
        $log.error(response);
    });
};

angular.module('protowiki').controller('recordsController', recordsController);