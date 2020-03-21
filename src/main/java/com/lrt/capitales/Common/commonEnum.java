package com.lrt.capitales.Common;

public class commonEnum {
    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */
        public static Direction fromAngle(double angle){
            if(inRange(angle, 45, 135)){
                return Direction.UP;
            }
            else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
                return Direction.RIGHT;
            }
            else if(inRange(angle, 225, 315)){
                return Direction.DOWN;
            }
            else{
                return Direction.LEFT;
            }

        }

        /**
         * @param angle an angle
         * @param init the initial bound
         * @param end the final bound
         * @return returns true if the given angle is in the interval [init, end).
         */
        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }
    }
}
