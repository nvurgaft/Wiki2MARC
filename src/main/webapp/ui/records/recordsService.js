
function recordsService($http, $q, $log) {

    var path = "rest/records/";

    return {
        getFiles: function () {
            $log.debug("Fetching stored files list");
            return $http.get(path + 'get-files').then(function (response) {
                return response.data;
            }, function (response) {
                return $q.reject(response.status + " : " + response.data);
            });
        },
        getFileDetails: function(fileName) {
            $log.debug("Fetching stored file details");
            return $http.get(path + 'get-file-detail', {
                params: {
                    fileName: fileName
                }
            }).then(function (response) {
                return response.data;
            }, function (response) {
                return $q.reject(response.status + " : " + response.data);
            });
        },
        getLogs: function() {
            return $q.when([]);
        },
        postXMLFileDetails: function (fileDetails) {
            $log.debug("Posting XML file to be processed: " + fileDetails);
            return $http.get(path + 'xml-parse-file', {
                params: {
                    file: fileDetails
                }
            }).then(function (response) {
                return response.data;
            }, function (response) {
                return $q.reject(response.data);
            });
        },
        deleteFile: function (file) {
            $log.debug("Deleting file: " + file);
            return $http.delete(path + 'remove-file', {
                params: {
                    fileName: file
                }
            }).then(function(response) {
                return response.data;
            }, function(response) {
               return $q.reject(response.status + " : " + response.data); 
            });
        }
    };
}

angular.module('app').factory('recordsService', recordsService);