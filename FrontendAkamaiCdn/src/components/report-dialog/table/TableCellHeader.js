import { View, StyleSheet, Text } from '@react-pdf/renderer';
import { BORDER_STYLE , BORDER_COLOR} from "./const";

const TableCellHeader = (props) => {
    const styles = StyleSheet.create({
        tableColHeader: {
            width: props.width + "%",
            borderStyle: BORDER_STYLE,
            borderColor: BORDER_COLOR,
            borderBottomColor: '#000',
            borderWidth: 1,
            borderLeftWidth: 0,
            borderTopWidth: 0
        },
        tableCellHeader: {
            margin: 5,
            fontSize: 12,
            fontWeight: 500
        },
    })

    return (
        <View style={styles.tableColHeader}>
            <Text style={styles.tableCellHeader}>{props.children}</Text>
        </View>
    )
}

export default TableCellHeader;