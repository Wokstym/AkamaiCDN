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

export class Format {
    static byte = [
        {unit: "b", to: 512, divider: 1},
        {unit: "kb", to: 524288, divider: 1024},
        {unit: "mb", divider: 1048576}
    ]

    static percent = [
        {unit: "%",  divider: 1},
    ]

    static millisecond = [
        {unit: "ms", to: 500, divider: 1},
        {unit: "s", to: 60000, divider: 1000},
        {unit: "m", divider: 60000}
    ]
}