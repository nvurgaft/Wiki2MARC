
function recordsController($log, recordsService) {
    
    var vm = this;
    vm.data = [];
    recordsService.getAll().then(function(response) {
        vm.data = response;
    });
};

angular.module('protowiki').controller('recordsController', recordsController);