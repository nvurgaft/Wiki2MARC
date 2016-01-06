
function sparqlService($http, $log) {
    return {
        get: function(inputQuery) {
            return $http.get('rest/sparql', {
                params: {
                    query: inputQuery
                }
            }).then(function(response) {
                return response.data;
            }, function(response) {
                return response.status + " : " + response.data;
            });
        }
    };
}

angular.module("protowiki").factory('sparqlService', sparqlService);
