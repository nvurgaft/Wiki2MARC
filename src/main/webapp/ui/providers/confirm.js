
confirmModalController.$inject = ["title", "message"];
function confirmModalController(title, message) {
    var vm = this;

    vm.title = title;
    vm.message = message;
}

angular.module('confirmModalController', []).controller('confirmModalController', confirmModalController);

function confirm() {

    var size;
    return {
        setSize: function (value) {
            size = value;
            return this;
        },
        $get: ["$uibModal", function ($uibModal) {
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
        }]
    };
}

angular.module('confirm', ['confirmModalController']).provider('confirm', confirm);
