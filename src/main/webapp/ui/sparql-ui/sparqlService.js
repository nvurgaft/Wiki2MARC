
function sparqlService($http, $log) {
    return {
        postQuery: function(query, endpoint) {
            $log.debug("Posting SPARQL query to remote API");
            return $http.get('rest/sparql', {
                params: {
                    query: query,
                    endpoint: endpoint
                },
                headers: {
                    'Content-Type': 'text/plain'
                }
            }).then(function(response) {
                return response.data;
            }, function(response) {
                return response.data;
            });
        }
    };
}

angular.module("protowiki").factory('sparqlService', sparqlService);
