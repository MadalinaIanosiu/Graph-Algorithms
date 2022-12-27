import java.util.Comparator;

class ArcComparator implements Comparator<SmallArc> {

    public int compare(SmallArc arc1, SmallArc arc2) {
        if (arc1.valoare > arc2.valoare)
            return 1;
        else if (arc1.valoare < arc2.valoare)
            return -1;
        return 0;
    }
}
public class SmallArc {
    public Integer firstNodeNr; //index nod initial
    public Integer secondNodeNr; //index nod final
    public Integer valoare; //lungimea arcului
    SmallArc(){

    }

    public SmallArc(Integer firstNodeNr, Integer secondNodeNr, Integer valoare) {
        this.firstNodeNr = firstNodeNr;
        this.secondNodeNr = secondNodeNr;
        this.valoare = valoare;
    }
}
