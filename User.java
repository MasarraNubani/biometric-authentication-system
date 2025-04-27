package com.example.authenticationproject;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;






public class User {
    String userID;
    double[] features;

    public User(String userID, double[] features) {
        this.userID = userID;
        this.features = features;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(userID);
        for (int i = 0; i < features.length; i++) {
            sb.append(",").append(features[i]);
        }
        return sb.toString();
    }


    public static List<User> loadUsersFromCSV(String filename) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    System.out.println("line wrong in file: " + line);
                    continue;
                }
                String userID = parts[0];
                double[] features = new double[parts.length - 1];
                boolean valid = true;
                for (int i = 1; i < parts.length; i++) {
                    try {
                        features[i - 1] = Double.parseDouble(parts[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("Error while changing value to number: " + parts[i] + "in line: " + line);
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    users.add(new User(userID, features));
                    System.out.println("User loaded " + userID);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("users.csv not found");
        } catch (IOException e) {
            System.out.println("error while reading users.csv");
            e.printStackTrace();
        }
        return users;
    }

//    public static void saveUsersToCSV(List<User> users, String filename) {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            for (User user : users) {
//                bw.write(user.toString());
//                bw.newLine();
//            }
//            System.out.println("users saved in file " + filename);
//        } catch (IOException e) {
//            System.out.println(" error while saving users to file");
//            e.printStackTrace();
//        }
//    }
}
