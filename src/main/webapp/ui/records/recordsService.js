/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function recordsService($http, $q, $log) {
    return {
        getAll: function() {
            $log.debug('fetching dummy data');
            return $q.when([{
                    name: "Douglas Adams",
                    qid: "42",
                    viaf: "22443300"
            },{
                    name: "Joe Smith",
                    qid: "43",
                    viaf: "22443301"
            },{
                    name: "Rex Nebular",
                    qid: "44",
                    viaf: "22443302"
            }]);
        }
    };
}

angular.module('protowiki').factory('recordsService', recordsService);