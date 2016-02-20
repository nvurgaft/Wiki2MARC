
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
