
function mainService($log, $q, $http) {
    return {
        getServerHeartbeat: function() {
            return $http.get("rest/heartbeat/server").then(function(response) {
                $log.debug("got: " + response.status);
                return response.status;
            }, function(response) {
                $log.debug(response.status + " : " + response.data);
                return $q.reject(response.status + " : " + response.data);
            });
        },
        getDatabaseHeartbeat: function() {
            return $http.get("rest/heartbeat/database").then(function(response) {
                $log.debug("got: " + response.status);
                return response.status;
            }, function(response) {
                $log.error(response.status + " : " + response.data);
                return $q.reject(response.status + " : " + response.data);
            });
        }
    };
}

angular.module('protowiki').factory('mainService', mainService);
