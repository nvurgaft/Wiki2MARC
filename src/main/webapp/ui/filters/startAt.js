
startAt.$inject = [];
function startAt() {
    return function (list, p, i) {
        if (Array.isArray(list) && list.length > 0) {
            if ((Number.isFinite(p) && p > 0) && (Number.isFinite(i) && i > p)) {
                return list.slice(p, p + i);
            }
        }
        return list;
    };
}
