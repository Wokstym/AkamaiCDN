import { View, StyleSheet, Text } from '@react-pdf/renderer';
import { BORDER_STYLE , BORDER_COLOR} from "./const";

const TableCell = (props) => {
    const styles = StyleSheet.create({
        tableCol: {
            width: props.width + "%",
            borderStyle: BORDER_STYLE,
            borderColor: BORDER_COLOR,
            borderWidth: 1,
            borderLeftWidth: 0,
            borderTopWidth: 0
        },
        tableCell: {
            margin: 5,
            fontSize: 10
        }
    })

    return (
        <View style={styles.tableCol}>
            <Text style={styles.tableCell}>{props.children}</Text>
        </View>
    )
}

export default TableCell;