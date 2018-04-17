# AffineTransforms
## Installation
#### IntelliJ Installation:
[pointConverter-1.0.jar](https://github.com/violas15/AffineTransforms/raw/master/pointConverter-1.0.jar)

Install the pointConverter-1.0.jar using IntelliJ by going to File->Project Structure->Modules->Dependencies and hit the
green plus to add Jar Dependencies. Then navigate to the pointConverter-1.0.jar.
Import using
```java
    import pointConverter.TeamD.API.PointConverter; 
```
    
#### Gradle Installation
To install this dependency using gradle, create a folder in the top level of your project file named libs. Inside this 
folder copy and paste the pointConverter-1.0.jar file.
The in your build.gradle add the following line in the dependencies section.

     ```java compile fileTree(dir: 'libs', include: '*.jar')```
     
This will include all the jar files in the libs folder into your gradle project. We recommend adding the libs folder to
your github repository so all users will have this API automatically installed. 

## Useage

#### Using Default Points:
```java
    import pointConverter.TeamD.API.PointConverter;

    double[] coords3D = PointConverter.convertTo3D(xCoord2D, yCoord2D, floorString)
    double[] coords2D = PointConverter.convertTo2D(xCoord3D, yCoord3D, floorString)
 ```

This function returns the corresponding 3D coordinates for the 2d coordinates passed into the function.
Valid floor strings for the default setup are 
* "3"
* "2"
* "1"
* "L1"
* "L2"


#### Using Custom Points:
If you are not satisfied with the default coordinates or if you want to change to custom floor strings you
 can add your own transformation.
    ```java
     PointConverter.clearTransforms(); // this clears all transforms in the list
     PointConverter.addTransform( String floorID,
                                  double twoDx1, double twoDy1,
                                  double twoDx2, double twoDy2,
                                  double twoDx3, double twoDy3,
                                  double threeDx1, double threeDy1,
                                  double threeDx2, double threeDy2,
                                  double threeDx3, double threeDy3);
     ```
                                
 This function allows you to add your own transformations with a certain floorID.
 This function accepts a seriers of 6 points, that correspond to the points you want to transform between.
  The point (twoDx1, twDy1) should be in the same relative location as the (threeDx1, threeDy1) etc...
                                     
 
