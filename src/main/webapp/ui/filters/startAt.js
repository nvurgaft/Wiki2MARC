
startAt.$inject = [];
function startAt() {
    return function(list, p, i) {
        return list.slice(p, p+i);
    };
}
