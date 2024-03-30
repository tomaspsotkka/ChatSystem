module dk.via.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.via.chatsystem to javafx.fxml;
    exports dk.via.chatsystem;
}