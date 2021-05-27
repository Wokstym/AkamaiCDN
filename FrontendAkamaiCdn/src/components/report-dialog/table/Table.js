import { View, StyleSheet } from '@react-pdf/renderer';
import { BORDER_STYLE , BORDER_COLOR} from "./const";

const styles = StyleSheet.create({
    body: {
        padding: 10
    },
    table: {
        display: "table",
        width: "auto",
        borderStyle: BORDER_STYLE,
        borderColor: BORDER_COLOR,
        borderWidth: 1,
        borderRightWidth: 0,
        borderBottomWidth: 0
    }
});

const Table = (props) => {
    return (
        <View style={styles.table}>
            {props.children}
        </View>
    )
}

export default Table;