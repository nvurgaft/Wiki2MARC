
function recordsService($http, $q, $log) {
    
    var path = "rest/records/";
    
    return {
        getFiles: function() {
            $log.info("Fetching stored files details");
            return $http.get(path + 'get-files').then(function(response) {
                return response.data;
            }, function(response) {
                return $q.reject(response.status + " : " + response.data);
            });
        },
        postXMLFileDetails: function(fileDetails) {
            return $http.post(path + '/parse-xml', fileDetails).then(function(response) {
                return response;
            }, function(response) {
                return $q.reject(response.status + " : " + response.data);
            });
        }
    };
}

angular.module('protowiki').factory('recordsService', recordsService);