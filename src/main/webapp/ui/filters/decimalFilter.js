
/* global parseFloat */

decimalFilter.$inject = [];
function decimalFilter() {
    return function (input, places) {
        if (angular.isNumber(input) && angular.isNumber(places)) {
            return parseFloat(Math.round(input * 1000) / 1000).toFixed(places);
        } else {
            return input;
        }
    };
}
