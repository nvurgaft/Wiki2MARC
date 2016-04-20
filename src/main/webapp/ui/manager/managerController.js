
function managerController($log, managerService) {

    var vm = this;

    vm.disablePersistence = false;

    vm.preferences = {
        login: "",
        password: "",
        host: "",
        port: 0,
        disableLocalPersistence: false
    };
    managerService.getAll().then(function (response) {
        $log.debug("getAll: ", response);
        vm.preferences = {
            login: response.login,
            password: response.password,
            host: response.host,
            port: response.port,
            disableLocalPersistence: response.disableLocalPersistence
        };
    }, function (response) {
        $log.error(response);
    });
    
    vm.saved = -1;
    vm.save = function () {
        vm.saved = -1;
        $log.debug("saved: ", vm.preferences);
        managerService.update().then(function(response) {
            $log.debug(response);
            vm.saved = 0;
        }, function(response) {
            $log.error(response);
            vm.saved = 1;
        });
    };
}

angular.module('app').controller('managerController', managerController);