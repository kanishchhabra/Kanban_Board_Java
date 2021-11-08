module com.example.a2javaprogramming {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.a2javaprogramming to javafx.fxml;
    exports com.example.a2javaprogramming;
}