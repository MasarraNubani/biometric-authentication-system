package com.example.authenticationproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.*;
import java.util.Arrays;
import java.io.File;

public class AuthApp extends Application {

    private static final double MinEyeWidth = 25.0;
    private static final double MaxEyeWidth = 35.0;

    private static final double MinEyeHeight = 10.0;
    private static final double MaxEyeHeight = 15.0;

    private static final double MinNoseLength = 40.0;
    private static final double MaxNoseLength = 55.0;

    private static final double MinNoseWidth = 20.0;
    private static final double MaxNoseWidth = 30.0;

    private static final double MinMouthWidth = 40.0;
    private static final double MaxMouthWidth = 50.0;

    private static final double MinMouthHeight = 8.0;
    private static final double MaxMouthHeight = 12.0;

    private static final double MinChinLength = 15.0;
    private static final double MaxChinLength = 25.0;

    private static final double MinFaceWidth = 70.0;
    private static final double MaxFaceWidth = 90.0;

    private static final double MinFaceHeight = 100.0;
    private static final double MaxFaceHeight= 120.0;

    private static final double MinEarSize = 20.0;
    private static final double MaxEarSize = 30.0;

    private static final List<String> featureNames = Arrays.asList(
            "Eye Width",
            "Eye Height",
            "Nose Length",
            "Nose Width",
            "Mouth Width",
            "Mouth Height",
            "Chin Length",
            "Face Width",
            "Face Height",
            "Ear Size"
    );

    //    private static final int NUM_USERS = 5;
    private static final int NUM_FEATURES = featureNames.size();

    private File usersFile;

    private List<User> users = new ArrayList<>();

    private ComboBox<String> distanceMetricBox;
    private ComboBox<String> userSelectionBox;
    private Spinner<Double> thresholdSpinner;
    private Label resultLabel;
    private Label fmrLabel;
    private Label fnmrLabel;

    private TextField[] featureFields = new TextField[featureNames.size()];

    private VBox vboxResults;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Authentication System Using Biometric ");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label instructionLabel = new Label("Enter your biometric features to verify your identity:");
        instructionLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: black;");
        grid.add(instructionLabel, 0, 0, 2, 1);
        GridPane.setMargin(instructionLabel, new Insets(0, 0, 15, 0));

        Button chooseFileButton = new Button("Upload Users File");
        chooseFileButton.setStyle("-fx-font-size: 12px; -fx-padding: 6px 10px;");
        HBox hbChooseFile = new HBox(1);
        hbChooseFile.setAlignment(Pos.CENTER_LEFT);
        hbChooseFile.getChildren().add(chooseFileButton);
        grid.add(hbChooseFile, 0, 1, 1, 1);
        GridPane.setMargin(hbChooseFile, new Insets(0, 0, 10, 0));

        Label userLabel = new Label("Select User:");
        userLabel.setStyle("-fx-font-size: 12px;");
        grid.add(userLabel, 0, 2);

        userSelectionBox = new ComboBox<>();
        userSelectionBox.setDisable(true);
        userSelectionBox.setPrefWidth(200);
        grid.add(userSelectionBox, 1, 2);
        GridPane.setMargin(userSelectionBox, new Insets(0, 0, 15, 0));

        GridPane featuresGrid = new GridPane();
        featuresGrid.setHgap(7);
        featuresGrid.setVgap(1);
        featuresGrid.setAlignment(Pos.CENTER_LEFT);

        int columns = 2;
        int row = 0;
        int col = 0;

        for (int i = 0; i < featureNames.size(); i++) {
            String feature = featureNames.get(i);

            Label rangeLabel = new Label("Range: " + getFeatureMin(feature) + " - " + getFeatureMax(feature));
            rangeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

            Label featureLabel = new Label(feature + ":");
            featureLabel.setStyle("-fx-font-size: 14px; -fx-pref-width: 120px;");

            TextField featureField = new TextField();
            featureField.setPrefWidth(150);
            featureField.setPromptText("Enter " + feature + " value");

            featureFields[i] = featureField;

            VBox featureVBox = new VBox(3);
            featureVBox.setAlignment(Pos.CENTER_LEFT);
            featureVBox.getChildren().addAll(rangeLabel, featureLabel, featureField);

            featuresGrid.add(featureVBox, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }

        grid.add(featuresGrid, 0, 3, 2, 1);
        GridPane.setMargin(featuresGrid, new Insets(0, 0, 15, 0));

        Label thresholdLabel = new Label("Enter Threshold Value:");
        thresholdLabel.setStyle("-fx-font-size: 14px;");
        grid.add(thresholdLabel, 0, 4);

        thresholdSpinner = new Spinner<>();
        thresholdSpinner.setEditable(true);
        thresholdSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, Double.MAX_VALUE, 10.0, 1.0));
        thresholdSpinner.setTooltip(new Tooltip("Enter a value greater than 0."));
        thresholdSpinner.setDisable(true);
        thresholdSpinner.setPrefWidth(150);
        grid.add(thresholdSpinner, 1, 4);
        GridPane.setMargin(thresholdSpinner, new Insets(0, 0, 10, 0));

        Label thresholdRangeLabel = new Label("Threshold must be greater than 0.");
        thresholdRangeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");
        grid.add(thresholdRangeLabel, 1, 4);
        thresholdRangeLabel.setVisible(false);

        Label metricLabel = new Label("Select Distance Metric:");
        metricLabel.setStyle("-fx-font-size: 14px;");
        grid.add(metricLabel, 0, 5);

        distanceMetricBox = new ComboBox<>();
        distanceMetricBox.getItems().addAll(
                "Absolute Distance",
                "Cosine Distance",
                "Manhattan Distance",
                "Hamming Distance"
        );
        distanceMetricBox.getSelectionModel().selectFirst();
        distanceMetricBox.setDisable(true);
        distanceMetricBox.setPrefWidth(150);
        grid.add(distanceMetricBox, 1, 5);
        GridPane.setMargin(distanceMetricBox, new Insets(0, 0, 15, 0));

        Button authButton = new Button("Authenticate");
        authButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        authButton.setDisable(true);

        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");
        resetButton.setDisable(true);

        HBox hbButtons = new HBox(10);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);
        hbButtons.getChildren().addAll(authButton, resetButton);
        grid.add(hbButtons, 1, 6);
        GridPane.setMargin(hbButtons, new Insets(0, 0, 15, 0));

        vboxResults = new VBox(4);
        vboxResults.setAlignment(Pos.CENTER);

        resultLabel = new Label("");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");

        fmrLabel = new Label("");
        fmrLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

        fnmrLabel = new Label("");
        fnmrLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

        vboxResults.getChildren().addAll(resultLabel, fmrLabel, fnmrLabel);
        grid.add(vboxResults, 0, 7, 2, 1);
        GridPane.setMargin(vboxResults, new Insets(0, 0, 0, 0));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(10, 10, 10, 10));

        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Users File (users.csv)");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                usersFile = selectedFile;
                System.out.println("Selected file: " + usersFile.getAbsolutePath());

                List<User> loadedUsers = User.loadUsersFromCSV(usersFile.getAbsolutePath());
                System.out.println("Number of users: " + loadedUsers.size());
                if (loadedUsers.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Users file is empty or invalid.");
                    return;
                }

                userSelectionBox.getItems().clear();

                for (int i = 0; i < loadedUsers.size(); i++) {
                    User user = loadedUsers.get(i);
                    userSelectionBox.getItems().add(user.userID);
                    System.out.println("Added user to dropdown: " + user.userID);
                }

                userSelectionBox.getSelectionModel().selectFirst();
                userSelectionBox.setDisable(false);

                for (int i = 0; i < featureFields.length; i++) {
                    TextField field = featureFields[i];
                    field.setDisable(false);
                }

                thresholdSpinner.setDisable(false);
                distanceMetricBox.setDisable(false);
                authButton.setDisable(false);
                resetButton.setDisable(false);

                thresholdRangeLabel.setVisible(false);

                users = loadedUsers;
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "No file selected.");
            }
        });

        resetButton.setOnAction(e -> {
            for (int i = 0; i < featureNames.size(); i++) {
                String feature = featureNames.get(i);
                featureFields[i].setText("");
                // featureFields[i].setText(String.valueOf(getFeatureMin(feature)));
            }
            thresholdSpinner.getValueFactory().setValue(10.0);
            distanceMetricBox.getSelectionModel().selectFirst();
            resultLabel.setText("");
            fmrLabel.setText("");
            fnmrLabel.setText("");
            thresholdRangeLabel.setVisible(false);

            if (vboxResults.getChildren().size() > 3) {
                vboxResults.getChildren().remove(3, vboxResults.getChildren().size());
            }
        });

        authButton.setOnAction(e -> {
            double[] inputFeatures = getInputFeatures();
            if (inputFeatures == null) {
                return;
            }

            double threshold = thresholdSpinner.getValue();

            if (threshold <= 0 || threshold >= 100) {
                thresholdRangeLabel.setVisible(true);
                showAlert(Alert.AlertType.ERROR, "Error Input", "Threshold should be greater than 0 and less than 100.");
                return;
            } else {
                thresholdRangeLabel.setVisible(false);
            }

            String selectedMetric = distanceMetricBox.getValue();
            String selectedUserID = userSelectionBox.getValue();

            User selectedUser = null;
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                if (user.userID.equals(selectedUserID)) {
                    selectedUser = user;
                    break;
                }
            }

            if (selectedUser == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected user does not exist.");
                return;
            }

            boolean allInRange = true;
            StringBuilder errorMessage = new StringBuilder("The features are out of range:\n");
            for (int i = 0; i < featureNames.size(); i++) {
                String feature = featureNames.get(i);
                double value = inputFeatures[i];
                if (value < getFeatureMin(feature) || value > getFeatureMax(feature)) {
                    allInRange = false;
                    errorMessage.append("- ").append(feature).append(" (Range: ").append(getFeatureMin(feature))
                            .append(" - ").append(getFeatureMax(feature)).append(")\n");
                }
            }

            if (!allInRange) {
                showAlert(Alert.AlertType.ERROR, "Feature Range Error", errorMessage.toString());
                return;
            }

            AuthenticationResult authResult = authenticateUser(inputFeatures, selectedUser, threshold, selectedMetric);
            displayResults(authResult, users, inputFeatures, selectedMetric, threshold, selectedUser);
        });

        Scene scene = new Scene(scrollPane, 600, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private double[] getInputFeatures() {
        double[] input = new double[NUM_FEATURES];
        for (int i = 0; i < featureNames.size(); i++) {
            String feature = featureNames.get(i);
            TextField featureField = featureFields[i];
            try {
                double value = Double.parseDouble(featureField.getText().trim());
                input[i] = value;
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Enter a valid value for " + feature + ".");
                return null;
            }
        }
        return input;
    }

    private AuthenticationResult authenticateUser(double[] inputFeatures, User selectedUser, double threshold, String distanceMetric) {
        double distance = calculateDistance(inputFeatures, selectedUser.features, distanceMetric);
        if (distance < threshold) {
            return new AuthenticationResult(true, selectedUser.userID);
        }
        return new AuthenticationResult(false, null);
    }

    private double calculateDistance(double[] input, double[] stored, String metric) {
        switch (metric) {
            case "Absolute Distance":
                return calculateAbsoluteDistance(input, stored);
            case "Cosine Distance":
                return calculateCosineDistance(input, stored);
            case "Manhattan Distance":
                return calculateManhattanDistance(input, stored);
            //            case "Hamming Distance":
            //                return calculateHammingDistance(input, stored);
            default:
                return Double.MAX_VALUE;
        }
    }

    private double calculateAbsoluteDistance(double[] input, double[] stored) {
        double sum = 0.0;
        for (int i = 0; i < input.length; i++) {
            sum += Math.abs(input[i] - stored[i]);
        }
        return sum;
    }

    private double calculateCosineDistance(double[] input, double[] stored) {
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < input.length; i++) {
            dot += input[i] * stored[i];
            normA += Math.pow(input[i], 2);
            normB += Math.pow(stored[i], 2);
        }
        return 1 - (dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10));
    }

    private double calculateManhattanDistance(double[] input, double[] stored) {
        double sum = 0.0;
        for (int i = 0; i < input.length; i++) {
            sum += Math.abs(input[i] - stored[i]);
        }
        return sum;
    }

//    private double calculateHammingDistance(double[] input, double[] stored) {
//        int[] binaryInput = binarizeFeatures(input);
//        int[] binaryStored = binarizeFeatures(stored);
//        int distance = 0;
//        for (int i = 0; i < binaryInput.length; i++) {
//            if (binaryInput[i] != binaryStored[i]) {
//                distance++;
//            }
//        }
//        return distance;
//    }


//    private double calculateMedian(double[] arr) {
//        double[] copy = Arrays.copyOf(arr, arr.length);
//        Arrays.sort(copy);
//        int middle = copy.length / 2;
//        if (copy.length % 2 == 0) {
//            return (copy[middle - 1] + copy[middle]) / 2.0;
//        } else {
//            return copy[middle];
//        }
//    }

    private void displayResults(AuthenticationResult authResult, List<User> users, double[] inputFeatures, String distanceMetric, double threshold, User selectedUser) {
        if (authResult == null) {
            return;
        }

        if (authResult.isAuthenticated()) {
            resultLabel.setText("User Accepted. Matched with: " + authResult.getMatchedUserID());
            resultLabel.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else {
            resultLabel.setText("User Rejected.");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
        }

        double fmr = calculateFMR(users, inputFeatures, distanceMetric, threshold);
        double fnmr = calculateFNMR(users, inputFeatures, distanceMetric, threshold, selectedUser);

        fmrLabel.setText(String.format("FMR (False Match Rate): %.2f%%", fmr * 100));
        fnmrLabel.setText(String.format("FNMR (False Non-Match Rate): %.2f%%", fnmr * 100));
    }

    private double calculateFMR(List<User> users, double[] inputFeatures, String distanceMetric, double threshold) {
        int impostorAccepted = 0;
        int totalImpostorAttempts = 0;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            double distance = calculateDistance(inputFeatures, user.features, distanceMetric);
            if (distance < threshold && !Arrays.equals(inputFeatures, user.features)) {
                impostorAccepted++;
            }
            totalImpostorAttempts++;
        }

        return (double) impostorAccepted / totalImpostorAttempts;
    }

    private double calculateFNMR(List<User> users, double[] inputFeatures, String distanceMetric, double threshold, User selectedUser) {
        int genuineRejected = 0;
        int totalGenuineAttempts = 0;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.userID.equals(selectedUser.userID)) {
                double distance = calculateDistance(inputFeatures, user.features, distanceMetric);
                if (distance >= threshold) {
                    genuineRejected++;
                }
                totalGenuineAttempts++;
            }
        }

        if (totalGenuineAttempts == 0) {
            return 0.0;
        }

        return (double) genuineRejected / totalGenuineAttempts;
    }

//    private double getFeatureRange(String feature) {
//        return getFeatureMax(feature) - getFeatureMin(feature);
//    }

    private double getFeatureMin(String feature) {
        switch (feature) {
            case "Eye Width":
                return MinEyeWidth;
            case "Eye Height":
                return MinEyeHeight;
            case "Nose Length":
                return MinNoseLength;
            case "Nose Width":
                return MinNoseWidth;
            case "Mouth Width":
                return MinMouthWidth;
            case "Mouth Height":
                return MinMouthHeight;
            case "Chin Length":
                return MinChinLength;
            case "Face Width":
                return MinFaceWidth;
            case "Face Height":
                return MinFaceHeight;
            case "Ear Size":
                return MinEarSize;
            default:
                return 0.0;
        }
    }

    private double getFeatureMax(String feature) {
        switch (feature) {
            case "Eye Width":
                return MaxEyeWidth;
            case "Eye Height":
                return MaxEyeHeight;
            case "Nose Length":
                return MaxNoseLength;
            case "Nose Width":
                return MaxNoseWidth;
            case "Mouth Width":
                return MaxMouthWidth;
            case "Mouth Height":
                return MaxMouthHeight;
            case "Chin Length":
                return MaxChinLength;
            case "Face Width":
                return MaxFaceWidth;
            case "Face Height":
                return MaxFaceHeight;
            case "Ear Size":
                return MaxEarSize;
            default:
                return 1.0;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
