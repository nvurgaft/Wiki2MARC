
/* global parseFloat */

decimalFilter.$inject = [];
function decimalFilter() {
    return function (input, places) {
        if (Number.isFinite(input) && Number.isFinite(places)) {
            return parseFloat(Math.round(input * 1000) / 1000).toFixed(places);
        } else {
            return input;
        }
    };
}
