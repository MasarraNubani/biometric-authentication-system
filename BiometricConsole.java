package com.example.authenticationproject;

import java.util.*;

public class BiometricConsole {

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
    private double calculateDistance(double[] input, double[] stored, String metric, String selectedUserID, boolean shouldPrint) {
        double distance = 0.0;
        switch (metric) {
            case "Absolute Distance":
                distance = calculateAbsoluteDistance(input, stored);
                break;
            case "Cosine Distance":
                distance = calculateCosineDistance(input, stored);
                break;
            case "Manhattan Distance":
                distance = calculateManhattanDistance(input, stored);
                break;

            default:
                distance = Double.MAX_VALUE;
        }
        if (shouldPrint) {
            System.out.printf("Distance (%s) between inputs and user %s: %.4f%n", metric, selectedUserID, distance);
        }
        return distance;
    }

    private double calculateAbsoluteDistance(double[] input, double[] stored) {
        double sum = 0.0;
        for (int i = 0; i < input.length; i++) {
            sum += Math.abs(input[i] - stored[i]);
        }
        return sum;
    }

    private double calculateCosineDistance(double[] input, double[] stored) {
        double d = 0.0;
        double nA = 0.0;
        double nB = 0.0;
        for (int i = 0; i < input.length; i++) {
            d += input[i] * stored[i];
            nA += Math.pow(input[i], 2);
            nB += Math.pow(stored[i], 2);
        }
        return 1 - (d / (Math.sqrt(nA) * Math.sqrt(nB) + 1e-10)); // 1 - Cosine Similarity to get distance
    }

    private double calculateManhattanDistance(double[] input, double[] stored) {
        double sum = 0.0;
        for (int i = 0; i < input.length; i++) {
            sum += Math.abs(input[i] - stored[i]);
        }
        return sum;
    }





    private double calculateFMR(List<User> users, double[] inputFeatures, String distanceMetric, double threshold, String selectedUserID) {
        int impostorAccepted = 0;
        int totalImpostorAttempts = 0;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            if (user.userID.equals(selectedUserID)) {
                continue;
            }

            double distance = calculateDistance(inputFeatures, user.features, distanceMetric, user.userID, false);
            if (distance < threshold) {
                impostorAccepted++;
            }
            totalImpostorAttempts++;
        }


        if (totalImpostorAttempts == 0) {
            return 0.0;
        }

        return (double) impostorAccepted / totalImpostorAttempts;
    }

    private double calculateFNMR(List<User> users, double[] inputFeatures, String distanceMetric, double threshold, User selectedUser) {
        int genuineRejected = 0;
        int totalGenuineAttempts = 0;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);

            if (user.userID.equals(selectedUser.userID)) {
                double distance = calculateDistance(inputFeatures, user.features, distanceMetric, user.userID, false);

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

    private double calculateEER(List<User> users, double[] inputFeatures, String distanceMetric) {
        double bestEER = Double.MAX_VALUE;
        double bestThreshold = 0.0;

        for (double threshold = 0.1; threshold <= 100.0; threshold += 0.1) {
            String selectedUserID = users.get(0).userID;
            double fmr = calculateFMR(users, inputFeatures, distanceMetric, threshold, selectedUserID);
            double fnmr = calculateFNMR(users, inputFeatures, distanceMetric, threshold, users.get(0));
            double eer = Math.abs(fmr - fnmr);
            if (eer < bestEER) {
                bestEER = eer;
                bestThreshold = threshold;
            }
        }

        System.out.printf("Best Threshold for EER: %.2f with EER: %.2f%%%n", bestThreshold, bestEER * 100);
        return bestEER;
    }

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

    private static final List<String> NameOffeature = Arrays.asList(
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

    private List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        BiometricConsole app = new BiometricConsole();
        app.run();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        System.out.println(" Biometric Authentication Application ");

        initializeUsers();

        if (users.isEmpty()) {
            System.out.println("No user data available. Exiting the application.");
            return;
        }

        boolean continueAuthentication = true;

        while (continueAuthentication) {
            System.out.println("\nAvailable Users:");
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i).userID);
            }

            int userChoice = -1;
            while (userChoice < 1 || userChoice > users.size()) {
                System.out.print("\nSelect a user by number (1-" + users.size() + "): ");
                try {
                    userChoice = Integer.parseInt(in.nextLine());
                    if (userChoice < 1 || userChoice > users.size()) {
                        System.out.println("Invalid selection. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            User selectedUser = users.get(userChoice - 1);
            System.out.println("Selected User: " + selectedUser.userID);



            double[] inputFeatures = new double[NameOffeature.size()];
            System.out.println("\nEnter your biometric features:");
            for (int i = 0; i < NameOffeature.size(); i++) {
                String feature = NameOffeature.get(i);
                double value = -1.0;
                while (value < getFeatureMin(feature) || value > getFeatureMax(feature)) {
                    System.out.print(feature + " (" + getFeatureMin(feature) + " - " + getFeatureMax(feature) + "): ");
                    try {
                        value = Double.parseDouble(in.nextLine());
                        if (value < getFeatureMin(feature) || value > getFeatureMax(feature)) {
                            System.out.println("Value out of range. Please enter a value between " + getFeatureMin(feature) + " and " + getFeatureMax(feature) + ".");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                }
                inputFeatures[i] = value;
            }

            System.out.println("\nSelect Distance Metric:");
            List<String> metrics = Arrays.asList(
                    "1. Absolute Distance",
                    "2. Cosine Distance",
                    "3. Manhattan Distance",
                    "4. Hamming Distance"
            );
            for (String metric : metrics) {
                System.out.println(metric);
            }
            int metricChoice = -1;
            while (metricChoice < 1 || metricChoice > metrics.size()) {
                System.out.print("Enter your choice (1-4): ");
                try {
                    metricChoice = Integer.parseInt(in.nextLine());
                    if (metricChoice < 1 || metricChoice > metrics.size()) {
                        System.out.println("Invalid selection. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            String selectedMetric = "";
            switch (metricChoice) {
                case 1:
                    selectedMetric = "Absolute Distance";
                    break;
                case 2:
                    selectedMetric = "Cosine Distance";
                    break;
                case 3:
                    selectedMetric = "Manhattan Distance";
                    break;
                case 4:
                    selectedMetric = "Hamming Distance";
                    break;
            }
            System.out.println("Selected Distance Metric: " + selectedMetric);

            // Enter threshold
            double threshold = -1.0;
            while (threshold <= 0) {
                System.out.print("\nEnter the threshold value (must be greater than 0): ");
                try {
                    threshold = Double.parseDouble(in.nextLine());
                    if (threshold <= 0) {
                        System.out.println("Threshold must be greater than 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }

            AuthenticationResult authResult = authenticateUser(inputFeatures, selectedUser, threshold, selectedMetric);
            if (authResult.isAuthenticated()) {
                System.out.println("\nAuthentication Result: User accepted. Matched with: " + authResult.getMatchedUserID());
            } else {
                System.out.println("\nAuthentication Result: User rejected.");
            }

            double fmr = calculateFMR(users, inputFeatures, selectedMetric, threshold, selectedUser.userID);
            double fnmr = calculateFNMR(users, inputFeatures, selectedMetric, threshold, selectedUser);

            System.out.printf("False Match Rate (FMR): %.2f%%%n", fmr * 100);
            System.out.printf("False Non-Match Rate (FNMR): %.2f%%%n", fnmr * 100);

            double eer = calculateEER(users, inputFeatures, selectedMetric);
            System.out.printf("Equal Error Rate (EER): %.2f%%%n", eer * 100);

            boolean validResponse = false;
            while (!validResponse) {
                System.out.print("\nDo you want to perform another authentication? (Yes/No): ");
                String response = in.nextLine().trim().toLowerCase();
                if (response.equals("yes") || response.equals("y")) {
                    validResponse = true;
                } else if (response.equals("no") || response.equals("n")) {
                    validResponse = true;
                    continueAuthentication = false;
                    System.out.println("Exiting the application. Thank you for using!");
                } else {
                    System.out.println("Invalid response. Please enter 'Yes' or 'No'.");
                }
            }
        }

        in.close();
    }

    private void initializeUsers() {

        users.add(new User("user1", new double[]{25, 11, 50, 25, 45, 10, 20, 80, 114, 25}));
        users.add(new User("user2", new double[]{27, 10, 52, 22, 48, 11, 18, 75, 115, 23}));
        users.add(new User("user3", new double[]{31, 15, 54, 28, 42, 9, 19, 78, 110, 24}));
        users.add(new User("user4", new double[]{27, 14, 49, 24, 46, 10, 21, 82, 101, 26}));
        users.add(new User("user5", new double[]{33, 15, 53, 27, 44, 12, 17, 76, 114, 22}));
    }

    // Display selected user's features in an organized manner
//    private void displayFeatures(User user) {
//        System.out.println("\nFeatures of User " + user.userID + ":");
//        for (int i = 0; i < NameOffeature.size(); i++) {
//            System.out.println(NameOffeature.get(i) + ": " + user.features[i]);
//        }
//    }


    private AuthenticationResult authenticateUser(double[] inputFeatures, User selectedUser, double threshold, String distanceMetric) {
        double distance = calculateDistance(inputFeatures, selectedUser.features, distanceMetric, selectedUser.userID, true);
        if (distance < threshold) {
            return new AuthenticationResult(true, selectedUser.userID);
        }
        return new AuthenticationResult(false, null);
    }



}
