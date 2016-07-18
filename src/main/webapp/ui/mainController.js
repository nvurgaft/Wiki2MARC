
mainController.$inject = ['mainService']
function mainController(mainService) {
    var vm = this;

    vm.serverStatus = "";
    vm.serverStatusOK = false;
    mainService.getServerHeartbeat().then(function (status) {
        vm.serverStatusOK = true;
        vm.serverStatus = status;
    }, function (error) {
        vm.serverStatusOK = false;
    });

    vm.databaseStatus = "";
    vm.databaseStatusOK = false;
    mainService.getDatabaseHeartbeat().then(function (status) {
        vm.databaseStatusOK = true;
        vm.databaseStatus = status;
    }, function (error) {
        vm.databaseStatusOK = false;
    });
}