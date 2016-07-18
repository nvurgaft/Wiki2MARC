
managerService.$inject = ["$http", "$q", "$log"];
function managerService($http, $q, $log) {

    var path = "";
    var serviceName = "managerService";
    return {
        getAll: function () {
            $log.debug("In " + serviceName + " uploading file");
            return $http.get(path).then(function (response) {
                return response;
            }, function (response) {
                return $q.reject(response.status + " : " + response.data);
            });
        },
        update: function (propertiesMap) {
            $log.debug("In " + serviceName + " updating properties");
            return $http.post(path, propertiesMap).then(function (response) {
                return response;
            }, function (response) {
                return $q.reject(response.status + " : " + response.data);
            });
        }
    };
}