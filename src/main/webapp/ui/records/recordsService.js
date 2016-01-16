
function recordsService($http, $q, $log) {
    return {
        getFiles: function() {
            $log.info("Fetching stored files details");
            return $http.get('rest/records/get-files').then(function(response) {
                return response.data;
            }, function(response) {
                return $q.reject(response.status + " : " + response.data);
            });
        },
        postXMLFileDetails: function(fileDetails) {
            
            return $http.post('rest/records/parse-xml')
        }
    };
}

angular.module('protowiki').factory('recordsService', recordsService);