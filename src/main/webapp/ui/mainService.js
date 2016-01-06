
function mainService($log, $http) {
    return {
        getHeartbeat: function() {
            return $http.get("rest/heartbeat").then(function(response) {
                $log.debug(response.data);
                return response.data;
            }, function(response) {
                $log.debug(response.status + " : " + response.data);
                return response.status + " : " + response.data;
            });
        }
    };
}

angular.module('protowiki').factory('mainService', mainService);
