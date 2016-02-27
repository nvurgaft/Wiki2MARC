
function importController($log, FileUploader) {
    var vm = this;
    vm.uploader = new FileUploader({
        url: "rest/upload/xml_file",
        withCredentials: true,
        method: "POST",
        removeAfterUpload: true
    });
    
    // callbacks
    vm.uploader.onAfterAddingFile = function(item) {
        $log.info("File uploaded", item);
    };
    vm.uploader.onCompleteAll = function() {
        $log.info("Files uploaded");
    };
    vm.uploader.onErrorItem = function(item, response, status) {
        $log.error("And error occured while uploading file: " + status);
    };
    
    vm.uploadFile = function(file) {
        file.upload();
    };
    
    vm.removeFile = function(file) {
        file.remove();
    };
    
    vm.uploadQueue = function() {
        vm.uploader.uploadAll();
        $log.info('File queue uploaded');
    };
    
    vm.clearUploadQueue = function() {
        vm.uploader.clearQueue();
        $log.info('File queue cleared');
    };
}

angular.module('app').controller('importController', importController);