module com.tsa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.tsa to javafx.fxml;
    exports com.tsa;
}
