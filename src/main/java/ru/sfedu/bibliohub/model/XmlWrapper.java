package ru.sfedu.bibliohub.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import ru.sfedu.bibliohub.model.bean.*;

import java.io.Serializable;
import java.util.List;

@Root(name = "List")
public class XmlWrapper<T> implements Serializable {

    @ElementListUnion({
            @ElementList(inline = true, required = false, type = Book.class),
            @ElementList(inline = true, required = false, type = LibraryCard.class),
            @ElementList(inline = true, required = false, type = PerpetualCard.class),
            @ElementList(inline = true, required = false, type = Rent.class),
            @ElementList(inline = true, required = false, type = TemporaryCard.class),
    })
    private List<T> list;

    public XmlWrapper() {
    }

    public XmlWrapper(List<T> list) {
        setList(list);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
