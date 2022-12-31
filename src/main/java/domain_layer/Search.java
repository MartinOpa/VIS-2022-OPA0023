package domain_layer;

import java.util.List;

public interface Search<T> {
    public List<T> filter(List<T> list, String parameter);
    //nějak mi bohužel nešlo implementovat interface s překrytím metod a nestihl jsem přijít na to proč
}
