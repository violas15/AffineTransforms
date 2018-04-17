package pointConverter.TeamD.API;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.HashMap;

public  class PointConverter {
    private static HashMap<String,AffineTransform> transforms = new HashMap<>();


    /**
     * Returns a double[] containing the 3d points generated from the 2dxy coordinates
     * @param x
     * @param y
     * @param floor
     * @return*/
    public static double[] convertTo3D(double x, double y,String floor){
        if(transforms.isEmpty()){
            createTransforms();
        }
        if(!transforms.containsKey(floor)){
            System.err.println("No transform found for floor: " + floor);
            Exception e = new FloorTransformNotFoundException(floor);
            e.printStackTrace();
            return new double[0];
        }
        double[] dest = new double[2];
        double[] src = new double[2];
        src[0] = x; src[1] = y;
        transforms.get(floor).transform(src, 0, dest, 0, 1);
        return dest;
    }

    /**
     * Returns a double[] containing the 2d points generated from the 3dxy coordinates
     * @param x
     * @param y
     * @param floor
     * @return
     */
    public static double[] convertTo2D(double x, double y, String floor){
        if(transforms.isEmpty()){
            createTransforms();
        }
        if(!transforms.containsKey(floor)){
            System.err.println("No transform found for floor: " + floor);
            Exception e = new FloorTransformNotFoundException(floor);
            e.printStackTrace();
            return new double[0];
        }
        double[] dest = new double[2];
        double[] src = new double[2];
        src[0] = x; src[1] = y;
        try {
            transforms.get(floor).inverseTransform(src, 0, dest, 0, 1);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * This function clears the current transformations from the hashmap
     */
    public static void clearTransforms(){
        transforms.clear();
    }

    /**
     * This function is for adding your own transformations to the mapping.
     * @param floorID This is the string that identifies what floor the node is on
     * @param twoDx1 2d X location of point 1
     * @param twoDy1 2d Y location of point 1
     * @param twoDx2 2d X location of point 2
     * @param twoDy2 2d Y location of point 2
     * @param twoDx3 2d X location of point 3
     * @param twoDy3 2d Y location of point 3
     *
     * @param threeDx1 3d X location of point 1
     * @param threeDy1 3d Y location of point 1
     * @param threeDx2 3d X location of point 2
     * @param threeDy2 3d Y location of point 2
     * @param threeDx3 3d X location of point 3
     * @param threeDy3 3d Y location of point 3
     */

    public static void addTransform(String floorID,
                                    double twoDx1, double twoDy1,
                                    double twoDx2, double twoDy2,
                                    double twoDx3, double twoDy3,
                                    double threeDx1, double threeDy1,
                                    double threeDx2, double threeDy2,
                                    double threeDx3, double threeDy3){
        transforms.put(floorID, deriveAffineTransform(twoDx1, twoDy1,twoDx2,twoDy2,twoDx3,twoDy3,threeDx1,threeDy1,threeDx2,threeDy2,threeDx3,threeDy3));
    }

    private static void createTransforms(){

        transforms.put("3",deriveAffineTransform(1705,3064,1100,1496,4735,1160,1016,2310, 1104, 1351, 3872, 2174));
        transforms.put("2", deriveAffineTransform(981,2514,1761,1498,4833,846,730,1920,1534,1558,4039,2052));
        transforms.put("1", deriveAffineTransform(990,2843,1486,1241,3386,535,593,2123,1459,1396,3092,1517));
        transforms.put("L1", deriveAffineTransform(1846, 3115, 1796, 1537, 4733, 1159, 1006, 2475, 1572, 1651, 3870, 2199));
        transforms.put("L2", deriveAffineTransform(1846, 3115, 1796, 1537, 4733, 1159, 1006, 2511, 1588, 1687, 3870, 2225));
    }


    private static AffineTransform deriveAffineTransform(
            double oldX1, double oldY1,
            double oldX2, double oldY2,
            double oldX3, double oldY3,
            double newX1, double newY1,
            double newX2, double newY2,
            double newX3, double newY3) {

        double[][] oldData = { {oldX1, oldX2, oldX3}, {oldY1, oldY2, oldY3}, {1, 1, 1} };
        RealMatrix oldMatrix = MatrixUtils.createRealMatrix(oldData);

        double[][] newData = { {newX1, newX2, newX3}, {newY1, newY2, newY3} };
        RealMatrix newMatrix = MatrixUtils.createRealMatrix(newData);

        RealMatrix inverseOld = new LUDecomposition(oldMatrix).getSolver().getInverse();
        RealMatrix transformationMatrix = newMatrix.multiply(inverseOld);

        double m00 = transformationMatrix.getEntry(0, 0);
        double m01 = transformationMatrix.getEntry(0, 1);
        double m02 = transformationMatrix.getEntry(0, 2);
        double m10 = transformationMatrix.getEntry(1, 0);
        double m11 = transformationMatrix.getEntry(1, 1);
        double m12 = transformationMatrix.getEntry(1, 2);

        return new AffineTransform(m00, m10, m01, m11, m02, m12);
    }

    private static class FloorTransformNotFoundException extends Exception {
        String floor;
        public FloorTransformNotFoundException(String floor) {
            super();
            this.floor = floor;
        }

        @Override
        public String getMessage() {
            return super.getMessage() + "\n Floor "+ floor+ " not found in the transforms. Have you called addTransform for your floorID?";
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
            System.err.println(getMessage());
        }
    }
}
