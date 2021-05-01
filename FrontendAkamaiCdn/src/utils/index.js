export function groupBy(arr, field) {
    return Object.entries(
        arr.reduce((reducer, next) => {
            reducer[next[field]] = reducer[next[field]] || [];
            reducer[next[field]].push({
                ...next,
            });
            return reducer;
        }, {})
    );
}

export function buildQs(obj) {
    return (
        "?" +
        Object.entries(obj)
            .map(([param, value]) => `${param}=${value}`)
            .join("&")
    );
}