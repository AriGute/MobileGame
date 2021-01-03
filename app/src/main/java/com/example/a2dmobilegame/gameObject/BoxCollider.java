package com.example.a2dmobilegame.gameObject;

public class BoxCollider {

    private  Point point;
    private  float width;
    private  float hight;

    /**
     * Box collide should represend the borders of specific bitmap/gameObject.
     * @param point bitmap/gameObject position(x,y) as Point object.
     * @param width bitmap/gameObject width as float.
     * @param hight bitmap/gameObject hight as float.
     */
    public BoxCollider(Point point, float width, float hight){
        this.point = point;
        this.width = width;
        this.hight = hight;
    }

    /**
     *
     * @return X position.
     */
    public float getX(){
        return point.getX();
    }

    /**
     *
     * @return Y position.
     */
    public float getY(){
        return point.getY();
    }

    /**
     *
     * @return Box width.
     */
    public  float getWidth(){
        return width;
    }

    /**
     *
     * @return Box hight.
     */
    public  float getHight(){
        return hight;
    }

    /**
     * Check if this box is over another(Collide).
     * @param other other boxCollider.
     * @return true if the one box is on another.
     */
    public  boolean isColliding(BoxCollider other){
        if(point.getX() < other.getX()){
            if((point.getX()+width) > other.getX()){
                if(point.getY() < other.getY()){
                    if(point.getY()+hight > other.getY()){
                        return true;
                    }
                }else if(other.getY() < point.getY()){
                    if(other.getY()+other.getHight() > point.getY()){
                        return true;
                    }
                }
            }
        }else if(other.getX() < point.getX()){
            if(other.getX()+other.getWidth() > point.getX()){
                if(point.getY() < other.getY()){
                    if(point.getY()+hight > other.getY()){
                        return true;
                    }
                }else if(other.getY() < point.getY()){
                    if(other.getY()+other.getHight() > point.getY()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
