package NodeSystem;

import java.util.HashSet;

/**
 * Created by asebe on 9/27/2016.
 */
public interface INode {

    void setF(double f);
    double getF();
    void setG(double g);
    double getG();
    void setObject(IDistance object);
    IDistance getObject();
    void setParent(INode node);
    INode getParent();
    HashSet<INode> findNeighboringNodes();
    double distanceFrom(INode node);

}
