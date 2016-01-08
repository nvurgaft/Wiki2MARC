
function mainController(mainService) {
    var vm = this;
    vm.serverStatus = "";
    mainService.getServerHeartbeat().finally(function (status) {
        vm.serverStatus = "[ " + status + " ]";
    });
    
    vm.databaseStatus = "";
    mainService.getDatabaseHeartbeat().finally(function(status) {
        vm.databaseStatus + "[ " + status + " ]";
    });
}

angular.module('protowiki').controller('mainController', mainController);
