
function confirmModalController(title, message, $uibModalInstance) {
    var vm = this;

    vm.title = title;
    vm.message = message;

    vm.ok = function () {
        $uibModalInstance.close(true);
    };

    vm.cancel = function () {
        $uibModalInstance.dismiss(false);
    };
}

angular.module('confirmModalController', []).controller('confirmModalController', confirmModalController);

function confirm() {

    var size;
    return {
        setSize: function (value) {
            size = value;
            return this;
        },
        $get: function ($uibModal) {
            return {
                show: function(message, title) {
                    return $uibModal.open({
                        animation: true,
                        templateUrl: 'ui/providers/confirm-tmpl.html',
                        controller: 'confirmModalController as vm',
                        size: size,
                        resolve: {
                            title: function () {
                                return title || "Confirm";
                            },
                            message: function () {
                                return message || "Are you sure ?";
                            }
                        }
                    }).result;
                }
            };
        }
    };
}

angular.module('confirm', ['confirmModalController']).provider('confirm', confirm);
