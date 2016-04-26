
/* global _ */

function importController($log, FileUploader) {
    var vm = this;
    vm.uploader = new FileUploader({
        url: "rest/upload/xml_file",
        withCredentials: true,
        method: "POST",
        removeAfterUpload: true,
        onAfterAddingFile: function (item) {        
            $log.info("File uploaded", item._file.name);
            // we don't allow duplicates in the file queue
            var found = _.find(this.queue, function (current) {
                // if the names are equals but different objects
                return (current._file.name === item._file.name && current!==item);
            });
            if (angular.isDefined(found)) {
                vm.removeFile(found);
            }
        },
        onCompleteAll: function () {
            $log.info("Files uploaded");
        },
        onErrorItem: function (item, response, status) {
            $log.error("And error occured while uploading file: " + status);
        }
    });

    vm.uploadFile = function (file) {
        file.upload();
    };

    vm.removeFile = function (file) {
        file.remove();
    };

    vm.uploadQueue = function () {
        vm.uploader.uploadAll();
        $log.info('File queue uploaded');
    };

    vm.clearUploadQueue = function () {
        vm.uploader.clearQueue();
        $log.info('File queue cleared');
    };
}

angular.module('app').controller('importController', importController);