import { View, StyleSheet } from '@react-pdf/renderer';


const styles = StyleSheet.create({
    tableRow: {
        margin: "auto",
        flexDirection: "row"
    }
});

const TableRow = (props) => {
    return(
        <View style={styles.tableRow}>
            {props.children}
        </View>
    )
}

export default TableRow;