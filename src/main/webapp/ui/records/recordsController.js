
function recordsController($log, recordsService) {
    
    var vm = this;
    vm.data = [];
    
    vm.currentPage = 1;
    vm.itemsPerPage = 10;
    
    vm.onPageSelected = function(pageNum) {
        $log.debug("Selected page " + pageNum);
    };
    
    recordsService.getFiles().then(function(response) {
        vm.data = response;
        vm.totalFiles = vm.data.length;
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