# biometric-authentication-system
This project implements a biometric authentication system using Java, where users authenticate themselves by providing values for predefined features. The system compares the provided values with stored user templates using a distance metric and decides whether to authenticate the user based on a threshold.

## Features:
- Authentication system for five users with 10 predefined biometric features.
- Users can authenticate either through a **console-based interface** or a **graphical user interface (GUI)**.
- Distance metric options: **Absolute Distance**, and other metrics can be implemented.
- Supports threshold-based authentication with configurable threshold values.
- Calculates **False Match Rate (FMR)** and **False Non-Match Rate (FNMR)**.
  
## System Design:
- **Template Creation**: Each user has a template consisting of 10 features. The initial feature values are constant and stored in the system.
- **Authentication**: When a user tries to authenticate, they provide their feature values, and the system compares these to their template using distance metrics.
- **Thresholds**: The system uses a threshold to decide if the authentication is successful or failed. Different thresholds can be experimented with, and the system calculates **FMR** and **FNMR**.

## How to Run:
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/biometric-authentication-system-java.git
    cd biometric-authentication-system-java
    ```

2. Compile the Java code:
    ```bash
    javac Main.java
    ```

3. Run the program:
    ```bash
    java Main
    ```

## Input:
The program will ask for values for 10 biometric features (such as Eye Width, Nose Length, etc.) for the user trying to authenticate.

### Example of Features:
1. Eye Width (Range: 25-35 units)
2. Eye Height (Range: 10-15 units)
3. Nose Length (Range: 40-55 units)
4. Nose Width (Range: 20-30 units)
5. Mouth Width (Range: 40-50 units)
6. Mouth Height (Range: 8-12 units)
7. Chin Length (Range: 15-25 units)
8. Face Width (Range: 70-90 units)
9. Face Height (Range: 100-120 units)
10. Ear Size (Range: 20-30 units)

## Output:
The system will output whether the user is authenticated or rejected based on their feature values and the threshold. It will also show the **FMR** and **FNMR** calculations.

## Experimenting with Thresholds:
You can modify the threshold value in the code to see how it affects the **FMR** and **FNMR**.

## Example:
To test with a sample threshold value, change the following in the `Main.java`:

```java
double threshold = 50.0; // Change this value to experiment with different thresholds
