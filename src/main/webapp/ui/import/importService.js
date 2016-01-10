
function importService($http, $q, $log) {

    var path = "";
    var serviceName = "ImportService";
    return {
        uploadFile: function (file) {
            $log.debug("In " + serviceName + " uploading file");
            return $http.post(path, file, {
                headers: {
                }
            }).then(function (response) {
                return response;
            }, function (response) {
                return $q.reject(response.status + " : " + response.data);
            });
        },
        uploadBatch: function (files) {
            $log.debug("In " + serviceName + " uploading " + files.length + " file");
            return $http.post(path, files, {
                headers: {
                }
            }).then(function (response) {
                return response;
            }, function (response) {
                return $q.reject(response.status + " : " + response.data);
            });
        }
    };
}

angular.module('protowiki').factory('importService', importService);
