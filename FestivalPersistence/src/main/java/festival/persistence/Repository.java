package festival.persistence;

public interface Repository<ID, T> {
    void add(T elem);

    void update(ID id, T elem);

    void delete(ID id) throws Exception;

    Iterable<T> findAll();
}
