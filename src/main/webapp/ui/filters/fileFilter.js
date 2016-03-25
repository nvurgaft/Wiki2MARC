
function fileFilter() {
    return function (data, extension) {
        if (angular.isString(extension)) {
            if (!extension.startsWith('.')) {
                extension = '.' + extension;
            }
            if (angular.isObject(data)) {
                return data.endsWith(extension);
            } else if (angular.isArray(data)) {
                return _.filter(data, function (item) {
                    return item.endsWith(extension);
                });
            }
        }
    };
}